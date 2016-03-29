package genesis.entity.effect;

import genesis.util.random.DoubleRange;
import net.minecraft.client.particle.*;
import net.minecraft.world.World;

public class EntitySplashInsideBlockFX extends EntityFX
{
	protected static final DoubleRange RANGE_XZ = DoubleRange.create(-0.06, 0.06);
	protected static final DoubleRange RANGE_Y = DoubleRange.create(0.1, 0.3);
	
	public static class Factory implements IParticleFactory
	{
		@Override
		public EntitySplashInsideBlockFX getEntityFX(int particleID, World world, double x, double y, double z,
				double xVel, double yVel, double zVel, int... params)
		{
			return new EntitySplashInsideBlockFX(world, x, y, z, xVel, yVel, zVel);
		}
	}
	
	public EntitySplashInsideBlockFX(World world, double x, double y, double z,
			double xVel, double yVel, double zVel)
	{
		super(world, x, y, z, xVel, yVel, zVel);
		
		setParticleTextureIndex(19 + rand.nextInt(4));
		
		particleGravity = 1.5F;
		
		// Keep EntityFX's random velocity if no velocity is specified.
		if (xVel != 0 || yVel != 0 || zVel != 0)
		{
			xSpeed = xVel;
			ySpeed = yVel;
			zSpeed = zVel;
		}
		else
		{
			xSpeed = RANGE_XZ.get(rand);
			ySpeed = RANGE_Y.get(rand);
			zSpeed = RANGE_XZ.get(rand);
		}
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (isCollided)
		{
			setExpired();
		}
	}
}
