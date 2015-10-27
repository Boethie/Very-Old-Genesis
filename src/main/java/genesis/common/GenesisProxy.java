package genesis.common;

import java.util.*;

import genesis.client.model.ListedItemMeshDefinition;
import genesis.util.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.registry.*;

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
	
	protected void doRegistrationCallback(Object object)
	{
		if (object instanceof IRegistrationCallback)
		{
			((IRegistrationCallback) object).onRegistered();
		}
	}
	
	public void registerBlock(Block block, String name, boolean doModel)
	{
		registerBlock(block, name, ItemBlock.class, doModel);
	}
	
	public void registerBlock(Block block, String name)
	{
		registerBlock(block, name, true);
	}
	
	public void registerBlock(Block block, String name, Class<? extends ItemBlock> clazz, boolean doModel)
	{
		GameRegistry.registerBlock(block, clazz, name);
		doRegistrationCallback(block);
	}
	
	public void registerBlock(Block block, String name, Class<? extends ItemBlock> clazz)
	{
		registerBlock(block, name, clazz, true);
	}
	
	public void registerFluidBlock(BlockFluidBase block, String name)
	{
		registerBlock(block, name);
	}
	
	/**
	 * Registers a Block to an instance of an ItemBlock.
	 * (To bypass Object... args in GameRegistry.registerBlocks which only works if the passed arguments' types are not
	 * subclasses to the constructor's parameter types.
	 * This method does not register an item model.
	 */
	public void registerBlockWithItem(Block block, String name, Item item)
	{
		GameRegistry.registerBlock(block, null, name);
		GameRegistry.registerItem(item, name);
		GameData.getBlockItemMap().put(block, item);
		doRegistrationCallback(block);
	}
	
	public void registerItem(Item item, String name, boolean doModel)
	{
		GameRegistry.registerItem(item, name);
		doRegistrationCallback(item);
	}
	
	public void registerItem(Item item, String name)
	{
		registerItem(item, name, true);
	}
	
	public ResourceLocation getItemModelLocation(String textureName)
	{
		return null;
	}
	
	public void registerModel(Item item, int metadata, String textureName)
	{
	}
	
	public void registerModel(Block item, int metadata, String textureName)
	{
	}
	
	public void registerModel(Item item, ListedItemMeshDefinition definition)
	{
	}
	
	public void callSided(SidedFunction sidedFunction)
	{
		sidedFunction.server(this);
	}
}
