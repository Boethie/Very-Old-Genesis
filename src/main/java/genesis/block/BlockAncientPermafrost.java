package genesis.block;

import genesis.common.GenesisItems;
import genesis.metadata.EnumFood;
import genesis.util.WorldUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockAncientPermafrost extends BlockPermafrost
{
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		Random rand = WorldUtils.getWorldRandom(world, RANDOM);
		int chance = rand.nextInt(100);
		ItemStack stack = null;
		
		// if (chance < 3) stack = new ItemStack(GenesisItems.necklace);
		if (chance < 5)
		{
			stack = new ItemStack(Items.arrow);
		}
		else if (chance < 7)
		{
			stack = new ItemStack(Items.bow);
		}
		else if (chance < 10)
		{
			stack = new ItemStack(Items.wooden_pickaxe);// pick
		}
		else if (chance < 35)
		{
			stack = new ItemStack(Items.bone);
		}
		else if (chance < 40)
		{
			stack = GenesisItems.foods.getRawStack(EnumFood.ERYOPS_LEG);// meat
		}
		
		if (stack == null)
		{
			return Collections.emptyList();
		}
		
		return Collections.singletonList(stack);
	}
}
