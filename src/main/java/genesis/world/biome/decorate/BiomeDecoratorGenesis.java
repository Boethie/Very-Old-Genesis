package genesis.world.biome.decorate;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CLAY;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.FLOWERS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_WATER;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SAND;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;

import java.util.Random;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumOre;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisConfig;
import genesis.util.functional.StateMatcher;
import genesis.util.random.i.IntFromFloat;
import genesis.util.random.i.IntRange;
import genesis.util.random.i.RandomIntProvider;
import genesis.world.biome.BiomeGenesis;
import genesis.world.biome.DecorationEntry;
import genesis.world.gen.feature.WorldGenGenesisLiquids;
import genesis.world.gen.feature.WorldGenGenesisSand;
import genesis.world.gen.feature.WorldGenMinableGenesis;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BiomeDecoratorGenesis extends BiomeDecorator
{
	private static final IntRange RANGE = IntRange.create(8, 23);
	
	public WorldGenerator radioactiveGen;
	public WorldGenerator komatiiteGen;
	public WorldGenerator gneissGen;
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

	public RandomIntProvider grassCountProvider;
	public RandomIntProvider flowerCountProvider;
	public RandomIntProvider treeCountProvider;
	
	public BiomeDecoratorGenesis()
	{
		clayGen = new WorldGenCircleReplacement((s) -> s.getMaterial() == Material.WATER,
				4, 1,
				StateMatcher.forBlocks(Blocks.DIRT, GenesisBlocks.OOZE)
						.or(StateMatcher.forBlocks(GenesisBlocks.SILT.getBlocks(SiltBlocks.SILT))),
				GenesisBlocks.RED_CLAY.getDefaultState());
		sandGen = new WorldGenGenesisSand(GenesisBlocks.SILT.getBlockState(SiltBlocks.SILT, EnumSilt.SILT), 7);
	}
	
	@Override
	public void decorate(World world, Random rand, Biome biome, BlockPos chunkStart)
	{
		chunkPos = chunkStart;
		radioactiveGen = new WorldGenMinableGenesis(GenesisBlocks.RADIOACTIVE_TRACES.getDefaultState(), 4, 8);
		komatiiteGen = new WorldGenMinableGenesis(GenesisBlocks.KOMATIITE.getDefaultState(), 18, 36);
		gneissGen = new WorldGenMinableGenesis(GenesisBlocks.GNEISS.getDefaultState(), 14, 28);
		rhyoliteGen = new WorldGenMinableGenesis(GenesisBlocks.RHYOLITE.getDefaultState(), 10, 20);
		doleriteGen = new WorldGenMinableGenesis(GenesisBlocks.DOLERITE.getDefaultState(), 10, 20);
		trondhjemiteGen = new WorldGenMinableGenesis(GenesisBlocks.TRONDHJEMITE.getDefaultState(), 7, 14);
		fauxGen = new WorldGenMinableGenesis(GenesisBlocks.FAUX_AMPHIBOLITE.getDefaultState(), 5, 10);
		anorthositeGen = new WorldGenMinableGenesis(GenesisBlocks.ANORTHOSITE.getDefaultState(), 5, 10);
		quartzGen = new WorldGenMinableGenesis(GenesisBlocks.ORES.getOreState(EnumOre.QUARTZ), 4, 8);
		zirconGen = new WorldGenMinableGenesis(GenesisBlocks.ORES.getOreState(EnumOre.ZIRCON), 1, 4);
		garnetGen = new WorldGenMinableGenesis(GenesisBlocks.ORES.getOreState(EnumOre.GARNET), 1, 4);
		hematiteGen = new WorldGenMinableGenesis(GenesisBlocks.ORES.getOreState(EnumOre.HEMATITE), 4, 8);
		manganeseGen = new WorldGenMinableGenesis(GenesisBlocks.ORES.getOreState(EnumOre.MANGANESE), 1, 3);
		malachiteGen = new WorldGenMinableGenesis(GenesisBlocks.ORES.getOreState(EnumOre.MALACHITE), 2, 4);
		azuriteGen = new WorldGenMinableGenesis(GenesisBlocks.ORES.getOreState(EnumOre.AZURITE), 2, 4);
		olivineGen = new WorldGenMinableGenesis(GenesisBlocks.ORES.getOreState(EnumOre.OLIVINE), 1, 4);
		flintGen = new WorldGenMinableGenesis(GenesisBlocks.ORES.getOreState(EnumOre.FLINT), 4, 8, GenesisBlocks.LIMESTONE);
		marcasiteGen = new WorldGenMinableGenesis(GenesisBlocks.ORES.getOreState(EnumOre.MARCASITE), 1, 4, GenesisBlocks.LIMESTONE);
		genDecorations(biome, world, rand);
	}
	
	protected BlockPos getPos(World world, Random rand, int topStart)
	{
		return world.getHeight(chunkPos.add(RANGE.get(rand), 0, RANGE.get(rand))).up(topStart);
	}
	
	protected BlockPos getPos(World world, Random rand)
	{
		return getPos(world, rand, 0);
	}
	
	@Override
	protected void genDecorations(Biome biome, World world, Random rand)
	{
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(world, rand, chunkPos));
		
		BiomeGenesis biomeGenesis = biome instanceof BiomeGenesis ? (BiomeGenesis) biome : null;
		
		generateOres(world, rand);
		
		if (TerrainGen.decorate(world, rand, chunkPos, SAND))
		{
			for (int i = 0; i < sandPerChunk2; i++)
			{
				sandGen.generate(world, rand, getPos(world, rand));
			}
		}
		
		if (TerrainGen.decorate(world, rand, chunkPos, CLAY))
		{
			for (int i = 0; i < clayPerChunk; i++)
			{
				clayGen.generate(world, rand, getPos(world, rand));
			}
		}
		
		if (TerrainGen.decorate(world, rand, chunkPos, TREE) && treeCountProvider != null)
		{
			for (int i = treeCountProvider.get(rand); i > 0; i--)
			{
				BlockPos pos = getPos(world, rand);
				WorldGenAbstractTree tree = biome.genBigTreeChance(rand);
				tree.setDecorationDefaults();
				
				if (tree.generate(world, rand, pos))
				{
					tree.generateSaplings(world, rand, pos);	// Allows tree gen to place saplings.
				}
			}
		}
		
		if (biomeGenesis != null)
		{
			for (DecorationEntry genEntry : biomeGenesis.getDecorations())
			{
				for (int i = genEntry.getCountPerChunk(rand); i > 0; i--)
				{
					genEntry.getGenerator().generate(world, rand, getPos(world, rand));
				}
			}
		}
		
		if (TerrainGen.decorate(world, rand, chunkPos, GRASS) && grassCountProvider != null)
		{
			for (int i = grassCountProvider.get(rand); i > 0; i--)
			{
				WorldGenerator gen = biome.getRandomWorldGenForGrass(rand);
				
				if (gen != null)
					gen.generate(world, rand, getPos(world, rand));
			}
		}
		
		if (TerrainGen.decorate(world, rand, chunkPos, FLOWERS)
				&& biomeGenesis != null && flowerCountProvider != null)
		{
			for (int i = flowerCountProvider.get(rand); i > 0; i--)
			{
				WorldGenerator gen = biomeGenesis.getRandomFlower(rand);
				
				if (gen != null)
					gen.generate(world, rand, getPos(world, rand));
			}
		}
		
		if (biomeGenesis != null)
		{
			for (DecorationEntry genEntry : biomeGenesis.getPostDecorations())
			{
				for (int i = genEntry.getCountPerChunk(rand); i > 0; i--)
				{
					genEntry.getGenerator().generate(world, rand, getPos(world, rand));
				}
			}
		}
		
		if (generateLakes)
		{
			if (TerrainGen.decorate(world, rand, chunkPos, LAKE_WATER))
			{
				for (int i = 0; i < 50; ++i)
				{
					BlockPos pos = chunkPos.add(
							rand.nextInt(16) + 8,
							rand.nextInt(rand.nextInt(rand.nextInt(247) + 8) + 1),
							rand.nextInt(16) + 8);
					new WorldGenGenesisLiquids(Blocks.FLOWING_WATER).generate(world, rand, pos);
				}
			}
			
			if (TerrainGen.decorate(world, rand, chunkPos, LAKE_LAVA))
			{
				for (int i = 0; i < 20; ++i)
				{
					BlockPos pos = chunkPos.add(
							rand.nextInt(16) + 8,
							rand.nextInt(rand.nextInt(rand.nextInt(240) + 8) + 8),
							rand.nextInt(16) + 8);
					new WorldGenGenesisLiquids(GenesisBlocks.KOMATIITIC_LAVA).generate(world, rand, pos);
				}
			}
		}
		
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(world, rand, chunkPos));
	}
	
	@Override
	protected void generateOres(World world, Random rand)
	{
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(world, rand, chunkPos));
		
		//if (TerrainGen.generateOre(currentWorld, randomGenerator, quartzGen, chunkPos, QUARTZ))
		
		genStandardOre1(world, rand, 28, radioactiveGen, 0, 64);
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
		
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(world, rand, chunkPos));
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
	
	public void setGrassCount(RandomIntProvider provider)
	{
		this.grassCountProvider = provider;
	}
	
	public void setGrassCount(int min, int max)
	{
		setGrassCount(IntRange.create(min, max));
	}
	
	public void setGrassCount(float count)
	{
		setGrassCount(new IntFromFloat(count));
	}
	
	public void setGrassCount(int count)
	{
		setGrassCount(IntRange.create(count));
	}
	
	public void setFlowerCount(RandomIntProvider provider)
	{
		this.flowerCountProvider = provider;
	}
	
	public void setFlowerCount(int min, int max)
	{
		setFlowerCount(IntRange.create(min, max));
	}
	
	public void setFlowerCount(float count)
	{
		setFlowerCount(new IntFromFloat(count));
	}
	
	public void setFlowerCount(int count)
	{
		setFlowerCount(IntRange.create(count));
	}
	
	public void setTreeCount(RandomIntProvider provider)
	{
		this.treeCountProvider = provider;
	}
	
	public void setTreeCount(int min, int max)
	{
		setTreeCount(IntRange.create(min, max));
	}
	
	public void setTreeCount(float count)
	{
		setTreeCount(new IntFromFloat(count));
	}
	
	public void setTreeCount(int count)
	{
		setTreeCount(count + 0.1F);	// 10% chance of having one extra tree in a chunk.
	}
}
