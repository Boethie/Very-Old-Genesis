package genesis.client;

import genesis.block.BlockMoss;
import genesis.block.tileentity.BlockGenesisFlowerPot;
import genesis.block.tileentity.BlockGenesisFlowerPot.IFlowerPotPlant;
import genesis.block.tileentity.TileEntityGenesisFlowerPot;
import genesis.util.GenesisMath;
import genesis.util.WorldUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;

public class Colorizers
{
	/* ======================== Blocks ======================== */
	public static final IBlockColor BLOCK_GRASS = (s, w, p, t) ->
			w != null && p != null
					? BiomeColorHelper.getGrassColorAtPos(w, p)
					: ColorizerGrass.getGrassColor(0.5, 1);
	
	public static final IBlockColor BLOCK_LEAVES = (s, w, p, t) ->
			w != null && p != null
					? BiomeColorHelper.getFoliageColorAtPos(w, p)
					: ColorizerFoliage.getFoliageColorBasic();
	
	public static final IBlockColor BLOCK_MOSS = (state, world, pos, renderPass) ->
	{
		if (world == null || pos == null || renderPass != 1)
			if (renderPass == 1)
				return ColorizerGrass.getGrassColor(0.5, 1);
			else
				return 0xFFFFFF;
		
		int grassColor = BiomeColorHelper.getGrassColorAtPos(world, pos);
		
		int grassR = (grassColor >> 16) & 0xFF;
		int grassG = (grassColor >> 8) & 0xFF;
		int grassB = grassColor & 0xFF;
		
		float avgStage = 0;
		float stageSamples = 0;
		
		for (BlockPos checkPos : WorldUtils.getArea(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
		{
			IBlockState checkState = world.getBlockState(checkPos);
			
			if (checkState.getPropertyNames().contains(BlockMoss.STAGE))
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
		int dryR = (dryColor >> 16) & 0xFF;
		int dryG = (dryColor >> 8) & 0xFF;
		int dryB = dryColor & 0xFF;
		
		float amount = 1 - avgStage;
		
		grassR = GenesisMath.lerp(grassR, dryR, amount);
		grassG = GenesisMath.lerp(grassG, dryG, amount);
		grassB = GenesisMath.lerp(grassB, dryB, amount);
		
		grassColor = (grassR << 16) |
					(grassG << 8) |
					grassB;
		
		return grassColor;
	};
	
	public static final IBlockColor BLOCK_FLOWER_POT = (s, w, p, t) ->
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
	public static final IItemColor ITEM_ARMOR = (s, t) -> ((ItemArmor) s.getItem()).getColor(s);
}
