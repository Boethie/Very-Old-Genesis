package genesis.metadata;

import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.item.ItemBlockMulti;
import genesis.item.ItemMulti;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;
import genesis.util.FlexibleStateMap;
import genesis.util.GenesisStateMap;
import genesis.util.Stringify;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMap.Builder;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

/**
 * Used to create Blocks/Items with variants of ObjectTypes.
 * I.E. tree blocks have Blocks/Items {LOG, LEAVES, BILLET, FENCE} and variants {ARCHAEOPTERIS, SIGILLARIA, LEPIDODENDRON, etc.}.
 * 
 * @author Zaggy1024
 */
public class VariantsOfTypesCombo
{
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
		protected LinkedHashMap<Object, Class> blockArgs = new LinkedHashMap();
		protected Class<? extends Item> itemClass;
		
		protected List<IMetadata> variantExclusions;
		protected boolean separateVariantJsons = true;
		
		protected CreativeTabs tab = null;
		
		public ObjectType(String name, String unlocalizedName, Class<? extends Block> blockClass, Class<? extends Item> itemClass, IMetadata... variantExclusions)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.blockClass = blockClass;
			this.itemClass = itemClass;
			this.variantExclusions = Arrays.asList(variantExclusions);
			
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

		public List<IMetadata> getValidVariants(List<IMetadata> list)
		{
			list.removeAll(variantExclusions);
			return list;
		}
		
