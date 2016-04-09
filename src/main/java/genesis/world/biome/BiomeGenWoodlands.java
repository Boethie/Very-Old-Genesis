package genesis.world.biome;

import java.util.Random;

import genesis.combo.DungBlocksAndItems;
import genesis.combo.variant.*;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.*;
import genesis.world.gen.feature.*;
import genesis.world.gen.feature.WorldGenTreeBase.TreeTypes;
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
		
		addDecoration(new WorldGenRockBoulders().setMaxHeight(3).setCountPerChunk(3));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(2).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.CARNIVORE)).setInGround(false).setWaterRequired(false).setMaxHeight(2).setRarity(12).setCountPerChunk(1));
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
		addTree(new WorldGenTreeDryophyllum(11, 15, false).setTreeCountPerChunk(9));
		addTree(new WorldGenTreeDryophyllum(13, 17, false).setType(TreeTypes.TYPE_2).setTreeCountPerChunk(1).setRarity(8));
		addTree(new WorldGenTreeMetasequoia(12, 24, true).setTreeCountPerChunk(2));
		
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
