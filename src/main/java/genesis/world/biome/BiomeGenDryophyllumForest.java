package genesis.world.biome;

import java.util.Random;

import genesis.combo.DungBlocksAndItems;
import genesis.combo.variant.EnumDung;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeFicus;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenDryophyllumForest extends BiomeGenBaseGenesis
{
	public BiomeGenDryophyllumForest(int id)
	{
		super(id);
		setBiomeName("Dryophyllum Forest");
		setTemperatureRainfall(0.8F, 0.9F);
		setHeight(0.05F, 0.15F);
		
		theBiomeDecorator.grassPerChunk = 0;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.zingiberopsis).setPatchSize(3).setCountPerChunk(1));
		addDecoration(new WorldGenPlant(EnumPlant.PALAEOASTER).setCountPerChunk(1));
		
		addDecoration(new WorldGenRockBoulders().setMaxHeight(4).setCountPerChunk(3));
		addDecoration(new WorldGenRockBoulders().setRarity(10).setWaterRequired(false).setMaxHeight(3).addBlocks(GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.THEROPODA)).setCountPerChunk(1));
		addDecoration(new WorldGenDebris().setCountPerChunk(7));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeFicus(5, 10, false).setTreeCountPerChunk(1).setRarity(3));
		addTree(new WorldGenDeadLog(4, 8, EnumTree.DRYOPHYLLUM, true).setTreeCountPerChunk(1));
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
