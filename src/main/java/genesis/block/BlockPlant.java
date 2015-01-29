package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.EnumPlant;
import genesis.util.Constants;
import genesis.util.Metadata;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockPlant extends BlockBush
{
	public BlockPlant()
	{
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setDefaultState(Metadata.getDefaultState(this, getVariant(), getMetaClass()));
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setBlockBounds(0.5F - 0.4F, 0.0F, 0.5F - 0.4F, 0.5F + 0.4F, 0.4F * 2, 0.5F + 0.4F);
	}

	@Override
	protected boolean canPlaceBlockOn(Block ground)
	{
		return (ground == GenesisBlocks.moss) || super.canPlaceBlockOn(ground);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return Metadata.getMetadata(state, getVariant());
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		Metadata.getSubBlocks(getMetaClass(), list);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return Metadata.getState(this, getVariant(), getMetaClass(), meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return Metadata.getMetadata(state, getVariant());
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, getVariant());
	}

	@Override
	public Block.EnumOffsetType getOffsetType()
	{
		return Block.EnumOffsetType.XYZ;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 100;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 60;
	}

	protected IProperty getVariant()
	{
		return Constants.PLANT_VARIANT;
	}

	protected Class getMetaClass()
	{
		return EnumPlant.class;
	}
}
