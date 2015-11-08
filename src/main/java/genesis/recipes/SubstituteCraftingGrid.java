package genesis.recipes;

import com.google.common.base.Function;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

public class SubstituteCraftingGrid extends InventoryCrafting
{
	protected final InventoryCrafting wrapping;
	protected final Function<ItemStack, ItemStack> substitution;
	
	public SubstituteCraftingGrid(InventoryCrafting wrapping, Function<ItemStack, ItemStack> substitution)
	{
		super(null, 0, 0);
		
		this.wrapping = wrapping;
		this.substitution = substitution;
	}
	
	protected ItemStack getSubstituted(ItemStack stack)
	{
		ItemStack substitute = substitution.apply(stack);
		
		if (substitute != null)
		{
			return substitute.copy();
		}
		
		return stack;
	}
	
	@Override
	public int getSizeInventory()
	{
		return wrapping.getSizeInventory();
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return getSubstituted(wrapping.getStackInSlot(index));
	}
	
	@Override
	public ItemStack getStackInRowAndColumn(int row, int column)
	{
		return getSubstituted(wrapping.getStackInRowAndColumn(row, column));
	}
	
	@Override
	public String getCommandSenderName()
	{
		return wrapping.getCommandSenderName();
	}
	
	@Override
	public boolean hasCustomName()
	{
		return wrapping.hasCustomName();
	}
	
	@Override
	public IChatComponent getDisplayName()
	{
		return wrapping.getDisplayName();
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return getSubstituted(wrapping.getStackInSlotOnClosing(index));
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return getSubstituted(wrapping.decrStackSize(index, count));
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		wrapping.setInventorySlotContents(index, stack);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return wrapping.getInventoryStackLimit();
	}
	
	@Override
	public void markDirty()
	{
		wrapping.markDirty();
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return wrapping.isUseableByPlayer(player);
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
		wrapping.openInventory(player);
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
		wrapping.closeInventory(player);
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return wrapping.isItemValidForSlot(index, stack);
	}
	
	@Override
	public int getField(int id)
	{
		return wrapping.getField(id);
	}
	
	@Override
	public void setField(int id, int value)
	{
		wrapping.setField(id, value);
	}
	
	@Override
	public int getFieldCount()
	{
		return wrapping.getFieldCount();
	}
	
	@Override
	public void clear()
	{
		wrapping.clear();
	}
	
	@Override
	public int getHeight()
	{
		return wrapping.getHeight();
	}
	
	@Override
	public int getWidth()
	{
		return wrapping.getWidth();
	}
}
