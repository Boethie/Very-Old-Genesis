package genesis.metadata;

public enum EnumMaterial implements IMetadata
{
	RESIN("resin"),
	ARAUCARIOXYLON_CONE("araucarioxylon_cone", "araucarioxylonCone"),
	SPHENOPHYLLUM_FIBER("sphenophyllum_fiber", "sphenophyllumFiber"),
	ODONTOPTERIS_FIDDLEHEAD("odontopteris_fiddlehead", "odontopterisFiddlehead"),
	PROGRAMINIS("programinis"),
	PROTOTAXITES_FLESH("prototaxites_flesh", "prototaxitesFlesh"),
	ARTHROPLEURA_CHITIN("arthropleura_chitin", "arthropleuraChitin"),
	LIOPLEURODON_TOOTH("liopleurodon_tooth", "liopleurodonTooth"),
	TYRANNOSAURUS_SALIVA("tyrannosaurus_saliva", "tyrannosaurusSaliva"),
	TYRANNOSAURUS_TOOTH("tyrannosaurus_tooth", "tyrannosaurusTooth"),
	EPIDEXIPTERYX_FEATHER("epidexipteryx_feather", "epidexipteryxFeather");
	
	final String name;
	final String unlocalizedName;
	
	EnumMaterial(String name)
	{
		this(name, name);
	}
	
	EnumMaterial(String name, String unlocalizedName)
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
