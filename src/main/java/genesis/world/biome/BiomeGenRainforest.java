package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenRainforest extends BiomeGenBaseGenesis
{
	public BiomeGenRainforest(int id)
	{
		super(id);
		setBiomeName("Rainforest");
		setTemperatureRainfall(0.95F, 1.4F);
		setHeight(0.15F, 0.05F);
		waterColorMultiplier = 0x725113;
		
		theBiomeDecorator.grassPerChunk = 6;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setNextToWater(false).setPatchSize(3).setCountPerChunk(2));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(4).setCountPerChunk(2));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setWaterProximity(2, 0).setNextToWater(true).setPatchSize(4).setCountPerChunk(8));
		addDecoration(new WorldGenRockBoulders().setMaxHeight(4).setCountPerChunk(8));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		addDecoration(new WorldGenDebris().setCountPerChunk(12));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
		
		addDecoration(new WorldGenRockBoulders().setRarity(110).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeLepidodendron(14, 18, true).setTreeCountPerChunk(10));
		addTree(new WorldGenTreeSigillaria(10, 15, true).setTreeCountPerChunk(7));
		addTree(new WorldGenTreePsaronius(5, 8, true).setTreeCountPerChunk(3));
		
		addTree(new WorldGenDeadLog(3, 6, EnumTree.LEPIDODENDRON, true).setTreeCountPerChunk(5));
		addTree(new WorldGenDeadLog(3, 6, EnumTree.SIGILLARIA, true).setTreeCountPerChunk(4));
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
	
	@Override
	public void generateBiomeTerrain(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 1;
		mossStages[1] = 2;
		super.generateBiomeTerrain(world, rand, primer, blockX, blockZ, d);
	}
}
