package genesis.util.random.drops.blocks;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.util.random.i.IntRange;

public abstract class BlockDrop implements BlockStackProvider
{
	protected final IntRange range;
	
	public BlockDrop(int min, int max)
	{
		range = IntRange.create(min, max);
	}
	
	public BlockDrop(int size)
	{
		range = IntRange.create(size);
	}
	
	@Override
	public final ItemStack getStack(IBlockState state, Random rand)
	{
		return getStack(state, rand, range.get(rand));
	}
	
	public abstract ItemStack getStack(IBlockState state, Random rand, int size);
}