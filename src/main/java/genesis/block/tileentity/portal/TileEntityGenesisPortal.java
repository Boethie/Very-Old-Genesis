package genesis.block.tileentity.portal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.block.tileentity.TileEntityBase;
import genesis.common.GenesisConfig;
import genesis.portal.GenesisPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGenesisPortal extends TileEntityBase implements ITickable
{
	private float radius = 2.5f;
	private byte timer = 0;
	private int ticksAlive = 0;
	private static final float ROTSPEED = 1;
	
	@SideOnly(Side.CLIENT)
	public ArrayList<PortalParticle> particles;
	@SideOnly(Side.CLIENT)
	private int spawnCool;
	
	private static final Random rand = new Random();
	
	public TileEntityGenesisPortal()
	{
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			particles = new ArrayList<PortalParticle>();
			spawnCool = 0;
		}
	}
	
	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			if (--timer <= 0)
			{
				GenesisPortal.fromPortalBlock(worldObj, pos).updatePortalStatus(worldObj);
				timer = GenesisPortal.PORTAL_CHECK_TIME;
			}
		}
		ticksAlive++;
		float rad = getRadius();
		if (rad > 0)
		{
			float rMax = 625 * rad * rad;
			double x = this.pos.getX() + 0.5;
			double y = this.pos.getY() + 0.5;
			double z = this.pos.getZ() + 0.5;
			for (Entity entity : worldObj.loadedEntityList)
			{
				if (entity.timeUntilPortal > 0) continue;
				if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying) continue;
				AxisAlignedBB bb = entity.getEntityBoundingBox();
				double dX = x - (bb.minX + bb.maxX) / 2;
				double dY = y - (bb.minY + bb.maxY) / 2;
				double dZ = z - (bb.minZ + bb.maxZ) / 2;
				double dsqr = (dX * dX + dY * dY + dZ * dZ);
				if (dsqr > 0 && dsqr <= rMax)
				{
					double f = rad * rad * rad * .015625 / (dsqr * MathHelper.sqrt_double(dsqr));
					entity.motionX += dX * f;
					entity.motionY += dY * f;
					entity.motionZ += dZ * f;
				}
			}
			if (worldObj.isRemote)
			{
				if (GenesisConfig.affectParticles)
				{
					ArrayDeque<Particle>[][] particles = Minecraft.getMinecraft().effectRenderer.fxLayers;
					for (int i = 0; i < particles.length; i++) for (int j = 0; j < particles[i].length; j++) for (Particle p : particles[i][j])
					{
						double dX = x - p.posX;
						double dY = y - p.posY;
						double dZ = z - p.posZ;
						double dsqr = (dX * dX + dY * dY + dZ * dZ);
						if (dsqr > 0 && dsqr <= rMax)
						{
							double f = rad * rad * rad * .015625 / (dsqr * MathHelper.sqrt_double(dsqr));
							p.motionX += dX * f;
							p.motionY += dY * f;
							p.motionZ += dZ * f;
						}
					}
				}
				int size = particles.size();
				for (int i = 0; i < size; i++)
				{
					PortalParticle p = particles.get(i);
					p.life--;
					if (p.life <= 0)
					{
						particles.remove(i);
						i--;
						size--;
					}
				}
				if (--spawnCool <= 0)
				{
					particles.add(new PortalParticle(randomUnitVec()));
					spawnCool = 5;
				}
			}
		}
	}
	
	private static Vec3d randomUnitVec()
	{
		double theta = rand.nextDouble() * 2 * Math.PI;
		double r = MathHelper.sqrt_double(rand.nextDouble());
		double z = MathHelper.sqrt_double(1 - r * r);
		if (rand.nextBoolean()) z = -z;
		return new Vec3d(r * Math.cos(theta), r * Math.sin(theta), z);
	}
	
	@Override
	public boolean shouldRenderInPass(int pass)
	{
		return pass == 1;
	}
	
	@Override
	public double getMaxRenderDistanceSquared()
	{
		return 65536;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		compound.setFloat("radius", radius);
		compound.setByte("timer", timer);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		radius = compound.getFloat("radius");
		timer = compound.getByte("timer");
	}
	
	@Override
	protected void writeVisualData(NBTTagCompound compound, boolean save)
	{
		compound.setInteger("alive", ticksAlive);
		compound.setFloat("radius", radius);
	}
	
	@Override
	protected void readVisualData(NBTTagCompound compound, boolean save)
	{
		ticksAlive = compound.getInteger("alive");
		radius = compound.getFloat("radius");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
		double off1 = 0.5 - radius;
		double off2 = 0.5 + radius;
		return new AxisAlignedBB(pos.add(off1, off1, off1), pos.add(off2, off2, off2));
    }
	
	public float getAngle(float partial)
	{
		return (ticksAlive + partial) * ROTSPEED;
	}
	
	public float getRadius(float partial)
	{
		if (ticksAlive < 20) return radius * (ticksAlive + partial) * 0.05f;
		else return radius;
	}
	
	public float getRadius()
	{
		if (ticksAlive < 20) return radius * (ticksAlive) * 0.05f;
		else return radius;
	}
	

	@SideOnly(Side.CLIENT)
	public static class PortalParticle
	{
		private final Vec3d vec;
		public int life;
		
		public PortalParticle(Vec3d vec)
		{
			this.vec = vec;
			life = 40;
		}
		
		public Vec3d getVec(float partial)
		{
			double r = (life - partial) * .025;
			if (r <= 0) return Vec3d.ZERO;
			r = Math.pow(r, .25);
			return vec.scale(r);
		}
	}
}
