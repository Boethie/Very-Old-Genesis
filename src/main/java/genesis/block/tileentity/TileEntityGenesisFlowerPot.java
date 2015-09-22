package genesis.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGenesisFlowerPot extends TileEntity
{
	private ItemStack contents;

	public TileEntityGenesisFlowerPot() {}

	public TileEntityGenesisFlowerPot(ItemStack contents)
	{
		setContents(contents);
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		NBTTagCompound contentsNBT = new NBTTagCompound();
		contents.writeToNBT(contentsNBT);
		compound.setTag("contents", contentsNBT);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		contents = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("contents"));
	}

	public S35PacketUpdateTileEntity getDescriptionPacket()
	{
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		return new S35PacketUpdateTileEntity(pos, 5, compound);
	}
	
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
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