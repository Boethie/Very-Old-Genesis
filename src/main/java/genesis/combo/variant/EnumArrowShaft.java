package genesis.combo.variant;

public enum EnumArrowShaft implements IArrowMetadata<EnumArrowShaft>
{
	WOOD("wood", 0.8F),
	BONE("bone", 1.25F);
	
	final String name;
	final String unlocalizedName;
	final float mass;
	
	EnumArrowShaft(String name, String unlocalizedName, float mass)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.mass = mass;
	}
	
	EnumArrowShaft(String name, float mass)
	{
		this(name, name, mass);
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
	public float getMass()
	{
		return mass;
	}
}
