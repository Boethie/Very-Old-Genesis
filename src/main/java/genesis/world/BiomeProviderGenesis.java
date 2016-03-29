package genesis.world;

import genesis.world.layer.GenLayerGenesis;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;

public class BiomeProviderGenesis extends BiomeProvider
{
	public BiomeProviderGenesis(long seed)
	{
		super();
		GenLayer[] agenlayer = GenLayerGenesis.initializeAllBiomeGenerators(seed);
		this.genBiomes = agenlayer[0];
		this.biomeIndexLayer = agenlayer[1];
	}
	
	public BiomeProviderGenesis(World worldIn)
	{
		this(worldIn.getSeed());
	}
}
