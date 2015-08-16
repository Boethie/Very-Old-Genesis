package genesis.item;

import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemGenesisBucketMilk extends ItemBucketMilk
{
	public ItemGenesisBucketMilk()
	{
		setCreativeTab(GenesisCreativeTabs.MISC);
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
}
