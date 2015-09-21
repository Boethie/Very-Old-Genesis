package genesis.block.tileentity.gui.render;

import genesis.block.tileentity.TileEntityStorageBox;
import genesis.block.tileentity.gui.*;
import net.minecraft.entity.player.EntityPlayer;

public class GuiContainerStorageBox extends GuiContainerBase
{
	public GuiContainerStorageBox(EntityPlayer player, TileEntityStorageBox te)
	{
		super(new ContainerStorageBox(player, te), te);
	}
}
