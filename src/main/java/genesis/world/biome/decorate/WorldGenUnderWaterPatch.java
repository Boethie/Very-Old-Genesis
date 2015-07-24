package genesis.world.biome.decorate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenUnderWaterPatch extends WorldGenDecorationBase
{
	private List<IBlockState> blocks = new ArrayList<IBlockState>();
	
	public WorldGenUnderWaterPatch(IBlockState... block)
	{
		for (int i = 0; i < block.length; ++i)
		{
			blocks.add(block[i]);
		}
	}
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block;
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (!block.isAir(world, pos) && !block.isLeaves(world, pos) && !(block == Blocks.water))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		boolean placedSome = false;
		
		if(placeSmallPatch(world, pos, random))
			placedSome = true;
		
		if (random.nextInt(2) == 0)
			if(placeSmallPatch(world, pos.add(1, 0, 1), random))
				placedSome = true;
		
		if (random.nextInt(2) == 0)
			if(placeSmallPatch(world, pos.add(1, 0, -1), random))
				placedSome = true;
		
		if (random.nextInt(2) == 0)
			if(placeSmallPatch(world, pos.add(-1, 0, 1), random))
				placedSome = true;
		
		if (random.nextInt(2) == 0)
			if(placeSmallPatch(world, pos.add(-1, 0, -1), random))
				placedSome = true;
		
		return placedSome;
	}
	
	private boolean placeSmallPatch(World world, BlockPos pos, Random random)
	{
		if (!(
				world.getBlockState(pos.up()).getBlock() == Blocks.water
				&& world.getBlockState(pos).getBlock() != Blocks.water))
			return false;
		
		setBlockInWorld(world, pos, blocks.get(random.nextInt(blocks.size())), true);
		if (world.getBlockState(pos.north()).getBlock() != Blocks.water)
			setBlockInWorld(world, pos.north(), blocks.get(random.nextInt(blocks.size())), true);
		if (world.getBlockState(pos.south()).getBlock() != Blocks.water)
			setBlockInWorld(world, pos.south(), blocks.get(random.nextInt(blocks.size())), true);
		if (world.getBlockState(pos.east()).getBlock() != Blocks.water)
			setBlockInWorld(world, pos.east(), blocks.get(random.nextInt(blocks.size())), true);
		if (world.getBlockState(pos.west()).getBlock() != Blocks.water)
			setBlockInWorld(world, pos.west(), blocks.get(random.nextInt(blocks.size())), true);
		
		return true;
	}
}
