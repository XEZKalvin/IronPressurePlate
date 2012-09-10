package xelitez.ironpp.client;

import xelitez.ironpp.CommonProxy;
import xelitez.ironpp.TileEntityPressurePlate;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class ClientProxy extends CommonProxy
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityPressurePlate)
		{
			return new GuiAPressurePlate((TileEntityPressurePlate)te);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}
	
	@Override
	public void RegisterKeyHandler()
	{
		KeyBindingRegistry.registerKeyBinding(new KeyHandler());
	}
	
	@Override
	public String getKey(int i)
	{
		return KeyHandler.instance().getKey(i);
	}
}
