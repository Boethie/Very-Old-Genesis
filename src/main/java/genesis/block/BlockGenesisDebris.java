package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.metadata.VariantsOfTypesCombo.BlockProperties;
import genesis.util.BlockStateToMetadata;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

import java.util.List;

public class BlockGenesisDebris extends BlockGenesisVariants<EnumTree>
{
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public static final PropertyBool FROM_TREE = PropertyBool.create("from_tree");
	
	public BlockGenesisDebris(List<EnumTree> variants, TreeBlocksAndItems owner, ObjectType<BlockGenesisDebris, ItemBlockMulti<EnumTree>> type)
	{
		super(variants, owner, type, Material.leaves);
		
		blockState = new BlockState(this, variantProp, FROM_TREE);
		setDefaultState(blockState.getBaseState().withProperty(FROM_TREE, true));
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setStepSound(soundTypeGrass);
		
		// TODO: Should it drop an item? addDrop(type);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, variantProp, FROM_TREE);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, variantProp, FROM_TREE);
	}
}
