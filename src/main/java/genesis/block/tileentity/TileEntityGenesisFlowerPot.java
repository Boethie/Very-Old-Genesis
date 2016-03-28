package genesis.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGenesisFlowerPot extends TileEntity
{
	private ItemStack contents;

	public TileEntityGenesisFlowerPot() {}

	public TileEntityGenesisFlowerPot(ItemStack contents)
	{
		setContents(contents);
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		NBTTagCompound contentsNBT = new NBTTagCompound();
		contents.writeToNBT(contentsNBT);
		compound.setTag("contents", contentsNBT);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		contents = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("contents"));
	}

	@Override
	public Packet<?> getDescriptionPacket()
	{
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		return new SPacketUpdateTileEntity(pos, 5, compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		readFromNBT(packet.getNbtCompound());
	}
	
	public void setContents(ItemStack contents)
	{
		this.contents = contents;
	}
	
	public ItemStack getContents()
	{
		return contents;
	}
}