package genesis.metadata;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.*;

import genesis.item.ItemGenesisFood;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class FoodItems extends VariantsCombo<IFoodMetadata, Block, ItemGenesisFood<IFoodMetadata>>
{
	private static final List<IFoodMetadata> ORDERED_VARIANTS;
	private static final Map<EnumFood, Pair<IFoodMetadata, IFoodMetadata>> MAP_VARIANTS;
	
	static
	{
		ImmutableList.Builder<IFoodMetadata> orderedBuilder = ImmutableList.builder();
		ImmutableMap.Builder<EnumFood, Pair<IFoodMetadata, IFoodMetadata>> mapBuilder = ImmutableMap.builder();
		
		for (final EnumFood food : EnumFood.values())
		{
			IFoodMetadata raw = new IFoodMetadata()
			{
				@Override public String getName() { return food.getName(); }
				@Override public String getUnlocalizedName() { return food.getUnlocalizedName() + ".raw"; }
				@Override public int getFoodAmount() { return food.getRawFoodAmount(); }
				@Override public float getSaturationModifier() { return food.getRawSaturationModifier(); }
			};
			IFoodMetadata cooked = null;
			
			if (food.hasCookedVariant())
			{
				cooked = new IFoodMetadata()
				{
					@Override public String getName() { return "cooked_" + food.getName(); }
					@Override public String getUnlocalizedName() { return food.getUnlocalizedName() + ".cooked"; }
					@Override public int getFoodAmount() { return food.getCookedFoodAmount(); }
					@Override public float getSaturationModifier() { return food.getCookedSaturationModifier(); }
				};
			}
			
			orderedBuilder.add(raw);
			orderedBuilder.add(cooked);
			mapBuilder.put(food, Pair.of(raw, cooked));
		}
		
		ORDERED_VARIANTS = orderedBuilder.build();
		MAP_VARIANTS = mapBuilder.build();
	}
	
	public static IFoodMetadata getRawVariant(EnumFood food)
	{
		return MAP_VARIANTS.get(food).getLeft();
	}
	
	public static IFoodMetadata getCookedVariant(EnumFood food)
	{
		return MAP_VARIANTS.get(food).getRight();
	}
	
	public FoodItems()
	{
		super(ObjectType.createItem("food", ItemGenesisFood.class), ORDERED_VARIANTS);
		
		setUnlocalizedPrefix(Unlocalized.PREFIX);
		
		soleType.setResourceName("");
	}
	
	public ItemStack getRawStack(EnumFood variant)
	{
		return super.getStack(getRawVariant(variant));
	}
	
	public ItemStack getCookedStack(EnumFood variant)
	{
		if (!variant.hasCookedVariant())
		{
			return null;
		}
		
		return super.getStack(getCookedVariant(variant));
	}
}
