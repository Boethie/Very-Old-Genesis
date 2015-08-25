package genesis.util.render;

import genesis.client.GenesisCustomModelLoader;
import genesis.common.Genesis;
import genesis.util.Constants.Unlocalized;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition.Variants;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ItemModelMesherForge;
import net.minecraftforge.client.model.*;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Charsets;
import com.google.common.collect.*;

public class ModelHelpers
{
	public static final Block fakeBlock = new BlockAir(){}.setUnlocalizedName(Unlocalized.PREFIX + "dummyBlock");
	public static final HashBiMap<ModelResourceLocation, IBlockState> locationToFakeState = HashBiMap.create();
	
	public static ModelLoader forgeModelLoader;
	public static ModelResourceLocation missingModelLocation;
	public static BlockRendererDispatcher dispatcher;
	public static BlockModelShapes modelShapes;
	public static BlockModelRenderer modelRenderer;
	public static ModelManager modelManager;
	public static ItemModelMesher modelMesher;
	public static Method getItemMesherIndex;
	public static Map<Integer, ModelResourceLocation> itemLocation;
	public static Map<IBlockState, ModelResourceLocation> blockResourceMap;
	public static Class<? extends IModel> classVanillaModelWrapper;
	public static Field vanillaModelWrapperModelBlock;
	public static IdentityHashMap<Item, TIntObjectHashMap<ModelResourceLocation>> itemModelLocations;
	public static Field modelBlockDefinitionMap;
	public static Field destroyBlockIcons;
	
	protected static List<Pair<BlockState, ResourceLocation>> forcedModels = new ArrayList();
	protected static boolean doInit = true;
	
	public static void preInit()
	{
		ModelHelpers instance = new ModelHelpers();
		
		addForcedModels();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getModelLoaderClass(String name)
	{
		Class<?>[] classes = ModelLoader.class.getDeclaredClasses();
		
		for (Class<?> clazz : classes)
		{
			if (clazz.getName().endsWith("$" + name))
			{
				return (Class<T>) clazz;
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
			modelManager = getModelMesher().getModelManager();
		}
		
		return modelManager;
	}
	
	public static ItemModelMesher getModelMesher()
	{
		if (modelMesher == null)
		{
			modelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		}
		
		return modelMesher;
	}
	
	public static String getPropertyString(Map<IProperty, Comparable<?>> properties)
	{
		String output = "";
		
		for (Map.Entry<IProperty, Comparable<?>> entry : properties.entrySet())
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
		return getStateToModelLocationMap().get(state);
	}
	
	public static IdentityHashMap<Item, TIntObjectHashMap<ModelResourceLocation>> getItemModelLocationMap()
	{
		if (itemModelLocations == null)
		{
			itemModelLocations = ReflectionHelper.getPrivateValue(ItemModelMesherForge.class, (ItemModelMesherForge) getModelMesher(), "locations");
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
			String stackModel = ModelHelpers.getLocationFromStack(stack).toString();
			stackModel = stackModel.substring(0, stackModel.lastIndexOf("#"));
			
			if (set.contains(stackModel))
			{
				return stackModel;
			}
			
			String regID = ((ResourceLocation) Item.itemRegistry.getNameForObject(stack.getItem())).toString();
			String regStackID = regID + "@" + stack.getMetadata();
			
			if (set.contains(regStackID))
			{
				return regStackID;
			}
			else if (set.contains(regID))
			{
				return regID;
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
	
	@SuppressWarnings("unchecked")
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
			if (vanillaModelWrapperModelBlock == null)
			{
				vanillaModelWrapperModelBlock = ReflectionHelper.findField(classVanillaModelWrapper, "model");
			}
			
			try
			{
				return (ModelBlock) vanillaModelWrapperModelBlock.get(model);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return null;
	}
	
	public static IModel getModel(ResourceLocation loc)
	{
		IModel model;
		
		try
		{
			model = ModelLoaderRegistry.getModel(loc);
		}
		catch (Exception e)
		{
			model = ModelLoaderRegistry.getMissingModel();
		}
		
		return model;
	}
	
	public static IModel getLoadedModel(ResourceLocation loc)
	{
		GenesisCustomModelLoader.acceptNothing = true;
		IModel model = getModel(loc);
		GenesisCustomModelLoader.acceptNothing = false;
		
		return model;
	}
	
	public static ModelBlock getModelBlock(ResourceLocation loc)
	{
		return getModelBlock(getLoadedModel(loc));
	}
	
	public static Map<String, Variants> getModelBlockDefinitionMap(ModelBlockDefinition definition)
	{
		if (modelBlockDefinitionMap == null)
		{
			modelBlockDefinitionMap = ReflectionHelper.findField(ModelBlockDefinition.class, "mapVariants", "field_178332_b");
		}
		
		try
		{
			return (Map<String, Variants>) modelBlockDefinitionMap.get(definition);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static Map<String, Variants> getBlockstatesVariants(ResourceLocation loc)
	{
		ResourceLocation blockstatesLocation = new ResourceLocation(loc.getResourceDomain(), "blockstates/" + loc.getResourcePath() + ".json");
		List<IResource> resources = null;
		
		try
		{
			resources = Minecraft.getMinecraft().getResourceManager().getAllResources(blockstatesLocation);
		}
		catch (IOException e)
		{
			Genesis.logger.warn("Encountered an IO exception while getting the IResources for location " + blockstatesLocation, e);
		}
		
		Map<String, Variants> output = new HashMap<String, Variants>();
		
		try
		{
			for (IResource resource : resources)
			{
				InputStreamReader reader = new InputStreamReader(resource.getInputStream(), Charsets.UTF_8);
				ModelBlockDefinition definition = ModelBlockDefinition.parseFromReader(reader);
				output.putAll(getModelBlockDefinitionMap(definition));
			}
		}
		catch (Exception e)
		{
			Genesis.logger.warn("Encountered an exception while loading the blockstates json at " + blockstatesLocation, e);
		}
		
		return output;
	}
	
	public static TextureAtlasSprite getDestroyBlockIcon(float progress)
	{
		progress = MathHelper.clamp_float(progress, 0, 1);
		int index = (int) Math.ceil(progress * 10) - 1;
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/destroy_stage_" + index);
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
		List<String> newVariants = new ArrayList<String>(variants.size());
		
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
		forceModelLoading(getBlockstatesVariants(loc).keySet(), loc);
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
		
		final Map<IBlockState, IBlockState> actualToFakeState = Maps.newHashMap();
		
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
