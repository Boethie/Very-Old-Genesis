package genesis.util.random.drops;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockMultiDrop extends BlockDrop
{
	protected final ImmutableList<BlockDrop> drops;
	
	public BlockMultiDrop(BlockDrop... drops)
	{
		super(0, drops.length - 1);
		
		this.drops = ImmutableList.copyOf(drops);
	}
	
	public BlockMultiDrop(List<BlockDrop> drops)
	{
		super(0, drops.size() - 1);
		
		this.drops = ImmutableList.copyOf(drops);
	}
	
	public BlockDrop getDrop(Random rand)
	{
		return drops.get(get(rand));
	}
	
	public ItemStack getStack(IBlockState state, Random rand, int size)
	{
		return getDrop(rand).getStack(state, size);
	}
	
	@Override
	@Deprecated
	public ItemStack getStack(IBlockState state, int size)
	{
		return getStack(state, new Random(), size);
	}
	
	@Override
	public ItemStack getStack(IBlockState state, Random rand)
	{
		return getDrop(rand).getStack(state, rand);
	}
}
