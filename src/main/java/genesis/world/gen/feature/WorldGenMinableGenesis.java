package genesis.world.gen.feature;

import java.util.Random;

import genesis.common.GenesisBlocks;
import genesis.util.RandomIntRange;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.google.common.base.Predicate;

public class WorldGenMinableGenesis extends WorldGenMinable
{
	public final RandomIntRange count;
	
	public WorldGenMinableGenesis(Block oreBlock, int minCount, int maxCount)
	{
		this(oreBlock, minCount, maxCount, GenesisBlocks.granite);
	}

	public WorldGenMinableGenesis(Block oreBlock, int minCount, int maxCount, Block targetBlock)
	{
		this(oreBlock.getDefaultState(), minCount, maxCount, BlockHelper.forBlock(targetBlock));
	}

	public WorldGenMinableGenesis(IBlockState ore, int minCount, int maxCount, Predicate target)
	{
		super(ore, minCount, target);
		count = new RandomIntRange(minCount, maxCount);
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		//numberOfBlocks = count.getRandom(rand);
		boolean generate = super.generate(worldIn, rand, position);
		//numberOfBlocks = count.min;
		return generate;
	}
}
