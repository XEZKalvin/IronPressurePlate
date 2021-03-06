/**
 * class to handle incomming packets
 *
 * @author Kalvin
 */
package xelitez.ironpp;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import xelitez.ironpp.client.GuiAPressurePlate;
import xelitez.ironpp.client.GuiModifyPressurePlate;
import xelitez.ironpp.client.GuiPassword;
import xelitez.ironpp.netty.Packet;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.client.FMLClientHandler;

public class PacketHandler
{
	public static final PacketHandler INSTANCE = new PacketHandler();
	
    /**
     * this is a method for handling packets that were
     * sent to the client.
     * @param manager
     * @param packet
     * @param player
     * @param dat		the data that it can read from
     * @param ID		the packet ID
     */
    public void handleClientPacket(Packet packet, EntityPlayer player, ByteArrayDataInput dat, short ID)
    {
        EntityPlayer thePlayer = (EntityPlayer)player;
        World world = thePlayer.worldObj;

        if (world != null)
        {
            if (FMLClientHandler.instance().getClient().currentScreen instanceof GuiAPressurePlate)
            {
                if (ID == 1)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        ((EntityPlayerSP)thePlayer).closeScreen();
                    }

                    return;
                }

                if (ID == 2)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    int allowedmobs = dat.readInt();

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        for (int var1 = 0; var1 < allowedmobs; var1++)
                        {
                            boolean bool = dat.readBoolean();
                            GuiAPressurePlate.enabled[var1] = bool;
                        }
                    }

