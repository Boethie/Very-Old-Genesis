package genesis.util;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AABBUtils
{
	public static AxisAlignedBB offset(AxisAlignedBB bb, EnumFacing facing, double distance)
	{
		return bb.offset(facing.getFrontOffsetX() * distance,
						facing.getFrontOffsetY() * distance,
						facing.getFrontOffsetZ() * distance);
	}
	
	public static AxisAlignedBB extend(AxisAlignedBB bb, EnumFacing facing, double distance)
	{
		return bb.addCoord(facing.getFrontOffsetX() * distance,
							facing.getFrontOffsetY() * distance,
							facing.getFrontOffsetZ() * distance);
	}
	
	public static AxisAlignedBB expand(AxisAlignedBB bb, EnumFacing facing, double distance)
	{
		return bb.expand(Math.abs(facing.getFrontOffsetX() * distance),
						Math.abs(facing.getFrontOffsetY() * distance),
						Math.abs(facing.getFrontOffsetZ() * distance));
	}
	
	public static AxisAlignedBB expandSides(AxisAlignedBB bb, EnumFacing facing, double distance)
	{
		switch (facing.getAxis())
		{
		case X:
			return bb.expand(0, distance, distance);
		case Y:
			return bb.expand(distance, 0, distance);
		case Z:
			return bb.expand(distance, distance, 0);
		}
		
		throw new IllegalArgumentException("Unknown axis " + facing.getAxis());
	}
	
	public static void addIfIntersects(List<AxisAlignedBB> list, AxisAlignedBB mask, AxisAlignedBB bb)
	{
		if (mask.intersectsWith(bb))
			list.add(bb);
	}
	
	public static void addBlockBounds(List<AxisAlignedBB> list, AxisAlignedBB mask, AxisAlignedBB bb, BlockPos pos)
	{
		addIfIntersects(list, mask, bb.offset(pos.getX(), pos.getY(), pos.getZ()));
	}
}
