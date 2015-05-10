package genesis.block.tileentity.gui;

import java.util.*;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.*;

public abstract class ContainerBase extends Container
{
	public static final int UI_WIDTH = 176;
	public static final int UI_HEIGHT = 166;
	
	public static final int INV_PL_H = 4;
	public static final int INV_PL_W = 9;
	public static final int SLOT_W = 18;
	public static final int SLOT_H = 18;
	public static final int BORDER_W = 4;
	public static final int BORDER_H = 4;

	protected int playerInvStart = -1;
	protected int playerInvEnd = -1;
	protected int playerHotbarStart = -1;
	protected int playerHotbarEnd = -1;
	
	public int separatorH = 4;
	public int paddingX = 3;
	public int paddingY = 3;
	public int textH = 10;
	
	protected List<Slot> topSlots = new ArrayList();
	protected Set<Slot> bigSlots = new HashSet();
	
	public int width;
	public int height;
	public IInventory inventory;
	
	public ContainerBase(InventoryPlayer invPlayer, IInventory inventory, int w, int h)
	{
		super();
		
		this.inventory = inventory;
		this.width = w;
		this.height = h;
		
		int posX = Math.round(width / 2F) + 1;
		int posY = height + 1;
		
		posX -= (INV_PL_W * SLOT_W) / 2;
		posY -= INV_PL_H * SLOT_H + separatorH + BORDER_H + paddingY;
		
		// Add player inv
		for (int iY = 1; iY <= INV_PL_H; iY++)
		{
			if (iY == INV_PL_H)
			{
				posY += separatorH;
				iY = 0;
			}
			
			for (int iX = 0; iX < 9; iX++)
			{
				Slot slot = new Slot(invPlayer, iX + iY * INV_PL_W, posX, posY);
				addSlotToContainer(slot);
				
				if (iY == 0)
				{
					if (playerHotbarStart == -1)
					{
						playerHotbarStart = slot.slotNumber;
					}
					
					playerHotbarEnd = slot.slotNumber;
				}
				else
				{
					if (playerInvStart == -1)
					{
						playerInvStart = slot.slotNumber;
					}
					
					playerInvEnd = slot.slotNumber;
				}
				
				posX += SLOT_W;
			}
			
			posX -= INV_PL_W * SLOT_W;
			posY += SLOT_H;
			
			if (iY == 0)
			{
				break;
			}
		}
	}
	
	public ContainerBase(InventoryPlayer invPlayer, IInventory inventory)
	{
		this(invPlayer, inventory, UI_WIDTH, UI_HEIGHT);
	}
	
	protected <T extends Slot> T addTopAlignedSlot(T slot)
	{
		int x = (width - SLOT_W) / 2 + 1;
		int y = BORDER_H + paddingY + textH;
		slot.xDisplayPosition += x;
		slot.yDisplayPosition += y;
		addSlotToContainer(slot);
		topSlots.add(slot);
		
		return slot;
	}

	protected <T extends Slot> T addBigTopAlignedSlot(T slot)
	{
		addTopAlignedSlot(slot);
		bigSlots.add(slot);
		
		return slot;
	}
	
	public boolean isBigSlot(Slot slot)
	{
		return bigSlots.contains(slot);
	}
	
	protected boolean isPlayerMain(int slotNumber)
	{
		return slotNumber >= playerInvStart && slotNumber <= playerInvEnd;
	}
	
	protected boolean mergeStackToPlayerMain(ItemStack stack, boolean reverse)
	{
		return mergeItemStack(stack, playerInvStart, playerInvEnd + 1, reverse);
	}
	
	protected boolean isPlayerHotbar(int slotNumber)
	{
		return slotNumber >= playerHotbarStart && slotNumber <= playerHotbarEnd;
	}
	
	protected boolean mergeStackToPlayerHotbar(ItemStack stack, boolean reverse)
	{
		return mergeItemStack(stack, playerHotbarStart, playerHotbarEnd + 1, reverse);
	}
	
	protected boolean mergeStackToPlayerInv(ItemStack stack, boolean reverse)
	{
		int start = Math.min(playerHotbarStart, playerInvStart);
		int end = Math.max(playerHotbarEnd, playerInvEnd);
		
		return mergeItemStack(stack, start, end + 1, reverse);
	}
	
	protected boolean mergeStackToSlot(ItemStack stack, Slot slot)
	{
		return mergeItemStack(stack, slot.slotNumber, slot.slotNumber + 1, false);
	}
	
	public static enum TransferResult
	{
		SUCCESS, FAILURE, CONTINUE;
	}
	
	protected TransferResult transferToContainerSlot(EntityPlayer player, Slot fromSlot, ItemStack stack)
	{
		// Attempt to merge into input or fuel slots
		for (Slot toSlot : topSlots)
		{
			if (inventory.isItemValidForSlot(toSlot.getSlotIndex(), stack))
			{
				if (mergeStackToSlot(stack, toSlot))
				{
					return TransferResult.SUCCESS;
				}
			}
		}
		
		return TransferResult.CONTINUE;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber)
	{
		ItemStack originalStack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			originalStack = stack.copy();
			
			if (isPlayerMain(slotNumber) || isPlayerHotbar(slotNumber))	// Is player slot
			{
				TransferResult result = transferToContainerSlot(player, slot, stack);
				
				if (result == TransferResult.FAILURE)
				{
					return null;
				}
				
				// Insert into the main player inventory instead
				if (result == TransferResult.CONTINUE)
				{
					if (isPlayerMain(slotNumber))	// Player inventory
					{
						//if (!mergeItemStack(stack, 30, 39, false))
						if (!mergeStackToPlayerHotbar(stack, false))
						{
							return null;
						}
					}
					else if (isPlayerHotbar(slotNumber))
					{
						//if (!mergeItemStack(stack, 3, 30, false))
						if (!mergeStackToPlayerMain(stack, false))
						{
							return null;
						}
					}
				}
			}
			else	// The slot is a top slot. Merge to player inventory.
			{
				if (!mergeStackToPlayerInv(stack, false))
				{
					return null;
				}
			}

			if (stack.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (stack.stackSize == originalStack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(player, stack);
		}
		
		return originalStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

}
