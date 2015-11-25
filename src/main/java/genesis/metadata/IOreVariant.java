package genesis.metadata;

import java.util.*;

import genesis.util.random.IntRange;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface IOreVariant<V> extends IMetadata<V>
{
	public int getHarvestLevel();
	public float getHardness();
	public float getExplosionResistance();
	public IntRange getDropExperience();
	public float getSmeltingExperience();
	public List<ItemStack> getDrops(IBlockState state, Random rand);
}
