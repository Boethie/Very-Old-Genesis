package genesis.metadata;

import com.google.common.collect.ImmutableList;

import genesis.block.*;
import genesis.item.*;
import genesis.util.Constants;

public class SiltBlocks extends VariantsOfTypesCombo<EnumSilt>
{
	public static final ObjectType<BlockSilt, ItemBlockMulti<EnumSilt>> SILT = ObjectType.createBlock("silt", BlockSilt.class);
	public static final ObjectType<BlockSiltstone, ItemBlockMulti<EnumSilt>> SILTSTONE = ObjectType.createBlock("siltstone", "rock.siltstone", BlockSiltstone.class);
	
	public SiltBlocks()
	{
		super(ImmutableList.of(SILT, SILTSTONE), EnumSilt.class, ImmutableList.copyOf(EnumSilt.values()));
		
		setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
	}
}
