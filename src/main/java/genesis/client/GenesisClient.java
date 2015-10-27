package genesis.client;

import genesis.client.model.FluidModelMapper;
import genesis.client.model.ListedItemMeshDefinition;
import genesis.common.*;
import genesis.util.*;
import genesis.util.render.ModelHelpers;
import genesis.client.sound.music.MusicEventHandler;

import java.util.*;

import com.google.common.collect.Maps;

import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.client.registry.*;

public class GenesisClient extends GenesisProxy
{
	private static final Minecraft MC = FMLClientHandler.instance().getClient();
	
	protected Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> mapTESRsToRegister = Maps.newHashMap();
	
	public static Minecraft getMC()
	{
		return MC;
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
		
		//Music Event Handler
		MinecraftForge.EVENT_BUS.register(new MusicEventHandler());
		
		// Gotta register TESRs after Minecraft has initialized, otherwise the vanilla piston TESR crashes.
		for (Map.Entry<Class<? extends TileEntity>, TileEntitySpecialRenderer> entry : mapTESRsToRegister.entrySet())
		{
			ClientRegistry.bindTileEntitySpecialRenderer(entry.getKey(), entry.getValue());
		}
		
		GenesisEntities.registerEntityRenderers();
		
		GenesisParticles.createParticles();
	}
	
	@Override
	public void registerBlock(Block block, String name, Class<? extends ItemBlock> clazz, boolean doModel)
	{
		super.registerBlock(block, name, clazz, doModel);
		
		if (doModel)
		{
			registerModel(block, name);
		}
	}
	
	@Override
	public void registerBlockWithItem(Block block, String name, Item item)
	{
		super.registerBlockWithItem(block, name, item);
	}
	
	@Override
	public void registerFluidBlock(BlockFluidBase block, String name)
	{
		super.registerFluidBlock(block, name);
		
		FluidModelMapper.registerFluid(block);
	}
	
	@Override
	public void callSided(SidedFunction sidedFunction)
	{
		sidedFunction.client(this);
	}
	
	@Override
	public void registerItem(Item item, String name, boolean doModel)
	{
		super.registerItem(item, name, doModel);
		
		if (doModel)
		{
			registerModel(item, name);
		}
	}
	
	public void registerModel(Block block, String textureName)
	{
		registerModel(block, 0, textureName);
	}
	
	@Override
	public ModelResourceLocation getItemModelLocation(String textureName)
	{
		return new ModelResourceLocation(Constants.ASSETS_PREFIX + textureName, "inventory");
	}
	
	@Override
	public void registerModel(Item item, int metadata, String textureName)
	{
		ModelLoader.setCustomModelResourceLocation(item, metadata, getItemModelLocation(textureName));
		addVariantName(item, textureName);
	}
	
	private void registerModel(Item item, String textureName)
	{
		registerModel(item, 0, textureName);
	}
	
	@Override
	public void registerModel(Block block, int metadata, String textureName)
	{
		Item item = Item.getItemFromBlock(block);
		
		if (item != null)
		{
			registerModel(item, metadata, textureName);
		}
	}
	
	@Override
	public void registerModel(Item item, ListedItemMeshDefinition definition)
	{
		ModelLoader.setCustomMeshDefinition(item, definition);
		definition.getVariants().forEach(variant -> addVariantName(item, variant));
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
	
	public void addVariantName(Block block, String name)
	{
		addVariantName(Item.getItemFromBlock(block), name);
	}
	
	public void addVariantName(Item item, String name)
	{
		ModelBakery.addVariantName(item, Constants.ASSETS_PREFIX + name);
	}
	
	public void registerTileEntityRenderer(Class<? extends TileEntity> teClass, TileEntitySpecialRenderer renderer)
	{
		mapTESRsToRegister.put(teClass, renderer);
	}
}
