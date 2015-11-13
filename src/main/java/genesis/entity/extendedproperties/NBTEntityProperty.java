package genesis.entity.extendedproperties;

import net.minecraft.nbt.NBTTagCompound;

public class NBTEntityProperty extends EntityPropertyBase<NBTTagCompound>
{
	public NBTEntityProperty(String name, NBTTagCompound defaultValue, boolean writeToNBT)
	{
		super(name, defaultValue, writeToNBT);
	}
	
	@Override
	public void doWriteToNBT(NBTTagCompound value, NBTTagCompound compound)
	{
		if (value != null)
		{
			compound.setTag(getName(), value);
		}
	}
	
	@Override
	public NBTTagCompound doReadFromNBT(NBTTagCompound compound)
	{
		return compound.hasKey(getName(), 10) ? compound.getCompoundTag(getName()) : null;
	}
}
