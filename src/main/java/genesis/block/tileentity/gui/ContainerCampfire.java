package genesis.block.tileentity.gui;

import java.util.*;

import genesis.block.tileentity.*;
import genesis.util.gui.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

public class ContainerCampfire extends ContainerBase
{
	protected TileEntityCampfire campfire;

	protected int lastCookTime;
	protected int lastBurnTime;
	protected int lastItemBurnTime;
	
	public final RestrictedSlot input;
	public final RestrictedSlot fuel;
	public final SlotFurnaceOutput output;
	public final RestrictedSlot ingredient1;
	public final RestrictedSlot ingredient2;

	public ContainerCampfire(InventoryPlayer inventoryPlayer, TileEntityCampfire te)
	{
		super(inventoryPlayer, te, UI_WIDTH, UI_HEIGHT + 26);
		
		campfire = te;
		
		int y = 0;
		ingredient1 = addTopAlignedSlot(new RestrictedDisabledSlot(te, TileEntityCampfire.SLOT_INGREDIENT_1, -64, y));
		ingredient2 = addTopAlignedSlot(new RestrictedDisabledSlot(te, TileEntityCampfire.SLOT_INGREDIENT_2, -32, y));
		input = addTopAlignedSlot(new RestrictedSlot(te, TileEntityCampfire.SLOT_INPUT, -48, y += SLOT_H + 8));
		fuel = addTopAlignedSlot(new RestrictedSlot(te, TileEntityCampfire.SLOT_FUEL, -48, y += SLOT_H * 2));
		output = addBigTopAlignedSlot(new SlotFurnaceOutput(inventoryPlayer.player, te, TileEntityCampfire.SLOT_OUTPUT, 48, y /= 2));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value)
	{
		switch (id)
		{
		case 0:
			campfire.cookTime = value;
			break;
		case 1:
			campfire.burnTime = value;
			break;
		case 2:
			campfire.totalBurnTime = value;
			break;
		}
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting iCrafting)
	{
		super.addCraftingToCrafters(iCrafting);
		
		iCrafting.sendProgressBarUpdate(this, 0, campfire.cookTime);
		iCrafting.sendProgressBarUpdate(this, 1, campfire.burnTime);
		iCrafting.sendProgressBarUpdate(this, 2, campfire.totalBurnTime);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for (ICrafting iCrafting : (List<ICrafting>) crafters)
		{
			if (lastCookTime != campfire.cookTime)
			{
				iCrafting.sendProgressBarUpdate(this, 0, campfire.cookTime);
			}
			
			if (lastBurnTime != campfire.burnTime)
			{
				iCrafting.sendProgressBarUpdate(this, 1, campfire.burnTime);
			}
			
			if (lastItemBurnTime != campfire.totalBurnTime)
			{
				iCrafting.sendProgressBarUpdate(this, 2, campfire.totalBurnTime);
			}
		}
		
		lastCookTime = campfire.cookTime;
		lastBurnTime = campfire.burnTime;
		lastItemBurnTime = campfire.totalBurnTime;
	}
}