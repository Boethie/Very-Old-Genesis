package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.*;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.*;
import genesis.world.gen.feature.*;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenRainforest extends BiomeGenBaseGenesis
{
	public BiomeGenRainforest(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		getDecorator().setGrassCount(8);
		addGrass(WorldGenPlant.create(EnumPlant.ZYGOPTERIS).setPatchCount(14), 1);
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setNextToWater(false).setPatchCount(3), 2);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchCount(4), 2);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setPatchCount(3), 3);
		addDecoration(new WorldGenRockBoulders().setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 6);
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.333F);
		addDecoration(new WorldGenMossStages(), 30);
		addDecoration(new WorldGenDebris(), 28);
		addDecoration(new WorldGenRoots(), 13);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(27);
		
		addTree(new WorldGenTreeSigillaria(10, 15, true), 7);
		addTree(new WorldGenTreePsaronius(5, 8, true), 4);
		addTree(new WorldGenTreeLepidodendron(14, 18, true), 10);
		
		addTree(new WorldGenDeadLog(3, 6, EnumTree.LEPIDODENDRON, true), 6);
		addTree(new WorldGenDeadLog(3, 6, EnumTree.SIGILLARIA, true), 4);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 1;
		mossStages[1] = 2;
		super.genTerrainBlocks(world, rand, primer, blockX, blockZ, d);
	}
}
