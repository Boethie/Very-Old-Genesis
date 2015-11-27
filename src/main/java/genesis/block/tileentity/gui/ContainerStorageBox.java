package genesis.block.tileentity.gui;

import genesis.block.tileentity.TileEntityStorageBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerStorageBox extends ContainerBase
{
	public ContainerStorageBox(EntityPlayer player, TileEntityStorageBox box)
	{
		super(player.inventory, box);
		
		int boxCount = 0;
		int yPos = 0;
		int xPosBase = 0;
		int xPos = 0;
		
		for (TileEntityStorageBox addBox : box.iterableFromMainToEnd())
		{
			if (boxCount > 0 && boxCount % 2 == 0)
			{
				yPos = 0;
				xPosBase += slotW * addBox.getSlotsWidth() + paddingX;
			}
			
			for (int y = 0; y < addBox.getSlotsHeight(); y++)
			{
				xPos = xPosBase;
				
				for (int x = 0; x < addBox.getSlotsWidth(); x++)
				{
					addTopAlignedSlot(new Slot(addBox, y * addBox.getSlotsWidth() + x, xPos, yPos));
					xPos += slotW;
				}
				
				yPos += slotH;
			}
			
			boxCount++;
		}
		
		setUpGUILayout();
		
		inventory.openInventory(player);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		
		inventory.closeInventory(player);
	}
}
