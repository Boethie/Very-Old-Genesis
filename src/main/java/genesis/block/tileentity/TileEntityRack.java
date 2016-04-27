package genesis.block.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRack extends TileEntity implements IInventory
{
	private ItemStack[] inventory = new ItemStack[1];
	
	public static boolean isItemValid(ItemStack stack)
	{
		if (stack != null)
		{
			Item item = stack.getItem();
			
			return (item instanceof ItemBlock)
					|| (item instanceof ItemPotion)
					|| (item instanceof ItemBucketMilk)
					|| (item instanceof ItemSoup)
					|| item == Items.lava_bucket
					|| item == Items.water_bucket
					|| item == Items.cake;
		}
		
		return false;
	}
	
	@Override
	public Packet<?> getDescriptionPacket()
	{
		NBTTagCompound data = new NBTTagCompound();
		writeToNBT(data);
		return new SPacketUpdateTileEntity(pos, 1, data);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		readFromNBT(packet.getNbtCompound());
		this.markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		NBTTagList list = compound.getTagList("items", 10);
		inventory = new ItemStack[getSizeInventory()];
		
		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound itemCompound = list.getCompoundTagAt(i);
			int slot = itemCompound.getByte("slot");
			
			if (slot >= 0 && slot < inventory.length)
			{
				inventory[slot] = ItemStack.loadItemStackFromNBT(itemCompound);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		NBTTagList list = new NBTTagList();
		
		for (int i = 0; i < inventory.length; ++i)
		{
			if (inventory[i] != null)
			{
				NBTTagCompound itemCompound = new NBTTagCompound();
				itemCompound.setByte("slot", (byte) i);
				inventory[i].writeToNBT(itemCompound);
				list.appendTag(itemCompound);
			}
		}
		
		compound.setTag("items", list);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 65536;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(pos.getX() - 0.5, pos.getY(), pos.getZ() - 0.5,
				pos.getX() + 1.5, pos.getY() + 1.5, pos.getZ() + 1.5);
	}
	
	@Override
	public String getName()
	{
		return null;
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return null;
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory[index];
	}
	
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return isItemValidForSlot(index, itemStackIn);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(inventory, index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(inventory, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory[index] = stack;
		
		if (stack != null && stack.stackSize > getInventoryStackLimit())
		{
			stack.stackSize = getInventoryStackLimit();
		}
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{

	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{

	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return stack != null && isItemValid(stack);
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
	}
	
	public int getDirection()
	{
		return this.getBlockMetadata();
	}
}
