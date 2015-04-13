package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.BlocksAndItemsWithVariantsOfTypes;
import genesis.metadata.EnumPlant;
import genesis.metadata.IMetadata;
import genesis.metadata.Properties;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockPlant extends BlockBush
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@Properties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final BlocksAndItemsWithVariantsOfTypes owner;
	
	public final PropertyEnum variantProp;
	public final List<IMetadata> variants;
	
	public BlockPlant(List<IMetadata> variants, BlocksAndItemsWithVariantsOfTypes owner)
	{
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setBlockBounds(0.5F - 0.4F, 0.0F, 0.5F - 0.4F, 0.5F + 0.4F, 0.4F * 2, 0.5F + 0.4F);

		this.owner = owner;
		
		variantProp = PropertyEnum.create("variant", variants.get(0).getClass(), variants);
		this.variants = variants;
		
		blockState = createOurBlockState();
		setDefaultState(getBlockState().getBaseState());
	}
	
	protected BlockState createOurBlockState()
	{
		return new BlockState(this, variantProp);
	}

	@Override
	public BlockPlant setUnlocalizedName(String unlocalizedName)
	{
		super.setUnlocalizedName(Constants.PREFIX + unlocalizedName);
		
		return this;
	}

	@Override
	protected boolean canPlaceBlockOn(Block ground)
	{
		return (ground == GenesisBlocks.moss) || super.canPlaceBlockOn(ground);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return variants.indexOf(state.getValue(variantProp));
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(this, variants, list);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, variantProp);
	}

	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata, variantProp);
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
}
