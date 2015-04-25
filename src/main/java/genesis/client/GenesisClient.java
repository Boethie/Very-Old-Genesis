package genesis.client;

import genesis.common.*;
import genesis.metadata.*;
import genesis.util.*;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GenesisClient extends GenesisProxy
{
	private static final Minecraft MC = FMLClientHandler.instance().getClient();
	
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
	public void init()
	{
		((IReloadableResourceManager) MC.getResourceManager()).registerReloadListener(new ColorizerDryMoss());
		
		ModelLoaderRegistry.registerLoader(GenesisCustomModelLoader.instance);
        MinecraftForge.EVENT_BUS.register(GenesisCustomModelLoader.instance);

		registerModelStateMap(GenesisBlocks.prototaxites, new StateMap.Builder().addPropertiesToIgnore(BlockCactus.AGE).build());
	}

	@Override
	public void registerBlock(Block block, String name, Class<? extends ItemBlock> clazz)
	{
		super.registerBlock(block, name, clazz);
		
		registerModel(block, name);
	}
	
	public void callClientOnly(ClientOnlyFunction clientOnlyFunction)
	{
		clientOnlyFunction.apply(this);
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
