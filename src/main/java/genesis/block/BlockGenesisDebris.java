package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.metadata.EnumTree;
import genesis.metadata.PropertyIMetadata;
import genesis.metadata.TreeBlocksAndItems;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.metadata.VariantsOfTypesCombo.BlockProperties;
import genesis.util.BlockStateToMetadata;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class BlockGenesisDebris extends BlockGenesis
{
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[0]; //Not sure how to use this yet
	}
	
	public final TreeBlocksAndItems owner;
	public final ObjectType<BlockGenesisDebris, ItemBlockMulti<EnumTree>> type;
	
	public final List<EnumTree> variants;
	public final PropertyIMetadata<EnumTree> variantProp;
	
	public static final PropertyBool FROM_TREE = PropertyBool.create("from_tree");
	
	
	public BlockGenesisDebris(List<EnumTree> variants, TreeBlocksAndItems owner, ObjectType<BlockGenesisDebris, ItemBlockMulti<EnumTree>> type)
	{
		super(Material.leaves);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata<EnumTree>("variant", variants);
		setDefaultState(getBlockState().getBaseState().withProperty(FROM_TREE, true));
		
		blockState = new BlockState(this, variantProp, FROM_TREE);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setStepSound(soundTypeGrass);
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
		return owner.getItemMetadata(type, (EnumTree) state.getValue(variantProp));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
	{
		return owner.getStack(type, (EnumTree) world.getBlockState(pos).getValue(variantProp));
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(type, variants, list);
	}
}
