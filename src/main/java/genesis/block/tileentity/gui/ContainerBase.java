package genesis.block.tileentity.gui;

import java.util.*;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container
{
	public static final int DEFAULT_UI_WIDTH = 176;
	public static final int DEFAULT_UI_HEIGHT = 166;
	
	public static final int INV_PL_H = 4;
	public static final int INV_PL_W = 9;
	
	protected int playerInvStart = -1;
	protected int playerInvEnd = -1;
	protected int playerHotbarStart = -1;
	protected int playerHotbarEnd = -1;
	
	public int borderW = 4;
	public int borderH = 4;
	public int slotW = 18;
	public int slotH = 18;
	public int separatorH = 4;
	public int paddingX = 3;
	public int paddingY = 3;
	public int textH = 10;
	
	protected List<Slot> playerSlots = new ArrayList<Slot>();
	protected List<Slot> topSlots = new ArrayList<Slot>();
	protected Set<Slot> bigSlots = new HashSet<Slot>();

	protected UIArea playerInvArea = null;
	protected UIArea containerInvArea = null;
	
	public int width;
	public int height;
	public IInventory inventory;
	
	protected ContainerBase(IInventory inventory)
	{
		super();
		
		this.inventory = inventory;
	}
	
	public ContainerBase(InventoryPlayer invPlayer, IInventory inventory)
	{
		this(inventory);
		
		addPlayerInventory(invPlayer);
	}
	
	protected void addPlayerInventory(InventoryPlayer invPlayer)
	{
		int posX = 0;
		int posY = 0;
		
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
				playerSlots.add(addSlotToContainer(slot));
				
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
				
				posX += slotW;
			}
			
			posX -= INV_PL_W * slotW;
			posY += slotH;
			
			if (iY == 0)
			{
				break;
			}
		}
	}
	
	protected <T extends Slot> T addTopAlignedSlot(T slot)
	{
		//int x = (width - SLOT_W) / 2 + 1;
		//int y = BORDER_H + paddingY + textH;
		//slot.xDisplayPosition += x;
		//slot.yDisplayPosition += y;
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

	public UIArea getPlayerInventoryArea()
	{
		if (playerInvArea == null)
		{
			playerInvArea = getSlotsArea(playerSlots);
		}
		
		return playerInvArea;
	}
	
	public int getPlayerInventoryTextY()
	{
		return getPlayerInventoryArea().top - textH - 1;
	}
	
	public UIArea getContainerInventoryArea()
	{
		if (containerInvArea == null)
		{
			containerInvArea = getSlotsArea(topSlots);
		}
		
		return containerInvArea;
	}
	
	public int getActualSlotWidth(Slot slot)
	{
		if (!inventorySlots.contains(slot))
		{
			return 0;
		}
		
		if (bigSlots.contains(slot))
		{
			return slotW + 2;
		}
		
		return slotW;
	}
	
	public int getActualSlotHeight(Slot slot)
	{
		if (!inventorySlots.contains(slot))
		{
			return 0;
		}
		
		if (bigSlots.contains(slot))
		{
			return slotH + 2;
		}
		
		return slotH;
	}
	
	public static class UIArea
	{
		public final int left;
		public final int right;
		public final int top;
		public final int bottom;
		
		private int width = -1;
		private int height = -1;
		
		public UIArea(int left, int right, int top, int bottom)
		{
			this.left = left;
			this.right = right;
			this.top = top;
			this.bottom = bottom;
		}
		
		public int getWidth()
		{
			if (width == -1)
			{
				width = right - left;
			}
			
			return width;
		}
		
		public int getHeight()
		{
			if (height == -1)
			{
				height = bottom - top;
			}
			
			return height;
		}
	}
	
	public UIArea getSlotsArea(Collection<Slot> slots)
	{
		if (slots == null || slots.isEmpty())
		{
			return null;
		}
		
		int left = Integer.MAX_VALUE;
		int right = -Integer.MAX_VALUE;
		int top = Integer.MAX_VALUE;
		int bottom = -Integer.MAX_VALUE;
		
		for (Slot slot : slots)
		{
			left = Math.min(left, slot.xDisplayPosition - 1);
			right = Math.max(right, slot.xDisplayPosition + getActualSlotWidth(slot) - 1);
			top = Math.min(top, slot.yDisplayPosition - 1);
			bottom = Math.max(bottom, slot.yDisplayPosition + getActualSlotHeight(slot) - 1);
		}
		
		return new UIArea(left, right, top, bottom);
	}
	
	public void fitGUIAroundSlots(Collection<Slot> slots)
	{
		UIArea area = getSlotsArea(slots);
		
		int left = (width / 2) - area.left;
		int right = area.right - (width / 2);
		int maxDistH = Math.max(left, right);
		width = Math.max(width, (maxDistH + paddingX) * 2);
		height = Math.max(height, area.getHeight() + textH + (paddingY + borderH) * 2);
	}
	
	public void moveSlots(Collection<Slot> slots, int offX, int offY)
	{
		for (Slot slot : slots)
		{
			slot.xDisplayPosition += offX;
			slot.yDisplayPosition += offY;
		}
	}
	
	public void centerSlotsHorizontally(Collection<Slot> slots)
	{
		UIArea area = getSlotsArea(slots);
		moveSlots(slots, -area.left + (width - area.getWidth()) / 2, 0);
	}
	
	public void positionSlotsVertically(Collection<Slot> slots, int y)
	{
		UIArea area = getSlotsArea(slots);
		moveSlots(slots, 0, -area.top + y);
	}
	
	public void resetCached()
	{
		playerInvArea = null;
		containerInvArea = null;
	}
	
	public void setUpGUILayout()
	{
		UIArea topArea = getContainerInventoryArea();
		UIArea playerArea = getPlayerInventoryArea();
		width = Math.max(topArea.getWidth(), playerArea.getWidth()) + (paddingY + borderH) * 2;
		
		centerSlotsHorizontally(topSlots);
		centerSlotsHorizontally(playerSlots);
		
		positionSlotsVertically(topSlots, borderH + paddingY + textH);
		positionSlotsVertically(playerSlots, getSlotsArea(topSlots).bottom + textH + 4);
		fitGUIAroundSlots(inventorySlots);
		
		resetCached();
	}
	
	protected boolean isPlayerMain(int slotNumber)
	{
		return slotNumber >= playerInvStart && slotNumber <= playerInvEnd;
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean useEndIndex)
	{
		boolean merged = false;
		
		// Attempt to merge into existing stacks first.
		if (stack.isStackable())
		{
			for (int i = useEndIndex ? endIndex - 1 : startIndex;
					stack.stackSize > 0 && (useEndIndex ? i >= startIndex : i < endIndex);
					i += useEndIndex ? -1 : 1)
			{
				Slot slot = inventorySlots.get(i);
				ItemStack slotStack = slot.getStack();
				
				if (slotStack != null && slotStack.getItem() == stack.getItem() &&
						(!stack.getHasSubtypes() || stack.getMetadata() == slotStack.getMetadata()) &&
						ItemStack.areItemStackTagsEqual(stack, slotStack))
				{
					int newSize = Math.min(slotStack.stackSize + stack.stackSize, stack.getMaxStackSize());
					
					ItemStack testStack = slotStack.copy();
					testStack.stackSize = newSize;
					
					if (slot.isItemValid(testStack))
					{
						stack.stackSize -= newSize - slotStack.stackSize;
						slotStack.stackSize = newSize;
						slot.onSlotChanged();
						merged = true;
					}
				}
			}
		}
		
		// Then try to merge into empty slots.
		if (stack.stackSize > 0)
		{
			for (int i = useEndIndex ? endIndex - 1 : startIndex;
					useEndIndex ? i >= startIndex : i < endIndex;
					i += useEndIndex ? -1 : 1)
			{
				Slot slot = inventorySlots.get(i);
				ItemStack slotStack = slot.getStack();

				if (slotStack == null && slot.isItemValid(stack))
				{
					slot.putStack(stack.copy());
					slot.onSlotChanged();
					stack.stackSize = 0;
					merged = true;
					break;
				}
			}
		}

		return merged;
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
	
	public enum TransferResult
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
		Slot slot = inventorySlots.get(slotNumber);
		
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
						if (!mergeStackToPlayerHotbar(stack, false))
						{
							return null;
						}
					}
					else if (isPlayerHotbar(slotNumber))
					{
						if (!mergeStackToPlayerMain(stack, false))
						{
							return null;
						}
					}
				}
			}
			else	// The slot is a top slot. Merge to player inventory.
			{
				if (!mergeStackToPlayerInv(stack, true))
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
