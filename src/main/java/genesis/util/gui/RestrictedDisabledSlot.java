package genesis.util.gui;

import net.minecraft.inventory.*;

public class RestrictedDisabledSlot extends RestrictedSlot
{
	public static interface IInventoryDisabledSlots extends IInventory
	{
		public boolean isSlotDisabled(int index);
	}
	
	protected final IInventoryDisabledSlots disabledSlots;
	
	public RestrictedDisabledSlot(IInventoryDisabledSlots inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
		
		disabledSlots = inventory;
	}
	
	@Override
	public boolean canBeHovered()
	{
		return disabledSlots.isSlotDisabled(getSlotIndex());
	}
}