                    return;
                }

                if (ID == 3)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        GuiAPressurePlate.tpp.allowedPlayers.clear();
                        int allowedPlayers = dat.readInt();
                        {
                            for (int var1 = 0; var1 < allowedPlayers; var1++)
                            {
                                short nameLength = dat.readShort();
                                String username = "";

                                for (int var2 = 0; var2 < nameLength; var2++)
                                {
                                    username = new StringBuilder().append(username).append(dat.readChar()).toString();
                                }

                                boolean bool = dat.readBoolean();
                                GuiAPressurePlate.tpp.addPlayer(username);
                                GuiAPressurePlate.tpp.setEnabledForPlayer(username, bool);
                            }

                            GuiAPressurePlate.lineUp();
                        }
                    }

                    return;
                }

                if (ID == 4)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    int index = dat.readInt();

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        GuiAPressurePlate.switchbutton(index);
                    }

                    return;
                }

                if (ID == 11)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        if (GuiAPressurePlate.tpp.settings == null)
                        {
                            GuiAPressurePlate.tpp.registerSettings();
                        }

                        int var2 = dat.readInt();

                        for (int var3 = 0; var3 < var2; var3++)
                        {
                            if (var3 <= GuiAPressurePlate.tpp.settings.size())
                            {
                                GuiAPressurePlate.tpp.setSetting(var3, dat.readBoolean());
                            }
                        }

                        GuiAPressurePlate.LineUpSettings();
                    }

                    return;
                }

                if (ID == 10)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        int var2 = dat.readInt();

                        if (GuiAPressurePlate.tpp.settings.size() >= var2)
                        {
                            GuiAPressurePlate.tpp.switchSetting(var2);
                        }
                    }

                    return;
                }
            }

            if (FMLClientHandler.instance().getClient().currentScreen instanceof GuiModifyPressurePlate)
            {
                if (ID == 1)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        ((EntityPlayerSP)thePlayer).closeScreen();
                    }
                }

                if (ID == 3)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        GuiAPressurePlate.tpp.allowedPlayers.clear();
                        int allowedPlayers = dat.readInt();
                        {
                            for (int var1 = 0; var1 < allowedPlayers; var1++)
                            {
                                short nameLength = dat.readShort();
                                String username = "";

                                for (int var2 = 0; var2 < nameLength; var2++)
                                {
                                    username = new StringBuilder().append(username).append(dat.readChar()).toString();
                                }

                                boolean bool = dat.readBoolean();
                                GuiAPressurePlate.tpp.setEnabledForPlayer(username, bool);
                            }

                            GuiAPressurePlate.lineUp();
                        }
                    }

                    return;
                }

                if (ID == 4)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    int index = dat.readInt();

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        GuiAPressurePlate.switchbutton(index);
                    }

                    return;
                }

                if (ID == 5)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        boolean bool = dat.readBoolean();
                        int usernamelength = dat.readInt();
                        String username = "";

                        for (int var2 = 0; var2 < usernamelength; var2++)
                        {
                            char c = dat.readChar();
                            username = new StringBuilder().append(username).append(c).toString();
                        }

                        if (thePlayer.getCommandSenderName().matches(username))
                        {
                            if (bool)
                            {
                                GuiModifyPressurePlate.showText("Player added", 20);
                            }
                            else
                            {
                                GuiModifyPressurePlate.showText("Player is already in list", 20);
                            }
                        }
                    }

                    return;
                }

                if (ID == 6)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        boolean bool = dat.readBoolean();
                        int usernamelength = dat.readInt();
                        String username = "";

                        for (int var2 = 0; var2 < usernamelength; var2++)
                        {
                            char c = dat.readChar();
                            username = new StringBuilder().append(username).append(c).toString();
                        }

                        if (thePlayer.getCommandSenderName().matches(username))
                        {
                            if (bool)
                            {
                                GuiModifyPressurePlate.showText("Player removed", 20);
                            }
                            else
                            {
                                GuiModifyPressurePlate.showText("Player is not in list", 20);
                            }
                        }
                    }

                    return;
                }

                if (ID == 10)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        int index = dat.readInt();

                        if (GuiAPressurePlate.tpp.settings.size() >= index)
                        {
                            GuiAPressurePlate.tpp.switchSetting(index);
                        }
                    }

                    return;
                }

                if (ID == 11)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    if (GuiAPressurePlate.tpp.xCoord == coords[0] && GuiAPressurePlate.tpp.yCoord == coords[1] && GuiAPressurePlate.tpp.zCoord == coords[2])
                    {
                        if (GuiAPressurePlate.tpp.settings == null)
                        {
                            GuiAPressurePlate.tpp.registerSettings();
                        }

                        int var2 = dat.readInt();

                        for (int var3 = 0; var3 < var2; var3++)
                        {
                            if (var3 <= GuiAPressurePlate.tpp.settings.size())
                            {
                                GuiAPressurePlate.tpp.setSetting(var3, dat.readBoolean());
                            }
                        }

                        GuiAPressurePlate.LineUpSettings();
                    }

                    return;
                }
            }

            if (FMLClientHandler.instance().getClient().currentScreen instanceof GuiPassword)
            {
                if (ID == 12)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    int length = dat.readInt();
                    StringBuilder sb = new StringBuilder();

                    for (int var1 = 0; var1 < length; var1++)
                    {
                        sb.append(dat.readChar());
                    }

                    String name = sb.toString();

                    if (((GuiPassword)FMLClientHandler.instance().getClient().currentScreen).tpp != null && ((GuiPassword)FMLClientHandler.instance().getClient().currentScreen).tpp.xCoord == coords[0] && ((GuiPassword)FMLClientHandler.instance().getClient().currentScreen).tpp.yCoord == coords[1] && ((GuiPassword)FMLClientHandler.instance().getClient().currentScreen).tpp.zCoord == coords[2])
                    {
                        if (name.matches(thePlayer.getCommandSenderName()))
                        {
                            GuiPassword gui = (GuiPassword)FMLClientHandler.instance().getClient().currentScreen;
                            gui.enterGui();
                        }
                    }

                    return;
                }

                if (ID == 13)
                {
                    int coords[]  = new int[3];

                    for (int var1 = 0; var1 < 3; var1++)
                    {
                        coords[var1] = dat.readInt();
                    }

                    boolean b = dat.readBoolean();

                    if (((GuiPassword)FMLClientHandler.instance().getClient().currentScreen).tpp != null && ((GuiPassword)FMLClientHandler.instance().getClient().currentScreen).tpp.xCoord == coords[0] && ((GuiPassword)FMLClientHandler.instance().getClient().currentScreen).tpp.yCoord == coords[1] && ((GuiPassword)FMLClientHandler.instance().getClient().currentScreen).tpp.zCoord == coords[2])
                    {
                        GuiPassword gui = (GuiPassword)FMLClientHandler.instance().getClient().currentScreen;

                        if (b)
                        {
                            gui.enterGui();
                        }
                        else
                        {
                            GuiPassword.showText("Wrong Password!!!", 20);
                        }
                    }

                    return;
                }

                if (ID == 15)
                {
                    return;
                }

                if (ID == 18)
                {
                    int guiID = dat.readInt();

                    if (FMLClientHandler.instance().getClient().currentScreen != null && FMLClientHandler.instance().getClient().currentScreen instanceof GuiPassword)
                    {
                        GuiPassword gui = (GuiPassword)FMLClientHandler.instance().getClient().currentScreen;
                        gui.set = guiID;
                    }

                    return;
                }
            }

            if (ID == 7)
            {
                int coords[]  = new int[3];

                for (int var1 = 0; var1 < 3; var1++)
                {
                    coords[var1] = dat.readInt();
                }

                TileEntity te = world.getTileEntity(coords[0], coords[1], coords[2]);
                TileEntityPressurePlate tpp = null;

                if (te != null || te instanceof TileEntityPressurePlate)
                {
                    tpp = (TileEntityPressurePlate)te;
                }

                int itemId = dat.readInt();
                int stackSize = dat.readInt();
                int itemDamage = dat.readInt();
                int dimension = dat.readInt();

                if (!PPRegistry.getContainsPressurePlate(coords[0], coords[1], coords[2], dimension))
                {
                    PPRegistry.addPressurePlate(coords[0], coords[1], coords[2], dimension, false, null);
                }

                if (itemId == 0 && stackSize == 0 & itemDamage == 0)
                {
                    if (tpp != null)
                    {
                        tpp.item[0] = null;
                    }

                    PPRegistry.setItem(coords[0], coords[1], coords[2], dimension, null);
                    world.markBlockForUpdate(coords[0], coords[1], coords[2]);
                }
                else
                {
                    if (tpp != null)
                    {
                        tpp.item[0] = new ItemStack(Item.getItemById(itemId), stackSize, itemDamage);
                    }

                    PPRegistry.setItem(coords[0], coords[1], coords[2], dimension, new ItemStack(Item.getItemById(itemId), stackSize, itemDamage));
                    world.markBlockForUpdate(coords[0], coords[1], coords[2]);
                }

                return;
            }

            if (ID == 8)
            {
                int coords[]  = new int[3];

                for (int var1 = 0; var1 < 3; var1++)
                {
                    coords[var1] = dat.readInt();
                }

                TileEntity te = world.getTileEntity(coords[0], coords[1], coords[2]);
                TileEntityPressurePlate tpp = null;

                if (te == null || !(te instanceof TileEntityPressurePlate))
                {
                    world.setTileEntity(coords[0], coords[1], coords[2], new TileEntityPressurePlate());
                    tpp = (TileEntityPressurePlate)te;
                }
                else
                {
                    tpp = (TileEntityPressurePlate)te;
                }

                if (tpp != null)
                {
                    tpp.currentOutput = dat.readInt();
                }

                world.markBlockForUpdate(coords[0], coords[1], coords[2]);
                return;
            }

            if (ID == 9)
            {
                PPRegistry.sendToServer = true;
                return;
            }

            if (ID == 11)
            {
                int coords[]  = new int[3];

                for (int var1 = 0; var1 < 3; var1++)
                {
                    coords[var1] = dat.readInt();
                }

                TileEntity te = world.getTileEntity(coords[0], coords[1], coords[2]);
                TileEntityPressurePlate tpp = null;

                if (te != null || te instanceof TileEntityPressurePlate)
                {
                    tpp = (TileEntityPressurePlate)te;
                }

                if (tpp != null)
                {
                    if (tpp.xCoord == coords[0] && tpp.yCoord == coords[1] && tpp.zCoord == coords[2])
                    {
                        if (tpp.settings == null)
                        {
                            tpp.registerSettings();
                        }

                        int var2 = dat.readInt();

                        for (int var3 = 0; var3 < var2; var3++)
                        {
                            if (var3 <= tpp.settings.size())
                            {
                                tpp.setSetting(var3, dat.readBoolean());
                            }
                        }
                    }
                }

                return;
            }

            if (ID == 12)
            {
                int coords[]  = new int[3];

                for (int var1 = 0; var1 < 3; var1++)
                {
                    coords[var1] = dat.readInt();
                }

                int length = dat.readInt();
                StringBuilder sb = new StringBuilder();

                for (int var1 = 0; var1 < length; var1++)
                {
                    sb.append(dat.readChar());
                }

                sb.toString();
                return;
            }

            if (ID == 14)
            {
                int coords[]  = new int[3];

                for (int var1 = 0; var1 < 3; var1++)
                {
                    coords[var1] = dat.readInt();
                }

                TileEntity te = world.getTileEntity(coords[0], coords[1], coords[2]);
                if (te != null || te instanceof TileEntityPressurePlate)
                {
                }

                int dimension = dat.readInt();

                if (!PPRegistry.getContainsPressurePlate(coords[0], coords[1], coords[2], dimension))
                {
                    PPRegistry.addPressurePlate(coords[0], coords[1], coords[2], dimension, false, null);
                }

                PPRegistry.setUsesPassword(coords[0], coords[1], coords[2], dimension, dat.readBoolean());
                return;
            }

            if (ID == 16)
            {
                int coords[]  = new int[3];

                for (int var1 = 0; var1 < 3; var1++)
                {
                    coords[var1] = dat.readInt();
                }

                TileEntity te = world.getTileEntity(coords[0], coords[1], coords[2]);
                TileEntityPressurePlate tpp = null;

                if (te != null || te instanceof TileEntityPressurePlate)
                {
                    tpp = (TileEntityPressurePlate)te;
                }

                int dimension = dat.readInt();

                if (tpp != null)
                {
                    PPRegistry.removePressurePlate(tpp, dimension);
                }

                return;
            }

            if (ID == 17)
            {
                int coords[]  = new int[3];

                for (int var1 = 0; var1 < 3; var1++)
                {
                    coords[var1] = dat.readInt();
                }

                TileEntity te = world.getTileEntity(coords[0], coords[1], coords[2]);
                TileEntityPressurePlate tpp = null;

                if (te != null || te instanceof TileEntityPressurePlate)
                {
                    tpp = (TileEntityPressurePlate)te;
                }

                int dimension = dat.readInt();

                if (tpp != null)
                {
                    PPRegistry.addPressurePlate(tpp, dimension);
                }

                return;
            }
            if(ID == 19)
            {
                int coords[]  = new int[3];

                for (int var1 = 0; var1 < 3; var1++)
                {
                    coords[var1] = dat.readInt();
                }

                TileEntity te = world.getTileEntity(coords[0], coords[1], coords[2]);
                if(te instanceof TileEntityPressurePlate)
                {
                	if(!(FMLClientHandler.instance().getClient().currentScreen instanceof GuiAPressurePlate))
                	{
                		((TileEntityPressurePlate) te).maxOutput = dat.readInt();
                		((TileEntityPressurePlate) te).itemsForMax = dat.readInt();
                	}
                	else if(GuiAPressurePlate.tpp != te || GuiAPressurePlate.hasData == false)
                	{
                		((TileEntityPressurePlate) te).maxOutput = dat.readInt();
                		((TileEntityPressurePlate) te).itemsForMax = dat.readInt();
                		GuiAPressurePlate.hasData = true;
                	}
                }
            }
        }
    }

    /**
     * method to handle packets that were sent to the server.
     * mostly the same as above but with different handling.
     * @param manager
     * @param packet
     * @param player
     * @param dat
     * @param ID
     */
    public void handleServerPacket(Packet packet, EntityPlayer player, ByteArrayDataInput dat, short ID)
    {
        EntityPlayerMP thePlayer = (EntityPlayerMP)player;
        World world = thePlayer.worldObj;

        if (ID == 1)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            TileEntityPressurePlate tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
            short var3 = dat.readShort();
            StringBuilder var4 = new StringBuilder();

            for (int var5 = 0; var5 < var3; var5++)
            {
                var4.append(dat.readChar());
            }

            String var6 = var4.toString();
            tpp.switchMob(var6, world);
            return;
        }

        if (ID == 2)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            TileEntityPressurePlate tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
            short var3 = dat.readShort();
            StringBuilder var4 = new StringBuilder();

            for (int var5 = 0; var5 < var3; var5++)
            {
                var4.append(dat.readChar());
            }

            String var6 = var4.toString();
            tpp.switchPlayer(var6, world);
            return;
        }

        if (ID == 3)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            if (world.getTileEntity(coords[0], coords[1], coords[2]) instanceof TileEntityPressurePlate)
            {
                TileEntityPressurePlate tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
                int usernameLength = dat.readInt();
                String username = "";

                for (int var2 = 0; var2 < usernameLength; var2++)
                {
                    char c = dat.readChar();
                    username = new StringBuilder().append(username).append(c).toString();
                }

                int playerUsernameLength = dat.readInt();
                String playerUsername = "";

                for (int var2 = 0; var2 < playerUsernameLength; var2++)
                {
                    char c = dat.readChar();
                    playerUsername = new StringBuilder().append(playerUsername).append(c).toString();
                }

                boolean bool = tpp.addPlayer(username);
                PacketSendManager.sendAddBooleanToClient(bool, coords[0], coords[1], coords[2], playerUsername);
                PacketSendManager.sendPressurePlatePlayerDataToClient(tpp);
            }

            return;
        }

        if (ID == 4)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            if (world.getTileEntity(coords[0], coords[1], coords[2]) instanceof TileEntityPressurePlate)
            {
                TileEntityPressurePlate tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
                int usernameLength = dat.readInt();
                String username = "";

                for (int var2 = 0; var2 < usernameLength; var2++)
                {
                    char c = dat.readChar();
                    username = new StringBuilder().append(username).append(c).toString();
                }

                String playerUsername = "";
                int playerUsernameLength = dat.readInt();

                for (int var3 = 0; var3 < playerUsernameLength; var3++)
                {
                    char c = dat.readChar();
                    playerUsername = new StringBuilder().append(playerUsername).append(c).toString();
                }

                boolean bool = tpp.removePlayer(username);
                PacketSendManager.sendRemoveBooleanToClient(bool, coords[0], coords[1], coords[2], playerUsername);
                PacketSendManager.sendPressurePlatePlayerDataToClient(tpp);
            }

            return;
        }

        if (ID == 5)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            if (world.getTileEntity(coords[0], coords[1], coords[2]) instanceof TileEntityPressurePlate)
            {
                TileEntityPressurePlate tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
                PacketSendManager.sendPressurePlatePlayerDataToClient(tpp);
            }

            return;
        }

        if (ID == 6)
        {
            int[] coords = new int[3];

            for (int var2 = 0; var2 < 3; var2 ++)
            {
                coords[var2] = dat.readInt();
            }

            TileEntityPressurePlate tpp = null;

            if (world.getTileEntity(coords[0], coords[1], coords[2]) instanceof TileEntityPressurePlate)
            {
                tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
            }

            if (tpp != null)
            {
                PacketSendManager.sendPressurePlateMobDataToClient(tpp);
                PacketSendManager.sendPressurePlatePlayerDataToClient(tpp);
                PacketSendManager.sendSettingsDataToClient(tpp);
                PacketSendManager.sendChangedDataToClient(tpp);
            }

            return;
        }

        if (ID == 7)
        {
            PPRegistry.loggedIn = true;
        }

        if (ID == 8)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            TileEntityPressurePlate tpp = null;

            if (world.getTileEntity(coords[0], coords[1], coords[2]) instanceof TileEntityPressurePlate)
            {
                tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
            }

            if (tpp != null)
            {
                int index = dat.readInt();
                tpp.switchSetting(index);
            }

            return;
        }

        if (ID == 9)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            TileEntityPressurePlate tpp = null;

            if (world.getTileEntity(coords[0], coords[1], coords[2]) instanceof TileEntityPressurePlate)
            {
                tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
            }

            if (tpp != null)
            {
                int length = dat.readInt();
                StringBuilder sb = new StringBuilder();

                for (int var1 = 0; var1 < length; var1++)
                {
                    sb.append(dat.readChar());
                }

                String s = sb.toString();
                tpp.password = s;
                PacketSendManager.sendPasswordSetToClient(tpp, player);
            }

            return;
        }

        if (ID == 10)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            TileEntityPressurePlate tpp = null;

            if (world.getTileEntity(coords[0], coords[1], coords[2]) instanceof TileEntityPressurePlate)
            {
                tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
            }

            if (tpp != null)
            {
                int length = dat.readInt();
                StringBuilder sb = new StringBuilder();

                for (int var1 = 0; var1 < length; var1++)
                {
                    sb.append(dat.readChar());
                }

                String s = sb.toString();
                boolean b = tpp.password.matches(s);
                PacketSendManager.sendPasswordResponseToClient(tpp, b, thePlayer);
            }

            return;
        }

        if (ID == 11)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            TileEntityPressurePlate tpp = null;

            if (world.getTileEntity(coords[0], coords[1], coords[2]) instanceof TileEntityPressurePlate)
            {
                tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
            }

            if (tpp != null)
            {
                PacketSendManager.sendPasswordToClient(tpp, thePlayer);
            }

            return;
        }

        if (ID == 12)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            int GuiID = dat.readInt();
            TileEntityPressurePlate tpp = null;

            if (world.getTileEntity(coords[0], coords[1], coords[2]) instanceof TileEntityPressurePlate)
            {
                tpp = (TileEntityPressurePlate)world.getTileEntity(coords[0], coords[1], coords[2]);
            }

            if (tpp != null)
            {
                thePlayer.openGui(IronPP.instance, GuiID, world, tpp.xCoord, tpp.yCoord, tpp.zCoord);
            }

            return;
        }

        if (ID == 13)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }

            world.setBlockToAir(coords[0], coords[1], coords[2]);
        }
        if (ID == 14)
        {
            int coords[] = new int[3];

            for (int var1 = 0; var1 < 3; var1++)
            {
                coords[var1] = dat.readInt();
            }
            TileEntity te = world.getTileEntity(coords[0], coords[1], coords[2]);
            if(te instanceof TileEntityPressurePlate)
            {
            	((TileEntityPressurePlate) te).maxOutput = dat.readInt();
            	((TileEntityPressurePlate) te).itemsForMax = dat.readInt();
            	PacketSendManager.sendChangedDataToClient((TileEntityPressurePlate) te);
            }
        }
    }
}
