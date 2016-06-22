package genesis.world.biome;

import java.util.Random;

import genesis.block.BlockMoss;
import genesis.block.BlockMoss.EnumSoil;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenCircleReplacement;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.biome.decorate.WorldGenSplash;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeArchaeopteris;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenFloodplainsForest extends BiomeGenBaseGenesis
{
	public BiomeGenFloodplainsForest(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		theBiomeDecorator.clayPerChunk = 2;
		theBiomeDecorator.sandPerChunk2 = 3;
		
		getDecorator().setGrassCount(36);
		addGrass(WorldGenPlant.create(EnumPlant.RHACOPHYTON), 15);
		
		getDecorator().setFlowerCount(1);
		addFlower(WorldGenPlant.create(EnumPlant.PSILOPHYTON).setPatchCount(6), 1);
		
		addDecoration(WorldGenSplash.createHumusSplash(), 1.35F);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchCount(4), 2);
		addDecoration(WorldGenCircleReplacement.getPeatGen(), 1);
		addDecoration(new WorldGenMossStages(), 30);
		addDecoration(new WorldGenRoots(), 13);
		
		addPostDecoration(new WorldGenDebris(), 24);
		
		getDecorator().setTreeCount(6.6F);
		addTree(new WorldGenTreeArchaeopteris(15, 19, true), 19);
		
		addTree(new WorldGenDeadLog(5, 8, EnumTree.ARCHAEOPTERIS, true), 2);
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
