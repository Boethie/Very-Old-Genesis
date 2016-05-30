package genesis.util;

import net.minecraft.util.math.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;

public class AABBUtils
{
	public static AxisAlignedBB create(Vec3i pos)
	{
		return create((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
	}

	public static AxisAlignedBB create(Vec3d pos)
	{
		return create(pos.xCoord, pos.yCoord, pos.zCoord);
	}

	public static AxisAlignedBB create(double x, double y, double z)
	{
		return new AxisAlignedBB(x, y, z, x, y, z);
	}

	public static AxisAlignedBB offset(AxisAlignedBB bb, Vec3d pos)
	{
		return bb.offset(pos.xCoord, pos.yCoord, pos.zCoord);
	}

	public static AxisAlignedBB offset(AxisAlignedBB bb, EnumFacing facing, double distance)
	{
		return bb.offset(facing.getFrontOffsetX() * distance,
						facing.getFrontOffsetY() * distance,
						facing.getFrontOffsetZ() * distance);
	}

	public static AxisAlignedBB offset(AxisAlignedBB bb, EnumFacing.Plane plane, double distance)
	{
		for (EnumFacing facing : plane.facings())
		{
			bb = offset(bb, facing, distance);
		}

		return bb;
	}

	public static AxisAlignedBB extend(AxisAlignedBB bb, Vec3i pos)
	{
		return bb.addCoord((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
	}

	public static AxisAlignedBB extend(AxisAlignedBB bb, Vec3d pos)
	{
		return bb.addCoord(pos.xCoord, pos.yCoord, pos.zCoord);
	}
	
	public static AxisAlignedBB extend(AxisAlignedBB bb, EnumFacing facing, double distance)
	{
		return bb.addCoord(facing.getFrontOffsetX() * distance,
							facing.getFrontOffsetY() * distance,
							facing.getFrontOffsetZ() * distance);
	}

	public static AxisAlignedBB extend(AxisAlignedBB bb, EnumFacing.Plane plane, double distance)
	{
		for (EnumFacing facing : plane.facings())
		{
			bb = extend(bb, facing, distance);
		}

		return bb;
	}

	public static AxisAlignedBB expand(AxisAlignedBB bb, Vec3i pos)
	{
		return bb.expand((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
	}

	public static AxisAlignedBB expand(AxisAlignedBB bb, Vec3d pos)
	{
		return bb.expand(pos.xCoord, pos.yCoord, pos.zCoord);
	}

	public static AxisAlignedBB expand(AxisAlignedBB bb, EnumFacing facing, double distance)
	{
		return bb.expand(facing.getFrontOffsetX() * distance,
						facing.getFrontOffsetY() * distance,
						facing.getFrontOffsetZ() * distance);
	}

	public static AxisAlignedBB expand(AxisAlignedBB bb, EnumFacing.Plane plane, double distance)
	{
		for (EnumFacing facing : plane.facings())
		{
			bb = expand(bb, facing, distance);
		}

		return bb;
	}

	public static AxisAlignedBB expandSides(AxisAlignedBB bb, EnumFacing.Axis axis, double distance)
	{
		switch (axis)
		{
		case X:
			return bb.expand(0, distance, distance);
		case Y:
			return bb.expand(distance, 0, distance);
		case Z:
			return bb.expand(distance, distance, 0);
		}
		
		throw new IllegalArgumentException("Unknown axis " + axis);
	}

	public static AxisAlignedBB shrink(AxisAlignedBB bb, double x, double y, double z)
	{
		double minX = bb.minX;
		double minY = bb.minY;
		double minZ = bb.minZ;
		double maxX = bb.maxX;
		double maxY = bb.maxY;
		double maxZ = bb.maxZ;

		if (x < 0.0D) minX -= x;
		if (x > 0.0D) maxX -= x;

		if (y < 0.0D) minY -= y;
		if (y > 0.0D) maxY -= y;

		if (z < 0.0D) minZ -= z;
		if (z > 0.0D) maxZ -= z;

		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public static AxisAlignedBB shrink(AxisAlignedBB bb, Vec3i pos)
	{
		return shrink(bb, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
	}

	public static AxisAlignedBB shrink(AxisAlignedBB bb, Vec3d pos)
	{
		return shrink(bb, pos.xCoord, pos.yCoord, pos.zCoord);
	}

	public static AxisAlignedBB shrink(AxisAlignedBB bb, EnumFacing facing, double distance)
	{
		return shrink(bb, facing.getFrontOffsetX() * distance,
				facing.getFrontOffsetY() * distance,
				facing.getFrontOffsetZ() * distance);
	}

	public static AxisAlignedBB shrink(AxisAlignedBB bb, EnumFacing.Plane plane, double distance)
	{
		for (EnumFacing facing : plane.facings())
		{
			bb = shrink(bb, facing, distance);
		}

		return bb;
	}

	public static AxisAlignedBB contract(AxisAlignedBB bb, double x, double y, double z)
	{
		return bb.expand(-x, -y, -z);
	}

	public static AxisAlignedBB contract(AxisAlignedBB bb, Vec3i pos)
	{
		return contract(bb, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
	}

	public static AxisAlignedBB contract(AxisAlignedBB bb, Vec3d pos)
	{
		return contract(bb, pos.xCoord, pos.yCoord, pos.zCoord);
	}

	public static AxisAlignedBB contract(AxisAlignedBB bb, EnumFacing facing, double distance)
	{
		return contract(bb, facing.getFrontOffsetX() * distance,
				facing.getFrontOffsetY() * distance,
				facing.getFrontOffsetZ() * distance);
	}

	public static AxisAlignedBB contract(AxisAlignedBB bb, EnumFacing.Plane plane, double distance)
	{
		for (EnumFacing facing : plane.facings())
		{
			bb = contract(bb, facing, distance);
		}

		return bb;
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
