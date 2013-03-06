/**
 * Base class for the Iron pressure plate mod.
 * Registers all the block stuff and packet channels.
 *
 * @author Kalvin
 */
package xelitez.ironpp;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import xelitez.ironpp.client.PPRenderer;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.block.Block;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * Mod registration stuff.
 */
@Mod(	modid = "IronPP", 
		name = "Iron Pressure Plate mod",
		version = "3.3.3")
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
        versionBounds = "[3.3,3.4)",
        channels = {"IPP"},
        packetHandler = xelitez.ironpp.PacketHandler.class,
        connectionHandler = xelitez.ironpp.PPRegistry.class)
public class IronPP
{
    Configuration P;

    /**
     * Registers the default BlockIDs and the blocks.
     */
    public int defaultPressurePlateIronId = 2150;
    public int defaultAPressurePlateIronId = 2151;
    private int BlockPPiD;
    private int BlockAPPiD;
    public static Block PressurePlateIron;
    public static Block APressurePlateIron;
    private boolean customTexture;
    public boolean checkForUpdates;

    /**
     * Temporary storage for the iron pressure plate block texture index.
     */
    private int PPT;

    /**
     * Instances of the proxy and this class to be used by other classes.
     */
    @SidedProxy(clientSide = "xelitez.ironpp.client.ClientProxy", serverSide = "xelitez.ironpp.CommonProxy")
    public static CommonProxy proxy = new CommonProxy();
    @Instance(value = "IronPP")
    public static IronPP instance;
    
    public static Logger ippLog;

    /**
     * Since the INSTANCE is private I use this method to gain access to the class.
     */
    @PreInit
    public void preload(FMLPreInitializationEvent evt)
    {
        evt.getModMetadata().version = Version.getVersion() + " for " + Version.MC;
        P = new Configuration(new File("XEliteZ/IronPP.cfg")); //sets the file to create or load for the configuration file.
        ippLog = Logger.getLogger("IronPressurePlate");
        ippLog.setParent(FMLLog.getLogger());
        try
        {
            P.load(); //loads the configuration file.
            BlockPPiD = P.getBlock("PressurePlateIronId", defaultPressurePlateIronId).getInt(defaultPressurePlateIronId); //gets the ID that's currently set in the configuration file or sets it with the default.
            BlockAPPiD = P.getBlock("AdvancedPressurePlateIronId", defaultAPressurePlateIronId).getInt(defaultAPressurePlateIronId);
            Property PressurePlateIronTexture = P.get(Configuration.CATEGORY_GENERAL, "PressurePlateIronCustomTexture", false); //gets the boolean if the user wants to use a custom texture.
            PressurePlateIronTexture.comment = "set to true to enable custom textures which must be located in '.minecraft/bin/minecraft.jar' or the mod zip file as 'IronPP.png'"; //adds a comment to the boolean section in the configuration.
            Property update = P.get("Updates", "Check for updates", true);
            Property ignoreMinorBuilds = P.get("Updates", "Ignore minor builds", true);
            Property ignoreOtherMCVersions = P.get("Updates", "Ignore other MC versions", false);
            Property notify = P.get("Updates", "Notify about XEZUpdateUtility", true);
            Version.notify = notify.getBoolean(true);
            customTexture = PressurePlateIronTexture.getBoolean(false);
            checkForUpdates = update.getBoolean(true);
            Version.ignoremB = ignoreMinorBuilds.getBoolean(true);
            Version.ignoreMC = ignoreOtherMCVersions.getBoolean(false);
        }
        catch (Exception E)
        {
            ippLog.log(Level.SEVERE, "Failed to load Iron Pressure Plate configuration", E);
        }
        finally
        {
            P.save(); //saves the configuration file.
        }
    }
    /**
     * forge load method to load and register everything needed.
     * @param evt
     */
    @Init
    public void load(FMLInitializationEvent evt)
    {
        if (customTexture)
        {
            PPT = RenderingRegistry.addTextureOverride("/terrain.png", "/IronPP.png"); //replacement for ModLoaders addOverride method.
        }
        else
        {
            PPT = Block.blockSteel.blockIndexInTexture; //sets the block texture to the same as the one of the Iron Block
        }

        //the next two methods creates the instances of the blocks used in this mod.
        PressurePlateIron = new BlockPressurePlate(BlockPPiD, Block.blockSteel.blockIndexInTexture, EnumMobType.players, Material.iron).setHardness(0.5F).setStepSound(Block.soundMetalFootstep).setBlockName("pressurePlate");
        APressurePlateIron = new BlockAPressurePlate(BlockAPPiD, PPT, Material.iron).setHardness(0.5F).setStepSound(Block.soundMetalFootstep).setBlockName("ApressurePlate");
        GameRegistry.registerBlock(PressurePlateIron, "Iron Pressure Plate"); //Registers the block in the game.(replaces ModLoaders registerBlock method)
        GameRegistry.registerBlock(APressurePlateIron, "Advanced Iron Pressure Plate");
        LanguageRegistry.addName(APressurePlateIron, new StringBuilder().append("Advanced ").append(StatCollector.translateToLocal("tile.pressurePlate.name")).toString()); //adds a display name for Items or Blocks.(replaces ModLoaders addName method)
        GameRegistry.addRecipe(new ItemStack(PressurePlateIron, 1), new Object[] {"##", '#', Item.ingotIron}); //Registers a new recipe used in the crafting bench.(replaces ModLoaders addRecipe method)
        GameRegistry.addRecipe(new ItemStack(APressurePlateIron, 2), new Object[] {"###", "@#@", '#', Item.ingotIron, '@', Item.redstone});
        GameRegistry.registerTileEntity(TileEntityPressurePlate.class, "Advanced Pressure Plate"); //registers the TileEntity of the advanced iron pressure plate.(replaces ModLoaders registerTileEntity method)
        NetworkRegistry.instance().registerGuiHandler(instance, proxy); //Registers a GuiHandler assigned to this mod.(mostly for Network SMP Gui's)
        TickRegistry.registerTickHandler(new PPRegistry(), Side.CLIENT);
        TickRegistry.registerTickHandler(new PPRegistry(), Side.SERVER);
        if(evt.getSide().isClient())
        {
        	RenderingRegistry.registerBlockHandler(2151, new PPRenderer());
        }
        proxy.RegisterKeyHandler();
        PPSettings.addLineWithButton("Unlisted players are by default:", "Enabled", "Disabled", false, 0);
        PPSettings.addLineWithButton("Sound is:", "On", "Off", true, 1);
        PPSettings.addLineWithButton("Password", "Enabled", "Disabled", false, 2);
        PPSettings.addClickableLine("Set Password", 0);
        PPSettings.addLineWithButton("Ask password on break", "Yes", "No", false, 3);
        PPSettings.addLine("Note: if you have set no password but enabled password then just press enter if you get stuck on the gui screen");
        try
        {
            if (checkForUpdates)
            {
				Class<? extends Object> clazz = Class.forName("xelitez.updateutility.UpdateRegistry");
                Method registermod = clazz.getDeclaredMethod("addMod", Object.class, Object.class);
                registermod.invoke(null, this, new Update());
                Version.registered = true;
            }
        }
        catch (Exception E)
        {
        	ippLog.log(Level.INFO, "IronPressurePlate failed to register to the XEZUpdateUtility");
        	ippLog.log(Level.INFO, "It isn't required but you should download it if possible");
            if (checkForUpdates)
            {
                Version.checkForUpdatesNoXEZ();
            }
        }
    }
}