package genesis.combo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;

import genesis.combo.VariantsOfTypesCombo.TypeNamePosition;
import genesis.combo.variant.IMetadata;
import genesis.combo.variant.IVariant;
import genesis.item.ItemBlockMulti;
import genesis.item.ItemMulti;
import genesis.util.FlexibleStateMap;
import genesis.util.ReflectionUtils;
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
public class ObjectType<B extends Block, I extends Item> implements IVariant
{
	private final String name;
	protected String resourceName;
	protected String unlocalizedName;
	protected VariantsOfTypesCombo.TypeNamePosition typeNamePosition = TypeNamePosition.PREFIX;
	
	private final Class<B> blockClass;
	private Object[] blockArgs = {};
	private final Class<I> itemClass;
	private Object[] itemArgs = {};
	
	protected Set<IMetadata<?>> excludeVariants;
	protected Set<IMetadata<?>> onlyVariants;
	protected boolean separateVariantJsons = true;
	protected IProperty<?>[] stateMapIgnoredProperties;
	
	protected boolean variantAsName = true;
	protected boolean registerVariantModels = true;
	
	protected Function<IMetadata<?>, String> variantNameFunction = null;
	protected ObjectFunction<? super B, ? super I> afterConstructed;
	protected ObjectFunction<? super B, ? super I> afterRegistered;
	
	protected CreativeTabs tab = null;
	
