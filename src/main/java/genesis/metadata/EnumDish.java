package genesis.metadata;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.potion.PotionEffect;

public enum EnumDish implements IFood, IMetadata<EnumDish>
{
	PORRIDGE("porridge_base", "porridge.base", 4, 4.8F),
	PORRIDGE_ARAUCARIOXYLON("porridge_araucarioxylon", "porridge.araucarioxylon", 5, 5.3F),
	PORRIDGE_ODONTOPTERIS("porridge_odontopteris", "porridge.odontopteris", 5, 5.4F),
	PORRIDGE_ZINGIBEROPSIS("porridge_zingiberopsis", "porridge.zingiberopsis", 6, 5.8F),
	PORRIDGE_ARCHAEOMARASMIUS("porridge_archaeomarasmius", "porridge.archaeomarasmius", 5, 5.5F),
	
	MASHED_NEUROPTERIDIUM("mashed_neuropteridium", "mashed.neuropteridium", 5, 5.7F),
	
	STEW_ARCHAEOMARASMIUS("stew_archaeomarasmius", "stew.archaeomarasmius", 6, 6.4F),
	STEW_SPIRIFER("stew_spirifer", "stew.spirifer", 8, 6.9F),
	STEW_CLIMATIUS("stew_climatius", "stew.climatius", 10, 12.9F),
	STEW_MEGANEURA("stew_meganeura", "stew.meganeura", 8, 6.9F),
	STEW_APHTHOROBLATINNA("stew_aphthoroblattina", "stew.aphthoroblattina", 7, 6.6F),
	STEW_ERYOPS("stew_eryops", "stew.eryops", 10, 12.9F),
	STEW_GRYPHAEA("stew_gryphaea", "stew.gryphaea", 8, 6.9F),
	STEW_CERATITES("stew_ceratites", "stew.ceratites", 9, 7.8F),
	STEW_LIOPLEURODON("stew_liopleurodon", "stew.liopleurodon", 15, 16.2F),
	STEW_TYRANNOSAURUS("stew_tyrannosaurus", "stew.tyrannosaurus", 15, 16.2F);
	
	final String name;
	final String unlocalizedName;
	final int food;
	final float saturation;
	final List<PotionEffect> effects;
	
	EnumDish(String name, String unlocalizedName, int food, float saturation, PotionEffect... effects)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.food = food;
		this.saturation = saturation;
		this.effects = ImmutableList.copyOf(effects);
	}
	
	EnumDish(String name, int food, float saturation, PotionEffect... effects)
	{
		this(name, name, food, saturation, effects);
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
	
	@Override
	public int getFoodAmount()
	{
		return food;
	}
	
	@Override
	public float getSaturationModifier()
	{
		return saturation;
	}
	
	@Override
	public Iterable<PotionEffect> getEffects()
	{
		return effects;
	}
}
