package genesis.world.biome;

import java.util.Random;

import genesis.block.BlockMoss;
import genesis.block.BlockMoss.EnumSoil;
import genesis.combo.variant.EnumDebrisOther;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenAsplenium;
import genesis.world.biome.decorate.WorldGenCircleReplacement;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenDecorationOnBlock;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenBoulders;
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
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenWoodlands extends BiomeGenBaseGenesis
{
	public BiomeGenWoodlands(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		//TODO: Todites will be replaced by another fern.
		getDecorator().setGrassCount(7);
		addGrass(WorldGenPlant.create(EnumPlant.TODITES).setPatchCount(9), 1);
		
		getDecorator().setFlowerCount(0.15F);
		addFlower(WorldGenPlant.create(EnumPlant.PALAEOASTER).setPatchCount(9), 1);
		
		addDecoration(WorldGenSplash.createHumusSplash(), 2.55F);
		addDecoration(new WorldGenStemonitis().setPatchCount(14), 6);
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.zingiberopsis).setPatchCount(3), 0.15F);
		addDecoration(WorldGenCircleReplacement.getPeatGen(), 1);
		addDecoration(new WorldGenDebris(), 14);
		addDecoration(new WorldGenDebris(EnumDebrisOther.TYRANNOSAURUS_FEATHER).setPatchCount(1, 2), 0.1F);
		addDecoration(new WorldGenBoulders(0.166F, 0.333F, 1).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 2);
		addDecoration(new WorldGenRoots(), 13);
		addDecoration(new WorldGenDecorationOnBlock((s) -> s.getMaterial() == Material.water, GenesisBlocks.cobbania.getDefaultState()), 2.8F);
		addDecoration(new WorldGenAsplenium().setPatchCount(18), 10);
	}
	
	protected void addTrees()
	{
		getDecorator().setTreeCount(4.9F);
		
		addTree(new WorldGenTreeLaurophyllum(3, 4, false), 100);
		addTree(new WorldGenTreeFicus(4, 8, false), 30);
		addTree(new WorldGenTreeGinkgo(8, 13, false), 11);
		addTree(new WorldGenTreeGinkgo(12, 17, false).setType(TreeTypes.TYPE_2), 3);
		addTree(new WorldGenTreeDryophyllum(8, 13, false), 600);
		addTree(new WorldGenTreeDryophyllum(18, 22, false).setType(TreeTypes.TYPE_2), 50);
		addTree(new WorldGenTreeMetasequoia(12, 24, true), 100);
		
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
