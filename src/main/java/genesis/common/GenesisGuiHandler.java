package genesis.common;

import genesis.block.tileentity.TileEntityCampfire;
import genesis.block.tileentity.gui.ContainerCampfire;
import genesis.block.tileentity.gui.render.GuiContainerCampfire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

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
