package genesis.util.math;

import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class Vectors
{
	private Vectors() {}
	
	public static Vec3d randomDirection(Random rnd, Vec3d baseVec, Vec3d spread)
	{
		double x = baseVec.xCoord + rnd.nextDouble()*spread.xCoord - spread.xCoord*0.5;
		double y = baseVec.yCoord + rnd.nextDouble()*spread.yCoord - spread.yCoord*0.5;
		double z = baseVec.zCoord + rnd.nextDouble()*spread.zCoord - spread.zCoord*0.5;
		
		double lenSq = x*x + y*y + z*z;
		double lenInv = 1.0 / Math.sqrt(lenSq);
		
		return new Vec3d(x*lenInv, y*lenInv, z*lenInv);
	}
	
	public static Vec3d randomGaussianDirection(Random rnd, Vec3d baseVec, Vec3d spread)
	{
		double x = baseVec.xCoord + MathHelper.clamp_double(rnd.nextGaussian(), -2, 2)*spread.xCoord;
		double y = baseVec.yCoord + MathHelper.clamp_double(rnd.nextGaussian(), -2, 2)*spread.yCoord;
		double z = baseVec.zCoord + MathHelper.clamp_double(rnd.nextGaussian(), -2, 2)*spread.zCoord;
		
		double lenSq = x*x + y*y + z*z;
		double lenInv = 1.0 / Math.sqrt(lenSq);
		
		return new Vec3d(x*lenInv, y*lenInv, z*lenInv);
	}
	
	public static Vec3d randomizedMirroredXZ(Random rand, Vec3d branchDir, Vec3d spread)
	{
		return randomGaussianDirection(rand,
				new Vec3d(-branchDir.xCoord, branchDir.yCoord, -branchDir.zCoord),
				new Vec3d(spread.xCoord*branchDir.xCoord, spread.yCoord*branchDir.yCoord, spread.zCoord*branchDir.zCoord));
	}
	
	public static EnumFacing.Axis getMainHorizAxis(Vec3d direction)
	{
		double x = Math.abs(direction.xCoord);
		double z = Math.abs(direction.zCoord);
		return x > z? EnumFacing.Axis.X: EnumFacing.Axis.Z;
	}
	
	public static EnumFacing.Axis getMainAxis(Vec3d direction)
	{
		double x = Math.abs(direction.xCoord);
		double y = Math.abs(direction.yCoord);
		double z = Math.abs(direction.zCoord);
		
		if (y > x && y > z)
		{
			return EnumFacing.Axis.Y;
		}
		return getMainHorizAxis(direction);
	}
}
