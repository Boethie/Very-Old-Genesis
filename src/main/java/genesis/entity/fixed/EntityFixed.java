package genesis.entity.fixed;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityFixed extends Entity
{
	protected BlockPos fixedTo;
	
	public EntityFixed(World world)
	{
		super(world);
		
		fixedTo = new BlockPos(this);
	}
	
	public EntityFixed(World world, Vec3 position)
	{
		this(world);
		
		setPositionAndUpdate(position.xCoord, position.yCoord, position.zCoord);
	}
	
	@Override
	protected void entityInit()
	{
	}
	
	@Override
	public void setPositionAndUpdate(double x, double y, double z)
	{
		super.setPositionAndUpdate(x, y, z);
		fixedTo = new BlockPos(posX, posY, posZ);
	}
	
	protected abstract boolean isValid();
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (!worldObj.isRemote && !isValid())
		{
			System.out.println("not valid");
			setDead();
		}
	}
	
	/**
	 * Prevents vanilla Entity code from pushing the entity up and out the top of what it is resting in/on.
	 */
	@Override
	public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int ticks, boolean unknown)
	{
        setPosition(x, y, z);
        setRotation(yaw, pitch);
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setDouble("x", posX);
		compound.setDouble("y", posY);
		compound.setDouble("z", posZ);
		
		compound.setDouble("blockX", posX);
		compound.setDouble("blockY", posY);
		compound.setDouble("blockZ", posZ);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		posX = compound.getDouble("x");
		posY = compound.getDouble("y");
		posZ = compound.getDouble("z");
		setPositionAndUpdate(posX, posY, posZ);
		
		if (compound.hasKey("blockX") && compound.hasKey("blockY") && compound.hasKey("blockZ"))
		{
			fixedTo = new BlockPos(compound.getInteger("blockX"), compound.getInteger("blockY"), compound.getInteger("blockZ"));
		}
		else
		{
			fixedTo = new BlockPos(posX, posY, posZ);
		}
	}
}
