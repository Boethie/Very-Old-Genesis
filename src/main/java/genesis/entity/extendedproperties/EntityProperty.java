package genesis.entity.extendedproperties;

import net.minecraft.nbt.NBTTagCompound;

public interface EntityProperty<T>
{
	String getName();
	void writeToNBT(T value, NBTTagCompound compound);
	T readFromNBT(NBTTagCompound compound);
	T getDefaultValue();
	boolean isValidValue(T value);
}
