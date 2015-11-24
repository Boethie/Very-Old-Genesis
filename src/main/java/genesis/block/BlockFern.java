package genesis.block;

import genesis.common.GenesisSounds;
import genesis.item.ItemBlockMulti;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class BlockFern<V extends IPlantMetadata<V>> extends BlockPlant<V> implements IShearable
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public BlockFern(VariantsOfTypesCombo<V> owner, ObjectType<BlockPlant<V>, ? extends ItemBlockMulti<V>> type, List<V> variants, Class<V> variantClass, ObjectType<? extends BlockGenesisDoublePlant<V>, ? extends ItemBlockMulti<V>> doubleType)
	{
		super(owner, type, variants, variantClass, doubleType);
		
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
		
		V variant = world.getBlockState(pos).getValue(variantProp);
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
