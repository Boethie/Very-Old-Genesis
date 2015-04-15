package genesis.block;

import java.util.List;

import genesis.client.GenesisSounds;
import genesis.common.GenesisItems;
import genesis.metadata.EnumDung;
import genesis.metadata.Properties;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.util.BlockStateToMetadata;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlockDung extends BlockGenesis
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
	
	public final List<EnumDung> variants;
	public final PropertyEnum variantProp;
	
	public BlockDung(List<EnumDung> variants, VariantsOfTypesCombo owner)
	{
		super(Material.ground);
		
		this.owner = owner;
		
		this.variants = variants;
		variantProp = PropertyEnum.create("variant", EnumDung.class, variants);

		blockState = new BlockState(this, variantProp);
		setDefaultState(getBlockState().getBaseState());
		
		setHardness(0.5F);
		setStepSound(GenesisSounds.DUNG);
		
		//setItemDropped(GenesisItems.dung);
		//setQuantityDropped(4);
		
		Blocks.fire.setFireInfo(this, 5, 5);
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
