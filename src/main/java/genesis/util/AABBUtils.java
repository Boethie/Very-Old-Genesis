package genesis.util;

import net.minecraft.util.AxisAlignedBB;
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
}
