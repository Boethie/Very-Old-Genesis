package genesis.block.tileentity.portal;

import java.util.List;

import genesis.block.tileentity.TileEntityBase;
import genesis.portal.GenesisPortal;
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
	public float radius = 2.5f;
	public byte timer = 0;
	public int ticksAlive = 0;
	
	public TileEntityGenesisPortal() {}
	
	@Override
	public void setPos(BlockPos pos)
	{
		super.setPos(pos);
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
		double x = this.pos.getX() + 0.5;
		double y = this.pos.getY() + 0.5;
		double z = this.pos.getZ() + 0.5;
		for (Entity entity : worldObj.loadedEntityList)
		{
			//if (entity.getPortalCooldown() > 0) continue;
			if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying) continue;
			AxisAlignedBB bb = entity.getEntityBoundingBox();
			double dX = x - (bb.minX + bb.maxX) / 2;
			double dY = y - (bb.minY + bb.maxY) / 2;
			double dZ = z - (bb.minZ + bb.maxZ) / 2;
			double dsqr = (dX * dX + dY * dY + dZ * dZ);
			if (dsqr > 0)
			{
				double f = radius * radius * radius * .015625 / (dsqr * MathHelper.sqrt_double(dsqr));
				entity.motionX += dX * f;
				entity.motionY += dY * f;
				entity.motionZ += dZ * f;
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
		return INFINITE_EXTENT_AABB;
    }
}
