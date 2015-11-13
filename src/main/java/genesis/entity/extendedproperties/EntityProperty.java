package genesis.entity.extendedproperties;

import net.minecraft.nbt.NBTTagCompound;

public interface EntityProperty<T>
{
	public String getName();
	public void writeToNBT(T value, NBTTagCompound compound);
	public T readFromNBT(NBTTagCompound compound);
	public T getDefaultValue();
}
