package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;
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
		
		theBiomeDecorator.grassPerChunk = 8;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setNextToWater(false).setPatchSize(3).setCountPerChunk(3));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(4).setCountPerChunk(3));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setWaterProximity(2, 0).setNextToWater(true).setPatchSize(4).setCountPerChunk(8));
		
		addDecoration(new WorldGenRockBoulders().setMaxHeight(3).setCountPerChunk(8));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(4).setCountPerChunk(1));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		addDecoration(new WorldGenDebris().setCountPerChunk(28));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeSigillaria(10, 15, true).setTreeCountPerChunk(7));
		addTree(new WorldGenTreePsaronius(5, 8, true).setTreeCountPerChunk(3));
		addTree(new WorldGenTreeLepidodendron(14, 18, true).setTreeCountPerChunk(10));
		
		addTree(new WorldGenDeadLog(3, 6, EnumTree.LEPIDODENDRON, true).setTreeCountPerChunk(6));
		addTree(new WorldGenDeadLog(3, 6, EnumTree.SIGILLARIA, true).setTreeCountPerChunk(5));
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getPlantBlockState(EnumPlant.ZYGOPTERIS)).setVolume(96);
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
