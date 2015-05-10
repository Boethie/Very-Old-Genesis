package genesis.client;

import genesis.common.*;
import genesis.metadata.*;
import genesis.util.*;
import genesis.util.ReflectionHelper;
import genesis.util.render.ModelHelpers;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.client.registry.*;
import net.minecraftforge.fml.relauncher.*;

public class GenesisClient extends GenesisProxy
{
	private static final Minecraft MC = FMLClientHandler.instance().getClient();
	
	protected Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> mapTESRsToRegister = new HashMap();
	
	public static Minecraft getMC()
	{
		return MC;
	}
	
	public static boolean fancyGraphicsEnabled()
	{
		return MC.isFancyGraphicsEnabled();
	}
	
	private boolean hasInit = false;
	
	@Override
	public void preInit()
	{
        ModelHelpers.preInit();
	}

	@Override
	public void init()
	{
		((IReloadableResourceManager) MC.getResourceManager()).registerReloadListener(new ColorizerDryMoss());
		
		ModelLoaderRegistry.registerLoader(GenesisCustomModelLoader.instance);
        MinecraftForge.EVENT_BUS.register(GenesisCustomModelLoader.instance);
        
        // Gotta register TESRs after Minecraft has initialized, otherwise the vanilla piston TESR crashes.
        for (Map.Entry<Class<? extends TileEntity>, TileEntitySpecialRenderer> entry : mapTESRsToRegister.entrySet())
        {
        	ClientRegistry.bindTileEntitySpecialRenderer(entry.getKey(), entry.getValue());
        }
	}

	@Override
	public void registerBlock(Block block, String name, Class<? extends ItemBlock> clazz)
	{
		super.registerBlock(block, name, clazz);
		
		registerModel(block, name);
	}
	
	public void callSided(SidedFunction sidedFunction)
	{
		sidedFunction.client(this);
	}

	@Override
	public void registerItem(Item item, String name)
	{
		super.registerItem(item, name);

		registerModel(item, name);
	}

	private void registerMetaModels(Block block, IMetadata[] values)
	{
	}

	private void registerModel(Block block, String textureName)
	{
		registerModel(block, 0, textureName);
	}

	private void registerModel(Block block, int metadata, String textureName)
	{
		Item itemFromBlock = Item.getItemFromBlock(block);
		
		if (itemFromBlock != null)
		{
			registerModel(itemFromBlock, metadata, textureName);
		}
	}

	private void registerModel(Item item, String textureName)
	{
		registerModel(item, 0, textureName);
	}

	public void registerModel(Item item, int metadata, String textureName)
	{
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Constants.ASSETS + textureName, "inventory"));
		addVariantName(item, textureName);
	}

	public void registerModelStateMap(Block block, IStateMapper map)
	{
		if (map instanceof StateMap)
		{
			map = new GenesisStateMap((StateMap) map);
		}
		
	    ModelLoader.setCustomStateMapper(block, map);
	}
	
	public void registerCustomModel(String path, IModel model)
	{
		GenesisCustomModelLoader.registerCustomModel(path, model);
	}
	
	public void registerCustomModel(ResourceLocation path, ISmartBlockModel model)
	{
		GenesisCustomModelLoader.registerCustomModel(path, model);
	}
	
	private void addVariantName(Block block, String name)
	{
		addVariantName(Item.getItemFromBlock(block), name);
	}
	
	private void addVariantName(Item item, String name)
	{
		ModelBakery.addVariantName(item, Constants.ASSETS + name);
	}
	
	public void registerTileEntityRenderer(Class<? extends TileEntity> teClass, TileEntitySpecialRenderer renderer)
	{
		mapTESRsToRegister.put(teClass, renderer);
	}
}
