package genesis.client;

import genesis.client.debug.GenesisDebugOverlay;
import genesis.client.model.*;
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

	private List<Pair<IBlockColor, Block[]>> blockColorsList = new ArrayList<>();
	private List<Pair<IItemColor, Item[]>> itemColorsList = new ArrayList<>();
	
	public static Minecraft getMC()
	{
		return MC;
	}
	
	@Override
	public void preInit()
	{
		GenesisBlocks.preInitClient();
		GenesisItems.preInitClient();
		
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
		GenesisItems.initClient();
		
		GenesisDebugOverlay.register();
	}
	
	@Override
	public void registerBlock(Block block, Item item, ResourceLocation name, boolean doModel)
	{
		super.registerBlock(block, item, name, doModel);
		
		if (doModel)
		{
			registerModel(block, name);
		}
	}
	
	@Override
	public void registerFluidBlock(BlockFluidBase block, ResourceLocation name)
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
	public void registerItem(Item item, ResourceLocation name, boolean doModel)
	{
		super.registerItem(item, name, doModel);
		
		if (doModel)
		{
			registerModel(item, name);
		}
	}
	
	public void registerModel(Block block, ResourceLocation variantName)
	{
		registerModel(block, 0, variantName);
	}
	
	public ModelResourceLocation getItemModelLocation(ResourceLocation variantName)
	{
		return new ModelResourceLocation(variantName, "inventory");
	}
	
	@Override
	public void registerModel(Item item, int metadata, ResourceLocation variantName)
	{
		ModelLoader.setCustomModelResourceLocation(item, metadata, getItemModelLocation(variantName));
		addVariantName(item, variantName);
	}
	
	private void registerModel(Item item, ResourceLocation variantName)
	{
		registerModel(item, 0, variantName);
	}
	
	@Override
	public void registerModel(Block block, int metadata, ResourceLocation variantName)
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
		
		for (ResourceLocation variant : definition.getVariants())
		{
			addVariantName(item, variant);
		}
	}
	
	public void addVariantName(Block block, ResourceLocation name)
	{
		addVariantName(Item.getItemFromBlock(block), name);
	}
	
	public void addVariantName(Item item, ResourceLocation name)
	{
		ModelBakery.registerItemVariants(item, name);
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
