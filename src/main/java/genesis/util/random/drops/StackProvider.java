package genesis.util.random.drops;

import java.util.Random;

import genesis.util.random.Range;
import net.minecraft.item.ItemStack;

public interface StackProvider extends Range<Integer>
{
	ItemStack getStack(int size);
	ItemStack getStack(Random rand);
}
