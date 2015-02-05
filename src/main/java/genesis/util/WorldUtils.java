package genesis.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldUtils
{
	/**
	 * @param worldIn The world.
	 * @param pos The position to start from.
	 * @param dHoriz The horizontal distance to check from the starting block position.
	 * @param dVert The vertical distance to check from the starting block position.
	 * @return Whether there is water in range.
	 */
	public static boolean waterInRange(World worldIn, BlockPos pos, int dHoriz, int dVert)
	{
		Iterable<BlockPos> checkArea = (Iterable<BlockPos>) BlockPos.getAllInBox(pos.add(-dHoriz, -1 - dVert, -dHoriz), pos.add(dHoriz, -1 + dVert, dHoriz));
		
		for (BlockPos checkPos : checkArea)
		{
			if (worldIn.getBlockState(checkPos).getBlock().getMaterial() == Material.water)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static List<IBlockState> getBlocksAround(final World world, final BlockPos pos){
		final List<IBlockState> blocks = new ArrayList<IBlockState>();

		BlockPos shift = pos.north();
		blocks.add(world.getBlockState(shift));

		shift = pos.north();
		shift = shift.east();
		blocks.add(world.getBlockState(shift));

		shift = pos.east();
		blocks.add(world.getBlockState(shift));

		shift = pos.east();
		shift = shift.south();
		blocks.add(world.getBlockState(shift));

		shift = pos.south();
		blocks.add(world.getBlockState(shift));

		shift = pos.south();
		shift = pos.west();
		blocks.add(world.getBlockState(shift));

		shift = pos.west();
		blocks.add(world.getBlockState(shift));

		shift = pos.west();
		shift = shift.north();
		blocks.add(world.getBlockState(shift));

		return blocks;
	}
}
