package genesis.block.tileentity.portal;

import genesis.block.tileentity.TileEntityBase;
import genesis.portal.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		for (BlockPos partPos : new MenhirData(world, pos, oldState))
		{
			world.checkLight(partPos);
		}
		
		return super.shouldRefresh(world, pos, oldState, newState);
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
		
		if (worldObj != null)
		{
			for (BlockPos pos : new MenhirData(worldObj, pos))
			{
				worldObj.checkLight(pos);
			}
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
