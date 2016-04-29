package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.*;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.*;
import genesis.world.gen.feature.*;

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
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchCount(4), 2);
		addDecoration(WorldGenCircleReplacement.getPeatGen(), 1);
		addDecoration(new WorldGenMossStages(), 30);
		addDecoration(new WorldGenDebris(), 26);
		addDecoration(new WorldGenRoots(), 13);
		
		getDecorator().setTreeCount(10);
		addTree(new WorldGenTreeArchaeopteris(15, 20, true), 9);
		
		addTree(new WorldGenDeadLog(3, 6, EnumTree.ARCHAEOPTERIS, true), 3);
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
