package genesis.world.biome;

import java.util.Random;

import genesis.combo.PlantBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenPrototaxites;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenMarsh extends BiomeGenBaseGenesis
{
	public BiomeGenMarsh(int id)
	{
		super(id);
		setBiomeName("Marsh");
		setTemperatureRainfall(1.15F, 0.3F);
		setHeight(0.0F, -0.01F);
		
		theBiomeDecorator.grassPerChunk = 5;
		
		addDecoration(new WorldGenPrototaxites().setCountPerChunk(1));
		
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
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer p_180622_3_, int p_180622_4_, int p_180622_5_, double p_180622_6_)
	{
		double d1 = GRASS_COLOR_NOISE.func_151601_a(p_180622_4_ * 0.25D, p_180622_5_ * 0.25D);
		
		if (d1 > -0.2D)
		{
			int k = p_180622_4_ & 15;
			int l = p_180622_5_ & 15;
			
			for (int i1 = 255; i1 >= 0; --i1)
			{
				if (p_180622_3_.getBlockState(l, i1, k).getBlock().getMaterial() != Material.air)
				{
					if (i1 == 62 && p_180622_3_.getBlockState(l, i1, k).getBlock() != Blocks.water)
					{
						p_180622_3_.setBlockState(l, i1, k, Blocks.water.getDefaultState());
					}
					
					break;
				}
			}
		}
		
		generateBiomeTerrain(world, rand, p_180622_3_, p_180622_4_, p_180622_5_, p_180622_6_);
	}
	
	@Override
	public void generateBiomeTerrain(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 0;
		mossStages[1] = 1;
		super.generateBiomeTerrain(world, rand, primer, blockX, blockZ, d);
	}
}
