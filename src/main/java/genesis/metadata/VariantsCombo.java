package genesis.metadata;

import java.util.*;

import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType.ObjectNamePosition;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.item.*;

/**
 * Used to create a combo of Blocks or Items with variants. Can only contain <i>one</i> ObjectType.
 * 
 * @author Zaggy1024
 */
public class VariantsCombo<T> extends VariantsOfTypesCombo
{
	public final ObjectType<T> soleType;
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param objectType The sole ObjectType that this VariantsCombo will use.
	 * @param itemClass the Item class to initialize for each variant.
	 */
	public VariantsCombo(final ObjectType<T> objectType, List<IMetadata> variants)
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
	public VariantsCombo(ObjectType<T> objectType, IMetadata[] variants)
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
	public VariantsCombo(String name, String unlocalizedName, Class<? extends Block> blockClass, Class<? extends Item> itemClass, IMetadata[] variants)
	{
		this(new ObjectType(name, unlocalizedName, blockClass, itemClass), variants);
	}
	
	/**
	 * Constructs a BlocksAndItemsWithVariantsOfTypes with one ObjectType for simple one-type combos.
	 *  
	 * @param name The name to attach to each variant (i.e. "ore", to result in "variant_ore").
	 * @param blockClass The Block class to initialize for each variant.
	 * @param itemClass the Item class to initialize for each variant.
	 */
	public VariantsCombo(String name, Class<? extends Block> blockClass, Class<? extends Item> itemClass, IMetadata[] variants)
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
	public VariantsCombo(String name, String unlocalizedName, IMetadata[] variants)
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
	public VariantsCombo(String name, IMetadata[] variants)
	{
		this(name, name, variants);
	}
	
	/**
	 * Gets a stack of the specified Item in this combo.
	 */
	public ItemStack getStack(IMetadata variant, int stackSize)
	{
		return getStack(soleType, variant, stackSize);
	}
	
	/**
	 * Gets a stack of the specified Item in this combo.
	 */
	public ItemStack getStack(IMetadata variant)
	{
		return getStack(variant, 1);
	}

	/**
	 * Gets the Block or Item for the type and variant.
	 * 
	 * @return The Block/Item casted to the type provided by the generic type of this combo's sole {@link #ObjectType}.
	 */
	public T getObject(IMetadata variant)
	{
		return (T) getVariantEntry(soleType, variant).object;
	}
	
	/**
	 * Gets the metadata used to get the Item of this variant.
	 */
	public int getMetadata(IMetadata variant)
	{
		return getMetadata(soleType, variant);
	}
	
	/**
	 * Gets all the Blocks or Items that the sole {@link #ObjectType} uses.
	 */
	public HashSet<T> getObjects()
	{
		HashSet<T> out = new HashSet();
		
		for (IMetadata variant : getValidVariants(soleType))
		{
			out.add(getObject(soleType, variant));
		}
		
		return out;
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
	public List<IMetadata> getValidVariants()
	{
		return getValidVariants(soleType);
	}
}
