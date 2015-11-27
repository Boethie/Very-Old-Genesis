package genesis.entity.extendedproperties;

import net.minecraft.nbt.NBTTagCompound;

public abstract class EntityPropertyBase<T> implements EntityProperty<T>
{
	private final String name;
	private final T defaultValue;
	private final boolean writeToNBT;
	
	public EntityPropertyBase(String name, T defaultValue, boolean writeToNBT)
	{
		this.name = name;
		this.defaultValue = defaultValue;
		this.writeToNBT = writeToNBT;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public T getDefaultValue()
	{
		return defaultValue;
	}
	
	public abstract void doWriteToNBT(T value, NBTTagCompound compound);
	public abstract T doReadFromNBT(NBTTagCompound compound);
	
	@Override
	public void writeToNBT(T value, NBTTagCompound compound)
	{
		if (writeToNBT)
		{
			doWriteToNBT(value, compound);
		}
	}
	
	@Override
	public T readFromNBT(NBTTagCompound compound)
	{
		return writeToNBT ? doReadFromNBT(compound) : null;
	}
	
	@Override
	public boolean isValidValue(T value)
	{
		return true;
	}
	
	@Override
	public String toString()
	{
		return getName() + "[default=" + getDefaultValue() + "]";
	}
}