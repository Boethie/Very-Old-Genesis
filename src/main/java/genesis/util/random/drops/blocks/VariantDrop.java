package genesis.util.random.drops.blocks;

import java.util.Random;

import genesis.combo.*;
import genesis.combo.variant.IMetadata;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class VariantDrop<V extends IMetadata<V>> extends BlockDrop
{
	public static <V extends IMetadata<V>>
	VariantDrop<V> create(VariantsOfTypesCombo<V> combo, ObjectType<V, ?, ?> type, int min, int max)
	{
		return new VariantDrop<>(combo, type, min, max);
	}
	
	public static <V extends IMetadata<V>>
	VariantDrop<V> create(VariantsOfTypesCombo<V> combo, ObjectType<V, ?, ?> type, int size)
	{
		return new VariantDrop<>(combo, type, size);
	}
	
	public VariantsOfTypesCombo<V> combo;
	public ObjectType<V, ?, ?> type;
	
	public VariantDrop(VariantsOfTypesCombo<V> combo, ObjectType<V, ?, ?> type, int min, int max)
	{
		super(min, max);
		
		this.combo = combo;
		this.type = type;
	}
	
	public VariantDrop(VariantsOfTypesCombo<V> combo, ObjectType<V, ?, ?> type, int size)
	{
		this(combo, type, size, size);
	}
	
	@Override
	public ItemStack getStack(IBlockState state, Random rand, int size)
	{
		return combo.getStack(type, combo.getVariant(state), size);
	}
}
