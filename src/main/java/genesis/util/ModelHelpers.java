package genesis.util;

import genesis.client.GenesisCustomModelLoader;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ModelHelpers
{
	public static ModelLoader forgeModelLoader;
	public static BlockModelShapes modelShapes;
	public static Map<IBlockState, ModelResourceLocation> blockResourceMap;
	public static Class<? extends IModel> classVanillaModelWrapper;
	
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
	
	public static BlockModelShapes getBlockModelShapes()
	{
		if (modelShapes == null)
		{
			modelShapes = (BlockModelShapes) ObfuscationReflectionHelper.getPrivateValue(ModelBakery.class, getModelLoader(), "blockModelShapes", "field_177610_k");
		}
		
		return modelShapes;
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
		IModel childModel = ModelLoaderRegistry.getModel(loc);
		GenesisCustomModelLoader.acceptNothing = false;
		
		return childModel;
	}
	
	public static ModelBlock getModelBlock(ResourceLocation loc)
	{
		return getModelBlock(getLoadedModel(loc));
	}
}
