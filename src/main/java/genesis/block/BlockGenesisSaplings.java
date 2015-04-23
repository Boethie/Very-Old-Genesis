package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.metadata.EnumTree;
import genesis.metadata.IMetadata;
import genesis.metadata.Properties;
import genesis.metadata.PropertyIMetadata;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockGenesisSaplings extends BlockSapling
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@Properties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{ STAGE };
	}
	
	public final VariantsOfTypesCombo owner;
	
	public final List<EnumTree> variants;
	public final PropertyIMetadata variantProp;
	
	public BlockGenesisSaplings(List<EnumTree> variants, VariantsOfTypesCombo owner)
	{
		super();
		
		this.owner = owner;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata("variant", variants);
		
		blockState = new BlockState(this, variantProp, STAGE);
		setDefaultState(getBlockState().getBaseState());
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setStepSound(soundTypeGrass);
	}

	@Override
	public BlockGenesisSaplings setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(Constants.PREFIX + name);
		
		return this;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(this, variants, list);
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = getDefaultState();
		state = state.withProperty(variantProp, (Comparable) owner.getVariant(this, meta));
		return state;
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getMetadata(this, (IMetadata) state.getValue(variantProp));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
	{
		return owner.getStack(this, (IMetadata) world.getBlockState(pos).getValue(variantProp));
	}
	
	@Override
	public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		
	}
}
