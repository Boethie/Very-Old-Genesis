package genesis.block;

import genesis.combo.ObjectType;
import genesis.combo.SiltBlocks;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumSilt;
import genesis.combo.variant.PropertyIMetadata;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.util.BlockStateToMetadata;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockSiltstone extends BlockGenesis
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final SiltBlocks owner;
	public final ObjectType<BlockSiltstone, ItemBlockMulti<EnumSilt>> type;
	
	public final PropertyIMetadata<EnumSilt> variantProp;
	public final List<EnumSilt> variants;
	
	public BlockSiltstone(SiltBlocks owner, ObjectType<BlockSiltstone, ItemBlockMulti<EnumSilt>> type, List<EnumSilt> variants, Class<EnumSilt> variantClass)
	{
		super(Material.rock, SoundType.STONE);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		this.variantProp = new PropertyIMetadata<EnumSilt>("variant", variants, variantClass);
		
		blockState = new BlockStateContainer(this, variantProp);
		setDefaultState(blockState.getBaseState());
		
		setHardness(0.9F);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
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
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, state.getValue(variantProp));
	}
}
