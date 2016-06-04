package genesis.combo;

import java.util.*;

import com.google.common.collect.ImmutableList;

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
	public static <V extends IMetadata<V>, B extends Block, I extends Item>
	VariantsCombo<V, B, I> create(String name, ObjectType<V, B, I> type, Class<V> variantClass, List<V> variants)
	{
		return new VariantsCombo<>(name, type, variantClass, variants);
	}
	
	public static <V extends IMetadata<V>, B extends Block, I extends Item>
	VariantsCombo<V, B, I> create(String name, ObjectType<V, B, I> objectType, Class<V> variantClass, V[] variants)
	{
		return new VariantsCombo<>(name, objectType, variantClass, variants);
	}
	
	private final ObjectType<V, B, I> type;
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param objectType The sole ObjectType that this VariantsCombo will use.
	 */
	public VariantsCombo(String name, ObjectType<V, B, I> objectType, Class<V> variantClass, List<V> variants)
	{
		super(name, ImmutableList.of(objectType), variantClass, variants);
		
		type = objectType;
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param objectType The sole ObjectType that this VariantsCombo will use.
	 */
	public VariantsCombo(String name, ObjectType<V, B, I> objectType, Class<V> variantClass, V[] variants)
	{
		this(name, objectType, variantClass, ImmutableList.copyOf(variants));
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 * @param unlocalizedName The unlocalized name for each variant.
	 * @param blockClass The Block class to initialize for each variant.
	 * @param itemClass the Item class to initialize for each variant.
	 */
	public VariantsCombo(String name, String typeName, String typeUnlocalizedName, Class<B> blockClass, Class<I> itemClass,
			Class<V> variantClass, V[] variants)
	{
		this(name, new ObjectType<>(variantClass, typeName, typeUnlocalizedName, blockClass, itemClass),
				variantClass, variants);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 * @param blockClass The Block class to initialize for each variant.
	 * @param itemClass the Item class to initialize for each variant.
	 */
	public VariantsCombo(String name, String typeName, Class<B> blockClass, Class<I> itemClass,
			Class<V> variantClass, V[] variants)
	{
		this(name, typeName, typeName, blockClass, itemClass, variantClass, variants);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.<br><br>
	 * 
	 * This overload of the other simple constructor is for simple <i>Item</i> combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 */
	public VariantsCombo(String name, String typeName, String typeUnlocalizedName, Class<V> variantClass, V[] variants)
	{
		this(name, typeName, typeUnlocalizedName, null, null, variantClass, variants);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.<br><br>
	 * 
	 * This overload of the other simple constructor is for simple <i>Item</i> combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 */
	public VariantsCombo(String name, String typeName, Class<V> variantClass, V[] variants)
	{
		this(name, typeName, typeName, variantClass, variants);
	}
	
	public ObjectType<V, B, I> getObjectType()
	{
		return type;
	}
	
	@Override
	public VariantsCombo<V, B, I> setNames(String domain, String unloc)
	{
		super.setNames(domain, unloc);
		return this;
	}
	
	/**
	 * Gets a stack of the specified Item in this combo.
	 */
	public ItemStack getStack(V variant, int stackSize)
	{
		return getStack(type, variant, stackSize);
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
		return getItemMetadata(type, variant);
	}
	
	/**
	 * Gets a random IBlockState.
	 */
	public IBlockState getBlockState(V variant)
	{
		return super.getBlockState(type, variant);
	}
	
	/**
	 * Gets a random IBlockState.
	 */
	public IBlockState getRandomBlockState(Random rand)
	{
		return getRandomBlockState(type, rand);
	}
	
	/**
	 * Gets the valid variants for this combo's sole {@link ObjectType}.
	 */
	public List<V> getValidVariants()
	{
		return getValidVariants(type);
	}
	
	/**
	 * Gets the Block for this combo's sole {@link ObjectType} and the provided variant.
	 */
	public B getBlock(V variant)
	{
		return super.getBlock(type, variant);
	}
	
	/**
	 * Gets the list of Blocks for this combo's sole {@link ObjectType}.
	 */
	public Collection<B> getBlocks()
	{
		return super.getBlocks(type);
	}
	
	/**
	 * Gets the Item for this combo's sole {@link ObjectType} and the provided variant.
	 */
	public I getItem(V variant)
	{
		return super.getItem(type, variant);
	}
	
	/**
	 * Gets the list of Items for this combo's sole {@link ObjectType}.
	 */
	public Collection<I> getItems()
	{
		return super.getItems(type);
	}
	
	/**
	 * @return Whether the state is contained by this combo.
	 */
	public boolean containsStack(ItemStack stack)
	{
		return super.isStackOf(stack, type);
	}
	
	/**
	 * @return Whether the stack is contained in this combo.
	 */
	public boolean isStackOf(ItemStack stack)
	{
		return super.isStackOf(stack, type);
	}
	
	/**
	 * @return Whether the stack is of the specified variant in this combo.
	 */
	public boolean isStackOf(ItemStack stack, V variant)
	{
		return super.isStackOf(stack, variant, type);
	}
	
	/**
	 * @return Whether the state is contained by this combo.
	 */
	public boolean containsState(IBlockState state)
	{
		return super.isStateOf(state, type);
	}
	
	/**
	 * @return Whether the state is contained by this combo.
	 */
	public boolean isStateOf(IBlockState state, V variant)
	{
		return super.isStateOf(state, variant, type);
	}
	
	/**
	 * @return listToFill, after having added all sub-items for this list of variants.
	 */
	public List<ItemStack> fillSubItems(List<V> variants, List<ItemStack> listToFill, Collection<V> noDrops)
	{
		return super.fillSubItems(type, variants, listToFill, noDrops);
	}
	
	/**
	 * @return listToFill, after having added all sub-items for this list of variants.
	 */
	@SafeVarargs
	public final List<ItemStack> fillSubItems(List<V> variants, List<ItemStack> listToFill, V... noDrops)
	{
		return super.fillSubItems(type, variants, listToFill, noDrops);
	}
	
	/**
	 * @return All sub-items with the variants contained in the list.
	 */
	public List<ItemStack> getSubItems(List<V> variants, Collection<V> noDrops)
	{
		return super.getSubItems(type, variants, noDrops);
	}
	
	/**
	 * @return All sub-items with the variants contained in the list.
	 */
	@SafeVarargs
	public final List<ItemStack> getSubItems(List<V> variants, V... noDrops)
	{
		return super.getSubItems(type, variants, noDrops);
	}
}
