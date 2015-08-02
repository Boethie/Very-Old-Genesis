package genesis.world.biome;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenGrowingPlant.GrowingPlantType;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenUnderWaterPatch;
import genesis.world.biome.decorate.WorldGenZygopteris;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeCordaites;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

import java.util.Random;

import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenSwampRainforest extends BiomeGenBaseGenesis
{
	public BiomeGenSwampRainforest(int id)
	{
		super(id);
		setBiomeName("Swamp Rainforest");
		topBlock = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, 0).withProperty(BlockGrass.SNOWY, false);
		setTemperatureRainfall(0.95F, 1.0F);
		setHeight(0.0F, 0.03F);
		theBiomeDecorator.grassPerChunk = 5;
		
		waterColorMultiplier = 0x725113;
		setColor(522674);
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setNextToWater(false).setPatchSize(5).setCountPerChunk(5));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(5).setCountPerChunk(5));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setWaterProximity(1, 0).setNextToWater(true).setPlantType(GrowingPlantType.COLUMN).setPatchSize(4).setCountPerChunk(8));
		addDecoration(new WorldGenUnderWaterPatch(GenesisBlocks.peat.getDefaultState()).setCountPerChunk(10));
		addDecoration(new WorldGenMossStages().setCountPerChunk(400));
		
		addTree(new WorldGenTreeLepidodendron(11, 15, true).setTreeCountPerChunk(5));
		addTree(new WorldGenTreeSigillaria(9, 12, true).setTreeCountPerChunk(4));
		addTree(new WorldGenTreeCordaites(15, 20, true).setTreeCountPerChunk(6));
		addTree(new WorldGenTreePsaronius(5, 6, true).setTreeCountPerChunk(2));
		
		addTree(new WorldGenRottenLog(3, 6, EnumTree.LEPIDODENDRON, true).setTreeCountPerChunk(10));
		addTree(new WorldGenRottenLog(3, 6, EnumTree.SIGILLARIA, true).setTreeCountPerChunk(10));
		addTree(new WorldGenRottenLog(3, 6, EnumTree.CORDAITES, true).setTreeCountPerChunk(10));
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenZygopteris();
	}
	
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer p_180622_3_, int p_180622_4_, int p_180622_5_, double p_180622_6_)
	{
		double d1 = field_180281_af.func_151601_a((double)p_180622_4_ * 0.25D, (double)p_180622_5_ * 0.25D);
		
		if (d1 > -0.2D)
		{
			int k = p_180622_4_ & 15;
			int l = p_180622_5_ & 15;
			
			for (int i1 = 255; i1 >= 0; --i1)
			{
				if (p_180622_3_.getBlockState(l, i1, k).getBlock().getMaterial() != Material.air)
				{
					if (i1 == 62 && p_180622_3_.getBlockState(l, i1, k).getBlock() != Blocks.water)
					{
						p_180622_3_.setBlockState(l, i1, k, Blocks.water.getDefaultState());
					}
					
					break;
				}
			}
		}
		
		generateBiomeTerrain(world, rand, p_180622_3_, p_180622_4_, p_180622_5_, p_180622_6_);
    }
}
