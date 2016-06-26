package genesis.util.random.drops.blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockMultiDrop implements BlockStackProvider
{
	protected final ImmutableList<BlockDrop> drops;
	
	public BlockMultiDrop(List<BlockDrop> drops)
	{
		this.drops = ImmutableList.copyOf(drops);
	}
	
	public BlockMultiDrop(BlockDrop... drops)
	{
		this(ImmutableList.copyOf(drops));
	}
	
	public BlockDrop getDrop(Random rand)
	{
		return drops.get(rand.nextInt(drops.size()));
	}
	
	@Override
	public ItemStack getStack(IBlockState state, Random rand)
	{
		return getDrop(rand).getStack(state, rand);
	}
}
