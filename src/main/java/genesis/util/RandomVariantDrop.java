package genesis.util;

import java.util.Random;

import net.minecraft.item.ItemStack;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;

public class RandomVariantDrop extends RandomIntRange
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
		return combo.getStack(type, variant, getRandom(rand));
	}
}
