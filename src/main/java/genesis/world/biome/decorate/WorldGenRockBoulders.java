package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenRockBoulders extends WorldGenDecorationBase
{
	private List<IBlockState> blocks = new ArrayList<IBlockState>();
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block;
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (!block.isLeaves(world, pos) && !block.isLeaves(world, pos))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos))
			return false;
		
		boolean water_exists = findBlockInRange(world, pos, Blocks.water.getDefaultState(), 1, 1, 1);
		
		if (!water_exists)
			return false;
		
		if (blocks.size() == 0)
		{
			blocks.add(GenesisBlocks.granite.getDefaultState());
			blocks.add(GenesisBlocks.mossy_granite.getDefaultState());
		}
		
		generateRockColumn(world, pos, random);
		
		if (random.nextInt(100) > 20)
		{
			generateRockColumn(world, pos.add(1, 0, 0), random);
			if (random.nextInt(10) > 5)
				generateRockColumn(world, pos.add(1, 0, 1), random);
		}
		
		if (random.nextInt(100) > 20)
		{
			generateRockColumn(world, pos.add(-1, 0, 0), random);
			if (random.nextInt(10) > 5)
				generateRockColumn(world, pos.add(-1, 0, -1), random);
		}
		
		if (random.nextInt(100) > 20)
		{
			generateRockColumn(world, pos.add(0, 0, 1), random);
			if (random.nextInt(10) > 5)
				generateRockColumn(world, pos.add(-1, 0, 1), random);
		}
		
		if (random.nextInt(100) > 20)
		{
			generateRockColumn(world, pos.add(0, 0, -1), random);
			if (random.nextInt(10) > 5)
				generateRockColumn(world, pos.add(1, 0, -1), random);
		}
		
		return true;
	}
	
	public WorldGenDecorationBase addBlocks(IBlockState... blockTypes)
	{
		for (int i = 0; i < blockTypes.length; ++i)
		{
			blocks.add(blockTypes[i]);
		}
		
		return this;
	}
	
	private void generateRockColumn(World world, BlockPos pos, Random rand)
	{
		BlockPos rockPos = pos;
		
		setBlockInWorld(world, rockPos, blocks.get(rand.nextInt(blocks.size())), true);
		
		if (rand.nextInt(10) > 1)
		{
			rockPos = rockPos.up();
			setBlockInWorld(world, rockPos, blocks.get(rand.nextInt(blocks.size())), true);
			
			if (rand.nextInt(10) > 4)
			{
				rockPos = rockPos.up();
				setBlockInWorld(world, rockPos, blocks.get(rand.nextInt(blocks.size())), true);
				
				if (rand.nextInt(10) > 8)
				{
					rockPos = rockPos.up();
					setBlockInWorld(world, rockPos, blocks.get(rand.nextInt(blocks.size())), true);
				}
			}
		}
	}
}
