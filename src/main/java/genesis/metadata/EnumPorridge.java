package genesis.metadata;

public enum EnumPorridge implements IMetadata
{
	BASE("base", "", 4, 4.8F),
	ZINGIBEROPSIS("zingiberopsis", "zingiberopsis", 5, 5.2F),
	ODONTOPTERIS("odontopteris", "odontopteris", 6, 5.6F),
	ARCHAEOMARASMIUS("archaeomarasmius", "archaeomarasmius", 5, 5.2F);
	
	final String name;
	final String unlocalizedName;
	final int food;
	final float saturation;
	final String[] ingredientNames;
	
	EnumPorridge(String name, String unlocalizedName, int food, float saturation, String... ingredientNames)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.food = food;
		this.saturation = saturation;
		this.ingredientNames = ingredientNames;
	}
	
	EnumPorridge(String name, int food, float saturation, String... ingredientNames)
	{
		this(name, name, food, saturation, ingredientNames);
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
	
	public int getFoodAmount()
	{
		return food;
	}
	
	public float getSaturationModifier()
	{
		return saturation;
	}
}
