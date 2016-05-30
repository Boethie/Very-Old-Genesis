package genesis.util.random.drops.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockDrops
{
	protected ImmutableList<? extends BlockDrop> drops;
	
	/**
	 * Note: This does <i>not</i> choose a {@link BlockDrop} to use randomly from the array that is passed, it combines the stacks in a list.
	 * @param drops The {@link BlockDrop}s to use to fill a {@link List}<{@link ItemStack}> with randomly generated stacks.
	 */
	public BlockDrops(BlockDrop... drops)
	{
		this.drops = ImmutableList.copyOf(drops);
	}
	
	public BlockDrops(ItemStack stack, int min, int max)
	{
		this(new BlockStackDrop(stack, min, max));
	}
	
	public BlockDrops(ItemStack stack, int size)
	{
		this(stack, size, size);
	}
	
	public BlockDrops(Item item, int min, int max)
	{
		this(new ItemStack(item), min, max);
	}
	
	public BlockDrops(Item item, int size)
	{
		this(item, size, size);
	}
	
	public BlockDrops(Block item, int min, int max)
	{
		this(new ItemStack(item), min, max);
	}
	
	public BlockDrops(Block item, int size)
	{
		this(item, size, size);
	}
	
	public List<? extends BlockDrop> getDrops()
	{
		return drops;
	}
	
	public List<ItemStack> getDrops(IBlockState state, Random rand)
	{
		ArrayList<ItemStack> out = Lists.newArrayList();
		
		for (BlockDrop drop : getDrops())
		{
			ItemStack stack = drop.getStack(state, rand);

			if (stack != null)
			{
				out.add(stack);
			}
		}
		
		return out;
	}
}
