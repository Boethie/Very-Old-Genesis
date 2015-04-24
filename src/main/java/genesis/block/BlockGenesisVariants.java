package genesis.block;

import java.util.*;

import genesis.common.GenesisCreativeTabs;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;
import genesis.util.RandomVariantDrop;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGenesisVariants<T extends IMetadata, S extends VariantsOfTypesCombo> extends Block
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{};
	}
	
	public final S owner;
	public final ObjectType<BlockGenesisVariants<T, S>> type;
	
	public final List<T> variants;
	public final PropertyIMetadata variantProp;
	
	protected final HashSet<T> noItemVariants = new HashSet();
	
	protected final ArrayList<RandomVariantDrop> drops = new ArrayList();
	
	public BlockGenesisVariants(List<T> variants, S owner, ObjectType<BlockGenesisVariants<T, S>> type, Material material)
	{
		super(material);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata("variant", variants);

		blockState = new BlockState(this, variantProp);
		setDefaultState(getBlockState().getBaseState());
		
		addDrop(type);
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}

	@Override
	public Block setCreativeTab(CreativeTabs tab)
	{
		return super.setCreativeTab(tab);
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
		super.setUnlocalizedName(Constants.PREFIX + name);
		
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
	
	public BlockGenesisVariants addDrop(ObjectType type, RandomVariantDrop drop)
	{
		drops.add(drop);
		
		return this;
	}
	
	public BlockGenesisVariants addDrop(ObjectType type, int min, int max)
	{
		return addDrop(type, new RandomVariantDrop(owner, type, min, max));
	}
	
	public BlockGenesisVariants addDrop(ObjectType type)
	{
		return addDrop(type, 1, 1);
	}
	
	@Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
		ArrayList<ItemStack> stackList = new ArrayList();
		T variant = (T) state.getValue(variantProp);
		
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
		return owner.getMetadata(type, (T) state.getValue(variantProp));
	}
}
