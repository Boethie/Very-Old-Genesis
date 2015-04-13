package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.BlocksAndItemsWithVariantsOfTypes;
import genesis.metadata.EnumTree;
import genesis.metadata.Properties;
import genesis.util.BlockStateToMetadata;

import java.util.List;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockGenesisLogs extends BlockLog
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@Properties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{ LOG_AXIS };
	}
	
	public final BlocksAndItemsWithVariantsOfTypes owner;
	
	public final PropertyEnum variantProp;
	public final List<EnumTree> variants;
	
	public BlockGenesisLogs(List<EnumTree> variants, BlocksAndItemsWithVariantsOfTypes owner)
	{
		super();
		
		this.owner = owner;
		
		variantProp = PropertyEnum.create("variant", EnumTree.class, variants);
		this.variants = variants;
		
		blockState = createOurBlockState();
		setDefaultState(getBlockState().getBaseState().withProperty(LOG_AXIS, EnumAxis.NONE));
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}
	
	protected BlockState createOurBlockState()
	{
		return new BlockState(this, variantProp, LOG_AXIS);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		for (EnumTree treeType : variants)
		{
			list.add(new ItemStack(itemIn, 1, variants.indexOf(treeType)));
		}
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, variantProp, LOG_AXIS);
	}

	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata, variantProp, LOG_AXIS);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		
		if (placer.isSneaking())
		{
			state = state.withProperty(LOG_AXIS, EnumAxis.NONE);
		}
		
		return state;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return variants.indexOf(state.getValue(variantProp));
	}
}
