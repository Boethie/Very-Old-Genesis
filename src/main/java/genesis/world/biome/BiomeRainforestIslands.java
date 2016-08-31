package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.EnumAquaticPlant;
import genesis.combo.variant.EnumCoral;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenAquaticPlants;
import genesis.world.biome.decorate.WorldGenBoulders;
import genesis.world.biome.decorate.WorldGenCorals;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.biome.decorate.WorldGenSplash;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeRainforestIslands extends BiomeGenesis
{
	public BiomeRainforestIslands(BiomeProperties properties)
	{
		super(properties);
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		getDecorator().setGrassCount(9);
		addGrass(WorldGenPlant.create(EnumPlant.ZYGOPTERIS).setPatchCount(14), 1);
		
		getDecorator().setFlowerCount(4);
		addFlower(WorldGenPlant.create(EnumPlant.LEPACYCLOTES).setNextToWater(true).setPatchCount(4), 1);
		
		addDecoration(WorldGenSplash.createHumusSplash(), 2.75F);
		addDecoration(new WorldGenBoulders(0.055F, 3, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.25F, 0.75F)), 1.35F);
		addDecoration(new WorldGenPebbles(), 20);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setNextToWater(false).setPatchCount(3), 2);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchCount(4), 2);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setPatchCount(3), 4);
		
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HALYSITES), 0.35F);
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.HELIOLITES), 0.35F);
		addDecoration(new WorldGenCorals(2, 5, EnumCoral.FAVOSITES), 0.35F);
		
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.BANGIOMORPHA).setPatchCount(9), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARPOLIA).setPatchCount(9), 0.9F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.MARGERETIA).setPatchCount(9), 0.9F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.GRYPANIA).setPatchCount(9), 0.1F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.HAZELLA), 2);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.DIAGONIELLA), 1.6F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.CHANCELLORIA), 1.6F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.VETULOCYSTIS), 1.6F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PIRANIA), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.VAUXIA), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.PTERIDINIUM), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.CHARNIA, EnumAquaticPlant.CHARNIA_TOP), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.ERNIETTA), 1);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.WAPKIA), 0.9F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.DINOMISCHUS), 0.07F);
		addDecoration(new WorldGenAquaticPlants(EnumAquaticPlant.ECHMATOCRINUS), 0.07F);
		
		addDecoration(new WorldGenMossStages(), 30);
		addDecoration(new WorldGenRoots(), 13);
		
		addPostDecoration(new WorldGenDebris(), 28);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(20.6F);
		
		addTree(new WorldGenTreeSigillaria(10, 15, true).generateVine(11), 17);
		addTree(new WorldGenTreePsaronius(5, 8, true).generateVine(11), 14);
		addTree(new WorldGenTreeLepidodendron(14, 20, true).generateVine(11), 20);
		
		addTree(new WorldGenDeadLog(5, 8, EnumTree.LEPIDODENDRON, true), 2);
		addTree(new WorldGenDeadLog(4, 7, EnumTree.SIGILLARIA, true), 1);
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
