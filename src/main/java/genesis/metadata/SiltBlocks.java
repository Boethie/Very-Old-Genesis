package genesis.metadata;

import net.minecraft.block.*;
import genesis.block.*;
import genesis.item.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.Constants;

@SuppressWarnings("unchecked")
public class SiltBlocks extends VariantsOfTypesCombo<ObjectType<? extends Block, ? extends ItemBlockMulti>, EnumSilt>
{
	public static final ObjectType<BlockSilt, ItemBlockMulti> SILT = new ObjectType<BlockSilt, ItemBlockMulti>("silt", BlockSilt.class, null).setNamePosition(ObjectNamePosition.PREFIX);
	public static final ObjectType<BlockSiltstone, ItemBlockMulti> SILTSTONE = new ObjectType<BlockSiltstone, ItemBlockMulti>("siltstone", "rock.siltstone", BlockSiltstone.class, null).setNamePosition(ObjectNamePosition.PREFIX);
	
	public SiltBlocks()
	{
		super(new ObjectType[]{SILTSTONE, SILT}, EnumSilt.values());
		
		setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
	}
}
