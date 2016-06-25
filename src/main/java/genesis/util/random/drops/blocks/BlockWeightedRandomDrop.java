package genesis.util.random.drops.blocks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import java.util.Random;

public class BlockWeightedRandomDrop implements BlockStackProvider
{
	protected final ImmutableList<WeightedRandomBlockDrop> drops;
	
	public BlockWeightedRandomDrop(WeightedRandomBlockDrop... drops)
	{
		this.drops = ImmutableList.copyOf(drops);
	}
	
	public BlockDrop getDrop(Random rand)
	{
		return WeightedRandom.getRandomItem(rand, drops).drop;
	}
	
	@Override
	public ItemStack getStack(IBlockState state, Random rand)
	{
		return getDrop(rand).getStack(state, rand);
	}
	
	public static class WeightedRandomBlockDrop extends WeightedRandom.Item
	{
		public final BlockDrop drop;
		
		public WeightedRandomBlockDrop(BlockDrop drop, int weight)
		{
			super(weight);
			this.drop = drop;
		}
	}
}
