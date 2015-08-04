package genesis.world.biome;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPebbles;

public class BiomeGenBeachGenesis extends BiomeGenBaseGenesis
{
	public BiomeGenBeachGenesis (int id)
	{
		super(id);
		setBiomeName("Beach");
		fillerBlock = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST);
		setHeight(0.05F, 0.1F);
		setHeight(height_Shores);
		
		addDecoration(new WorldGenPebbles().setCountPerChunk(25));
	}
}
