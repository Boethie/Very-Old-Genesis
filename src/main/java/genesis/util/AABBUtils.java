package genesis.util;

import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.HashSet;
import net.minecraft.util.math.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.*;
import net.minecraft.util.Rotation;

public class AABBUtils
{
	private static final Vec3d BLOCK_CENTER = new Vec3d(0.5, 0.5, 0.5);
	private static final AxisAlignedBB BLOCK_CENTER_AABB = create(BLOCK_CENTER);
	private static final HashMap<Plane, ImmutableSet<EnumFacing>> POSITIVE_PLANE_FACINGS = new HashMap<>();

	public static AxisAlignedBB create(Vec3i pos)
	{
		return create(pos.getX(), pos.getY(), pos.getZ());
	}

	public static AxisAlignedBB create(Vec3d pos)
	{
		return create(pos.xCoord, pos.yCoord, pos.zCoord);
	}

	public static AxisAlignedBB create(double x, double y, double z)
	{
		return new AxisAlignedBB(x, y, z, x, y, z);
	}

	public static AxisAlignedBB createCenter()
	{
		return BLOCK_CENTER_AABB;
	}

	public static AxisAlignedBB createCenterExpansion(Vec3d pos)
	{
		return expand(BLOCK_CENTER_AABB, pos);
	}

	public static AxisAlignedBB createCenterExpansion(double x, double y, double z)
	{
		return BLOCK_CENTER_AABB.expand(x, y, z);
	}

	public static AxisAlignedBB createCenterExpansion(double distance)
	{
		return BLOCK_CENTER_AABB.expandXyz(distance);
	}

	public static AxisAlignedBB createExpansion(Vec3i pos, Vec3d expand)
	{
		return expand(create(pos), expand);
	}

	public static AxisAlignedBB createExpansion(Vec3d pos, Vec3d expand)
	{
		return expand(create(pos), expand);
	}

	public static AxisAlignedBB createExpansion(double x, double y, double z, Vec3d expand)
	{
		return expand(create(x, y, z), expand);
	}

	public static AxisAlignedBB createExpansion(Vec3i pos, EnumFacing facing, double distance)
	{
		return expand(create(pos), facing, distance);
	}

	public static AxisAlignedBB createExpansion(Vec3d pos, EnumFacing facing, double distance)
	{
		return expand(create(pos), facing, distance);
	}

	public static AxisAlignedBB createExpansion(double x, double y, double z, EnumFacing facing, double distance)
	{
		return expand(create(x, y, z), facing, distance);
	}

	public static AxisAlignedBB createExpansion(Vec3i pos, Plane plane, double distance)
	{
		return expand(create(pos), plane, distance);
	}

	public static AxisAlignedBB createExpansion(Vec3d pos, Plane plane, double distance)
	{
		return expand(create(pos), plane, distance);
	}

	public static AxisAlignedBB createExpansion(double x, double y, double z, Plane plane, double distance)
	{
		return expand(create(x, y, z), plane, distance);
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

	public static AxisAlignedBB offset(AxisAlignedBB bb, Plane plane, double distance)
	{
		for (EnumFacing facing : POSITIVE_PLANE_FACINGS.get(plane))
		{
			bb = offset(bb, facing, distance);
		}

		return bb;
	}

	public static AxisAlignedBB extend(AxisAlignedBB bb, Vec3i pos)
	{
		return bb.addCoord(pos.getX(), pos.getY(), pos.getZ());
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

	public static AxisAlignedBB extend(AxisAlignedBB bb, Plane plane, double distance)
	{
		for (EnumFacing facing : POSITIVE_PLANE_FACINGS.get(plane))
		{
			bb = extend(bb, facing, distance);
		}

		return bb;
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

	public static AxisAlignedBB expand(AxisAlignedBB bb, Plane plane, double distance)
	{
		for (EnumFacing facing : POSITIVE_PLANE_FACINGS.get(plane))
		{
			bb = expand(bb, facing, distance);
		}

		return bb;
	}

	public static AxisAlignedBB expandSides(AxisAlignedBB bb, Axis axis, double distance)
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
		return shrink(bb, pos.getX(), pos.getY(), pos.getZ());
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

	public static AxisAlignedBB shrink(AxisAlignedBB bb, Plane plane, double distance)
	{
		for (EnumFacing facing : POSITIVE_PLANE_FACINGS.get(plane))
		{
			bb = shrink(bb, facing, distance);
		}

		return bb;
	}

	public static AxisAlignedBB contract(AxisAlignedBB bb, double x, double y, double z)
	{
		return bb.expand(-x, -y, -z);
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

	public static AxisAlignedBB contract(AxisAlignedBB bb, Plane plane, double distance)
	{
		for (EnumFacing facing : POSITIVE_PLANE_FACINGS.get(plane))
		{
			bb = contract(bb, facing, distance);
		}

		return bb;
	}

	public static AxisAlignedBB contractSides(AxisAlignedBB bb, Axis axis, double distance)
	{
		switch (axis)
		{
			case X:
				return contract(bb, 0, distance, distance);
			case Y:
				return contract(bb, distance, 0, distance);
			case Z:
				return contract(bb, distance, distance, 0);
		}

		throw new IllegalArgumentException("Unknown axis " + axis);
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

	public static AxisAlignedBB rotateTo(AxisAlignedBB bb, EnumFacing facing)
	{
		return rotateTo(bb, BLOCK_CENTER, facing);
	}

	static
	{
		for (Plane plane : Plane.values())
		{
			HashSet<EnumFacing> facings = new HashSet<>();

			for (Axis axis : Axis.values())
			{
				if (axis.getPlane() == plane)
				{
					facings.add(EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, axis));
				}
			}

			POSITIVE_PLANE_FACINGS.put(plane, ImmutableSet.copyOf(facings));
		}
	}
}
