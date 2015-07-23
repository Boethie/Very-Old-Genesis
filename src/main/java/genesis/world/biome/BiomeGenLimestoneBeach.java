package genesis.world.biome;

import genesis.common.GenesisBlocks;

public class BiomeGenLimestoneBeach extends BiomeGenBaseGenesis
{
	public BiomeGenLimestoneBeach (int id)
	{
		super(id);
		this.biomeName = "Limestone Beach";
		this.topBlock = GenesisBlocks.limestone.getDefaultState();
		this.fillerBlock = GenesisBlocks.limestone.getDefaultState();
		this.minHeight = 0.05F;
		this.maxHeight = 0.1F;
	}
}
