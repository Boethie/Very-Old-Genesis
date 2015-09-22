package genesis.metadata;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IPlantMetadata extends IMetadata
{
	public int getColorMultiplier(IBlockAccess world, BlockPos pos);
	
	public int getRenderColor();
}
