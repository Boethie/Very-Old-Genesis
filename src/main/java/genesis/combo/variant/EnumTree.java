package genesis.combo.variant;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public enum EnumTree implements IMetadata<EnumTree>
{
	ARCHAEOPTERIS("archaeopteris"), SIGILLARIA("sigillaria"), LEPIDODENDRON("lepidodendron"),
	CORDAITES("cordaites"), PSARONIUS("psaronius"), GINKGO("ginkgo"), BJUVIA("bjuvia"), VOLTZIA("voltzia"),
	ARAUCARIOXYLON("araucarioxylon"), METASEQUOIA("metasequoia"), ARCHAEANTHUS("archaeanthus");

	public static final Set<EnumTree> NO_BILLET = ImmutableSet.of(PSARONIUS, BJUVIA);
	public static final Set<EnumTree> NO_DEAD = ImmutableSet.of(PSARONIUS, GINKGO, BJUVIA, VOLTZIA, ARCHAEANTHUS);
	public static final Set<EnumTree> NO_DEBRIS = ImmutableSet.of(CORDAITES, PSARONIUS, BJUVIA, VOLTZIA);
	
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
