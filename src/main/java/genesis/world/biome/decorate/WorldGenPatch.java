package genesis.world.biome.decorate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenPatch extends WorldGenDecorationBase
{
	private List<IBlockState> blockCollection = new ArrayList<IBlockState>();
	private List<BlockState> allowedBlocks = new ArrayList<BlockState>();
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block;
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (!block.isAir(world, pos) && !block.isLeaves(world, pos))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		if (!(allowedBlocks.contains(world.getBlockState(pos).getBlock().getBlockState())))
			return false;
		
		boolean generated = false;
		
		if (setBlock(world, pos, random))
			generated = true;
		
		for (int i = 0; i < 64; ++i)
			setBlock(world, pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3), random);
		
		return generated;
	}
	
	private boolean setBlock(World world, BlockPos pos, Random rand)
	{
		if (!(allowedBlocks.contains(world.getBlockState(pos).getBlock().getBlockState())))
			return false;
		
		if (blockCollection.size() == 0)
			return false;
		
		setBlockInWorld(world, pos, blockCollection.get(rand.nextInt(blockCollection.size())), true);
		
		return true;
	}
	
	public WorldGenPatch addBlocks(IBlockState... blocks)
	{
		for (int i = 0; i < blocks.length; ++i)
		{
			blockCollection.add(blocks[i]);
		}
		
		return this;
	}
	
	public WorldGenPatch addAllowedBlocks(BlockState... blocks)
	{
		for (int i = 0; i < blocks.length; ++i)
		{
			allowedBlocks.add(blocks[i]);
		}
		
		return this;
	}
}
