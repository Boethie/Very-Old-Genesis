package genesis.metadata;

import com.google.common.collect.Lists;

import java.util.List;

public enum EnumTree implements IMetadata
{
	ARCHAEOPTERIS("archaeopteris"), SIGILLARIA("sigillaria"), LEPIDODENDRON("lepidodendron"),
	CORDAITES("cordaites"), PSARONIUS("psaronius"), BJUVIA("bjuvia"), VOLTZIA("voltzia"),
	ARAUCARIOXYLON("araucarioxylon");

	public static final EnumTree[] NO_BILLET = { PSARONIUS, BJUVIA };
	public static final EnumTree[] NO_ROTTEN = { PSARONIUS, BJUVIA, VOLTZIA };
	public static final List<EnumTree> NO_DEBRIS = Lists.newArrayList(CORDAITES, PSARONIUS, BJUVIA, VOLTZIA);
	
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
