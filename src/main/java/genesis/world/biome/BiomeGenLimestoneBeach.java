package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPebbles;

public class BiomeGenLimestoneBeach extends BiomeGenBaseGenesis
{
	public BiomeGenLimestoneBeach (int id)
	{
		super(id);
		setBiomeName("Limestone Beach");
		topBlock = GenesisBlocks.limestone.getDefaultState();
		fillerBlock = GenesisBlocks.limestone.getDefaultState();
		setHeight(0.05F, 0.1F);
		setHeight(height_Shores);
		setColor(10658436);
		
		addDecoration(new WorldGenPebbles().setCountPerChunk(25));
	}
}
