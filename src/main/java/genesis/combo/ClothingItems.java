package genesis.combo;

import com.google.common.collect.ImmutableList;

import genesis.combo.variant.EnumClothing;
import genesis.item.ItemGenesisArmor;
import net.minecraft.block.Block;

public class ClothingItems extends VariantsOfTypesCombo<EnumClothing>
{
	public static final ObjectType<Block, ItemGenesisArmor> HELMET = ObjectType.createItem("helmet", ItemGenesisArmor.class)
			.setItemArguments(0)
			.setTypeNamePosition(TypeNamePosition.POSTFIX);
	public static final ObjectType<Block, ItemGenesisArmor> CHESTPLATE = ObjectType.createItem("chestplate", ItemGenesisArmor.class)
			.setItemArguments(1)
			.setTypeNamePosition(TypeNamePosition.POSTFIX);
	public static final ObjectType<Block, ItemGenesisArmor> LEGGINGS = ObjectType.createItem("leggings", ItemGenesisArmor.class)
			.setItemArguments(2)
			.setTypeNamePosition(TypeNamePosition.POSTFIX);
	public static final ObjectType<Block, ItemGenesisArmor> BOOTS = ObjectType.createItem("boots", ItemGenesisArmor.class)
			.setItemArguments(3)
			.setTypeNamePosition(TypeNamePosition.POSTFIX);
	
	public ClothingItems()
	{
		super(ImmutableList.of(HELMET, CHESTPLATE, LEGGINGS, BOOTS),
				EnumClothing.class, ImmutableList.copyOf(EnumClothing.values()));
	}
}
