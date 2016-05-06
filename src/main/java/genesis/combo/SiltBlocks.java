package genesis.combo;

import com.google.common.collect.ImmutableList;

import genesis.block.*;
import genesis.combo.variant.EnumSilt;
import genesis.item.*;
import genesis.util.Constants;

public class SiltBlocks extends VariantsOfTypesCombo<EnumSilt>
{
	public static final ObjectType<EnumSilt, BlockSilt, ItemBlockMulti<EnumSilt>> SILT =
			ObjectType.createBlock(EnumSilt.class, "silt", BlockSilt.class);
	public static final ObjectType<EnumSilt, BlockSiltstone, ItemBlockMulti<EnumSilt>> SILTSTONE =
			ObjectType.createBlock(EnumSilt.class, "siltstone", "rock.siltstone", BlockSiltstone.class);
	
	static
	{
		SILT.setVariantNameFunction((v) -> v == EnumSilt.SILT ? SILT.getName() : SILT.getName() + "_" + v.getName())
				.setUseSeparateVariantJsons(false);
		SILTSTONE.setVariantNameFunction((v) -> v == EnumSilt.SILT ? SILTSTONE.getName() : SILTSTONE.getName() + "_" + v.getName())
				.setUseSeparateVariantJsons(false);
	}
	
	public SiltBlocks()
	{
		super(ImmutableList.of(SILT, SILTSTONE), EnumSilt.class, ImmutableList.copyOf(EnumSilt.values()));
		
		setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
	}
}
