package genesis.combo;

import com.google.common.collect.*;

import java.util.*;

import genesis.block.BlockGenesisDebris;
import genesis.combo.variant.EnumDebrisOther;
import genesis.combo.variant.EnumTree;
import genesis.combo.variant.IMetadata;
import genesis.combo.variant.MultiMetadataList;
import genesis.combo.variant.MultiMetadataList.MultiMetadata;
import genesis.item.ItemBlockMulti;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class DebrisBlocks extends VariantsCombo<MultiMetadata, BlockGenesisDebris, ItemBlockMulti<MultiMetadata>>
{
	public static final MultiMetadataList VARIANTS;
	
	static
	{
		ArrayList<IMetadata<?>> list = new ArrayList<IMetadata<?>>();
		Iterables.addAll(list, Iterables.filter(Arrays.asList(EnumTree.values()), (v) -> v.hasDebris()));
		Collections.addAll(list, EnumDebrisOther.values());
		VARIANTS = new MultiMetadataList(list);
	}
	
	public DebrisBlocks()
	{
		super(ObjectType.createBlock(MultiMetadata.class, "debris", BlockGenesisDebris.class), MultiMetadata.class, VARIANTS);
		
		setNames(Constants.MOD_ID, Unlocalized.PREFIX);
		
		getObjectType().setTypeNamePosition(TypeNamePosition.POSTFIX);
	}
	
	// Trees
	public MultiMetadata getVariant(EnumTree variant)
	{
		return VARIANTS.getVariant(variant);
	}
	
	public ItemStack getStack(EnumTree variant, int stackSize)
	{
		return super.getStack(getVariant(variant), stackSize);
	}
	
	public ItemStack getStack(EnumTree variant)
	{
		return getStack(variant, 1);
	}
	
	public int getItemMetadata(EnumTree variant)
	{
		return super.getItemMetadata(getVariant(variant));
	}
	
	public IBlockState getBlockState(EnumTree variant)
	{
		return super.getBlockState(getVariant(variant));
	}
	
	public BlockGenesisDebris getBlock(EnumTree variant)
	{
		return super.getBlock(getVariant(variant));
	}
	
	public ItemBlockMulti<MultiMetadata> getItem(EnumTree variant)
	{
		return super.getItem(getVariant(variant));
	}
	
	public boolean isStackOf(ItemStack stack, EnumTree variant)
	{
		return super.isStackOf(stack, getVariant(variant));
	}
	
	public boolean isStateOf(IBlockState state, EnumTree variant)
	{
		return super.isStateOf(state, getVariant(variant));
	}
	
	// Other
	public MultiMetadata getVariant(EnumDebrisOther variant)
	{
		return VARIANTS.getVariant(variant);
	}
	
	public ItemStack getStack(EnumDebrisOther variant, int stackSize)
	{
		return super.getStack(getVariant(variant), stackSize);
	}
	
	public ItemStack getStack(EnumDebrisOther variant)
	{
		return getStack(variant, 1);
	}
	
	public int getItemMetadata(EnumDebrisOther variant)
	{
		return super.getItemMetadata(getVariant(variant));
	}
	
	public IBlockState getBlockState(EnumDebrisOther variant)
	{
		return super.getBlockState(getVariant(variant));
	}
	
	public BlockGenesisDebris getBlock(EnumDebrisOther variant)
	{
		return super.getBlock(getVariant(variant));
	}
	
	public ItemBlockMulti<MultiMetadata> getItem(EnumDebrisOther variant)
	{
		return super.getItem(getVariant(variant));
	}
	
	public boolean isStackOf(ItemStack stack, EnumDebrisOther variant)
	{
		return super.isStackOf(stack, getVariant(variant));
	}
	
	public boolean isStateOf(IBlockState state, EnumDebrisOther variant)
	{
		return super.isStateOf(state, getVariant(variant));
	}
}
