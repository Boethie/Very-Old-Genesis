package genesis.metadata;

import genesis.item.ItemToolHead;

public class ToolHeads extends BlocksAndItemsWithVariantsOfTypes
{
	public static final ObjectType<ItemToolHead> TOOLHEAD_AXE = new ObjectType<ItemToolHead>("toolhead_axe", null, ItemToolHead.class, EnumToolHeads.NO_AXE);
	public static final ObjectType<ItemToolHead> TOOLHEAD_KNIFE = new ObjectType<ItemToolHead>("toolhead_knife", null, ItemToolHead.class, EnumToolHeads.NO_KNIFE);
	public static final ObjectType<ItemToolHead> TOOLHEAD_PICK = new ObjectType<ItemToolHead>("toolhead_pick", null, ItemToolHead.class, EnumToolHeads.NO_PICK);
	public static final ObjectType<ItemToolHead> TOOLHEAD_SPEAR = new ObjectType<ItemToolHead>("toolhead_spear", null, ItemToolHead.class, EnumToolHeads.NO_SPEAR);
	public static final ObjectType<ItemToolHead> TOOLHEAD_POINT = new ObjectType<ItemToolHead>("toolhead_point", null, ItemToolHead.class, EnumToolHeads.NO_POINT);
	public static final ObjectType<ItemToolHead> TOOLHEAD_ARROW = new ObjectType<ItemToolHead>("toolhead_arrow", null, ItemToolHead.class, EnumToolHeads.NO_ARROW);

	public ToolHeads()
	{
		super(new ObjectType[] {TOOLHEAD_AXE, TOOLHEAD_KNIFE, TOOLHEAD_PICK, TOOLHEAD_POINT, TOOLHEAD_SPEAR, TOOLHEAD_ARROW}, EnumToolHeads.values());
	}
	//TODO; Automatic tool recipe creation, be careful with exceptions!

}
