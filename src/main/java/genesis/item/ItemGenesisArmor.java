package genesis.item;

import genesis.combo.ClothingItems;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumClothing;
import net.minecraft.block.Block;
import net.minecraft.item.ItemArmor;

@ItemVariantCount(1)
public class ItemGenesisArmor extends ItemArmor
{
	protected final ClothingItems owner;
	protected final ObjectType<Block, ItemGenesisArmor> type;
	
	protected final EnumClothing variant;
	
	public ItemGenesisArmor(ClothingItems owner, ObjectType<Block, ItemGenesisArmor> type,
			EnumClothing variant, Class<EnumClothing> variantClass,
			int armorType)
	{
		super(variant.getMaterial(), -1, armorType);
		
		this.owner = owner;
		this.type = type;
		
		this.variant = variant;
	}
}
