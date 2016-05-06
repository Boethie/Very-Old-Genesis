package genesis.combo;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import genesis.combo.VariantsOfTypesCombo.TypeNamePosition;
import genesis.combo.variant.*;
import genesis.item.*;
import genesis.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Represents a type of item and/or block for a combo. Contains the data that determines
 * what/how the items and blocks are constructed and registered.
 *
 * @param <B> For the type that should be returned when getting this type's Block.
 * @param <I> For the type that should be returned when getting this type's Item.
 */
public class ObjectType<V extends IMetadata<V>, B extends Block, I extends Item> implements IVariant
{
	private final String name;
	protected String resourceName;
	protected String unlocalizedName;
	protected VariantsOfTypesCombo.TypeNamePosition typeNamePosition = TypeNamePosition.PREFIX;
	
	private final Class<B> blockClass;
	private Object[] blockArgs = {};
	private final Class<I> itemClass;
	private Object[] itemArgs = {};

	protected Class<V> variantClass;
	protected Predicate<? super V> variantFilter;
	
	protected boolean separateVariantJsons = true;
	protected IProperty<?>[] stateMapIgnoredProperties;
	
	protected boolean variantAsName = true;
	protected boolean registerVariantModels = true;
	
	protected Function<V, String> variantNameFunction = null;
	protected ObjectFunction<? super B, ? super I> afterConstructed;
	protected ObjectFunction<? super B, ? super I> afterRegistered;
	
	protected CreativeTabs tab = null;
	
	@SuppressWarnings("unchecked")
	public ObjectType(Class<V> variantClass, String name, String unlocalizedName, Class<B> blockClass, Class<I> itemClass)
	{
		this.variantClass = variantClass;
		this.name = name;
		this.resourceName = name;
		this.unlocalizedName = unlocalizedName;
		
		if (itemClass == null)
		{
			if (blockClass != null)
			{
				itemClass = (Class<I>) ItemBlockMulti.class;
			}
			else
			{
				itemClass = (Class<I>) ItemMulti.class;
			}
		}
		
		this.blockClass = blockClass;
		this.itemClass = itemClass;
	}
	
