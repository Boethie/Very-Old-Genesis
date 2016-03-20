package genesis.world.biome;

import java.util.Random;

import genesis.combo.DungBlocksAndItems;
import genesis.combo.variant.EnumDung;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeArchaeanthus;
import genesis.world.gen.feature.WorldGenTreeGinkgo;
import genesis.world.gen.feature.WorldGenTreeMetasequoia;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenMetaForest extends BiomeGenBaseGenesis
{
	public BiomeGenMetaForest(int id)
	{
		super(id);
		setBiomeName("Metasequoia Forest");
		setTemperatureRainfall(1.1F, 0.9F);
		setHeight(0.04F, 0.165F);
		
		theBiomeDecorator.grassPerChunk = 5;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenPalaeoagaracites().setPatchSize(24).setCountPerChunk(128));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPatchSize(5).setCountPerChunk(4));
		
		addDecoration(new WorldGenRockBoulders().setMaxHeight(3).setCountPerChunk(3));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(4).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setInGround(false).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.HERBIVORE)).setRarity(14).setCountPerChunk(1));
		addDecoration(new WorldGenDebris().setCountPerChunk(7));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeArchaeanthus(7, 20, false).setTreeCountPerChunk(1).setRarity(7));
		addTree(new WorldGenTreeMetasequoia(12, 24, true).setTreeCountPerChunk(5));
		addTree(new WorldGenTreeMetasequoia(23, 27, true).setType(1).setTreeCountPerChunk(2));
		addTree(new WorldGenTreeGinkgo(6, 17, false).setTreeCountPerChunk(1).setRarity(10));
		
		addTree(new WorldGenDeadLog(4, 8, EnumTree.METASEQUOIA, true).setTreeCountPerChunk(1));
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getPlantBlockState(EnumPlant.ASTRALOPTERIS)).setVolume(64);
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
