package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumPlant;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenMossStages;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenMarsh extends BiomeGenBaseGenesis
{
	public BiomeGenMarsh(int id)
	{
		super(id);
		setBiomeName("Marsh");
		this.temperature = 1.15f;
		//this.topBlock = Blocks.dirt.getDefaultState();
		setHeight(0.0F, 0.01F);
		
		theBiomeDecorator.grassPerChunk = 2;
		
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		//BiomeGenHills
		//Asteroxylon
		//Prototaxites on Prototaxites Mycellium
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(
				GenesisBlocks.plants.getBlockState(EnumPlant.PSILOPHYTON)
				,GenesisBlocks.plants.getBlockState(EnumPlant.SCIADOPHYTON)
				,GenesisBlocks.plants.getBlockState(EnumPlant.NOTHIA)
				,GenesisBlocks.plants.getBlockState(EnumPlant.COOKSONIA)
				,GenesisBlocks.plants.getBlockState(EnumPlant.BARAGWANATHIA)
				,GenesisBlocks.plants.getBlockState(EnumPlant.RHYNIA)).setVolume(64);
	}
	
	@Override
	public void generateBiomeTerrain(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[1];
		mossStages[0] = 0;
		super.generateBiomeTerrain(world, rand, primer, blockX, blockZ, d);
	}
	
	/*@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer p_180622_3_, int p_180622_4_, int p_180622_5_, double p_180622_6_)
	{
		int k = p_180622_4_ & 15;
		int l = p_180622_5_ & 15;
		
		for (int i1 = 255; i1 >= 0; --i1)
		{
			if (p_180622_3_.getBlockState(l, i1, k).getBlock().getMaterial() != Material.air)
			{
				if (i1 == 61 && p_180622_3_.getBlockState(l, i1, k).getBlock() != Blocks.water)
				{
					p_180622_3_.setBlockState(l, i1, k, Blocks.water.getDefaultState());
				}
				
				break;
			}
		}
		
		this.generateBiomeTerrain(world, rand, p_180622_3_, p_180622_4_, p_180622_5_, p_180622_6_);
    }*/
}
