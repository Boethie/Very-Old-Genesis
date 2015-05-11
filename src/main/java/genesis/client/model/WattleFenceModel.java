package genesis.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Function;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.Tessellator;
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
import genesis.client.GenesisClient;
import genesis.client.GenesisCustomModelLoader;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.IMetadata;
import genesis.metadata.TreeBlocksAndItems;
import genesis.util.Constants;
import genesis.util.render.ModelHelpers;

public class WattleFenceModel implements IModel, IMultiBakedModelOwner
{
	protected static final ResourceLocation POST_MODEL = new ResourceLocation(Constants.MOD_ID, "block/wattle_fence");
	protected static final ResourceLocation NORTH_MODEL = new ResourceLocation(Constants.MOD_ID, "block/wattle_fence_north");
	protected static final ResourceLocation EAST_MODEL = new ResourceLocation(Constants.MOD_ID, "block/wattle_fence_east");
	protected static final ResourceLocation SOUTH_MODEL = new ResourceLocation(Constants.MOD_ID, "block/wattle_fence_south");
	protected static final ResourceLocation WEST_MODEL = new ResourceLocation(Constants.MOD_ID, "block/wattle_fence_west");
	protected static IModel post;
	protected static IModel north;
	protected static IModel east;
	protected static IModel south;
	protected static IModel west;
	
	private static void getModels()
	{
		if (post == null)
		{
			post = ModelLoaderRegistry.getModel(POST_MODEL);
			north = ModelLoaderRegistry.getModel(NORTH_MODEL);
			east = ModelLoaderRegistry.getModel(EAST_MODEL);
			south = ModelLoaderRegistry.getModel(SOUTH_MODEL);
			west = ModelLoaderRegistry.getModel(WEST_MODEL);
		}
	}
	
	public static void register(List<EnumTree> variants)
	{
		for (EnumTree variant : variants)
		{
			WattleFenceModel model = new WattleFenceModel();
			((GenesisClient) Genesis.proxy).registerCustomModel("models/block/" + variant.getName() + "_wattle_fence", model);
			//Genesis.proxy.registerCustomModel("models/item/" + variant.getName() + "_wattle_fence", model);

			/*getModels();
			MultiBakedModelWrapper wrapper = new MultiBakedModelWrapper(new WattleFenceModel(), null, post, north, east, south, west);
			wrapper.bake(ModelRotation.X0_Y0, Tessellator.getInstance().getWorldRenderer().getVertexFormat(), bakedTextureGetter);
			
			Genesis.proxy.registerCustomModel(new ModelResourceLocation("models/block/" + variant.getName() + "_wattle_fence", "inventory"));*/
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
		
		for (EnumTree variant : GenesisBlocks.trees.getValidVariants(TreeBlocksAndItems.WATTLE_FENCE))
		{
			IBlockState blockState = GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.WATTLE_FENCE, variant);
			
			ModelResourceLocation loc = ModelHelpers.getLocationFromState(blockState);
			loc = new ModelResourceLocation(loc.getResourceDomain() + ":block/" + loc.getResourcePath() + "#" + loc.getVariant());
			
			textures.addAll(ModelHelpers.getLoadedModel(loc).getTextures());
		}
		
		return textures;
	}

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

	@Override
	public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		getModels();
		MultiBakedModelWrapper wrapper = new MultiBakedModelWrapper(this, null, post, north, east, south, west);
		wrapper.bake(state, format, bakedTextureGetter);
		
		return wrapper;
	}

	@Override
	public IModelState getDefaultState()
	{
        return ModelRotation.X0_Y0;
	}
}
