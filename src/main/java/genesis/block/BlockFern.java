package genesis.block;

import genesis.util.Constants;
import genesis.util.Metadata;

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

public class BlockFern extends BlockPlant implements IShearable
{
	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
		ret.add(Metadata.newStack((EnumFern) world.getBlockState(pos).getValue(Constants.FERN_VARIANT)));
		return ret;
	}

	@Override
	protected IProperty getVariant()
	{
		return Constants.FERN_VARIANT;
	}

	@Override
	protected Class getMetaClass()
	{
		return EnumFern.class;
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
