package genesis.world.biome;

import java.util.Random;

import genesis.combo.DungBlocksAndItems;
import genesis.combo.variant.EnumDung;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;
import genesis.world.gen.feature.WorldGenTreeGinkgo;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenSavanna extends BiomeGenBaseGenesis
{
	public BiomeGenSavanna(int id)
	{
		super(id);
		setBiomeName("Savanna");
		setTemperatureRainfall(1.2F, 0.0F);
		setDisableRain();
		setHeight(0.1F, 0.05F);
		
		theBiomeDecorator.grassPerChunk = 0;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.cladophlebis).setPatchSize(4).setCountPerChunk(5));
		addDecoration(new WorldGenRockBoulders().setRarity(8).setWaterRequired(false).setMaxHeight(3).addBlocks(GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.SAUROPODA)).setCountPerChunk(1));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30).setPatchSize(12));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeAraucarioxylon(18, 22, true).setType(1).setGenerateRandomSaplings(false).setRarity(3).setTreeCountPerChunk(1));
		addTree(new WorldGenTreeGinkgo(12, 17, true).setTreeCountPerChunk(1).setRarity(2));
	}
	
	@Override
	public float getFogDensity()
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
		mossStages[0] = 0;
		mossStages[1] = 1;
		super.generateBiomeTerrain(world, rand, primer, blockX, blockZ, d);
	}
}
