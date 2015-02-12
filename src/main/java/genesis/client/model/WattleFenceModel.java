package genesis.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Function;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import genesis.client.GenesisCustomModelLoader;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.IMetadata;
import genesis.metadata.TreeBlocks;
import genesis.util.Constants;
import genesis.util.ModelHelpers;

public class WattleFenceModel implements IModel, IMultiBakedModelOwner
{
	protected static final ResourceLocation POST_MODEL = new ResourceLocation(Constants.MOD_ID, "block/wattle_fence");
	protected static final ResourceLocation NORTH_MODEL = new ResourceLocation(Constants.MOD_ID, "block/wattle_fence_north");
	protected static final ResourceLocation EAST_MODEL = new ResourceLocation(Constants.MOD_ID, "block/wattle_fence_east");
	protected static final ResourceLocation SOUTH_MODEL = new ResourceLocation(Constants.MOD_ID, "block/wattle_fence_south");
	protected static final ResourceLocation WEST_MODEL = new ResourceLocation(Constants.MOD_ID, "block/wattle_fence_west");
	
	public static void register(List<EnumTree> variants)
	{
		for (EnumTree variant : variants)
		{
			Genesis.proxy.registerCustomModel("models/block/" + variant.getName() + "_wattle_fence", new WattleFenceModel());
		}
	}

	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return new ArrayList<ResourceLocation>(){{
			add(POST_MODEL);
			add(NORTH_MODEL);
			add(EAST_MODEL);
			add(SOUTH_MODEL);
			add(WEST_MODEL);
		}};
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		ArrayList<ResourceLocation> textures = new ArrayList<ResourceLocation>();
		
		for (IMetadata variant : GenesisBlocks.trees.getValidVariants(TreeBlocks.WATTLE_FENCE))
		{
			ResourceLocation loc = new ResourceLocation(Constants.MOD_ID, "blocks/stick_" + variant.getName());
			textures.add(loc);
			/*IBlockState blockState = GenesisBlocks.trees.getBlockState(TreeBlocks.WATTLE_FENCE, variant);

			ModelResourceLocation loc = GenesisCustomModelLoader.blockResourceMap.get(blockState);
			loc = new ModelResourceLocation(loc.getResourceDomain() + ":block/" + loc.getResourcePath() + "#" + loc.getVariant());
			
			GenesisCustomModelLoader.acceptNothing = true;
			IModel childModel = ModelLoaderRegistry.getModel(loc);
			GenesisCustomModelLoader.acceptNothing = false;
			
			textures.addAll(childModel.getTextures());*/
		}
		
		return textures;
	}

	@Override
	public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		/*IFlexibleBakedModel post = ModelLoaderRegistry.getModel(POST_MODEL).bake(state, format, bakedTextureGetter);
		IFlexibleBakedModel north = ModelLoaderRegistry.getModel(NORTH_MODEL).bake(state, format, bakedTextureGetter);
		IFlexibleBakedModel east = ModelLoaderRegistry.getModel(EAST_MODEL).bake(state, format, bakedTextureGetter);
		IFlexibleBakedModel south = ModelLoaderRegistry.getModel(SOUTH_MODEL).bake(state, format, bakedTextureGetter);
		IFlexibleBakedModel west = ModelLoaderRegistry.getModel(WEST_MODEL).bake(state, format, bakedTextureGetter);*/
		
		IModel post = ModelLoaderRegistry.getModel(POST_MODEL);
		IModel north = ModelLoaderRegistry.getModel(NORTH_MODEL);
		IModel east = ModelLoaderRegistry.getModel(EAST_MODEL);
		IModel south = ModelLoaderRegistry.getModel(SOUTH_MODEL);
		IModel west = ModelLoaderRegistry.getModel(WEST_MODEL);
		
		//TextureAtlasSprite texture = ModelLoaderRegistry.getModel(variantTextures.values().iterator().next()).bake(state, format, bakedTextureGetter).getTexture();

		MultiBakedModelWrapper wrapper = new MultiBakedModelWrapper(this, null, post, north, east, south, west);
		//MultiBakedModelWrapper wrapper = new MultiBakedModelWrapper(this, null, post, north);
		wrapper.bake(state, format, bakedTextureGetter);
		//MultiBakedModelWrapper wrapper = new MultiBakedModelWrapper(this, null, post);
		
		return wrapper;
	}

	@Override
	public IModelState getDefaultState()
	{
        return ModelRotation.X0_Y0;
	}
	
	/*protected IBlockState fenceState = null;
	
	public WattleFenceModel()
	{
	}
	
	public WattleFenceModel(IBlockState state)
	{
		fenceState = state;
	}

	@Override
	public List getFaceQuads(EnumFacing dir)
	{
		if (fenceState != null)
		{
			
		}
		
		return new ArrayList();
	}

	@Override
	public List getGeneralQuads()
	{
		if (fenceState != null)
		{
			
		}
		
		return new ArrayList();
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return false;
	}

	@Override
	public boolean isGui3d()
	{
		return false;
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getTexture()
	{
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return null;
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state)
	{
		return null;
	}*/

	@Override
	public boolean shouldRenderPart(IBlockState state, IBakedModel model, int part)
	{
		boolean north = (Boolean) state.getValue(BlockFence.NORTH);
		boolean east = (Boolean) state.getValue(BlockFence.EAST);
		boolean south = (Boolean) state.getValue(BlockFence.SOUTH);
		boolean west = (Boolean) state.getValue(BlockFence.WEST);
		
		switch (part)
		{
		case 0:
			return north;
		case 1:
			return east;
		case 2:
			return south;
		case 3:
			return west;
		}
		
		return false;
	}
}
