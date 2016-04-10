package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.entity.living.IEntityPreferredBiome;
import genesis.entity.living.flying.EntityMeganeura;
import genesis.world.biome.decorate.*;
import genesis.world.gen.feature.*;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenSwampRainforest extends BiomeGenBaseGenesis implements IEntityPreferredBiome
{
	public BiomeGenSwampRainforest(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		theBiomeDecorator.clayPerChunk = 4;
		theBiomeDecorator.sandPerChunk2 = 2;
		theBiomeDecorator.grassPerChunk = 6;
		
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityMeganeura.class, 8, 4, 8));
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setPatchSize(3).setCountPerChunk(3));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(4).setCountPerChunk(5));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setPatchSize(4).setCountPerChunk(10));
		addDecoration(WorldGenCircleReplacement.getPeatGen().setCountPerChunk(10));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		addDecoration(new WorldGenDebris().setCountPerChunk(33));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
		addGrassFlowers();
		
		addTree(new WorldGenTreeSigillaria(9, 12, true).setTreeCountPerChunk(2));
		addTree(new WorldGenTreePsaronius(5, 6, true).setTreeCountPerChunk(2));
		addTree(new WorldGenTreeCordaites(12, 17, true).setTreeCountPerChunk(5));
		addTree(new WorldGenTreeLepidodendron(11, 15, true).setTreeCountPerChunk(6));
		
		addTree(new WorldGenDeadLog(3, 6, EnumTree.LEPIDODENDRON, true).setTreeCountPerChunk(8));
		addTree(new WorldGenDeadLog(3, 6, EnumTree.SIGILLARIA, true).setTreeCountPerChunk(4));
		addTree(new WorldGenDeadLog(3, 6, EnumTree.CORDAITES, true).setCanGrowInWater(true).setTreeCountPerChunk(8));
	}
	
	@Override
	public float getFogDensity()
	{
		return 0.75F;
	}
	
	@Override
	public float getNightFogModifier()
	{
		//the higher the number, more dense fog will be at night
		return 0.65F;
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

	@Override
	public boolean shouldEntityPreferBiome(EntityLivingBase entity)
	{
		return true;
	}
}
