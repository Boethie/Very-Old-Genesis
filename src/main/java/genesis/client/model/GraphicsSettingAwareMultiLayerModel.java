package genesis.client.model;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import genesis.util.Constants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.Collection;
import java.util.List;

// These classes are basically a modified copy of Forge's MultiLayerModel
// with support for changing parts based on graphics settings.
public class GraphicsSettingAwareMultiLayerModel
{
	public static void register()
	{
		ModelLoaderRegistry.registerLoader(Model.Loader.INSTANCE);
	}
	
	public static class Model implements IModel, IModelCustomData
	{
		public static final Model INSTANCE = new Model(ImmutableMap.of());
		
		private final ImmutableMap<Pair<GraphicsSetting, BlockRenderLayer>, ModelResourceLocation> models;
		
		public Model(ImmutableMap<Pair<GraphicsSetting, BlockRenderLayer>, ModelResourceLocation> models)
		{
			this.models = models;
		}
		
		@Override
		public Collection<ResourceLocation> getDependencies()
		{
			return ImmutableList.copyOf(models.values());
		}
		
		@Override
		public Collection<ResourceLocation> getTextures()
		{
			return ImmutableList.of();
		}
		
		private static ImmutableMap<Pair<GraphicsSetting, BlockRenderLayer>, IBakedModel> buildModels(ImmutableMap<Pair<GraphicsSetting, BlockRenderLayer>, ModelResourceLocation> models, IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
		{
			ImmutableMap.Builder<Pair<GraphicsSetting, BlockRenderLayer>, IBakedModel> builder = ImmutableMap.builder();
			for (Pair<GraphicsSetting, BlockRenderLayer> key : models.keySet())
			{
				IModel model = ModelLoaderRegistry.getModelOrLogError(models.get(key), "Couldn't load MultiLayerModel dependency: " + models.get(key));
				builder.put(key, model.bake(new ModelStateComposition(state, model.getDefaultState()), format, bakedTextureGetter));
			}
			return builder.build();
		}
		
		@Override
		public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
		{
			IModel missing = ModelLoaderRegistry.getMissingModel();
			return new Baked(buildModels(models, state, format, bakedTextureGetter),
					missing.bake(missing.getDefaultState(), format, bakedTextureGetter),
					IPerspectiveAwareModel.MapWrapper.getTransforms(state)
			);
		}
		
		@Override
		public IModelState getDefaultState()
		{
			return TRSRTransformation.identity();
		}
		
		@Override
		public Model process(ImmutableMap<String, String> customData)
		{
			ImmutableMap.Builder<Pair<GraphicsSetting, BlockRenderLayer>, ModelResourceLocation> builder = ImmutableMap.builder();
			for (String key : customData.keySet())
			{
				if ("base".equals(key))
				{
					builder.put(Pair.of(null, null), getLocation(customData.get(key)));
				}
				for (BlockRenderLayer layer : BlockRenderLayer.values())
				{
					for (GraphicsSetting setting : GraphicsSetting.values())
					{
						if ((setting.toString() + " " + layer.toString()).equals(key))
						{
							builder.put(Pair.of(setting, layer), getLocation(customData.get(key)));
						}
					}
				}
			}
			ImmutableMap<Pair<GraphicsSetting, BlockRenderLayer>, ModelResourceLocation> models = builder.build();
			if (models.isEmpty()) return INSTANCE;
			return new Model(models);
		}
		
		private ModelResourceLocation getLocation(String json)
		{
			JsonElement e = new JsonParser().parse(json);
			if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isString())
			{
				return new ModelResourceLocation(e.getAsString());
			}
			FMLLog.severe("Expect ModelResourceLocation, got: ", json);
			return new ModelResourceLocation("builtin/missing", "missing");
		}
		
		private static final class Baked implements IPerspectiveAwareModel
		{
			private final ImmutableMap<Pair<GraphicsSetting, BlockRenderLayer>, IBakedModel> models;
			private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> cameraTransforms;
			
