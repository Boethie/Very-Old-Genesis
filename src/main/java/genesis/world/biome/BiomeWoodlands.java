package genesis.world.biome;

import java.util.Random;

import genesis.combo.variant.EnumDebrisOther;
import genesis.combo.variant.EnumDung;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenAsplenium;
import genesis.world.biome.decorate.WorldGenBoulders;
import genesis.world.biome.decorate.WorldGenCircleReplacement;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenDecorationOnBlock;
import genesis.world.biome.decorate.WorldGenDung;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.biome.decorate.WorldGenSplash;
import genesis.world.biome.decorate.WorldGenStemonitis;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeBase.TreeTypes;
import genesis.world.gen.feature.WorldGenTreeDryophyllum;
import genesis.world.gen.feature.WorldGenTreeFicus;
import genesis.world.gen.feature.WorldGenTreeGinkgo;
import genesis.world.gen.feature.WorldGenTreeLaurophyllum;
import genesis.world.gen.feature.WorldGenTreeMetasequoia;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeWoodlands extends BiomeGenesis
{
	public BiomeWoodlands(Biome.BiomeProperties properties)
	{
		super(properties);
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		getDecorator().setGrassCount(7);
		addGrass(WorldGenPlant.create(EnumPlant.DRYOPTERIS).setPatchCount(9), 1);
		
		getDecorator().setFlowerCount(0.15F);
		addFlower(WorldGenPlant.create(EnumPlant.PALAEOASTER).setPatchCount(9), 1);
		
		addDecoration(WorldGenSplash.createHumusSplash(), 2.75F);
		addDecoration(new WorldGenBoulders(0.166F, 0.333F, 1).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.25F, 0.75F)), 0.4F);
		addDecoration(new WorldGenDung(EnumDung.CARNIVORE), 0.08F);
		addDecoration(new WorldGenStemonitis().setPatchCount(14), 6);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.zingiberopsis).setPatchCount(3), 0.15F);
		addDecoration(new WorldGenAsplenium().setPatchCount(7), 10);
		addDecoration(new WorldGenDecorationOnBlock((s) -> s.getMaterial() == Material.WATER, GenesisBlocks.cobbania.getDefaultState()), 2.8F);
		addDecoration(WorldGenCircleReplacement.getPeatGen(), 1);
		addDecoration(new WorldGenRoots(), 13);
		
		addPostDecoration(new WorldGenDebris(), 14);
		addPostDecoration(new WorldGenDebris(EnumDebrisOther.TYRANNOSAURUS_FEATHER).setPatchCount(1, 2), 0.1F);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(4.9F);
		
		addTree(new WorldGenTreeLaurophyllum(1, 2, false).setType(TreeTypes.TYPE_2), 60);
		addTree(new WorldGenTreeLaurophyllum(3, 4, false), 40);
		addTree(new WorldGenTreeFicus(4, 8, false), 30);
		addTree(new WorldGenTreeGinkgo(9, 13, false), 11);
		addTree(new WorldGenTreeGinkgo(12, 17, false).setType(TreeTypes.TYPE_2), 3);
		addTree(new WorldGenTreeDryophyllum(8, 13, false), 600);
		addTree(new WorldGenTreeDryophyllum(18, 22, false).setType(TreeTypes.TYPE_2), 50);
		addTree(new WorldGenTreeMetasequoia(13, 24, true), 100);
		//addTree(new WorldGenTreeDryophyllum(16, 18, false, GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.DEAD_LOG, EnumTree.DRYOPHYLLUM)).setType(TreeTypes.TYPE_3), 1);
		
		addTree(new WorldGenDeadLog(5, 8, EnumTree.DRYOPHYLLUM, true), 28);
		addTree(new WorldGenDeadLog(5, 8, EnumTree.METASEQUOIA, true), 5);
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
