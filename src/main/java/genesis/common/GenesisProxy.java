package genesis.common;

import genesis.util.SidedFunction;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GenesisProxy
{
	protected List<SidedFunction> preInitCalls = new ArrayList<SidedFunction>();
	
	public void registerPreInitCall(SidedFunction call)
	{
		preInitCalls.add(call);
	}
	
	public void preInit()
	{
		for (SidedFunction call : preInitCalls)
		{
			call.server(this);
		}
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
		GameRegistry.registerBlock(block, clazz, name);
	}
	
	public void registerFluidBlock(BlockFluidBase block, String name){
		registerBlock(block, name);
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

	public void callSided(SidedFunction sidedFunction)
	{
		sidedFunction.server(this);
	}
}
