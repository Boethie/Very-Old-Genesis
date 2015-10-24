package genesis.metadata;

public enum EnumAquaticPlant implements IMetadata
{
	GRYPANIA("grypania"), BANGIOMORPHA("bangiomorpha"), MARPOLIA("marpolia"), MARGERETIA("margaretia"), CHANCELLORIA("chancelloria"),
	HAZELLA("hazelia"), DIAONIELLA("diagoniella"), PIRANIA("pirania"), VAUXIA("vauxia"), WAPKIA("wapkia"), ERNIETTA("ernietta"),
	PTERIDINIUM("pteridinium"), DINOMISCHUS("dinomischus"), VETULOCYSTIS("vetulocystis"), ECHMATOCRINUS("echmatocrinus"), THAUMAPTILON("thaumaptilon"),
	PRIMOCANDELABRUM("primocandelabrum"), CHARNIA("charnia"), CHARNIA_TOP("charnia_top");

	final String name;
	final String unlocalizedName;

	EnumAquaticPlant(String name)
	{
		this(name, name);
	}

	private EnumAquaticPlant(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public String getUnlocalizedName()
	{
		return this.unlocalizedName;
	}
}
