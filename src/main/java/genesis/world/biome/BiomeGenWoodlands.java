package genesis.world.biome;

import java.util.Random;

import genesis.combo.DungBlocksAndItems;
import genesis.combo.variant.EnumDebrisOther;
import genesis.combo.variant.EnumDung;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenDecorationOnBlock;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
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
		
		theBiomeDecorator.grassPerChunk = 12;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(WorldGenPlant.create(EnumPlant.PALAEOASTER).setRarity(4).setPatchSize(48).setCountPerChunk(1));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.zingiberopsis).setRarity(4).setPatchSize(3).setCountPerChunk(1));
		addGrassFlowers();
		
		addDecoration(new WorldGenRockBoulders().setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)).setCountPerChunk(2));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)).setRarity(2).setCountPerChunk(1));
		//addDecoration(new WorldGenRockBoulders(GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.CARNIVORE)).setInGround(false).setWaterRequired(false).setRadius(2).setRarity(12).setCountPerChunk(1));
		addDecoration(new WorldGenDebris().addAdditional(GenesisBlocks.debris.getBlockState(EnumDebrisOther.TYRANNOSAURUS_FEATHER)).setCountPerChunk(20));
		addDecoration(new WorldGenRoots().setCountPerChunk(26));
		addDecoration(new WorldGenDecorationOnBlock((s) -> s.getMaterial() == Material.water, GenesisBlocks.cobbania.getDefaultState()).setCountPerChunk(10));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeLaurophyllum(3, 4, false).setTreeCountPerChunk(2));
		addTree(new WorldGenTreeFicus(4, 8, false).setTreeCountPerChunk(1).setRarity(8));
		addTree(new WorldGenTreeGinkgo(8, 13, false).setTreeCountPerChunk(1).setRarity(6));
		addTree(new WorldGenTreeGinkgo(12, 17, false).setType(TreeTypes.TYPE_2).setTreeCountPerChunk(1).setRarity(22));
		addTree(new WorldGenTreeDryophyllum(11, 15, false).setGenerateRandomSaplings(true).setTreeCountPerChunk(6));
		addTree(new WorldGenTreeDryophyllum(13, 17, false).setGenerateRandomSaplings(true).setType(TreeTypes.TYPE_2).setTreeCountPerChunk(1).setRarity(10));
		addTree(new WorldGenTreeMetasequoia(12, 24, true).setGenerateRandomSaplings(true).setTreeCountPerChunk(2));
		
		addTree(new WorldGenDeadLog(3, 6, EnumTree.DRYOPHYLLUM, true).setTreeCountPerChunk(2));
		addTree(new WorldGenDeadLog(3, 6, EnumTree.METASEQUOIA, true).setTreeCountPerChunk(1).setRarity(4));
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getPlantBlockState(EnumPlant.ONOCLEA)).setVolume(64);
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
