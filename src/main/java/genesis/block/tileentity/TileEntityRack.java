package genesis.block.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRack extends TileEntityBase implements IInventory
{
	private ItemStack[] inventory = new ItemStack[EnumFacing.HORIZONTALS.length];
	
	public static boolean isItemValid(ItemStack stack)
	{
		if (stack != null)
		{
			Item item = stack.getItem();
			
			return (item instanceof ItemBlock)
					|| (item instanceof ItemPotion)
					|| (item instanceof ItemBucketMilk)
					|| (item instanceof ItemSoup)
					|| item == Items.LAVA_BUCKET
					|| item == Items.WATER_BUCKET
					|| item == Items.CAKE;
		}
		
		return false;
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
	
	public boolean hasRack(EnumFacing facing)
	{
		return worldObj.getBlockState(pos).getValue(BlockRack.RACKS.get(facing));
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if (stack != null)
		{
			stack.stackSize = Math.min(stack.stackSize, getInventoryStackLimit());
			
			// Check whether there's a rack where the item is being placed.
			if (!hasRack(EnumFacing.getHorizontal(index)))
				stack = null;
		}
		
		inventory[index] = stack;
		markDirty();
	}
	
	public ItemStack getStackInSide(EnumFacing facing)
	{
		return getStackInSlot(facing.getHorizontalIndex());
	}
	
	public void setStackInSide(EnumFacing facing, ItemStack stack)
	{
		setInventorySlotContents(facing.getHorizontalIndex(), stack);
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return stack != null && isItemValid(stack);
	}
	
	@Override
	protected void writeVisualData(NBTTagCompound compound, boolean save)
	{
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
	protected void readVisualData(NBTTagCompound compound, boolean save)
	{
		NBTTagList list = compound.getTagList("items", NBT.TAG_COMPOUND);
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
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
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
}
