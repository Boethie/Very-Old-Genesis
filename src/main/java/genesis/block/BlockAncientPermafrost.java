package genesis.block;

import genesis.common.GenesisItems;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockAncientPermafrost extends BlockPermafrost
{
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		int chance = rand.nextInt(100);

		// if (chance < 3) return GenesisItems.necklace;
		if (chance < 5)
		{
			return Items.arrow;
		}
		else if (chance < 7)
		{
			return Items.bow;
		}
		else if (chance < 10)
		{
			return Items.wooden_pickaxe;// pick
		}
		else if (chance < 35)
		{
			return Items.bone;
		}
		else if (chance < 40)
		{
			return GenesisItems.eryops_leg;// meat
		}

		return null;
	}
}
