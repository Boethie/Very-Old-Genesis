package genesis.world.gen;

import java.util.HashMap;
import java.util.HashSet;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import genesis.util.SuperSimplexNoise;
import genesis.world.ChunkGeneratorGenesis;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;

public class MapGenCavesGenesis extends MapGenBase
{	
	private static final HashSet<Block> DIGGABLE_BLOCKS = new HashSet<Block>();
	private static final HashMap<IBlockState, IBlockState> ABOVE_BLOCK_REPLACEMENTS
		= new HashMap<IBlockState, IBlockState>();
	private static final IBlockState[] LEVEL_REPLACEMENT_BLOCKS = new IBlockState[256];
	private static final double[] LEVEL_CAVE_THRESHOLDS = new double[256];
	static {
		DIGGABLE_BLOCKS.add(GenesisBlocks.granite);
		DIGGABLE_BLOCKS.add(Blocks.dirt);
		DIGGABLE_BLOCKS.add(GenesisBlocks.moss);
		DIGGABLE_BLOCKS.add(Blocks.hardened_clay);
		DIGGABLE_BLOCKS.add(Blocks.stained_hardened_clay);
		DIGGABLE_BLOCKS.add(Blocks.sandstone);
		DIGGABLE_BLOCKS.add(Blocks.red_sandstone);
		DIGGABLE_BLOCKS.add(Blocks.mycelium);
		DIGGABLE_BLOCKS.add(Blocks.snow_layer);
		DIGGABLE_BLOCKS.add(Blocks.sand);
		DIGGABLE_BLOCKS.add(Blocks.gravel);

		ABOVE_BLOCK_REPLACEMENTS.put(
				GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.SILT),
				GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.SILT));
		ABOVE_BLOCK_REPLACEMENTS.put(
				GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT),
				GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT));
		
		{
			int y = 0;
			for (; y < 7; y++) LEVEL_REPLACEMENT_BLOCKS[y] = GenesisBlocks.komatiitic_lava.getDefaultState();
			for (; y < 256; y++) LEVEL_REPLACEMENT_BLOCKS[y] = Blocks.air.getDefaultState();
		}
		
		for (int y = 0; y < 256; y++) {
			LEVEL_CAVE_THRESHOLDS[y] = (1.0 / (1.0 + Math.pow((y - 10)/20.0, 2))); 
		}
	}
	
	@Override
	public void generate(IChunkProvider chunkProvider, World world, int chunkX, int chunkZ, ChunkPrimer data)
	{		
		//System.out.println("Geerating caves for chunk: " + chunkX + "," + chunkZ);
		//long gStart = System.nanoTime();
		SuperSimplexNoise noise = new SuperSimplexNoise(world.getSeed());
		SuperSimplexNoise.NoiseInstance3[] instances = new SuperSimplexNoise.NoiseInstance3[] {
				new SuperSimplexNoise.NoiseInstance3(noise, 0, 1, -1, 2)
		};
		double[] values = new double[3];
		SuperSimplexNoise noise6 = new SuperSimplexNoise(world.getSeed() + 5);
		
		for (int z = 0; z < 16; z++)
		{
			int blockZ = z + chunkZ * 16;
			
			for (int x = 0; x < 16; x++)
			{
				int blockX = x + chunkX * 16;
				
				//Get biome data for this column.
				BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(blockX, 0, blockZ));

				//Use 2D noise for the lava boulders.
				double lavaBoulderValue = noise6.eval(blockX / 16.0, blockZ / 16.0);
						
				for (int y = 255; y >= 0; y--)
				{
					int blockY = y;
					
					//Get the block currently at this point.
					IBlockState thisBlockState = data.getBlockState(x, y, z);
					Block thisBlock = thisBlockState.getBlock();
					
					//The block needs to be either in the list or be a biome block to be diggable.
					if (!DIGGABLE_BLOCKS.contains(thisBlock)
							&& thisBlock != biome.topBlock.getBlock()
							&& thisBlock != biome.fillerBlock.getBlock()) continue;
					
					//Get first noise evaluation
					values[0] = values[1] = values[2] = 0;
					SuperSimplexNoise.eval(blockX / 64.0, blockY / 64.0, blockZ / 64.0, instances, values);
					
					//X and Y derivatives make tunnels
					double tunnelValue = values[1] * values[1] + values[2] * values[2];
					
					//Value determines the intensity of lava rooms.
					double lavaRoomIntensity = values[0];
					lavaRoomIntensity = (lavaRoomIntensity / 2 + 0.5);
					lavaRoomIntensity *= lavaRoomIntensity;
					lavaRoomIntensity = 1 - lavaRoomIntensity;
					
					//Interpolate between tunnels and lava rooms based on this.
					double lavaroomInterp = LEVEL_CAVE_THRESHOLDS[y] * lavaRoomIntensity;
					double tunnelInterp = 1 - lavaroomInterp;
					
					//Decide if this block is part of a cave.
					if (tunnelInterp * tunnelValue > 0.1) continue;
					
					//Now for the lava boulders. Factor in lavaroomInterp so they smoothly join with walls.
					//double lavaBoulderValue = noise6.eval(blockX / 16.0, blockY / 64.0, blockZ / 16.0);
					double thisLavaBoulderValue = lavaBoulderValue;
					thisLavaBoulderValue *= lavaroomInterp;
					thisLavaBoulderValue -= y / 12.0;
					if (thisLavaBoulderValue > 0.0) continue;
					
					//Replace blocks above and below if we should.
					//TODO vary this 55 a bit with noise
					if (y > 55)
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
		//long took = System.nanoTime() - gStart;
		//System.out.println("Cave gen took " + (took / 1000000000.0d) + " seconds.");
	}
}
