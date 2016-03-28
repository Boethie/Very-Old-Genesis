package genesis.block.tileentity.gui;

import com.google.common.collect.ImmutableList;

import genesis.block.tileentity.*;
import genesis.block.tileentity.TileEntityKnapper.KnappingState;
import genesis.block.tileentity.crafting.KnappingRecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;

public class ContainerKnapper extends ContainerBase
{
	public class SlotKnapping extends Slot
	{
		public SlotKnapping(IInventory inv, int index, int x, int y)
		{
			super(inv, index, x, y);
		}
		
		public KnappingState getKnappingState()
		{
			return workbench.getKnappingSlotState(getSlotIndex());
		}
		
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return !workbench.isKnappingEnabled() && super.isItemValid(stack);
		}
		
		@Override
		public boolean canBeHovered()
		{
			return !workbench.areKnappingSlotsLocked();
		}
	}
	
	public TileEntityKnapper workbench;
	
	public final ImmutableList<SlotKnapping> craftingSlots;
	public final ImmutableList<SlotKnapping> leftCraftingSlots;
	public final ImmutableList<SlotKnapping> rightCraftingSlots;
	public final Slot knappingInputSlot;
	public final Slot knappingInputSlotLocked;
	public final Slot knappingToolSlot;
	public final Slot knappingToolSlotDamaged;
	public final SlotCrafting outputSlotMain;
	public final Slot outputSlotWaste;
	
	public ContainerKnapper(EntityPlayer player, TileEntityKnapper workbench)
	{
		super(player.inventory, workbench);
		
		this.workbench = workbench;
		
		int topAreaHeight = slotH * 3;
		int sep = 36;
		
		int knappingX = -sep - (slotW * 2);
		int knappingH = slotH * 2 + 8;
		int knappingY = (topAreaHeight - knappingH) / 2;

		knappingInputSlot = addTopAlignedSlot(new Slot(workbench, TileEntityKnapper.SLOT_KNAP_MATERIAL, knappingX, knappingY));
		knappingInputSlotLocked = addTopAlignedSlot(new Slot(workbench, TileEntityKnapper.SLOT_KNAP_MATERIAL_LOCKED, knappingX + slotW, knappingY)
		{
			@Override
			public int getSlotStackLimit()
			{
				return 1;
			}
			
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
			
			@Override
			public boolean canTakeStack(EntityPlayer player)
			{
				return false;
			}
		});
		knappingToolSlot = addTopAlignedSlot(new Slot(workbench, TileEntityKnapper.SLOT_KNAP_TOOL, knappingX, knappingY + knappingH - slotH)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return KnappingRecipeRegistry.isKnappingTool(stack);
			}
		});
		knappingToolSlotDamaged = addTopAlignedSlot(new Slot(workbench, TileEntityKnapper.SLOT_KNAP_TOOL_DAMAGED, knappingX + slotW, knappingY + knappingH - slotH)
		{
			@Override
			public int getSlotStackLimit()
			{
				return 1;
			}
		});
		
		int craftingSlotCount = TileEntityKnapper.SLOTS_CRAFTING_END - TileEntityKnapper.SLOTS_CRAFTING_START + 1;
		
		int rowWidth = 3;
		int rows = (int) Math.ceil(craftingSlotCount / (float) rowWidth);
		
		int craftX = 0;
		int craftY = (topAreaHeight - slotH * rows) / 2;

		ImmutableList.Builder<SlotKnapping> mainBuilder = ImmutableList.builder();
		ImmutableList.Builder<SlotKnapping> leftBuilder = ImmutableList.builder();
		ImmutableList.Builder<SlotKnapping> rightBuilder = ImmutableList.builder();
		
		for (int y = 0; y < rows; y++)
		{
			int startX = y * rowWidth;
			
			SlotKnapping left = null;
			SlotKnapping right = null;
			
			for (int x = 0; x < rowWidth && startX + x < craftingSlotCount; x++)
			{
				int index = startX + x;
				int posX = craftX + x * slotW;
				int posY = craftY + y * slotH;
				
				SlotKnapping cur = addTopAlignedSlot(new SlotKnapping(workbench, index, posX, posY));
				mainBuilder.add(cur);
				
				if (left == null)
					left = cur;
				right = cur;
			}
			
			if (left != null)
				leftBuilder.add(left);
			if (right != null)
				rightBuilder.add(right);
		}
		
		craftingSlots = mainBuilder.build();
		leftCraftingSlots = leftBuilder.build();
		rightCraftingSlots = rightBuilder.build();
		
		int outputX = getSlotsArea(topSlots).right + sep + 1;
		int outputY = (topAreaHeight - slotH * 2) / 2;
		
		outputSlotMain = addTopAlignedSlot(new SlotCrafting(player, workbench.getCraftingInventory(), workbench.getCraftingOutput(), TileEntityKnapper.SLOT_OUTPUT_MAIN, outputX, outputY));
		outputSlotWaste = addTopAlignedSlot(new Slot(workbench, TileEntityKnapper.SLOT_OUTPUT_WASTE, outputX, outputY + slotH)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
		});
		
		setUpGUILayout();
	}
	
	protected int[] prevProgresses = new int[TileEntityKnapper.SLOTS_CRAFTING_COUNT];
	protected boolean wasLocked = false;
	
	protected void sendGUIData(boolean force)
	{
		for (ICrafting crafting : crafters)
		{
			int i = 0;
			
			for (; i < prevProgresses.length; i++)
			{
				KnappingState curState = workbench.getKnappingSlotState(i);
				int progress = curState.getProgress();
				
				if (force || prevProgresses[i] != progress)
				{
					crafting.sendProgressBarUpdate(this, i, progress);
					prevProgresses[i] = progress;
				}
			}
			
			boolean locked = workbench.areKnappingSlotsLocked();
			
			if (force || wasLocked != locked)
			{
				crafting.sendProgressBarUpdate(this, i, locked ? 1 : 0);
				wasLocked = locked;
			}
		}
	}
	
	@Override
	public void updateProgressBar(int id, int value)
	{
		super.updateProgressBar(id, value);
		
		if (id < prevProgresses.length)
		{
			KnappingState state = workbench.getKnappingSlotState(id);
			
			if (state != null)
			{
				state.setProgress(value);
			}
		}
		else
		{
			id -= prevProgresses.length;
			
			switch (id)
			{
			case 0:
				workbench.setKnappingSlotsLocked(value == 1);
				break;
			}
		}
	}
	
	@Override
	public void onCraftGuiOpened(ICrafting iCrafting)
	{
		super.onCraftGuiOpened(iCrafting);
		
		sendGUIData(true);
	}
	
	public int changeTicks = 0;
	 
	@Override
	public void detectAndSendChanges()
	{
		int outputSlot = outputSlotMain.slotNumber;
		ItemStack oldOutput = inventoryItemStacks.get(outputSlot);
		ItemStack newOutput = inventorySlots.get(outputSlot).getStack();
		
		if (!ItemStack.areItemStacksEqual(oldOutput, newOutput))
		{
			for (ICrafting crafting : crafters)
			{
				if (crafting instanceof EntityPlayerMP)
				{
					((EntityPlayerMP) crafting).playerNetServerHandler.sendPacket(new SPacketSetSlot(windowId, outputSlot, newOutput));
				}
			}
		}
		
		super.detectAndSendChanges();
		
		sendGUIData(changeTicks % 5 == 0);
		changeTicks++;
	}
	
	@Override
	public ItemStack slotClick(int slotID, int button, int mode, EntityPlayer player)
	{
		if (slotID >= 0)
		{
			Slot slot = inventorySlots.get(slotID);
			
			if (slot == knappingInputSlotLocked)
			{
				workbench.resetKnappingState();
				return null;
			}
			
			if (slot.getHasStack() && (slot == outputSlotMain || slot == outputSlotWaste))
			{
				if (slot == outputSlotMain)
				{
					workbench.setKnappingSlotsLocked(true);
				}
				
				ItemStack old = slot.getStack().copy();
				ItemStack out = super.slotClick(slotID, button, mode, player);
				
				if (slot == outputSlotMain)
				{
					if (!slot.getHasStack())
					{
						if (workbench.isKnappingEnabled())
						{
							KnappingRecipeRegistry.onOutputTaken(workbench, workbench, player);
						}
						
						workbench.resetKnappingState();
					}
					else
					{
						ItemStack newStack = slot.getStack();
						
						if (old.isItemEqual(newStack) && old.stackSize == newStack.stackSize)
						{
							workbench.setKnappingSlotsLocked(false);
						}
					}
				}
				
				return out;
			}
		}
		
		return super.slotClick(slotID, button, mode, player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		if (slotID >= 0)
		{
			Slot slot = inventorySlots.get(slotID);
			
			if (slot == outputSlotMain)// && slot.getHasStack())
			{
				/*ItemStack stack = slot.getStack();
				ItemStack oldStack = stack.copy();
				
				if (!mergeStackToPlayerInv(stack, true))
				{
					return null;
				}
				
				if (stack.stackSize <= 0)
				{
					slot.putStack(null);
				}
				else
				{
					slot.onSlotChange(stack, oldStack);
				}
				
				return oldStack;*/
				
				while (slot.getHasStack())
				{
					if (super.transferStackInSlot(player, slotID) == null)
					{
						return null;
					}
					
					workbench.updateRecipeOutput();
					
					if (!slot.getHasStack())
					{
						return null;
					}
				}
			}
		}
		
		return super.transferStackInSlot(player, slotID);
	}
}
