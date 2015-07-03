package genesis.world.gen.feature;

import genesis.common.Genesis;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public abstract class WorldGenTreeBase extends WorldGenAbstractTree
{
	IBlockState wood;
	IBlockState leaves;
	protected boolean notify;
	
	protected int minHeight;
    protected int maxHeight;
	
	public WorldGenTreeBase(IBlockState wood, IBlockState leaves, boolean notify)
	{
		super(notify);
		
		this.wood = wood;
		this.leaves = leaves;
		this.notify = notify;
	}
	
	@Override
	public abstract boolean generate(World worldIn, Random rand, BlockPos pos);
	
	protected void generateLeafLayerCircle(World world, Random random, double radius, int xo, int zo, int h)
	{
		for (int x = (int) Math.ceil(xo - radius); x <= (int) Math.ceil(xo + radius); x++)
		{
			for (int z = (int) Math.ceil(zo - radius); z <= (int) Math.ceil(zo + radius); z++)
			{
				double xfr = z - zo;
				double zfr = x - xo;
				
				if (xfr * xfr + zfr * zfr <= radius * radius)
				{
					setBlockInWorld(world, new BlockPos(x, h, z), leaves);
				}
			}
		}
	}
	
	protected void setBlockInWorld(World world, BlockPos pos, IBlockState state)
	{
		if (state == wood &&
					(world.getBlockState(pos).getBlock().isAir(world, pos) 
					|| world.getBlockState(pos).getBlock().getMaterial().isReplaceable() 
					|| world.getBlockState(pos).getBlock().isLeaves(world, pos)))
		{
			if (notify)
			{
				world.setBlockState(pos, state, 3);
			}
			else
			{
				world.setBlockState(pos, state, 2);
			}
		}
		else if (state == leaves && world.getBlockState(pos).getBlock().isAir(world, pos))
		{
			if (notify)
			{
				world.setBlockState(pos, state, 3);
			}
			else
			{
				world.setBlockState(pos, state, 2);
			}
		}
	}
	
	protected boolean isCubeClear(World world, BlockPos pos, int radius, int height)
	{
		Iterable<BlockPos> posList = (Iterable<BlockPos>) BlockPos.getAllInBox(pos.add(-radius, 0, -radius), pos.add(radius, height, radius));
		
		for (BlockPos checkPos : posList)
		{
			if (!world.isAirBlock(checkPos) && !world.getBlockState(checkPos).getBlock().isLeaves(world, pos))
			{
				return false;
			}
		}
		
		return true;
	}
}
