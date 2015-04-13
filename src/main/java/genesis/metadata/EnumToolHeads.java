package genesis.metadata;

import net.minecraft.item.Item;

public enum EnumToolHeads implements IMetaSingle
{
	CHIPPEDGRANITE("chippedGranite"), POLISHEDGRANITE("polishedGranite"), SHARPENEDGRANITE("sharpenedGranite"), CHIPPEDRHYOLITE("chippedRhyolite"), POLISHEDRHYOLITE("polishedRhyolite"),
	SHARPENEDRHYOLITE("sharpenedRhyolite"), CHIPPEDQUARTZITE("chippedQuartzite"), POLISHEDQUARTZITE("polishedQuartzite"), SHARPENEDQUARTZITE("sharpenedQuartzite"),
	CHIPPEDDOLERITE("chippedDolerite"), POLISHEDDOLERITE("polishedDolerite"), SHARPENEDDOLERITE("sharpenedDolerite"), CHIPPEDBROWNFLINT("chippedBrownFlint"), POLISHEDBROWNFLINT("polishedBrownFlint"),
	SHARPENEDBROWNFLINT("sharpenedBrownFlint");

	public static final EnumToolHeads[] NO_AXE;
	public static final EnumToolHeads[] NO_KNIFE;
	public static final EnumToolHeads[] NO_PICK;
	public static final EnumToolHeads[] NO_SPEAR;
	public static final EnumToolHeads[] NO_POINT;
	public static final EnumToolHeads[] NO_ARROW;

	static
	{
		NO_AXE = new EnumToolHeads[] {SHARPENEDBROWNFLINT, SHARPENEDDOLERITE, SHARPENEDGRANITE, SHARPENEDQUARTZITE, SHARPENEDRHYOLITE};
		NO_KNIFE = new EnumToolHeads[] {POLISHEDBROWNFLINT, POLISHEDDOLERITE, POLISHEDGRANITE, POLISHEDQUARTZITE, POLISHEDRHYOLITE};
		NO_PICK = new EnumToolHeads[] {SHARPENEDBROWNFLINT, SHARPENEDDOLERITE, SHARPENEDGRANITE, SHARPENEDQUARTZITE, SHARPENEDRHYOLITE};
		NO_SPEAR = new EnumToolHeads[]{CHIPPEDGRANITE, POLISHEDGRANITE, SHARPENEDGRANITE, CHIPPEDRHYOLITE, POLISHEDRHYOLITE, SHARPENEDRHYOLITE, CHIPPEDQUARTZITE, POLISHEDQUARTZITE, SHARPENEDQUARTZITE, CHIPPEDDOLERITE, POLISHEDDOLERITE, SHARPENEDDOLERITE, POLISHEDBROWNFLINT};
		NO_POINT = new EnumToolHeads[]{CHIPPEDGRANITE, POLISHEDGRANITE, SHARPENEDGRANITE, CHIPPEDRHYOLITE, POLISHEDRHYOLITE, SHARPENEDRHYOLITE, CHIPPEDDOLERITE, POLISHEDDOLERITE, SHARPENEDDOLERITE, POLISHEDBROWNFLINT, SHARPENEDBROWNFLINT, POLISHEDQUARTZITE, SHARPENEDQUARTZITE};
		NO_ARROW = new EnumToolHeads[]{CHIPPEDGRANITE, POLISHEDGRANITE, SHARPENEDGRANITE, CHIPPEDRHYOLITE, POLISHEDRHYOLITE, SHARPENEDRHYOLITE, CHIPPEDDOLERITE, POLISHEDDOLERITE, SHARPENEDDOLERITE, POLISHEDBROWNFLINT, POLISHEDQUARTZITE};
	}
	//TODO; Possibly remove unused variants
	String name;
	String unlocalizedName;

	EnumToolHeads(String name)
	{
		this(name, name);
	}

	EnumToolHeads(String name, String unlocalizedName)
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

	@Override
	public Item getItem()
	{
		return null;
	}
}
