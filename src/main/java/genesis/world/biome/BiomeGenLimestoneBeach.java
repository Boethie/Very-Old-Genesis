package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPebbles;

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
		
		theBiomeDecorator.grassPerChunk = 0;
		
		addDecoration(new WorldGenPebbles().setCountPerChunk(25));
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
