package genesis.combo;

import java.util.*;

import com.google.common.collect.ImmutableList;

import genesis.combo.VariantsOfTypesCombo.ObjectType;
import genesis.combo.variant.IMetadata;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.item.*;

/**
 * Used to create a combo of Blocks or Items with variants. Can only contain <i>one</i> ObjectType.
 * 
 * @author Zaggy1024
 */
public class VariantsCombo<V extends IMetadata<V>, B extends Block, I extends Item> extends VariantsOfTypesCombo<V>
{
	public static <V extends IMetadata<V>, B extends Block, I extends Item> VariantsCombo<V, B, I> create(ObjectType<B, I> objectType, Class<V> variantClass, List<V> variants)
	{
		return new VariantsCombo<V, B, I>(objectType, variantClass, variants);
	}
	
	public static <V extends IMetadata<V>, B extends Block, I extends Item> VariantsCombo<V, B, I> create(ObjectType<B, I> objectType, Class<V> variantClass, V[] variants)
	{
		return new VariantsCombo<V, B, I>(objectType, variantClass, variants);
	}
	
	public final ObjectType<B, I> soleType;
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param objectType The sole ObjectType that this VariantsCombo will use.
	 */
	public VariantsCombo(final ObjectType<B, I> objectType, Class<V> variantClass, List<V> variants)
	{
		super(ImmutableList.of(objectType), variantClass, variants);
		
		soleType = objectType;
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param objectType The sole ObjectType that this VariantsCombo will use.
	 */
	public VariantsCombo(ObjectType<B, I> objectType, Class<V> variantClass, V[] variants)
	{
		this(objectType, variantClass, Arrays.asList(variants));
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 * @param unlocalizedName The unlocalized name for each variant.
	 * @param blockClass The Block class to initialize for each variant.
	 * @param itemClass the Item class to initialize for each variant.
	 */
	public VariantsCombo(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass, Class<V> variantClass, V[] variants)
	{
		this(new ObjectType<B, I>(name, unlocalizedName, blockClass, itemClass), variantClass, variants);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 * @param blockClass The Block class to initialize for each variant.
	 * @param itemClass the Item class to initialize for each variant.
	 */
	public VariantsCombo(String name, Class<? extends B> blockClass, Class<? extends I> itemClass, Class<V> variantClass, V[] variants)
	{
		this(name, name, blockClass, itemClass, variantClass, variants);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.<br><br>
	 * 
	 * This overload of the other simple constructor is for simple <i>Item</i> combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 */
	public VariantsCombo(String name, String unlocalizedName, Class<V> variantClass, V[] variants)
	{
		this(name, unlocalizedName, null, null, variantClass, variants);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.<br><br>
	 * 
	 * This overload of the other simple constructor is for simple <i>Item</i> combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 */
	public VariantsCombo(String name, Class<V> variantClass, V[] variants)
	{
		this(name, name, variantClass, variants);
	}
	
	/**
	 * Gets a stack of the specified Item in this combo.
	 */
	public ItemStack getStack(V variant, int stackSize)
	{
		return getStack(soleType, variant, stackSize);
	}
	
	/**
	 * Gets a stack of the specified Item in this combo.
	 */
	public ItemStack getStack(V variant)
	{
		return getStack(variant, 1);
	}
	
	/**
	 * Gets the metadata used to get the Item of this variant.
	 */
	public int getItemMetadata(V variant)
	{
		return getItemMetadata(soleType, variant);
	}
	
	/**
	 * Gets a random IBlockState.
	 */
	public IBlockState getBlockState(V variant)
	{
		return super.getBlockState(soleType, variant);
	}
	
	/**
	 * Gets a random IBlockState.
	 */
	public IBlockState getRandomBlockState(Random rand)
	{
		return getRandomBlockState(soleType, rand);
	}
	
	/**
	 * Gets the valid variants for this combo's sole {@link ObjectType}.
	 */
	public List<V> getValidVariants()
	{
		return getValidVariants(soleType);
	}
	
	/**
	 * Gets the Block for this combo's sole {@link ObjectType} and the provided variant.
	 */
	public B getBlock(V variant)
	{
		return super.getBlock(soleType, variant);
	}
	
	/**
	 * Gets the list of Blocks for this combo's sole {@link ObjectType}.
	 */
	public Collection<B> getBlocks()
	{
		return super.getBlocks(soleType);
	}
	
	/**
	 * Gets the Item for this combo's sole {@link ObjectType} and the provided variant.
	 */
	public I getItem(V variant)
	{
		return super.getItem(soleType, variant);
	}
	
	/**
	 * Gets the list of Items for this combo's sole {@link ObjectType}.
	 */
	public Collection<I> getItems()
	{
		return super.getItems(soleType);
	}
	
	/**
	 * @return Whether the state is contained by this combo.
	 */
	public boolean containsStack(ItemStack stack)
	{
		return super.isStackOf(stack, soleType);
	}
	
	/**
	 * @return Whether the state is contained by this combo.
	 */
	public boolean isStackOf(ItemStack stack, V variant)
	{
		return super.isStackOf(stack, soleType, variant);
	}
	
	/**
	 * @return Whether the state is contained by this combo.
	 */
	public boolean containsState(IBlockState state)
	{
		return super.isStateOf(state, soleType);
	}
	
	/**
	 * @return Whether the state is contained by this combo.
	 */
	public boolean isStateOf(IBlockState state, V variant)
	{
		return super.isStateOf(state, soleType, variant);
	}
	
	/**
	 * @return listToFill, after having added all sub-items for this list of variants.
	 */
	public List<ItemStack> fillSubItems(List<V> variants, List<ItemStack> listToFill, Collection<V> noDrops)
	{
		return super.fillSubItems(soleType, variants, listToFill, noDrops);
	}
	
	/**
	 * @return listToFill, after having added all sub-items for this list of variants.
	 */
	@SafeVarargs
	public final List<ItemStack> fillSubItems(List<V> variants, List<ItemStack> listToFill, V... noDrops)
	{
		return super.fillSubItems(soleType, variants, listToFill, noDrops);
	}
	
	/**
	 * @return All sub-items with the variants contained in the list.
	 */
	public List<ItemStack> getSubItems(List<V> variants, Collection<V> noDrops)
	{
		return super.getSubItems(soleType, variants, noDrops);
	}
	
	/**
	 * @return All sub-items with the variants contained in the list.
	 */
	@SafeVarargs
	public final List<ItemStack> getSubItems(List<V> variants, V... noDrops)
	{
		return super.getSubItems(soleType, variants, noDrops);
	}
}
