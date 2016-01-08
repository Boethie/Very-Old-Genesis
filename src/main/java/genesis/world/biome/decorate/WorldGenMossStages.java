package genesis.world.biome.decorate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenMossStages extends WorldGenDecorationBase
{
	private List<IBlockState> allowedBlocks = new ArrayList<IBlockState>();
	
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
		
		if (
				!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt)
				&& !(allowedBlocks.contains(world.getBlockState(pos))))
			return false;
		
		boolean generated = false;
		
		if (setMoss(world, pos, random))
			generated = true;
		
		int mossCount = (this.getPatchSize() <= 1)? 64 : this.getPatchSize();
		
		for (int i = 0; i < mossCount; ++i)
			setMoss(world, pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3), random);
		
		return generated;
	}
	
	private boolean setMoss(World world, BlockPos pos, Random rand)
	{
		if (
				world.getBlockState(pos).getBlock() != GenesisBlocks.moss 
				&& world.getBlockState(pos).getBlock() != Blocks.dirt 
				&& !(allowedBlocks.contains(world.getBlockState(pos))))
			return false;
		
		int stage = GenesisBlocks.moss.getTargetStage(GenesisBlocks.moss.getFertility(world, pos, true), rand);
		
		if (stage >= 0)
		{
			setBlockInWorld(world, pos, GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, stage), true);
		}
		
		return true;
	}
	
	public WorldGenMossStages addAllowedBlocks(IBlockState... states)
	{
		for (int i = 0; i < states.length; ++i)
		{
			allowedBlocks.add(states[i]);
		}
		
		return this;
	}
}
