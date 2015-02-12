package genesis.common;

import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GenesisProxy
{
	public void preInit()
	{
	}

	public void init()
	{
	}

	public void postInit()
	{
	}

	public void registerBlock(Block block, String name)
	{
		registerBlock(block, name, ItemBlock.class);
	}

	public void registerBlock(Block block, String name, Class<? extends ItemBlock> clazz)
	{
		registerBlock(block, name, clazz, new Object[0]);
	}

	public void registerBlock(Block block, String name, Class<? extends ItemBlock> clazz, Object... args)
	{
		GameRegistry.registerBlock(block, clazz, name, args);
	}

	/**
	 * Registers a Block to an instance of an ItemBlock.
	 * (To bypass Object... args in GameRegistry.registerBlocks which only works if the passed arguments' types are NOT
	 * subclasses to the constructor's parameter types.
	 */
	public void registerBlockWithItem(Block block, String name, Item item)
	{
		GameRegistry.registerBlock(block, null, name);
		GameRegistry.registerItem(item, name);
		GameData.getBlockItemMap().put(block, item);
	}

	public void registerItem(Item item, String name)
	{
		GameRegistry.registerItem(item, name);
	}
	
	public void registerModel(Item item, int metadata, String textureName)
	{
	}
	
	public void registerModelStateMap(Block block, IStateMapper stateMap)
	{
	}

	public void registerCustomModel(String path, IModel model)
	{
	}

	public void registerCustomModel(ResourceLocation path, ISmartBlockModel wattleFenceModel)
	{
	}
}
