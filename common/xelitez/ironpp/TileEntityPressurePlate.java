/**
 * this is the class for the TileEntity of the advanced
 * iron Pressure Plate block.
 * 
 * @author Kalvin
 */
package xelitez.ironpp;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class TileEntityPressurePlate extends TileEntity
{
	
	/**
	 * registeres the lists of mobs and players.
	 */
	public PPList[] allowedMobs;
	public List mobs  = new ArrayList();
	public List allowedPlayers = new ArrayList();
	
	public TileEntityPressurePlate()
    {
    	registerMobs();
    }
    
	/**
	 * a method to register all mobs that are registered in the entity list.
	 * most of the credit for this method goes to Risugami.
	 */
    public void registerMobs()
    {
        if (mobs.size() == 0)
        {
            Field field = (net.minecraft.src.EntityList.class).getDeclaredFields()[1];
            field.setAccessible(true);
            Map map = null;

            try
            {
                map = (Map)field.get(null);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

            if (map != null)
            {
                for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();)
                {
                    Class class1 = (Class)iterator.next();

                    try
                    {
                        if ((net.minecraft.src.EntityLiving.class).isAssignableFrom(class1) && class1.getConstructor(new Class[]
                                {
                                    net.minecraft.src.World.class
                                }) != null && !Modifier.isAbstract(class1.getModifiers()))
                        {
                            String s1 = (String)map.get(class1);
                            mobs.add(s1);
                        }
                    }
                    catch (SecurityException securityexception)
                    {
                        securityexception.printStackTrace();
                    }
                    catch (NoSuchMethodException nosuchmethodexception) { }
                }
            }

            Collections.sort(mobs);
            mobs.add(0, "humanoid");
            mobs.add(1, "Item");
        }
        if(allowedMobs == null)
        {
        	allowedMobs = new PPList[mobs.size()];
        }
        boolean[] bl;
        bl = new boolean[mobs.size()];
    	for(int var1 = 0;var1 < mobs.size();var1++)
    	{
    		if(allowedMobs[var1] == null)
    		{
    			allowedMobs[var1] = new PPList((String)mobs.get(var1));
    		}
    		else
    		{
        		bl[var1] = allowedMobs[var1].getEnabled();
    			allowedMobs[var1] = new PPList((String)mobs.get(var1), bl[var1]);
    		}
    	}
    }
    
    /**
     * method used to register the player that has
     * placed the pressure plate.
     * @param username
     */
    public void registerPlayer(String username)
    {
        if(!isInPlayerList(username))
        {
        	addPlayer(username);
        }
    }
    
    /**
     * method to add a playername to the pressure plate
     * @param player	name of the player
     * @return	true or false 	(telling if it succeeded to add or failed)
     */
    public boolean addPlayer(String player)
    {
    	for(int i = 0;i < allowedPlayers.size();i++)
    	{
    		if(((PPPlayerList)allowedPlayers.get(i)).getUsername().matches(player))
    		{
    			return false;
    		}
    	}
    	allowedPlayers.add(new PPPlayerList(player));
    	return true;
    }
    
    /**
     * method to remove the playername in the same way as described above.
     * @param player
     * @return
     */
    public boolean removePlayer(String player)
    {
    	for(int i = 0;i < allowedPlayers.size();i++)
    	{
    		if(((PPPlayerList)allowedPlayers.get(i)).getUsername().matches(player))
    		{
    			allowedPlayers.remove(i);
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * method to switch a mob to set if the mob can trigger
     * the pressure plate. (Note that this does not directly
     * changes the gui if on a remote world)
     * @param var1		the registered name of the mob
     * @param world		the game world(used to check if it's a server world or not)
     */
    public void switchMob(String var1, World world)
    {	
    	for(int var4 = 0;var4 < allowedMobs.length;var4++)
    	{
    		PPList pp = allowedMobs[var4];
    		if(world.isRemote)
    		{
	    		if(pp.getMobname().matches(var1) && !pp.getEnabled())
	    		{
	    			allowedMobs[var4].enable();
	    			return;
	    		}
	    		if(pp.getMobname().matches(var1) && pp.getEnabled())
	    		{
	    			allowedMobs[var4].disable();
	    			return;
	    		}
    		}
    		else
    		{
    			if(pp.getMobname().matches(var1))
    			{
    				if(!pp.getEnabled())
    				{
    					allowedMobs[var4].enable();
    				}
    				else
    				{
    					allowedMobs[var4].disable();
    				}
    				PacketSendManager.sendSwitchMobButtonPacketToClient(this, var4);	
    				return;
    			}
    		}
    	}
    	return;
    }
    
    /**
     * almost the same method as the one above this one
     * but to switch players.
     * @param var1
     * @param world
     */
    public void switchPlayer(String var1, World world)
    {	
    	for(int var4 = 0;var4 < allowedPlayers.size();var4++)
    	{
    		PPPlayerList pp = (PPPlayerList)allowedPlayers.get(var4);
    		if(pp.getUsername().matches(var1))
    		{
	    		if(!pp.getEnabled())
	    		{
	    			pp.enable();
	    		}
	    		else
	    		{
	    			pp.disable();
	    		}
    			PacketSendManager.sendPressurePlatePlayerDataToClient(this);
    			return;
    		}
    	}
    	return;
    }
    
    /**
     * method to check if a mob is in the list and then
     * if it should be activating the pressure plate or not.
     * @param var1	mob name
     * @return true if it activates
     */
    public boolean findMobName(String var1)
    {
    	for(int var2 = 0;var2 < allowedMobs.length;var2++)
    	{
    		PPList pp = allowedMobs[var2];
    		if(var1 != null)
    		{
    			if(pp.getMobname().matches(var1) && pp.getEnabled())
    			{
    				return true;
    			}	
    		}
    	}
        return false;
    }
    
    /**
     * same as the method above but for players
     * @param username
     * @return
     */
    public boolean isInPlayerList(String username)
    {
    	for(int var1 = 0; var1 < allowedPlayers.size(); var1++)
    	{
    		PPPlayerList pp = (PPPlayerList)allowedPlayers.get(var1);
    		if(username != null && pp != null)
    		{
    			if(pp.getUsername().matches(username) && pp.getEnabled())
    			{
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * a method to directly enable or disable a player.
     * @param username
     * @param enabled
     */
    public void setEnabledForPlayer(String username, boolean enabled)
    {
    	for(int var1 = 0; var1 < allowedPlayers.size(); var1++)
    	{
    		PPPlayerList pp = (PPPlayerList)allowedPlayers.get(var1);
			if(pp.getUsername().matches(username))
			{
				pp.isEnabled = enabled;
			}
    	}
    }
    
    /**
     * loads the pressure plate data.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Mobs");
        this.registerMobs();
    	for(int var3 = 0; var3 < mobs.size();var3++)
    	{
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            int var5 = var4.getByte("mob");
            if (var5 >= 0 && var5 < this.allowedMobs.length)
            {
                this.allowedMobs[var5] = PPList.loadSettingsFromNBT(var4, (String)mobs.get(var3));
            }
    	}
        NBTTagList var6 = par1NBTTagCompound.getTagList("Players");
    	for(int var7 = 0; var7 < var6.tagCount();var7++)
    	{
            NBTTagCompound var8 = (NBTTagCompound)var6.tagAt(var7);
            int var9 = var8.getByte("player");
            String var10 = var8.getString("username");
            boolean var11 = var8.getBoolean("isEnabled");
            if (var9 >= 0 && var9 < this.allowedMobs.length)
            {
                this.allowedPlayers.add(new PPPlayerList(var10, var11));
            }
    	}
    }

    /**
     * saves the pressure plate data.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < allowedMobs.length; ++var3)
        {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("mob", (byte)var3);
            this.allowedMobs[var3].writeToNBT(var4);
            var2.appendTag(var4);  
        }
        par1NBTTagCompound.setTag("Mobs", var2);
        NBTTagList var6 = new NBTTagList();
        if(allowedPlayers.size() > 0 && allowedPlayers != null)
        {
	        for (int var5 = 0; var5 < allowedPlayers.size(); var5++)
	        {
	            NBTTagCompound var7 = new NBTTagCompound();
	            var7.setByte("player", (byte)var5);
	            var7.setString("username", ((PPPlayerList)allowedPlayers.get(var5)).getUsername());
	            var7.setBoolean("isEnabled", ((PPPlayerList)allowedPlayers.get(var5)).getEnabled());
	            var6.appendTag(var7); 
	        }
	        par1NBTTagCompound.setTag("Players", var6);
        }
    }
    
    

}
