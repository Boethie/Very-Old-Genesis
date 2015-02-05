package genesis.util;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldUtils
{
	/**
	 * @param worldIn
	 *            The world.
	 * @param pos
	 *            The position to start from.
	 * @param dHoriz
	 *            The horizontal distance to check from the starting block position.
	 * @param dVert
	 *            The vertical distance to check from the starting block position.
	 * @return Whether there is water in range.
	 */
	public static boolean waterInRange(World worldIn, BlockPos pos, int dHoriz, int dVert)
	{
		Iterable<BlockPos> checkArea = BlockPos.getAllInBox(pos.add(-dHoriz, -1 - dVert, -dHoriz), pos.add(dHoriz, -1 + dVert, dHoriz));

		for (BlockPos checkPos : checkArea)
		{
			if (worldIn.getBlockState(checkPos).getBlock().getMaterial() == Material.water)
			{
				return true;
			}
		}

		return false;
	}
}
