package genesis.entity.effect;

import genesis.util.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntitySplashInsideBlockFX extends EntityFX
{
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

		RandomDoubleRange rangeXZ = new RandomDoubleRange(-0.06, 0.06);
		RandomDoubleRange rangeY = new RandomDoubleRange(0.1, 0.3);
		
		// Keep EntityFX's random velocity if no velocity is specified.
		if (xVel != 0 || yVel != 0 || zVel != 0)
		{
			motionX = xVel;
			motionY = yVel;
			motionZ = zVel;
		}
		else
		{
			motionX = rangeXZ.getRandom(rand);
			motionY = rangeY.getRandom(rand);
			motionZ = rangeXZ.getRandom(rand);
		}
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (isCollided)
		{
			setDead();
		}
	}
}
