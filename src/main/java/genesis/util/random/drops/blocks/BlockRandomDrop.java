package genesis.util.random.drops.blocks;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockRandomDrop implements BlockStackProvider
{
	public final BlockDrop drop;
	public final float chance;
	
	/**
	 * @param drop The base drop
	 * @param chance The decimal chance of dropping
	 */
	public BlockRandomDrop(BlockDrop drop, float chance)
	{
		this.drop = drop;
		this.chance = chance;
	}
	
	@Override
	public ItemStack getStack(IBlockState state, Random rand)
	{
		return rand.nextFloat() < chance ? drop.getStack(state, rand) : null;
	}
}
