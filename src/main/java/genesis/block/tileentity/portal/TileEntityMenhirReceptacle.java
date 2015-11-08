package genesis.block.tileentity.portal;

import genesis.block.tileentity.TileEntityBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public class TileEntityMenhirReceptacle extends TileEntityBase implements IUpdatePlayerListBox
{
	protected ItemStack containedItem = null;
	protected byte timer = 0;
	
	public TileEntityMenhirReceptacle()
	{
	}
	
	public void setContainedItem(ItemStack stack)
	{
		containedItem = stack;
		sendDescriptionPacket();
		GenesisPortal.fromMenhirBlock(worldObj, pos).updatePortalStatus(worldObj);
	}
	
	public ItemStack getReceptacleItem()
	{
		return containedItem;
	}
	
	public boolean isReceptacleActive()
	{
		return getReceptacleItem() != null;
	}
	
	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			timer--;
			
			if (timer <= 0)
			{
				GenesisPortal.fromMenhirBlock(worldObj, pos).updatePortalStatus(worldObj);
				timer = GenesisPortal.PORTAL_CHECK_TIME;
			}
		}
	}
	
	@Override
	protected void writeVisualData(NBTTagCompound compound, boolean save)
	{
		if (containedItem != null)
		{
			compound.setTag("containedItem", containedItem.writeToNBT(new NBTTagCompound()));
		}
		else
		{
			compound.removeTag("containedItem");
		}
	}
	
	@Override
	protected void readVisualData(NBTTagCompound compound, boolean save)
	{
		if (compound.hasKey("containedItem"))
		{
			containedItem = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("containedItem"));
		}
		else
		{
			containedItem = null;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		compound.setByte("timer", timer);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		timer = compound.getByte("timer");
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
	}
}
