package genesis.combo;

import genesis.client.GenesisClient;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.IMetadata;
import genesis.combo.variant.IVariant;
import genesis.common.*;
import genesis.item.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.*;

import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

/**
 * Used to create Blocks/Items with variants of ObjectTypes.
 * I.E. tree blocks have Blocks/Items {LOG, LEAVES, BILLET, FENCE} and variants {ARCHAEOPTERIS, SIGILLARIA, LEPIDODENDRON, etc.}.
 * 
 * @author Zaggy1024
 */
public class VariantsOfTypesCombo<V extends IMetadata<V>>
{
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.FIELD})
	public @interface BlockProperties {
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ItemVariantCount
	{
		int value();
	}
	
	public enum TypeNamePosition
	{
		PREFIX, POSTFIX, NONE;
	}
	
	/**
	 * Represents a type of item and/or block for a combo. Contains the data that determines
	 * what/how the items and blocks are constructed and registered.
	 *
	 * @param <B> For the type that should be returned when getting this type's Block.
	 * @param <I> For the type that should be returned when getting this type's Item.
	 */
	public static class ObjectType<B extends Block, I extends Item> implements IVariant
	{
		private final String name;
		protected String resourceName;
		protected String unlocalizedName;
		protected TypeNamePosition typeNamePosition = TypeNamePosition.PREFIX;
		
		private final Class<? extends B> blockClass;
		private Object[] blockArgs = {};
		private final Class<? extends I> itemClass;
		private Object[] itemArgs = {};
		
		protected Set<IMetadata<?>> excludeVariants;
		protected Set<IMetadata<?>> onlyVariants;
		protected boolean separateVariantJsons = true;
		protected IProperty<?>[] stateMapIgnoredProperties;
		
		protected boolean variantAsName = true;
		protected boolean registerVariantModels = true;
		
		protected CreativeTabs tab = null;
		
		@SuppressWarnings("unchecked")
		public ObjectType(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
		{
			this.name = name;
			this.resourceName = name;
			this.unlocalizedName = unlocalizedName;
			this.excludeVariants = ImmutableSet.copyOf(variantExclusions);
			
			if (itemClass == null)
			{
				if (blockClass != null)
				{
					itemClass = (Class<? extends I>) ItemBlockMulti.class;
				}
				else
				{
					itemClass = (Class<? extends I>) ItemMulti.class;
				}
			}
			
			this.blockClass = blockClass;
			this.itemClass = itemClass;
		}
		
		public ObjectType(String name, Class<? extends B> blockClass, Class<? extends I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
		{
			this(name, name, blockClass, itemClass, variantExclusions);
		}
		
		public ObjectType(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass, IMetadata<?>... variantExclusions)
		{
			this(name, unlocalizedName, blockClass, itemClass, Arrays.asList(variantExclusions));
		}
		
		public ObjectType(String name, Class<? extends B> blockClass, Class<? extends I> itemClass, IMetadata<?>... variantExclusions)
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
		
		public TypeNamePosition getTypeNamePosition()
		{
			return typeNamePosition;
		}
		
		public ObjectType<B, I> setTypeNamePosition(TypeNamePosition typeNamePosition)
		{
			this.typeNamePosition = typeNamePosition;
			
			return this;
		}
		
		public Class<? extends B> getBlockClass()
		{
			return blockClass;
		}
		
		public Class<? extends I> getItemClass()
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
		
		public <V extends IMetadata<V>> void afterConstructed(B block, I item, List<V> variants)
		{
			if (tab != null)
			{
				if (block != null)
				{
					block.setCreativeTab(tab);
				}
				
				if (item != null)
				{
					item.setCreativeTab(tab);
				}
			}
		}
		
		public <V extends IMetadata<V>> void afterConstructedPass(Block block, Item item, List<V> variants)
		{
			afterConstructed(ReflectionUtils.safeCast(blockClass, block), ReflectionUtils.safeCast(itemClass, item), variants);
		}
		
		protected void afterRegistered(B block, I item)
		{
		}
		
		public final void afterRegisteredPass(Block block, Item item)
		{
			afterRegistered(ReflectionUtils.safeCast(blockClass, block), ReflectionUtils.safeCast(itemClass, item));
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
		
		public String getVariantName(IMetadata<?> variant)
		{
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
		public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
		{
			return new ObjectType<B, I>(name, unlocalizedName, blockClass, itemClass, variantExclusions);
		}
		
		/**
		 * Full parameters, default ItemBlock.
		 */
		public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, String unlocalizedName, Class<? extends B> blockClass, Collection<? extends IMetadata<V>> variantExclusions)
		{
			return create(name, unlocalizedName, blockClass, ReflectionUtils.<ItemBlockMulti<V>>convertClass(ItemBlockMulti.class), variantExclusions);
		}
		
		/**
		 * Full parameters, no block.
		 */
		public static <I extends Item> ObjectType<Block, I> createItem(String name, String unlocalizedName, Class<? extends I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
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
		public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, Class<? extends B> blockClass, Class<? extends I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
		{
			return new ObjectType<B, I>(name, blockClass, itemClass, variantExclusions);
		}
		
		/**
		 * Same name for registry and unlocalized, default ItemBlock.
		 */
		public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, Class<? extends B> blockClass, Collection<? extends IMetadata<V>> variantExclusions)
		{
			return create(name, blockClass, ReflectionUtils.<ItemBlockMulti<V>>convertClass(ItemBlockMulti.class), variantExclusions);
		}
		
		/**
		 * Same name for registry and unlocalized, no block.
		 */
		public static <I extends Item> ObjectType<Block, I> createItem(String name, Class<? extends I> itemClass, Collection<? extends IMetadata<?>> variantExclusions)
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
		public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass, IMetadata<?>... variantExclusions)
		{
			return new ObjectType<B, I>(name, unlocalizedName, blockClass, itemClass, variantExclusions);
		}
		
		/**
		 * Default ItemBlock, varargs exclusion.
		 */
		public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, String unlocalizedName, Class<? extends B> blockClass, V... variantExclusions)
		{
			return create(name, unlocalizedName, blockClass, ReflectionUtils.<ItemBlockMulti<V>>convertClass(ItemBlockMulti.class), variantExclusions);
		}
		
		/**
		 * No block, varargs exclusion.
		 */
		public static <I extends Item> ObjectType<Block, I> createItem(String name, String unlocalizedName, Class<? extends I> itemClass, IMetadata<?>... variantExclusions)
		{
			return new ObjectType<Block, I>(name, unlocalizedName, null, itemClass, variantExclusions);
		}
		
		/**
		 * Default item, varargs exclusion.
		 */
		public static <V extends IMetadata<V>> ObjectType<Block, ItemMulti<V>> createItem(String name, String unlocalizedName, V... variantExclusions)
		{
			return create(name, unlocalizedName, null, null, variantExclusions);
		}
		
		
		/**
		 * Same name for registry and unlocalized, varargs exclusion.
		 */
		public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, Class<? extends B> blockClass, Class<? extends I> itemClass, IMetadata<?>... variantExclusions)
		{
			return new ObjectType<B, I>(name, name, blockClass, itemClass, variantExclusions);
		}
		
		/**
		 * Default ItemBlock, same name for registry and unlocalized, with varargs exclusion.
		 */
		public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, Class<? extends B> blockClass, V... variantExclusions)
		{
			return create(name, blockClass, ReflectionUtils.<ItemBlockMulti<V>>convertClass(ItemBlockMulti.class), variantExclusions);
		}
		
		/**
		 * No block, same name for registry and unlocalized, with varargs exclusion.
		 */
		public static <I extends Item> ObjectType<Block, I> createItem(String name, Class<? extends I> itemClass, IMetadata<?>... variantExclusions)
		{
			return new ObjectType<Block, I>(name, name, null, itemClass, variantExclusions);
		}
		
		/**
		 * Default item, same name for registry and unlocalized, with varargs exclusion.
		 */
		public static <V extends IMetadata<V>> ObjectType<Block, ItemMulti<V>> createItem(String name, V... variantExclusions)
		{
			return create(name, null, null, variantExclusions);
		}
		
		
		/**
		 * No exclusion.
		 */
		public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass)
		{
			return new ObjectType<B, I>(name, unlocalizedName, blockClass, itemClass, Collections.<IMetadata<?>>emptySet());
		}
		
		/**
		 * Default ItemBlock, no exclusion.
		 */
		public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, String unlocalizedName, Class<? extends B> blockClass)
		{
			return create(name, unlocalizedName, blockClass, ReflectionUtils.<ItemBlockMulti<V>>convertClass(ItemBlockMulti.class), Collections.<IMetadata<?>>emptySet());
		}
		
		/**
		 * No block, no exclusion.
		 */
		public static <I extends Item> ObjectType<Block, I> createItem(String name, String unlocalizedName, Class<? extends I> itemClass)
		{
			return new ObjectType<Block, I>(name, unlocalizedName, null, itemClass, Collections.<IMetadata<?>>emptySet());
		}
		
		/**
		 * Default item, no exclusion.
		 */
		public static <V extends IMetadata<V>> ObjectType<Block, ItemMulti<V>> createItem(String name, String unlocalizedName)
		{
			return create(name, unlocalizedName, null, null, Collections.<IMetadata<?>>emptySet());
		}
		
		
		/**
		 * Same name for registry and unlocalized, no exclusion.
		 */
		public static <B extends Block, I extends Item> ObjectType<B, I> create(String name, Class<? extends B> blockClass, Class<? extends I> itemClass)
		{
			return new ObjectType<B, I>(name, name, blockClass, itemClass, Collections.<IMetadata<?>>emptySet());
		}
		
		/**
		 * Default ItemBlock, same name for registry and unlocalized, with no exclusion.
		 */
		public static <B extends Block, V extends IMetadata<V>> ObjectType<B, ItemBlockMulti<V>> createBlock(String name, Class<? extends B> blockClass)
		{
			return create(name, blockClass, ReflectionUtils.<ItemBlockMulti<V>>convertClass(ItemBlockMulti.class), Collections.<IMetadata<?>>emptySet());
		}
		
		/**
		 * No block, same name for registry and unlocalized, with no exclusion.
		 */
		public static <I extends Item> ObjectType<Block, I> createItem(String name, Class<? extends I> itemClass)
		{
			return new ObjectType<Block, I>(name, name, null, itemClass, Collections.<IMetadata<?>>emptySet());
		}
		
		/**
		 * Default item, same name for registry and unlocalized, with no exclusion.
		 */
		public static <V extends IMetadata<V>> ObjectType<Block, ItemMulti<V>> createItem(String name)
		{
			return create(name, null, null, Collections.<IMetadata<?>>emptySet());
		}
	}
	
	/**
	 * Data about a variant of a {@link ObjectType}.
	 */
	public class VariantData
	{
		public final ObjectType<?, ?> type;
		public final int subsetID;
		public final Item item;
		public final int itemMetadata;
		public final Block block;
		public final V variant;
		
		private VariantData(ObjectType<?, ?> type, int subsetID, Block block, Item item, V variant, int metadata)
		{
			this.type = type;
			this.subsetID = subsetID;
			this.item = item;
			this.itemMetadata = metadata;
			this.block = block;
			this.variant = variant;
		}
		
		public ItemStack getStack(int size)
		{
			if (item == null)
			{
				throw new IllegalArgumentException("Variant " + variant.getName() + " of ObjectType " + type.getName() + " does not include an Item instance.");
			}
			
			return new ItemStack(item, size, itemMetadata);
		}
		
		public ItemStack getStack()
		{
			return getStack(1);
		}
	}
	
	/**
	 * Data about a subset of an {@link ObjectType}.
	 */
	public class SubsetData
	{
		public final ObjectType<?, ?> type;
		public final int id;
		public final Item item;
		public final Block block;
		public final int maxSize;
		public final int size;
		public final BitMask itemVariantMask;
		public final ImmutableMap<Integer, V> variants;
		public final IProperty<V> variantProperty;
		
		public SubsetData(ObjectType<?, ?> type, int id, Block block, Item item,
				int maxSize, int size,
				BitMask itemVariantMask, ImmutableMap<Integer, V> variants,
				IProperty<V> variantProperty)
		{
			this.type = type;
			this.id = id;
			this.item = item;
			this.block = block;
			this.maxSize = maxSize;
			this.size = size;
			this.itemVariantMask = itemVariantMask;
			this.variants = variants;
			this.variantProperty = variantProperty;
		}
	}
	
	/**
	 * Map of Block/Item types to a map of variants to the block/item itself.
	 */
	protected final ImmutableTable<ObjectType<?, ?>, V, VariantData> variantDataTable;
	protected final ImmutableTable<ObjectType<?, ?>, Integer, SubsetData> subsetDataTable;
	protected final ImmutableMap<Block, SubsetData> blockMap;
	protected final ImmutableMap<Item, SubsetData> itemMap;
	public final ImmutableList<ObjectType<?, ?>> types;
	public final ImmutableList<V> variants;
	public final Class<V> variantClass;
	public final HashSet<ObjectType<?, ?>> registeredTypes = new HashSet<ObjectType<?, ?>>();
	protected String unlocalizedPrefix = "";
	
	/**
	 * Creates a {@link #VariantsOfTypesCombo} with each {@link Block}/{@link Item} represented by the list of {@link ObjectType},
	 * with each {@code ObjectType} having the provided variants.<br><br>
	 * 
	 * All {@code Block} classes that are constructed in this method MUST have a "public static IProperty<?>[] getProperties()" to allow us
	 * to determine how many variants can be stored in the block.<br><br>
	 * 
	 * The {@code Block}'s variant property must contain all the variants from the list provided to it by the combo.<br><br>
	 * 
	 * The {@code Block} and {@code Item} classes must also have a constructor with arguments
	 * {@code (List<IMetadata>, VariantsOfTypesCombo, ObjectType)}, as well as the arguments provided by the {@code ObjectType}.
	 * The {@code List} tells the object what variants it stores, the {@code VariantsOfTypesCombo}
	 * is the owner group of objects, and the {@code ObjectType} is the {@code ObjectType} the object stores.
	 * 
	 * @param types The list of {@link ObjectType} definitions of the {@code Block} and {@code Item} classes to store.
	 * @param variants The {@link IMetadata} representations of the variants to store for each Block/Item.
	 */
	@SuppressWarnings("unchecked")
	public VariantsOfTypesCombo(List<? extends ObjectType<?, ?>> types, Class<V> variantClass, List<? extends V> variants)
	{
		this.types = ImmutableList.copyOf(types);
		this.variants = ImmutableList.copyOf(variants);
		this.variantClass = variantClass;
		
		try
		{
			ImmutableTable.Builder<ObjectType<?, ?>, V, VariantData> variantTable = ImmutableTable.builder();
			ImmutableTable.Builder<ObjectType<?, ?>, Integer, SubsetData> subsetTable = ImmutableTable.builder();
			ImmutableMap.Builder<Block, SubsetData> blockMap = ImmutableMap.builder();
			ImmutableMap.Builder<Item, SubsetData> itemMap = ImmutableMap.builder();
			
			for (final ObjectType<?, ?> type : this.types)
			{
				Class<? extends Block> blockClass = type.getBlockClass();
				Class<? extends Item> itemClass = type.getItemClass();
				
				List<V> typeVariants = type.getValidVariants(new ArrayList<V>(this.variants));
				
				int maxSubsetSize = Short.MAX_VALUE - 1;	// ItemStack max damage value.
				
				if (itemClass.isAnnotationPresent(ItemVariantCount.class))
				{
					ItemVariantCount annot = itemClass.getAnnotation(ItemVariantCount.class);
					maxSubsetSize = Math.min(annot.value(), maxSubsetSize);
				}
				
				// If the block class isn't null, we must get the maximum number of variants it can store in its metadata.
				if (blockClass != null)
				{
					Object propsListObj = null;
					
					for (Field field : blockClass.getDeclaredFields())
					{
						if (field.isAnnotationPresent(BlockProperties.class) && (field.getModifiers() & Modifier.STATIC) == Modifier.STATIC && field.getType().isArray())
						{
							field.setAccessible(true);
							propsListObj = field.get(null);
						}
					}
					
					if (propsListObj == null)
					{
						for (Method method : blockClass.getDeclaredMethods())
						{
							if (method.isAnnotationPresent(BlockProperties.class) && (method.getModifiers() & Modifier.STATIC) == Modifier.STATIC && method.getReturnType().isArray())
							{
								method.setAccessible(true);
								propsListObj = method.invoke(null);
							}
						}
					}
					
					if (!(propsListObj instanceof IProperty[]))
					{
						throw new IllegalArgumentException("Failed to find properties necessary to store in metadata for block class " + blockClass.getSimpleName());
					}
					
					maxSubsetSize = Math.min(BlockStateToMetadata.getMetadataLeftAfter((IProperty[]) propsListObj), maxSubsetSize);
				}
				
				int typeVariantsCount = typeVariants.size();
				int subsets = (int) Math.ceil(typeVariantsCount / (float) maxSubsetSize);
				
				for (int subset = 0; subset < subsets; subset++)
				{
					int from = subset * maxSubsetSize;
					int to = Math.min(from + maxSubsetSize, typeVariantsCount);
					int subsetSize = to - from;
					ImmutableList<V> subVariants = ImmutableList.copyOf(typeVariants.subList(from, to));
					Object variantsArg;
					
					if (maxSubsetSize == 1)
					{
						variantsArg = subVariants.get(0);
					}
					else
					{
						variantsArg = subVariants;
					}
					
					Block block = null;
					Item item = null;
					Object[] itemArgs;
					
					if (blockClass != null)
					{
						// Get Block constructor and call it.
						final Object[] blockArgs = {this, type, variantsArg, variantClass};
						final Object[] args = ArrayUtils.addAll(blockArgs, type.getBlockArguments());
						
						block = ReflectionUtils.construct(blockClass, args);
						
						itemArgs = new Object[]{block, this, type, variantsArg, variantClass};
					}
					else
					{
						itemArgs = new Object[]{this, type, variantsArg, variantClass};
					}
					
					// Get Item constructor and call it.
					final Object[] args = ArrayUtils.addAll(itemArgs, type.getItemArguments());
					item = ReflectionUtils.construct(itemClass, args);
					
					type.afterConstructedPass(block, item, subVariants);
					
					BitMask mask;
					
					if (item instanceof IItemMetadataBitMask)
					{
						mask = ((IItemMetadataBitMask) item).getMetadataBitMask();
					}
					else
					{
						mask = BitMask.forValueCount(maxSubsetSize);
					}
					
					// Add the Block or Item to our object map with its metadata ID.
					int variantMetadata = 0;
					ImmutableMap.Builder<Integer, V> variantMap = ImmutableMap.builder();
					
					for (V variant : subVariants)
					{
						if (mask.decode(mask.encode(0, variantMetadata)) != variantMetadata)
						{
							throw new RuntimeException("Item metadata bitwise mask did not encode and decode metadata " + variantMetadata + " properly for type " + type + " subset number " + subset + ".");
						}
						
						variantTable.put(type, variant, new VariantData(type, subset, block, item, variant, mask.encode(0, variantMetadata)));
						variantMap.put(variantMetadata, variant);
						
						variantMetadata++;
					}
					
					IProperty<V> variantProperty = null;
					
					if (block != null)
					{
						for (IProperty<?> property : block.getBlockState().getProperties())
						{
							if (!property.getValueClass().isAssignableFrom(variantClass))
							{
								continue;
							}
							
							Collection<?> values = property.getAllowedValues();
							boolean equal = subVariants.size() == values.size();
							
							if (equal)
							{
								for (Object value : values)
								{
									if (!subVariants.contains(value))
									{
										equal = false;
										break;
									}
								}
							}
							
							if (equal)
							{
								if (variantProperty != null)
								{
									throw new RuntimeException("Multiple properties have subvariants for type " + type + " subset number " + subset + ".");
								}
								
								variantProperty = (IProperty<V>) property;
							}
						}
						
						if (variantProperty == null)
						{
							throw new RuntimeException("No variant property found for type " + type + " subset number " + subset + ".");
						}
					}
					
					SubsetData subsetData = new SubsetData(type, subset, block, item,
							maxSubsetSize, subsetSize,
							mask, variantMap.build(),
							variantProperty);
					subsetTable.put(type, subset, subsetData);
					
					if (block != null)
					{
						blockMap.put(block, subsetData);
					}
					
					if (item != null)
					{
						itemMap.put(item, subsetData);
					}
				}
			}
			
			this.variantDataTable = variantTable.build();
			this.subsetDataTable = subsetTable.build();
			this.blockMap = blockMap.build();
			this.itemMap = itemMap.build();
		}
		catch (Exception e)
		{
			throw new RuntimeException("An error occurred while constructing a " + VariantsOfTypesCombo.class.getSimpleName() + ".\n" +
					getIdentification(),
					e);
		}
	}
	
	public VariantsOfTypesCombo<V> setUnlocalizedPrefix(String prefix)
	{
		unlocalizedPrefix = prefix;
		return this;
	}
	
	public String getUnlocalizedPrefix()
	{
		return unlocalizedPrefix;
	}
	
	/**
	 * Registers all the variants of this {@link ObjectType}.
	 */
	public void registerVariants(final ObjectType<?, ?> type)
	{
		if (!registeredTypes.add(type))
		{
			return;
		}
		
		List<Integer> subsets = new ArrayList<Integer>(subsetDataTable.row(type).keySet());
		Collections.sort(subsets);
		
		for (int subsetID : subsets)
		{
			SubsetData subset = subsetDataTable.get(type, subsetID);
			final Block block = subset.block;
			final Item item = subset.item;
			
			String registryName;
			
			if (subset.maxSize == 1 && type.usesVariantAsRegistryName())
			{
				registryName = type.getVariantName(getVariant(item, 0));
			}
			else
			{
				String name = type.getName();
				registryName = name + (name.equals("") ? "" : "_") + subsetID;
			}
			
			String unlocName = getUnlocalizedPrefix() + type.getUnlocalizedName();
			
			if (block != null)
			{
				Genesis.proxy.registerBlockWithItem(block, registryName, item);
				block.setUnlocalizedName(unlocName);
				
				// Register resource locations for the block.
				Genesis.proxy.callSided(new SidedFunction()
				{
					@Override
					@SideOnly(Side.CLIENT)
					public void client(GenesisClient client)
					{
						FlexibleStateMap mapper = new FlexibleStateMap();
						
						if (type.getUseSeparateVariantJsons())
						{
							switch (type.getTypeNamePosition())
							{
							case PREFIX:
								mapper.setPrefix(type.getResourceName(), "_");
								break;
							case POSTFIX:
								mapper.setPostfix(type.getResourceName(), "_");
								break;
							default:
								break;
							}
							
							IProperty<V> variantProp = getVariantProperty(block);
							
							if (variantProp != null)
							{
								mapper.setNameProperty(variantProp);
							}
						}
						else
						{
							mapper.setPrefix(type.getResourceName(), "_");
						}
						
						type.customizeStateMap(mapper);
						
						if (block instanceof IModifyStateMap)
						{
							((IModifyStateMap) block).customizeStateMap(mapper);
						}
						
						client.registerModelStateMap(block, mapper);
					}
				});
				// End registering block resource locations.
			}
			else
			{
				Genesis.proxy.registerItem(item, registryName, false);
			}
			
			// Set item model locations.
			if (type.shouldRegisterVariantModels())
			{
				for (V variant : subset.variants.values())
				{
					VariantData data = getVariantData(type, variant);
					Genesis.proxy.registerModel(data.item, data.itemMetadata, type.getVariantName(variant));
				}
			}
			
			// Set unlocalized name.
			item.setUnlocalizedName(unlocName);
			
			type.afterRegisteredPass(block, item);
		}
	}
	
	/**
	 * Registers all variants of all {@link ObjectType}s associated with this combo.
	 */
	public void registerAll()
	{
		for (ObjectType<?, ?> type : types)
		{
			registerVariants(type);
		}
	}
	
	/**
	 * Gets the property named "variant" from a Block for use in registering a StateMap for the Block.
	 */
	public IProperty<V> getVariantProperty(Block block)
	{
		SubsetData subset = getSubsetData(block);
		return subset != null ? subset.variantProperty : null;
	}
	
	/**
	 * @return A String to help identifying which combo is causing an error.
	 */
	public String getIdentification()
	{
		return "This " + getClass().getSimpleName() + " contains " + ObjectType.class.getSimpleName() + "s " + Stringify.stringifyIterable(types) + " and variants " + Stringify.stringifyIterable(variants) + ".";
	}

	/**
	 * Gets the VariantEntry.Value containing the all the information about this variant and its Block and Item.
	 */
	public VariantData getVariantData(ObjectType<?, ?> type, V variant)
	{
		if (!variantDataTable.contains(type, variant))
		{
			throw new RuntimeException("Attempted to get a variant entry for type " + type + " and variant " + variant + " from a " + VariantsOfTypesCombo.class.getSimpleName() + " that does not contain that cell.\n" +
					getIdentification());
		}
		
		return variantDataTable.get(type, variant);
	}
	
	/**
	 * Returns the Block for this {@link ObjectType} and variant, casted to the ObjectType's block's generic type.
	 */
	public <B extends Block> B getBlock(ObjectType<B, ? extends Item> type, V variant)
	{
		return ReflectionUtils.safeCast(type.blockClass, getVariantData(type, variant).block);
	}
	
	/**
	 * Returns a list of all the constructed Blocks for the specified {@link ObjectType}.
	 */
	public <B extends Block> Collection<B> getBlocks(ObjectType<B, ? extends Item> type)
	{
		HashSet<B> out = new HashSet<B>();
		
		for (V variant : getValidVariants(type))
		{
			out.add(getBlock(type, variant));
		}
		
		return out;
	}
	
	/**
	 * Returns the Item for this {@link ObjectType} and variant, casted to the ObjectType's item generic type.
	 */
	public <I extends Item> I getItem(ObjectType<? extends Block, I> type, V variant)
	{
		return ReflectionUtils.safeCast(type.itemClass, getVariantData(type, variant).item);
	}
	
	/**
	 * Returns a list of all the constructed Items for the specified {@link ObjectType}.
	 */
	public <I extends Item> Collection<I> getItems(ObjectType<? extends Block, I> type)
	{
		HashSet<I> out = new HashSet<I>();
		
		for (V variant : getValidVariants(type))
		{
			out.add(getItem(type, variant));
		}
		
		return out;
	}
	
	/**
	 * Gets the subset key for this item, containing its ObjectType and ID.
	 */
	public SubsetData getSubsetData(Item item)
	{
		return itemMap.get(item);
	}
	
	/**
	 * Gets the subset key for this block, containing its ObjectType and ID.
	 */
	public SubsetData getSubsetData(Block block)
	{
		return blockMap.get(block);
	}
	
	/**
	 * Gets the {@link VariantData} for this item and metadata.
	 */
	public VariantData getVariantData(Item item, int meta)
	{
		SubsetData data = getSubsetData(item);
		
		if (data != null)
		{
			int index = data.itemVariantMask.decode(meta);
			
			if (data.variants.containsKey(index))
			{
				return getVariantData(data.type, data.variants.get(index));
			}
		}
		
		return null;
	}

	/**
	 * Gets the {@link VariantData} for this item stack.
	 */
	public VariantData getVariantData(ItemStack stack)
	{
		if (stack != null)
		{
			return getVariantData(stack.getItem(), stack.getMetadata());
		}
		
		return null;
	}
	
	/**
	 * Gets the variant for the specified Item and item metadata, in the specified {@link ObjectType}.
	 */
	public V getVariant(Item item, int meta)
	{
		VariantData tableKey = getVariantData(item, meta);
		
		if (tableKey != null)
		{
			return tableKey.variant;
		}
		
		return null;
	}
	
	/**
	 * Gets the variant for the specified Item and item metadata, in the specified {@link ObjectType}.
	 */
	public V getVariant(ItemStack stack)
	{
		return stack != null ? getVariant(stack.getItem(), stack.getMetadata()) : null;
	}
	
	/**
	 * Gets the variant for the specified Block and item metadata, in the specified {@link ObjectType}.
	 * This method assumes that this Block has a corresponding Item.
	 */
	public V getVariant(Block block, int meta)
	{
		return getVariant(Item.getItemFromBlock(block), meta);
	}
	
	/**
	 * @return The variant contained in this block state. Returns null if the block is not owned by this combo.
	 */
	public V getVariant(IBlockState state)
	{
		IProperty<V> prop = getVariantProperty(state.getBlock());
		return prop != null ? state.getValue(prop) : null;
	}
	
	/**
	 * Gets the variant data for the provided block state.
	 */
	public VariantData getVariantData(IBlockState state)
	{
		SubsetData subsetData = getSubsetData(state.getBlock());
		
		if (subsetData != null)
		{
			return getVariantData(subsetData.type, getVariant(state));
		}
		
		return null;
	}
	
	/**
	 * @return Whether the provided {@link ObjectType} contains the block state.
	 */
	public boolean containsBlockState(ObjectType<?, ?> type, IBlockState state)
	{
		SubsetData subset = getSubsetData(state.getBlock());
		return subset != null && subset.type == type;
	}
	
	/**
	 * @return Whether the states have the same variant.
	 */
	public boolean areSameVariant(IBlockState state1, IBlockState state2)
	{
		V variant1 = getVariant(state1);
		V variant2 = getVariant(state2);
		return variant1 != null && variant1 == variant2;
	}
	
	/**
	 * @return Whether the stack is of the specified {@link ObjectType}.
	 */
	public boolean isStackOf(ItemStack stack, ObjectType<?, ?> type)
	{
		if (stack == null)
			return false;
		
		SubsetData data = getSubsetData(stack.getItem());
		
		return data != null && data.type == type;
	}
	
	/**
	 * @return Whether the stack is of the specified {@link ObjectType} and variant.
	 */
	public boolean isStackOf(ItemStack stack, V variant, ObjectType<?, ?> type)
	{
		return isStackOf(stack, type) && getVariant(stack) == variant;
	}
	
	/**
	 * @return Whether the stack is of one of the specified {@link ObjectType}s.
	 */
	public boolean isStackOf(ItemStack stack, ObjectType<?, ?>... types)
	{
		if (stack == null)
			return false;
		
		SubsetData data = getSubsetData(stack.getItem());
		
		if (data != null)
			for (ObjectType<?, ?> type : types)
				if (type == data.type)
					return true;
		
		return false;
	}
	
	/**
	 * @return Whether the stack is of one of the specified {@link ObjectType}s and variant.
	 */
	public boolean isStackOf(ItemStack stack, V variant, ObjectType<?, ?>... types)
	{
		return isStackOf(stack, types) && getVariant(stack) == variant;
	}
	
	/**
	 * @return Whether the state is of the specified {@link ObjectType}.
	 */
	public boolean isStateOf(IBlockState state, ObjectType<?, ?> type)
	{
		SubsetData data = getSubsetData(state.getBlock());
		return data != null && data.type == type;
	}
	
	/**
	 * @return Whether the state is of  the specified {@link ObjectType} and variant.
	 */
	public boolean isStateOf(IBlockState state, V variant, ObjectType<?, ?> type)
	{
		return isStateOf(state, type) && getVariant(state) == variant;
	}
	
	/**
	 * @return Whether the state is of one of the specified {@link ObjectType}s.
	 */
	public boolean isStateOf(IBlockState state, ObjectType<?, ?>... types)
	{
		SubsetData data = getSubsetData(state.getBlock());
		
		if (data != null)
			for (ObjectType<?, ?> type : types)
				if (type == data.type)
					return true;
		
		return false;
	}
	
	/**
	 * @return Whether the state is of one of the specified {@link ObjectType}s and variant.
	 */
	public boolean isStateOf(IBlockState state, V variant, ObjectType<?, ?>... types)
	{
		return isStateOf(state, types) && getVariant(state) == variant;
	}
	
	/**
	 * Gets a stack of the specified Item in this combo with the specified stack size.
	 */
	public ItemStack getStack(ObjectType<?, ?> type, V variant, int stackSize)
	{
		return getVariantData(type, variant).getStack(stackSize);
	}
	
	/**
	 * Gets a stack of the specified ObjectType and variant in this combo.
	 */
	public ItemStack getStack(ObjectType<?, ?> type, V variant)
	{
		return getStack(type, variant, 1);
	}
	
	/**
	 * Gets a stack of the specified ObjectType and variant from a block state in this combo.
	 */
	public ItemStack getStack(ObjectType<?, ?> type, IBlockState state)
	{
		return getStack(type, getVariant(state), 1);
	}
	
	/**
	 * Gets a stack of the specified Item in this combo.
	 */
	public ItemStack getStack(ObjectType<?, ?> type)
	{
		return getStack(type, getValidVariants(type).get(0), 1);
	}
	
	/**
	 * Gets the metadata used to get the Item of this {@link ObjectType} and variant.
	 */
	public int getItemMetadata(ObjectType<?, ?> type, V variant)
	{
		VariantData entry = getVariantData(type, variant);
		return entry.itemMetadata;
	}
	
	/**
	 * Gets an IBlockState for the specified Block variant in this combo.
	 */
	public IBlockState getBlockState(ObjectType<?, ?> type, V variant)
	{
		VariantData entry = getVariantData(type, variant);
		Block block = entry.block;
		
		if (block != null)
		{
			return block.getDefaultState().withProperty(getVariantProperty(block), variant);
		}
		
		throw new IllegalArgumentException("Variant " + variant.getName() + " of " + ObjectType.class.getSimpleName() + " " + type.getName() + " does not include a Block instance." +
				getIdentification());
	}
	
	/**
	 * Gets a random IBlockState for the specified {@link ObjectType}.
	 */
	public IBlockState getRandomBlockState(ObjectType<?, ?> type, Random rand)
	{
		List<V> variants = getValidVariants(type);
		
		return getBlockState(type, variants.get(rand.nextInt(variants.size())));
	}
	
	/**
	 * Returns a new List containing the valid variants for this type of object.
	 * 
	 * @return {@literal List<IMetadata>} containing all the variants this object can be.
	 */
	public List<V> getValidVariants(ObjectType<?, ?> type)
	{
		return type.getValidVariants(new ArrayList<V>(variants));
	}
	
	/**
	 * Returns a new list containing the valid variants shared between <code>type</code> and <code>otherTypes</code>.
	 */
	public List<V> getSharedValidVariants(ObjectType<?, ?> type, ObjectType<?, ?>... otherTypes)
	{
		List<V> output = getValidVariants(type);
		
		for (ObjectType<?, ?> otherType : otherTypes)
		{
			output.retainAll(getValidVariants(otherType));
		}
		
		return output;
	}
	
	/**
	 * @return listToFill, after having added all sub-items for this {@link ObjectType} and list of variants.
	 */
	public List<ItemStack> fillSubItems(ObjectType<?, ?> objectType, List<V> variants, List<ItemStack> listToFill, Collection<V> exclude)
	{
		for (V variant : variants)
		{
			if (!exclude.contains(variant))
			{
				listToFill.add(getStack(objectType, variant));
			}
		}
		
		return listToFill;
	}
	
	/**
	 * @return listToFill, after having added all sub-items for this {@link ObjectType} and list of variants.
	 */
	@SafeVarargs
	public final List<ItemStack> fillSubItems(ObjectType<?, ?> objectType, List<V> variants, List<ItemStack> listToFill, V... exclude)
	{
		return fillSubItems(objectType, variants, listToFill, ImmutableSet.copyOf(exclude));
	}
	
	/**
	 * @return listToFill, after having added all sub-items for this {@link ObjectType} and list of variants.
	 */
	public List<ItemStack> fillSubItems(ObjectType<?, ?> objectType, List<V> variants, List<ItemStack> listToFill)
	{
		return fillSubItems(objectType, variants, listToFill, Collections.<V>emptySet());
	}
	
	/**
	 * @return All sub-items for the {@link ObjectType} with the variants contained in the list.
	 */
	public final List<ItemStack> getSubItems(ObjectType<?, ?> objectType, List<V> variants, Collection<V> exclude)
	{
		return fillSubItems(objectType, variants, new ArrayList<ItemStack>(), exclude);
	}
	
	/**
	 * @return All sub-items for the {@link ObjectType} with the variants contained in the list.
	 */
	@SafeVarargs
	public final List<ItemStack> getSubItems(ObjectType<?, ?> objectType, List<V> variants, V... exclude)
	{
		return getSubItems(objectType, variants, ImmutableSet.copyOf(exclude));
	}
	
	/**
	 * @return All sub-items for the {@link ObjectType} with the variants contained in the list.
	 */
	public final List<ItemStack> getSubItems(ObjectType<?, ?> objectType, List<V> variants)
	{
		return getSubItems(objectType, variants, Collections.<V>emptySet());
	}
	
	/**
	 * Gets all sub-items for the {@link ObjectType}.
	 */
	public final List<ItemStack> getSubItems(ObjectType<?, ?> objectType, Set<V> exclude)
	{
		return getSubItems(objectType, getValidVariants(objectType), exclude);
	}
	
	/**
	 * Gets all sub-items for the {@link ObjectType}.
	 */
	@SafeVarargs
	public final List<ItemStack> getSubItems(ObjectType<?, ?> objectType, V... exclude)
	{
		return getSubItems(objectType, ImmutableSet.copyOf(exclude));
	}
	
	/**
	 * Gets all sub-items for the {@link ObjectType}.
	 */
	public final List<ItemStack> getSubItems(ObjectType<?, ?> objectType)
	{
		return getSubItems(objectType, Collections.<V>emptySet());
	}
	
	/**
	 * @param stack The stack to get the name for.
	 * @param base The base string to add the variant's unlocalized name onto (i.e. "tile.genesis.material.pebble").
	 * This will usually be gotten through <code>super.getUnlocalizedName(stack)</code>.
	 * @return The unlocalized name for the stack.<br>
	 * If the variant can't be found, this method will return the invalid metadata unlocalized name.
	 */
	public String getUnlocalizedName(ItemStack stack, String base)
	{
		V variant = getVariant(stack);
		
		if (variant == null)
		{
			return Unlocalized.INVALID_METADATA;
		}
		
		String variantName = variant.getUnlocalizedName();
		
		if (!"".equals(variantName) && !base.substring(base.length() - 1).equals("."))
		{
			variantName = "." + variantName;
		}
		
		return base + variantName;
	}
	
	/**
	 * @return The internal Table containing the mappings of {@link ObjectType} and variant to {@link VariantData}
	 */
	public Table<ObjectType<?, ?>, V, VariantData> getVariants()
	{
		return variantDataTable;
	}
	
	/**
	 * @return The internal Table containing the mappings of {@link ObjectType} and subset ID to {@link SubsetData}
	 */
	public Table<ObjectType<?, ?>, Integer, SubsetData> getSubsets()
	{
		return subsetDataTable;
	}
}
