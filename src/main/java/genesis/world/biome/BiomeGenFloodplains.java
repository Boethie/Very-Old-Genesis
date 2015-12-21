package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.DungBlocksAndItems;
import genesis.metadata.EnumDung;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.biome.decorate.WorldGenUnderWaterPatch;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenFloodplains extends BiomeGenBaseGenesis
{
	public BiomeGenFloodplains(int id)
	{
		super(id);
		setBiomeName("Floodplains");
		setTemperatureRainfall(0.8F, 0.9F);
		setHeight(0.1F, -0.1F);
		
		theBiomeDecorator.clayPerChunk = 4;
		theBiomeDecorator.sandPerChunk2 = 4;
		theBiomeDecorator.grassPerChunk = 3;
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.cladophlebis).setPatchSize(4).setCountPerChunk(3));
		addDecoration(new WorldGenRockBoulders().setRarity(10).setWaterRequired(false).setMaxHeight(3).addBlocks(GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.SAUROPODA)).setCountPerChunk(1));
		addDecoration(new WorldGenUnderWaterPatch(Blocks.water, GenesisBlocks.peat.getDefaultState()).setCountPerChunk(1));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		addDecoration(new WorldGenRoots().setCountPerChunk(32));
		
		addDecoration(new WorldGenRockBoulders().setRarity(110).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
		
		addTree(new WorldGenTreeAraucarioxylon(18, 22, true).setType(1).setGenerateRandomSaplings(false).setRarity(2).setTreeCountPerChunk(1));
	}
	
	@Override
	public float getFogDensity(int x, int y, int z)
	{
		return 0.75F;
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
