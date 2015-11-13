package genesis.common;

import genesis.entity.extendedproperties.GenesisEntityData;
import genesis.entity.extendedproperties.NBTEntityProperty;
import genesis.portal.GenesisPortal;
import genesis.world.TeleporterGenesis;
import genesis.world.WorldProviderGenesis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class GenesisDimensions
{
	public static final NBTEntityProperty STORED_PLAYERS = new NBTEntityProperty("dimensionPlayers", new NBTTagCompound(), true);
	public static final String GENESIS_PLAYER_DATA = "genesis";
	public static final String OTHER_PLAYER_DATA = "other";
	
	public static void register()
	{
		DimensionManager.registerProviderType(GenesisConfig.genesisProviderId, WorldProviderGenesis.class, true);
		DimensionManager.registerDimension(GenesisConfig.genesisDimId, GenesisConfig.genesisProviderId);
		
		GenesisEntityData.registerProperty(EntityPlayerMP.class, STORED_PLAYERS);
	}
	
	public static TeleporterGenesis getTeleporter(WorldServer world)
	{
		for (Teleporter teleporter : world.customTeleporters)
		{
			if (teleporter instanceof TeleporterGenesis)
			{
				return (TeleporterGenesis) teleporter;
			}
		}
		
		return new TeleporterGenesis(world);
	}
	
	public static boolean teleportToDimension(Entity entity, GenesisPortal portal, int id, boolean force)
	{
		if (!entity.worldObj.isRemote)
		{
			EntityPlayerMP player = null;
			
			if (entity instanceof EntityPlayerMP)
			{
				player = (EntityPlayerMP) entity;
			}
			
			MinecraftServer server = MinecraftServer.getServer();
			ServerConfigurationManager manager = server.getConfigurationManager();
			
			WorldServer newWorld = server.worldServerForDimension(id);
			
			TeleporterGenesis teleporter = getTeleporter(newWorld);
			teleporter.setOriginatingPortal(portal);
			
			boolean teleported = false;
			
			if (player != null)
			{
				NBTTagCompound dimensionPlayers = null;
				NBTTagCompound restoreData = null;
				
				if (!force && !player.capabilities.isCreativeMode)
				{
					String storingName = null;
					String restoreName = null;
					
					// Set the names that will be loaded from and saved to in the extended entity property.
					if (id == GenesisConfig.genesisDimId)
					{
						storingName = OTHER_PLAYER_DATA;
						restoreName = GENESIS_PLAYER_DATA;
					}
					else
					{
						storingName = GENESIS_PLAYER_DATA;
						restoreName = OTHER_PLAYER_DATA;
					}
					
					// Get the stored players from both sides.
					dimensionPlayers = GenesisEntityData.getValue(player, STORED_PLAYERS);
					
					// Get the player to restore.
					restoreData = dimensionPlayers.getCompoundTag(restoreName);	
					dimensionPlayers.removeTag(restoreName);	// Remove the stored player so that no duplication occurs.
					
					// Write the current player.
					NBTTagCompound storingData = new NBTTagCompound();
					player.writeToNBT(storingData);	// Write the current player to the compound.
					storingData.getCompoundTag(GenesisEntityData.COMPOUND_KEY).removeTag(STORED_PLAYERS.getName());
					
					// Save the current player to the data.
					dimensionPlayers.setTag(storingName, storingData);
				}
				
				// Transfer the original player.
				manager.transferPlayerToDimension(player, id, teleporter);
				
				if (dimensionPlayers != null)
				{
					// Save player position.
					double x = player.posX;
					double y = player.posY;
					double z = player.posZ;
					float yaw = player.rotationYaw;
					float pitch = player.rotationPitch;
					
					// Create a new player to reset all their stats and inventory.
					EntityPlayerMP respawnedPlayer = manager.recreatePlayerEntity(player, id, false);
					respawnedPlayer.playerNetServerHandler.playerEntity = respawnedPlayer;	// recreate doesn't set this.
					
					entity = respawnedPlayer;
					
					if (restoreData != null)
					{	// Restore the player's inventory from data saved when the player traveled from the dimension previously.
						respawnedPlayer.readFromNBT(restoreData);
						respawnedPlayer.dimension = id;
						respawnedPlayer.capabilities.isFlying = player.capabilities.isFlying;	// Will be sent to client by setGameType.
						respawnedPlayer.theItemInWorldManager.setGameType(player.theItemInWorldManager.getGameType());
					}
					
					respawnedPlayer.inventory.currentItem = player.inventory.currentItem;	// Keep the current selected hotbar item.
					manager.syncPlayerInventory(respawnedPlayer);	// Send the player's inventory, stats and current item.
					
					// Save the old player's data for restoration later.
					GenesisEntityData.setValue(respawnedPlayer, STORED_PLAYERS, dimensionPlayers);
					
					// Restore the player to the position of the portal.
					respawnedPlayer.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
				}
				
				teleported = true;
			}
			else
			{	// Is broken, and we probably don't want vanilla entities entering the dimension anyway.
				// TODO: Should maybe get this working for Genesis -> Overworld, and maybe a server option?
				//manager.transferEntityToWorld(entity, entity.dimension, oldWorld, newWorld, teleporter);
			}
			
			//GenesisSounds.playMovingEntitySound(new ResourceLocation(Constants.ASSETS_PREFIX + "portal.enter"), false,
			//		entity, 0.9F + oldWorld.rand.nextFloat() * 0.2F, 0.8F + oldWorld.rand.nextFloat() * 0.4F);
			
			if (teleported)
			{
				entity.timeUntilPortal = GenesisPortal.COOLDOWN;
			}
			
			return teleported;
		}
		
		return true;
	}
}
