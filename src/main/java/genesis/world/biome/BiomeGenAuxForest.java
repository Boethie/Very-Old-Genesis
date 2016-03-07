package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.EnumDebrisOther;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;
import genesis.world.gen.feature.WorldGenTreeGinkgo;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenAuxForest extends BiomeGenBaseGenesis
{
	public BiomeGenAuxForest(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Forest");
		setTemperatureRainfall(1.1F, 0.9F);
		setHeight(0.05F, 0.15F);
		
		theBiomeDecorator.grassPerChunk = 7;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.cladophlebis).setPatchSize(9).setCountPerChunk(2));
		addDecoration(new WorldGenPlant(EnumPlant.SANMIGUELIA).setNextToWater(true).setPatchSize(4).setCountPerChunk(16));
		
		addDecoration(new WorldGenRockBoulders().setMaxHeight(3).setCountPerChunk(3));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(3).setCountPerChunk(1));
		addDecoration(new WorldGenDebris().addAdditional(GenesisBlocks.debris.getBlockState(EnumDebrisOther.COELOPHYSIS_FEATHER)).setCountPerChunk(7));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeAraucarioxylon(25, 30, true).setTreeCountPerChunk(4));
		addTree(new WorldGenTreeGinkgo(6, 17, false).setTreeCountPerChunk(1).setRarity(6));
		addTree(new WorldGenDeadLog(4, 8, EnumTree.ARAUCARIOXYLON, true).setTreeCountPerChunk(1));
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getPlantBlockState(EnumPlant.TODITES)).setVolume(64);
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
