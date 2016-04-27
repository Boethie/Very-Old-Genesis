package genesis.combo;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

@FunctionalInterface
public interface ObjectFunction<B extends Block, I extends Item>
{
	void apply(B block, I item);
}
