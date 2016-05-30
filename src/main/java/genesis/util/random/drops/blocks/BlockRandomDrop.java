package genesis.util.random.drops.blocks;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockRandomDrop extends BlockDrop
{
	public final BlockDrop drop;
	public final double chance;

	/**
	 * @param drop The base drop
	 * @param chance The decimal chance of dropping
	 */
	public BlockRandomDrop(BlockDrop drop, double chance)
	{
		super(1);

		this.drop = drop;
		this.chance = chance;
	}

	public ItemStack getStack(IBlockState state, Random rand, int size)
	{
		return rand.nextDouble() < chance ? drop.getStack(state, size) : null;
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
		return rand.nextDouble() < chance ? drop.getStack(state, rand) : null;
	}
}
