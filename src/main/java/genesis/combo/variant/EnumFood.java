package genesis.combo.variant;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.potion.PotionEffect;

public enum EnumFood implements IMetadata<EnumFood>
{
	SPIRIFER("spirifer", raw(2, 0.4F).cooked(3, 1)),
	CLIMATIUS("climatius", raw(2, 0.4F).cooked(5, 6)),
	MEGANEURA("meganeura", raw(2, 0.4F).cooked(3, 1)),
	APHTHOROBLATINNA("aphthoroblattina", raw(1, 0.2F).cooked(2, 0.8F)),
	ERYOPS_LEG("eryops_leg", "eryopsLeg", raw(2, 0.8F).cooked(5, 6)),
	GRYPHAEA("gryphaea", raw(2, 0.4F).cooked(3, 1)),
	CERATITES("ceratites", raw(2, 0.4F).cooked(4, 1.8F)),
	LIOPLEURODON("liopleurodon", raw(3, 2.8F).cooked(10, 13.8F)),
	TYRANNOSAURUS("tyrannosaurus", raw(3, 2.8F).cooked(10, 13.8F));
	
	final String name;
	final String unlocalizedName;
	final int rawFood;
	final float rawSaturation;
	final List<PotionEffect> rawEffects;
	
	final boolean hasCooked;
	final int cookedFood;
	final float cookedSaturation;
	final List<PotionEffect> cookedEffects;
	
	EnumFood(String name, String unlocalizedName, Props props)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		
		rawFood = props.rawFood;
		rawSaturation = props.rawSaturation;
		rawEffects = props.rawEffects;
		
		hasCooked = props.hasCooked;
		cookedFood = props.cookedFood;
		cookedSaturation = props.cookedSaturation;
		cookedEffects = props.cookedEffects;
	}
	
	EnumFood(String name, Props props)
	{
		this(name, name, props);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	// Raw variant getters
	public int getRawFoodAmount()
	{
		return rawFood;
	}
	
	public float getRawSaturationModifier()
	{
		return rawSaturation;
	}
	
	public Iterable<PotionEffect> getRawEffects()
	{
		return rawEffects;
	}
	
	// Cooked variant getters
	public boolean hasCookedVariant()
	{
		return hasCooked;
	}
	
	public int getCookedFoodAmount()
	{
		return cookedFood;
	}
	
	public float getCookedSaturationModifier()
	{
		return cookedSaturation;
	}
	
	public Iterable<PotionEffect> getCookedEffects()
	{
		return cookedEffects;
	}
	
	private static Props raw(int food, float saturation, PotionEffect... effects)
	{
		return new Props(food, saturation, effects);
	}
	
	private static final class Props
	{
		int rawFood;
		float rawSaturation;
		List<PotionEffect> rawEffects;
		
		boolean hasCooked = false;
		int cookedFood;
		float cookedSaturation;
		List<PotionEffect> cookedEffects;
		
		private Props(int food, float saturation, PotionEffect... effects)
		{
			this.rawFood = food;
			this.rawSaturation = saturation;
			this.rawEffects = ImmutableList.copyOf(effects);
		}
		
		private Props cooked(int food, float saturation, PotionEffect... effects)
		{
			this.hasCooked = true;
			this.cookedFood = food;
			this.cookedSaturation = saturation;
			this.cookedEffects = ImmutableList.copyOf(effects);
			return this;
		}
	}
}
