package genesis.client;

import genesis.client.model.FluidModelMapper;
import genesis.client.model.ListedItemMeshDefinition;
import genesis.client.render.CamouflageColorEventHandler;
import genesis.common.*;
import genesis.util.*;
import genesis.util.render.ModelHelpers;
import genesis.client.sound.music.MusicEventHandler;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.color.*;
import net.minecraft.client.resources.*;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.*;

public class GenesisClient extends GenesisProxy
{
	private static final Minecraft MC = FMLClientHandler.instance().getClient();
	
	private List<ClientFunction> preInitClientCalls = new ArrayList<>();

	private List<Pair<IBlockColor, Block[]>> blockColorsList = new ArrayList<>();
	private List<Pair<IItemColor, Item[]>> itemColorsList = new ArrayList<>();
	
	public static Minecraft getMC()
	{
		return MC;
	}
	
	@Override
	public void clientPreInitCall(ClientFunction function)
	{
		preInitClientCalls.add(function);
	}
	
	@Override
	public void preInit()
	{
		for (ClientFunction call : preInitClientCalls)
		{
			call.apply(this);
		}
		
		GenesisBlocks.preInitClient();
		GenesisEntities.registerEntityRenderers();
		
		// This should be called as late as possible in preInit.
		ModelHelpers.preInit();
	}
	
	@Override
	public void init()
	{
		//Music Event Handler
		MinecraftForge.EVENT_BUS.register(new MusicEventHandler());
		
		MinecraftForge.EVENT_BUS.register(new CamouflageColorEventHandler());
		
		GenesisParticles.createParticles();
		
		((IReloadableResourceManager) MC.getResourceManager()).registerReloadListener(new ColorizerDryMoss());
		
		GenesisBlocks.initClient();
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
	public void callClient(ClientFunction function)
	{
		function.apply(this);
	}
	
	@Override
	public void callServer(ServerFunction function)
	{
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
	
	public void registerModel(Block block, String variantName)
	{
		registerModel(block, 0, variantName);
	}
	
	@Override
	public ModelResourceLocation getItemModelLocation(String variantName)
	{
		return new ModelResourceLocation(Constants.ASSETS_PREFIX + variantName, "inventory");
	}
	
	@Override
	public void registerModel(Item item, int metadata, String variantName)
	{
		ModelLoader.setCustomModelResourceLocation(item, metadata, getItemModelLocation(variantName));
		addVariantName(item, variantName);
	}
	
	private void registerModel(Item item, String variantName)
	{
		registerModel(item, 0, variantName);
	}
	
	@Override
	public void registerModel(Block block, int metadata, String variantName)
	{
		Item item = Item.getItemFromBlock(block);
		
		if (item != null)
		{
			registerModel(item, metadata, variantName);
		}
	}
	
	@Override
	public void registerModel(Item item, ListedItemMeshDefinition definition)
	{
		ModelLoader.setCustomMeshDefinition(item, definition);
		
		for (String variant : definition.getVariants())
		{
			addVariantName(item, variant);
		}
	}
	
	public void addVariantName(Block block, String name)
	{
		addVariantName(Item.getItemFromBlock(block), name);
	}
	
	public void addVariantName(Item item, String name)
	{
		ModelBakery.registerItemVariants(item, new ResourceLocation(Constants.ASSETS_PREFIX + name));
	}
	
	public void registerColorer(IBlockColor colorer, Block... blocks)
	{
		blockColorsList.add(Pair.of(colorer, blocks));
	}
	
	public void registerColorer(IItemColor colorer, Item... blocks)
	{
		itemColorsList.add(Pair.of(colorer, blocks));
	}
}
