package genesis.common;

import genesis.block.tileentity.*;
import genesis.block.tileentity.gui.*;
import genesis.block.tileentity.gui.render.*;

import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import net.minecraftforge.fml.common.network.*;

public class GenesisGuiHandler implements IGuiHandler
{
	public static final int CAMPFIRE_ID = 0;
	
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
    	BlockPos pos = new BlockPos(x, y, z);
    	
        switch (id)
        {
        case CAMPFIRE_ID:
            return new ContainerCampfire(player.inventory, (TileEntityCampfire) world.getTileEntity(pos));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
    	BlockPos pos = new BlockPos(x, y, z);
    	
        switch (id)
        {
        case CAMPFIRE_ID:
            return new GuiContainerCampfire(player.inventory, (TileEntityCampfire) world.getTileEntity(pos));
        }

        return null;
    }
}
