package genesis.block;

import genesis.client.GenesisClient;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.*;
import genesis.util.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class BlockGenesisLeaves extends BlockLeaves
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@Properties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{ CHECK_DECAY, DECAYABLE };
	}
	
	public final VariantsOfTypesCombo owner;
	
	public final List<EnumTree> variants;
	public final PropertyIMetadata variantProp;
	
	public BlockGenesisLeaves(List<EnumTree> variants, VariantsOfTypesCombo owner)
	{
		super();
		
		this.owner = owner;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata("variant", variants);
		
		blockState = new BlockState(this, variantProp, CHECK_DECAY, DECAYABLE);
		setDefaultState(getBlockState().getBaseState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setStepSound(soundTypeGrass);
	}

	@Override
	public BlockGenesisLeaves setUnlocalizedName(String name)
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
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(this, variants, list);
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList();
		drops.add(owner.getStack(this, (IMetadata) world.getBlockState(pos).getValue(variantProp)));
		return drops;
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = getDefaultState();
		state = state.withProperty(variantProp, (Comparable) owner.getVariant(this, meta));
		state = state.withProperty(DECAYABLE, false);
		return state;
	}

	@Override
	public EnumType getWoodType(int meta)
	{
		return null;
	}
	
	protected static boolean fancyGraphicsEnabled()
	{
		return GenesisClient.getMC().isFancyGraphicsEnabled();
	}
	
	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return fancyGraphicsEnabled() ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return !fancyGraphicsEnabled();
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return !fancyGraphicsEnabled() && worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
	}
}