	@SuppressWarnings("unchecked")
	public ObjectType(String name, String unlocalizedName, Class<B> blockClass, Class<I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
	{
		this.name = name;
		this.resourceName = name;
		this.unlocalizedName = unlocalizedName;
		this.excludeVariants = ImmutableSet.copyOf(variantExclusions);
		
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
	
	public ObjectType(String name, Class<B> blockClass, Class<I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
	{
		this(name, name, blockClass, itemClass, variantExclusions);
	}
	
	public ObjectType(String name, String unlocalizedName, Class<B> blockClass, Class<I> itemClass, IMetadata<?>... variantExclusions)
	{
		this(name, unlocalizedName, blockClass, itemClass, Arrays.asList(variantExclusions));
	}
	
	public ObjectType(String name, Class<B> blockClass, Class<I> itemClass, IMetadata<?>... variantExclusions)
	{
		this(name, name, blockClass, itemClass, variantExclusions);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	public ObjectType<B, I> setResourceName(String name)
	{
		resourceName = name;
		
		return this;
	}
	
	public String getResourceName()
	{
		return resourceName;
	}
	
	public ObjectType<B, I> setUnlocalizedName(String unlocName)
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
	
	public ObjectType<B, I> setTypeNamePosition(VariantsOfTypesCombo.TypeNamePosition typeNamePosition)
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
	
	public ObjectType<B, I> setExcludedVariants(Collection<? extends IMetadata<?>> list)
	{
		excludeVariants = ImmutableSet.copyOf(list);
		
		return this;
	}
	
	public ObjectType<B, I> setValidVariants(Collection<? extends IMetadata<?>> list)
	{
		onlyVariants = ImmutableSet.copyOf(list);
		
		return this;
	}

	public <V extends IMetadata<V>> List<V> getValidVariants(List<V> list)
	{
		list.removeAll(excludeVariants);
		
		if (onlyVariants != null)
		{
			list.retainAll(onlyVariants);
		}
		
		return list;
	}
	
	public boolean getUseSeparateVariantJsons()
	{
		return separateVariantJsons;
	}
	
	public ObjectType<B, I> setUseSeparateVariantJsons(boolean value)
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
	public ObjectType<B, I> setUseVariantAsRegistryName(boolean value)
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
	public ObjectType<B, I> setShouldRegisterVariantModels(boolean value)
	{
		registerVariantModels = value;
		return this;
	}
	
	public Object[] getBlockArguments()
	{
		return blockArgs;
	}
	
	public ObjectType<B, I> setBlockArguments(Object... args)
	{
		this.blockArgs = args;
		
		return this;
	}

	public Object[] getItemArguments()
	{
		return itemArgs;
	}
	
	public ObjectType<B, I> setItemArguments(Object... args)
	{
		this.itemArgs = args;
		
		return this;
	}
	
	public ObjectType<B, I> setCreativeTab(CreativeTabs tab)
	{
		this.tab = tab;
		
		return this;
	}
	
	public ObjectType<B, I> setConstructedFunction(ObjectFunction<? super B, ? super I> function)
	{
		afterConstructed = function;
		return this;
	}
	
	public <V extends IMetadata<V>> void afterConstructed(Block block, Item item, List<V> variants)
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
	
	public ObjectType<B, I> setRegisteredFunction(ObjectFunction<? super B, ? super I> function)
	{
		afterRegistered = function;
		return this;
	}
	
	public final void afterRegistered(Block block, Item item)
	{
		if (afterRegistered != null)
			afterRegistered.apply(ReflectionUtils.nullSafeCast(blockClass, block), ReflectionUtils.nullSafeCast(itemClass, item));
	}
	
	public ObjectType<B, I> setIgnoredProperties(IProperty<?>... properties)
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
	
	public ObjectType<B, I> setVariantNameFunction(Function<IMetadata<?>, String> function)
	{
		variantNameFunction = function;
		return this;
	}
	
	public final String getVariantName(IMetadata<?> variant)
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
	
	// LOTS of create, createBlock and createItem methods for convenience.
	/**
	 * Full parameters.
	 */
	public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, String unlocalizedName, Class<B> blockClass, Class<I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
	{
		return new ObjectType<B, I>(name, unlocalizedName, blockClass, itemClass, variantExclusions);
	}
	
	/**
	 * Full parameters, default ItemBlock.
	 */
	public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, String unlocalizedName, Class<B> blockClass, Collection<? extends IMetadata<V>> variantExclusions)
	{
		return create(name, unlocalizedName, blockClass, ReflectionUtils.convertClass(ItemBlockMulti.class), variantExclusions);
	}
	
	/**
	 * Full parameters, no block.
	 */
	public static <I extends Item> ObjectType<Block, I> createItem(String name, String unlocalizedName, Class<I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
	{
		return create(name, unlocalizedName, null, itemClass, variantExclusions);
	}
	
	/**
	 * Full parameters, default item.
	 */
	public static <V extends IMetadata<V>> ObjectType<Block, ItemMulti<V>> createItem(String name, String unlocalizedName, Collection<? extends IMetadata<V>> variantExclusions)
	{
		return create(name, unlocalizedName, null, null, variantExclusions);
	}
	
	
	/**
	 * Same name for registry and unlocalized, all other parameters.
	 */
	public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, Class<B> blockClass, Class<I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
	{
		return new ObjectType<B, I>(name, blockClass, itemClass, variantExclusions);
	}
	
	/**
	 * Same name for registry and unlocalized, default ItemBlock.
	 */
	public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, Class<B> blockClass, Collection<? extends IMetadata<V>> variantExclusions)
	{
		return create(name, blockClass, ReflectionUtils.convertClass(ItemBlockMulti.class), variantExclusions);
	}
	
	/**
	 * Same name for registry and unlocalized, no block.
	 */
	public static <I extends Item> ObjectType<Block, I> createItem(String name, Class<I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
	{
		return create(name, null, itemClass, variantExclusions);
	}
	
	/**
	 * Same name for registry and unlocalized, default item.
	 */
	public static <V extends IMetadata<V>> ObjectType<Block, ItemMulti<V>> createItem(String name, Collection<? extends V> variantExclusions)
	{
		return create(name, null, null, variantExclusions);
	}
	
	
	/**
	 * Varargs exclusion.
	 */
	public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, String unlocalizedName, Class<B> blockClass, Class<I> itemClass, IMetadata<?>... variantExclusions)
	{
		return new ObjectType<B, I>(name, unlocalizedName, blockClass, itemClass, variantExclusions);
	}
	
	/**
	 * Default ItemBlock, varargs exclusion.
	 */
	@SafeVarargs
	public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, String unlocalizedName, Class<B> blockClass, V... variantExclusions)
	{
		return create(name, unlocalizedName, blockClass, ReflectionUtils.convertClass(ItemBlockMulti.class), variantExclusions);
	}
	
	/**
	 * No block, varargs exclusion.
	 */
	public static <I extends Item> ObjectType<Block, I> createItem(String name, String unlocalizedName, Class<I> itemClass, IMetadata<?>... variantExclusions)
	{
		return new ObjectType<Block, I>(name, unlocalizedName, null, itemClass, variantExclusions);
	}
	
	/**
	 * Default item, varargs exclusion.
	 */
	@SafeVarargs
	public static <V extends IMetadata<V>> ObjectType<Block, ItemMulti<V>> createItem(String name, String unlocalizedName, V... variantExclusions)
	{
		return create(name, unlocalizedName, null, null, variantExclusions);
	}
	
	
	/**
	 * Same name for registry and unlocalized, varargs exclusion.
	 */
	public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, Class<B> blockClass, Class<I> itemClass, IMetadata<?>... variantExclusions)
	{
		return new ObjectType<B, I>(name, name, blockClass, itemClass, variantExclusions);
	}
	
	/**
	 * Default ItemBlock, same name for registry and unlocalized, with varargs exclusion.
	 */
	@SafeVarargs
	public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, Class<B> blockClass, V... variantExclusions)
	{
		return create(name, blockClass, ReflectionUtils.convertClass(ItemBlockMulti.class), variantExclusions);
	}
	
