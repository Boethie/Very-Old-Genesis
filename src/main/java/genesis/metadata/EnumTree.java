package genesis.metadata;

public enum EnumTree implements IMetadata
{
	ARCHAEOPTERIS("archaeopteris"), SIGILLARIA("sigillaria"), LEPIDODENDRON("lepidodendron"),
	CORDAITES("cordaites"), PSARONIUS("psaronius"), ARAUCARIOXYLON("araucarioxylon");
	
	public static final EnumTree[] NO_BILLET = { PSARONIUS };

	final String name;
	final String unlocalizedName;
	
	EnumTree(String name)
	{
		this(name, name);
	}
	
	EnumTree(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
	}
	
	@Override
	public String toString()
	{
		return name;
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
}
