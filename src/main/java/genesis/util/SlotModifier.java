package genesis.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface SlotModifier
{
	int getSlot();
	ItemStack getStack();
	void modifySize(int amount);
	void setStack(ItemStack stack);
	void clearStack();
	
	abstract class SlotModifierBase implements SlotModifier
	{
		@Override
		public void modifySize(int amount)
		{
			ItemStack stack = getStack();
			
			if (stack != null)
			{
				stack.stackSize += amount;
				setStack(stack);
			}
		}
		
		@Override
		public void clearStack()
		{
			setStack(null);
		}
	}
	
	class SlotModifierInventory extends SlotModifierBase
	{
		final IInventory inventory;
		final int slot;
		
		public SlotModifierInventory(IInventory inventory, int slot)
		{
			this.inventory = inventory;
			this.slot = slot;
		}
		
		@Override
		public int getSlot()
		{
			return slot;
		}
		
		@Override
		public ItemStack getStack()
		{
			return inventory.getStackInSlot(slot);
		}
		
		@Override
		public void setStack(ItemStack stack)
		{
			inventory.setInventorySlotContents(slot, stack);
		}
	}
}
