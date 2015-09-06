package genesis.util;

public class GenesisMath
{
	public static double lerp(double a, double b, double v)
	{
		if (v == 0)
			return a;
		else if (v == 1)
			return b;
		
		return a + (b - a) * v;
	}
	
	public static float lerp(float a, float b, float v)
	{
		if (v == 0)
			return a;
		else if (v == 1)
			return b;
		
		return a + (b - a) * v;
	}
	
	public static int lerp(int a, int b, float v)
	{
		if (v == 0)
			return a;
		else if (v == 1)
			return b;
		
		return a + Math.round((b - a) * v);
	}
}
