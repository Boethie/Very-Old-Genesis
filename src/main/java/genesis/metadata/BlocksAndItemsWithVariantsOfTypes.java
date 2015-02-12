package genesis.metadata;

import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;
import genesis.util.GenesisStateMap;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

/**
 * Used to create a matrix of Blocks/Items and their variants.
 * I.E. tree blocks have Blocks/Items {LOG, LEAVES, BILLET, FENCE} and variants {ARCHAEOPTERIS, SIGILLARIA, LEPIDODENDRON, etc.}.
 * 
 * @author Zaggy1024
 */
public abstract class BlocksAndItemsWithVariantsOfTypes
{
	/**
	 * Contains the types of Blocks/Items contained in a BlocksAndItemsWithVariantsOfTypes.
	 * 
	 * @param <T> For the type that should be returned when getting this type's Block/Item.
	 */
	public static class ObjectType<T extends Object> implements IMetadata
	{
		protected String name;
		protected String unlocalizedName;
		protected Class<? extends Block> blockClass;
		protected Class<? extends Item> itemClass;
		protected List<IMetadata> variantExclusions;
		
		public ObjectType(String name, String unlocalizedName, Class<? extends Block> blockClass, Class<? extends Item> itemClass, IMetadata... variantExclusions)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.blockClass = blockClass;
			this.itemClass = itemClass;
			this.variantExclusions = Arrays.asList(variantExclusions);
		}
		
		public ObjectType(String name, Class<? extends Block> blockClass, Class<? extends Item> itemClass, IMetadata... variantExclusions)
		{
			this(name, name, blockClass, itemClass, variantExclusions);
		}
		
		@Override
		public String getName()
		{
			return name;
		}
		
		@Override
		public String getUnlocalizedName()
		{
			return unlocalizedName;
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

		public IStateMapper getStateMapper(T obj)
		{
			return null;
		}
	}
	
	public final List<ObjectType> types;
	public final List<IMetadata> variants;
	
	/**
	 * Map of Block/Item types to a map of variants to the block/item itself.
	 */
	protected final HashMap<ObjectType, HashMap<IMetadata, Pair<Object, Integer>>> map = new HashMap<ObjectType, HashMap<IMetadata, Pair<Object, Integer>>>();
	
