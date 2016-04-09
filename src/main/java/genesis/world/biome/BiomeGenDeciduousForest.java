package genesis.world.biome;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeBase.TreeTypes;
import genesis.world.gen.feature.WorldGenTreeDryophyllum;
import genesis.world.gen.feature.WorldGenTreeGinkgo;
import genesis.world.gen.feature.WorldGenTreeLaurophyllum;
import genesis.world.gen.feature.WorldGenTreeMetasequoia;

public class BiomeGenDeciduousForest extends BiomeGenBaseGenesis
{
	public BiomeGenDeciduousForest(BiomeProperties properties)
	{
		super(properties);
		
		theBiomeDecorator.grassPerChunk = 12;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenRockBoulders().setMaxHeight(3).setCountPerChunk(3));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(2).setCountPerChunk(1));
		addDecoration(new WorldGenDebris().setCountPerChunk(20));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeLaurophyllum(3, 4, false).setTreeCountPerChunk(3));
		addTree(new WorldGenTreeGinkgo(8, 13, false).setTreeCountPerChunk(1).setRarity(4));
		addTree(new WorldGenTreeGinkgo(12, 17, false).setType(TreeTypes.TYPE_2).setTreeCountPerChunk(1).setRarity(15));
		addTree(new WorldGenTreeDryophyllum(11, 15, false).setGenerateRandomSaplings(true).setTreeCountPerChunk(12));
		addTree(new WorldGenTreeDryophyllum(13, 17, false).setGenerateRandomSaplings(true).setType(TreeTypes.TYPE_2).setTreeCountPerChunk(1).setRarity(15));
		addTree(new WorldGenTreeMetasequoia(12, 24, true).setGenerateRandomSaplings(true).setTreeCountPerChunk(4));
		addTree(new WorldGenDeadLog(4, 8, EnumTree.DRYOPHYLLUM, true).setTreeCountPerChunk(8));
		addTree(new WorldGenDeadLog(4, 8, EnumTree.METASEQUOIA, true).setTreeCountPerChunk(1));
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getPlantBlockState(EnumPlant.ONOCLEA)).setVolume(64);
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
