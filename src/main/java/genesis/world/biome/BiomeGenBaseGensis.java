package genesis.world.biome;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class BiomeGenBaseGensis extends BiomeGenBase
{

	public BiomeGenBaseGensis(int id)
	{
		super(id);
		this.topBlock = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST).withProperty(BlockMoss.SNOWY, false);
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.waterColorMultiplier = 0xaa791e;
	}

}
