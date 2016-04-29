package genesis.util;

import net.minecraft.util.math.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;

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
	
	public static AxisAlignedBB rotate(AxisAlignedBB bb, Rotation rot)
	{
		switch (rot)
		{
		case NONE:
			return bb;
		case CLOCKWISE_90:
			return new AxisAlignedBB(-bb.minZ, bb.minY, bb.minX,
									-bb.maxZ, bb.maxY, bb.maxX);
		case CLOCKWISE_180:
			return new AxisAlignedBB(-bb.minX, bb.minY, -bb.minZ,
									-bb.maxX, bb.maxY, -bb.maxZ);
		case COUNTERCLOCKWISE_90:
			return new AxisAlignedBB(bb.minZ, bb.minY, -bb.minX,
									bb.maxZ, bb.maxY, -bb.maxX);
		}
		
		throw new IllegalArgumentException("Unknown rotation " + rot);
	}
	
	public static AxisAlignedBB rotateTo(AxisAlignedBB bb, Vec3d center, EnumFacing facing)
	{
		if (facing.getAxis() == Axis.Y)
			throw new IllegalArgumentException("Cannot rotate an bounding box to UP or DOWN");
		
		bb = bb.offset(-center.xCoord, -center.yCoord, -center.zCoord);
		
		switch (facing)
		{
		case NORTH:
			break;
		case EAST:
			bb = rotate(bb, Rotation.CLOCKWISE_90);
			break;
		case SOUTH:
			bb = rotate(bb, Rotation.CLOCKWISE_180);
			break;
		case WEST:
			bb = rotate(bb, Rotation.COUNTERCLOCKWISE_90);
			break;
		default:
			throw new IllegalArgumentException("Unknown facing " + facing);
		}
		
		return bb.offset(center.xCoord, center.yCoord, center.zCoord);
	}
	
	private static final Vec3d BLOCK_CENTER = new Vec3d(0.5, 0.5, 0.5);
	
	public static AxisAlignedBB rotateTo(AxisAlignedBB bb, EnumFacing facing)
	{
		return rotateTo(bb, BLOCK_CENTER, facing);
	}
}
