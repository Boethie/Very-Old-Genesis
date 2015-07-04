package genesis.client.model;

import genesis.util.Constants;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;

public class FluidModel extends StateMapperBase implements ItemMeshDefinition
{
	private static final FluidModel INSTANCE = new FluidModel();
	private static final HashMap<Block, ModelResourceLocation> BLOCKS = new HashMap<Block, ModelResourceLocation>();
	private static final HashMap<Item, ModelResourceLocation> ITEMS = new HashMap<Item, ModelResourceLocation>();
	
	public static void registerFluid(BlockFluidBase block)
	{
		Item item = Item.getItemFromBlock(block);
		ModelResourceLocation modelLocation = new ModelResourceLocation(Constants.ASSETS_PREFIX + "fluid", block.getFluid().getName());
		
		ModelLoader.setCustomStateMapper(block, INSTANCE);
		ModelLoader.setCustomMeshDefinition(item, INSTANCE);
		
		BLOCKS.put(block, modelLocation);
		ITEMS.put(item, modelLocation);
	}
	
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		return BLOCKS.get(state.getBlock());
	}
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return ITEMS.get(stack.getItem());
	}
}
