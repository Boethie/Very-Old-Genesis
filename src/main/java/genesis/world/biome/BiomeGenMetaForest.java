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
import genesis.world.gen.feature.WorldGenTreeBase.TreeTypes;
import genesis.world.gen.feature.WorldGenTreeGinkgo;
import genesis.world.gen.feature.WorldGenTreeMetasequoia;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenMetaForest extends BiomeGenBaseGenesis
{
	public BiomeGenMetaForest(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		theBiomeDecorator.grassPerChunk = 5;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenPalaeoagaracites().setPatchSize(24).setCountPerChunk(96));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPatchSize(5).setCountPerChunk(3));
		
		addDecoration(new WorldGenRockBoulders().setRadius(3).setCountPerChunk(3));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(3).setRarity(4).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.HERBIVORE)).setInGround(false).setWaterRequired(false).setRadius(2).setRarity(14).setCountPerChunk(1));
		addDecoration(new WorldGenDebris().setCountPerChunk(7));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeArchaeanthus(7, 20, false).setTreeCountPerChunk(1).setRarity(7));
		addTree(new WorldGenTreeMetasequoia(12, 24, true).setGenerateRandomSaplings(true).setTreeCountPerChunk(5));
		addTree(new WorldGenTreeMetasequoia(23, 27, true).setGenerateRandomSaplings(true).setType(TreeTypes.TYPE_2).setTreeCountPerChunk(2));
		addTree(new WorldGenTreeGinkgo(8, 13, false).setTreeCountPerChunk(1).setRarity(10));
		addTree(new WorldGenTreeGinkgo(12, 17, false).setType(TreeTypes.TYPE_2).setTreeCountPerChunk(1).setRarity(20));
		
		addTree(new WorldGenDeadLog(4, 8, EnumTree.METASEQUOIA, true).setRarity(1).setTreeCountPerChunk(1));
		addTree(new WorldGenDeadLog(4, 8, EnumTree.METASEQUOIA, true).setType(1).setRarity(4).setTreeCountPerChunk(1));
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
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 1;
		mossStages[1] = 2;
		super.genTerrainBlocks(world, rand, primer, blockX, blockZ, d);
	}
}
