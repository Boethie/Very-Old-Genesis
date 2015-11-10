package genesis.util;

import net.minecraft.util.AxisAlignedBB;

public class BoundingBoxHelpers
{
	public static AxisAlignedBB subCoord(AxisAlignedBB bb, double x, double y, double z)
	{
		if (x == 0 && y == 0 && z == 0)
		{
			return bb;
		}
		
		double minX = bb.minX;
		double minY = bb.minY;
		double minZ = bb.minZ;
		double maxX = bb.maxX;
		double maxY = bb.maxY;
		double maxZ = bb.maxZ;
		
		if (x > 0)
			minX -= x;
		else
			maxX -= x;
		
		if (y > 0)
			minY -= y;
		else
			maxY -= y;
		
		if (z > 0)
			minZ -= z;
		else
			maxZ -= z;
		
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
