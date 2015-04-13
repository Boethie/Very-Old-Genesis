package genesis.block;

import genesis.client.GenesisSounds;
import genesis.metadata.BlocksAndItemsWithVariantsOfTypes;
import genesis.metadata.EnumFern;
import genesis.metadata.IMetadata;
import genesis.metadata.Properties;
import genesis.util.Constants;
import genesis.util.Metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class BlockFern extends BlockPlant implements IShearable
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@Properties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public BlockFern(List<IMetadata> variants, BlocksAndItemsWithVariantsOfTypes owner)
	{
		super(variants, owner);
		
		setStepSound(GenesisSounds.FERN);
	}
	
	protected BlockState createOurBlockState()
	{
		return new BlockState(this, variantProp);
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
		ret.add(owner.getStack(this, variant));
		
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

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random)
	{
		return 1 + random.nextInt((fortune * 2) + 1);
	}

	@Override
	public int getBlockColor()
	{
		return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	@Override
	public int getRenderColor(IBlockState state)
	{
		return getBlockColor();
	}

	@Override
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		return worldIn.getBiomeGenForCoords(pos).getGrassColorAtPos(pos);
	}
}
