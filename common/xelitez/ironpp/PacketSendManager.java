/**
 * this class is the class that I assigned to send packets between
 * server and client containing data on what to do with mostly
 * using the TileEntityPressurePlate class as a check.
 *
 * @author Kalvin
 */

package xelitez.ironpp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import xelitez.ironpp.client.GuiModifyPressurePlate;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketSendManager
{
    /**
     * method for sending packets to the server.
     * the server can either be the minecraft internal
     * server or a remote server.
     * @param packet	the packet to be send
     */
    private static void sendPacketToServer(Packet250CustomPayload packet)
    {
        FMLClientHandler.instance().sendPacket(packet);
    }

    /**
     * method for sending packets to the client.
     * this source can be either the internal server of
     * the minecraft client or a remote server.
     * @param packet
     */
    private static void sendPacketToAllPlayers(Packet250CustomPayload packet)
    {
        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);
    }

    /**
     * method for sending a packet that says a server to
     * switch the value of a certain mob to either
     * enable or disable.
     * @param tpp	The TileEntityPressurePlate.
     * @param Mob	The Index of the mob to be switched.
     */
    public static void sendSwitchMobPacketToServer(TileEntityPressurePlate tpp, int Mob)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(1);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }

            if (tpp.allowedMobs[Mob] != null)
            {
                PPList pp = tpp.allowedMobs[Mob];
                data.writeShort(pp.getMobname().length());
                data.writeChars(pp.getMobname());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    /**
     * method for sending a packet that says a server to
     * switch the value of a certain player to either
     * enable or disable.
     * @param tpp		TileEntityPressurePlate
     * @param player	index of the player
     */
    public static void sendSwitchPlayerPacketToServer(TileEntityPressurePlate tpp, int player)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(2);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }

            if (((PPPlayerList)tpp.allowedPlayers.get(player)) != null)
            {
                PPPlayerList pp = (PPPlayerList)tpp.allowedPlayers.get(player);
                data.writeShort(pp.getUsername().length());
                data.writeChars(pp.getUsername());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    /**
     * sends a packet to the server to add a player's
     * username to the correct pressure plate
     * @param tpp		TileEntityPressurePlate
     * @param name		the name to be added
     * @param gui		the instance of the Gui that wants to send the packet
     * @param client	the game's instance for a checksum on which player sends it
     */
    public static void sendAddPlayerPacketToServer(TileEntityPressurePlate tpp, String name, GuiModifyPressurePlate gui, Minecraft client)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(3);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }

            data.writeInt(name.length());

            for (int var1 = 0; var1 < name.length(); var1++)
            {
                data.writeChar(name.charAt(var1));
            }

            data.writeInt(client.thePlayer.username.length());

            for (int var2 = 0; var2 < client.thePlayer.username.length(); var2++)
            {
                data.writeChar(client.thePlayer.username.charAt(var2));
            }
        }
        catch (IOException e)
        {
            GuiModifyPressurePlate.showText("Player adding failed", 20);
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    /**
     * does about the same as the add player packet
     * but then to remove the player
     * @param tpp
     * @param name
     * @param gui
     * @param client
     */
    public static void sendRemovePlayerPacketToServer(TileEntityPressurePlate tpp, String name, GuiModifyPressurePlate gui, Minecraft client)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(4);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }

            data.writeInt(name.trim().length());

            for (int var1 = 0; var1 < name.length(); var1++)
            {
                data.writeChar(name.charAt(var1));
            }

            data.writeInt(client.thePlayer.username.length());

            for (int var2 = 0; var2 < client.thePlayer.username.length(); var2++)
            {
                data.writeChar(client.thePlayer.username.charAt(var2));
            }
        }
        catch (IOException e)
        {
            GuiModifyPressurePlate.showText("Player removing failed", 20);
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    /**
     * sends a packet to the server to get data
     * of the pressure plate and then the server
     * has to send the data back and tell the Gui
     * to load the data values.
     * @param tpp
     */
    public static void sendGuiReloaderToServer(TileEntityPressurePlate tpp)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(5);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    /**
     * request the data of a pressure plate
     * and then the server sends back the
     * data.(used for the gui)
     * @param tpp
     */
    public static void requestPPDataFromServer(int xCoord, int yCoord, int zCoord)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(6);
            int[] coords = {xCoord, yCoord, zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    public static void requestPPDataFromServer(TileEntityPressurePlate tpp)
    {
        requestPPDataFromServer(tpp.xCoord, tpp.yCoord, tpp.zCoord);
    }

    public static void sendIsReadyToServer()
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(7);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    public static void sendSwitchSettingToServer(TileEntityPressurePlate tpp, int index)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(8);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }

            data.writeInt(index);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    public static void sendNewPasswordToServer(TileEntityPressurePlate tpp, String password)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(9);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }

            data.writeInt(password.length());
            data.writeChars(password);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    public static void sendPasswordToServer(TileEntityPressurePlate tpp, String password)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(10);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }

            data.writeInt(password.length());
            data.writeChars(password);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    public static void getPasswordFromServer(TileEntityPressurePlate tpp)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(11);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    public static void sendOpenGuiPacketToServer(TileEntityPressurePlate tpp, int GuiID)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(12);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }

            data.writeInt(GuiID);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    public static void sendRemoveBlockPacketToServer(TileEntityPressurePlate tpp)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(true);
            data.writeShort(13);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var1 = 0; var1 < 3; var1++)
            {
                data.writeInt(coords[var1]);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToServer(packet);
    }

    /**
     * send a packet to all players that closes
     * the gui of the players if the gui of the
     * pressure plate is the one which is destroyed.
     * @param tilepp
     */
    public static void sendCloseGuiPacketToAllPlayers(TileEntityPressurePlate tilepp)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(1);
            int[] coords = {tilepp.xCoord, tilepp.yCoord, tilepp.zCoord};

            for (int var6 = 0; var6 < 3; var6++)
            {
                data.writeInt(coords[var6]);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToAllPlayers(packet);
    }

    /**
     * sends data to the client containing the mobs data
     * values.
     * @param tpp
     */
    public static void sendPressurePlateMobDataToClient(TileEntityPressurePlate tpp)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(2);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var5 = 0; var5 < 3; var5++)
            {
                data.writeInt(coords[var5]);
            }

            data.writeInt(tpp.allowedMobs.length);

            for (int var1 = 0; var1 < tpp.allowedMobs.length; var1++)
            {
                data.writeBoolean(tpp.allowedMobs[var1].getEnabled());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToAllPlayers(packet);
    }

    /**
     * sends data to the client containing the players data
     * values.
     * @param tpp
     */
    public static void sendPressurePlatePlayerDataToClient(TileEntityPressurePlate tpp)
    {
        ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes2);

        try
        {
            data.writeBoolean(false);
            data.writeShort(3);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var5 = 0; var5 < 3; var5++)
            {
                data.writeInt(coords[var5]);
            }

            data.writeInt(tpp.allowedPlayers.size());

            for (int var6 = 0; var6 < tpp.allowedPlayers.size(); var6++)
            {
                data.writeShort(((PPPlayerList)tpp.allowedPlayers.get(var6)).getUsername().length());

                for (int var7 = 0; var7 < ((PPPlayerList)tpp.allowedPlayers.get(var6)).getUsername().length(); var7++)
                {
                    data.writeChar(((PPPlayerList)tpp.allowedPlayers.get(var6)).getUsername().charAt(var7));
                }

                data.writeBoolean(((PPPlayerList)tpp.allowedPlayers.get(var6)).getEnabled());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet2 = new Packet250CustomPayload();
        packet2.channel = "IPP";
        packet2.data = bytes2.toByteArray();
        packet2.length = packet2.data.length;
        sendPacketToAllPlayers(packet2);
    }

    /**
     * sends a packet to the client telling to
     * disable or enable a certain mob.
     * (this same method of switching buttons is not
     * used for the players since changes can occur while switching,
     * for players it sends back all player data instead)
     * @param tpp
     * @param mob	the index of the mob.
     */
    public static void sendSwitchMobButtonPacketToClient(TileEntityPressurePlate tpp, int mob)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(4);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var5 = 0; var5 < 3; var5++)
            {
                data.writeInt(coords[var5]);
            }

            if (tpp.allowedMobs[mob] != null)
            {
                data.writeInt(mob);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToAllPlayers(packet);
    }

    /**
     * sends a packet to the client to say if
     * adding a player to the pressure plate has
     * succeeded or failed.
     * @param b			the boolean that says if it has failed or succeeded
     * @param var1		x coordinate of the corresponding pressure plate
     * @param var2		y coordinate of the corresponding pressure plate
     * @param var3		z coordinate of the corresponding pressure plate
     * @param username	username of the player that wanted to add a name to the pressure plate.
     */
    public static void sendAddBooleanToClient(boolean b, int var1, int var2, int var3, String username)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(5);
            int[] coords = {var1, var2, var3};

            for (int var11 = 0; var11 < 3; var11++)
            {
                data.writeInt(coords[var11]);
            }

            data.writeBoolean(b);
            data.writeInt(username.length());

            for (int var4 = 0; var4 < username.length(); var4++)
            {
                data.writeChar(username.charAt(var4));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToAllPlayers(packet);
    }

    /**
     * does the same as the method above this one but only
     * is this one for the removal of a player.
     * @param b
     * @param var1
     * @param var2
     * @param var3
     * @param username
     */
    public static void sendRemoveBooleanToClient(boolean b, int var1, int var2, int var3, String username)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(6);
            int coords[] = new int[3];
            coords[0] = var1;
            coords[1] = var2;
            coords[2] = var3;

            for (int var11 = 0; var11 < 3; var11++)
            {
                data.writeInt(coords[var11]);
            }

            data.writeBoolean(b);
            data.writeInt(username.length());

            for (int var4 = 0; var4 < username.length(); var4++)
            {
                data.writeChar(username.charAt(var4));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToAllPlayers(packet);
    }

    public static void sendItemStackToClients(int x, int y, int z, int stack, int damage, int stacksize, int dimension)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(7);
            data.writeInt(x);
            data.writeInt(y);
            data.writeInt(z);
            data.writeInt(stack);
            data.writeInt(stacksize);
            data.writeInt(damage);
            data.writeInt(dimension);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToAllPlayers(packet);
    }

    public static void sendBlockBooleanToClient(TileEntityPressurePlate tpp, boolean b)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(8);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var11 = 0; var11 < 3; var11++)
            {
                data.writeInt(coords[var11]);
            }

            data.writeBoolean(b);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToAllPlayers(packet);
    }

    public static void SendIsReadyToClient(Player player)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(9);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        PacketDispatcher.sendPacketToPlayer(packet, player);
    }

    public static void sendSwitchSettingButtonToClient(TileEntityPressurePlate tpp, int index)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(10);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var5 = 0; var5 < 3; var5++)
            {
                data.writeInt(coords[var5]);
            }

            data.writeInt(index);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToAllPlayers(packet);
    }

    public static void sendSettingsDataToClient(TileEntityPressurePlate tpp)
    {
        ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes2);

        try
        {
            data.writeBoolean(false);
            data.writeShort(11);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var5 = 0; var5 < 3; var5++)
            {
                data.writeInt(coords[var5]);
            }

            data.writeInt(PPSettings.buttons.size());

            for (int var6 = 0; var6 < PPSettings.buttons.size(); var6++)
            {
                data.writeBoolean(tpp.getIsEnabled(var6));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet2 = new Packet250CustomPayload();
        packet2.channel = "IPP";
        packet2.data = bytes2.toByteArray();
        packet2.length = packet2.data.length;
        sendPacketToAllPlayers(packet2);
    }

    public static void sendPasswordSetToClient(TileEntityPressurePlate tpp, Player player)
    {
        ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes2);

        try
        {
            data.writeBoolean(false);
            data.writeShort(12);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var5 = 0; var5 < 3; var5++)
            {
                data.writeInt(coords[var5]);
            }

            String name = ((EntityPlayer)player).username;
            data.writeInt(name.length());
            data.writeChars(name);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet2 = new Packet250CustomPayload();
        packet2.channel = "IPP";
        packet2.data = bytes2.toByteArray();
        packet2.length = packet2.data.length;
        sendPacketToAllPlayers(packet2);
    }

    public static void sendPasswordResponseToClient(TileEntityPressurePlate tpp, Boolean b, Player player)
    {
        ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes2);

        try
        {
            data.writeBoolean(false);
            data.writeShort(13);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var5 = 0; var5 < 3; var5++)
            {
                data.writeInt(coords[var5]);
            }

            data.writeBoolean(b);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet2 = new Packet250CustomPayload();
        packet2.channel = "IPP";
        packet2.data = bytes2.toByteArray();
        packet2.length = packet2.data.length;
        PacketDispatcher.sendPacketToPlayer(packet2, player);
    }

    public static void sendUsesPasswordToClient(int xCoord, int yCoord, int zCoord, int dimension, Boolean b)
    {
        ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes2);

        try
        {
            data.writeBoolean(false);
            data.writeShort(14);
            int[] coords = {xCoord, yCoord, zCoord};

            for (int var5 = 0; var5 < 3; var5++)
            {
                data.writeInt(coords[var5]);
            }

            data.writeInt(dimension);
            data.writeBoolean(b);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet2 = new Packet250CustomPayload();
        packet2.channel = "IPP";
        packet2.data = bytes2.toByteArray();
        packet2.length = packet2.data.length;
        sendPacketToAllPlayers(packet2);
    }

    public static void sendPasswordToClient(TileEntityPressurePlate tpp, Player player)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(15);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var6 = 0; var6 < 3; var6++)
            {
                data.writeInt(coords[var6]);
            }

            data.writeInt(tpp.password.length());
            data.writeChars(tpp.password);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        PacketDispatcher.sendPacketToPlayer(packet, player);
    }

    public static void sendRemovePressurePlateToClient(TileEntityPressurePlate tpp, int dimension)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(16);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var6 = 0; var6 < 3; var6++)
            {
                data.writeInt(coords[var6]);
            }

            data.writeInt(dimension);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToAllPlayers(packet);
    }

    public static void sendAddPressurePlateToClient(TileEntityPressurePlate tpp, int dimension)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(17);
            int[] coords = {tpp.xCoord, tpp.yCoord, tpp.zCoord};

            for (int var6 = 0; var6 < 3; var6++)
            {
                data.writeInt(coords[var6]);
            }

            data.writeInt(dimension);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        sendPacketToAllPlayers(packet);
    }

    public static void sendPPIntToClient(int i, EntityPlayer player)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeBoolean(false);
            data.writeShort(18);
            data.writeInt(i);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "IPP";
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        PacketDispatcher.sendPacketToPlayer(packet, (Player)player);
    }
}