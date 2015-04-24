package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.*;
import genesis.util.*;

import java.util.List;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

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
	
	public final VariantsOfTypesCombo owner;
	
	public final List<EnumTree> variants;
	public final PropertyIMetadata variantProp;
	
	public BlockGenesisLogs(List<EnumTree> variants, VariantsOfTypesCombo owner)
	{
		super();
		
		this.owner = owner;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata("variant", variants);
		
		blockState = new BlockState(this, variantProp, LOG_AXIS);
		setDefaultState(getBlockState().getBaseState().withProperty(LOG_AXIS, EnumAxis.NONE));
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		
		Blocks.fire.setFireInfo(this, 5, 5);
	}

	@Override
	public BlockGenesisLogs setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(Constants.PREFIX + name);
		
		return this;
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(this, variants, list);
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
		return owner.getStack(this, (IMetadata) state.getValue(variantProp)).getItemDamage();
	}
}
