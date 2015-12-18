package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.DungBlocksAndItems;
import genesis.metadata.EnumDung;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPatch;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenAuxPlains extends BiomeGenBaseGenesis
{
	public BiomeGenAuxPlains(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Plains");
		setTemperatureRainfall(1.2F, 0.0F);
		setDisableRain();
		setHeight(0.1F, 0.05F);
		
		theBiomeDecorator.grassPerChunk = 1;
		theBiomeDecorator.sandPerChunk2 = 2;
		
		addDecoration(new WorldGenPatch().addBlocks(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT)).addAllowedBlocks(GenesisBlocks.moss.getBlockState()).setCountPerChunk(4));
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.cladophlebis).setPatchSize(4).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setRarity(10).setWaterRequired(false).setMaxHeight(3).addBlocks(GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, EnumDung.SAUROPODA)).setCountPerChunk(1));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		addDecoration(new WorldGenRoots().setCountPerChunk(32));
		
		addDecoration(new WorldGenRockBoulders().setRarity(110).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
		
		addTree(new WorldGenTreeAraucarioxylon(18, 22, true).setType(1).setRarity(5).setTreeCountPerChunk(2));
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
