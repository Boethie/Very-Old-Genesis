package genesis.common;

import genesis.capabilities.GenesisCapabilities;
import genesis.capabilities.IDimensionPlayers;
import genesis.portal.GenesisPortal;
import genesis.stats.GenesisAchievements;
import genesis.util.Constants;
import genesis.util.PlayerWorldState;
import genesis.world.*;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GenesisDimensions
{
	public static final DimensionType GENESIS_DIMENSION = DimensionType.register(Constants.MOD_NAME, "_genesis",
			GenesisConfig.genesisDimId, WorldProviderGenesis.class, false);
	
	public static void register()
	{
		DimensionManager.registerDimension(GenesisConfig.genesisDimId, GENESIS_DIMENSION);
	}
	
	public static boolean isGenesis(World world)
	{
		return world.provider.getDimensionType() == GENESIS_DIMENSION;
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
	
	public static boolean teleportToDimension(Entity entity, GenesisPortal portal, DimensionType dim, boolean force)
	{
		if (!entity.worldObj.isRemote)
		{
			int dimID = dim.getId();
			
			double motionX = entity.motionX;
			double motionY = entity.motionY;
			double motionZ = entity.motionZ;
			
			EntityPlayerMP player = null;
			
			if (entity instanceof EntityPlayerMP)
			{
				player = (EntityPlayerMP) entity;
			}
			
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			PlayerList manager = server.getPlayerList();
			
			WorldServer newWorld = server.worldServerForDimension(dimID);
			
			TeleporterGenesis teleporter = getTeleporter(newWorld);
			teleporter.setOriginatingPortal(portal);
			
			boolean teleported = false;
			
			if (player != null)
			{
				//NBTTagCompound dimensionPlayers = null;
				//NBTTagCompound restoreData = null;
				IDimensionPlayers dimPlayers = null;
				boolean respawn = !force && !player.capabilities.isCreativeMode;
				
				if (respawn)
				{
					/*String storingName = null;
					String restoreName = null;
					
					// Set the names that will be loaded from and saved to in the extended entity property.
					if (dim == GENESIS_DIMENSION)
					{
						storingName = OTHER_PLAYER_DATA;
						restoreName = GENESIS_PLAYER_DATA;
					}
					else
					{
						storingName = GENESIS_PLAYER_DATA;
						restoreName = OTHER_PLAYER_DATA;
					}*/
					
					// Get the stored players from both sides.
					//dimensionPlayers = GenesisEntityData.getValue(player, STORED_PLAYERS);
					
					// Get the player to restore.
					//restoreData = dimensionPlayers.getCompoundTag(restoreName);	
					//dimensionPlayers.removeTag(restoreName);	// Remove the stored player so that no duplication occurs.
					
					dimPlayers = player.getCapability(GenesisCapabilities.DIMENSION_PLAYERS, null);
					
					if (dimPlayers != null)
						dimPlayers.storePlayer(DimensionType.getById(player.dimension));
					
					// Save the current player to the data.
					//dimensionPlayers.setTag(storingName, storingData);
				}
				
				// Transfer the original player.
				manager.transferPlayerToDimension(player, dimID, teleporter);
				
				if (respawn)
				{
					PlayerWorldState playerState = new PlayerWorldState(player);
					
					// Create a new player to reset all their stats and inventory.
					EntityPlayerMP newPlayer = manager.recreatePlayerEntity(player, dimID, false);
					newPlayer.connection.playerEntity = newPlayer;	// recreate doesn't set this.
					
					if (dimPlayers != null)
					{	// Restore the player's inventory from data saved when the player traveled from the dimension previously.
						dimPlayers.restorePlayer(dim, newPlayer);
					}
					
					// Restore the player's state in the world.
					playerState.restore(newPlayer);
					
					// TODO: Save the old player's data for restoration later.
					
					entity = player = newPlayer;
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
			
			entity.motionX = motionX;
			entity.motionY = motionY;
			entity.motionZ = motionZ;
			
			if (player != null)
			{
				if (dim == GENESIS_DIMENSION)
					player.addStat(GenesisAchievements.enterGenesis, 1);
			}
			
			return teleported;
		}
		
		return true;
	}
}
