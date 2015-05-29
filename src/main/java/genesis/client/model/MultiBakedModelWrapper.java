package genesis.client.model;

import genesis.client.GenesisCustomModelLoader;
import genesis.util.render.ModelHelpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class MultiBakedModelWrapper implements IFlexibleBakedModel, ISmartBlockModel, ISmartItemModel
{
	IBlockState state = null;
	IMultiBakedModelOwner owner;
	IModel mainModel;
	IModel[] otherModels;
	IFlexibleBakedModel mainBakedModel;
	IFlexibleBakedModel[] otherBakedModels;
	TextureAtlasSprite texture;
	
	public MultiBakedModelWrapper(IMultiBakedModelOwner owner, TextureAtlasSprite texture, IModel main, IModel... others)
	{
		this.owner = owner;
		this.mainModel = main;
		this.otherModels = others;
		this.texture = texture;
	}
	
	public MultiBakedModelWrapper(IBlockState state, IMultiBakedModelOwner owner, TextureAtlasSprite texture, IModel main, IModel... others)
	{
		this(owner, texture, main, others);
		
		this.state = state;
	}
	
	protected IFlexibleBakedModel resolveTextures(IModel model, IModelState modelState, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		IFlexibleBakedModel out;
		
		if (state != null)
		{
			ModelResourceLocation loc = ModelHelpers.getLocationFromState(state);
			loc = new ModelResourceLocation(loc.getResourceDomain() + ":block/" + loc.getResourcePath() + "#" + loc.getVariant());
			
			ModelBlock childModelBlock = ModelHelpers.getModelBlock(ModelHelpers.getLoadedModel(loc));
			
			ModelBlock toBakeBlock = ModelHelpers.getModelBlock(model);
			
			Set<Entry<String, String>> entries = (Set<Entry<String, String>>) childModelBlock.textures.entrySet();
			
			for (Entry<String, String> entry : entries)
			{
				toBakeBlock.textures.put(entry.getKey(), entry.getValue());
			}

			out = model.bake(modelState, format, bakedTextureGetter);
		}
		else
		{
			out = model.bake(modelState, format, bakedTextureGetter);
		}
		
		return out;
	}
	
	IModelState modelState;
	VertexFormat format;
	Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
	
	public void bake(IModelState modelState, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		this.modelState = modelState;
		this.format = format;
		this.bakedTextureGetter = bakedTextureGetter;
		
		mainBakedModel = resolveTextures(mainModel, modelState, format, bakedTextureGetter);
		
		otherBakedModels = new IFlexibleBakedModel[otherModels.length];
		
		for (int i = 0; i < otherModels.length; i++)
		{
			otherBakedModels[i] = resolveTextures(otherModels[i], modelState, format, bakedTextureGetter);
		}
	}
	
	@Override
	public boolean isAmbientOcclusion()
	{
		return mainBakedModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return mainBakedModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return mainBakedModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getTexture()
	{
		if (texture != null)
		{
			return texture;
		}
		
		return mainBakedModel.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return mainBakedModel.getItemCameraTransforms();
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing side)
	{
		if (state != null)
		{
			ArrayList<BakedQuad> quads = new ArrayList<BakedQuad>();
			quads.addAll(mainBakedModel.getFaceQuads(side));
			
			int i = 0;
			
			for (IFlexibleBakedModel bakedModel : otherBakedModels)
			{
				if (owner.shouldRenderPart(state, bakedModel, i))
				{
					quads.addAll(bakedModel.getFaceQuads(side));
				}
				
				i++;
			}
			
			return quads;
		}
		
		return new ArrayList<BakedQuad>();
	}

	@Override
	public List<BakedQuad> getGeneralQuads()
	{
		if (state != null)
		{
			ArrayList<BakedQuad> quads = new ArrayList<BakedQuad>();
			quads.addAll(mainBakedModel.getGeneralQuads());
			
			int i = 0;
			
			for (IFlexibleBakedModel bakedModel : otherBakedModels)
			{
				if (owner.shouldRenderPart(state, bakedModel, i))
				{
					quads.addAll(bakedModel.getGeneralQuads());
				}
				
				i++;
			}
			
			return quads;
		}
		
		return new ArrayList<BakedQuad>();
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state)
	{
		MultiBakedModelWrapper stateModel = new MultiBakedModelWrapper(state, owner, texture, mainModel, otherModels);
		stateModel.bake(modelState, format, bakedTextureGetter);
		
		return stateModel;
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack)
	{
		ItemBlock itemBlock = (ItemBlock) stack.getItem();
		
		return new MultiBakedModelWrapper(itemBlock.block.getDefaultState(), owner, texture, mainModel, otherModels);
	}

	@Override
	public VertexFormat getFormat()
	{
		return mainBakedModel.getFormat();
	}
}
