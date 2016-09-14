package genesis.block.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntityBase extends TileEntity
{
	@Override
	public void markDirty()
	{
		if (worldObj != null && !worldObj.isAirBlock(pos))
		{
			IBlockState oldState = getBlockType().getStateFromMeta(getBlockMetadata());

			super.markDirty();

			worldObj.notifyBlockUpdate(pos, oldState, worldObj.getBlockState(pos), 0b1000);
		}
	}

	protected abstract void writeVisualData(NBTTagCompound compound, boolean save);

	protected abstract void readVisualData(NBTTagCompound compound, boolean save);

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		readVisualData(packet.getNbtCompound(), false);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		writeVisualData(compound, true);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.markDirty();

		readVisualData(compound, true);
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound compound = new NBTTagCompound();
		super.writeToNBT(compound);
		writeVisualData(compound, false);
		return compound;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return !oldState.equals(newState);
	}
}
