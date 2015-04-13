package genesis.item;

import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;
import genesis.util.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemGenesisBucketMilk extends Item
{
	public ItemGenesisBucketMilk()
	{
		this.setMaxStackSize(1);
		this.setCreativeTab(GenesisCreativeTabs.MISC);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		if (!playerIn.capabilities.isCreativeMode)
		{
			--stack.stackSize;
		}

		if (!worldIn.isRemote)
		{
			playerIn.curePotionEffects(new ItemStack(Items.milk_bucket));
		}

		playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
		return stack.stackSize <= 0 ? new ItemStack(GenesisItems.ceramic_bucket) : stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		return itemStackIn;
	}

	@Override
	public ItemGenesisBucketMilk setUnlocalizedName(String unlocalizedName)
	{
		super.setUnlocalizedName(Constants.PREFIX + "misc." + unlocalizedName);

		return this;
	}
}
