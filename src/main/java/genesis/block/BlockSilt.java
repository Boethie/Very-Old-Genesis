package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.BlockStateToMetadata;

import java.util.List;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockSilt extends BlockFalling
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final SiltBlocks owner;
	public final ObjectType<BlockSilt, ItemBlockMulti> type;
	
	public final PropertyIMetadata<EnumSilt> variantProp;
	public final List<EnumSilt> variants;
	
	public BlockSilt(List<EnumSilt> variants, SiltBlocks owner, ObjectType<BlockSilt, ItemBlockMulti> type)
	{
		super(Material.sand);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		this.variantProp = new PropertyIMetadata<EnumSilt>("variant", variants);
		
		blockState = new BlockState(this, variantProp);
		setDefaultState(blockState.getBaseState());
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		setStepSound(soundTypeSand);
		setHardness(0.5F);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, (EnumSilt) state.getValue(variantProp));
	}
}
