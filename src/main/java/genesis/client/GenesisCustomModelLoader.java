package genesis.client;

import java.util.HashMap;
import java.util.Map.Entry;

import genesis.util.Constants;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GenesisCustomModelLoader implements ICustomModelLoader
{
	public static GenesisCustomModelLoader instance = new GenesisCustomModelLoader();
	public static boolean acceptNothing = false;

	protected HashMap<String, IModel> modelMap = new HashMap<String, IModel>();
	protected HashMap<ResourceLocation, ISmartBlockModel> smartModels = new HashMap<ResourceLocation, ISmartBlockModel>();
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) { }

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		
		
		if (acceptNothing)
		{
			return false;
		}
		
		if (Constants.MOD_ID.equals(modelLocation.getResourceDomain()))
		{
			if (modelMap.keySet().contains(modelLocation.getResourcePath()))
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation)
	{
		if (modelLocation.getResourceDomain().equals(Constants.MOD_ID))
		{
			return modelMap.get(modelLocation.getResourcePath());
		}
		
		return null;
	}
	
	public static void registerCustomModel(String path, IModel model)
	{
		instance.modelMap.put(path, model);
	}
	
	public static void registerCustomModel(ResourceLocation loc, ISmartBlockModel model)
	{
		instance.smartModels.put(loc, model);
	}

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event)
    {
    	for (Entry<ResourceLocation, ISmartBlockModel> entry : smartModels.entrySet())
    	{
    		ISmartBlockModel model = entry.getValue();
    		ResourceLocation resource = entry.getKey();
            event.modelRegistry.putObject(resource, model);
    	}
    }
}
