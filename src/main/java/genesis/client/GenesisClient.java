package genesis.client;

import genesis.client.model.FluidModel;
import genesis.common.GenesisEntities;
import genesis.common.GenesisProxy;
import genesis.metadata.IMetadata;
import genesis.util.Constants;
import genesis.util.GenesisStateMap;
import genesis.util.SidedFunction;
import genesis.util.render.ModelHelpers;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import com.google.common.collect.Maps;

public class GenesisClient extends GenesisProxy
{
	private static final Minecraft MC = FMLClientHandler.instance().getClient();
	
	protected Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> mapTESRsToRegister = Maps.newHashMap();
	
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
		for (SidedFunction call : preInitCalls)
		{
			call.client(this);
		}
		
		// This should be called as late as possible in preInit.
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
		
		GenesisEntities.registerEntityRenderers();
		
		GenesisParticles.createParticles();
	}

	@Override
	public void registerBlock(Block block, String name, Class<? extends ItemBlock> clazz)
	{
		super.registerBlock(block, name, clazz);
		
		registerModel(block, name);
	}
	
	@Override
	public void registerFluidBlock(BlockFluidBase block, String name)
	{
		registerBlock(block, name);
		FluidModel.registerFluid(block);
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
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Constants.ASSETS_PREFIX + textureName, "inventory"));
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
		ModelBakery.addVariantName(item, Constants.ASSETS_PREFIX + name);
	}
	
	public void registerTileEntityRenderer(Class<? extends TileEntity> teClass, TileEntitySpecialRenderer renderer)
	{
		mapTESRsToRegister.put(teClass, renderer);
	}
}
