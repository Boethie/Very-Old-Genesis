package genesis.util.render;

import genesis.client.*;
import genesis.common.*;
import genesis.util.*;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition.*;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ItemModelMesherForge;
import net.minecraftforge.client.model.*;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ModelHelpers
{
	public static final Block fakeBlock = new BlockAir(){}.setUnlocalizedName(Constants.PREFIX + "dummyBlock");
	public static final HashBiMap<ModelResourceLocation, IBlockState> locationToFakeState = HashBiMap.create();
	
	public static ModelLoader forgeModelLoader;
	public static ModelResourceLocation missingModelLocation;
	public static BlockRendererDispatcher dispatcher;
	public static BlockModelShapes modelShapes;
	public static BlockModelRenderer modelRenderer;
	public static ModelManager modelManager;
	public static Map<IBlockState, ModelResourceLocation> blockResourceMap;
	public static Class<? extends IModel> classVanillaModelWrapper;
	public static IdentityHashMap<Item, TIntObjectHashMap<ModelResourceLocation>> itemModelLocations;
	
	protected static List<Pair<BlockState, ResourceLocation>> forcedModels = new ArrayList();
	protected static boolean doInit = true;
	
	public static void preInit()
	{
		ModelHelpers instance = new ModelHelpers();
		
		addForcedModels();
	}
	
	public static Class getModelLoaderClass(String name)
	{
		Class<?>[] classes = ModelLoader.class.getDeclaredClasses();
		
		for (Class<?> clazz : classes)
		{
			if (clazz.getName().endsWith("$" + name))
			{
				return clazz;
			}
		}
		
		return null;
	}
	
	public static <T> ModelLoader getModelLoader()
	{
		if (forgeModelLoader == null)
		{
			Class<T> classVanillaLoader = getModelLoaderClass("VanillaLoader");
			T vanillaLoader = ReflectionHelper.getPrivateValue(classVanillaLoader, null, "instance");
			forgeModelLoader = ReflectionHelper.getPrivateValue(classVanillaLoader, vanillaLoader, "loader");
		}
		
		return forgeModelLoader;
	}
	
	public static BlockRendererDispatcher getBlockDispatcher()
	{
		if (dispatcher == null)
		{
			dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		}
		
		return dispatcher;
	}
	
	public static BlockModelShapes getBlockModelShapes()
	{
		if (modelShapes == null)
		{
			modelShapes = getModelManager().getBlockModelShapes();
		}
		
		return modelShapes;
	}
	
	public static BlockModelRenderer getBlockRenderer()
	{
		if (modelRenderer == null)
		{
			modelRenderer = getBlockDispatcher().getBlockModelRenderer();
		}
		
		return modelRenderer;
	}
	
	public static ModelManager getModelManager()
	{
		if (modelManager == null)
		{
			modelManager = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "modelManager", "field_178128_c");
		}
		
		return modelManager;
	}
	
	public static String getPropertyString(Map<IProperty, Comparable> properties)
	{
		String output = "";
		
		for (Map.Entry<IProperty, Comparable> entry : properties.entrySet())
		{
			if (output.length() > 0)
			{
				output += ",";
			}
			
			IProperty property = entry.getKey();
			output += property.getName() + "=" + property.getName(entry.getValue());
		}
		
		if (output.length() <= 0)
		{
			output = "normal";
		}
		
		return output;
	}
	
	public static ModelResourceLocation getLocationWithProperties(ResourceLocation loc, String properties)
	{
		return new ModelResourceLocation(loc.getResourceDomain() + ":" + loc.getResourcePath(), properties);
	}
	
	public static boolean isGeneratedItemModel(ItemStack stack)
	{
		return isGeneratedItemModel(getLocationFromStack(stack));
	}
	
	public static boolean isGeneratedItemModel(ModelResourceLocation loc)
	{
		final ModelResourceLocation missing = getMissingModelLocation();
		
		if (loc.equals(missing))
		{
			return false;
		}
		
		if (!loc.getResourcePath().startsWith("item/"))
		{
			loc = new ModelResourceLocation(loc.getResourceDomain() + ":item/" + loc.getResourcePath(), loc.getVariant());
		}

		final ResourceLocation generated = new ResourceLocation("minecraft:builtin/generated");
		
		ModelBlock itemModel = ModelHelpers.getModelBlock(loc);
		ModelBlock curModel = itemModel;
		
		while (curModel != null)
		{
			ResourceLocation parent = curModel.getParentLocation();
			
			if (parent == null)
			{
				break;
			}
			
			if (parent.equals(generated))
			{
				return true;
			}
			
			curModel = ModelHelpers.getModelBlock(parent);
		}
		
		return false;
	}
	
	/**
	 * Render the model at the ModelResourceLocation as a plain baked model. Not preferable for rendering in a block.
	 */
	public static void renderModel(ModelResourceLocation loc)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		IBakedModel bakedModel = getModelManager().getModel(loc);
		getBlockRenderer().renderModelBrightnessColor(bakedModel, 1, 1, 1, 1);
	}
	
	/**
	 * Used to allow a randomized variants of the owner block's block state.
	 */
	public static void renderBlockModel(ModelResourceLocation loc, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		IBakedModel bakedModel = getBlockDispatcher().getModelFromBlockState(state, world, pos);
		getBlockRenderer().renderModelBrightnessColor(bakedModel, 1, 1, 1, 1);
	}
	
	/**
	 * Used to allow a randomized variants of the owner block's block state.
	 */
	public static void renderBlockModel(ModelResourceLocation loc, IBlockAccess world, BlockPos pos)
	{
		renderBlockModel(loc, locationToFakeState.get(loc), world, pos);
	}
	
	public static Map<IBlockState, ModelResourceLocation> getStateToModelLocationMap()
	{
		if (blockResourceMap == null)
		{
			blockResourceMap = getBlockModelShapes().getBlockStateMapper().putAllStateModelLocations();
		}
		
		return blockResourceMap;
	}
	
	public static ModelResourceLocation getLocationFromState(IBlockState state)
	{
		getStateToModelLocationMap();
		return blockResourceMap.get(state);
	}
	
	public static IdentityHashMap<Item, TIntObjectHashMap<ModelResourceLocation>> getItemModelLocationMap()
	{
		if (itemModelLocations == null)
		{
			itemModelLocations = ReflectionHelper.getPrivateValue(ItemModelMesherForge.class, (ItemModelMesherForge) Minecraft.getMinecraft().getRenderItem().getItemModelMesher(), "locations");
		}
		
		return itemModelLocations;
	}
	
	public static ModelResourceLocation getMissingModelLocation()
	{
		if (missingModelLocation == null)
		{
			missingModelLocation = ReflectionHelper.getPrivateValue(ModelBakery.class, null, "MODEL_MISSING", "field_177604_a");
		}
		
		return missingModelLocation;
	}
	
	public static ModelResourceLocation getLocationFromStack(ItemStack stack)
	{
		if (stack != null)
		{
			getItemModelLocationMap();
			ModelResourceLocation loc = itemModelLocations.get(stack.getItem()).get(stack.getMetadata());
			
			if (loc != null)
			{
				return loc;
			}
		}
		
		return getMissingModelLocation();
	}
	
	public static String getStringIDInSetForStack(ItemStack stack, Set<String> set, String... fallbacks)
	{
		if (stack != null)
		{
			String id = ModelHelpers.getLocationFromStack(stack).toString();
			id = id.substring(0, id.lastIndexOf("#"));
			
			if (set.contains(id))
			{
				return id;
			}
			
			id = ((ResourceLocation) Item.itemRegistry.getNameForObject(stack.getItem())).toString();
			
			if (set.contains(id))
			{
				return id;
			}
			
			for (String fallback : fallbacks)
			{
				if (set.contains(fallback))
				{
					return fallback;
				}
			}
		}
		
		return null;
	}
	
	public static Class<? extends IModel> getVanillaModelWrapper()
	{
		if (classVanillaModelWrapper == null)
		{
			Class<?>[] classes = ModelLoader.class.getDeclaredClasses();
			
			for (Class<?> clazz : classes)
			{
				if ("net.minecraftforge.client.model.ModelLoader$VanillaModelWrapper".equals(clazz.getName()))
				{
					classVanillaModelWrapper = (Class<? extends IModel>) clazz;
				}
			}
		}
		
		return classVanillaModelWrapper;
	}
	
	public static ModelBlock getModelBlock(IModel model)
	{
		getVanillaModelWrapper();
		
		if (classVanillaModelWrapper.isInstance(model))
		{
			Field modelField = ReflectionHelper.findField(classVanillaModelWrapper, "model");
			
			try
			{
				return (ModelBlock) modelField.get(model);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return null;
	}
	
	public static IModel getLoadedModel(ResourceLocation loc)
	{
		GenesisCustomModelLoader.acceptNothing = true;
		IModel model = ModelLoaderRegistry.getModel(loc);
		GenesisCustomModelLoader.acceptNothing = false;
		
		return model;
	}
	
	public static ModelBlock getModelBlock(ResourceLocation loc)
	{
		return getModelBlock(getLoadedModel(loc));
	}
	
	public static Set<String> getBlockstatesVariants(ResourceLocation loc)
	{
		JsonDeserializer<List<String>> deserializer = new JsonDeserializer<List<String>>()
		{
			@Override
			public List<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException
			{
				ArrayList<String> output = new ArrayList();
				JsonObject variantsObject = JsonUtils.getJsonObject(json.getAsJsonObject(), "variants");
				
				for (Entry<String, JsonElement> entry : variantsObject.entrySet())
				{
					output.add(entry.getKey());
				}
				
				return output;
			}
		};
		Gson builder = new GsonBuilder().registerTypeAdapter(List.class, deserializer).create();
		
		ResourceLocation blockstatesLocation = new ResourceLocation(loc.getResourceDomain(), "blockstates/" + loc.getResourcePath() + ".json");
		List<IResource> resources;
		
		try
		{
			resources = Minecraft.getMinecraft().getResourceManager().getAllResources(blockstatesLocation);
		}
		catch (IOException exception)
		{
			throw new RuntimeException("Encountered an IO exception while getting the IResources for location " + blockstatesLocation, exception);
		}
		
		Set<String> output = new HashSet();
		
		for (IResource resource : resources)
		{
			InputStream stream = resource.getInputStream();
			
			try
			{
				output.addAll(builder.fromJson(new InputStreamReader(stream, Charsets.UTF_8), List.class));
			}
			catch (JsonParseException exception)
			{
				throw new RuntimeException("Encountered an JSON parsing exception when loading a list of variants for " + loc + " from " + resource.getResourceLocation() + " in resourcepack " + resource.getResourcePackName(), exception);
			}
			finally
			{
				IOUtils.closeQuietly(stream);
			}
		}
		
		return output;
	}
	
	public static void forceModelLoading(BlockState state, ResourceLocation loc)
	{
		forcedModels.add(Pair.of(state, loc));
	}
	
	public static void forceModelLoading(final String name, final Collection<String> states, ResourceLocation loc)
	{
		IProperty property = new IProperty()
		{
			@Override public String getName()
			{
				return name;
			}
			@Override public Collection<String> getAllowedValues()
			{
				return states;
			}
			@Override public Class<? extends String> getValueClass()
			{
				return String.class;
			}
			@Override public String getName(Comparable value)
			{
				return (String) value;
			}
		};
		
		forceModelLoading(new BlockState(null, property), loc);
	}
	
	public static void forceModelLoading(Collection<String> variants, ResourceLocation loc)
	{
		String sharedPropertyName = null;
		List<String> newVariants = new ArrayList(variants.size());
		
		for (String variant : variants)
		{
			int equalsIndex = variant.indexOf("=");
			String variantPropertyName = variant.substring(0, equalsIndex);
			
			if (sharedPropertyName == null)
			{
				sharedPropertyName = variantPropertyName;
			}
			else if (!sharedPropertyName.equals(variantPropertyName))
			{
				throw new RuntimeException("Multiple property names found while attempting to create a list of variants in the blockstates json of " + loc + ".");
			}
			
			newVariants.add(variant.substring(equalsIndex + 1));
		}
		
		forceModelLoading(sharedPropertyName, newVariants, loc);
	}
	
	public static void forceModelLoading(ResourceLocation loc)
	{
		forceModelLoading(getBlockstatesVariants(loc), loc);
	}
	
	public static void forceModelLoading(final String propertyName, ResourceLocation loc, String... states)
	{
		forceModelLoading(propertyName, Arrays.asList(states), loc);
	}
	
	public static void forceModelLoading(Block actualBlock, ResourceLocation loc)
	{
		forceModelLoading(actualBlock.getBlockState(), loc);
	}
	
	protected static void addForcedModels()
	{
		Genesis.proxy.registerBlock(fakeBlock, "dummy_block");
		
		final Map<IBlockState, IBlockState> actualToFakeState = new HashMap();
		
		for (Pair<BlockState, ResourceLocation> entry : forcedModels)
		{
			for (final IBlockState actualState : (Collection<IBlockState>) entry.getKey().getValidStates())
			{
				IBlockState fakeState = new IBlockState()
				{
					@Override public Collection getPropertyNames()
					{
						return actualState.getPropertyNames();
					}
					@Override public Comparable getValue(IProperty property)
					{
						return actualState.getValue(property);
					}
					@Override public IBlockState withProperty(IProperty property, Comparable value)
					{
						return actualToFakeState.get(actualState.withProperty(property, value));
					}
					@Override public IBlockState cycleProperty(IProperty property)
					{
						return actualToFakeState.get(actualState.cycleProperty(property));
					}
					@Override public ImmutableMap getProperties()
					{
						return actualState.getProperties();
					}
					@Override public Block getBlock()
					{
						return fakeBlock;
					}
					@Override public String toString()
					{
						return actualState.toString() + "(fake)";
					}
				};
				ResourceLocation locationNoVariant = entry.getValue();
				ModelResourceLocation location =
						new ModelResourceLocation(locationNoVariant.getResourceDomain() + ":" + locationNoVariant.getResourcePath(),
						getPropertyString(fakeState.getProperties()));
				
				actualToFakeState.put(actualState, fakeState);
				locationToFakeState.put(location, fakeState);
			}
		}
		
		IStateMapper stateMapper = new IStateMapper()
		{
			@Override
			public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block block)
			{
				return locationToFakeState.inverse();
			}
		};
		ModelLoader.setCustomStateMapper(fakeBlock, stateMapper);
	}
}
