package genesis.metadata;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.util.EnumHelper;
import genesis.block.BlockPlant;
import genesis.metadata.BlocksAndItemsWithVariantsOfTypes.ObjectType;

public class PlantBlocks extends BlocksAndItemsWithVariantsOfTypes
{
	public static final ObjectType<BlockPlant> PLANT = new ObjectType<BlockPlant>("plant", BlockPlant.class, null)
			.setPostfix(null).setUseVariantJsons(false);
	
	public PlantBlocks()
	{
		super(new ObjectType[] {PLANT}, EnumPlant.values());
	}
}
