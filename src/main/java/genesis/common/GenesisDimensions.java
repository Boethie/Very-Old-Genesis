package genesis.common;

import genesis.world.TeleporterGenesis;
import genesis.world.WorldProviderGenesis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class GenesisDimensions
{
	public static void registerDimensions()
	{
		DimensionManager.registerProviderType(GenesisConfig.genesisProviderId, WorldProviderGenesis.class, true);
		DimensionManager.registerDimension(GenesisConfig.genesisDimId, GenesisConfig.genesisProviderId);
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
	
	public static void teleportToDimension(Entity entity, int id)
	{
		if (!entity.worldObj.isRemote)
		{
			MinecraftServer server = MinecraftServer.getServer();
			ServerConfigurationManager manager = server.getConfigurationManager();
			WorldServer oldWorld = (WorldServer) entity.worldObj;
			WorldServer newWorld = server.worldServerForDimension(id);
			TeleporterGenesis teleporter = getTeleporter(newWorld);
			
			if (entity instanceof EntityPlayerMP)
			{
				manager.transferPlayerToDimension((EntityPlayerMP) entity, id, teleporter);
			}
			else
			{
				manager.transferEntityToWorld(entity, entity.dimension, oldWorld, newWorld, teleporter);
			}
		}
	}
}