		public IStateMapper getStateMapper(Block block)
		{
			return null;
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
		
		public LinkedHashMap<Object, Class> getBlockArguments()
		{
			return blockArgs;
		}
		
		public ObjectType<T> setBlockArguments(LinkedHashMap<Object, Class> args)
		{
			this.blockArgs = args;
			
			return this;
		}
		
		public ObjectType<T> setBlockArguments(Object... args)
		{
			LinkedHashMap<Object, Class> argsMap = new LinkedHashMap();
			
			for (Object object : args)
			{
				argsMap.put(object, object.getClass());
			}
			
			setBlockArguments(argsMap);
			
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

		public void customizeStateMap(FlexibleStateMap stateMap)
		{
		}
	}
	
	protected class VariantEntry {
		public Block block;
		public Item item;
		public int metadata;
		public int id;
		public Object object;
		
		public VariantEntry(Block block, Item item, int id, int metadata)
		{
			this.block = block;
			this.item = item;
			this.id = id;
			this.metadata = metadata;
			
			if (block != null)
			{
				object = block;
			}
			else
			{
				object = item;
			}
		}
	}
	
	/**
	 * Map of Block/Item types to a map of variants to the block/item itself.
	 */
	protected final HashMap<ObjectType, HashMap<IMetadata, VariantEntry>> map = new HashMap<ObjectType, HashMap<IMetadata, VariantEntry>>();
	public final List<ObjectType> types;
	public final List<IMetadata> variants;
	public final HashSet<ObjectType> registeredTypes = new HashSet();
	
	/**
	 * Creates a BlocksAndItemsWithVariantsOfTypes with Blocks/Items from the "types" list containing the variants in "variants".
	 * 
	 * All Block classes that are constructed in this method MUST have a "public static IProperty[] getProperties()" to allow us to
	 * determine how many variants can be stored in the block.
	 * 
	 * The Block's variant property must have a name of "variant" exactly.
	 * 
	 * The Block and Item classes must also have a constructor with arguments
	 * (List<IMetadata>, BlocksAndItemsWithVariantsOfTypes). The List tells the Block or Item what variants it
	 * stores, and the BlocksAndItems... is the owner group of objects.
	 * 
	 * @param types The types of Blocks/Items (Objects) to store.
	 * @param variants The variants to store for each Block/Item.
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
				HashMap<IMetadata, VariantEntry> variantMap = new HashMap<IMetadata, VariantEntry>();
				
				List<IMetadata> typeVariants = type.getValidVariants(new ArrayList<IMetadata>(variants));

				int maxVariants = Short.MAX_VALUE - 1;	// ItemStack max damage value.

				// If the block class isn't null, we must get the maximum number of variants it can store in its metadata.
				if (blockClass != null)
				{
					Object propsListObj = null;
					
					for (Field field : blockClass.getDeclaredFields())
					{
						if (field.isAnnotationPresent(Properties.class) && (field.getModifiers() & Modifier.STATIC) == Modifier.STATIC && field.getType().isArray())
						{
							field.setAccessible(true);
							propsListObj = field.get(null);
						}
					}
					
					if (propsListObj == null)
					{
						for (Method method : blockClass.getDeclaredMethods())
						{
							if (method.isAnnotationPresent(Properties.class) && (method.getModifiers() & Modifier.STATIC) == Modifier.STATIC && method.getReturnType().isArray())
							{
								method.setAccessible(true);
								propsListObj = method.invoke(null);
							}
						}
					}
					
					if (propsListObj == null)
					{
						throw new IllegalArgumentException("Failed to find variant properties for block class " + blockClass.getCanonicalName());
					}
					
					maxVariants = BlockStateToMetadata.getMetadataLeftAfter((IProperty[]) propsListObj);
				}
				
				int subsets = (int) Math.ceil(typeVariants.size() / (float) maxVariants);
				
				for (int subset = 0; subset < subsets; subset++)
				{
					final List<IMetadata> subVariants = typeVariants.subList(subset * maxVariants, Math.min((subset + 1) * maxVariants, typeVariants.size()));

					Block block = null;
					Item item = null;
					
					if (blockClass != null)
					{
						final LinkedHashMap<Object, Class> argMap = type.getBlockArguments();
						
						Class thisClass = getClass();
						Constructor<? extends Block> constructor = null;
						
						ArrayList argsList = new ArrayList();
						argsList.add(subVariants);
						argsList.add(this);
						argsList.addAll(argMap.keySet());
						Object[] args = argsList.toArray();
						
						while (true)
						{
							ArrayList<Class> listClasses = new ArrayList<Class>();
							listClasses.add(List.class);
							listClasses.add(thisClass);
							listClasses.addAll(argMap.values());
							Class[] argClasses = listClasses.toArray(new Class[listClasses.size()]);
							
							try
							{
								constructor = blockClass.getConstructor(argClasses);
								break;
							}
							catch (NoSuchMethodException e) { }
							
							if (thisClass == VariantsOfTypesCombo.class)
							{
								break;
							}
							
							thisClass = thisClass.getSuperclass();
						}
						
						if (constructor == null)
						{
							throw new RuntimeException("Could not find a valid constructor for arguments " + Stringify.stringify(args) + " in Block class " + blockClass.getSimpleName() + ".\n"
									+ getIdentification());
						}
						
						try
						{
							block = constructor.newInstance(args);
						}
						catch (IllegalArgumentException e)
						{
							Class[] argTypes = new Class[args.length];
							
							for (int i = 0; i < args.length; i++)
							{
								argTypes[i] = args[i].getClass();
							}
							
							throw new RuntimeException("IllegalArgumentException encountered while trying to construct a block.\n"
									+ "Arguments types expected: " + Stringify.stringify(constructor.getParameterTypes()) + "\n"
									+ "Argument types passed: " + Stringify.stringify(argTypes) + "\n",
									e);
						}
						
						// Construct item as necessary for registering as the item to accompany a block.
						Class blockSuperClass = blockClass;
						Constructor<Item> itemConstructor = null;
						
						while (blockSuperClass != Object.class)
						{
							try
							{
								itemConstructor = (Constructor<Item>) itemClass.getConstructor(blockSuperClass, List.class, VariantsOfTypesCombo.class);
								break;
							}
							catch (NoSuchMethodException e)
							{
								blockSuperClass = blockSuperClass.getSuperclass();
							}
						}
						
						item = itemConstructor.newInstance(block, subVariants, this);
					}
					else
					{
						// Construct and register the item alone.
						Constructor<Item> itemConstructor = (Constructor<Item>) itemClass.getConstructor(List.class, VariantsOfTypesCombo.class);
						item = itemConstructor.newInstance(subVariants, this);
					}
					
					type.afterConstructed(block, item, subVariants);
					
					// Add the Block or Item to our object map with its metadata ID.
					int variantMetadata = 0;
					
					for (IMetadata variant : subVariants)
					{
						variantMap.put(variant, new VariantEntry(block, item, subset, variantMetadata));
						variantMetadata++;
					}
				}
				
				map.put(type, variantMap);
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
	 * Registers all the variants of this ObjectType.
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
			VariantEntry entry = getVariantEntry(type, variant);
			
			final Block block = entry.block;
			final Item item = entry.item;
			final int id = entry.id;
			final int metadata = entry.metadata;
			
			if (!registeredIDs.contains(id))
			{
				String registryName = type.getName() + "_" + entry.id;
				
				if (block != null)
				{
					Genesis.proxy.registerBlockWithItem(block, registryName, item);
					block.setUnlocalizedName(type.getUnlocalizedName());
					
					// Register resource locations for the block.
					IStateMapper stateMap = type.getStateMapper(block);
					
					if (stateMap == null)
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
						
						stateMap = flexStateMap;
					}
					
					Genesis.proxy.registerModelStateMap(block, stateMap);
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
				String resource = variant.getName();
				
				switch (type.getNamePosition())
				{
				case PREFIX:
					resource = type.getName() + "_" + resource;
					break;
				case POSTFIX:
					resource += "_" + type.getName();
					break;
				default:
				}
				
				Function<IMetadata, String> nameFunction = type.getResourceNameFunction();
				
				if (nameFunction != null)
				{
					resource = nameFunction.apply(variant);
				}
				
				Genesis.proxy.registerModel(item, metadata, resource);
			}
		}
	}
	
	/**
	 * Registers all variants of all ObjectTypes associated with this combo.
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
		return "This " + getClass().getSimpleName() + " contains ObjectTypes " + Stringify.stringify(types) + " and variants " + Stringify.stringify(variants) + ".";
	}

	/**
	 * Gets the Pair containing the metadata for this variant and its container Block or Item.
	 * 
	 * @param type
	 * @param variant
	 * @return The Block/Item casted to the type provided by the generic type in "type".
	 */
	public VariantEntry getVariantEntry(ObjectType type, IMetadata variant)
	{
		HashMap<IMetadata, VariantEntry> variantMap = map.get(type);
		
		if (variantMap == null)
		{
			throw new RuntimeException("Attempted to get an object of type " + type + " from a " + VariantsOfTypesCombo.class.getSimpleName() + " that does not contain that type.\n" +
					getIdentification());
		}
		
		if (!variantMap.containsKey(variant))
		{
			throw new RuntimeException("Attempted to get an object of variant " + variant + " from a BlocksAndItemsWithVariantsOfTypes that does not contain that type.\n" +
					getIdentification());
		}
		
		return map.get(type).get(variant);
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
	 * Gets all the Blocks or Items that this ObjectType uses.
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
		VariantEntry entry = getVariantEntry(type, variant);
		Block block = entry.block;
		
		if (block != null)
		{
			return block.getDefaultState().withProperty(getVariantProperty(block), (Comparable) variant);
		}
		
		throw new IllegalArgumentException("Variant " + variant.getName() + " of ObjectType " + type.getName() + " does not include a Block instance.");
	}
	
	/**
	 * Gets a random IBlockState for the specified ObjectType.
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
		VariantEntry entry = getVariantEntry(type, variant);
		
		Item item = entry.item;
		
		if (item != null)
		{
			ItemStack stack = new ItemStack(item, stackSize, entry.metadata);
			
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
	 * Gets a stack of the specified Item in this combo.
	 * 
	 * @param obj The Block or Item containing the subitem of the specified variant.
	 */
	public ItemStack getStack(Object obj, IMetadata variant, int stackSize)
	{
		return getStack(getObjectType(obj), variant, 1);
	}

	/**
	 * Gets a stack of the specified Item in this combo.
	 * 
	 * @param obj The Block or Item containing the subitem of the specified variant.
	 */
	public ItemStack getStack(Object obj, IMetadata variant)
	{
		return getStack(getObjectType(obj), variant);
	}
	
	/**
	 * Gets the metadata used to get the Item of this ObjectType and variant.
	 */
	public int getMetadata(ObjectType type, IMetadata variant)
	{
		VariantEntry entry = getVariantEntry(type, variant);
		
		return entry.metadata;
	}
	
	/**
	 * Gets the metadata used to get the Item of this ObjectType and variant.
	 */
	public int getMetadata(Object obj, IMetadata variant)
	{
		return getMetadata(getObjectType(obj), variant);
	}
	
	/**
	 * Gets all the valid variants for this type of object.
	 * 
	 * @return List<IMetadata> containing all the variants this object can be.
	 */
	public List<IMetadata> getValidVariants(ObjectType type)
	{
		return type.getValidVariants(new ArrayList(variants));
	}
	
	/**
	 * @return Returns the ObjectType that the parameter obj was created for.
	 */
	public ObjectType getObjectType(Object obj)
	{
		Class objClass = obj.getClass();
		
		for (ObjectType type : types)
		{
			if (objClass == type.getBlockClass() || objClass == type.getItemClass())
			{
				return type;
			}
		}
		
		throw new RuntimeException("Could not find an ObjectType owner for " + Stringify.stringify(obj) + ".\n" +
				getIdentification());
	}
	
	/**
	 * Wrapper for getSubItems(Object, List<IMetadata>, List<ItemStack>) to create a new list.
	 */
	public List<ItemStack> getSubItems(Object obj, List<IMetadata> variants)
	{
		return fillSubItems(obj, variants, new ArrayList<ItemStack>());
	}
	
	/**
	 * Fills the provided list with all the valid sub-items for this Block or Item.
	 * 
	 * @return List<ItemStack> containing all sub-items for this Block or Item.
	 */
	public <T extends IMetadata> List<ItemStack> fillSubItems(Object obj, List<T> variants, List<ItemStack> listToFill, Set<T> exclude)
	{
		ObjectType objectType = getObjectType(obj);
		
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
	 * @return List<ItemStack> containing all sub-items for this Block or Item.
	 */
	public <T extends IMetadata> List<ItemStack> fillSubItems(Object obj, List<T> variants, List<ItemStack> listToFill, T... exclude)
	{
		return fillSubItems(obj, variants, listToFill, Sets.newHashSet(exclude));
	}
}
