package genesis.block.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntityBase extends TileEntity
{
	@Override
	public void markDirty()
	{
		super.markDirty();
		
		if (worldObj != null)
		{
			IBlockState state = worldObj.getBlockState(pos);
			worldObj.notifyBlockUpdate(pos, state, state, 0b1000);
		}
	}
	
	protected abstract void writeVisualData(NBTTagCompound compound, boolean save);
	
	protected abstract void readVisualData(NBTTagCompound compound, boolean save);
	
	@Override
	public Packet<?> getDescriptionPacket()
	{
		NBTTagCompound compound = new NBTTagCompound();
		writeVisualData(compound, false);
		return new SPacketUpdateTileEntity(pos, 0, compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		readVisualData(packet.getNbtCompound(), false);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		writeVisualData(compound, true);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		readVisualData(compound, true);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return !oldState.equals(newState);
	}
}
