package genesis.metadata;

import genesis.block.BlockSilt;
import genesis.block.BlockSiltstone;
import genesis.item.ItemBlockMulti;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants;
import net.minecraft.block.Block;

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
