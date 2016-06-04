package genesis.common;

import genesis.client.model.ListedItemMeshDefinition;
import genesis.util.functional.ClientFunction;
import genesis.util.functional.ServerFunction;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.registry.*;

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
	
	protected void doRegistrationCallback(Object object)
	{
		if (object instanceof IRegistrationCallback)
		{
			((IRegistrationCallback) object).onRegistered();
		}
	}
	
	public void registerItem(Item item, ResourceLocation name, boolean doModel)
	{
		GameRegistry.register(item, name);
		doRegistrationCallback(item);
	}
	
	public void registerItem(Item item, ResourceLocation name)
	{
		registerItem(item, name, true);
	}
	
	public void registerBlock(Block block, Item item, ResourceLocation name, boolean doModel)
	{
		GameRegistry.register(block, name);
		
		if (item != null)
			registerItem(item, name);
		
		doRegistrationCallback(block);
	}
	
	public void registerBlock(Block block, ResourceLocation name, boolean doModel)
	{
		registerBlock(block, new ItemBlock(block), name, doModel);
	}
	
	public void registerBlock(Block block, ResourceLocation name)
	{
		registerBlock(block, name, true);
	}
	
	public void registerBlock(Block block, Item item, ResourceLocation name)
	{
		registerBlock(block, item, name, true);
	}
	
	public void registerFluidBlock(BlockFluidBase block, ResourceLocation name)
	{
		registerBlock(block, null, name);
	}
	
	public void registerModel(Item item, int metadata, ResourceLocation variantName)
	{
	}
	
	public void registerModel(Block item, int metadata, ResourceLocation variantName)
	{
	}
	
	public void registerModel(Item item, ListedItemMeshDefinition definition)
	{
	}
	
	public void callClient(ClientFunction function)
	{
	}
	
	public void callServer(ServerFunction function)
	{
		function.apply(this);
	}
}
