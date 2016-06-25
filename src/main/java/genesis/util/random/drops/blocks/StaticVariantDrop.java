package genesis.util.random.drops.blocks;

import java.util.Random;

import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.variant.IMetadata;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class StaticVariantDrop<V extends IMetadata<V>> extends VariantDrop<V>
{
	public static <V extends IMetadata<V>>
	StaticVariantDrop<V> create(VariantsOfTypesCombo<V> combo, ObjectType<V, ?, ?> type, V variant, int min, int max)
	{
		return new StaticVariantDrop<>(combo, type, variant, min, max);
	}
	
	public static <V extends IMetadata<V>>
	StaticVariantDrop<V> create(VariantsOfTypesCombo<V> combo, ObjectType<V, ?, ?> type, V variant, int size)
	{
		return new StaticVariantDrop<>(combo, type, variant, size);
	}
	
	public V variant;
	
	public StaticVariantDrop(VariantsOfTypesCombo<V> combo, ObjectType<V, ?, ?> type, V variant, int min, int max)
	{
		super(combo, type, min, max);
		this.variant = variant;
	}
	
	public StaticVariantDrop(VariantsOfTypesCombo<V> combo, ObjectType<V, ?, ?> type, V variant, int size)
	{
		super(combo, type, size);
		this.variant = variant;
	}
	
	@Override
	public ItemStack getStack(IBlockState state, Random rand, int size)
	{
		return combo.getStack(type, variant, size);
	}
}
