package genesis.util.range;

import java.util.Random;

import net.minecraft.item.ItemStack;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.range.IntRange.Range;

@SuppressWarnings({"rawtypes", "unchecked"})
public class RandomVariantDrop extends IntRange.Range
{
	public VariantsOfTypesCombo combo;
	public ObjectType type;
	
	public RandomVariantDrop(VariantsOfTypesCombo combo, ObjectType type, int min, int max)
	{
		super(min, max);
		
		this.combo = combo;
		this.type = type;
	}
	
	public ItemStack getRandomStack(IMetadata variant, Random rand)
	{
		return combo.getStack(type, variant, get(rand));
	}
}
