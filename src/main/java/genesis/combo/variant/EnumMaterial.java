package genesis.combo.variant;

public enum EnumMaterial implements IMetadata<EnumMaterial>
{
	DUNG_BRICK("dung_brick", "dungBrick"),
	RESIN("resin"),
	ARAUCARIOXYLON_CONE("araucarioxylon_cone", "araucarioxylonCone"),
	SPHENOPHYLLUM_FIBER("sphenophyllum_fiber", "sphenophyllumFiber"),
	ODONTOPTERIS_FIDDLEHEAD("odontopteris_fiddlehead", "odontopterisFiddlehead"),
	CLADOPHLEBIS_FROND("cladophlebis_frond", "cladophlebisFrond"),
	PROGRAMINIS("programinis"),
	SALT("salt"),
	ARTHROPLEURA_CHITIN("arthropleura_chitin", "arthropleuraChitin"),
	COELOPHYSIS_FEATHER("coelophysis_feather", "coelophysisFeather"),
	EPIDEXIPTERYX_FEATHER("epidexipteryx_feather", "epidexipteryxFeather"),
	LIOPLEURODON_TOOTH("liopleurodon_tooth", "liopleurodonTooth"),
	TYRANNOSAURUS_SALIVA("tyrannosaurus_saliva", "tyrannosaurusSaliva"),
	TYRANNOSAURUS_TOOTH("tyrannosaurus_tooth", "tyrannosaurusTooth");
	
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
