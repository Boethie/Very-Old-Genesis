package genesis.util;

import genesis.client.GenesisCustomModelLoader;

import java.lang.reflect.Field;

import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ModelHelpers
{
	public static ModelBlock getModelBlock(IModel model)
	{
		if (GenesisCustomModelLoader.vanillaModelWrapperClass.isInstance(model))
		{
			Field modelField = ReflectionHelper.findField(GenesisCustomModelLoader.vanillaModelWrapperClass, "model");
			
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
}
