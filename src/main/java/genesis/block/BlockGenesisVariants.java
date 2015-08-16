package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.metadata.IMetadata;
import genesis.metadata.PropertyIMetadata;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.BlockProperties;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.BlockStateToMetadata;
import genesis.util.RandomVariantDrop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@SuppressWarnings("rawtypes")
public class BlockGenesisVariants<V extends IMetadata> extends Block
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final VariantsOfTypesCombo<ObjectType<BlockGenesisVariants, ? extends Item>, V> owner;
	public final ObjectType<BlockGenesisVariants, ? extends Item> type;
	
	public final List<V> variants;
	public final PropertyIMetadata<V> variantProp;
	
	protected final HashSet<V> noItemVariants = new HashSet<V>();
	
	protected final List<RandomVariantDrop> drops = new ArrayList<RandomVariantDrop>();
	
	public BlockGenesisVariants(List<V> variants, VariantsOfTypesCombo<ObjectType<BlockGenesisVariants, ? extends Item>, V> owner, ObjectType<BlockGenesisVariants, ? extends Item> type, Material material)
	{
		super(material);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata<V>("variant", variants);
		
		blockState = new BlockState(this, variantProp);
		setDefaultState(getBlockState().getBaseState());
		
		addDrop(type);
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}
	
	@Override
	public BlockGenesisVariants<V> setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
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
	
	@Override
	public BlockGenesisVariants<V> setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(name);
		return this;
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	public BlockGenesisVariants<V> clearDrops()
	{
		drops.clear();
		
		return this;
	}
	
	public BlockGenesisVariants<V> addDrop(RandomVariantDrop drop)
	{
		drops.add(drop);
		
		return this;
	}
	
	public BlockGenesisVariants<V> addDrop(ObjectType type, int min, int max)
	{
		return addDrop(new RandomVariantDrop(owner, type, min, max));
	}
	
	public BlockGenesisVariants<V> addDrop(ObjectType type)
	{
		return addDrop(type, 1, 1);
	}
	
	@Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
		ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();
		V variant = (V) state.getValue(variantProp);
		
		if (!noItemVariants.contains(variant))
		{
	        Random rand = world instanceof World ? ((World) world).rand : RANDOM;
			
			for (RandomVariantDrop drop : drops)
			{
				ItemStack stack = drop.getRandomStack(variant, rand);
				
				if (stack != null)
				{
					stackList.add(stack);
				}
			}
		}
		
		return stackList;
    }
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, (V) state.getValue(variantProp));
	}
}
