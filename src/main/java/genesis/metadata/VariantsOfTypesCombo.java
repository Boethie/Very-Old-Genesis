package genesis.metadata;

import genesis.client.GenesisClient;
import genesis.common.*;
import genesis.item.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;
import genesis.metadata.VariantsOfTypesCombo.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.*;

import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

/**
 * Used to create Blocks/Items with variants of ObjectTypes.
 * I.E. tree blocks have Blocks/Items {LOG, LEAVES, BILLET, FENCE} and variants {ARCHAEOPTERIS, SIGILLARIA, LEPIDODENDRON, etc.}.
 * 
 * @author Zaggy1024
 */
@SuppressWarnings("rawtypes")
public class VariantsOfTypesCombo<O extends ObjectType, V extends IMetadata>
{
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.FIELD})
	public static @interface BlockProperties {
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface ItemVariantCount
	{
		public int value();
	}
	
	public static enum ObjectNamePosition {
		PREFIX, POSTFIX, NONE;
	}
	
	/**
	 * Represents a type of item and/or block for a combo. Contains the data that determines
	 * what/how the items and blocks are constructed and registered.
	 * 
	 * @param <T> For the type that should be returned when getting this type's Block/Item.
	 */
	public static class ObjectType<B extends Block, I extends Item> implements IMetadata
	{
		protected String name;
		protected String unlocalizedName;
		protected Function<String, String> resourceNameFunction;
		protected ObjectNamePosition namePosition = ObjectNamePosition.POSTFIX;
		
		protected Class<? extends B> blockClass;
		protected Object[] blockArgs = {};
		protected Class<? extends I> itemClass;
		private Object[] itemArgs = {};
		
		protected List<? extends IMetadata> variantExclusions;
		protected List<? extends IMetadata> onlyVariants;
		protected boolean separateVariantJsons = true;
		protected IProperty[] stateMapIgnoredProperties;
		
		protected boolean variantAsName = true;
		
		protected CreativeTabs tab = null;

		@SuppressWarnings("unchecked")
		public ObjectType(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass, List<? extends IMetadata> variantExclusions)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.blockClass = blockClass;
			this.itemClass = itemClass;
			this.variantExclusions = variantExclusions;
			
			if (this.itemClass == null)
			{
				if (this.blockClass != null)
				{
					this.itemClass = (Class<? extends I>) ItemBlockMulti.class;
				}
				else
				{
					this.itemClass = (Class<? extends I>) ItemMulti.class;
				}
			}
			
			//Type[] typeArgs = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
			//this.blockClass = (Class<B>) typeArgs[0];
			//this.itemClass = (Class<I>) typeArgs[1];
		}
		
		public ObjectType(String name, Class<? extends B> blockClass, Class<? extends I> itemClass, List<? extends IMetadata> variantExclusions)
		{
			this(name, name, blockClass, itemClass, variantExclusions);
		}
		
		public ObjectType(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass, IMetadata... variantExclusions)
		{
			this(name, unlocalizedName, blockClass, itemClass, Arrays.asList(variantExclusions));
		}
		
		public ObjectType(String name, Class<? extends B> blockClass, Class<? extends I> itemClass, IMetadata... variantExclusions)
		{
			this(name, name, blockClass, itemClass, variantExclusions);
		}
		
		public String getName()
		{
			return name;
		}
		
		public ObjectType<B, I> setUnlocalizedName(String unlocName)
		{
			this.unlocalizedName = unlocName;
			
			return this;
		}
		
		public String getUnlocalizedName()
		{
			return unlocalizedName;
		}
		
		public ObjectType<B, I> setResourceNameFunction(Function<String, String> func)
		{
			this.resourceNameFunction = func;
			
			return this;
		}
		
		public Function<String, String> getResourceNameFunction()
		{
			return resourceNameFunction;
		}
		
		public ObjectNamePosition getNamePosition()
		{
			return namePosition;
		}
		
		public ObjectType<B, I> setNamePosition(ObjectNamePosition namePosition)
		{
			this.namePosition = namePosition;
			
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
		
		public ObjectType<B, I> setValidVariants(List<? extends IMetadata> list)
		{
			onlyVariants = ImmutableList.copyOf(list);
			
			return this;
		}

		public <V extends IMetadata> List<V> getValidVariants(List<V> list)
		{
			list.removeAll(variantExclusions);
			
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
		
		public ObjectType<B, I> setUseSeparateVariantJsons(boolean use)
		{
			separateVariantJsons = use;
			
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
		public ObjectType<B, I> setUseVariantAsRegistryName(boolean use)
		{
			variantAsName = use;
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
		
		public void afterConstructed(B block, I item, List<? extends IMetadata> variants)
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
		
		public void afterRegistered(B block, I item)
		{
		}
		
		public ObjectType<B, I> setIgnoredProperties(IProperty... properties)
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

		public String getVariantName(IMetadata variant)
		{
			String resource = variant.getName();
			
			if ("".equals(resource))
			{
				resource += getName();
			}
			else
			{
				switch (getNamePosition())
				{
				case PREFIX:
					resource = getName() + "_" + resource;
					break;
				case POSTFIX:
					resource += "_" + getName();
					break;
				default:
				}
			}
			
			Function<String, String> nameFunction = getResourceNameFunction();
			
			if (nameFunction != null)
			{
				resource = nameFunction.apply(resource);
			}
			
			return resource;
		}
		
		public String toString()
		{
			return getName();
		}
	}
	
	/**
	 * Data about a variant of a {@link ObjectType}.
	 */
	public class VariantData
	{
		public final O type;
		public final int subsetID;
		public final Item item;
		public final int itemMetadata;
		public final Block block;
		public final V variant;
		
		private VariantData(O type, int subsetID, Block block, Item item, V variant, int metadata)
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
		public final O type;
		public final int id;
		public final Item item;
		public final Block block;
		public final int maxSize;
		public final int size;
		public final ImmutableMap<Integer, V> variants;
		
		public SubsetData(O type, int id, Block block, Item item, int maxSize, int size, ImmutableMap<Integer, V> variants)
		{
			this.type = type;
			this.id = id;
			this.item = item;
			this.block = block;
			this.maxSize = maxSize;
			this.size = size;
			this.variants = variants;
		}
	}
	
	/**
	 * Map of Block/Item types to a map of variants to the block/item itself.
	 */
	protected final ImmutableTable<O, V, VariantData> variantDataTable;
	protected final ImmutableTable<O, Integer, SubsetData> subsetDataTable;
	protected final ImmutableMap<Block, SubsetData> blockMap;
	protected final ImmutableMap<Item, SubsetData> itemMap;
	public final ImmutableList<O> types;
	public final ImmutableList<V> variants;
	public final HashSet<O> registeredTypes = new HashSet<O>();
	protected String unlocalizedPrefix = "";
	
	/**
	 * Creates a {@link #VariantsOfTypesCombo} with each {@link #Block}/{@link #Item} represented by the list of {@link #ObjectType},
	 * with each {@code ObjectType} having the provided variants.<br><br>
	 * 
	 * All {@code Block} classes that are constructed in this method MUST have a "public static IProperty[] getProperties()" to allow us
	 * to determine how many variants can be stored in the block.<br><br>
	 * 
	 * The {@code Block}'s variant property must have a name of "variant" exactly.<br><br>
	 * 
	 * The {@code Block} and {@code Item} classes must also have a constructor with arguments
	 * {@code (List<IMetadata>, VariantsOfTypesCombo, ObjectType)}, as well as the arguments provided by the {@code ObjectType}.
	 * The {@code List} tells the object what variants it stores, the {@code VariantsOfTypesCombo}
	 * is the owner group of objects, and the {@code ObjectType} is the {@code ObjectType} the object stores.
	 * 
	 * @param types The list of {@link #ObjectType} definitions of the {@code Block} and {@code Item} classes to store.
	 * @param variants The {@link #IMetadata} representations of the variants to store for each Block/Item.
	 */
	@SuppressWarnings("unchecked")
	public VariantsOfTypesCombo(List<O> typesIn, List<V> variantsIn)
	{
		this.types = ImmutableList.copyOf(typesIn);
		this.variants = ImmutableList.copyOf(variantsIn);
		
		try
		{
			ImmutableTable.Builder<O, V, VariantData> objectDataTable = ImmutableTable.builder();
			ImmutableTable.Builder<O, Integer, SubsetData> subsetDataTable = ImmutableTable.builder();
			ImmutableMap.Builder<Block, SubsetData> blockMap = ImmutableMap.builder();
			ImmutableMap.Builder<Item, SubsetData> itemMap = ImmutableMap.builder();
			
			for (final O type : types)
			{
				Class<? extends Block> blockClass = type.getBlockClass();
				Class<? extends Item> itemClass = type.getItemClass();
				
				List<V> typeVariants = type.getValidVariants(new ArrayList<V>(variants));
				
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
						final Object[] blockArgs = {variantsArg, this, type};
						final Object[] args = ArrayUtils.addAll(blockArgs, type.getBlockArguments());
						
						block = ReflectionUtils.construct(blockClass, args);
						
						itemArgs = new Object[]{block, variantsArg, this, type};
					}
					else
					{
						itemArgs = new Object[]{variantsArg, this, type};
					}
					
					// Get Item constructor and call it.
					final Object[] args = ArrayUtils.addAll(itemArgs, type.getItemArguments());
					item = ReflectionUtils.construct(itemClass, args);
					
					type.afterConstructed(block, item, subVariants);
					
					// Add the Block or Item to our object map with its metadata ID.
					int variantMetadata = 0;
					ImmutableMap.Builder<Integer, V> variantMap = ImmutableMap.builder();
					
					for (V variant : subVariants)
					{
						objectDataTable.put(type, variant, new VariantData(type, subset, block, item, variant, variantMetadata));
						variantMap.put(variantMetadata, variant);
						variantMetadata++;
					}
					
					SubsetData subsetData = new SubsetData(type, subset, block, item, maxSubsetSize, subsetSize, variantMap.build());
					subsetDataTable.put(type, subset, subsetData);
					
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
			
			this.variantDataTable = objectDataTable.build();
			this.subsetDataTable = subsetDataTable.build();
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
	
	public VariantsOfTypesCombo(O[] objectTypes, V[] variants)
	{
		this(Arrays.asList(objectTypes), Arrays.asList(variants));
	}
	
	public VariantsOfTypesCombo<O, V> setUnlocalizedPrefix(String prefix)
	{
		unlocalizedPrefix = prefix;
		return this;
	}
	
	public String getUnlocalizedPrefix()
	{
		return unlocalizedPrefix;
	}
	
	/**
	 * Registers all the variants of this {@link #ObjectType}.
	 */
	@SuppressWarnings("unchecked")
	public void registerVariants(final O type)
	{
		if (!registeredTypes.add(type))
		{
			return;
		}
		
		Set<Integer> registeredSubsets = new HashSet<Integer>();
		List<V> variants = getValidVariants(type);
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
				registryName = type.getName() + "_" + subsetID;
			}
			
			if (block != null)
			{
				Genesis.proxy.registerBlockWithItem(block, registryName, item);
				block.setUnlocalizedName(getUnlocalizedPrefix() + type.getUnlocalizedName());
				
				// Register resource locations for the block.
				Genesis.proxy.callSided(new SidedFunction()
				{
					@Override
					@SideOnly(Side.CLIENT)
					public void client(GenesisClient client)
					{
						FlexibleStateMap flexStateMap = new FlexibleStateMap();
						
						if (type.getUseSeparateVariantJsons())
						{
							switch (type.getNamePosition())
							{
							case PREFIX:
								flexStateMap.setPrefix(type.getName(), "_");
								break;
							case POSTFIX:
								flexStateMap.setPostfix(type.getName(), "_");
								break;
							default:
								break;
							}
							
							IProperty variantProp = getVariantProperty(block);
							
							if (variantProp != null)
							{
								flexStateMap.setNameProperty(variantProp);
							}
						}
						else
						{
							flexStateMap.setPrefix(type.getName(), "_");
						}
						
						type.customizeStateMap(flexStateMap);
						
						if (block instanceof IModifyStateMap)
						{
							((IModifyStateMap) block).customizeStateMap(flexStateMap);
						}
						
						Function<String, String> nameFunction = type.getResourceNameFunction();
						
						if (nameFunction != null)
						{
							flexStateMap.setNameFunction(nameFunction);
						}
						
						client.registerModelStateMap(block, flexStateMap);
					}
				});
				// End registering block resource locations.
			}
			else
			{
				Genesis.proxy.registerItem(item, registryName);
			}
			
			// Set unlocalized names and item model locations.
			for (V variant : subset.variants.values())
			{
				VariantData data = getVariantData(type, variant);
				Genesis.proxy.registerModel(data.item, data.itemMetadata, type.getVariantName(variant));
			}
			
			item.setUnlocalizedName(type.getUnlocalizedName());
			
			type.afterRegistered(block, item);
		}
	}
	
	/**
	 * Registers all variants of all {@link #ObjectType}s associated with this combo.
	 */
	public void registerAll()
	{
		for (O type : types)
		{
			registerVariants(type);
		}
	}
	
	/**
	 * Gets the property named "variant" from a Block for use in registering a StateMap for the Block.
	 */
	@SuppressWarnings("unchecked")
	protected IProperty getVariantProperty(Block block)
	{
		IProperty prop = null;
		
		for (IProperty curProp : (Collection<IProperty>) block.getBlockState().getProperties())
		{
			if ("variant".equals(curProp.getName()))
			{
				prop = curProp;
				break;
			}
		}
		
		return prop;
	}
	
	/**
	 * @return A String to help identifying which combo is causing an error.
	 */
	public String getIdentification()
	{
		return "This " + getClass().getSimpleName() + " contains " + ObjectType.class.getSimpleName() + "s " + Stringify.stringify(types) + " and variants " + Stringify.stringify(variants) + ".";
	}

	/**
	 * Gets the VariantEntry.Value containing the all the information about this variant and its Block and Item.
	 */
	public VariantData getVariantData(O type, V variant)
	{
		if (!variantDataTable.contains(type, variant))
		{
			throw new RuntimeException("Attempted to get a variant entry for type " + type + " and variant " + variant + " from a " + VariantsOfTypesCombo.class.getSimpleName() + " that does not contain that cell.\n" +
					getIdentification());
		}
		
		return variantDataTable.get(type, variant);
	}
	
	/**
	 * Returns the Block for this {@link #ObjectType} and variant, casted to the ObjectType's block's generic type.
	 */
	@SuppressWarnings("unchecked")
	public <B extends Block> B getBlock(ObjectType<B, ? extends Item> type, V variant)
	{
		return (B) getVariantData((O) type, variant).block;
	}
	
	/**
	 * Returns a list of all the constructed Blocks for the specified {@link #ObjectType}.
	 */
	@SuppressWarnings("unchecked")
	public <B extends Block> Collection<B> getBlocks(ObjectType<B, ? extends Item> type)
	{
		HashSet<B> out = new HashSet<B>();
		
		for (V variant : getValidVariants((O) type))
		{
			out.add(getBlock(type, variant));
		}
		
		return out;
	}
	
	/**
	 * Returns the Item for this {@link #ObjectType} and variant, casted to the ObjectType's item generic type.
	 */
	@SuppressWarnings("unchecked")
	public <I extends Item> I getItem(ObjectType<? extends Block, I> type, V variant)
	{
		return (I) getVariantData((O) type, variant).item;
	}
	
	/**
	 * Returns a list of all the constructed Items for the specified {@link #ObjectType}.
	 */
	@SuppressWarnings("unchecked")
	public <I extends Item> Collection<I> getItems(ObjectType<? extends Block, I> type)
	{
		HashSet<I> out = new HashSet<I>();
		
		for (V variant : getValidVariants((O) type))
		{
			out.add(getItem(type, variant));
		}
		
		return out;
	}
	
	/**
	 * Gets an IBlockState for the specified Block variant in this combo.
	 */
	public IBlockState getBlockState(O type, V variant)
	{
		VariantData entry = getVariantData(type, variant);
		Block block = entry.block;
		
		if (block != null)
		{
			return block.getDefaultState().withProperty(getVariantProperty(block), (Comparable) variant);
		}
		
		throw new IllegalArgumentException("Variant " + variant.getName() + " of " + ObjectType.class.getSimpleName() + " " + type.getName() + " does not include a Block instance.");
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
	 * Gets the {@link #VariantData} for this item and metadata.
	 */
	public VariantData getVariantData(Item item, int meta)
	{
		SubsetData data = getSubsetData(item);
		return getVariantData(data.type, data.variants.get(meta));
	}

	/**
	 * Gets the {@link #VariantData} for this item stack.
	 */
	public VariantData getVariantData(ItemStack stack)
	{
		return getVariantData(stack.getItem(), stack.getMetadata());
	}
	
	/**
	 * Gets the variant for the specified Item and item metadata, in the specified {@link #ObjectType}.
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
	 * Gets the variant for the specified Item and item metadata, in the specified {@link #ObjectType}.
	 */
	public V getVariant(ItemStack stack)
	{
		return getVariant(stack.getItem(), stack.getMetadata());
	}
	
	/**
	 * Gets the variant for the specified Block and item metadata, in the specified {@link #ObjectType}.
	 * This method assumes that this Block has a corresponding Item.
	 */
	public V getVariant(Block block, int meta)
	{
		return getVariant(Item.getItemFromBlock(block), meta);
	}
	
	/**
	 * @return The variant contained in this block state. Returns null if the block is not owned by this combo.
	 */
	@SuppressWarnings("unchecked")
	public V getVariant(IBlockState state)
	{
		IProperty prop = getVariantProperty(state.getBlock());	// TODO 1.8.2+: Make getVariantProperty return IProperty<V>
		
		if (prop == null)
		{
			return null;
		}
		
		return (V) state.getValue(prop);
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
	 * @return Whether the states have the same variant.
	 */
	public boolean areSameVariant(IBlockState state1, IBlockState state2)
	{
		V variant1 = getVariant(state1);
		V variant2 = getVariant(state2);
		return variant1 != null && variant1 == variant2;
	}
	
	/**
	 * Gets a random IBlockState for the specified {@link #ObjectType}.
	 */
	public IBlockState getRandomBlockState(O type, Random rand)
	{
		List<V> variants = getValidVariants(type);
		
		return getBlockState(type, variants.get(rand.nextInt(variants.size())));
	}
	
	/**
	 * Gets a stack of the specified Item in this combo with the specified stack size.
	 */
	public ItemStack getStack(O type, V variant, int stackSize)
	{
		return getVariantData(type, variant).getStack(stackSize);
	}
	
	/**
	 * Gets a stack of the specified ObjectType and variant in this combo.
	 */
	public ItemStack getStack(O type, V variant)
	{
		return getStack(type, variant, 1);
	}
	
	/**
	 * Gets a stack of the specified ObjectType and variant from a block state in this combo.
	 */
	public ItemStack getStack(O type, IBlockState state)
	{
		return getStack(type, getVariant(state), 1);
	}
	
	/**
	 * Gets a stack of the specified Item in this combo.
	 */
	public ItemStack getStack(O type)
	{
		return getStack(type, getValidVariants(type).get(0), 1);
	}
	
	/**
	 * Gets the metadata used to get the Item of this {@link #ObjectType} and variant.
	 */
	public int getItemMetadata(O type, V variant)
	{
		VariantData entry = getVariantData(type, variant);
		return entry.itemMetadata;
	}
	
	/**
	 * Returns a new List containing the valid variants for this type of object.
	 * 
	 * @return {@literal List<IMetadata>} containing all the variants this object can be.
	 */
	@SuppressWarnings("unchecked")
	public List<V> getValidVariants(O type)
	{
		return type.getValidVariants(new ArrayList<V>(variants));
	}
	
	/**
	 * Returns a new list containing the valid variants shared between <code>type</code> and <code>otherTypes</code>.
	 */
	public List<V> getSharedValidVariants(O type, O... otherTypes)
	{
		List<V> output = getValidVariants(type);
		
		for (O otherType : otherTypes)
		{
			output.retainAll(getValidVariants(otherType));
		}
		
		return output;
	}
	
	/**
	 * Fills the provided list with all the valid sub-items for this Block or Item.
	 * 
	 * @return {@literal List<ItemStack>} containing all sub-items for this Block or Item.
	 */
	public List<ItemStack> fillSubItems(O objectType, List<V> variants, List<ItemStack> listToFill, Set<V> exclude)
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
	 * Fills the provided list with all the valid sub-items for this Block or Item.
	 * 
	 * @return {@literal List<ItemStack>} containing all sub-items for this Block or Item.
	 */
	public List<ItemStack> fillSubItems(O objectType, List<V> variants, List<ItemStack> listToFill, V... exclude)
	{
		return fillSubItems(objectType, variants, listToFill, Sets.newHashSet(exclude));
	}
	
	/**
	 * Wrapper for
	 * {@link #fillSubItems(ObjectType, List, List, T[]) fillSubItems(ObjectType objectType, List&lt;IMetadata&gt; variants, List&lt;ItemStack&gt; listToFill, IMetadata... exclude)}
	 * to create a new list.
	 */
	public List<ItemStack> getSubItems(O objectType, List<V> variants, V... exclude)
	{
		return fillSubItems(objectType, variants, new ArrayList<ItemStack>(), exclude);
	}
	
	/**
	 * Gets all sub-items for the {@link ObjectType} {@code objectType}.
	 */
	public List<ItemStack> getSubItems(O objectType, V... exclude)
	{
		return getSubItems(objectType, getValidVariants(objectType), exclude);
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
		int metadata = stack.getMetadata();
		V variant = getVariant(stack.getItem(), metadata);
		
		if (variant == null)
		{
			return Unlocalized.INVALID_METADATA;
		}
		
		String variantName = variant.getUnlocalizedName();
		
		if (!"".equals(variantName))
		{
			variantName = "." + variantName;
		}
		
		return base + variantName;
	}
	
	/**
	 * @return The internal Table containing the mappings of {@link ObjectType} and variant to {@link VariantData}
	 */
	public Table<O, V, VariantData> getVariants()
	{
		return variantDataTable;
	}
	
	/**
	 * @return The internal Table containing the mappings of {@link ObjectType} and subset ID to {@link SubsetData}
	 */
	public Table<O, Integer, SubsetData> getSubsets()
	{
		return subsetDataTable;
	}
}