	public ObjectType(Class<V> variantClass, String name, Class<B> blockClass, Class<I> itemClass)
	{
		this(variantClass, name, name, blockClass, itemClass);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	public ObjectType<V, B, I> setResourceName(String name)
	{
		resourceName = name;
		
		return this;
	}
	
	public String getResourceName()
	{
		return resourceName;
	}
	
	public ObjectType<V, B, I> setUnlocalizedName(String unlocName)
	{
		this.unlocalizedName = unlocName;
		
		return this;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	public VariantsOfTypesCombo.TypeNamePosition getTypeNamePosition()
	{
		return typeNamePosition;
	}
	
	public ObjectType<V, B, I> setTypeNamePosition(VariantsOfTypesCombo.TypeNamePosition typeNamePosition)
	{
		this.typeNamePosition = typeNamePosition;
		
		return this;
	}
	
	public Class<B> getBlockClass()
	{
		return blockClass;
	}
	
	public Class<I> getItemClass()
	{
		return itemClass;
	}
	
	public ObjectType<V, B, I> setVariantFilter(Predicate<? super V> filter)
	{
		variantFilter = filter;
		return this;
	}
	
	public List<V> getValidVariants(List<V> list)
	{
		Stream<V> stream = list.stream()
				.flatMap(StreamUtils.instances(variantClass));
		
		if (variantFilter != null)
			stream = stream.filter(variantFilter);
		
		return stream.collect(StreamUtils.toImmList());
	}
	
	public boolean getUseSeparateVariantJsons()
	{
		return separateVariantJsons;
	}
	
	public ObjectType<V, B, I> setUseSeparateVariantJsons(boolean value)
	{
		separateVariantJsons = value;
		return this;
	}
	
	public boolean usesVariantAsRegistryName()
	{
		return variantAsName;
	}
	
	/**
	 * Sets whether to register blocks and items with their variant names,
	 * if the maximum subset size is 1 (so one variant per block and item instance).
	 */
	public ObjectType<V, B, I> setUseVariantAsRegistryName(boolean value)
	{
		variantAsName = value;
		return this;
	}
	
	public boolean shouldRegisterVariantModels()
	{
		return registerVariantModels;
	}
	
	/**
	 * Sets whether to register the variant models automatically when registering the blocks.
	 */
	public ObjectType<V, B, I> setShouldRegisterVariantModels(boolean value)
	{
		registerVariantModels = value;
		return this;
	}
	
	public Object[] getBlockArguments()
	{
		return blockArgs;
	}
	
	public ObjectType<V, B, I> setBlockArguments(Object... args)
	{
		this.blockArgs = args;
		return this;
	}

	public Object[] getItemArguments()
	{
		return itemArgs;
	}
	
	public ObjectType<V, B, I> setItemArguments(Object... args)
	{
		this.itemArgs = args;
		return this;
	}
	
	public ObjectType<V, B, I> setCreativeTab(CreativeTabs tab)
	{
		this.tab = tab;
		return this;
	}
	
	public ObjectType<V, B, I> setConstructedFunction(ObjectFunction<? super B, ? super I> function)
	{
		afterConstructed = function;
		return this;
	}
	
	public void afterConstructed(Block block, Item item, List<V> variants)
	{
		if (tab != null)
		{
			if (block != null)
				block.setCreativeTab(tab);
			if (item != null)
				item.setCreativeTab(tab);
		}
		
		if (afterConstructed != null)
			afterConstructed.apply(ReflectionUtils.nullSafeCast(blockClass, block), ReflectionUtils.nullSafeCast(itemClass, item));
	}
	
	public ObjectType<V, B, I> setRegisteredFunction(ObjectFunction<? super B, ? super I> function)
	{
		afterRegistered = function;
		return this;
	}
	
	public final void afterRegistered(Block block, Item item)
	{
		if (afterRegistered != null)
			afterRegistered.apply(ReflectionUtils.nullSafeCast(blockClass, block), ReflectionUtils.nullSafeCast(itemClass, item));
	}
	
	public ObjectType<V, B, I> setIgnoredProperties(IProperty<?>... properties)
	{
		stateMapIgnoredProperties = properties;
		return this;
	}
	
	public void customizeStateMap(FlexibleStateMap stateMap)
	{
		if (stateMapIgnoredProperties != null)
		{
			stateMap.clearIgnoredProperties();
			stateMap.addIgnoredProperties(stateMapIgnoredProperties);
		}
	}
	
	public ObjectType<V, B, I> setVariantNameFunction(Function<V, String> function)
	{
		variantNameFunction = function;
		return this;
	}
	
	public final String getVariantName(V variant)
	{
		if (variantNameFunction != null)
			return variantNameFunction.apply(variant);
		
		String resource = variant.getName();
		
		if ("".equals(resource))
		{
			resource += getResourceName();
		}
		else
		{
			String sep = getResourceName().equals("") ? "" : "_";
			
			switch (getTypeNamePosition())
			{
			case PREFIX:
				resource = getResourceName() + sep + resource;
				break;
			case POSTFIX:
				resource += sep + getResourceName();
				break;
			default:
			}
		}
		
		return resource;
	}
	
	@Override
	public String toString()
	{
		return getName() + "[name=" + getName() + ",item=" + getItemClass() + ",block=" + getBlockClass() + "]";
	}
	
	// create, createBlock and createItem methods for convenience.
	/**
	 * Full parameters.
	 */
	public static <V extends IMetadata<V>, B extends Block, I extends Item>
	ObjectType<V, B, I> create(Class<V> variantClass, String name, String unlocalizedName, Class<B> blockClass, Class<I> itemClass)
	{
		return new ObjectType<V, B, I>(variantClass, name, unlocalizedName, blockClass, itemClass);
	}
	
	/**
	 * Full parameters, default ItemBlock.
	 */
	public static <V extends IMetadata<V>, B extends Block>
	ObjectType<V, B, ItemBlockMulti<V>> createBlock(Class<V> variantClass, String name, String unlocalizedName, Class<B> blockClass)
	{
		return create(variantClass, name, unlocalizedName, blockClass, ReflectionUtils.convertClass(ItemBlockMulti.class));
	}
	
	/**
	 * Full parameters, no block.
	 */
	public static <V extends IMetadata<V>, I extends Item>
	ObjectType<V, Block, I> createItem(Class<V> variantClass, String name, String unlocalizedName, Class<I> itemClass)
	{
		return create(variantClass, name, unlocalizedName, null, itemClass);
	}
	
	/**
	 * Full parameters, default item.
	 */
	public static <V extends IMetadata<V>>
	ObjectType<V, Block, ItemMulti<V>> createItem(Class<V> variantClass, String name, String unlocalizedName)
	{
		return create(variantClass, name, unlocalizedName, null, null);
	}
	
	
	/**
	 * Same name for registry and unlocalized, all other parameters.
	 */
	public static <V extends IMetadata<V>, B extends Block, I extends Item>
	ObjectType<V, B, I> create(Class<V> variantClass, String name, Class<B> blockClass, Class<I> itemClass)
	{
		return new ObjectType<>(variantClass, name, blockClass, itemClass);
	}
	
	/**
	 * Same name for registry and unlocalized, default ItemBlock.
	 */
	public static <V extends IMetadata<V>, B extends Block>
	ObjectType<V, B, ItemBlockMulti<V>> createBlock(Class<V> variantClass, String name, Class<B> blockClass)
	{
		return create(variantClass, name, blockClass, ReflectionUtils.convertClass(ItemBlockMulti.class));
	}
	
	/**
	 * Same name for registry and unlocalized, no block.
	 */
	public static <V extends IMetadata<V>, I extends Item>
	ObjectType<V, Block, I> createItem(Class<V> variantClass, String name, Class<I> itemClass)
	{
		return create(variantClass, name, null, itemClass);
	}
	
	/**
	 * Same name for registry and unlocalized, default item.
	 */
	public static <V extends IMetadata<V>>
	ObjectType<V, Block, ItemMulti<V>> createItem(Class<V> variantClass, String name)
	{
		return create(variantClass, name, null, null);
	}
}