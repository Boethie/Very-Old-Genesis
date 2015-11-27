package genesis.metadata;

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
	public int getColorMultiplier(IBlockAccess world, BlockPos pos);
	
	public int getRenderColor();
	
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos);
	
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, List<ItemStack> normalDrop);
	
	public boolean isReplaceable(World world, BlockPos pos);
	
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, Random rand, List<ItemStack> normalDrop);
	
	public EnumPlantType[] getSoilTypes();
	
	public int getWaterDistance();
}
