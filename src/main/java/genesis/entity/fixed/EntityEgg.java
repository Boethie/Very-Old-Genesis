package genesis.entity.fixed;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityEgg extends EntityFixed
{
	protected int age = 0;
	protected int maxAge = 0;
	
	public EntityEgg(World world)
	{
		super(world);
		
		setMaxAge();
	}
	
	public EntityEgg(World world, Vec3d position)
	{
		super(world, position);
		
		setMaxAge();
	}
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		
		float size = 0.0625F;
		setSize(size, size);
		renderDistanceWeight = 8;
	}
	
	protected abstract void setMaxAge();
	
	protected abstract void spawnBaby();
	
	public void grow()
	{
		if (!worldObj.isRemote)
		{
			spawnBaby();
			setDead();
		}
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		age++;
		
		if (age >= maxAge)
		{
			grow();
		}
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		compound.setInteger("age", age);
		compound.setInteger("maxAge", maxAge);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		
		age = compound.getInteger("age");
		maxAge = compound.getInteger("maxAge");
	}
	
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}
}
