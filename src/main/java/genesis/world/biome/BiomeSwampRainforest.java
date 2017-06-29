package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.entity.living.IEntityPreferredBiome;
import genesis.entity.living.flying.EntityMeganeura;
import genesis.world.biome.decorate.WorldGenCircleReplacement;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.biome.decorate.WorldGenSplash;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeCordaites;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeSwampRainforest extends BiomeGenesis implements IEntityPreferredBiome
{
	public BiomeSwampRainforest(Biome.BiomeProperties properties)
	{
		super(properties);
		
		addDecorations();
		addTrees();

		theBiomeDecorator.clayPerChunk = 2;
		theBiomeDecorator.sandPerChunk2 = 3;

		spawnableCreatureList.add(new Biome.SpawnListEntry(EntityMeganeura.class, 8, 4, 8));
	}
	
	protected void addDecorations()
	{
		getDecorator().setGrassCount(6);
		addGrass(WorldGenPlant.create(EnumPlant.ZYGOPTERIS).setPatchCount(9), 1);

		addDecoration(WorldGenSplash.createHumusSplash(), 2.45F);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.ODONTOPTERIS).setPatchCount(3), 2);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.SPHENOPHYLLUM).setPatchCount(4), 4);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.CALAMITES).setPatchCount(3), 4);
		addDecoration(new WorldGenMossStages(), 30);
		addDecoration(WorldGenCircleReplacement.getPeatGen(), 10);
		addDecoration(new WorldGenRoots(), 13);

		addPostDecoration(new WorldGenDebris(), 33);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(13);
		addTree(new WorldGenTreeCordaites(12, 17, true).generateVine(6), 16);
		addTree(new WorldGenTreePsaronius(5, 6, true).generateVine(6), 11);
		addTree(new WorldGenTreeLepidodendron(15, 18, true).generateVine(6), 17);
		addTree(new WorldGenTreeSigillaria(9, 12, true).generateVine(6), 11);

		addTree(new WorldGenDeadLog(4, 7, EnumTree.LEPIDODENDRON, true), 4);
		addTree(new WorldGenDeadLog(4, 7, EnumTree.SIGILLARIA, true), 2);
		addTree(new WorldGenDeadLog(4, 7, EnumTree.CORDAITES, true).setCanGrowInWater(true), 5);
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
		double d1 = GRASS_COLOR_NOISE.getValue(chunkX * 0.25D, chunkZ * 0.25D);

		if (d1 > -0.2D)
		{
			int k = chunkX & 15;
			int l = chunkZ & 15;

			for (int i1 = 255; i1 >= 0; --i1)
			{
				IBlockState state = chunkPrimer.getBlockState(l, i1, k);

				if (state.getMaterial() != Material.AIR)
				{
					if (i1 == 62 && chunkPrimer.getBlockState(l, i1, k).getBlock() != Blocks.WATER)
					{
						chunkPrimer.setBlockState(l, i1, k, Blocks.WATER.getDefaultState());
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
