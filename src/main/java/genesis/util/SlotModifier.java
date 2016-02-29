package genesis.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface SlotModifier
{
	public static SlotModifier from(IInventory inventory, int slot)
	{
		return new SlotModifierInventory(inventory, slot);
	}
	
	public ItemStack getStack();
	
	public boolean isValid(ItemStack stack);
	public void set(ItemStack stack);
	
	public default ItemStack incrementSize(int amount)
	{
		ItemStack stack = getStack();
		
		if (stack == null)
			throw new RuntimeException("Stack is null, cannot increment size.");
		
		stack.stackSize += amount;
		
		if (stack.stackSize <= 0)
			stack = null;
		
		set(stack);
		return getStack();
	}
	
	abstract class SlotModifierBase implements SlotModifier
	{
		@Override
		public String toString()
		{
			if (getStack() == null)
				return "null";
			
			return getStack().toString();
		}
	}
	
	class SlotModifierInventory extends SlotModifierBase
	{
		final IInventory inventory;
		final int slot;
		
		private SlotModifierInventory(IInventory inventory, int slot)
		{
			this.inventory = inventory;
			this.slot = slot;
		}
		
		@Override
		public ItemStack getStack()
		{
			return inventory.getStackInSlot(slot);
		}
		
		@Override
		public boolean isValid(ItemStack stack)
		{
			return inventory.isItemValidForSlot(slot, stack);
		}
		
		@Override
		public void set(ItemStack stack)
		{
			inventory.setInventorySlotContents(slot, stack);
		}
	}
}
