package genesis.world.biome.decorate;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CLAY;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_WATER;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SAND;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisConfig;
import genesis.metadata.EnumOre;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import genesis.world.gen.feature.WorldGenGenesisLiquids;
import genesis.world.gen.feature.WorldGenGenesisSand;
import genesis.world.gen.feature.WorldGenMinableGenesis;
import genesis.world.gen.feature.WorldGenTreeBase;
import genesis.world.gen.feature.WorldGenUndergroundColumns;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenClay;
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
		((WorldGenClay) clayGen).field_150546_a = GenesisBlocks.red_clay;
		sandGen = new WorldGenGenesisSand(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT), 7);
	}
	
	@Override
	public void decorate(World world, Random random, BiomeGenBase biome, BlockPos chunkStart)
	{
		if (currentWorld != null)
		{
			throw new RuntimeException("Already decorating");
		}
		else
		{
			currentWorld = world;
			randomGenerator = random;
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
			manganeseGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.MANGANESE), 1, 3);
			hematiteGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.HEMATITE), 4, 8);
			malachiteGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.MALACHITE), 2, 4);
			azuriteGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.AZURITE), 2, 4);
			olivineGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.OLIVINE), 1, 4);
			flintGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.FLINT), 4, 8, GenesisBlocks.limestone);
			marcasiteGen = new WorldGenMinableGenesis(GenesisBlocks.ores.getOreState(EnumOre.MARCASITE), 1, 3, GenesisBlocks.limestone);
			genDecorations(biome);
			currentWorld = null;
			randomGenerator = null;
		}
	}
	
	@Override
	protected void genDecorations(BiomeGenBase biome)
	{
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(currentWorld, randomGenerator, field_180294_c));
		
		for (int i = 0; nextInt(10) > 2 && i < 8; ++i)
		{
			BlockPos pos = field_180294_c.add(nextInt(16) + 8, nextInt(nextInt(248) + 8), nextInt(16) + 8);
			(new WorldGenUndergroundColumns(randomGenerator, 18)).generate(currentWorld, randomGenerator, pos);
			(new WorldGenUndergroundColumns(randomGenerator, 7)).generate(currentWorld, randomGenerator, pos.add(1 + nextInt(2), 0, 1 + nextInt(2)));
			(new WorldGenUndergroundColumns(randomGenerator, 7)).generate(currentWorld, randomGenerator, pos.add(1 + nextInt(2), 0, 1 + nextInt(2)));
			(new WorldGenUndergroundColumns(randomGenerator, 7)).generate(currentWorld, randomGenerator, pos.add(1 + nextInt(2), 0, 1 + nextInt(2)));
		}
		
		for (int i = 0; nextInt(3) == 0 && i < 4; ++i)
		{
			BlockPos pos = field_180294_c.add(nextInt(16) + 8, nextInt(nextInt(248) + 8), nextInt(16) + 8);
			(new WorldGenUndergroundColumns(randomGenerator, 7)).generate(currentWorld, randomGenerator, pos);
		}
		
		generateOres();

        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, SAND);
        
        for (int i = 0; doGen && i < sandPerChunk2; ++i)
        {
            int x = nextInt(16) + 8;
            int z = nextInt(16) + 8;
            sandGen.generate(currentWorld, randomGenerator, currentWorld.getTopSolidOrLiquidBlock(field_180294_c.add(x, 0, z)));
        }
		
		doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, CLAY);
		
		for (int i = 0; doGen && i < clayPerChunk; ++i)
		{
			int x = nextInt(16) + 8;
			int z = nextInt(16) + 8;
			clayGen.generate(currentWorld, randomGenerator, currentWorld.getTopSolidOrLiquidBlock(field_180294_c.add(x, 0, z)));
		}
		
		doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, TREE);
		
		for (int i = 0; doGen && i < trees.size(); ++i)
		{
			int count = trees.get(i).getTreeCountPerChunk();
			if (nextInt(10) == 0) ++count;
			
			for (int j = 0; j < count; ++j)
			{
				int x = nextInt(16) + 8;
				int z = nextInt(16) + 8;
				int y = nextInt(currentWorld.getHeight(field_180294_c.add(x, 0, z)).getY() * 2);
				trees.get(i).generate(currentWorld, randomGenerator, field_180294_c.add(x, y, z));
			}
		}
		
		doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, GRASS);
		
		for (int i = 0; doGen && i < grassPerChunk; ++i)
		{
			int x = nextInt(16) + 8;
			int z = nextInt(16) + 8;
			int y = nextInt(currentWorld.getHeight(field_180294_c.add(x, 0, z)).getY() * 2);
			biome.getRandomWorldGenForGrass(randomGenerator).generate(currentWorld, randomGenerator, field_180294_c.add(x, y, z));
		}
		
		doGen = true;//TODO
		
		for (int i = 0; i < decorations.size(); ++ i)
		{
			for (int j = 0; doGen && j < decorations.get(i).getCountPerChunk(); ++j)
			{
				int x = nextInt(16) + 8;
				int z = nextInt(16) + 8;
				int y = nextInt(currentWorld.getHeight(field_180294_c.add(x, 0, z)).getY() * 2);
				decorations.get(i).generate(currentWorld, randomGenerator, field_180294_c.add(x, y, z));
			}
		}
		
		if (generateLakes)
		{
			doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, LAKE_WATER);
			
			for (int i = 0; doGen && i < 50; ++i)
			{
				BlockPos pos = field_180294_c.add(nextInt(16) + 8, nextInt(nextInt(248) + 8), nextInt(16) + 8);
				(new WorldGenGenesisLiquids(Blocks.flowing_water)).generate(currentWorld, randomGenerator, pos);
			}
			
			doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, LAKE_LAVA);
			
            for (int i = 0; doGen && i < 20; ++i)
            {
            	BlockPos pos = field_180294_c.add(nextInt(16) + 8, nextInt(nextInt(nextInt(240) + 8) + 8), nextInt(16) + 8);
                (new WorldGenGenesisLiquids(GenesisBlocks.komatiitic_lava)).generate(currentWorld, randomGenerator, pos);
            }
		}
		
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, randomGenerator, field_180294_c));
	}
	
	@Override
	protected void generateOres()
	{
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(currentWorld, randomGenerator, field_180294_c));
		
		//if (TerrainGen.generateOre(currentWorld, randomGenerator, quartzGen, field_180294_c, QUARTZ))
		
		genStandardOre1(62, komatiiteGen, 0, 16);
		genStandardOre1(20, gneissGen, 0, 64);
		genStandardOre1(11, rhyoliteGen, 64, 128);
		genStandardOre1(11, doleriteGen, 64, 128);
		genStandardOre1(8, trondhjemiteGen, 0, 128);
		genStandardOre1(5, fauxGen, 0, 64);
		genStandardOre1(5, anorthositeGen, 0, 128);
		
		genStandardOreByLayers(quartzGen, GenesisConfig.quartzCount, 5, 55, 128, 131);
		genStandardOreByLayers(zirconGen, GenesisConfig.zirconCount, 5, 55, 128, 131);
		genStandardOreByLayers(garnetGen, GenesisConfig.garnetCount, 5, 55, 128, 131);
		genStandardOreByLayers(hematiteGen, GenesisConfig.hematiteCount, 40, 68, 128, 131);
		genStandardOreByLayers(manganeseGen, GenesisConfig.manganeseCount, 40, 68, 128, 131);
		genStandardOreByLayers(malachiteGen, GenesisConfig.malachiteCount, 40, 68, 128, 131);
		genStandardOreByLayers(azuriteGen, GenesisConfig.azuriteCount, 40, 68, 128, 131);
		genStandardOreByLayers(olivineGen, GenesisConfig.olivineCount, 5, 13, 13, 15);
		genStandardOreByLayers(flintGen, GenesisConfig.flintCount, 40, 67, 128, 131);
		genStandardOreByLayers(marcasiteGen, GenesisConfig.marcasiteCount, 40, 67, 128, 131);
		
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(currentWorld, randomGenerator, field_180294_c));
	}
	
	private void genStandardOreByLayers(WorldGenerator gen, int count, int lower, int midLower, int midUpper, int upper)
	{
		genStandardOre1(count, gen, lower, midLower - 1);
		genStandardOre1(((int)(count * 0.6)), gen, midLower, midUpper);
		genStandardOre1(((int)(count * 0.1)), gen, midUpper + 1, upper);
	}
	
	// Safety wrapper to prevent exceptions.
	private int nextInt(int i)
	{
		return i <= 1 ? 0 : randomGenerator.nextInt(i);
	}
}
