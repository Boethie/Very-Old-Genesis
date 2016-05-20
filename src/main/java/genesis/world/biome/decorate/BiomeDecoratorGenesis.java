package genesis.world.biome.decorate;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.*;

import java.util.Random;

import com.google.common.base.Predicates;

import genesis.combo.*;
import genesis.combo.variant.*;
import genesis.common.*;
import genesis.util.random.i.*;
import genesis.world.biome.BiomeGenBaseGenesis;
import genesis.world.biome.DecorationEntry;
import genesis.world.gen.feature.*;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.*;

public class BiomeDecoratorGenesis extends BiomeDecorator
{
	private static final IntRange RANGE = IntRange.create(8, 23);
	
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

	public RandomIntProvider grassCountProvider;
	public RandomIntProvider flowerCountProvider;
	public RandomIntProvider treeCountProvider;
	
	public BiomeDecoratorGenesis()
	{
		clayGen = new WorldGenCircleReplacement((s) -> s.getMaterial() == Material.water,
				4, 1,
				Predicates.or(
						BlockStateMatcher.forBlock(Blocks.dirt),
						BlockStateMatcher.forBlock(GenesisBlocks.ooze)),
				GenesisBlocks.red_clay.getDefaultState());
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
	
	protected BlockPos getPos(World world, Random rand, int topStart)
	{
		return world.getHeight(field_180294_c.add(RANGE.get(rand), 0, RANGE.get(rand))).up(topStart);
	}
	
	protected BlockPos getPos(World world, Random rand)
	{
		return getPos(world, rand, 0);
	}
	
	@Override
	protected void genDecorations(BiomeGenBase biome, World world, Random rand)
	{
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(world, rand, field_180294_c));
		
		BiomeGenBaseGenesis biomeGenesis = biome instanceof BiomeGenBaseGenesis ? (BiomeGenBaseGenesis) biome : null;
		
		generateOres(world, rand);
		
		if (TerrainGen.decorate(world, rand, field_180294_c, SAND))
		{
			for (int i = 0; i < sandPerChunk2; i++)
			{
				sandGen.generate(world, rand, getPos(world, rand));
			}
		}
		
		if (TerrainGen.decorate(world, rand, field_180294_c, CLAY))
		{
			for (int i = 0; i < clayPerChunk; i++)
			{
				clayGen.generate(world, rand, getPos(world, rand));
			}
		}
		
		if (TerrainGen.decorate(world, rand, field_180294_c, TREE) && treeCountProvider != null)
		{
			for (int i = treeCountProvider.get(rand); i > 0; i--)
			{
				BlockPos pos = getPos(world, rand);
				WorldGenAbstractTree tree = biome.genBigTreeChance(rand);
				tree.func_175904_e();
				
				if (tree.generate(world, rand, pos))
				{
					tree.func_180711_a(world, rand, pos);	// Allows tree gen to place saplings.
				}
			}
		}
		
		for (DecorationEntry genEntry : biomeGenesis.getDecorations())
		{
			for (int i = genEntry.getCountPerChunk(rand); i > 0; i--)
			{
				genEntry.getGenerator().generate(world, rand, getPos(world, rand));
			}
		}
		
		if (TerrainGen.decorate(world, rand, field_180294_c, GRASS) && grassCountProvider != null)
		{
			for (int i = grassCountProvider.get(rand); i > 0; i--)
			{
				WorldGenerator gen = biome.getRandomWorldGenForGrass(rand);
				
				if (gen != null)
					gen.generate(world, rand, getPos(world, rand));
			}
		}
		
		if (TerrainGen.decorate(world, rand, field_180294_c, FLOWERS)
				&& biomeGenesis != null && flowerCountProvider != null)
		{
			for (int i = flowerCountProvider.get(rand); i > 0; i--)
			{
				WorldGenerator gen = biomeGenesis.getRandomFlower(rand);
				
				if (gen != null)
					gen.generate(world, rand, getPos(world, rand));
			}
		}
		
		if (generateLakes)
		{
			if (TerrainGen.decorate(world, rand, field_180294_c, LAKE_WATER))
			{
				for (int i = 0; i < 50; ++i)
				{
					BlockPos pos = field_180294_c.add(
							rand.nextInt(16) + 8,
							rand.nextInt(rand.nextInt(rand.nextInt(247) + 8) + 1),
							rand.nextInt(16) + 8);
					new WorldGenGenesisLiquids(Blocks.flowing_water).generate(world, rand, pos);
				}
			}
			
			if (TerrainGen.decorate(world, rand, field_180294_c, LAKE_LAVA))
			{
				for (int i = 0; i < 20; ++i)
				{
					BlockPos pos = field_180294_c.add(
							rand.nextInt(16) + 8,
							rand.nextInt(rand.nextInt(rand.nextInt(240) + 8) + 8),
							rand.nextInt(16) + 8);
					new WorldGenGenesisLiquids(GenesisBlocks.komatiitic_lava).generate(world, rand, pos);
				}
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
