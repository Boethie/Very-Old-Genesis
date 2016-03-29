package genesis.world;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.*;

import java.util.List;
import java.util.Random;

import genesis.common.*;
import genesis.world.gen.*;
import genesis.world.gen.feature.*;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.*;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.*;

public class ChunkGeneratorGenesis implements IChunkGenerator
{
	protected World world;
	protected Random rand;
	protected boolean mapFeatures;
	
	protected ChunkProviderSettings settings;
	
	protected IBlockState liquid = Blocks.water.getDefaultState();
	
	protected MapGenBase caveGen = new MapGenCavesGenesis();
	
	protected BiomeGenBase[] biomes;
	
	protected NoiseGeneratorOctaves lPerlin2NoiseGen;
	protected NoiseGeneratorOctaves lPerlinNoiseGen;
	protected NoiseGeneratorOctaves perlinNoiseGen;
	protected NoiseGeneratorPerlin heightNoiseGen;
	protected NoiseGeneratorOctaves scaleNoiseGen;	// Unused
	protected NoiseGeneratorOctaves depthNoiseGen;
	protected NoiseGeneratorOctaves forestNoiseGen;	// Unused
	
	protected double[] heightmap;
	protected double[] perlinNoise;
	protected double[] stoneNoise;
	protected double[] lPerlin2Noise;
	protected double[] lPerlinNoise;
	protected double[] depthNoise;
	protected float[] distances;
	
	public ChunkGeneratorGenesis(World world, long seed, boolean mapFeatures, String generatorOptions)
	{
		this.world = world;
		rand = new Random(seed);
		this.mapFeatures = mapFeatures;
		
		// TODO: Create our own map settings JSON.
		if (generatorOptions != null)
		{
			ChunkProviderSettings.Factory factory = ChunkProviderSettings.Factory.jsonToFactory(generatorOptions);
			factory.useDungeons = false;
			factory.useStrongholds = false;
			factory.useVillages = false;
			factory.useMineShafts = false;
			factory.useTemples = false;
			factory.useMonuments = false;
			settings = factory.func_177864_b();	// build
			liquid = (settings.useLavaOceans ? Blocks.lava : Blocks.water).getDefaultState();
		}
		
		lPerlin2NoiseGen = new NoiseGeneratorOctaves(rand, 16);
		lPerlinNoiseGen = new NoiseGeneratorOctaves(rand, 16);
		perlinNoiseGen = new NoiseGeneratorOctaves(rand, 8);
		heightNoiseGen = new NoiseGeneratorPerlin(rand, 4);
		scaleNoiseGen = new NoiseGeneratorOctaves(rand, 10);
		depthNoiseGen = new NoiseGeneratorOctaves(rand, 16);
		forestNoiseGen = new NoiseGeneratorOctaves(rand, 8);
		
		InitNoiseGensEvent.ContextOverworld ctx =
				new InitNoiseGensEvent.ContextOverworld(
						lPerlin2NoiseGen,
						lPerlinNoiseGen,
						perlinNoiseGen,
						heightNoiseGen,
						scaleNoiseGen,
						depthNoiseGen,
						forestNoiseGen);
		ctx = TerrainGen.getModdedNoiseGenerators(world, rand, ctx);
		lPerlin2NoiseGen = ctx.getLPerlin1();
		lPerlinNoiseGen = ctx.getLPerlin2();
		perlinNoiseGen = ctx.getPerlin();
		heightNoiseGen = ctx.getHeight();
		scaleNoiseGen = ctx.getScale();
		depthNoiseGen = ctx.getDepth();
		forestNoiseGen = ctx.getForest();
		
		distances = new float[25];

		for (int i = -2; i <= 2; ++i)
		{
			for (int j = -2; j <= 2; ++j)
			{
				distances[i + 2 + (j + 2) * 5] = 10 / MathHelper.sqrt_float(i * i + j * j + 0.2F);
			}
		}
	}
	
