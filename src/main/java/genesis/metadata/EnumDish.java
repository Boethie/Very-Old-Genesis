package genesis.metadata;

public enum EnumDish implements IFood, IMetadata<EnumDish>
{
	PORRIDGE("porridge_base", "porridge.base", 4, 4.8F),
	PORRIDGE_ARAUCARIOXYLON("porridge_araucarioxylon", "porridge.araucarioxylon", 5, 5.7F),
	PORRIDGE_ODONTOPTERIS("porridge_odontopteris", "porridge.odontopteris", 5, 6F),
	PORRIDGE_ZINGIBEROPSIS("porridge_zingiberopsis", "porridge.zingiberopsis", 6, 6.6F),
	PORRIDGE_ARCHAEOMARASMIUS("porridge_archaeomarasmius", "porridge.archaeomarasmius", 5, 6.2F);
	
	final String name;
	final String unlocalizedName;
	final int food;
	final float saturation;
	
	EnumDish(String name, String unlocalizedName, int food, float saturation)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.food = food;
		this.saturation = saturation;
	}
	
	EnumDish(String name, int food, float saturation)
	{
		this(name, name, food, saturation);
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
}
