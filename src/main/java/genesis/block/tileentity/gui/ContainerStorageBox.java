package genesis.block.tileentity.gui;

import java.util.Iterator;

import genesis.block.tileentity.BlockStorageBox;
import genesis.block.tileentity.TileEntityKnapper;
import genesis.block.tileentity.TileEntityStorageBox;
import genesis.common.Genesis;
import genesis.util.FacingHelpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.world.World;

public class ContainerStorageBox extends ContainerBase
{
	public ContainerStorageBox(EntityPlayer player, TileEntityStorageBox box)
	{
		super(player.inventory, box);
		
		World world = box.getWorld();
		BlockStorageBox block = box.getBlockType();
		
		int boxCount = 0;
		int yPos = 0;
		int xPosBase = 0;
		int xPos = 0;
		
		for (TileEntityStorageBox addBox : box.iterableFromMainToEnd())
		{
			if (boxCount > 0 && boxCount % 2 == 0)
			{
				yPos = 0;
				xPosBase += SLOT_W * addBox.getSlotsWidth() + paddingX;
			}
			
			for (int y = 0; y < addBox.getSlotsHeight(); y++)
			{
				xPos = xPosBase;
				
				for (int x = 0; x < addBox.getSlotsWidth(); x++)
				{
					addTopAlignedSlot(new Slot(addBox, y * addBox.getSlotsWidth() + x, xPos, yPos));
					xPos += SLOT_W;
				}
				
				yPos += SLOT_H;
			}
			
			boxCount++;
		}
		
		setupGUILayout();
		
		inventory.openInventory(player);
	}
	
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		
		inventory.closeInventory(player);
	}
}