	@Override
	public void populate(int chunkX, int chunkZ)
	{
		BlockFalling.fallInstantly = true;
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;
		BlockPos pos = new BlockPos(blockX, 0, blockZ);
		BiomeGenBase biome = world.getBiomeGenForCoords(pos.add(16, 0, 16));
		rand.setSeed(world.getSeed());
		long xSeed = rand.nextLong() / 2L * 2L + 1L;
		long ySeed = rand.nextLong() / 2L * 2L + 1L;
		rand.setSeed(chunkX * xSeed + chunkZ * ySeed ^ world.getSeed());
		boolean village = false;
		
		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(this, world, rand, chunkX, chunkZ, village));
		
		int waterLakeChance = settings.waterLakeChance;
		
		if (biome == GenesisBiomes.marsh)
			waterLakeChance = 1;
		if (biome == GenesisBiomes.rainforest)
			waterLakeChance = 2;
		if (biome == GenesisBiomes.rainforestHills)
			waterLakeChance = 2;
		if (biome == GenesisBiomes.rainforestM)
			waterLakeChance = 2;
		/*
		if (
				biome == GenesisBiomes.redLowlands
				|| biome == GenesisBiomes.redLowlandsM
				|| biome == GenesisBiomes.redLowlandsHills)
		{
			for (int i = 0; i < 24; ++i)
			{
				int x = rand.nextInt(8) + 8;
				int z = rand.nextInt(8) + 8;
				int y = 128;
				
				new WorldGenGenesisSurfacePatch(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT)).generate(world, rand, pos.add(x, y, z));
			}
		}
		/*
		if (rand.nextInt(48) == 0)
		{
			int x = rand.nextInt(8) + 8;
			int z = rand.nextInt(8) + 8;
			int y = 128;
			
			new WorldGenCrater().generate(world, rand, pos.add(x, y, z));
		}*/
		
		if (settings.useWaterLakes 
			&& !village 
			&& rand.nextInt(waterLakeChance) == 0 
			&& TerrainGen.populate(this, world, rand, chunkX, chunkZ, village, LAKE))
		{
			int x = rand.nextInt(16) + 8;
			int y = rand.nextInt(256);
			int z = rand.nextInt(16) + 8;
			new WorldGenGenesisLakes(Blocks.water.getDefaultState()).generate(world, rand, pos.add(x, y, z));
		}
		
		if (TerrainGen.populate(this, world, rand, chunkX, chunkZ, village, LAVA) && !village && rand.nextInt(settings.lavaLakeChance / 10) == 0 && settings.useLavaLakes)
		{
			int x = rand.nextInt(16) + 8;
			int y = rand.nextInt(rand.nextInt(248) + 8);
			int z = rand.nextInt(16) + 8;
			
			if (y < 63 || rand.nextInt(settings.lavaLakeChance / 8) == 0)
			{
				new WorldGenGenesisLakes(GenesisBlocks.komatiitic_lava.getDefaultState()).generate(world, rand, pos.add(x, y, z));
			}
		}
		
		if (settings.useDungeons)
		{
			boolean doGen = TerrainGen.populate(this, world, rand, chunkX, chunkZ, village, DUNGEON);
			for (int x = 0; doGen && x < settings.dungeonChance; ++x)
			{
				int y = rand.nextInt(16) + 8;
				int z = rand.nextInt(256);
				int j2 = rand.nextInt(16) + 8;
				(new WorldGenDungeons()).generate(world, rand, pos.add(y, z, j2));
			}
		}
		
		biome.decorate(world, rand, new BlockPos(blockX, 0, blockZ));
		
		if (TerrainGen.populate(this, world, rand, chunkX, chunkZ, village, ANIMALS))
		{
			WorldEntitySpawner.performWorldGenSpawning(world, biome, blockX + 8, blockZ + 8, 16, 16, rand);
		}
		
		pos = pos.add(8, 0, 8);
		
		boolean doGen = TerrainGen.populate(this, world, rand, chunkX, chunkZ, village, ICE);
		
