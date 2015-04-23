package genesis.client;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisProxy;
import genesis.metadata.IMetadata;
import genesis.util.Constants;
import genesis.util.GenesisStateMap;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GenesisClient extends GenesisProxy
{
	private static final Minecraft MC = FMLClientHandler.instance().getClient();
	
	public static Minecraft getMC()
	{
		return MC;
	}
	
	private boolean hasInit = false;

	@Override
	public void init()
	{
		((IReloadableResourceManager) MC.getResourceManager()).registerReloadListener(new ColorizerDryMoss());
		
		ModelLoaderRegistry.registerLoader(GenesisCustomModelLoader.instance);
        MinecraftForge.EVENT_BUS.register(GenesisCustomModelLoader.instance);
	}

	@Override
	public void registerBlock(Block block, String name, Class<? extends ItemBlock> clazz, Object... args)
	{
		super.registerBlock(block, name, clazz, args);

		if ((args != null) && (args.length > 0))
		{
			for (Object arg : args)
			{
				if (arg instanceof Class)
				{
					Class argClass = (Class) arg;
					if (IMetadata.class.isAssignableFrom(argClass))
					{
						IMetadata[] values = (IMetadata[]) argClass.getEnumConstants();
						for (int metadata = 0; metadata < values.length; metadata++)
						{
							String textureName = values[metadata].getName();

							registerModel(block, metadata, textureName);
							addVariantName(block, textureName);
						}
					}
				}
			}
		}
		else
		{
			registerModel(block, name);
		}
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

	@Override
	public void registerModelStateMap(Block block, IStateMapper map)
	{
		if (map instanceof StateMap)
		{
			map = new GenesisStateMap((StateMap) map);
		}
		
	    ModelLoader.setCustomStateMapper(block, map);
	}
	
	@Override
	public void registerCustomModel(String path, IModel model)
	{
		GenesisCustomModelLoader.registerCustomModel(path, model);
	}
	
	@Override
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

	private class ItemTexture
	{
		private final Item item;
		private final int metadata;
		private final String name;

		private ItemTexture(Item item, int metadata, String name)
		{
			this.item = item;
			this.metadata = metadata;
			this.name = name;
		}
	}

	private class BlockStateMap
	{
		private final Block block;
		private final IStateMapper map;

		private BlockStateMap(Block block, IStateMapper map)
		{
			this.block = block;
			this.map = map;
		}
	}
}