	/**
	 * No block, same name for registry and unlocalized, with varargs exclusion.
	 */
	public static <I extends Item> ObjectType<Block, I> createItem(String name, Class<I> itemClass, IMetadata<?>... variantExclusions)
	{
		return new ObjectType<Block, I>(name, name, null, itemClass, variantExclusions);
	}
	
	/**
	 * Default item, same name for registry and unlocalized, with varargs exclusion.
	 */
	@SafeVarargs
	public static <V extends IMetadata<V>> ObjectType<Block, ItemMulti<V>> createItem(String name, V... variantExclusions)
	{
		return create(name, null, null, variantExclusions);
	}
	
	
	/**
	 * No exclusion.
	 */
	public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, String unlocalizedName, Class<B> blockClass, Class<I> itemClass)
	{
		return new ObjectType<B, I>(name, unlocalizedName, blockClass, itemClass, Collections.emptySet());
	}
	
	/**
	 * Default ItemBlock, no exclusion.
	 */
	public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, String unlocalizedName, Class<B> blockClass)
	{
		return create(name, unlocalizedName, blockClass, ReflectionUtils.convertClass(ItemBlockMulti.class), Collections.emptySet());
	}
	
	/**
	 * No block, no exclusion.
	 */
	public static <I extends Item> ObjectType<Block, I> createItem(String name, String unlocalizedName, Class<I> itemClass)
	{
		return new ObjectType<Block, I>(name, unlocalizedName, null, itemClass, Collections.emptySet());
	}
	
	/**
	 * Default item, no exclusion.
	 */
	public static <V extends IMetadata<V>> ObjectType<Block, ItemMulti<V>> createItem(String name, String unlocalizedName)
	{
		return create(name, unlocalizedName, null, null, Collections.emptySet());
	}
	
	
	/**
	 * Same name for registry and unlocalized, no exclusion.
	 */
	public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, Class<B> blockClass, Class<I> itemClass)
	{
		return new ObjectType<B, I>(name, name, blockClass, itemClass, Collections.emptySet());
	}
	
	/**
	 * Default ItemBlock, same name for registry and unlocalized, with no exclusion.
	 */
	public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, Class<B> blockClass)
	{
		return create(name, blockClass, ReflectionUtils.convertClass(ItemBlockMulti.class), Collections.emptySet());
	}
	
	/**
	 * No block, same name for registry and unlocalized, with no exclusion.
	 */
	public static <I extends Item> ObjectType<Block, I> createItem(String name, Class<I> itemClass)
	{
		return new ObjectType<Block, I>(name, name, null, itemClass, Collections.emptySet());
	}
	
	/**
	 * Default item, same name for registry and unlocalized, with no exclusion.
	 */
	public static <V extends IMetadata<V>> ObjectType<Block, ItemMulti<V>> createItem(String name)
	{
		return create(name, null, null, Collections.emptySet());
	}
}