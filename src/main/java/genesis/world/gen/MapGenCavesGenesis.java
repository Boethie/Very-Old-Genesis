package genesis.world.gen;

import java.util.HashMap;
import java.util.HashSet;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.util.SuperSimplexNoise;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

public class MapGenCavesGenesis extends MapGenBase
{	
	private static final HashSet<Block> DIGGABLE_BLOCKS = new HashSet<>();
	private static final HashMap<IBlockState, IBlockState> ABOVE_BLOCK_REPLACEMENTS = new HashMap<>();
	private static final IBlockState[] LEVEL_REPLACEMENT_BLOCKS = new IBlockState[256];
	static {
		DIGGABLE_BLOCKS.add(GenesisBlocks.granite);
		DIGGABLE_BLOCKS.add(Blocks.dirt);
		DIGGABLE_BLOCKS.add(GenesisBlocks.humus);
		DIGGABLE_BLOCKS.add(GenesisBlocks.moss);
		DIGGABLE_BLOCKS.add(GenesisBlocks.prototaxites_mycelium);
		DIGGABLE_BLOCKS.addAll(GenesisBlocks.silt.getBlocks(SiltBlocks.SILT));
		DIGGABLE_BLOCKS.addAll(GenesisBlocks.silt.getBlocks(SiltBlocks.CRACKED_SILT));
		DIGGABLE_BLOCKS.addAll(GenesisBlocks.silt.getBlocks(SiltBlocks.SILTSTONE));
		DIGGABLE_BLOCKS.add(GenesisBlocks.limestone);

		ABOVE_BLOCK_REPLACEMENTS.put(
				GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT),
				GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.SILT));
		ABOVE_BLOCK_REPLACEMENTS.put(
				GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT),
				GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT));
		ABOVE_BLOCK_REPLACEMENTS.put(
				GenesisBlocks.silt.getBlockState(SiltBlocks.CRACKED_SILT, EnumSilt.SILT),
				GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.SILT));
		ABOVE_BLOCK_REPLACEMENTS.put(
				GenesisBlocks.silt.getBlockState(SiltBlocks.CRACKED_SILT, EnumSilt.RED_SILT),
				GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT));
		
		{
			int y = 0;
			for (; y < 7; y++) LEVEL_REPLACEMENT_BLOCKS[y] = GenesisBlocks.komatiitic_lava.getDefaultState();
			for (; y < 256; y++) LEVEL_REPLACEMENT_BLOCKS[y] = Blocks.air.getDefaultState();
		}
	}
	
	@Override
	public void generate(World world, int chunkX, int chunkZ, ChunkPrimer data)
	{
		SuperSimplexNoise.NoiseInstance3[] noiseInstances_64_96_64_A = new SuperSimplexNoise.NoiseInstance3[] {
				new SuperSimplexNoise.NoiseInstance3(new SuperSimplexNoise(world.getSeed() + 0), 0, 1, 2, 3)
		};
		SuperSimplexNoise.NoiseInstance3[] noiseInstances_64_96_64_B = new SuperSimplexNoise.NoiseInstance3[] {
				new SuperSimplexNoise.NoiseInstance3(new SuperSimplexNoise(world.getSeed() + 1), 4, 5, 6, 7)
		};
		double[] values = new double[8];
		SuperSimplexNoise lavaBoulderNoise = new SuperSimplexNoise(world.getSeed() + 2);
		
		for (int z = 0; z < 16; z++)
		{
			int blockZ = z + chunkZ * 16;
			
			for (int x = 0; x < 16; x++)
			{
				int blockX = x + chunkX * 16;
				
				//Get biome data for this column.
				BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(blockX, 0, blockZ));

				//Use 2D noise for the lava boulders.
				double lavaBoulderValue = lavaBoulderNoise.eval(blockX / 16.0, blockZ / 16.0);
						
				for (int y = 255; y >= 1; y--)
				{
					int blockY = y;
					
					//Get the block currently at this point.
					IBlockState thisBlockState = data.getBlockState(x, y, z);
					Block thisBlock = thisBlockState.getBlock();
					
					//The block needs to be either in the list or be a biome block to be diggable.
					if (!DIGGABLE_BLOCKS.contains(thisBlock)
							&& thisBlock != biome.topBlock.getBlock()
							&& thisBlock != biome.fillerBlock.getBlock()) continue;
					
					//Get noise values & derivatives
					for (int i = 0; i < 8; i++) values[i] = 0;
					SuperSimplexNoise.eval(blockX / 96.0 + 0.00, blockY / 64.0 + 0.00, blockZ / 96.0 + 0.00, noiseInstances_64_96_64_A, values);
					SuperSimplexNoise.eval(blockX / 96.0 + 0.25, blockY / 64.0 + 0.25, blockZ / 96.0 + 0.25, noiseInstances_64_96_64_B, values);
					double F1 = values[0], F1x = values[1], F1y = values[2], F1z = values[3];
					double F2 = values[4], F2x = values[5], F2y = values[6], F2z = values[7];

					//Get noise derivative magnitudes
					double F1dNormSq = F1x*F1x + F1y*F1y + F1z*F1z, F1dNorm = Math.sqrt(F1dNormSq);
					double F2dNormSq = F2x*F2x + F2y*F2y + F2z*F2z, F2dNorm = Math.sqrt(F2dNormSq);
					
					if (F1dNormSq == 0 || F2dNormSq == 0) continue;
					
					//Get unit dot product of derivatives
					double dot = F1x*F2x + F1y*F2y + F1z*F2z;
					double dotU = dot / F1dNorm / F2dNorm;
					
					if (dotU == 1 || dotU == -1) continue;
					
					//Lava rooms
					double threshold = 0.0075;
					double lavaRoomDisp = (y - 7 + F1 * 2) / 17.0;
					lavaRoomDisp *= lavaRoomDisp;
					if (lavaRoomDisp < 1) {
						lavaRoomDisp = 1 - lavaRoomDisp;
						lavaRoomDisp *= lavaRoomDisp;
						lavaRoomDisp *= lavaRoomDisp;
						
						//Vary the placement of the caves by re-using one of the cave noise values.
						lavaRoomDisp *= F2 / 2 + .5;
						
						//Threshold gets bigger the more we want lava rooms here
						threshold *= Math.pow(1 / threshold, lavaRoomDisp);
						
						//As we increase the threshold we need to decrease our cave-distortion-correction to avoid weird effects.
						dotU *= (1 - lavaRoomDisp);
						
						//Lava boulders
						double lavaBoulderMaxHeight = lavaRoomDisp * 16;
						double lavaBoulderHeight = lavaBoulderMaxHeight * lavaBoulderValue;
						if (lavaBoulderHeight >= y)
							continue;
					}
					
					//Approximate distance to nearest cave path (noise zero-surface intersection) using crazy math
					//(but factoring in lava-room alterations)
					double value = (F2 - F1*dotU) * (F2 - F1*dotU) / (1 - dotU * dotU) + F1 * F1;
					
					//Threshold
					if (value > threshold) continue;
					
					//Replace blocks above and below if we should.
					//Vary the blocks' distribution by re-using one of the cave nosies.
					if (y > 60 + 3 * F1)
					{
						//Block above
						if (y != 255) {
							IBlockState upTransition = ABOVE_BLOCK_REPLACEMENTS.getOrDefault(data.getBlockState(x, y + 1, z), null);
							if (upTransition != null) {
								data.setBlockState(x, y + 1, z, upTransition);
							}
						}
						
						//Block below (biome top block shift-down)
						if (biome.fillerBlock.equals(data.getBlockState(x, y - 1, z))) {
							data.setBlockState(x, y - 1, z, biome.topBlock);
						}
					}
					
					//Dig this block, it's part of a cave.
					data.setBlockState(x, y, z, LEVEL_REPLACEMENT_BLOCKS[y]);
				}
				
			}
		}
	}
}
