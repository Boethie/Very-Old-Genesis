package genesis.util.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class RestrictedSlot extends Slot
{
	public RestrictedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	public boolean isItemValid(ItemStack stack)
	{
		return inventory.isItemValidForSlot(getSlotIndex(), stack);
	}
}
