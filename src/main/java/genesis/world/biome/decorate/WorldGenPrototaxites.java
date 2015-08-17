package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenPrototaxites extends WorldGenDecorationBase
{
	private IBlockState baseBlock = GenesisBlocks.prototaxites_mycelium.getDefaultState();
	private IBlockState bodyBlock = GenesisBlocks.prototaxites.getDefaultState();
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		if (random.nextInt(4) != 0)
			return false;
		
		Block block;
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			
			if (!block.isAir(world, pos) && !block.isLeaves(world, pos) /*&& !(block == Blocks.water)*/)
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		boolean placedSome = false;
		
		if (placeMycelliumBase(world, pos, random))
			placedSome = true;
		
		if (placedSome)
		{
			placeMycelliumBase(world, pos.north(), random);
			placeMycelliumBase(world, pos.south(), random);
			placeMycelliumBase(world, pos.east(), random);
			placeMycelliumBase(world, pos.west(), random);
			
			int size = 1 + random.nextInt(5);
			
			for (int i = 1; i <= size; ++i)
			{
				setBlockInWorld(world, pos.up(i), bodyBlock, true);
			}
		}
		
		return placedSome;
	}
	
	private boolean placeMycelliumBase(World world, BlockPos pos, Random rand)
	{
		if (!(
				world.getBlockState(pos).getBlock() == Blocks.dirt
				|| world.getBlockState(pos).getBlock() == GenesisBlocks.moss)
			|| !(world.getBlockState(pos.up()).getBlock().isAir(world, pos)))
			return false;
		
		setBlockInWorld(world, pos, baseBlock, true);
		
		if (!world.getBlockState(pos.north()).getBlock().isAir(world, pos) && rand.nextInt(2) == 0)
			setBlockInWorld(world, pos.north(), baseBlock, true);
		if (!world.getBlockState(pos.south()).getBlock().isAir(world, pos) && rand.nextInt(2) == 0)
			setBlockInWorld(world, pos.south(), baseBlock, true);
		if (!world.getBlockState(pos.east()).getBlock().isAir(world, pos) && rand.nextInt(2) == 0)
			setBlockInWorld(world, pos.east(), baseBlock, true);
		if (!world.getBlockState(pos.west()).getBlock().isAir(world, pos) && rand.nextInt(2) == 0)
			setBlockInWorld(world, pos.west(), baseBlock, true);
		
		return true;
	}
}
