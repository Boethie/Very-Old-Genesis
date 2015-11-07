package genesis.metadata;

import com.google.common.collect.ImmutableList;

import genesis.block.*;
import genesis.item.*;
import genesis.util.Constants;

public class SiltBlocks extends VariantsOfTypesCombo<EnumSilt>
{
	public static final ObjectType<BlockSilt, ItemBlockMulti<EnumSilt>> SILT = ObjectType.create("silt", BlockSilt.class, ItemBlockMulti.<EnumSilt>getClassV()).setNamePosition(ObjectNamePosition.PREFIX);
	public static final ObjectType<BlockSiltstone, ItemBlockMulti<EnumSilt>> SILTSTONE = ObjectType.create("siltstone", "rock.siltstone", BlockSiltstone.class, ItemBlockMulti.<EnumSilt>getClassV()).setNamePosition(ObjectNamePosition.PREFIX);
	
	public SiltBlocks()
	{
		super(ImmutableList.of(SILTSTONE, SILT), ImmutableList.copyOf(EnumSilt.values()));
		
		setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
	}
}
