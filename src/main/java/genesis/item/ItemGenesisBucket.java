package genesis.item;

import genesis.common.*;
import genesis.util.*;
import net.minecraft.block.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;

public class ItemGenesisBucket extends ItemBucket
{
	protected Block containedBlock;
	
	public ItemGenesisBucket(Block block)
	{
		super(block);
		
		containedBlock = block;
		
		if (isEmpty())
		{
			setMaxStackSize(16);
		}
		
		setCreativeTab(GenesisCreativeTabs.MISC);
	}
	
	public boolean isEmpty()
	{
		return containedBlock == Blocks.air;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target)
	{
		if (isEmpty() && target instanceof EntityCow)
		{
			if (stack.stackSize == 1)
			{
				stack.setItem(GenesisItems.ceramic_bucket_milk);
			}
			else
			{
				--stack.stackSize;
				playerIn.inventory.addItemStackToInventory(new ItemStack(GenesisItems.ceramic_bucket_milk));
			}
		}
		
		return true;
	}

	@Override
	public ItemGenesisBucket setUnlocalizedName(String unlocalizedName)
	{
		super.setUnlocalizedName(Constants.PREFIX + "misc." + unlocalizedName);

		return this;
	}
	
	@Override
	public ItemGenesisBucket setContainerItem(Item containerItem)
	{
		super.setContainerItem(containerItem);
		
		return this;
	}
}
