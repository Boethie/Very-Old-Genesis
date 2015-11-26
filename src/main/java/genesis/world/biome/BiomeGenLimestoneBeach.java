package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenLimestoneBeach extends BiomeGenBaseGenesis
{
	public BiomeGenLimestoneBeach (int id)
	{
		super(id);
		setBiomeName("Limestone Beach");
		setTemperatureRainfall(0.8F, 0.4F);
		topBlock = GenesisBlocks.limestone.getDefaultState();
		fillerBlock = GenesisBlocks.limestone.getDefaultState();
		setHeight(0.05F, 0.1F);
		
		waterColorMultiplier = 0x059044;
		
		addDecoration(new WorldGenPebbles().setCountPerChunk(25));
		
		addDecoration(new WorldGenRockBoulders().setRarity(95).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
