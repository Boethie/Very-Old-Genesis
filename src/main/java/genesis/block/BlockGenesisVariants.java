package genesis.block;

import java.util.*;

import genesis.common.GenesisCreativeTabs;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;
import genesis.util.random.drops.blocks.VariantDrop;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class BlockGenesisVariants<V extends IMetadata> extends Block
{
	/**
	 * Used in {@link #VariantsOfTypesCombo}.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final VariantsOfTypesCombo<V> owner;
	public final ObjectType<? extends BlockGenesisVariants<V>, ? extends Item> type;
	
	public final List<V> variants;
	public final PropertyIMetadata<V> variantProp;
	
	protected final HashSet<V> noItemVariants = new HashSet<V>();
	
	protected final List<VariantDrop<V>> drops = new ArrayList<VariantDrop<V>>();
	
	public BlockGenesisVariants(List<V> variants, VariantsOfTypesCombo<V> owner, ObjectType<? extends BlockGenesisVariants<V>, ? extends Item> type, Material material)
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
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	public BlockGenesisVariants<V> clearDrops()
	{
		drops.clear();
		
		return this;
	}
	
	public BlockGenesisVariants<V> addDrop(VariantDrop<V> drop)
	{
		drops.add(drop);
		
		return this;
	}
	
	public BlockGenesisVariants<V> addDrop(ObjectType<?, ?> type, int min, int max)
	{
		return addDrop(new VariantDrop<V>(owner, type, min, max));
	}
	
	public BlockGenesisVariants<V> addDrop(ObjectType<?, ?> type)
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
			
			for (VariantDrop<V> drop : drops)
			{
				ItemStack stack = drop.getStack(state, rand);
				
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
