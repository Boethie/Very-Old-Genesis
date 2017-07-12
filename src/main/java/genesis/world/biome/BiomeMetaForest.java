package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenBoulders;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.biome.decorate.WorldGenSplash;
import genesis.world.biome.decorate.WorldGenStemonitis;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeBase.TreeTypes;
import genesis.world.gen.feature.WorldGenTreeGinkgo;
import genesis.world.gen.feature.WorldGenTreeMetasequoia;
import genesis.world.gen.feature.WorldGenTreeTropidogyne;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeMetaForest extends BiomeGenesis
{
	public BiomeMetaForest(Biome.BiomeProperties properties)
	{
		super(properties);
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		getDecorator().setGrassCount(5);
		addGrass(WorldGenPlant.create(EnumPlant.CRETACIFILIX).setPatchCount(9), 1);
		
		addDecoration(WorldGenSplash.createHumusSplash(), 4.1F);
		addDecoration(new WorldGenBoulders(0.199F, 0.333F, 1).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.25F, 0.75F)), 0.21F);
		
		//TODO: WorldGenPaleogaracites should be instead called/moved inside the dead log generation.
		addDecoration(new WorldGenPalaeoagaracites().setPatchCount(24), 14);
		addDecoration(new WorldGenStemonitis().setPatchCount(14), 6);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.PROGRAMINIS).setPatchCount(5), 1.75F);
		addDecoration(new WorldGenRoots(), 13);
		
		addPostDecoration(new WorldGenDebris(), 7);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(4.2F);
		
		addTree(new WorldGenTreeMetasequoia(17, 22, true).generateVine(21, GenesisBlocks.FRULLANIA), 300);
		addTree(new WorldGenTreeMetasequoia(23, 27, true).generateVine(21, GenesisBlocks.FRULLANIA).setType(TreeTypes.TYPE_2), 120);
		addTree(new WorldGenTreeGinkgo(10, 13, false).generateVine(21, GenesisBlocks.FRULLANIA), 5);
		addTree(new WorldGenTreeGinkgo(12, 17, false).setType(TreeTypes.TYPE_2).generateVine(21, GenesisBlocks.FRULLANIA), 1);
		
		addTree(new WorldGenDeadLog(5, 8, EnumTree.METASEQUOIA, true), 9);
		addTree(new WorldGenDeadLog(5, 8, EnumTree.METASEQUOIA, true).setType(1), 4);
		
		addTree(WorldGenTreeTropidogyne.makeDefaultWithNotify(false), 40);
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
