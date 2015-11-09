package genesis.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface SlotModifier
{
	public int getSlot();
	public ItemStack getStack();
	public void modifySize(int amount);
	public void setStack(ItemStack stack);
	public void clearStack();
	
	public static abstract class SlotModifierBase implements SlotModifier
	{
		public void modifySize(int amount)
		{
			ItemStack stack = getStack();
			
			if (stack != null)
			{
				stack.stackSize += amount;
				setStack(stack);
			}
		}
		
		public void clearStack()
		{
			setStack(null);
		}
	}
	
	public static class SlotModifierInventory extends SlotModifierBase
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
