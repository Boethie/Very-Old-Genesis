package genesis.block.tileentity.portal;

import java.util.List;

import genesis.block.tileentity.TileEntityBase;
import genesis.portal.GenesisPortal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.util.*;

public class TileEntityGenesisPortal extends TileEntityBase implements ITickable
{
	protected double radius = 5 / 2.0;
	protected byte timer = 0;
	
	public float prevRotation = 0;
	public float rotation = 0;
	
	//Cached
	protected Vec3 center = null;
	protected AxisAlignedBB bounds = null;
	
	public TileEntityGenesisPortal()
	{
	}
	
	public void setPos(BlockPos pos)
	{
		super.setPos(pos);
		
		center = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		bounds = new AxisAlignedBB(center.xCoord - radius, center.yCoord - radius, center.zCoord - radius,
									center.xCoord + radius, center.yCoord + radius, center.zCoord + radius);
	}
	
	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			timer--;
			
			if (timer <= 0)
			{
				GenesisPortal.fromPortalBlock(worldObj, pos).updatePortalStatus(worldObj);
				timer = GenesisPortal.PORTAL_CHECK_TIME;
			}
		}
		else
		{
			prevRotation = rotation;
			rotation += 1.25;
			
			while (rotation > 360)
			{
				prevRotation -= 360;
				rotation -= 360;
			}
		}
		
		List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
		
		for (EntityLivingBase entity : entities)
		{
			EntityPlayer player = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
			
			if (player != null && player.capabilities.isFlying)
			{
				continue;
			}
			
			double diffX = entity.posX - center.xCoord;
			double diffY = entity.posY + (entity.getEyeHeight() / 2) - center.yCoord;
			double diffZ = entity.posZ - center.zCoord;
			double distance = diffX * diffX + diffY * diffY + diffZ * diffZ;
			
			if (distance < radius * radius)
			{
				distance = MathHelper.sqrt_double(distance);
				double speed = -(distance / radius) * 0.05;
				
				if (player != null && player.isSneaking())
				{
					speed *= 0.5;
				}
				
				entity.motionX += diffX * speed;
				entity.motionY += diffY * speed;
				entity.motionZ += diffZ * speed;
			}
		}
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
	
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		compound.setDouble("radius", radius);
		compound.setByte("timer", timer);
		
		compound.setFloat("rotation", rotation);
	}
	
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		radius = compound.getDouble("radius");
		timer = compound.getByte("timer");
		
		prevRotation = rotation = compound.getFloat("rotation");
	}
	
	@Override
	protected void writeVisualData(NBTTagCompound compound, boolean save)
	{
	}
	
	@Override
	protected void readVisualData(NBTTagCompound compound, boolean save)
	{
	}
}
