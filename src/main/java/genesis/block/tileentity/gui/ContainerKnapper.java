package genesis.block.tileentity.gui;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import genesis.block.tileentity.*;
import genesis.block.tileentity.TileEntityKnapper.KnappingState;
import genesis.block.tileentity.crafting.KnappingRecipeRegistry;
import genesis.util.gui.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.MathHelper;

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
	public final Slot outputSlotMain;
	public final Slot outputSlotWaste;
	
	public ContainerKnapper(InventoryPlayer invPlayer, TileEntityKnapper workbench)
	{
		super(invPlayer, workbench);
		
		this.workbench = workbench;
		
		int topAreaHeight = SLOT_H * 3;
		int sep = 36;
		
		int knappingX = -sep - SLOT_W;
		int knappingH = SLOT_H * 3 + 15;
		int knappingY = (topAreaHeight - knappingH) / 2;

		knappingInputSlot = addTopAlignedSlot(new Slot(workbench, TileEntityKnapper.SLOT_KNAP_MATERIAL, knappingX, knappingY));
		knappingInputSlotLocked = addTopAlignedSlot(new Slot(workbench, TileEntityKnapper.SLOT_KNAP_MATERIAL_LOCKED, knappingX, knappingY + (knappingH - SLOT_H) / 2)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
		});
		knappingToolSlot = addTopAlignedSlot(new Slot(workbench, TileEntityKnapper.SLOT_KNAP_TOOL, knappingX, knappingY + knappingH - SLOT_H)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return KnappingRecipeRegistry.isKnappingTool(stack);
			}
		});
		
		int craftingSlotCount = TileEntityKnapper.SLOTS_CRAFTING_END - TileEntityKnapper.SLOTS_CRAFTING_START + 1;
		
		int rowWidth = 3;
		int rows = (int) Math.ceil(craftingSlotCount / (float) rowWidth);
		
		int craftX = 0;
		int craftY = (topAreaHeight - SLOT_H * rows) / 2;

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
				int posX = craftX + x * SLOT_W;
				int posY = craftY + y * SLOT_H;
				
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
		
		int sepY = SLOT_H * 2;
		
		int outputX = getSlotsArea(topSlots).right + sep + 1;
		int outputY = (topAreaHeight - SLOT_H * 2) / 2;
		
		outputSlotMain = addTopAlignedSlot(new SlotCrafting(invPlayer.player, workbench.getCraftingInventory(), workbench.getCraftingOutput(), workbench.SLOT_OUTPUT_MAIN, outputX, outputY));
		outputSlotWaste = addTopAlignedSlot(new Slot(workbench, workbench.SLOT_OUTPUT_WASTE, outputX, outputY + SLOT_H)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
		});
		
		setupGUILayout();
	}
	
	protected int[] prevProgresses = new int[TileEntityKnapper.SLOTS_CRAFTING_COUNT];
	
	protected void sendGUIData(boolean force)
	{
		for (int i = 0; i < prevProgresses.length; i++)
		{
			KnappingState curState = workbench.getKnappingSlotState(i);
			int progress = curState.getProgress();
			
			if (force || prevProgresses[i] != progress)
			{
				for (ICrafting crafting : (List<ICrafting>) crafters)
				{
					crafting.sendProgressBarUpdate(this, i, progress);
				}
				
				prevProgresses[i] = progress;
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
        ItemStack oldOutput = (ItemStack) inventoryItemStacks.get(outputSlot);
        ItemStack newOutput = ((Slot) inventorySlots.get(outputSlot)).getStack();
        
        if (!ItemStack.areItemStacksEqual(oldOutput, newOutput))
        {
	        for (ICrafting crafting : (List<ICrafting>) crafters)
	        {
	        	if (crafting instanceof EntityPlayerMP)
	        	{
	        		((EntityPlayerMP) crafting).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(windowId, outputSlot, newOutput));
	        	}
	        }
        }
		
		super.detectAndSendChanges();
		
		sendGUIData(changeTicks % 5 == 0);
		changeTicks++;
	}
	
	@Override
	public void updateProgressBar(int id, int value)
	{
		super.updateProgressBar(id, value);
		
		KnappingState state = workbench.getKnappingSlotState(id);
		
		if (state != null)
		{
			state.setProgress(value);
		}
	}
	
	@Override
	public ItemStack slotClick(int slotID, int button, int mode, EntityPlayer player)
	{
		if (slotID >= 0)
		{
			Slot slot = (Slot) inventorySlots.get(slotID);
			
			if (slot == knappingInputSlotLocked)
			{
				workbench.setKnappingMaterialLocked(null);
				workbench.resetKnappingState();
				return null;
			}
			
			if (slot.getHasStack() && (slot == outputSlotMain || slot == outputSlotWaste))
			{
				ItemStack old = super.slotClick(slotID, button, mode, player);
				
				if (slot == outputSlotMain)
				{
					if (!slot.getHasStack())
					{
						workbench.resetKnappingState();
					}
					else if (old.stackSize != slot.getStack().stackSize)
					{
						workbench.setKnappingSlotsLocked(true);
					}
				}
				
				return old;
			}
		}
		
		return super.slotClick(slotID, button, mode, player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		if (slotID >= 0)
		{
			Slot slot = (Slot) inventorySlots.get(slotID);
			
			if (slot == outputSlotMain && slot.getHasStack())
			{
				ItemStack oldStack = slot.getStack().copy();
				
				while (slot.getHasStack())
				{
					mergeStackToPlayerInv(slot.getStack(), true);
					
					if (slot.getHasStack())
						oldStack.stackSize -= slot.getStack().stackSize;
					
					slot.onPickupFromSlot(player, oldStack);
					workbench.resetKnappingState();
					workbench.updateRecipeOutput();
					
					if (slot.getHasStack())
						oldStack = slot.getStack().copy();
				}
				
				return null;
			}
		}
		
		return super.transferStackInSlot(player, slotID);
	}
}
