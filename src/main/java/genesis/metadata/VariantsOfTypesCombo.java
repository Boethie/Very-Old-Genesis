package genesis.metadata;

import genesis.client.GenesisClient;
import genesis.common.*;
import genesis.item.*;
import genesis.util.*;
import genesis.util.ReflectionHelper;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

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
public class VariantsOfTypesCombo
{
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.FIELD})
	public static @interface BlockProperties {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ItemVariantCount
	{
		public int value();
	}
	
	/**
	 * Contains the types of Blocks/Items contained in a BlocksAndItemsWithVariantsOfTypes.
	 * 
	 * @param <T> For the type that should be returned when getting this type's Block/Item.
	 */
	public static class ObjectType<T extends Object> implements IMetadata
	{
		public static enum ObjectNamePosition {
			PREFIX, POSTFIX, NONE;
		}
		
		protected String name;
		protected String unlocalizedName;
		protected Function<IMetadata, String> resourceNameFunction;
		protected ObjectNamePosition namePosition = ObjectNamePosition.POSTFIX;
		
		protected Class<? extends Block> blockClass;
		protected Object[] blockArgs = {};
		protected Class<? extends Item> itemClass;
		private Object[] itemArgs = {};
		
		protected List<IMetadata> variantExclusions;
		protected List<IMetadata> onlyVariants;
		protected boolean separateVariantJsons = true;
		protected IProperty[] stateMapIgnoredProperties;
		
		protected CreativeTabs tab = null;

		public ObjectType(String name, String unlocalizedName, Class<? extends Block> blockClass, Class<? extends Item> itemClass, List<IMetadata> variantExclusions)
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
					this.itemClass = ItemBlockMulti.class;
				}
				else
				{
					this.itemClass = ItemMulti.class;
				}
			}
		}
		
		public ObjectType(String name, Class<? extends Block> blockClass, Class<? extends Item> itemClass, List<IMetadata> variantExclusions)
		{
			this(name, name, blockClass, itemClass, variantExclusions);
		}
		
		public ObjectType(String name, String unlocalizedName, Class<? extends Block> blockClass, Class<? extends Item> itemClass, IMetadata... variantExclusions)
		{
			this(name, unlocalizedName, blockClass, itemClass, Arrays.asList(variantExclusions));
		}
		
		public ObjectType(String name, Class<? extends Block> blockClass, Class<? extends Item> itemClass, IMetadata... variantExclusions)
		{
			this(name, name, blockClass, itemClass, variantExclusions);
		}
		
		public String getName()
		{
			return name;
		}
		
		public ObjectType<T> setUnlocalizedName(String unlocName)
		{
			this.unlocalizedName = unlocName;
			
			return this;
		}
		
		public String getUnlocalizedName()
		{
			return unlocalizedName;
		}
		
		public ObjectType<T> setResourceNameFunction(Function<IMetadata, String> func)
		{
			this.resourceNameFunction = func;
			
			return this;
		}
		
		public Function<IMetadata, String> getResourceNameFunction()
		{
			return resourceNameFunction;
		}
		
		public ObjectNamePosition getNamePosition()
		{
			return namePosition;
		}
		
		public ObjectType<T> setNamePosition(ObjectNamePosition namePosition)
		{
			this.namePosition = namePosition;
			
			return this;
		}
		
		public Class<? extends Block> getBlockClass()
		{
			return blockClass;
		}
		
		public Class<? extends Item> getItemClass()
		{
			return itemClass;
		}
		
		public ObjectType<T> setValidVariants(List<IMetadata> list)
		{
			onlyVariants = list;
			
			return this;
		}

		public List<IMetadata> getValidVariants(List<IMetadata> list)
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
		
		public ObjectType<T> setUseSeparateVariantJsons(boolean use)
		{
			separateVariantJsons = use;
			
			return this;
		}
		
		public Object[] getBlockArguments()
		{
			return blockArgs;
		}
		
		public ObjectType<T> setBlockArguments(Object... args)
		{
			this.blockArgs = args;
			
			return this;
		}

		public Object[] getItemArguments()
		{
			return itemArgs;
		}
		
		public ObjectType<T> setItemArguments(Object... args)
		{
			this.itemArgs = args;
			
			return this;
		}
		
		public ObjectType<T> setCreativeTab(CreativeTabs tab)
		{
			this.tab = tab;
			
			return this;
		}
		
		public void afterConstructed(Block block, Item item, List<IMetadata> variants)
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
		
		public void afterRegistered(Block block, Item item)
		{
		}
		
		public ObjectType<T> setIgnoredProperties(IProperty... properties)
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
			
			Function<IMetadata, String> nameFunction = getResourceNameFunction();
			
			if (nameFunction != null)
			{
				resource = nameFunction.apply(variant);
			}
			
			return resource;
		}
	}
	
	protected static abstract class VariantEntry
	{
		public final Item item;
		public final int itemMetadata;
		
		protected static class Value extends VariantEntry
		{
			public final Block block;
			public final int id;
			public final Object object;
			
			public Value(Block block, Item item, int id, int metadata)
			{
				super(item, metadata);
				
				this.block = block;
				this.id = id;
				this.object = block != null ? block : item;
			}
		}
		
		public static class Key extends VariantEntry
		{
			public Key(Item item, int metadata)
			{
				super(item, metadata);
			}
		}
		
		public VariantEntry(Item item, int metadata)
		{
			this.item = item;
			this.itemMetadata = metadata;
		}
		
		public int hashCode()
		{
			return item.hashCode() ^ itemMetadata;
		}
		
		public boolean equals(Object obj)
		{
			if (obj instanceof VariantEntry)
			{
				VariantEntry other = (VariantEntry) obj;
				
				if (other.item == item && other.itemMetadata == itemMetadata)
				{
					return true;
				}
			}
			
			return false;
		}
	}
	
	/**
	 * Map of Block/Item types to a map of variants to the block/item itself.
	 */
	protected final HashBiTable<ObjectType, IMetadata, VariantEntry.Value> entryMap = new HashBiTable();
	public final List<ObjectType> types;
	public final List<IMetadata> variants;
	public final HashSet<ObjectType> registeredTypes = new HashSet();
	
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
	public VariantsOfTypesCombo(List<ObjectType> types, List<IMetadata> variants)
	{
		this.variants = variants;
		this.types = types;
		
		try
		{
			for (final ObjectType type : types)
			{
				Class<? extends Block> blockClass = type.getBlockClass();
				Class<? extends Item> itemClass = type.getItemClass();
				
				List<IMetadata> typeVariants = type.getValidVariants(new ArrayList<IMetadata>(variants));

				int maxVariants = Short.MAX_VALUE - 1;	// ItemStack max damage value.
				
				if (itemClass.isAnnotationPresent(ItemVariantCount.class))
				{
					ItemVariantCount annot = itemClass.getAnnotation(ItemVariantCount.class);
					maxVariants = Math.min(annot.value(), maxVariants);
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
						throw new IllegalArgumentException("Failed to find variant properties for block class " + blockClass.getSimpleName());
					}
					
					maxVariants = Math.min(BlockStateToMetadata.getMetadataLeftAfter((IProperty[]) propsListObj), maxVariants);
				}
				
				int subsets = (int) Math.ceil(typeVariants.size() / (float) maxVariants);
				
				for (int subset = 0; subset < subsets; subset++)
				{
					final List<IMetadata> subVariants = typeVariants.subList(subset * maxVariants, Math.min((subset + 1) * maxVariants, typeVariants.size()));
					final Object variantsArg;
					
					if (maxVariants == 1)
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
						
						block = ReflectionHelper.construct(blockClass, args);
						
						itemArgs = new Object[]{block, variantsArg, this, type};
					}
					else
					{
						itemArgs = new Object[]{variantsArg, this, type};
					}

					// Get Item constructor and call it.
					final Object[] args = ArrayUtils.addAll(itemArgs, type.getItemArguments());
					item = ReflectionHelper.construct(itemClass, args);
					
					type.afterConstructed(block, item, subVariants);
					
					// Add the Block or Item to our object map with its metadata ID.
					int variantMetadata = 0;
					
					for (IMetadata variant : subVariants)
					{
						entryMap.put(type, variant, new VariantEntry.Value(block, item, subset, variantMetadata));
						variantMetadata++;
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("An error occurred while constructing a " + VariantsOfTypesCombo.class.getSimpleName() + ".\n" +
					getIdentification(),
					e);
		}
	}
	
	public VariantsOfTypesCombo(ObjectType[] objectTypes, IMetadata[] variants)
	{
		this(Arrays.asList(objectTypes), Arrays.asList(variants));
	}
	
	/**
	 * Registers all the variants of this {@link #ObjectType}.
	 */
	public void registerVariants(final ObjectType type)
	{
		if (!registeredTypes.add(type))
		{
			return;
		}
		
		ArrayList<Integer> registeredIDs = new ArrayList<Integer>();
		List<IMetadata> variants = getValidVariants(type);
		
		for (IMetadata variant : variants)
		{
			VariantEntry.Value entry = getVariantEntry(type, variant);
			
			final Block block = entry.block;
			final Item item = entry.item;
			final int id = entry.id;
			final int metadata = entry.itemMetadata;
			
			if (!registeredIDs.contains(id))
			{
				String registryName = type.getName() + "_" + entry.id;
				
				if (block != null)
				{
					Genesis.proxy.registerBlockWithItem(block, registryName, item);
					block.setUnlocalizedName(type.getUnlocalizedName());
					
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
									flexStateMap.setPrefix(type.getName() + "_");
									break;
								case POSTFIX:
									flexStateMap.setPostfix("_" + type.getName());
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
								flexStateMap.setPrefix(type.getName());
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

				type.afterRegistered(block, item);
				
				registeredIDs.add(id);
			}
			
			if (item != null)
			{
				item.setUnlocalizedName(type.getUnlocalizedName());
				
				// Register item model location.
				Genesis.proxy.registerModel(item, metadata, type.getVariantName(variant));
			}
		}
	}
	
	/**
	 * Registers all variants of all {@link #ObjectType}s associated with this combo.
	 */
	public void registerAll()
	{
		for (ObjectType type : types)
		{
			registerVariants(type);
		}
	}
	
	/**
	 * Gets the property named "variant" from a Block for use in registering a StateMap for the Block.
	 */
	protected IProperty getVariantProperty(Block block)
	{
		IProperty prop = null;
		
		for (IProperty curProp : (List<IProperty>) block.getBlockState().getProperties())
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
	 * Gets the Pair containing the metadata for this variant and its container Block or Item.
	 * 
	 * @param type
	 * @param variant
	 * @return The Block/Item casted to the type provided by the generic type in "type".
	 */
	public VariantEntry.Value getVariantEntry(ObjectType type, IMetadata variant)
	{
		if (!entryMap.containsRow(type))
		{
			throw new RuntimeException("Attempted to get an object of type " + type + " from a " + VariantsOfTypesCombo.class.getSimpleName() + " that does not contain that type.\n" +
					getIdentification());
		}
		
		if (!entryMap.containsColumn(variant))
		{
			throw new RuntimeException("Attempted to get an object of variant " + variant + " from a BlocksAndItemsWithVariantsOfTypes that does not contain that type.\n" +
					getIdentification());
		}
		
		return entryMap.get(type, variant);
	}

	/**
	 * Gets the Block or Item for the type and variant.
	 * 
	 * @return The Block/Item casted to the type provided by the generic type in "type".
	 */
	public <T> T getObject(ObjectType<T> type, IMetadata variant)
	{
		return (T) getVariantEntry(type, variant).object;
	}
	
	/**
	 * Gets all the Blocks or Items that this {@link #ObjectType} uses.
	 */
	public <T> HashSet<T> getObjects(ObjectType<T> type)
	{
		HashSet<T> out = new HashSet();
		
		for (IMetadata variant : getValidVariants(type))
		{
			out.add(getObject(type, variant));
		}
		
		return out;
	}
	
	/**
	 * Gets an IBlockState for the specified Block variant in this combo.
	 */
	public IBlockState getBlockState(ObjectType type, IMetadata variant)
	{
		VariantEntry.Value entry = getVariantEntry(type, variant);
		Block block = entry.block;
		
		if (block != null)
		{
			return block.getDefaultState().withProperty(getVariantProperty(block), (Comparable) variant);
		}
		
		throw new IllegalArgumentException("Variant " + variant.getName() + " of " + ObjectType.class.getSimpleName() + " " + type.getName() + " does not include a Block instance.");
	}
	
	public BiTable.Key<ObjectType, IMetadata> getVariantKey(Item item, int meta)
	{
		VariantEntry.Key valueKey = new VariantEntry.Key(item, meta);
		return entryMap.getKey(valueKey);
	}
	
	/**
	 * Gets the variant for the specified Item and item metadata, in the specified {@link #ObjectType}.
	 */
	public IMetadata getVariant(Item item, int meta)
	{
		BiTable.Key<ObjectType, IMetadata> tableKey = getVariantKey(item, meta);
		
		if (tableKey != null)
		{
			return tableKey.getColumn();
		}
		
		return null;
	}
	
	/**
	 * Gets the variant for the specified Block and item metadata, in the specified {@link #ObjectType}.
	 */
	public IMetadata getVariant(Block block, int meta)
	{
		return getVariant(Item.getItemFromBlock(block), meta);
	}
	
	/**
	 * Gets a random IBlockState for the specified {@link #ObjectType}.
	 */
	public IBlockState getRandomBlockState(ObjectType type, Random rand)
	{
		List<IMetadata> variants = getValidVariants(type);
		
		return getBlockState(type, variants.get(rand.nextInt(variants.size())));
	}
	
	/**
	 * Gets a stack of the specified Item in this combo with the specified stack size.
	 */
	public ItemStack getStack(ObjectType type, IMetadata variant, int stackSize)
	{
		VariantEntry.Value entry = getVariantEntry(type, variant);
		
		Item item = entry.item;
		
		if (item != null)
		{
			ItemStack stack = new ItemStack(item, stackSize, entry.itemMetadata);
			
			return stack;
		}
		
		throw new IllegalArgumentException("Variant " + variant.getName() + " of ObjectType " + type.getName() + " does not include an Item instance.");
	}
	
	/**
	 * Gets a stack of the specified Item in this combo.
	 */
	public ItemStack getStack(ObjectType type, IMetadata variant)
	{
		return getStack(type, variant, 1);
	}
	
	/**
	 * Gets a stack of the specified Item in this combo.
	 */
	public ItemStack getStack(ObjectType type)
	{
		return getStack(type, getValidVariants(type).get(0), 1);
	}
	
	/**
	 * Gets the metadata used to get the Item of this {@link #ObjectType} and variant.
	 */
	public int getMetadata(ObjectType type, IMetadata variant)
	{
		VariantEntry entry = getVariantEntry(type, variant);
		
		return entry.itemMetadata;
	}
	
	/**
	 * Gets all the valid variants for this type of object.
	 * 
	 * @return {@literal List<IMetadata>} containing all the variants this object can be.
	 */
	public List<IMetadata> getValidVariants(ObjectType type)
	{
		return type.getValidVariants(new ArrayList(variants));
	}
	
	/**
	 * Fills the provided list with all the valid sub-items for this Block or Item.
	 * 
	 * @return {@literal List<ItemStack>} containing all sub-items for this Block or Item.
	 */
	public <T extends IMetadata> List<ItemStack> fillSubItems(ObjectType objectType, List<T> variants, List<ItemStack> listToFill, Set<T> exclude)
	{
		for (IMetadata variant : variants)
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
	public <T extends IMetadata> List<ItemStack> fillSubItems(ObjectType objectType, List<T> variants, List<ItemStack> listToFill, T... exclude)
	{
		return fillSubItems(objectType, variants, listToFill, Sets.newHashSet(exclude));
	}
	
	/**
	 * Wrapper for
	 * {@link #fillSubItems(ObjectType, List, List, T[]) fillSubItems(ObjectType objectType, List&lt;IMetadata&gt; variants, List&lt;ItemStack&gt; listToFill, IMetadata... exclude)}
	 * to create a new list.
	 */
	public List<ItemStack> getSubItems(ObjectType objectType, List<IMetadata> variants)
	{
		return fillSubItems(objectType, variants, new ArrayList<ItemStack>());
	}
	
	/**
	 * Gets all sub-items for the {@link ObjectType} {@code objectType}.
	 */
	public List<ItemStack> getSubItems(ObjectType objectType)
	{
		return getSubItems(objectType, getValidVariants(objectType));
	}
}
