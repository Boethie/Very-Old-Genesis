package genesis.common;

import genesis.world.WorldProviderGenesis;
import net.minecraftforge.common.DimensionManager;

public class GenesisDimensions
{
	public static void registerDimensions()
	{
		DimensionManager.registerProviderType(GenesisConfig.genesisProviderId, WorldProviderGenesis.class, true);
		DimensionManager.registerDimension(GenesisConfig.genesisDimId, GenesisConfig.genesisProviderId);
	}
}
