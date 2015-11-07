package genesis.util.random.drops.blocks;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;

@SuppressWarnings("rawtypes")
public class VariantDrop<V extends IMetadata> extends BlockDrop
{
	public static <T extends IMetadata> VariantDrop<T> create(VariantsOfTypesCombo<T> combo, ObjectType type, int min, int max)
	{
		return new VariantDrop<T>(combo, type, min, max);
	}
	
	public static <T extends IMetadata> VariantDrop<T> create(VariantsOfTypesCombo<T> combo, ObjectType type, int size)
	{
		return new VariantDrop<T>(combo, type, size);
	}
	
	public VariantsOfTypesCombo<V> combo;
	public ObjectType type;
	
	public VariantDrop(VariantsOfTypesCombo<V> combo, ObjectType type, int min, int max)
	{
		super(min, max);
		
		this.combo = combo;
		this.type = type;
	}
	
	public VariantDrop(VariantsOfTypesCombo<V> combo, ObjectType type, int size)
	{
		this(combo, type, size, size);
	}
	
	@Override
	public ItemStack getStack(IBlockState state, int size)
	{
		return combo.getStack(type, combo.getVariant(state), size);
	}
	
	@Override
	public ItemStack getStack(IBlockState state, Random rand)
	{
		return getStack(state, get(rand));
	}
}
