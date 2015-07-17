package genesis.world.biome.decorate;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CLAY;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_WATER;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;
import genesis.common.GenesisBlocks;
import genesis.world.gen.feature.WorldGenTreeBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BiomeDecoratorGenesis extends BiomeDecorator
{
	public int odontopterisPerChunk = 0;
	public boolean generateDefaultTrees = true;
	
	public List<WorldGenTreeBase> trees = new ArrayList<WorldGenTreeBase>();
	
	public BiomeDecoratorGenesis()
	{
		WorldGenClay clayGen = new WorldGenClay(4);
		clayGen.field_150546_a = GenesisBlocks.red_clay;
		this.clayGen = clayGen;
		this.generateLakes = true;
	}
	
	@Override
	public void decorate(World world, Random random, BiomeGenBase biome, BlockPos chunkStart)
	{
		if (this.currentWorld != null)
		{
			throw new RuntimeException("Already decorating");
		}
		else
		{
			this.currentWorld = world;

			this.randomGenerator = random;
			this.field_180294_c = chunkStart;

			this.genDecorations(biome);
			this.currentWorld = null;
			this.randomGenerator = null;
		}
	}

	@Override
	protected void genDecorations(BiomeGenBase p_150513_1_)
	{
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(this.currentWorld, this.randomGenerator, this.field_180294_c));
		// this.generateOres();
		int i;
		int j;
		int k;

		boolean doGen = TerrainGen.decorate(this.currentWorld, this.randomGenerator, this.field_180294_c, CLAY);
		for (i = 0; doGen && i < this.clayPerChunk; ++i)
		{
			j = this.randomGenerator.nextInt(16) + 8;
			k = this.randomGenerator.nextInt(16) + 8;
			this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(j, 0, k)));
		}

		i = this.treesPerChunk;

		if (this.randomGenerator.nextInt(10) == 0 && this.generateDefaultTrees)
		{
			++i;
		}

		int l;
		BlockPos blockpos;

		doGen = TerrainGen.decorate(this.currentWorld, this.randomGenerator, this.field_180294_c, TREE);
		for (j = 0; doGen && j < i; ++j)
		{
			k = this.randomGenerator.nextInt(16) + 8;
			l = this.randomGenerator.nextInt(16) + 8;
			WorldGenAbstractTree worldgenabstracttree = p_150513_1_.genBigTreeChance(this.randomGenerator);
			worldgenabstracttree.func_175904_e();
			blockpos = this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l));

			if (worldgenabstracttree.generate(this.currentWorld, this.randomGenerator, blockpos))
			{
				worldgenabstracttree.func_180711_a(this.currentWorld, this.randomGenerator, blockpos);
			}
		}

		int i1;

		// TODO change to deadlog
		/*
		 * doGen = TerrainGen.decorate(this.currentWorld, this.randomGenerator, this.field_180294_c, FLOWERS);
		 * for (j = 0; doGen && j < this.flowersPerChunk; ++j)
		 * {
		 * k = this.randomGenerator.nextInt(16) + 8;
		 * l = this.randomGenerator.nextInt(16) + 8;
		 * i1 = this.nextInt(this.currentWorld.getHorizon(this.field_180294_c.add(k, 0, l)).getY() + 32);
		 * blockpos = this.field_180294_c.add(k, i1, l);
		 * BlockFlower.EnumFlowerType enumflowertype = p_150513_1_.pickRandomFlower(this.randomGenerator, blockpos);
		 * BlockFlower blockflower = enumflowertype.getBlockType().getBlock();
		 * 
		 * if (blockflower.getMaterial() != Material.air)
		 * {
		 * this.yellowFlowerGen.setGeneratedBlock(blockflower, enumflowertype);
		 * this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, blockpos);
		 * }
		 * }
		 */
		
		// TALL GRASS
		
		doGen = TerrainGen.decorate(this.currentWorld, this.randomGenerator, this.field_180294_c, GRASS);
		
		for (j = 0; doGen && j < this.grassPerChunk; ++j)
		{
			k = this.randomGenerator.nextInt(16) + 8;
			l = this.randomGenerator.nextInt(16) + 8;
			i1 = this.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() * 2);
			p_150513_1_.getRandomWorldGenForGrass(this.randomGenerator).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k, i1, l));
		}
		
		for (j = 0; doGen && j < this.odontopterisPerChunk; ++j)
		{
			k = this.randomGenerator.nextInt(16) + 8;
			l = this.randomGenerator.nextInt(16) + 8;
			i1 = this.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() * 2);
			(new WorldGenOdontopteris()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k, i1, l));
		}
		
		doGen = true; // TerrainGen.decorate(this.currentWorld, this.randomGenerator, this.field_180294_c, TREE);
		
		for (int it = 0; it < trees.size(); ++it)
		{
			for (j = 0; j < trees.get(it).getTreeCountPerChunk(); ++j)
			{
				k = this.randomGenerator.nextInt(16) + 8;
				l = this.randomGenerator.nextInt(16) + 8;
				i1 = this.nextInt(this.currentWorld.getHeight(this.field_180294_c.add(k, 0, l)).getY() * 2);
				trees.get(it).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k, i1, l));
			}
		}
		
		if (this.generateLakes)
		{
			BlockPos blockpos1;

			doGen = TerrainGen.decorate(this.currentWorld, this.randomGenerator, this.field_180294_c, LAKE_WATER);
			for (j = 0; doGen && j < 50; ++j)
			{
				blockpos1 = this.field_180294_c.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(this.randomGenerator.nextInt(248) + 8), this.randomGenerator.nextInt(16) + 8);
				(new WorldGenLiquids(Blocks.flowing_water)).generate(this.currentWorld, this.randomGenerator, blockpos1);
			}

			/*
			 * doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, LAKE_LAVA);
			 * for (j = 0; doGen && j < 20; ++j)
			 * {
			 * blockpos1 = this.field_180294_c.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8), this.randomGenerator.nextInt(16) + 8);
			 * (new WorldGenLiquids(Blocks.flowing_lava)).generate(this.currentWorld, this.randomGenerator, blockpos1);
			 * }
			 */
		}

		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(this.currentWorld, this.randomGenerator, this.field_180294_c));
	}

	private int nextInt(int i)
	{ // Safety wrapper to prevent exceptions.
		if (i <= 1)
		{
			return 0;
		}
		return this.randomGenerator.nextInt(i);
	}

}
