package genesis.client.model;

import genesis.util.Constants;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;

public class FluidModelMapper extends StateMapperBase
{
	private static final FluidModelMapper INSTANCE = new FluidModelMapper();
	private static final HashMap<Block, ModelResourceLocation> BLOCKS = new HashMap<Block, ModelResourceLocation>();
	
	public static void registerFluid(BlockFluidBase block)
	{
		ModelResourceLocation modelLocation = new ModelResourceLocation(Constants.ASSETS_PREFIX + "fluid", new ResourceLocation(block.getFluid().getName()).getResourcePath());
		
		ModelLoader.setCustomStateMapper(block, INSTANCE);
		
		BLOCKS.put(block, modelLocation);
	}
	
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		return BLOCKS.get(state.getBlock());
	}
}
