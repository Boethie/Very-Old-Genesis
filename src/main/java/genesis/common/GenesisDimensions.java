package genesis.common;

import genesis.capabilities.GenesisCapabilities;
import genesis.capabilities.IDimensionPlayers;
import genesis.capabilities.playerinventory.CapabilityPlayerInventory;
import genesis.capabilities.playerinventory.IPlayerInventory;
import genesis.portal.GenesisPortal;
import genesis.stats.GenesisAchievements;
import genesis.util.Constants;
import genesis.util.PlayerWorldState;
import genesis.world.TeleporterGenesis;
import genesis.world.WorldProviderGenesis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
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
				IPlayerInventory handler = player.getCapability(CapabilityPlayerInventory.PLAYER_INVENTORY_CAPABILITY, null);
				boolean respawn = !force && !player.capabilities.isCreativeMode;

				if (respawn)
				{
					if (dimID == GenesisConfig.genesisDimId && handler != null)
					{
						handler.storeInventory(player);
					}
				}

				// Transfer the original player.
				manager.transferPlayerToDimension(player, dimID, teleporter);

				if (respawn)
				{
//					PlayerWorldState playerState = new PlayerWorldState(player);

					// Create a new player to reset all their stats and inventory.
//					EntityPlayerMP newPlayer = manager.recreatePlayerEntity(player, dimID, false);
//					newPlayer.connection.playerEntity = newPlayer;	// recreate doesn't set this.


					// Restore the player's state in the world.
//					playerState.restore(newPlayer);
//
//					entity = player = newPlayer;
					
					if (dimID != GenesisConfig.genesisDimId && handler != null)
					{
						handler.restoreInventory(player);
					}
					
				}

				teleported = true;
			}
			//else
			//{	 Is broken, and we probably don't want vanilla entities entering the dimension anyway.
			//	// TODO: Should maybe get this working for Genesis -> Overworld, and maybe a server option?
			//	manager.transferEntityToWorld(entity, entity.dimension, oldWorld, newWorld, teleporter);
			//}

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
