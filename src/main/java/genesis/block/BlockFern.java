package genesis.block;

import genesis.common.GenesisSounds;
import genesis.item.ItemBlockMulti;
import genesis.metadata.EnumFern;
import genesis.metadata.IPlantMetadata;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.BlockProperties;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BlockFern extends BlockPlant implements IShearable
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public BlockFern(List<IPlantMetadata> variants, VariantsOfTypesCombo owner, ObjectType<BlockPlant, ItemBlockMulti> type)
	{
		super(variants, owner, type);
		
		setStepSound(GenesisSounds.FERN);
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		
		EnumFern variant = (EnumFern) world.getBlockState(pos).getValue(variantProp);
		ret.add(owner.getStack(type, variant));
		
		return ret;
	}

	@Override
	public boolean isReplaceable(World worldIn, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}
}
