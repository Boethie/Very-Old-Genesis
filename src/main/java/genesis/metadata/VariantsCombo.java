package genesis.metadata;

import java.util.*;

import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.metadata.VariantsOfTypesCombo.ObjectNamePosition;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.item.*;

/**
 * Used to create a combo of Blocks or Items with variants. Can only contain <i>one</i> ObjectType.
 * 
 * @author Zaggy1024
 */
public class VariantsCombo<V extends IMetadata, B extends Block, I extends Item> extends VariantsOfTypesCombo<ObjectType<B, I>, V>
{
	public final ObjectType<B, I> soleType;
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param objectType The sole ObjectType that this VariantsCombo will use.
	 * @param itemClass the Item class to initialize for each variant.
	 */
	public VariantsCombo(final ObjectType<B, I> objectType, List<V> variants)
	{
		super(new ArrayList(){{ add(objectType); }}, variants);
		
		soleType = types.get(0);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param objectType The sole ObjectType that this VariantsCombo will use.
	 * @param itemClass the Item class to initialize for each variant.
	 */
	public VariantsCombo(ObjectType<B, I> objectType, V[] variants)
	{
		this(objectType, Arrays.asList(variants));
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 * @param unlocalizedName The unlocalized name for each variant.
	 * @param blockClass The Block class to initialize for each variant.
	 * @param itemClass the Item class to initialize for each variant.
	 */
	public VariantsCombo(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass, V[] variants)
	{
		this(new ObjectType<B, I>(name, unlocalizedName, blockClass, itemClass), variants);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 * @param blockClass The Block class to initialize for each variant.
	 * @param itemClass the Item class to initialize for each variant.
	 */
	public VariantsCombo(String name, Class<? extends B> blockClass, Class<? extends I> itemClass, V[] variants)
	{
		this(name, name, blockClass, itemClass, variants);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.<br><br>
	 * 
	 * This overload of the other simple constructor is for simple <i>Item</i> combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 */
	public VariantsCombo(String name, String unlocalizedName, V[] variants)
	{
		this(name, unlocalizedName, null, null, variants);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.<br><br>
	 * 
	 * This overload of the other simple constructor is for simple <i>Item</i> combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 */
	public VariantsCombo(String name, V[] variants)
	{
		this(name, name, variants);
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
	public int getMetadata(V variant)
	{
		return getMetadata(soleType, variant);
	}
	
	/**
	 * Gets a random IBlockState.
	 */
	public IBlockState getRandomBlockState(Random rand)
	{
		return getRandomBlockState(soleType, rand);
	}
	
	/**
	 * Gets the valid variants for this combo's sole {@link #ObjectType}.
	 */
	public List<V> getValidVariants()
	{
		return getValidVariants(soleType);
	}
	
	/**
	 * Gets the list of Blocks for this combo's sole {@link #ObjectType}.
	 */
	public B getBlock(V variant)
	{
		return super.getBlock(soleType, variant);
	}
	
	/**
	 * Gets the list of Blocks for this combo's sole {@link #ObjectType}.
	 */
	public Collection<B> getBlocks()
	{
		return super.getBlocks(soleType);
	}
}
