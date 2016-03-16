package genesis.combo.variant;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.potion.PotionEffect;

public enum EnumDish implements IFood, IMetadata<EnumDish>
{
	PORRIDGE("porridge_base", "porridge.base", 4, 4.2F),
	PORRIDGE_GINKGO("porridge_ginkgo", "porridge.ginkgo", 5, 5.2F),
	PORRIDGE_ARAUCARIOXYLON("porridge_araucarioxylon", "porridge.araucarioxylon", 5, 4.6F),
	PORRIDGE_FIG("porridge_fig", "porridge.fig", 5, 5.4F),
	PORRIDGE_ODONTOPTERIS("porridge_odontopteris", "porridge.odontopteris", 5, 4.8F),
	PORRIDGE_ZINGIBEROPSIS("porridge_zingiberopsis", "porridge.zingiberopsis", 6, 5.6F),
	PORRIDGE_ARCHAEOMARASMIUS("porridge_archaeomarasmius", "porridge.archaeomarasmius", 5, 4.8F),
	PORRIDGE_LAUROPHYLLUM("porridge_laurophyllum", "porridge.laurophyllum", 5, 4.8F),
	
	MASHED_ZINGIBEROPSIS("mashed_zingiberopsis", "mashed.zingiberopsis", 5, 5.8F),
	
	STEW_ARCHAEOMARASMIUS("stew_archaeomarasmius", "stew.archaeomarasmius", 6, 6.2F),
	STEW_CLIMATIUS("stew_climatius", "stew.climatius", 10, 12.6F),
	STEW_CHEIROLEPIS("stew_cheirolepis", "stew.cheirolepis", 10, 13),
	STEW_MEGANEURA("stew_meganeura", "stew.meganeura", 8, 6.6F),
	STEW_APHTHOROBLATINNA("stew_aphthoroblattina", "stew.aphthoroblattina", 7, 6.4F),
	STEW_ERYOPS("stew_eryops", "stew.eryops", 10, 12.6F),
	STEW_CERATITES("stew_ceratites", "stew.ceratites", 9, 7.2F),
	STEW_LIOPLEURODON("stew_liopleurodon", "stew.liopleurodon", 15, 16),
	STEW_TYRANNOSAURUS("stew_tyrannosaurus", "stew.tyrannosaurus", 15, 16);
	
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
