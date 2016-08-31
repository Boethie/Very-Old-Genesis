package genesis.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityRottenStorageBox extends TileEntity
{
	public static final int SLOTS_W = 3;
	public static final int SLOTS_H = 2;
	public static final int SLOTS_COUNT = SLOTS_W * SLOTS_H;
	
	protected ItemStack[] inventory = new ItemStack[SLOTS_W * SLOTS_H];
	
	@Override
	public BlockRottenStorageBox getBlockType()
	{
		return (BlockRottenStorageBox) super.getBlockType();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		
		NBTTagList itemList = new NBTTagList();
		int i = 0;
		
		for (ItemStack stack : inventory)
		{
			if (stack != null)
			{
				NBTTagCompound itemComp = new NBTTagCompound();
				itemComp.setByte("slot", (byte) i);
				stack.writeToNBT(itemComp);
				
				itemList.appendTag(itemComp);
			}
			
			i++;
		}
		
		compound.setTag("items", itemList);

		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		NBTTagList tagList = compound.getTagList("items", NBT.TAG_COMPOUND);
		
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound itemCompound = (NBTTagCompound) tagList.get(i);
			byte slot = itemCompound.getByte("slot");
			
			if (slot >= 0 && slot < inventory.length)
			{
				inventory[slot] = ItemStack.loadItemStackFromNBT(itemCompound);
			}
		}
	}
}
