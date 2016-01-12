package genesis.block.tileentity.gui;

import genesis.block.tileentity.*;
import genesis.util.gui.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
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
	public final RestrictedSlot[] ingredients;
	//public final RestrictedSlot ingredient1;
	//public final RestrictedSlot ingredient2;

	public ContainerCampfire(EntityPlayer player, TileEntityCampfire te)
	{
		super(player.inventory, te);
		
		campfire = te;
		
		int y = 0;
		
		int ingSep = slotW + 6;
		ingredients = new RestrictedSlot[TileEntityCampfire.SLOTS_INGREDIENTS_COUNT];
		
		for (int i = 0; i < TileEntityCampfire.SLOTS_INGREDIENTS_COUNT; i++)
			ingredients[i] = addTopAlignedSlot(new RestrictedDisabledSlot(te, TileEntityCampfire.SLOTS_INGREDIENTS_START + i, i * ingSep, y));
		//ingredient1 = addTopAlignedSlot(new RestrictedDisabledSlot(te, TileEntityCampfire.SLOT_INGREDIENT_1, 0, y));
		//ingredient2 = addTopAlignedSlot(new RestrictedDisabledSlot(te, TileEntityCampfire.SLOT_INGREDIENT_2, ingSep, y));
		
		int leftWidth = (TileEntityCampfire.SLOTS_INGREDIENTS_COUNT - 1) * ingSep;
		
		input = addTopAlignedSlot(new RestrictedSlot(te, TileEntityCampfire.SLOT_INPUT, leftWidth / 2, y += slotH + 8));
		fuel = addTopAlignedSlot(new RestrictedSlot(te, TileEntityCampfire.SLOT_FUEL, leftWidth / 2, y += slotH * 2));
		output = addBigTopAlignedSlot(new SlotFurnaceOutput(player, te, TileEntityCampfire.SLOT_OUTPUT, 112, y /= 2));
		
		setUpGUILayout();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value)
	{
		super.updateProgressBar(id, value);
		
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
	public void onCraftGuiOpened(ICrafting iCrafting)
	{
		super.onCraftGuiOpened(iCrafting);
		
		iCrafting.sendProgressBarUpdate(this, 0, campfire.cookTime);
		iCrafting.sendProgressBarUpdate(this, 1, campfire.burnTime);
		iCrafting.sendProgressBarUpdate(this, 2, campfire.totalBurnTime);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		// TODO: Remove cast
		for (ICrafting iCrafting : crafters)
		{
			if (lastCookTime != campfire.cookTime)
			{
				iCrafting.sendProgressBarUpdate(this, 0, campfire.cookTime);
			}
			
			if (lastBurnTime != campfire.burnTime || campfire.isWet())
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