		for (int x = 0; doGen && x < 16; ++x)
		{
			for (int y = 0; y < 16; ++y)
			{
				BlockPos surface = world.getPrecipitationHeight(pos.add(x, 0, y));
				BlockPos water = surface.down();
				
				if (world.canBlockFreezeNoWater(water))
				{
					world.setBlockState(water, Blocks.ice.getDefaultState(), 2);
				}
				
				if (world.canSnowAt(surface, true))
				{
					world.setBlockState(surface, Blocks.snow_layer.getDefaultState(), 2);
				}
			}
		}
		
		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(this, world, rand, chunkX, chunkZ, village));
		
		BlockFalling.fallInstantly = false;
	}
	
	public void setBlocksInChunk(int x, int y, ChunkPrimer primer)
	{
		biomes = world.getBiomeProvider().getBiomesForGeneration(biomes, x * 4 - 2, y * 4 - 2, 10, 10);
		generateHeightmap(x * 4, 0, y * 4);
		
		for (int k = 0; k < 4; ++k)
		{
			int l = k * 5;
			int i1 = (k + 1) * 5;
			
			for (int j1 = 0; j1 < 4; ++j1)
			{
				int k1 = (l + j1) * 33;
				int l1 = (l + j1 + 1) * 33;
				int i2 = (i1 + j1) * 33;
				int j2 = (i1 + j1 + 1) * 33;
				
				for (int k2 = 0; k2 < 32; ++k2)
				{
					double d0 = 0.125D;
					double d1 = heightmap[k1 + k2];
					double d2 = heightmap[l1 + k2];
					double d3 = heightmap[i2 + k2];
					double d4 = heightmap[j2 + k2];
					double d5 = (heightmap[k1 + k2 + 1] - d1) * d0;
					double d6 = (heightmap[l1 + k2 + 1] - d2) * d0;
					double d7 = (heightmap[i2 + k2 + 1] - d3) * d0;
					double d8 = (heightmap[j2 + k2 + 1] - d4) * d0;
					
					for (int l2 = 0; l2 < 8; ++l2)
					{
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;
						
						for (int i3 = 0; i3 < 4; ++i3)
						{
							double d14 = 0.25D;
							double d16 = (d11 - d10) * d14;
							double d15 = d10 - d16;
							
							for (int j3 = 0; j3 < 4; ++j3)
							{
								if ((d15 += d16) > 0.0D)
								{
									primer.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + j3, GenesisBlocks.granite.getDefaultState());
								}
								else if (k2 * 8 + l2 < settings.seaLevel)
								{
									primer.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + j3, liquid);
								}
							}
							
							d10 += d12;
							d11 += d13;
						}
						
						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	private void generateHeightmap(int x, int y, int z)
	{
		depthNoise = depthNoiseGen.generateNoiseOctaves(depthNoise, x, z, 5, 5,
				settings.depthNoiseScaleX, settings.depthNoiseScaleZ, settings.depthNoiseScaleExponent);
		float horizScale = settings.coordinateScale;
		float vertScale = settings.heightScale;
		perlinNoise = perlinNoiseGen.generateNoiseOctaves(perlinNoise, x, y, z, 5, 33, 5,
				horizScale / settings.mainNoiseScaleX, vertScale / settings.mainNoiseScaleY, horizScale / settings.mainNoiseScaleZ);
		lPerlin2Noise = lPerlin2NoiseGen.generateNoiseOctaves(lPerlin2Noise, x, y, z, 5, 33, 5,
				horizScale, vertScale, horizScale);
		lPerlinNoise = lPerlinNoiseGen.generateNoiseOctaves(lPerlinNoise, x, y, z, 5, 33, 5,
				horizScale, vertScale, horizScale);
		
		z = 0;
		x = 0;
		int i = 0;
		int j = 0;
		
		for (int k = 0; k < 5; ++k)
		{
			for (int l = 0; l < 5; ++l)
			{
				float f2 = 0.0F;
				float f3 = 0.0F;
				float f4 = 0.0F;
				int i1 = 2;
				BiomeGenBase biomegenbase = biomes[k + 2 + (l + 2) * 10];
				
				for (int j1 = -i1; j1 <= i1; ++j1)
				{
					for (int k1 = -i1; k1 <= i1; ++k1)
					{
						BiomeGenBase biomegenbase1 = biomes[k + j1 + 2 + (l + k1 + 2) * 10];
						float f5 = settings.biomeDepthOffSet + biomegenbase1.getBaseHeight() * settings.biomeDepthWeight;
						float f6 = settings.biomeScaleOffset + biomegenbase1.getHeightVariation() * settings.biomeScaleWeight;
						
						if (world.getWorldInfo().getTerrainType() == WorldType.AMPLIFIED && f5 > 0.0F)
						{
							f5 = 1.0F + f5 * 2.0F;
							f6 = 1.0F + f6 * 4.0F;
						}
						
						float f7 = distances[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);
						
						if (biomegenbase1.getBaseHeight() > biomegenbase.getBaseHeight())
						{
							f7 /= 2.0F;
						}
						
						f2 += f6 * f7;
						f3 += f5 * f7;
						f4 += f7;
					}
				}
				
				f2 = f2 / f4;
				f3 = f3 / f4;
				f2 = f2 * 0.9F + 0.1F;
				f3 = (f3 * 4.0F - 1.0F) / 8.0F;
				double d7 = depthNoise[j] / 8000.0D;
				
				if (d7 < 0.0D)
				{
					d7 = -d7 * 0.3D;
				}
				
				d7 = d7 * 3.0D - 2.0D;
				
				if (d7 < 0.0D)
				{
					d7 = d7 / 2.0D;
					
					if (d7 < -1.0D)
					{
						d7 = -1.0D;
					}
					
					d7 = d7 / 1.4D;
					d7 = d7 / 2.0D;
				}
				else
				{
					if (d7 > 1.0D)
					{
						d7 = 1.0D;
					}
					
					d7 = d7 / 8.0D;
				}
				
				++j;
				double d8 = f3;
				double d9 = f2;
				d8 = d8 + d7 * 0.2D;
				d8 = d8 * settings.baseSize / 8;
				double d0 = settings.baseSize + d8 * 4;
				
				for (int l1 = 0; l1 < 33; ++l1)
				{
					double d1 = (l1 - d0) * settings.stretchY * 128 / 256 / d9;
					
					if (d1 < 0)
					{
						d1 *= 4;
					}
					
					double d2 = lPerlin2Noise[i] / settings.lowerLimitScale;
					double d3 = lPerlinNoise[i] / settings.upperLimitScale;
					double d4 = (perlinNoise[i] / 10 + 1) / 2;
					double d5 = MathHelper.denormalizeClamp(d2, d3, d4) - d1;
					
					if (l1 > 29)
					{
						double d6 = (l1 - 29) / 3;
						d5 = d5 * (1 - d6) + -10 * d6;
					}
					
					heightmap[i] = d5;
					++i;
				}
			}
		}
	}
	
	public void replaceBiomeBlocks(int blockX, int blockZ, ChunkPrimer chunkPrimer, BiomeGenBase[] biomes)
	{
		if (!ForgeEventFactory.onReplaceBiomeBlocks(this, blockX, blockZ, chunkPrimer, world))
			return;
		
		double d0 = 0.03125D;
		stoneNoise = heightNoiseGen.func_151599_a(stoneNoise, blockX * 16, blockZ * 16, 16, 16, d0 * 2, d0 * 2, 1);
		
		for (int k = 0; k < 16; ++k)
		{
			for (int l = 0; l < 16; ++l)
			{
				BiomeGenBase biomegenbase = biomes[l + k * 16];
				biomegenbase.genTerrainBlocks(world, rand, chunkPrimer, blockX * 16 + k, blockZ * 16 + l, stoneNoise[l + k * 16]);
			}
		}
	}
	
	@Override
	public Chunk provideChunk(int x, int z)
	{
		rand.setSeed(x * 341873128712L + z * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		setBlocksInChunk(x, z, chunkprimer);
		biomes = world.getBiomeProvider().loadBlockGeneratorData(biomes, x * 16, z * 16, 16, 16);
		replaceBiomeBlocks(x, z, chunkprimer, biomes);
		
		Chunk chunk = new Chunk(world, chunkprimer, x, z);
		byte[] abyte = chunk.getBiomeArray();
		
		for (int k = 0; k < abyte.length; ++k)
		{
			abyte[k] = (byte) BiomeGenBase.getIdForBiome(biomes[k]);
		}
		
		chunk.generateSkylightMap();
		return chunk;
	}
	
	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
	{
		return null;
	}
	
	@Override
	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return world.getBiomeGenForCoords(pos).getSpawnableList(creatureType);
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {
		// TODO Auto-generated method stub
		
	}
}
