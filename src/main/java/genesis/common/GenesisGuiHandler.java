package genesis.common;

import genesis.block.tileentity.*;
import genesis.block.tileentity.gui.*;
import genesis.block.tileentity.gui.render.*;
import genesis.client.gui.GuiGenesisAchievements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import net.minecraftforge.fml.common.network.*;

public class GenesisGuiHandler implements IGuiHandler
{
	public static final int WORKBENCH_ID = 0;
	public static final int CAMPFIRE_ID = 1;
	public static final int STORAGE_BOX_ID = 2;
	public static final int GENESIS_ACHIEVEMENT_ID = 3;
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		
		switch (id)
		{
		case WORKBENCH_ID:
			return new ContainerKnapper(player, (TileEntityKnapper) world.getTileEntity(pos));
		case CAMPFIRE_ID:
			return new ContainerCampfire(player, (TileEntityCampfire) world.getTileEntity(pos));
		case STORAGE_BOX_ID:
			return new ContainerStorageBox(player, (TileEntityStorageBox) world.getTileEntity(pos));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		
		switch (id)
		{
		case WORKBENCH_ID:
			return new GuiContainerKnapper(player, (TileEntityKnapper) world.getTileEntity(pos));
		case CAMPFIRE_ID:
			return new GuiContainerCampfire(player, (TileEntityCampfire) world.getTileEntity(pos));
		case STORAGE_BOX_ID:
			return new GuiContainerStorageBox(player, (TileEntityStorageBox) world.getTileEntity(pos));
		case GENESIS_ACHIEVEMENT_ID:
			return new GuiGenesisAchievements(new GuiIngameMenu(), Minecraft.getMinecraft().thePlayer.getStatFileWriter(), world);
		}
		
		return null;
	}
}
