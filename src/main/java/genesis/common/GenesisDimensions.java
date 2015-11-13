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
import net.minecraft.world.WorldSettings.GameType;
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
	
	public static void teleportToDimension(Entity entity, GenesisPortal portal, int id, boolean force)
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
			
			if (player != null)
			{	// Creates a new player
				String fromData = null;
				String toData = null;
				
				if (id == GenesisConfig.genesisDimId)
				{
					fromData = OTHER_PLAYER_DATA;
					toData = GENESIS_PLAYER_DATA;
				}
				else
				{
					fromData = GENESIS_PLAYER_DATA;
					toData = OTHER_PLAYER_DATA;
				}
				
				NBTTagCompound dimensionPlayers = GenesisEntityData.getValue(player, STORED_PLAYERS);
				NBTTagCompound loadingPlayer = dimensionPlayers.getCompoundTag(toData);
				dimensionPlayers.removeTag(toData);
				
				NBTTagCompound savedPlayer = new NBTTagCompound();
				player.writeToNBT(savedPlayer);
				savedPlayer.getCompoundTag(GenesisEntityData.COMPOUND_KEY).removeTag(STORED_PLAYERS.getName());
				dimensionPlayers.setTag(fromData, savedPlayer);
				
				manager.transferPlayerToDimension(player, id, teleporter);
				
				double x = player.posX;
				double y = player.posY;
				double z = player.posZ;
				float yaw = player.rotationYaw;
				float pitch = player.rotationPitch;
				GameType gameType = player.theItemInWorldManager.getGameType();
				
				EntityPlayerMP respawnedPlayer = manager.recreatePlayerEntity(player, id, false);
				respawnedPlayer.playerNetServerHandler.playerEntity = respawnedPlayer;	// recreate doesn't set this.
				
				if (loadingPlayer != null)
				{
					loadingPlayer.removeTag("abilities");
					respawnedPlayer.readFromNBT(loadingPlayer);
					respawnedPlayer.dimension = id;
					respawnedPlayer.theItemInWorldManager.setGameType(gameType);
				}
				
				entity = respawnedPlayer;
				
				respawnedPlayer.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
				respawnedPlayer.capabilities.isCreativeMode = false;
				manager.syncPlayerInventory(respawnedPlayer);
				GenesisEntityData.setValue(respawnedPlayer, STORED_PLAYERS, dimensionPlayers);
			}
			else
			{	// Is broken, and we probably don't want vanilla entities entering the dimension anyway.
				//manager.transferEntityToWorld(entity, entity.dimension, oldWorld, newWorld, teleporter);
			}
			
			//GenesisSounds.playMovingEntitySound(new ResourceLocation(Constants.ASSETS_PREFIX + "portal.enter"), false,
			//		entity, 0.9F + oldWorld.rand.nextFloat() * 0.2F, 0.8F + oldWorld.rand.nextFloat() * 0.4F);
			
			entity.timeUntilPortal = entity.getPortalCooldown();
		}
	}
}
