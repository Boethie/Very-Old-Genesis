package genesis.util;

import java.util.Random;

import net.minecraft.util.*;
import net.minecraft.util.math.*;

import net.minecraft.util.EnumFacing.*;
import static net.minecraft.util.EnumFacing.Axis.*;
import static net.minecraft.util.EnumFacing.AxisDirection.*;

public class FacingHelpers
{
	public static EnumFacing getFacing(Axis axis, AxisDirection dir)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			if (facing.getAxis() == axis && facing.getAxisDirection() == dir)
				return facing;
		}
		
		return null;
	}
	
	public static EnumFacing getRandomFacing(Axis axis, Random random)
	{
		return getFacing(axis, random.nextBoolean() ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE);
	}
	
	public static EnumFacing getFacingAlongAxis(Axis axis, float angle)
	{
		angle = (float) Math.toRadians(angle + 90);
		AxisDirection dir = NEGATIVE;
		
		switch (axis)
		{
		case X:
			dir = MathHelper.cos(angle) > 0 ? POSITIVE : NEGATIVE;
			break;
		case Z:
			dir = MathHelper.sin(angle) > 0 ? POSITIVE : NEGATIVE;
			break;
		default:
			break;
		}
		
		return getFacing(axis, dir);
	}
	
	public static Axis getAxis(String name)
	{
		for (Axis axis : Axis.values())
		{
			if (name.equalsIgnoreCase(axis.getName()))
				return axis;
		}
		
		return null;
	}
	
	public static AxisDirection getAxisDirection(String name)
	{
		for (AxisDirection dir : AxisDirection.values())
		{
			if (name.equalsIgnoreCase(dir.name()))
			{
				return dir;
			}
		}
		
		return null;
	}
	
	private static <T> T throwFor(Axis axis) throws IllegalArgumentException
	{
		throw new IllegalArgumentException("Axis " + axis + " should not exist.");
	}
	
	private static <T> T throwFor(AxisDirection dir) throws IllegalArgumentException
	{
		throw new IllegalArgumentException("AxisDirection " + dir + " should not exist.");
	}
	
	public static AxisDirection getOpposite(AxisDirection dir)
	{
		if (dir == null)
		{
			return null;
		}
		
		switch (dir)
		{
		case NEGATIVE:
			return POSITIVE;
		case POSITIVE:
			return NEGATIVE;
		default:
			return throwFor(dir);
		}
	}
	
	public static Axis rotateY(Axis axis)
	{
		switch (axis)
		{
		case X:
			return Z;
		case Y:
			return Y;
		case Z:
			return X;
		default:
			return throwFor(axis);
		}
	}
	
	private static final Vec3i[] U_VECS;
	private static final Vec3i[] V_VECS;
	
	static
	{
		U_VECS = new Vec3i[EnumFacing.VALUES.length];
		V_VECS = new Vec3i[EnumFacing.VALUES.length];
		int i;
		
		i = EnumFacing.UP.getIndex();
		U_VECS[i] = EnumFacing.EAST.getDirectionVec();
		V_VECS[i] = EnumFacing.SOUTH.getDirectionVec();
		
		i = EnumFacing.DOWN.getIndex();
		U_VECS[i] = EnumFacing.WEST.getDirectionVec();
		V_VECS[i] = EnumFacing.NORTH.getDirectionVec();
		
		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL.facings())
		{
			i = facing.getIndex();
			U_VECS[i] = facing.rotateY().getDirectionVec();
			V_VECS[i] = facing.getDirectionVec().crossProduct(U_VECS[i]);
		}
	}
	
	public static Vec3i getU(EnumFacing facing)
	{
		return U_VECS[facing.getIndex()];
	}
	
	public static Vec3i getV(EnumFacing facing)
	{
		return V_VECS[facing.getIndex()];
	}
}
