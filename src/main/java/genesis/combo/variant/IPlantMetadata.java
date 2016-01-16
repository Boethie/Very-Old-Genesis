package genesis.combo.variant;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public interface IPlantMetadata<V> extends IMetadata<V>
{
	int getColorMultiplier(IBlockAccess world, BlockPos pos);
	
	int getRenderColor();
	
	boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos);
	
	List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, List<ItemStack> normalDrop);
	
	boolean isReplaceable(World world, BlockPos pos);
	
	List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, Random rand, List<ItemStack> normalDrop);
	
	EnumPlantType[] getSoilTypes();
	
	int getWaterDistance();
}
