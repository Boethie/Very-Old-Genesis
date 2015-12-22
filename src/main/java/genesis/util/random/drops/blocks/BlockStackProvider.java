package genesis.util.random.drops.blocks;

import java.util.Random;

import genesis.util.random.drops.StackProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface BlockStackProvider extends StackProvider
{
	ItemStack getStack(IBlockState state, Random rand);
	ItemStack getStack(IBlockState state, int size);
}