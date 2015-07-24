package genesis.world.gen.feature;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public abstract class WorldGenTreeBase extends WorldGenAbstractTree
{
	IBlockState wood;
	IBlockState leaves;
	protected boolean notify;
	
	protected int minHeight;
    protected int maxHeight;
	
    private int treeCountPerChunk = 0;
    
	public WorldGenTreeBase(IBlockState wood, IBlockState leaves, boolean notify)
	{
		super(notify);
		
		this.wood = wood;
		this.leaves = leaves;
		this.notify = notify;
	}
	
	public WorldGenTreeBase setTreeCountPerChunk(int count)
	{
		treeCountPerChunk = count;
		return this;
	}
	
	public int getTreeCountPerChunk()
	{
		return treeCountPerChunk;
	}
	
	@Override
	public abstract boolean generate(World world, Random rand, BlockPos pos);
	
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
		setBlockInWorld(world, pos, state, false);
	}
	
	protected void setBlockInWorld(World world, BlockPos pos, IBlockState state, boolean force)
	{
		boolean place = true;
		
		if (
				state == wood 
				&& !(world.getBlockState(pos).getBlock().isAir(world, pos) 
						|| world.getBlockState(pos).getBlock().getMaterial().isReplaceable()
						|| world.getBlockState(pos).getBlock().isLeaves(world, pos))
				&& !force)
		{
			place = false;
		}
		
		if (
				state == leaves 
				&& !world.getBlockState(pos).getBlock().isAir(world, pos)
				&& !force)
		{
			place = false;
		}
		
		if (place)
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
	
	public void doBranchLeaves(World world, BlockPos pos, Random random, boolean cap, int length)
	{
		for (int i = 1; i <= length; ++i)
		{
			setBlockInWorld(world, pos.north(i), leaves);
			setBlockInWorld(world, pos.north(i - 1).east(), leaves);
			setBlockInWorld(world, pos.north(i - 1).west(), leaves);
			
			setBlockInWorld(world, pos.south(i), leaves);
			setBlockInWorld(world, pos.south(i - 1).east(), leaves);
			setBlockInWorld(world, pos.south(i - 1).west(), leaves);
			
			setBlockInWorld(world, pos.east(i), leaves);
			setBlockInWorld(world, pos.east(i - 1).north(), leaves);
			setBlockInWorld(world, pos.east(i - 1).south(), leaves);
			
			setBlockInWorld(world, pos.west(i), leaves);
			setBlockInWorld(world, pos.west(i - 1).north(), leaves);
			setBlockInWorld(world, pos.west(i - 1).south(), leaves);
		}
		
		if (cap)
		{
			setBlockInWorld(world, pos.up(1), leaves);
			setBlockInWorld(world, pos.up(1).north(), leaves);
			setBlockInWorld(world, pos.up(1).south(), leaves);
			setBlockInWorld(world, pos.up(1).east(), leaves);
			setBlockInWorld(world, pos.up(1).west(), leaves);
			setBlockInWorld(world, pos.up(2), leaves);
		}
	}
	
	public void doPineTopLeaves(World world, BlockPos genPos, BlockPos branchPos, int treeHeight, int leavesBase, Random rand, boolean alternate)
	{
		doPineTopLeaves(world, genPos, branchPos, treeHeight, leavesBase, rand, alternate, 4);
	}
	
	public void doPineTopLeaves(World world, BlockPos genPos, BlockPos branchPos, int treeHeight, int leavesBase, Random rand, boolean alternate, int maxLeaveLength)
	{
		boolean alt = false;
		float percent;
		int leaves;
		
		doBranchLeaves(world, branchPos, rand, true, 1);
		
		while (branchPos.getY() > leavesBase)
		{
			branchPos = branchPos.add(0, -1, 0);
			
			percent = 1.0F - (((float)branchPos.getY() - (float)leavesBase) / ((float)genPos.getY() + (float)treeHeight - (float)leavesBase));
			leaves = MathHelper.ceiling_float_int((float)maxLeaveLength * percent);
			
			if (leaves > maxLeaveLength)
				leaves = maxLeaveLength;
			
			if (alt || !alternate)
				doBranchLeaves(world, branchPos, rand, false, leaves);
			
			alt = !alt;
		}
	}
}
