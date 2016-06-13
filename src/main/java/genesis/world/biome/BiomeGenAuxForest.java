package genesis.world.biome;

import java.util.Random;

import genesis.block.BlockMoss;
import genesis.block.BlockMoss.EnumSoil;
import genesis.combo.variant.EnumDebrisOther;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.biome.decorate.WorldGenSplash;
import genesis.world.biome.decorate.WorldGenStemonitis;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;
import genesis.world.gen.feature.WorldGenTreeBase.TreeTypes;
import genesis.world.gen.feature.WorldGenTreeGinkgo;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenAuxForest extends BiomeGenBaseGenesis
{
	public BiomeGenAuxForest(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenStemonitis().setPatchCount(14), 6);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.cladophlebis).setPatchCount(9), 0.75F);
		
		addDecoration(WorldGenSplash.createHumusSplash(), 5.1F);
		addDecoration(new WorldGenDebris(), 22);
		addDecoration(new WorldGenDebris(EnumDebrisOther.COELOPHYSIS_FEATHER).setPatchCount(1, 2), 0.1F);
		addDecoration(new WorldGenRockBoulders().setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 2);
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.333F);
		addDecoration(new WorldGenRoots(), 13);
		
		getDecorator().setGrassCount(7);
		addGrass(WorldGenPlant.create(EnumPlant.TODITES).setPatchCount(9), 3);
		addGrass(WorldGenPlant.create(EnumPlant.PHLEBOPTERIS).setPatchCount(9), 1);
		
		getDecorator().setFlowerCount(5);
		addFlower(WorldGenPlant.create(EnumPlant.SANMIGUELIA).setNextToWater(true).setPatchCount(4), 2);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(4.35F);
		
		addTree(new WorldGenTreeAraucarioxylon(25, 30, true), 80);
		addTree(new WorldGenTreeGinkgo(8, 13, false), 3);
		addTree(new WorldGenTreeGinkgo(12, 17, false).setType(TreeTypes.TYPE_2), 1);
		
		addTree(new WorldGenDeadLog(7, 10, EnumTree.ARAUCARIOXYLON, true), 7);
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
