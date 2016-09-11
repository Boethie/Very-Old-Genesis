package genesis.world.biome;

import java.util.Random;

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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeFloodplainsForest extends BiomeGenesis
{
	public BiomeFloodplainsForest(Biome.BiomeProperties properties)
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
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.prototaxites), 0.175F);
		addDecoration(WorldGenCircleReplacement.getPeatGen(), 2.75F);
		addDecoration(new WorldGenMossStages(), 30);
		addDecoration(new WorldGenRoots(), 13);
		
		addPostDecoration(new WorldGenDebris(), 16);
		
		getDecorator().setTreeCount(4.2F);
		addTree(new WorldGenTreeArchaeopteris(15, 19, true), 29);
		
		addTree(new WorldGenDeadLog(5, 8, EnumTree.ARCHAEOPTERIS, true), 1);
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
