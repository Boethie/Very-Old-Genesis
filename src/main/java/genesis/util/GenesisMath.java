package genesis.util;

import net.minecraft.util.math.Vec3d;

public final class GenesisMath
{
	public static final double lerp(double a, double b, double v)
	{
		if (v == 0)
			return a;
		else if (v == 1)
			return b;
		
		return a + (b - a) * v;
	}
	
	public static final float lerp(float a, float b, float v)
	{
		if (v == 0)
			return a;
		else if (v == 1)
			return b;
		
		return a + (b - a) * v;
	}
	
	public static final int lerp(int a, int b, float v)
	{
		if (v == 0)
			return a;
		else if (v == 1)
			return b;
		
		return a + Math.round((b - a) * v);
	}
	
	public static final Vec3d lerp(Vec3d a, Vec3d b, double v)
	{
		if (v == 0)
			return a;
		else if (v == 1)
			return b;
		
		return new Vec3d(lerp(a.xCoord, b.xCoord, v),
						lerp(a.yCoord, b.yCoord, v),
						lerp(a.zCoord, b.zCoord, v));
	}
	
	public static final int ceilDiv(int a, int b)
	{
		return (a + b - 1) / b;
	}
}
