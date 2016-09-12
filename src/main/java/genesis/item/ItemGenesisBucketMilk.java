package genesis.item;

import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemGenesisBucketMilk extends ItemBucketMilk
{
	public ItemGenesisBucketMilk()
	{
		setCreativeTab(GenesisCreativeTabs.MISC);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
	{
		EntityPlayer player = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
		
		if (player == null || !player.capabilities.isCreativeMode)
			--stack.stackSize;
		
		if (!world.isRemote)
			entity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
		
		if (player != null)
			player.addStat(StatList.getObjectUseStats(this));
		
		return stack.stackSize <= 0 ? new ItemStack(GenesisItems.CERAMIC_BUCKET) : stack;
	}
}
