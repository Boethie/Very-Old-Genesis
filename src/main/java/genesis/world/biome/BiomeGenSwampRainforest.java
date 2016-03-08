package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.entity.living.IEntityPreferredBiome;
import genesis.entity.living.flying.EntityMeganeura;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.biome.decorate.WorldGenUnderWaterPatch;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeCordaites;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenSwampRainforest extends BiomeGenBaseGenesis implements IEntityPreferredBiome
{
	public BiomeGenSwampRainforest(int id)
	{
		super(id);
		setBiomeName("Swamp Rainforest");
		setTemperatureRainfall(0.95F, 1.4F);
		setHeight(-0.2F, 0.03F);
		
		waterColorMultiplier = 0x725113;
		
		theBiomeDecorator.clayPerChunk = 4;
		theBiomeDecorator.sandPerChunk2 = 2;
		theBiomeDecorator.grassPerChunk = 6;
		
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityMeganeura.class, 8, 4, 8));
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setNextToWater(false).setPatchSize(3).setCountPerChunk(3));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(4).setCountPerChunk(5));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setWaterProximity(2, 0).setNextToWater(true).setPatchSize(4).setCountPerChunk(10));
		addDecoration(new WorldGenUnderWaterPatch(Blocks.water, GenesisBlocks.peat.getDefaultState()).setCountPerChunk(10));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		addDecoration(new WorldGenDebris().setCountPerChunk(33));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
		
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

	@Override
	public boolean shouldEntityPreferBiome(EntityLivingBase entity)
	{
		return true;
	}
}
