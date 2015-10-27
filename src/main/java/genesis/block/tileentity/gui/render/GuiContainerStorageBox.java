package genesis.block.tileentity.gui.render;

import genesis.block.tileentity.TileEntityStorageBox;
import genesis.block.tileentity.gui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class GuiContainerStorageBox extends GuiContainerBase
{
	public GuiContainerStorageBox(EntityPlayer player, TileEntityStorageBox te)
	{
		super(new ContainerStorageBox(player, te), te);
	}
	
	@Override
	protected void drawDisplayName(int color)
	{
		String displayName;
		if(inventorySlots.inventorySlots.size() > 63)
			displayName = StatCollector.translateToLocal("container.genesis.largeStorageBox");
		else
			displayName = namer.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(displayName, container.getPlayerInventoryArea().left, 6, color);
	}
}
