package genesis.util.random.drops.blocks;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface BlockStackProvider
{
	ItemStack getStack(IBlockState state, Random rand);
}