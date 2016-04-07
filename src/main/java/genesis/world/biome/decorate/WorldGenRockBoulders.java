package genesis.world.biome.decorate;

import genesis.combo.SiltBlocks;
import genesis.common.GenesisBlocks;
import genesis.util.WorldBlockMatcher;
import genesis.util.WorldUtils;

import java.util.Random;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenRockBoulders extends WorldGenDecorationBase
{
	protected final ImmutableList<IBlockState> blocks;
	private boolean waterRequired = true;
	private boolean inGround = true;
	private int maxHeight = 5;
	
	public WorldGenRockBoulders(IBlockState... blocks)
	{
		super(WorldBlockMatcher.AIR_LEAVES,
				(s, w, p) -> s.getBlock() == Blocks.dirt
						|| s.getBlock() == GenesisBlocks.moss
						|| GenesisBlocks.silt.isStateOf(s, SiltBlocks.SILT));
		
		this.blocks = ImmutableList.copyOf(blocks);
	}
	
	public WorldGenRockBoulders()
	{
		this(GenesisBlocks.granite.getDefaultState(), GenesisBlocks.mossy_granite.getDefaultState());
	}
	
	@Override
	protected boolean doGenerate(World world, Random random, BlockPos pos)
	{
		if (waterRequired && !WorldUtils.waterInRange(world, pos, 1, 1, 1))
			return false;
		
		if (inGround)
			pos = pos.down();
		
		int maxHeight = 2 + random.nextInt(this.maxHeight - 1);
		
		generateRockColumn(world, pos, random, maxHeight);
		
		for (EnumFacing side : EnumFacing.HORIZONTALS)
		{
			if (random.nextInt(100) > 15)
			{
				BlockPos sidePos = pos.offset(side);
				generateRockColumn(world, sidePos, random, 1 + random.nextInt(maxHeight - random.nextInt(2)));
				
				if (random.nextInt(10) > 5)
					generateRockColumn(world, sidePos.offset(side.rotateY()), random, 1 + random.nextInt(maxHeight - random.nextInt(2)));
			}
		}
		
		return true;
	}
	
	public WorldGenRockBoulders setInGround(boolean in)
	{
		inGround = in;
		return this;
	}
	
	public WorldGenRockBoulders setMaxHeight(int mxHeight)
	{
		maxHeight = mxHeight;
		return this;
	}
	
	public WorldGenRockBoulders setWaterRequired(boolean required)
	{
		waterRequired = required;
		return this;
	}
	
	private void generateRockColumn(World world, BlockPos pos, Random rand, int height)
	{
		BlockPos rockPos = pos;
		
		for (int i = 1; i <= height; ++i)
		{
			setBlockInWorld(world, rockPos, blocks.get(rand.nextInt(blocks.size())), true);
			rockPos = rockPos.up();
		}
	}
}