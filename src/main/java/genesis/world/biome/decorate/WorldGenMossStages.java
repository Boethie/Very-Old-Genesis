package genesis.world.biome.decorate;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenMossStages extends WorldGenDecorationBase
{
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
		
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		boolean generated = false;
		
		if (setMoss(world, pos, random))
			generated = true;
		
		for (int i = 0; i < 80; ++i)
			setMoss(world, pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3), random);
		
		return generated;
	}
	
	private boolean setMoss(World world, BlockPos pos, Random rand)
	{
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos))
			return false;
		
		int light = world.getLight(pos.up());
		int stage = (light > 14)? (rand.nextInt(2) == 0)? 1 : 0 : (light > 13)? 2 : 3;
		
		setBlockInWorld(world, pos, GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, stage), true);
		
		return true;
	}
}
