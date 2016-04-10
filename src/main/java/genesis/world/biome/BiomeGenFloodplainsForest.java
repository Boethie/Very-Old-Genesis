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
		
		theBiomeDecorator.clayPerChunk = 4;
		theBiomeDecorator.sandPerChunk2 = 2;
		theBiomeDecorator.grassPerChunk = 0;
		
		addDecoration(WorldGenPlant.create(EnumPlant.RHACOPHYTON).setCountPerChunk(44));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(4).setCountPerChunk(3));
		addDecoration(WorldGenPlant.create(EnumPlant.PSILOPHYTON).setPatchSize(8).setCountPerChunk(3));
		addDecoration(WorldGenCircleReplacement.getPeatGen().setCountPerChunk(1));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		addDecoration(new WorldGenDebris().setCountPerChunk(26));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
		
		addTree(new WorldGenTreeArchaeopteris(15, 20, true).setTreeCountPerChunk(9));
		
		addTree(new WorldGenDeadLog(3, 6, EnumTree.ARCHAEOPTERIS, true).setTreeCountPerChunk(6));
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
