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
		for (EnumFacing facing : EnumFacing.values())
		{
			if (facing.getAxis() == axis && facing.getAxisDirection() == dir)
			{
				return facing;
			}
		}
		
		return null;
	}
	
	public static EnumFacing getRandomFacing(Axis axis, Random random)
	{
		return getFacing(axis, AxisDirection.values()[random.nextInt(AxisDirection.values().length)]);
	}
	
	public static EnumFacing getFacingAlongAxis(Axis axis, float angle)
	{
		angle = (float) Math.toRadians(angle + 90);
		AxisDirection dir = NEGATIVE;
		double x = MathHelper.cos(angle);
		double z = MathHelper.sin(angle);
		
		switch (axis)
		{
		case X:
			dir = x > 0 ? POSITIVE : NEGATIVE;
			break;
		case Z:
			dir = z > 0 ? POSITIVE : NEGATIVE;
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
			{
				return axis;
			}
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
	
	public static EnumFacing getPositiveFacing(Axis axis)
	{
		return getFacing(axis, POSITIVE);
	}
	
	public static EnumFacing getNegativeFacing(Axis axis)
	{
		return getFacing(axis, NEGATIVE);
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
}
