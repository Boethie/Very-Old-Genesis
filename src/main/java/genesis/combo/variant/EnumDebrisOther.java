package genesis.combo.variant;

public enum EnumDebrisOther implements IMetadata<EnumDebrisOther>
{
	CALAMITES("calamites"),
	COELOPHYSIS_FEATHER("coelophysis_feather", "coelophysisFeather"),
	EPIDEXIPTERYX_FEATHER("epidexipteryx_feather", "epidexipteryxFeather");
	
	final String name;
	final String unlocalizedName;
	
	EnumDebrisOther(String name)
	{
		this(name, name);
	}
	
	EnumDebrisOther(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
}
