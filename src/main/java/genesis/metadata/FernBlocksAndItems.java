package genesis.metadata;

import net.minecraftforge.common.util.EnumHelper;
import genesis.block.BlockFern;
import genesis.item.ItemBlockMulti;
import genesis.metadata.BlocksAndItemsWithVariantsOfTypes.ObjectType;

public class FernBlocksAndItems extends BlocksAndItemsWithVariantsOfTypes
{
	public static final ObjectType<BlockFern> FERN = new ObjectType<BlockFern>("fern", BlockFern.class, ItemBlockMulti.class)
			.setPostfix(null).setUseVariantJsons(false);
	
	public FernBlocksAndItems()
	{
		super(new ObjectType[] {FERN}, EnumFern.values());
	}
}
