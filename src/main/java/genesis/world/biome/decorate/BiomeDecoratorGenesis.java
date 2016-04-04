package genesis.world.biome.decorate;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumOre;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisConfig;
import genesis.world.gen.feature.WorldGenGenesisLiquids;
import genesis.world.gen.feature.WorldGenGenesisSand;
import genesis.world.gen.feature.WorldGenMinableGenesis;
import genesis.world.gen.feature.WorldGenTreeBase;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BiomeDecoratorGenesis extends BiomeDecorator
{
	public List<WorldGenTreeBase> trees = new ArrayList<WorldGenTreeBase>();
	public List<WorldGenDecorationBase> decorations = new ArrayList<WorldGenDecorationBase>();
	public WorldGenerator gneissGen;
	public WorldGenerator komatiiteGen;
	public WorldGenerator rhyoliteGen;
	public WorldGenerator doleriteGen;
	public WorldGenerator trondhjemiteGen;
	public WorldGenerator fauxGen;
	public WorldGenerator anorthositeGen;
	public WorldGenerator quartzGen;
	public WorldGenerator zirconGen;
	public WorldGenerator garnetGen;
	public WorldGenerator hematiteGen;
	public WorldGenerator manganeseGen;
	public WorldGenerator malachiteGen;
	public WorldGenerator azuriteGen;
	public WorldGenerator olivineGen;
	public WorldGenerator flintGen;
	public WorldGenerator marcasiteGen;
	
	public BiomeDecoratorGenesis()
	{
		clayGen = new WorldGenCircleReplacement((s) -> s.getMaterial() == Material.water,
				4, 1, BlockStateMatcher.forBlock(Blocks.dirt), GenesisBlocks.red_clay.getDefaultState());
		sandGen = new WorldGenGenesisSand(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT), 7);
	}
	
	@Override
	public void decorate(World world, Random rand, BiomeGenBase biome, BlockPos chunkStart)
	{
		field_180294_c = chunkStart;
		komatiiteGen = new WorldGenMinableGenesis(GenesisBlocks.komatiite.getDefaultState(), 18, 36);
		gneissGen = new WorldGenMinableGenesis(GenesisBlocks.gneiss.getDefaultState(), 14, 28);
		rhyoliteGen = new WorldGenMinableGenesis(GenesisBlocks.rhyolite.getDefaultState(), 10, 20);
		doleriteGen = new WorldGenMinableGenesis(GenesisBlocks.dolerite.getDefaultState(), 10, 20);
		trondhjemiteGen = new WorldGenMinableGenesis(GenesisBlocks.trondhjemite.getDefaultState(), 7, 14);
		fauxGen = new WorldGenMinableGenesis(GenesisBlocks.faux_amphibolite.getDefaultState(), 5, 10);
		anorthositeGen = new WorldGenMinableGenesis(GenesisBlocks.anorthosite.getDefaultState(), 5, 10);
		quartzGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.QUARTZ), 4, 8);
		zirconGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.ZIRCON), 1, 4);
		garnetGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.GARNET), 1, 4);
		hematiteGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.HEMATITE), 4, 8);
		manganeseGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.MANGANESE), 1, 3);
		malachiteGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.MALACHITE), 2, 4);
		azuriteGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.AZURITE), 2, 4);
		olivineGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.OLIVINE), 1, 4);
		flintGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.FLINT), 4, 8, GenesisBlocks.limestone);
		marcasiteGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.MARCASITE), 1, 3, GenesisBlocks.limestone);
		genDecorations(biome, world, rand);
	}
	
	@Override
	protected void genDecorations(BiomeGenBase biome, World world, Random rand)
	{
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(world, rand, field_180294_c));
		
		generateOres(world, rand);
		
		boolean doGen = TerrainGen.decorate(world, rand, field_180294_c, SAND);
		
		for (int i = 0; doGen && i < sandPerChunk2; ++i)
		{
			int x = rand.nextInt(16) + 8;
			int z = rand.nextInt(16) + 8;
			sandGen.generate(world, rand, world.getTopSolidOrLiquidBlock(field_180294_c.add(x, 0, z)));
		}
		
		doGen = TerrainGen.decorate(world, rand, field_180294_c, CLAY);
		
		for (int i = 0; doGen && i < clayPerChunk; ++i)
		{
			int x = rand.nextInt(16) + 8;
			int z = rand.nextInt(16) + 8;
			clayGen.generate(world, rand, world.getTopSolidOrLiquidBlock(field_180294_c.add(x, 0, z)));
		}
		
		doGen = TerrainGen.decorate(world, rand, field_180294_c, TREE);
		
		for (int i = 0; doGen && i < trees.size(); ++i)
		{
			int count = trees.get(i).getTreeCountPerChunk();
			if (rand.nextInt(10) == 0) ++count;
			
			for (int j = 0; j < count; ++j)
			{
				int x = rand.nextInt(16) + 8;
				int z = rand.nextInt(16) + 8;
				int y = rand.nextInt(world.getHeight(field_180294_c.add(x, 0, z)).getY() * 2);
				trees.get(i).generate(world, rand, field_180294_c.add(x, y, z));
			}
		}
		
		doGen = TerrainGen.decorate(world, rand, field_180294_c, GRASS);
		
		for (int i = 0; doGen && i < grassPerChunk; ++i)
		{
			int x = rand.nextInt(16) + 8;
			int z = rand.nextInt(16) + 8;
			int y = rand.nextInt(world.getHeight(field_180294_c.add(x, 0, z)).getY() * 2);
			biome.getRandomWorldGenForGrass(rand).generate(world, rand, field_180294_c.add(x, y, z));
		}
		
		doGen = true;//TODO
		
		for (WorldGenDecorationBase gen : decorations)
		{
			for (int j = 0; doGen && j < gen.getCountPerChunk(); ++j)
			{
				int x = rand.nextInt(16) + 8;
				int z = rand.nextInt(16) + 8;
				int y = rand.nextInt(world.getHeight(field_180294_c.add(x, 0, z)).getY() * 2);
				gen.generate(world, rand, field_180294_c.add(x, y, z));
			}
		}
		
		if (generateLakes)
		{
			doGen = TerrainGen.decorate(world, rand, field_180294_c, LAKE_WATER);
			
			for (int i = 0; doGen && i < 50; ++i)
			{
				BlockPos pos = field_180294_c.add(
						rand.nextInt(16) + 8,
						rand.nextInt(rand.nextInt(248) + 8),
						rand.nextInt(16) + 8);
				(new WorldGenGenesisLiquids(Blocks.flowing_water)).generate(world, rand, pos);
			}
			
			doGen = TerrainGen.decorate(world, rand, field_180294_c, LAKE_LAVA);
			
			for (int i = 0; doGen && i < 20; ++i)
			{
				BlockPos pos = field_180294_c.add(
						rand.nextInt(16) + 8,
						rand.nextInt(rand.nextInt(rand.nextInt(240) + 8) + 8),
						rand.nextInt(16) + 8);
				(new WorldGenGenesisLiquids(GenesisBlocks.komatiitic_lava)).generate(world, rand, pos);
			}
		}
		
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(world, rand, field_180294_c));
	}
	
	@Override
	protected void generateOres(World world, Random rand)
	{
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(world, rand, field_180294_c));
		
		//if (TerrainGen.generateOre(currentWorld, randomGenerator, quartzGen, field_180294_c, QUARTZ))
		
		genStandardOre1(world, rand, 62, komatiiteGen, 0, 16);
		genStandardOre1(world, rand, 20, gneissGen, 0, 64);
		genStandardOre1(world, rand, 11, rhyoliteGen, 64, 128);
		genStandardOre1(world, rand, 11, doleriteGen, 64, 128);
		genStandardOre1(world, rand, 8, trondhjemiteGen, 0, 128);
		genStandardOre1(world, rand, 5, fauxGen, 0, 64);
		genStandardOre1(world, rand, 5, anorthositeGen, 0, 128);
		
		genStandardOreByLayers(world, rand, quartzGen, GenesisConfig.quartzCount, 5, 55, 128, 131);
		genStandardOreByLayers(world, rand, zirconGen, GenesisConfig.zirconCount, 5, 55, 128, 131);
		genStandardOreByLayers(world, rand, garnetGen, GenesisConfig.garnetCount, 5, 55, 128, 131);
		genStandardOreByLayers(world, rand, hematiteGen, GenesisConfig.hematiteCount, 35, 68, 128, 131);
		genStandardOreByLayers(world, rand, manganeseGen, GenesisConfig.manganeseCount, 35, 68, 128, 131);
		genStandardOreByLayers(world, rand, malachiteGen, GenesisConfig.malachiteCount, 35, 68, 128, 131);
		genStandardOreByLayers(world, rand, azuriteGen, GenesisConfig.azuriteCount, 35, 68, 128, 131);
		genStandardOreByLayers(world, rand, olivineGen, GenesisConfig.olivineCount, 5, 13, 13, 15);
		genStandardOreByLayers(world, rand, flintGen, GenesisConfig.flintCount, 25, 68, 128, 131);
		genStandardOreByLayers(world, rand, marcasiteGen, GenesisConfig.marcasiteCount, 25, 68, 128, 131);
		
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(world, rand, field_180294_c));
	}
	
	protected void genStandardOreByLayers(World world, Random rand, WorldGenerator gen, int count, int lower, int midLower, int midUpper, int upper)
	{
		genStandardOre1(world, rand, count, gen, lower, midLower - 1);
		genStandardOre1(world, rand, (int) (count * 0.6), gen, midLower, midUpper);
		genStandardOre1(world, rand, (int) (count * 0.1), gen, midUpper + 1, upper);
	}
	
	// Safety wrapper to prevent exceptions.
	/*private int nextInt(int i)
	{
		return i <= 1 ? 0 : rand.nextInt(i);
	}*/
}
