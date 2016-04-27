package genesis.util.random.drops;

import java.util.Random;

import genesis.util.random.i.RandomIntProvider;
import net.minecraft.item.ItemStack;

public interface StackProvider extends RandomIntProvider
{
	ItemStack getStack(int size);
	ItemStack getStack(Random rand);
}
