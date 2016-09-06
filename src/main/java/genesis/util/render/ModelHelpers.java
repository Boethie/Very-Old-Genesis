package genesis.util.render;

import genesis.common.Genesis;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ItemModelMesherForge;
import net.minecraftforge.client.model.*;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;
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
	public static Map<Item, TIntObjectHashMap<ModelResourceLocation>> itemModelLocations;
	public static Map<Item, ItemMeshDefinition> itemModelDefinitions;
	public static Field modelBlockDefinitionMap;
	public static Field destroyBlockIcons;

	protected static List<Pair<BlockStateContainer, ResourceLocation>> forcedModels = new ArrayList<>();
	protected static boolean doInit = true;

	public static void preInit()
	{
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

	public static <T extends Comparable<T>> String getPropertyString(IProperty<T> property, T value)
	{
		return property.getName() + "=" + property.getName(value);
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> String getPropertyStringUnsafe(IProperty<?> property, Comparable<?> value)
	{
		return getPropertyString((IProperty<T>) property, (T) value);
	}

	public static String getPropertyString(Map<IProperty<?>, Comparable<?>> properties)
	{
		String output = "";

		for (Map.Entry<IProperty<?>, Comparable<?>> entry : properties.entrySet())
		{
			if (output.length() > 0)
			{
				output += ",";
			}

			output += getPropertyStringUnsafe(entry.getKey(), entry.getValue());
		}

		if (output.length() <= 0)
		{
			output = "normal";
		}

		return output;
	}

	public static String getPropertyString(IBlockState state)
	{
		return getPropertyString(state.getProperties());
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

		final ResourceLocation generated = new ResourceLocation("minecraft:builtin/generated");

		ModelBlock curModel = ModelHelpers.getModelBlock(loc);

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

	public static void bindAtlasTexture()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	public static void renderBakedModel(IBakedModel model)
	{
		getBlockRenderer().renderModelBrightnessColor(model, 1, 1, 1, 1);
	}

	/**
	 * Render the model at the ModelResourceLocation as a plain baked model. Not preferable for rendering in a block.
	 */
	public static void renderModel(ModelResourceLocation loc)
	{
		renderBakedModel(getModelManager().getModel(loc));
	}

	/**
	 * @return A randomized baked model using the provided block state and position in the world.
	 */
	public static IBakedModel getBakedBlockModel(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if (state == null)
		{
			return getModelManager().getModel(getMissingModelLocation());
		}

		return getBlockDispatcher().getModelForState(state);
	}

	/**
	 * Gets the block state for the model location that was forced to load by this class's dummy block.
	 */
	public static IBlockState getFakeState(ModelResourceLocation location)
	{
		return locationToFakeState.get(location);
	}

	/**
	 * @return A randomized model from a blockstates json forced to load by this helper.
	 */
	public static IBakedModel getBakedBlockModel(ModelResourceLocation loc, IBlockAccess world, BlockPos pos)
	{
		return getBakedBlockModel(getFakeState(loc), world, pos);
	}

	/**
	 * @return A duplicate of the original baked model with all faces mapped to the provided sprite,
	 * each face projected from its cardinal direction.
	 */
	public static IBakedModel getCubeProjectedBakedModel(IBlockState state, IBakedModel model, TextureAtlasSprite texture, BlockPos pos)
	{
		return new SimpleBakedModel.Builder(state, model, texture, pos).makeBakedModel();
	}

	/*/**
	 * @return A duplicate of the original baked model with all faces mapped to the provided sprite,
	 * using their original vertex UVs.
	 */
	/*public static IBakedModel getNormalizedCubeProjectedBakedModel(IBakedModel model, TextureAtlasSprite texture)
	{
		return new NormalizedCubeProjectedBakedModel(model, texture);
	}*/

	/**
	 * Renders a randomized block model for the provided state, world and position.
	 */
	public static void renderBlockModel(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		renderBakedModel(getBakedBlockModel(state, world, pos));
	}

	/**
	 * Renders a model that was forced to load using this helper's system.
	 */
	public static void renderBlockModel(ModelResourceLocation loc, IBlockAccess world, BlockPos pos)
	{
		renderBakedModel(getBakedBlockModel(loc, world, pos));
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

	public static Map<Item, TIntObjectHashMap<ModelResourceLocation>> getItemModelLocationMap()
	{
		if (itemModelLocations == null)
		{
			itemModelLocations = ReflectionHelper.getPrivateValue(ItemModelMesherForge.class, (ItemModelMesherForge) getModelMesher(), "locations");
		}

		return itemModelLocations;
	}

	public static Map<Item, ItemMeshDefinition> getItemModelDefinitions()
	{
		if (itemModelDefinitions == null)
		{
			itemModelDefinitions = ReflectionHelper.getPrivateValue(ItemModelMesher.class, getModelMesher(), "shapers", "field_178092_c");
		}

		return itemModelDefinitions;
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

			if (loc == null)
			{
				ItemMeshDefinition def = getItemModelDefinitions().get(stack.getItem());

				if (def != null)
					loc = def.getModelLocation(stack);
			}

			if (loc != null)
				return loc;
		}

		return getMissingModelLocation();
	}

	public static String getStringIDInSetForStack(ItemStack stack, Set<String> set, String... fallbacks)
	{
		if (stack != null)
		{
			ModelResourceLocation model = ModelHelpers.getLocationFromStack(stack);
			String stackModel = model.getResourceDomain() + "__" + model.getResourcePath();

			if (set.contains(stackModel))
			{
				return stackModel;
			}

			ResourceLocation regLoc = Item.REGISTRY.getNameForObject(stack.getItem());
			String regID = regLoc.getResourceDomain() + "__" + regLoc.getResourcePath();
			String regStackID = regID + "__meta__" + stack.getMetadata();

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

	public static Class<? extends IModel> getVanillaModelWrapper()
	{
		if (classVanillaModelWrapper == null)
			classVanillaModelWrapper = getModelLoaderClass("VanillaModelWrapper");

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

	public static ResourceLocation getRawItemLocation(ModelResourceLocation location)
	{
        return new ResourceLocation(location.getResourceDomain(), "item/" + location.getResourcePath());
	}

	public static IModel getModel(ResourceLocation loc)
	{
		IModel model;

		try
		{
			model = ModelLoaderRegistry.getModel(loc instanceof ModelResourceLocation ?
					getRawItemLocation((ModelResourceLocation) loc) : loc);

			if (model == null)
				model = ModelLoaderRegistry.getModel(loc);
		}
		catch (Exception e)
		{
			model = ModelLoaderRegistry.getMissingModel();
		}

		return model;
	}

	public static ModelBlock getModelBlock(ResourceLocation loc)
	{
		return getModelBlock(getModel(loc));
	}

	@SuppressWarnings("unchecked")
	public static Map<String, VariantList> getModelBlockDefinitionMap(ModelBlockDefinition definition)
	{
		if (modelBlockDefinitionMap == null)
		{
			modelBlockDefinitionMap = ReflectionHelper.findField(ModelBlockDefinition.class, "mapVariants", "field_178332_b");
		}

		try
		{
			return (Map<String, VariantList>) modelBlockDefinitionMap.get(definition);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Map<String, VariantList> getBlockstatesVariants(ResourceLocation loc)
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

		Map<String, VariantList> output = new HashMap<>();

		try
		{
			for (IResource resource : resources)
			{
				InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
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

	public static TextureAtlasSprite getDestroyBlockIcon(int progress)
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/destroy_stage_" + progress);
	}

	public static TextureAtlasSprite getDestroyBlockIcon(float progress)
	{
		progress = MathHelper.clamp_float(progress, 0, 1);
		int index = (int) Math.ceil(progress * 10) - 1;
		return getDestroyBlockIcon(index);
	}

	public static void forceModelLoading(BlockStateContainer state, ResourceLocation loc)
	{
		forcedModels.add(Pair.of(state, loc));
	}

	public static void forceModelLoading(final String name, final Collection<String> states, ResourceLocation loc)
	{
		if (states.isEmpty())
		{
			Genesis.logger.warn(new IllegalArgumentException("No states provided to force loading for model location '" + loc + "'."));
			return;
		}

		IProperty<String> property = new IProperty<String>()
		{
			@Override public String getName()
			{
				return name;
			}
			@Override public Collection<String> getAllowedValues()
			{
				return states;
			}
			@Override public Class<String> getValueClass()
			{
				return String.class;
			}
			@Override public String getName(String value)
			{
				return value;
			}
			@Override public Optional<String> parseValue(String value)
			{
				if (getAllowedValues().contains(value))
					return Optional.of(value);
				return Optional.absent();
			}
		};

		try
		{
			forceModelLoading(new BlockStateContainer(null, property), loc);
		}
		catch (RuntimeException e)
		{
			Genesis.logger.warn("An error occurred constructing a fake BlockStateContainer object to force loading of '" + loc + "'.");
		}
	}

	public static void forceModelLoading(Collection<String> variants, ResourceLocation loc)
	{
		String sharedPropertyName = null;
		List<String> newVariants = new ArrayList<>(variants.size());

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
		Genesis.proxy.registerBlock(fakeBlock, null, new ResourceLocation(Constants.MOD_ID, "dummy_block"));

		final Map<IBlockState, IBlockState> actualToFakeState = new HashMap<>();

		for (Pair<BlockStateContainer, ResourceLocation> entry : forcedModels)
		{
			for (final IBlockState actualState : entry.getKey().getValidStates())
			{
				@SuppressWarnings("deprecation")
				IBlockState fakeState = new IBlockState()
				{
					@Override
					public boolean onBlockEventReceived(World world, BlockPos pos, int id, int param)
					{
						return actualState.onBlockEventReceived(world, pos, id, param);
					}
					@Override
					public void neighborChanged(World world, BlockPos pos, Block block)
					{
						actualState.neighborChanged(world, pos, block);
					}
					@Override
					public Collection<IProperty<?>> getPropertyNames()
					{
						return actualState.getPropertyNames();
					}
					@Override
					public <T extends Comparable<T>> T getValue(IProperty<T> property)
					{
						return actualState.getValue(property);
					}
					@Override
					public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value)
					{
						return actualToFakeState.get(actualState.withProperty(property, value));
					}
					@Override
					public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property)
					{
						return actualToFakeState.get(actualState.cycleProperty(property));
					}
					@Override
					public ImmutableMap<IProperty<?>, Comparable<?>> getProperties()
					{
						return actualState.getProperties();
					}
					@Override
					public Block getBlock()
					{
						return fakeBlock;
					}
					@Override
					public Material getMaterial()
					{
						return actualState.getMaterial();
					}
					@Override
					public boolean isFullBlock()
					{
						return actualState.isFullBlock();
					}

					@Override
					public boolean canEntitySpawn(Entity entityIn)
					{
						return false;
					}

					@Override
					public int getLightOpacity()
					{
						return actualState.getLightOpacity();
					}
					@Override
					public int getLightOpacity(IBlockAccess world, BlockPos pos)
					{
						return actualState.getLightOpacity(world, pos);
					}
					@Override
					public int getLightValue()
					{
						return actualState.getLightValue();
					}
					@Override
					public int getLightValue(IBlockAccess world, BlockPos pos)
					{
						return actualState.getLightValue(world, pos);
					}
					@Override
					public boolean isTranslucent()
					{
						return actualState.isTranslucent();
					}
					@Override
					public boolean useNeighborBrightness()
					{
						return actualState.useNeighborBrightness();
					}
					@Override
					public MapColor getMapColor()
					{
						return actualState.getMapColor();
					}
					@Override
					public IBlockState withRotation(Rotation rot)
					{
						return actualState.withRotation(rot);
					}
					@Override
					public IBlockState withMirror(Mirror mirror)
					{
						return actualState.withMirror(mirror);
					}
					@Override
					public boolean isFullCube()
					{
						return actualState.isFullCube();
					}
					@Override
					public EnumBlockRenderType getRenderType()
					{
						return actualState.getRenderType();
					}
					@Override
					public int getPackedLightmapCoords(IBlockAccess world, BlockPos pos)
					{
						return actualState.getPackedLightmapCoords(world, pos);
					}
					@Override
					public float getAmbientOcclusionLightValue()
					{
						return actualState.getAmbientOcclusionLightValue();
					}
					@Override
					public boolean isBlockNormalCube()
					{
						return actualState.isBlockNormalCube();
					}
					@Override
					public boolean isNormalCube()
					{
						return actualState.isNormalCube();
					}
					@Override
					public boolean canProvidePower()
					{
						return actualState.canProvidePower();
					}
					@Override
					public int getWeakPower(IBlockAccess world, BlockPos pos, EnumFacing side)
					{
						return actualState.getWeakPower(world, pos, side);
					}
					@Override
					public boolean hasComparatorInputOverride()
					{
						return actualState.hasComparatorInputOverride();
					}
					@Override
					public int getComparatorInputOverride(World world, BlockPos pos)
					{
						return actualState.getComparatorInputOverride(world, pos);
					}
					@Override
					public float getBlockHardness(World world, BlockPos pos)
					{
						return actualState.getBlockHardness(world, pos);
					}
					@Override
					public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, BlockPos pos)
					{
						return actualState.getPlayerRelativeBlockHardness(player, world, pos);
					}
					@Override
					public int getStrongPower(IBlockAccess world, BlockPos pos, EnumFacing side)
					{
						return actualState.getStrongPower(world, pos, side);
					}
					@Override
					public EnumPushReaction getMobilityFlag()
					{
						return actualState.getMobilityFlag();
					}
					@Override
					public IBlockState getActualState(IBlockAccess world, BlockPos pos)
					{
						return actualState.getActualState(world, pos);
					}
					@Override
					public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos pos)
					{
						return actualState.getSelectedBoundingBox(world, pos);
					}
					@Override
					public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing facing)
					{
						return actualState.shouldSideBeRendered(world, pos, facing);
					}
					@Override
					public boolean isOpaqueCube()
					{
						return actualState.isOpaqueCube();
					}
					@Override
					public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos)
					{
						return actualState.getCollisionBoundingBox(world, pos);
					}
					@Override
					public void addCollisionBoxToList(World world, BlockPos pos,
							AxisAlignedBB mask, List<AxisAlignedBB> list, Entity entity)
					{
						actualState.addCollisionBoxToList(world, pos, mask, list, entity);
					}
					@Override
					public AxisAlignedBB getBoundingBox(IBlockAccess world, BlockPos pos)
					{
						return actualState.getBoundingBox(world, pos);
					}
					@Override
					public RayTraceResult collisionRayTrace(World world, BlockPos pos, Vec3d start, Vec3d end)
					{
						return actualState.collisionRayTrace(world, pos, start, end);
					}
					@Override
					public boolean isFullyOpaque()
					{
						return actualState.isFullyOpaque();
					}
					@Override
					public boolean doesSideBlockRendering(IBlockAccess world, BlockPos pos, EnumFacing side)
					{
						return actualState.doesSideBlockRendering(world, pos, side);
					}
					@Override
					public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
					{
						return actualState.isSideSolid(world, pos, side);
					}
					@Override
					public String toString()
					{
						return actualState.toString() + "(fake)";
					}
				};
				ResourceLocation locationNoVariant = entry.getValue();
				ModelResourceLocation location =
						new ModelResourceLocation(locationNoVariant.getResourceDomain() + ":" + locationNoVariant.getResourcePath(),
						getPropertyString(fakeState));

				actualToFakeState.put(actualState, fakeState);
				locationToFakeState.put(location, fakeState);
			}
		}

		IStateMapper stateMapper = block -> locationToFakeState.inverse();
		ModelLoader.setCustomStateMapper(fakeBlock, stateMapper);
	}
}
