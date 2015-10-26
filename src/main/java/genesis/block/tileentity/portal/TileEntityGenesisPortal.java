package genesis.block.tileentity.portal;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class TileEntityGenesisPortal extends TileEntity implements IUpdatePlayerListBox
{
	protected double radius = 5 / 2.0;
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
		List<EntityLivingBase> entities = (List<EntityLivingBase>) worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
		
		for (EntityLivingBase entity : entities)
		{
			double diffX = entity.posX - center.xCoord;
			double diffY = entity.posY + (entity.getEyeHeight() / 2) - center.yCoord;
			double diffZ = entity.posZ - center.zCoord;
			double distance = diffX * diffX + diffY * diffY + diffZ * diffZ;
			
			if (distance < radius * radius)
			{
				distance = MathHelper.sqrt_double(distance);
				double speed = -(distance / radius) * 0.05;
				entity.motionX += diffX * speed;
				entity.motionY += diffY * speed;
				entity.motionZ += diffZ * speed;
			}
		}
	}
}
