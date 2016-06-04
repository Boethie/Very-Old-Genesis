package genesis.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface SlotModifier
{
	static SlotModifier from(IInventory inventory, int slot)
	{
		return new SlotModifierInventory(inventory, slot);
	}
	
	ItemStack getStack();
	
	boolean isValid(ItemStack stack);
	void set(ItemStack stack);
	
	default boolean put(ItemStack stack)
	{
		if (stack == null)
			return true;
		
		ItemStack ourStack = getStack();
		
		if (ourStack == null)
		{
			set(stack.copy());
			return true;
		}
		
		if (!ourStack.isItemEqual(stack) || !ItemStack.areItemStackTagsEqual(ourStack, stack))
			return false;
		
		ourStack.stackSize += stack.stackSize;
		set(ourStack);
		return true;
	}
	
	/**
	 * @param amount The amount to consume from the slot.
	 * @return The stack split off from this slot's stack.
	 */
	default ItemStack consume(int amount)
	{
		if (amount < 0)
			throw new IllegalArgumentException("Cannot consume " + amount + " items, that would increase stack size.");
		
		ItemStack stack = getStack();
		
		if (stack == null)
			return null;
		
		ItemStack split = stack.splitStack(amount);
		
		if (stack.stackSize <= 0)
			stack = null;
		
		set(stack);
		return split;
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
