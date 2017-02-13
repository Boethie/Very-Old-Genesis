package genesis.world;

import genesis.world.layer.GenLayerDebug;
import genesis.world.layer.GenLayerGenesis;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;

public class BiomeProviderGenesis extends BiomeProvider
{
	// debug biome generator to make working on world generation eaiser, enable with -Dgenesis.debug.simplebiomegen=true
	private static final boolean DEBUG_SIMPLE_BIOMES = "true".equalsIgnoreCase(System.getProperty("genesis.debug.simplebiomegen"));
	
	public BiomeProviderGenesis(long seed)
	{
		super();
		GenLayer[] agenlayer = GenLayerGenesis.initializeAllBiomeerators(seed);
		this.genBiomes = DEBUG_SIMPLE_BIOMES ? new GenLayerDebug(4) : agenlayer[0];
		this.biomeIndexLayer = DEBUG_SIMPLE_BIOMES ? new GenLayerDebug(4 + 2) : agenlayer[1];
	}
	
	public BiomeProviderGenesis(World worldIn)
	{
		this(worldIn.getSeed());
	}
}
