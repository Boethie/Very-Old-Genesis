package genesis.entity.extendedproperties;

import net.minecraft.nbt.NBTTagCompound;

public class IntegerEntityProperty extends EntityPropertyBase<Integer>
{
	public IntegerEntityProperty(String name, Integer defaultValue, boolean write)
	{
		super(name, defaultValue, write);
	}
	
	@Override
	public void doWriteToNBT(Integer value, NBTTagCompound compound)
	{
		compound.setInteger(getName(), value);
	}
	
	@Override
	public Integer doReadFromNBT(NBTTagCompound compound)
	{
		return compound.getInteger(getName());
	}
}
