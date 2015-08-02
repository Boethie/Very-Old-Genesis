package genesis.world.biome.decorate;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CLAY;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_WATER;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;

import genesis.common.GenesisBlocks;
import genesis.world.gen.feature.WorldGenLiquidsGenesis;
import genesis.world.gen.feature.WorldGenTreeBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BiomeDecoratorGenesis extends BiomeDecorator
{
	public List<WorldGenTreeBase> trees = new ArrayList<WorldGenTreeBase>();
	public List<WorldGenDecorationBase> decorations = new ArrayList<WorldGenDecorationBase>();
	
	public BiomeDecoratorGenesis()
	{
		((WorldGenClay) clayGen).field_150546_a = GenesisBlocks.red_clay;
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
			genDecorations(biome);
			currentWorld = null;
			randomGenerator = null;
		}
	}
	
	@Override
	protected void genDecorations(BiomeGenBase biome)
	{
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(currentWorld, randomGenerator, field_180294_c));
		// generateOres();
		
		boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, CLAY);
		
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
				(new WorldGenLiquidsGenesis(Blocks.flowing_water)).generate(currentWorld, randomGenerator, pos);
			}
			
			doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, LAKE_LAVA);
			
            for (int i = 0; doGen && i < 20; ++i)
            {
            	BlockPos pos = field_180294_c.add(nextInt(16) + 8, nextInt(nextInt(nextInt(240) + 8) + 8), nextInt(16) + 8);
                (new WorldGenLiquidsGenesis(GenesisBlocks.komatiitic_lava)).generate(currentWorld, randomGenerator, pos);
            }
		}
		
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, randomGenerator, field_180294_c));
	}
	
	// Safety wrapper to prevent exceptions.
	private int nextInt(int i)
	{
		return i <= 1 ? 0 : randomGenerator.nextInt(i);
	}

}
