package genesis.client;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisProxy;
import genesis.item.IMetadata;
import genesis.item.ItemGenesisMetadata;
import genesis.util.Constants;
import genesis.util.Metadata;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GenesisClient extends GenesisProxy
{
	private static final Minecraft MC = FMLClientHandler.instance().getClient();
	private final ArrayList<ItemTexture> itemTextures = new ArrayList<ItemTexture>();
	private boolean hasInit = false;

	@Override
	public void init()
	{
		BlockModelShapes blockModelShapes = MC.getBlockRendererDispatcher().getBlockModelShapes();
		blockModelShapes.registerBlockWithStateMapper(GenesisBlocks.prototaxites, new StateMap.Builder().addPropertiesToIgnore(BlockCactus.AGE).build());
		// TODO: Cannot add prefix "genesis" when registering variants!
		// blockModelShapes.registerBlockWithStateMapper(GenesisBlocks.coral, new StateMap.Builder().setProperty(BlockCoral.VARIANT).build());

		hasInit = true;

		Iterator<ItemTexture> iterator = itemTextures.iterator();
		while (iterator.hasNext())
		{
			ItemTexture texture = iterator.next();
			registerModel(texture.item, texture.metadata, texture.name);
			iterator.remove();
		}
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
					if (Enum.class.isAssignableFrom(argClass) && IMetadata.class.isAssignableFrom(argClass))
					{
						registerMetaModels(block, (IMetadata[]) argClass.getEnumConstants());
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

		if (item instanceof ItemGenesisMetadata)
		{
			ArrayList<IMetadata> lookup = Metadata.getLookup(((ItemGenesisMetadata) item).getMetaClass());

			for (int metadata = 0; metadata < lookup.size(); metadata++)
			{
				String textureName = name + "_" + lookup.get(metadata).getName();
				registerModel(item, metadata, textureName);
				addVariantName(item, textureName);
			}
		}
		else
		{
			registerModel(item, name);
		}
	}

	private void registerMetaModels(Block block, IMetadata[] values)
	{
		for (int metadata = 0; metadata < values.length; metadata++)
		{
			String textureName = values[metadata].getName();
			registerModel(block, metadata, textureName);
			addVariantName(block, textureName);
		}
	}

	private void registerModel(Block block, String textureName)
	{
		registerModel(block, 0, textureName);
	}

	private void registerModel(Block block, int metadata, String textureName)
	{
		registerModel(Item.getItemFromBlock(block), metadata, textureName);
	}

	private void registerModel(Item item, String textureName)
	{
		registerModel(item, 0, textureName);
	}

	private void registerModel(Item item, int metadata, String textureName)
	{
		if (!hasInit)
		{
			itemTextures.add(new ItemTexture(item, metadata, textureName));
		}
		else
		{
			MC.getRenderItem().getItemModelMesher().register(item, metadata, new ModelResourceLocation(Constants.ASSETS + textureName, "inventory"));
		}
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
}
