package genesis.client;

import genesis.client.model.FluidModelMapper;
import genesis.client.model.ListedItemMeshDefinition;
import genesis.common.*;
import genesis.util.*;
import genesis.util.render.ModelHelpers;
import genesis.client.sound.music.MusicEventHandler;

import java.util.*;

import com.google.common.collect.Lists;

import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.client.registry.*;

public class GenesisClient extends GenesisProxy
{
	private static final Minecraft MC = FMLClientHandler.instance().getClient();
	
	private static final class TESREntry<T extends TileEntity>
	{
		final Class<T> type;
		final TileEntitySpecialRenderer<T> renderer;
		
		private TESREntry(Class<T> type, TileEntitySpecialRenderer<T> renderer)
		{
			this.type = type;
			this.renderer = renderer;
		}
		
		private void register()
		{
			ClientRegistry.bindTileEntitySpecialRenderer(type, renderer);
		}
	}
	
	protected List<TESREntry<?>> tileEntityRenderers = Lists.newArrayList();
	
	public static Minecraft getMC()
	{
		return MC;
	}
	
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
		
		//Music Event Handler
		MinecraftForge.EVENT_BUS.register(new MusicEventHandler());
		
		// Gotta register TESRs after Minecraft has initialized, otherwise the vanilla piston TESR crashes.
		for (TESREntry<?> entry : tileEntityRenderers)
		{
			entry.register();
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
		
		for (String variant : definition.getVariants())
		{
			addVariantName(item, variant);
		}
	}
	
	public void registerModelStateMap(Block block, IStateMapper map)
	{
		if (map instanceof StateMap)
		{
			map = new GenesisStateMap((StateMap) map);
		}
		
		ModelLoader.setCustomStateMapper(block, map);
	}
	
	public void addVariantName(Block block, String name)
	{
		addVariantName(Item.getItemFromBlock(block), name);
	}
	
	public void addVariantName(Item item, String name)
	{
		ModelBakery.addVariantName(item, Constants.ASSETS_PREFIX + name);
	}
	
	public <T extends TileEntity> void registerTileEntityRenderer(Class<T> teClass, TileEntitySpecialRenderer<T> renderer)
	{
		tileEntityRenderers.add(new TESREntry<T>(teClass, renderer));
	}
}
