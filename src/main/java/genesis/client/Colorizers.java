package genesis.client;

import genesis.block.BlockMoss;
import genesis.block.tileentity.BlockGenesisFlowerPot;
import genesis.block.tileentity.BlockGenesisFlowerPot.IFlowerPotPlant;
import genesis.block.tileentity.TileEntityGenesisFlowerPot;
import genesis.util.WorldUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;

public class Colorizers
{
	/* ======================== Blocks ======================== */
	public static final IBlockColor GRASS_BLOCK = (s, w, p, t) ->
			w != null && p != null
					? BiomeColorHelper.getGrassColorAtPos(w, p)
					: ColorizerGrass.getGrassColor(0.5, 1);
	
	public static final IBlockColor LEAVES_BLOCK = (s, w, p, t) ->
			w != null && p != null
					? BiomeColorHelper.getFoliageColorAtPos(w, p)
					: ColorizerFoliage.getFoliageColorBasic();
	
	public static final IBlockColor MOSS_BLOCK = (state, world, pos, renderPass) ->
	{
		if (world == null || pos == null || renderPass != 1)
			if (renderPass == 1)
				return ColorizerGrass.getGrassColor(0.5, 1);
			else
				return 0xFFFFFF;
		
		int color = BiomeColorHelper.getGrassColorAtPos(world, pos);
		
		int r = (color & 16711680) >> 16;
		int g = (color & 65280) >> 8;
		int b = color & 255;
		
		float avgStage = 0;
		float stageSamples = 0;
		
		for (BlockPos checkPos : WorldUtils.getArea(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
		{
			IBlockState checkState = world.getBlockState(checkPos);
			
			if (checkState.getBlock() == state.getBlock())
			{
				avgStage += checkState.getValue(BlockMoss.STAGE);
				stageSamples++;
			}
		}
		
		avgStage /= stageSamples;
		avgStage /= BlockMoss.STAGE_LAST;
		avgStage = MathHelper.clamp_float(avgStage, 0, 1);
		
		BiomeGenBase biome = world.getBiomeGenForCoords(pos);
		float temperature = MathHelper.clamp_float(biome.getFloatTemperature(pos), 0, 1);
		float humidity = MathHelper.clamp_float(biome.getRainfall(), 0, 1);
		
		int dryColor = biome.getModdedBiomeGrassColor(ColorizerDryMoss.getColor(temperature, humidity));
		int toR = (dryColor & 16711680) >> 16;
		int toG = (dryColor & 65280) >> 8;
		int toB = dryColor & 255;
		
		float amount = 1 - avgStage;
		
		r = r + (int) ((toR - r) * amount);	// Interpolate between the two color textures.
		g = g + (int) ((toG - g) * amount);
		b = b + (int) ((toB - b) * amount);
		
		color = ((r & 255) << 16) |
				((g & 255) << 8) |
				(b & 255);
		
		return color;
	};
	
	public static final IBlockColor FLOWER_POT_BLOCK = (s, w, p, t) ->
	{
		TileEntityGenesisFlowerPot pot = BlockGenesisFlowerPot.getTileEntity(w, p);
		
		if (pot != null)
		{
			IFlowerPotPlant customs = BlockGenesisFlowerPot.getPlantCustoms(pot.getContents());
			
			if (customs != null)
				return customs.getColorMultiplier(pot.getContents(), w, p);
		}
		
		return 0xFFFFFF;
	};
	
	/* ======================== Items ======================== */
	public static final IItemColor GRASS_ITEM = (s, t) -> ColorizerGrass.getGrassColor(0.5, 1);
	public static final IItemColor LEAVES_ITEM = (s, t) -> ColorizerGrass.getGrassColor(0.5, 1);
}