			private final IBakedModel base;
			private final IBakedModel missing;
			private final ImmutableMap<Optional<EnumFacing>, ImmutableList<BakedQuad>> quads;
			
			public Baked(ImmutableMap<Pair<GraphicsSetting, BlockRenderLayer>, IBakedModel> models, IBakedModel missing, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> cameraTransforms)
			{
				this.models = models;
				this.cameraTransforms = cameraTransforms;
				this.missing = missing;
				if (models.containsKey(Pair.<GraphicsSetting, BlockRenderLayer>of(null, null)))
				{
					base = models.get(Pair.<GraphicsSetting, BlockRenderLayer>of(null, null));
				}
				else
				{
					base = missing;
				}
				ImmutableMap.Builder<Optional<EnumFacing>, ImmutableList<BakedQuad>> quadBuilder = ImmutableMap.builder();
				quadBuilder.put(Optional.absent(), buildQuads(models, Optional.absent()));
				for (EnumFacing side : EnumFacing.values())
				{
					quadBuilder.put(Optional.of(side), buildQuads(models, Optional.of(side)));
				}
				quads = quadBuilder.build();
			}
			
			private static ImmutableList<BakedQuad> buildQuads(ImmutableMap<Pair<GraphicsSetting, BlockRenderLayer>, IBakedModel> models, Optional<EnumFacing> side)
			{
				ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
				for (IBakedModel model : models.values())
				{
					builder.addAll(model.getQuads(null, side.orNull(), 0));
				}
				return builder.build();
			}
			
			@Override
			public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
			{
				IBakedModel model;
				BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
				GraphicsSetting settings = Minecraft.getMinecraft().gameSettings.fancyGraphics ? GraphicsSetting.FANCY : GraphicsSetting.FAST;
				if (layer == null)
				{
					return quads.get(Optional.fromNullable(side));
				}
				else if (!models.containsKey(Pair.of(settings, layer)))
				{
					model = missing;
				}
				else
				{
					model = models.get(Pair.of(settings, layer));
				}
				// assumes that child model will handle this state properly. FIXME?
				return model.getQuads(state, side, rand);
			}
			
			@Override
			public boolean isAmbientOcclusion()
			{
				return base.isAmbientOcclusion();
			}
			
			@Override
			public boolean isGui3d()
			{
				return base.isGui3d();
			}
			
			@Override
			public boolean isBuiltInRenderer()
			{
				return base.isBuiltInRenderer();
			}
			
			@Override
			public TextureAtlasSprite getParticleTexture()
			{
				return base.getParticleTexture();
			}
			
			@Override
			public ItemCameraTransforms getItemCameraTransforms()
			{
				return ItemCameraTransforms.DEFAULT;
			}
			
			@Override
			public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
			{
				return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, cameraTransforms, cameraTransformType);
			}
			
			@Override
			public ItemOverrideList getOverrides()
			{
				return ItemOverrideList.NONE;
			}
		}
		
		public enum Loader implements ICustomModelLoader
		{
			INSTANCE;
			
			public void onResourceManagerReload(IResourceManager resourceManager)
			{
			}
			
			public boolean accepts(ResourceLocation modelLocation)
			{
				return modelLocation.getResourceDomain().equals(Constants.MOD_ID) && (
						modelLocation.getResourcePath().equals("graphics-aware-multi-layer") ||
								modelLocation.getResourcePath().equals("models/block/graphics-aware-multi-layer") ||
								modelLocation.getResourcePath().equals("models/item/graphics-aware-multi-layer"));
			}
			
			public IModel loadModel(ResourceLocation modelLocation)
			{
				return Model.INSTANCE;
			}
		}
	}
	
	private enum GraphicsSetting
	{
		FAST("Fast"), FANCY("Fancy");
		
		private String name;
		
		GraphicsSetting(String name)
		{
			this.name = name;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}