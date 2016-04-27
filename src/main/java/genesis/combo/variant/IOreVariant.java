package genesis.combo.variant;

import java.util.*;

import genesis.util.random.i.IntRange;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface IOreVariant<V> extends IMetadata<V>
{
	int getHarvestLevel();
	float getHardness();
	float getExplosionResistance();
	IntRange getDropExperience();
	float getSmeltingExperience();
	List<ItemStack> getDrops(IBlockState state, Random rand);
}
