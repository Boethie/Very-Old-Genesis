package genesis.util.gui;

import net.minecraft.inventory.*;

public class RestrictedDisabledSlot extends RestrictedSlot
{
	public interface IInventoryDisabledSlots extends IInventory
	{
		boolean isSlotDisabled(int index);
	}
	
	protected final IInventoryDisabledSlots disabledSlots;
	
	public RestrictedDisabledSlot(IInventoryDisabledSlots inv, int index, int x, int y)
	{
		super(inv, index, x, y);
		
		disabledSlots = inv;
	}
	
	@Override
	public boolean canBeHovered()
	{
		return disabledSlots.isSlotDisabled(getSlotIndex());
	}
}
