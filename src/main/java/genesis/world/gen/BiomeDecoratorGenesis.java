package genesis.world.gen;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CACTUS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CLAY;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.DEAD_BUSH;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.FLOWERS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_WATER;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LILYPAD;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.PUMPKIN;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.REED;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SAND;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SAND_PASS2;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.ANDESITE;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.COAL;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.DIAMOND;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.DIORITE;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.DIRT;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.GOLD;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.GRANITE;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.GRAVEL;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.IRON;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.LAPIS;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.REDSTONE;
import genesis.common.GenesisBlocks;
import genesis.world.gen.feature.WorldGenClayGenesis;
import genesis.world.gen.feature.WorldGenMinableGenesis;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BiomeDecoratorGenesis extends BiomeDecorator
{
	/**
	 * The dirt and ore generators.
	 */
	public WorldGenerator dirtGen;
	public WorldGenerator gravelGen;
	public WorldGenerator quartzGen;
	public WorldGenerator malachiteGen;
	public WorldGenerator hematiteGen;
	public WorldGenerator zirconGen;
	public WorldGenerator garnetGen;
	public WorldGenerator olivineGen;
	/**
	 * The number of dirt patches to attempt to generate per chunk.
	 */
	public int dirtPerChunk;

	public BiomeDecoratorGenesis()
	{
		super();

		this.dirtGen = new WorldGenMinableGenesis(Blocks.dirt, 32);
		this.gravelGen = new WorldGenMinableGenesis(Blocks.gravel, 32);
		this.quartzGen = new WorldGenMinableGenesis(GenesisBlocks.quartz_ore, 6, GenesisBlocks.quartzite);
		this.malachiteGen = new WorldGenMinableGenesis(GenesisBlocks.malachite_ore, 3);
		this.hematiteGen = new WorldGenMinableGenesis(GenesisBlocks.hematite_ore, 10);
		this.zirconGen = new WorldGenMinableGenesis(GenesisBlocks.zircon_ore, 8);
		this.garnetGen = new WorldGenMinableGenesis(GenesisBlocks.garnet_ore, 8, GenesisBlocks.faux_amphibolite);
		this.olivineGen = new WorldGenMinableGenesis(GenesisBlocks.olivine_ore, 10, GenesisBlocks.komatiite);
		this.clayGen = new WorldGenClayGenesis(4);
		this.sandGen = new WorldGenSand(Blocks.sand, 7);
        this.gravelAsSandGen = new WorldGenSand(GenesisBlocks.limestone, 6);
	}

	public void decorate(World worldIn, Random p_180292_2_, BiomeGenBase p_180292_3_, BlockPos p_180292_4_)
	{
		if (this.currentWorld != null)
		{
			throw new RuntimeException("Already decorating");
		}
		else
		{
			this.currentWorld = worldIn;
			this.randomGenerator = p_180292_2_;
			this.field_180294_c = p_180292_4_;
			this.genDecorations(p_180292_3_);
			this.currentWorld = null;
			this.randomGenerator = null;
		}
	}

	protected void genDecorations(BiomeGenBase p_150513_1_)
	{
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(currentWorld, randomGenerator, field_180294_c));
		this.generateOres();
		int triesForTrees;
		int triesForWaterLakes = 50;
		int triesForLavaLakes = 20;
		int i, xPos, zPos;
		boolean doGen;
		
		doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, SAND);
        for (i = 0; doGen && i < this.sandPerChunk2; ++i)
        {
        	xPos = this.randomGenerator.nextInt(16) + 8;
        	zPos = this.randomGenerator.nextInt(16) + 8;
            this.sandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(xPos, 0, zPos)));
        }
		
		doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, CLAY);
        for (i = 0; doGen && i < this.clayPerChunk; ++i)
        {
        	xPos = this.randomGenerator.nextInt(16) + 8;
        	zPos = this.randomGenerator.nextInt(16) + 8;
            this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(xPos, 0, zPos)));
        }
        
        doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, SAND_PASS2);
        for (i = 0; doGen && i < this.sandPerChunk; ++i)
        {
        	xPos = this.randomGenerator.nextInt(16) + 8;
        	zPos = this.randomGenerator.nextInt(16) + 8;
            this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(xPos, 0, zPos)));
        }

		triesForTrees = this.treesPerChunk;

		if (this.randomGenerator.nextInt(10) == 0)
		{
			++triesForTrees;
		}

		BlockPos blockpos;

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, TREE);
		for (i = 0; doGen && i < triesForTrees; ++i)
		{
			xPos = this.randomGenerator.nextInt(16) + 8;
			zPos = this.randomGenerator.nextInt(16) + 8;
			WorldGenAbstractTree worldgenabstracttree = p_150513_1_.genBigTreeChance(this.randomGenerator);
			worldgenabstracttree.func_175904_e();
			blockpos = this.currentWorld.getHorizon(this.field_180294_c.add(xPos, 0, zPos));

			if (worldgenabstracttree.generate(this.currentWorld, this.randomGenerator, blockpos))
			{
				worldgenabstracttree.func_180711_a(this.currentWorld, this.randomGenerator, blockpos);
			}
		}

		if (this.generateLakes)
		{
			BlockPos blockpos1;

			doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, LAKE_WATER);
			for (i = 0; doGen && i < triesForWaterLakes; ++i)
			{
				blockpos1 = this.field_180294_c.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(this.randomGenerator.nextInt(248) + 8), this.randomGenerator.nextInt(16) + 8);
				(new WorldGenLiquids(Blocks.flowing_water)).generate(this.currentWorld, this.randomGenerator, blockpos1);
			}

			doGen = TerrainGen.decorate(currentWorld, randomGenerator, field_180294_c, LAKE_LAVA);
			for (i = 0; doGen && i < triesForLavaLakes; ++i)
			{
				blockpos1 = this.field_180294_c.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8), this.randomGenerator.nextInt(16) + 8);
				(new WorldGenLiquids(/*TODO: Change this to komatiitic lava*/Blocks.flowing_lava)).generate(this.currentWorld, this.randomGenerator, blockpos1);
			}
		}

		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, randomGenerator, field_180294_c));
	}

	protected void generateOres()
	{
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(currentWorld, randomGenerator, field_180294_c));
		/*this.genStandardOre1(20, this.dirtGen, 0, 256);
        this.genStandardOre1(10, this.gravelGen, 0, 256);*/
		this.genStandardOre1(20, this.quartzGen, 24, 128);
		this.genStandardOre1(12, this.malachiteGen, 0, 96);
		this.genStandardOre1(12, this.hematiteGen, 0, 64);
		this.genStandardOre1(8, this.zirconGen, 0, 64);
		this.genStandardOre1(4, this.garnetGen, 24, 48);
		this.genStandardOre1(1, this.olivineGen, 0, 16);
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(currentWorld, randomGenerator, field_180294_c));
	}

	private int nextInt(int i) 
	{ 
		//Safety wrapper to prevent exceptions.
		if (i <= 1)
			return 0;
		return this.randomGenerator.nextInt(i);
	}

}