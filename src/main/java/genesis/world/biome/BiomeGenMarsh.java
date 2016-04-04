package genesis.world.biome;

import java.util.Random;

import genesis.combo.PlantBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.*;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenMarsh extends BiomeGenBaseGenesis
{
	public BiomeGenMarsh(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		theBiomeDecorator.grassPerChunk = 5;
		
		addDecoration(new WorldGenPrototaxites().setCountPerChunk(1).setRarity(4));
		
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		
		addDecoration(WorldGenPlant.create(GenesisBlocks.plants, PlantBlocks.DOUBLE_PLANT, EnumPlant.ASTEROXYLON).setCountPerChunk(5));
		addDecoration(WorldGenPlant.create(EnumPlant.RHYNIA).setPatchSize(4).setCountPerChunk(6));
		addDecoration(WorldGenPlant.create(EnumPlant.NOTHIA).setPatchSize(4).setCountPerChunk(5));
		addDecoration(WorldGenPlant.create(EnumPlant.SCIADOPHYTON).setPatchSize(4).setCountPerChunk(3));
		addDecoration(WorldGenPlant.create(EnumPlant.PSILOPHYTON).setPatchSize(4).setCountPerChunk(2));
		addDecoration(WorldGenPlant.create(EnumPlant.BARAGWANATHIA).setPatchSize(4).setCountPerChunk(1));
		addDecoration(WorldGenPlant.create(EnumPlant.COOKSONIA).setPatchSize(4).setCountPerChunk(1));
		
		addGrassFlowers();
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getPlantBlockState(EnumPlant.ASTEROXYLON)).setVolume(32);
	}
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunkPrimer, int chunkX, int chunkZ, double d)
	{
		double d1 = GRASS_COLOR_NOISE.func_151601_a(chunkX * 0.25D, chunkZ * 0.25D);
		
		if (d1 > -0.2D)
		{
			int k = chunkX & 15;
			int l = chunkZ & 15;
			
			for (int i1 = 255; i1 >= 0; --i1)
			{
				IBlockState state = chunkPrimer.getBlockState(l, i1, k);
				
				if (state.getBlock().getMaterial(state) != Material.air)
				{
					if (i1 == 62 && chunkPrimer.getBlockState(l, i1, k).getBlock() != Blocks.water)
					{
						chunkPrimer.setBlockState(l, i1, k, Blocks.water.getDefaultState());
					}
					
					break;
				}
			}
		}
		
		mossStages = new int[2];
		mossStages[0] = 0;
		mossStages[1] = 1;
		
		super.genTerrainBlocks(world, rand, chunkPrimer, chunkX, chunkZ, d);
	}
}
