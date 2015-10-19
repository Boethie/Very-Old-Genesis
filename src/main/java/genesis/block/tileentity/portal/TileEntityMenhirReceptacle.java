package genesis.block.tileentity.portal;

import genesis.block.tileentity.TileEntityBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityMenhirReceptacle extends TileEntityBase
{
	protected ItemStack containedItem = null;
	
	public void setContainedItem(ItemStack stack)
	{
		containedItem = stack;
		sendDescriptionPacket();
	}

	public boolean isReceptacleActive()
	{
		return containedItem != null;
	}
	
	@Override
	protected void writeVisualData(NBTTagCompound compound, boolean save)
	{
		if (containedItem != null)
		{
			compound.setTag("containedItem", containedItem.writeToNBT(new NBTTagCompound()));
		}
		else
		{
			compound.removeTag("containedItem");
		}
	}
	
	@Override
	protected void readVisualData(NBTTagCompound compound, boolean save)
	{
		if (compound.hasKey("containedItem"))
		{
			containedItem = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("containedItem"));
		}
		else
		{
			containedItem = null;
		}
	}
}
