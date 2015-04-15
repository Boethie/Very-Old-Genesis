package genesis.block;

import java.util.List;

import genesis.client.GenesisSounds;
import genesis.common.GenesisCreativeTabs;
import genesis.metadata.EnumCoral;
import genesis.metadata.Properties;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockCoral extends BlockGenesis
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@Properties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final VariantsOfTypesCombo owner;
	
	public final List<EnumCoral> variants;
	public final PropertyEnum variantProp;
	
	public BlockCoral(List<EnumCoral> variants, VariantsOfTypesCombo owner)
	{
		super(Material.coral);
		
		this.owner = owner;
		
		this.variants = variants;
		variantProp = PropertyEnum.create("variant", EnumCoral.class, variants);
		
		blockState = new BlockState(this, variantProp);
		setDefaultState(getBlockState().getBaseState());
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		setHardness(0.75F);
		setResistance(8.5F);
		setStepSound(GenesisSounds.CORAL);
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
}
