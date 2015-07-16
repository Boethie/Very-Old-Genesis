package genesis.world.biome;

import genesis.common.GenesisBlocks;

public class BiomeGenShallowOcean extends BiomeGenBaseGenesis
{

	public BiomeGenShallowOcean(int id)
	{
		super(id);
		this.biomeName = "Shallow Ocean";
		this.minHeight = -.8F;
		this.maxHeight = 0.1F;
		this.topBlock = GenesisBlocks.ooze.getDefaultState();
		this.fillerBlock = GenesisBlocks.ooze.getDefaultState();
		this.waterColorMultiplier = 0x008d49;
	}

}
