package genesis.metadata;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.*;

import genesis.item.ItemGenesisFood;
import genesis.util.Constants.Unlocalized;
import genesis.util.ReflectionUtils;
import genesis.metadata.FoodItems.*;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class FoodItems extends VariantsCombo<FoodWrapper, Block, ItemGenesisFood<FoodWrapper>>
{
	private static final List<FoodWrapper> ORDERED_VARIANTS;
	private static final Map<EnumFood, Pair<FoodWrapper, FoodWrapper>> MAP_VARIANTS;
	
	static
	{
		ImmutableList.Builder<FoodWrapper> orderedBuilder = ImmutableList.builder();
		ImmutableMap.Builder<EnumFood, Pair<FoodWrapper, FoodWrapper>> mapBuilder = ImmutableMap.builder();
		
		for (final EnumFood food : EnumFood.values())
		{
			FoodWrapper raw = new FoodWrapper(food, true);
			FoodWrapper cooked = null;
			
			if (food.hasCookedVariant())
			{
				cooked = new FoodWrapper(food, false);
			}
			
			orderedBuilder.add(raw);
			orderedBuilder.add(cooked);
			mapBuilder.put(food, Pair.of(raw, cooked));
		}
		
		ORDERED_VARIANTS = orderedBuilder.build();
		MAP_VARIANTS = mapBuilder.build();
	}
	
	public static FoodWrapper getRawVariant(EnumFood food)
	{
		return MAP_VARIANTS.get(food).getLeft();
	}
	
	public static FoodWrapper getCookedVariant(EnumFood food)
	{
		return MAP_VARIANTS.get(food).getRight();
	}
	
	public FoodItems()
	{
		super(ObjectType.createItem("food", ReflectionUtils.<ItemGenesisFood<FoodWrapper>>convertClass(ItemGenesisFood.class)), FoodWrapper.class, ORDERED_VARIANTS);
		
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
	
	public static final class FoodWrapper implements IFood, IMetadata<FoodWrapper>
	{
		final EnumFood food;
		final boolean raw;
		
		private FoodWrapper(EnumFood food, boolean raw)
		{
			this.food = food;
			this.raw = raw;
		}
		
		@Override
		public String getName()
		{
			return (raw ? "" : "cooked_") + food.getName();
		}
		
		@Override
		public String getUnlocalizedName()
		{
			return food.getUnlocalizedName() + (raw ? ".raw" : ".cooked");
		}
		
		@Override
		public int getFoodAmount()
		{
			return raw ? food.getRawFoodAmount() : food.getCookedFoodAmount();
		}
		
		@Override
		public float getSaturationModifier()
		{
			return raw ? food.getRawSaturationModifier() : food.getCookedSaturationModifier();
		}
		
		public boolean isCooked()
		{
			return !raw;
		}
		
		@Override
		public int compareTo(FoodWrapper o)
		{
			return Integer.compare(ORDERED_VARIANTS.indexOf(this), ORDERED_VARIANTS.indexOf(o));
		}

		@Override
		public Iterable<PotionEffect> getEffects()
		{
			return raw ? food.getRawEffects() : food.getCookedEffects();
		}
	}
}
