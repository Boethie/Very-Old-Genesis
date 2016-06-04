package genesis.combo;

import com.google.common.collect.ImmutableList;
import genesis.block.BlockRubble;
import genesis.combo.variant.EnumRubble;
import genesis.item.ItemBlockMulti;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;

public class RubbleBlocks extends VariantsOfTypesCombo<EnumRubble>
{
	public static final ObjectType<EnumRubble, BlockRubble, ItemBlockMulti<EnumRubble>> RUBBLE;

	static
	{
		RUBBLE = ObjectType.createBlock(EnumRubble.class, "rubble", Unlocalized.Section.ROCK + "rubble", BlockRubble.class);
		RUBBLE.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.POSTFIX);
	}

	public RubbleBlocks()
	{
		super("rubble", ImmutableList.of(RUBBLE),
				EnumRubble.class, ImmutableList.copyOf(EnumRubble.values()));

		setNames(Constants.MOD_ID, Unlocalized.PREFIX);
	}
}
