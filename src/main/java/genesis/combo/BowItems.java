package genesis.combo;

import genesis.combo.variant.BowVariants;
import genesis.combo.variant.BowVariants.BowVariant;
import genesis.item.ItemBowMulti;
import genesis.util.Constants;
import genesis.util.ReflectionUtils;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.Block;

public class BowItems extends VariantsCombo<BowVariant, Block, ItemBowMulti<BowVariant>>
{
	public BowItems()
	{
		super(ObjectType.createItem("bow", ReflectionUtils.convertClass(ItemBowMulti.class)), BowVariant.class, BowVariants.getAll());
		
		setNames(Constants.MOD_ID, Unlocalized.PREFIX);
		
		getObjectType().setShouldRegisterVariantModels(false);
	}
}
