package genesis.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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

	public void registerItem(Item item, String name)
	{
		GameRegistry.registerItem(item, name);
	}
}
