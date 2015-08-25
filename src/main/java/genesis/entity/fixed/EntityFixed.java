package genesis.entity.fixed;

import genesis.common.GenesisItems;
import genesis.util.RandomReflection;
import genesis.util.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

public abstract class EntityFixed extends Entity
{
	protected BlockPos fixedTo;
	
	public EntityFixed(World world)
	{
		super(world);
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
	
	public void setFixedTo(BlockPos pos)
	{
		fixedTo = pos;
	}
	
	@Override
	public void setPositionAndUpdate(double x, double y, double z)
	{
		super.setPositionAndUpdate(x, y, z);
		// Set the fixedTo position, as this is what gets called to set the position on spawn.
		setFixedTo(new BlockPos(posX, posY, posZ));
	}
	
	public abstract ItemStack getDroppedItem();
	
	public void dropItem()
	{
		WorldUtils.spawnItemsAt(worldObj, posX, posY, posZ, null, getDroppedItem());
	}
	
	public void setDeadAndDrop()
	{
		if (!isDead)
		{
			dropItem();
		}
		
		setDead();
	}
	
	protected abstract boolean isValid();
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (!worldObj.isRemote && !isValid())
		{
			setDeadAndDrop();
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (amount > 0)
		{
			if (source instanceof EntityDamageSource)
			{
				Entity sourceEntity = ((EntityDamageSource) source).getEntity();
				
				if (sourceEntity instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) sourceEntity;
					
					if (worldObj.isRemote && player == Minecraft.getMinecraft().thePlayer)
					{
						RandomReflection.setBlockHitDelay(5);
					}
					
					if (player.capabilities.isCreativeMode)
					{	// Stop items dropping the player attacking is in creative mode.
						setDead();
					}
				}
			}
			
			setDeadAndDrop();
			return true;
		}
		
		return false;
	}
	
	@Override
	public void setDead()
	{
		super.setDead();
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
			fixedTo = new BlockPos(this);
		}
	}
}
