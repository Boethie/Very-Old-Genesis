package genesis.util;

public class GenesisMath
{
	public static double lerp(double a, double b, double v)
	{
		return a + (b - a) * v;
	}
	
	public static float lerp(float a, float b, float v)
	{
		return a + (b - a) * v;
	}
	
	public static int lerp(int a, int b, float v)
	{
		return a + Math.round((b - a) * v);
	}
}
