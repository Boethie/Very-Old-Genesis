package genesis.block;

import java.util.*;

import genesis.common.GenesisCreativeTabs;
import genesis.item.*;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class BlockGenesisVariants<V extends IMetadata, C extends VariantsOfTypesCombo> extends Block
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final C owner;
	public final ObjectType<BlockGenesisVariants<V, C>, ItemBlockMulti> type;
	
	public final List<V> variants;
	public final PropertyIMetadata<V> variantProp;
	
	protected final HashSet<V> noItemVariants = new HashSet();
	
	protected final ArrayList<RandomVariantDrop> drops = new ArrayList();
	
	public BlockGenesisVariants(List<V> variants, C owner, ObjectType<BlockGenesisVariants<V, C>, ItemBlockMulti> type, Material material)
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
	public BlockGenesisVariants<V, C> setCreativeTab(CreativeTabs tab)
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
	public BlockGenesisVariants setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(Unlocalized.PREFIX + name);
		
		return this;
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	public BlockGenesisVariants clearDrops()
	{
		drops.clear();
		
		return this;
	}
	
	public BlockGenesisVariants addDrop(RandomVariantDrop drop)
	{
		drops.add(drop);
		
		return this;
	}
	
	public BlockGenesisVariants addDrop(ObjectType type, int min, int max)
	{
		return addDrop(new RandomVariantDrop(owner, type, min, max));
	}
	
	public BlockGenesisVariants addDrop(ObjectType type)
	{
		return addDrop(type, 1, 1);
	}
	
	@Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
		ArrayList<ItemStack> stackList = new ArrayList();
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
