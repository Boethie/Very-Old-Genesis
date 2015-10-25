package genesis.block.tileentity.portal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class TileEntityGenesisPortal extends TileEntity implements IUpdatePlayerListBox
{
	protected AxisAlignedBB bounds = null;
	
	public TileEntityGenesisPortal()
	{
	}
	
	public void setPos(BlockPos pos)
	{
		super.setPos(pos);
		
		bounds = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(),
				pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
	}
	
	@Override
	public void update()
	{
		List<EntityPlayer> players = (List<EntityPlayer>) worldObj.getEntitiesWithinAABB(EntityPlayer.class, bounds);
	}
}