	/**
	 * Creates a BlocksAndItemsWithVariantsOfTypes with Blocks/Items from the "types" list containing the variants in "variants".
	 * All Block classes that are constructed in this method MUST have a "public static IProperty[] getProperties()" to allow us to
	 * determine how many variants can be stored in the block.
	 * The Block's variant property must have a name of "variant" exactly.
	 * The Block and Item classes must also have a constructor that takes a List<IMetadata> that tells the Block what variants it stores.
	 * 
	 * @param types The types of Blocks/Items (Objects) to store.
	 * @param variants The variants to store for each Block/Item.
	 */
	public BlocksAndItemsWithVariantsOfTypes(List<ObjectType> types, List<IMetadata> variants)
	{
		this.variants = variants;
		this.types = types;

		try
		{
			for (final ObjectType type : types)
			{
				Class<? extends Block> blockClass = type.getBlockClass();
				Class<? extends Item> itemClass = type.getItemClass();
				HashMap<IMetadata, Pair<Object, Integer>> variantObjectMap = new HashMap<IMetadata, Pair<Object, Integer>>();
				
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

				for (int i = 0; i < Math.ceil(typeVariants.size() / (float) maxVariants); i++)
				{
					final List<IMetadata> subVariants = typeVariants.subList(i * maxVariants, Math.min((i + 1) * maxVariants, typeVariants.size()));

					Block block = null;
					Item item = null;
					String registryName = type.getName() + "_" + i;
					
					if (blockClass != null)
					{
						Constructor<Block> constructor = (Constructor<Block>) blockClass.getConstructor(List.class);
						block = constructor.newInstance(subVariants);
						
						// Construct item as necessary for registering as the item to accompany a block.
						if (blockClass != null)
						{
							if (itemClass != null)
							{
								Constructor<Item> itemConstructor = (Constructor<Item>) itemClass.getConstructor(blockClass, List.class);
								item = itemConstructor.newInstance(block, subVariants);
							}
							else
							{
								item = new ItemMultiTexture(block, block, new Function()
								{
									@Override
									public Object apply(Object input)
									{
										int metadata = ((ItemStack) input).getMetadata();
										return subVariants.get(metadata).getUnlocalizedName();
									}
								});
							}
						}
						
						Genesis.proxy.registerBlockWithItem(block, registryName, item);
						block.setUnlocalizedName(type.getUnlocalizedName());

						// Register resource locations for the block.
						final IProperty variantProp = getVariantProperty(block);
						
						if (variantProp != null)
						{
							IStateMapper stateMap = type.getStateMapper(block);
							
							if (stateMap == null)
							{
								stateMap = new StateMap.Builder().setProperty(variantProp).setBuilderSuffix("_" + type.getName()).build();
							}
							
							Genesis.proxy.registerModelStateMap(block, stateMap);
						}
						// End registering block resource locations.
					}
					else if (itemClass != null)
					{
						// Construct and register the item alone.
						Constructor<Item> itemConstructor = (Constructor<Item>) itemClass.getConstructor(List.class);
						item = itemConstructor.newInstance(subVariants);
						
						Genesis.proxy.registerItem(item, registryName);
					}
					
					if (item != null)
					{
						item.setUnlocalizedName(type.getUnlocalizedName());
						
						// Register item model locations.
						for (IMetadata variant : subVariants)
						{
							int metadata = subVariants.indexOf(variant);
							String resource = variant.getName() + "_" + type.getName();
							Genesis.proxy.registerModel(item, metadata, resource);
						}
					}
					
					// Add the Block or Item to our object map with its metadata ID.
					int variantMetadata = 0;
					
					for (IMetadata variant : subVariants)
					{
						Object obj;
						
						if (block != null)
						{
							obj = block;
						}
						else
						{
							obj = item;
						}
						
						variantObjectMap.put(variant, Pair.of(obj, variantMetadata));
						variantMetadata++;
					}
				}
				
				map.put(type, variantObjectMap);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public BlocksAndItemsWithVariantsOfTypes(ObjectType[] objectTypes, IMetadata[] variants)
	{
		this(Arrays.asList(objectTypes), Arrays.asList(variants));
	}
	
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
	 * Gets the Pair containing the metadata for this variant and its container Block or Item.
	 * 
	 * @param type
	 * @param variant
	 * @return The Block/Item casted to the type provided by the generic type in "type".
	 */
	public Pair<Object, Integer> getMetadataObjectPair(ObjectType type, IMetadata variant)
	{
		if (!types.contains(type))
		{
			throw new RuntimeException("Attempted to get an object of type " + type + " from a BlocksAndItemsWithVariantsOfTypes that does not contain that type.");
		}
		
		if (!variants.contains(variant))
		{
			throw new RuntimeException("Attempted to get an object of variant " + variant + " from a BlocksAndItemsWithVariantsOfTypes that does not contain that type.");
		}
		
		return map.get(type).get(variant);
	}

	/**
	 * Gets the Block or Item for the type and variant.
	 * 
	 * @param type
	 * @param variant
	 * @return The Block/Item casted to the type provided by the generic type in "type".
	 */
	public <T> T getObject(ObjectType<T> type, IMetadata variant)
	{
		return (T) getMetadataObjectPair(type, variant).getRight();
	}
	
	/**
	 * Gets an IBlockState for the specified Block variant in this combo.
	 */
	public IBlockState getBlockState(ObjectType type, IMetadata variant)
	{
		Pair<Object, Integer> pair = getMetadataObjectPair(type, variant);
		
		Object obj = pair.getLeft();
		
		if (obj instanceof Block)
		{
			Block block = (Block) obj;
			return block.getDefaultState().withProperty(getVariantProperty(block), (Comparable) variant);
		}
		
		throw new IllegalArgumentException("ObjectType " + type.getName() + " does not include a Block instance.");
	}
	
	/**
	 * Gets a stack of the specified Item in this combo with the specified stack size.
	 */
	public ItemStack getStack(ObjectType type, IMetadata variant, int stackSize)
	{
		Pair<Object, Integer> pair = getMetadataObjectPair(type, variant);
		
		Object obj = pair.getLeft();
		Item item;
		
		if (obj instanceof Block)
		{
			item = Item.getItemFromBlock((Block) obj);
		}
		else
		{
			item = (Item) obj;
		}
		
		ItemStack stack = new ItemStack(item, stackSize, pair.getRight());
		
		return stack;
	}
	
	/**
	 * Gets a stack of the specified Item in this combo.
	 */
	public ItemStack getStack(ObjectType type, IMetadata variant)
	{
		return getStack(type, variant, 1);
	}
	
	public List<IMetadata> getValidVariants(ObjectType type)
	{
		return type.getValidVariants(new ArrayList(variants));
	}
}
