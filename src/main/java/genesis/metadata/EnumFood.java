package genesis.metadata;

public enum EnumFood implements IMetadata<EnumFood>
{
	SPIRIFER("spirifer",
			2, 0.4F,
			3, 1.0F),
	climatius("climatius",
			2, 0.4F,
			5, 6.0F),
	meganeura("meganeura",
			2, 0.4F,
			3, 1.0F),
	aphthoroblattina("aphthoroblattina",
			1, 0.2F,
			2, 0.8F),
	eryops_leg("eryops_leg", "eryopsLeg",
			2, 0.8F,
			5, 6.0F),
	gryphaea("gryphaea",
			2, 0.4F,
			3, 1.0F),
	ceratites("ceratites",
			2, 0.4F,
			4, 1.8F),
	liopleurodon("liopleurodon",
			4, 2.8F,
			16, 19.8F),
	tyrannosaurus("tyrannosaurus",
			4, 2.8F,
			16, 19.8F);
	
	final String name;
	final String unlocalizedName;
	final int rawFood;
	final float rawSaturation;
	
	final boolean hasCooked;
	final int cookedFood;
	final float cookedSaturation;
	
	EnumFood(String name, String unlocalizedName,
			int rawFood, float rawSaturation,
			boolean hasCooked, int cookedFood, float cookedSaturation)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.rawFood = rawFood;
		this.rawSaturation = rawSaturation;
		this.hasCooked = hasCooked;
		this.cookedFood = cookedFood;
		this.cookedSaturation = cookedSaturation;
	}
	
	EnumFood(String name, String unlocalizedName,
			int rawFood, float rawSaturation,
			int cookedFood, float cookedSaturation)
	{
		this(name, unlocalizedName, rawFood, rawSaturation, true, cookedFood, cookedSaturation);
	}
	
	EnumFood(String name,
			int rawFood, float rawSaturation,
			int cookedFood, float cookedSaturation)
	{
		this(name, name, rawFood, rawSaturation, cookedFood, cookedSaturation);
	}
	
	EnumFood(String name, String unlocalizedName,
			int food, float saturation)
	{
		this(name, unlocalizedName, food, saturation, false, 0, 0);
	}
	
	EnumFood(String name,
			int food, float saturation)
	{
		this(name, name, food, saturation, false, 0, 0);
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
	
	public int getRawFoodAmount()
	{
		return rawFood;
	}
	
	public float getRawSaturationModifier()
	{
		return rawSaturation;
	}
	
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
}
