package genesis.world.biome;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import net.minecraft.block.BlockGrass;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class BiomeGenBaseGensis extends BiomeGenBase
{

	public BiomeGenBaseGensis(int id)
	{
		super(id);
		this.theBiomeDecorator = new BiomeDecoratorGenesis();
		this.theBiomeDecorator.grassPerChunk = 0;
		this.topBlock = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, BlockMoss.STAGE_LAST).withProperty(BlockGrass.SNOWY, false);
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.waterColorMultiplier = 0xaa791e;
	}

	@Override
	public int getFoliageColorAtPos(BlockPos p_180625_1_)
	{
		return 0x015a03;
	}

	
	
}
