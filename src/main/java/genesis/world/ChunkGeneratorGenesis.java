package genesis.world;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumSilt;
import genesis.common.*;
import genesis.util.noise.NoiseValueSchemes.D3C1;
import genesis.util.noise.SuperSimplexNoise;
import genesis.world.biome.BiomeGenesis;
import genesis.world.gen.feature.*;

import net.minecraft.block.*;

import genesis.common.GenesisBiomes;
import genesis.common.GenesisBlocks;
import genesis.util.noise.NoiseValueSchemes.D2C1;
import genesis.util.noise.NoiseValueSchemes.D3C1;
import genesis.util.noise.SuperSimplexNoise;
import genesis.world.biome.BiomeGenesis;
import genesis.world.gen.feature.WorldGenGenesisLakes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ChunkGeneratorGenesis implements IChunkGenerator
{
	//Independent constants
	private static final int BIOME_BLEND_RADIUS = 4;
	private static final float RIVER_RELATIVE_WEIGHT = 12.0F;
	private static final double CAVE_WATER_ATTENUATION_BUFFER_SQ = 24.0;

	//Constants to adjust custom gen to match the scales of the vanilla gen
	private static double HEIGHT_VARIATION_SCALE = 2.75;
	private static double BASE_HEIGHT_SCALE = 0.54;//375;
	private static double BASE_HEIGHT_OFFSET = 0.0075;
	private static final double VARIATION_PADDING = 27.0 / 140.0;
	private static double HEIGHT_NOISE_WAVELENGTH_SCALE = 6.0;
	private static double BLEND_NOISE_WAVELENGTH_SCALE = 8.0;

	/*//TODO remove this and put back static above
	static {
		//Set this to the path to your file. It should have all the numbers in order like 2.75 0.4 0.0075 6.0 8.0 (separate lines for each # is OK too)
		java.io.File f = new java.io.File("C:\\Users\\user\\Desktop\\Programming\\MCMods\\Genesis\\run\\saves\\conf_with_var.txt");
		java.util.Scanner s = null;
		try {
			s = new java.util.Scanner(f);
		} catch (java.io.FileNotFoundException e) {
			System.out.println("!!! Could not load test worldgen scaling config text file! Using default configs. !!!");
		}
		HEIGHT_VARIATION_SCALE = s.nextDouble();
		BASE_HEIGHT_SCALE = s.nextDouble();
		BASE_HEIGHT_OFFSET = s.nextDouble();
		HEIGHT_NOISE_WAVELENGTH_SCALE = s.nextDouble();
		BLEND_NOISE_WAVELENGTH_SCALE = s.nextDouble();
		s.close();
	}*/

	//Too high will affect performance and the interpolator derivatives.
	//Too low will not produce enough detail.
	private static final int HEIGHT_NOISE_OCTAVES = 3;
	private static final int BLEND_NOISE_OCTAVES = 4;
	
	//Dependent constants
	private static final int BIOME_BLEND_DIAMETER = 2 * BIOME_BLEND_RADIUS + 1;
	private static final int BIOME_BLEND_FULL_RANGE = 2 * BIOME_BLEND_RADIUS + 6;
	
	//Interpolation region corner offsets
	private static final int C000 = D3C1.STEP * (0 + 33 * (0 + 3 * 0));
	private static final int C001 = D3C1.STEP * (0 + 33 * (0 + 3 * 1));
	private static final int C010 = D3C1.STEP * (0 + 33 * (1 + 3 * 0));
	private static final int C011 = D3C1.STEP * (0 + 33 * (1 + 3 * 1));
	private static final int C100 = D3C1.STEP * (1 + 33 * (0 + 3 * 0));
	private static final int C101 = D3C1.STEP * (1 + 33 * (0 + 3 * 1));
	private static final int C110 = D3C1.STEP * (1 + 33 * (1 + 3 * 0));
	private static final int C111 = D3C1.STEP * (1 + 33 * (1 + 3 * 1));
	
	//Interpolation region value type offsets
	private static final int F = D3C1.F;
	private static final int Fx = D3C1.Fx;
	private static final int Fy = D3C1.Fy;
	private static final int Fz = D3C1.Fz;
	private static final int Fxy = D3C1.Fxy;
	private static final int Fxz = D3C1.Fxz;
	private static final int Fyz = D3C1.Fyz;
	private static final int Fxyz = D3C1.Fxyz;

	//Biome smoothing value type offsets
	private static final int B_F = 0;
	private static final int B_Fx = 1;
	private static final int B_Fz = 2;
	private static final int B_Fxz = 3;
	private static final int B_attn = 4;
	private static final int B_attnLog = 5;
	private static final int B_FxPart = 6;
	private static final int B_FzPart = 7;
	private static final int B_FxzPart = 8;
	private static final int B_STEP = 9;
	
	//Interpolation coefficient pre-computations.
	//Basically a quintic spline, with the second-derivatives at the endpoints zero
	//Used successively per dimension (8x8x8)
	//Produces nicer results (IMO ~KdotJPG) than Vanilla 4x8x4 trilinear
	//with similar performance requirements
	//Requires smooth terrain function
	//~KdotJPG
	private static final double[] I8 = new double[8 * 8];
	private static final int I8_P0 = 0 * 8;
	private static final int I8_M0 = 1 * 8;
	private static final int I8_P1 = 2 * 8;
	private static final int I8_M1 = 3 * 8;
	private static final int I8_D = 4 * 8;
	static {
		for (int t_8 = 0; t_8 < 8; t_8++) {
			double t = t_8 / 8.0;
			I8[t_8 + I8_P0] = 1 + t * t * t * (-10 + t * (15 + t * -6));
			I8[t_8 + I8_M0] = 8 * (t * (1 + t * t * (-6 + t * (8 + t * -3))));
			I8[t_8 + I8_P1] = t * t * t * (10 + t * (-15 + t * 6));
			I8[t_8 + I8_M1] = 8 * (t * t * t * (-4 + t * (7 + t * -3)));
			I8[t_8 + I8_P0 + I8_D] = t * t * (-30 + t * (60 + t * -30)) / 8.0;
			I8[t_8 + I8_M0 + I8_D] = 1 + t * t * (-18 + t * (32 + t * -15));
			I8[t_8 + I8_P1 + I8_D] = t * t * (30 + t * (-60 + t * 30)) / 8.0;
			I8[t_8 + I8_M1 + I8_D] = t * t * (-12 + t * (28 + t * -15));
		}
	}

	//Biome smoothing kernel & derivatives.
	//Derivatives may seem backwards, but they are of the equation (r^2 - (i-0.25x)^2 - (j-0.25y)^2)^2
	//0.25 is because the biome points for the heightmap generator are spaced by 4 blocks in a grid
	//Original vanilla used a discontinuous function; this is continuous and smooth
	//~KdotJPG
	private static float[] BIOME_DISTANCE_WEIGHTS = new float[BIOME_BLEND_DIAMETER * BIOME_BLEND_DIAMETER * B_STEP];
	static {
		for (int i = -BIOME_BLEND_RADIUS; i <= BIOME_BLEND_RADIUS; ++i) {
			for (int j = -BIOME_BLEND_RADIUS; j <= BIOME_BLEND_RADIUS; ++j) {
				int index = (i + BIOME_BLEND_RADIUS + (j + BIOME_BLEND_RADIUS) * BIOME_BLEND_DIAMETER) * B_STEP;
				
				float attn = (BIOME_BLEND_RADIUS * BIOME_BLEND_RADIUS - i * i - j * j);
				if (attn > 0) {
					float attnNormalized = attn / (BIOME_BLEND_RADIUS * BIOME_BLEND_RADIUS);

					//For smoothing the smoothing exponent itself, initially
					BIOME_DISTANCE_WEIGHTS[index + B_F] = attn * attn;
					BIOME_DISTANCE_WEIGHTS[index + B_Fx] = i * attn;
					BIOME_DISTANCE_WEIGHTS[index + B_Fz] = j * attn;
					BIOME_DISTANCE_WEIGHTS[index + B_Fxz] = 0.5f * i * j;

					//For smoothing using the exponent
					BIOME_DISTANCE_WEIGHTS[index + B_attn] = attnNormalized;
					BIOME_DISTANCE_WEIGHTS[index + B_attnLog] = (float) Math.log(attnNormalized);
					BIOME_DISTANCE_WEIGHTS[index + B_FxPart] = 0.5f * i / attn;
					BIOME_DISTANCE_WEIGHTS[index + B_FzPart] = 0.5f * j / attn;
					BIOME_DISTANCE_WEIGHTS[index + B_FxzPart] = 0.25f * i * j / (attn * attn);
				}
			}
		}
	}
	
	//For caves
	private static final double CAVE_LAVA_BOULDER_NOISE_WAVELENGTH = 16.0;
	private static final HashSet<Block> CAVE_DIGGABLE_BLOCKS = new HashSet<>();
	private static final HashMap<IBlockState, IBlockState> CAVE_ABOVE_BLOCK_REPLACEMENTS = new HashMap<>();
	private static final IBlockState[] CAVE_LEVEL_REPLACEMENT_BLOCKS = new IBlockState[256];
	static {
		CAVE_DIGGABLE_BLOCKS.add(GenesisBlocks.GRANITE);
		CAVE_DIGGABLE_BLOCKS.add(Blocks.DIRT);
		CAVE_DIGGABLE_BLOCKS.add(GenesisBlocks.HUMUS);
		CAVE_DIGGABLE_BLOCKS.add(GenesisBlocks.MOSS);
		CAVE_DIGGABLE_BLOCKS.addAll(GenesisBlocks.SILT.getBlocks(SiltBlocks.SILT));
		CAVE_DIGGABLE_BLOCKS.addAll(GenesisBlocks.SILT.getBlocks(SiltBlocks.CRACKED_SILT));
		CAVE_DIGGABLE_BLOCKS.addAll(GenesisBlocks.SILT.getBlocks(SiltBlocks.SILTSTONE));
		CAVE_DIGGABLE_BLOCKS.add(GenesisBlocks.OOZE);
		CAVE_DIGGABLE_BLOCKS.add(GenesisBlocks.LIMESTONE);

		CAVE_ABOVE_BLOCK_REPLACEMENTS.put(
				GenesisBlocks.SILT.getBlockState(SiltBlocks.SILT, EnumSilt.SILT),
				GenesisBlocks.SILT.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.SILT));
		CAVE_ABOVE_BLOCK_REPLACEMENTS.put(
				GenesisBlocks.SILT.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT),
				GenesisBlocks.SILT.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT));
		CAVE_ABOVE_BLOCK_REPLACEMENTS.put(
				GenesisBlocks.SILT.getBlockState(SiltBlocks.CRACKED_SILT, EnumSilt.SILT),
				GenesisBlocks.SILT.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.SILT));
		CAVE_ABOVE_BLOCK_REPLACEMENTS.put(
				GenesisBlocks.SILT.getBlockState(SiltBlocks.CRACKED_SILT, EnumSilt.RED_SILT),
				GenesisBlocks.SILT.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT));

		{
			int y = 0;
			for (; y < 7; y++) CAVE_LEVEL_REPLACEMENT_BLOCKS[y] = GenesisBlocks.KOMATIITIC_LAVA.getDefaultState();
			for (; y < 256; y++) CAVE_LEVEL_REPLACEMENT_BLOCKS[y] = Blocks.AIR.getDefaultState();
		}
	}
	
	protected World world;
	protected Random rand;
	protected boolean mapFeatures;

	protected ChunkProviderSettings settings;

	protected IBlockState liquid = Blocks.WATER.getDefaultState();

	protected Biome[] biomes;

	protected SuperSimplexNoise.Octaves noiseMainGen;
	protected SuperSimplexNoise.Octaves_Multi noiseLGen;
	protected SuperSimplexNoise stoneNoiseGen;

	protected double[] heightmap;
	protected double[] stoneNoise;

	protected double[] noiseMainValues;
	protected double[] noiseL1Values;
	protected double[] noiseL2Values;
	protected double[][] noiseLValuesArray;
	
	//For caves
	protected double[] caveLavaBoulderNoiseValues;
	double[] caveNoiseValues;
	boolean[] caveOpening;
	SuperSimplexNoise caveLavaBoulderNoiseGen;
	SuperSimplexNoise.NoiseInstance3[] caveNoiseInstances_64_96_64;

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
			settings = factory.build();	// build
			liquid = (settings.useLavaOceans ? Blocks.LAVA : Blocks.WATER).getDefaultState();
		}
		
		//Correspond to original vanilla noise gens
		noiseMainGen = new SuperSimplexNoise.Octaves(rand, HEIGHT_NOISE_OCTAVES);
		noiseLGen = new SuperSimplexNoise.Octaves_Multi(rand, BLEND_NOISE_OCTAVES, 2);
		stoneNoiseGen = new SuperSimplexNoise(rand);

		//Correspond to original vanilla value fields
		heightmap = new double[3 * 33 * 3 * D3C1.STEP];
		stoneNoise = new double[16 * 16];
		noiseMainValues = new double[3 * 33 * 3 * D3C1.STEP];
		noiseL1Values = new double[3 * 33 * 3 * D3C1.STEP];
		noiseL2Values = new double[3 * 33 * 3 * D3C1.STEP];
		noiseLValuesArray = new double[][] { noiseL1Values, noiseL2Values };
		
		//Cave
		caveLavaBoulderNoiseValues = new double[16 * 16];
		caveNoiseValues = new double[8];
		caveOpening = new boolean[16 * 16 * 256];
		caveLavaBoulderNoiseGen = new SuperSimplexNoise(rand);
		
		//Old multi-eval scheme, but new one isn't perfect either.
		//Functions just fine, but is an opportunity for organization later
		caveNoiseInstances_64_96_64 = new SuperSimplexNoise.NoiseInstance3[] {
				new SuperSimplexNoise.NoiseInstance3(new SuperSimplexNoise(rand), 0, 1, 2, 3),
				new SuperSimplexNoise.NoiseInstance3(new SuperSimplexNoise(rand), 4, 5, 6, 7)
		};
	}

	@Override
	public void populate(int chunkX, int chunkZ)
	{
		BlockFalling.fallInstantly = true;
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;
		BlockPos pos = new BlockPos(blockX, 0, blockZ);
		Biome biome = world.getBiome(pos.add(16, 0, 16));
		rand.setSeed(world.getSeed());
		long xSeed = rand.nextLong() / 2L * 2L + 1L;
		long ySeed = rand.nextLong() / 2L * 2L + 1L;
		rand.setSeed(chunkX * xSeed + chunkZ * ySeed ^ world.getSeed());

		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(this, world, rand, chunkX, chunkZ, false));

		int waterLakeChance = settings.waterLakeChance;

		if (biome == GenesisBiomes.redDesert || biome == GenesisBiomes.redDesertHills)
			waterLakeChance = 15;

		if (settings.useWaterLakes
						&& rand.nextInt(waterLakeChance) == 0
						&& TerrainGen.populate(this, world, rand, chunkX, chunkZ, false, LAKE))
		{
			int x = rand.nextInt(16) + 8;
			int y = rand.nextInt(256);
			int z = rand.nextInt(16) + 8;
			new WorldGenGenesisLakes(Blocks.WATER.getDefaultState()).generate(world, rand, pos.add(x, y, z));
		}

		if (TerrainGen.populate(this, world, rand, chunkX, chunkZ, false, LAVA) && rand.nextInt(settings.lavaLakeChance / 10) == 0 && settings.useLavaLakes)
		{
			int x = rand.nextInt(16) + 8;
			int y = rand.nextInt(rand.nextInt(248) + 8);
			int z = rand.nextInt(16) + 8;

			if (y < 63 || rand.nextInt(settings.lavaLakeChance / 8) == 0)
			{
				new WorldGenGenesisLakes(GenesisBlocks.KOMATIITIC_LAVA.getDefaultState()).generate(world, rand, pos.add(x, y, z));
			}
		}

		biome.decorate(world, rand, pos);

		pos = pos.add(8, 0, 8);

		boolean doGen = TerrainGen.populate(this, world, rand, chunkX, chunkZ, false, ICE);

		for (int x = 0; doGen && x < 16; ++x)
		{
			for (int y = 0; y < 16; ++y)
			{
				BlockPos surface = world.getPrecipitationHeight(pos.add(x, 0, y));
				BlockPos water = surface.down();

				if (world.canBlockFreezeNoWater(water))
				{
					world.setBlockState(water, Blocks.ICE.getDefaultState(), 2);
				}

				if (world.canSnowAt(surface, true))
				{
					world.setBlockState(surface, Blocks.SNOW_LAYER.getDefaultState(), 2);
				}
			}
		}

		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(this, world, rand, chunkX, chunkZ, false));

		BlockFalling.fallInstantly = false;
	}

	public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
		biomes = world.getBiomeProvider().getBiomesForGeneration(biomes, x * 4 - BIOME_BLEND_RADIUS, z * 4 - BIOME_BLEND_RADIUS, BIOME_BLEND_FULL_RANGE, BIOME_BLEND_FULL_RANGE);
		generateHeightmap(x * 16, 0, z * 16);
		
		//For caves
		{
			int i = 0;
			for (int iz = 0; iz < 16; iz++) {
				for (int ix = 0; ix < 16; ix++) {
					caveLavaBoulderNoiseValues[i] = caveLavaBoulderNoiseGen.eval((ix + x * 16) / CAVE_LAVA_BOULDER_NOISE_WAVELENGTH, (iz + z * 16) / CAVE_LAVA_BOULDER_NOISE_WAVELENGTH);
					i++;
				}
			}
		}
		
		//for shorthand
		double[] h = heightmap;
		
		//These are conditionally defined if they are needed.
		double hC00X_Fx = 0, hC01X_Fx = 0, hC10X_Fx = 0, hC11X_Fx = 0, hC00X_Fxy = 0, hC01X_Fxy = 0, hC10X_Fxy = 0, hC11X_Fxy = 0, hC00X_Fxz = 0, hC01X_Fxz = 0, hC10X_Fxz = 0, hC11X_Fxz = 0, hC00X_Fxyz = 0, hC01X_Fxyz = 0, hC10X_Fxyz = 0, hC11X_Fxyz = 0;
		double hC0ZX_Fx = 0, hC1ZX_Fx = 0, hC0ZX_Fz = 0, hC1ZX_Fz = 0, hC0ZX_Fxy = 0, hC1ZX_Fxy = 0, hC0ZX_Fyz = 0, hC1ZX_Fyz = 0;

		//For each 4x8x4 region
		for (int ix = 0; ix < 2; ++ix) {
			int ix_5 = ix * 3;
			for (int iz = 0; iz < 2; ++iz) {
				int ixz_33_8 = (ix_5 + iz) * 33 * D3C1.STEP;
				for (int iy_8 = 0; iy_8 < 32 * D3C1.STEP; iy_8 += D3C1.STEP) {
					//Index to heightmap base corner before offsets
					int ih = ixz_33_8 + iy_8;
					
					/*
					 * TODO whenever: Opportunity for efficiency.
					 * 
					 * A fast upper-bound can be determined from just the corners' values and derivatives.
					 * If this interpolation region will never reach a positive value, we can skip it altogether.
					 * 
					 * We don't need to loop through the caveOpenings array over this region,
					 * because it will never be checked there when the blocks there are air.
					 */
					
					//Now for the 8x8x8 region. Interpolate tri-quintically, using derivatives, and zero second derivatives.
					//An idea I've had for a long time that am finally getting to put to use ~KdotJPG
					for (int jx = 0; jx < 8; ++jx) {
						double hC00X_F = h[ih+C000+F] * I8[jx+I8_P0] + h[ih+C000+Fx] * I8[jx+I8_M0] + h[ih+C001+F] * I8[jx+I8_P1] + h[ih+C001+Fx] * I8[jx+I8_M1];
						double hC01X_F = h[ih+C010+F] * I8[jx+I8_P0] + h[ih+C010+Fx] * I8[jx+I8_M0] + h[ih+C011+F] * I8[jx+I8_P1] + h[ih+C011+Fx] * I8[jx+I8_M1];
						double hC10X_F = h[ih+C100+F] * I8[jx+I8_P0] + h[ih+C100+Fx] * I8[jx+I8_M0] + h[ih+C101+F] * I8[jx+I8_P1] + h[ih+C101+Fx] * I8[jx+I8_M1];
						double hC11X_F = h[ih+C110+F] * I8[jx+I8_P0] + h[ih+C110+Fx] * I8[jx+I8_M0] + h[ih+C111+F] * I8[jx+I8_P1] + h[ih+C111+Fx] * I8[jx+I8_M1];
						double hC00X_Fy = h[ih+C000+Fy] * I8[jx+I8_P0] + h[ih+C000+Fxy] * I8[jx+I8_M0] + h[ih+C001+Fy] * I8[jx+I8_P1] + h[ih+C001+Fxy] * I8[jx+I8_M1];
						double hC01X_Fy = h[ih+C010+Fy] * I8[jx+I8_P0] + h[ih+C010+Fxy] * I8[jx+I8_M0] + h[ih+C011+Fy] * I8[jx+I8_P1] + h[ih+C011+Fxy] * I8[jx+I8_M1];
						double hC10X_Fy = h[ih+C100+Fy] * I8[jx+I8_P0] + h[ih+C100+Fxy] * I8[jx+I8_M0] + h[ih+C101+Fy] * I8[jx+I8_P1] + h[ih+C101+Fxy] * I8[jx+I8_M1];
						double hC11X_Fy = h[ih+C110+Fy] * I8[jx+I8_P0] + h[ih+C110+Fxy] * I8[jx+I8_M0] + h[ih+C111+Fy] * I8[jx+I8_P1] + h[ih+C111+Fxy] * I8[jx+I8_M1];
						double hC00X_Fz = h[ih+C000+Fz] * I8[jx+I8_P0] + h[ih+C000+Fxz] * I8[jx+I8_M0] + h[ih+C001+Fz] * I8[jx+I8_P1] + h[ih+C001+Fxz] * I8[jx+I8_M1];
						double hC01X_Fz = h[ih+C010+Fz] * I8[jx+I8_P0] + h[ih+C010+Fxz] * I8[jx+I8_M0] + h[ih+C011+Fz] * I8[jx+I8_P1] + h[ih+C011+Fxz] * I8[jx+I8_M1];
						double hC10X_Fz = h[ih+C100+Fz] * I8[jx+I8_P0] + h[ih+C100+Fxz] * I8[jx+I8_M0] + h[ih+C101+Fz] * I8[jx+I8_P1] + h[ih+C101+Fxz] * I8[jx+I8_M1];
						double hC11X_Fz = h[ih+C110+Fz] * I8[jx+I8_P0] + h[ih+C110+Fxz] * I8[jx+I8_M0] + h[ih+C111+Fz] * I8[jx+I8_P1] + h[ih+C111+Fxz] * I8[jx+I8_M1];
						double hC00X_Fyz = h[ih+C000+Fyz] * I8[jx+I8_P0] + h[ih+C000+Fxyz] * I8[jx+I8_M0] + h[ih+C001+Fyz] * I8[jx+I8_P1] + h[ih+C001+Fxyz] * I8[jx+I8_M1];
						double hC01X_Fyz = h[ih+C010+Fyz] * I8[jx+I8_P0] + h[ih+C010+Fxyz] * I8[jx+I8_M0] + h[ih+C011+Fyz] * I8[jx+I8_P1] + h[ih+C011+Fxyz] * I8[jx+I8_M1];
						double hC10X_Fyz = h[ih+C100+Fyz] * I8[jx+I8_P0] + h[ih+C100+Fxyz] * I8[jx+I8_M0] + h[ih+C101+Fyz] * I8[jx+I8_P1] + h[ih+C101+Fxyz] * I8[jx+I8_M1];
						double hC11X_Fyz = h[ih+C110+Fyz] * I8[jx+I8_P0] + h[ih+C110+Fxyz] * I8[jx+I8_M0] + h[ih+C111+Fyz] * I8[jx+I8_P1] + h[ih+C111+Fxyz] * I8[jx+I8_M1];
						boolean derivativeCalculationsX = false;

						int blockX = x * 16 + ix * 8 + jx;

						for (int jz = 0; jz < 8; ++jz) {
							double hC0ZX_F = hC00X_F * I8[jz+I8_P0] + hC00X_Fz * I8[jz+I8_M0] + hC01X_F * I8[jz+I8_P1] + hC01X_Fz * I8[jz+I8_M1];
							double hC1ZX_F = hC10X_F * I8[jz+I8_P0] + hC10X_Fz * I8[jz+I8_M0] + hC11X_F * I8[jz+I8_P1] + hC11X_Fz * I8[jz+I8_M1];
							double hC0ZX_Fy = hC00X_Fy * I8[jz+I8_P0] + hC00X_Fyz * I8[jz+I8_M0] + hC01X_Fy * I8[jz+I8_P1] + hC01X_Fyz * I8[jz+I8_M1];
							double hC1ZX_Fy = hC10X_Fy * I8[jz+I8_P0] + hC10X_Fyz * I8[jz+I8_M0] + hC11X_Fy * I8[jz+I8_P1] + hC11X_Fyz * I8[jz+I8_M1];
							boolean derivativeCalculationsZ = false;
							
							int blockZ = z * 16 + iz * 8 + jz;
							double lavaBoulderValue = caveLavaBoulderNoiseValues[(iz * 8 + jz) * 16 + (ix * 8 + jx)];
							
							for (int jy = 0; jy < 8; ++jy) {
								double hCYZX_F = hC0ZX_F * I8[jy+I8_P0] + hC0ZX_Fy * I8[jy+I8_M0] + hC1ZX_F * I8[jy+I8_P1] + hC1ZX_Fy * I8[jy+I8_M1];
								
								if (hCYZX_F > 0.0D)	{
									
									//Go ahead and make it solid
									primer.setBlockState(ix * 8 + jx, iy_8 + jy, iz * 8 + jz, GenesisBlocks.GRANITE.getDefaultState());
									
									//Now we decide if a cave opening is in this block...
									
									int blockY = iy_8 + jy;
									int chunkBlockIndex = (ix * 8 + jx) * 256 * 16 + (iz * 8 + jz) * 256 + (iy_8 + jy);
									
									//At first no
									caveOpening[chunkBlockIndex] = false;
									
									//Cave noise evaluations
									for (int i = 0; i < 8; i++) caveNoiseValues[i] = 0;
									SuperSimplexNoise.eval(blockX / 96.0, blockY / 64.0, blockZ / 96.0, caveNoiseInstances_64_96_64, caveNoiseValues);
									double F1 = caveNoiseValues[0], F1x = caveNoiseValues[1], F1y = caveNoiseValues[2], F1z = caveNoiseValues[3];
									double F2 = caveNoiseValues[4], F2x = caveNoiseValues[5], F2y = caveNoiseValues[6], F2z = caveNoiseValues[7];
									
									//Get noise derivative magnitudes
									double F1dNormSq = F1x*F1x + F1y*F1y + F1z*F1z, F1dNorm = Math.sqrt(F1dNormSq);
									double F2dNormSq = F2x*F2x + F2y*F2y + F2z*F2z, F2dNorm = Math.sqrt(F2dNormSq);

									//We're in a local max/min of either noise
									//This will basically never happen, so we might consider just taking this out
									if (F1dNormSq == 0 || F2dNormSq == 0) {
										continue;
									}

									//Get unit dot product of derivatives
									double dot = F1x*F2x + F1y*F2y + F1z*F2z;
									double dotU = dot / F1dNorm / F2dNorm;

									//They're going the exact same direction
									//This will very rarely happen, so we might consider just taking this out
									if (dotU == 1 || dotU == -1) {
										continue;
									}

									//Lava rooms
									double threshold = 0.0075;
									double lavaRoomDisp = (blockY - 7 + F1 * 2) / 17.0;
									lavaRoomDisp *= lavaRoomDisp;
									if (lavaRoomDisp < 1) {
										lavaRoomDisp = 1 - lavaRoomDisp;
										lavaRoomDisp *= lavaRoomDisp;
										lavaRoomDisp *= lavaRoomDisp;

										//Vary the placement of the caves by re-using one of the cave noise values.
										lavaRoomDisp *= F2 / 2 + .5;

										//Threshold gets bigger the more we want lava rooms here
										threshold *= Math.pow(1 / threshold, lavaRoomDisp);

										//As we increase the threshold we need to decrease our cave-distortion-correction to avoid weird effects.
										dotU *= (1 - lavaRoomDisp);

										//Lava boulders
										double lavaBoulderMaxHeight = lavaRoomDisp * 16;
										double lavaBoulderHeight = lavaBoulderMaxHeight * lavaBoulderValue;
										if (lavaBoulderHeight >= blockY) {
											continue;
										}
									}

									//Approximate distance to nearest cave path (noise zero-surface intersection) using crazy math
									//(but factoring in lava-room alterations)
									double value = (F2 - F1*dotU) * (F2 - F1*dotU) / (1 - dotU * dotU) + F1 * F1;
									
									//Make caves slightly taller
									double heightAdjuster = (F1y * F1 + F2y * F2);
									value /= (1 + heightAdjuster * heightAdjuster * 6.0);
									
									//Curve caves open slightly at surface, to match the terrain
									double CAVE_OPEN_RANGE = 2.0 / 8.0;
									double caveOpenThresh = 1.0 - hCYZX_F / CAVE_OPEN_RANGE;
									if (caveOpenThresh > 0) {
										caveOpenThresh *= caveOpenThresh;
										value *= 1.0 - caveOpenThresh;
									}

									//Replace super-flat un-traversable regions with dead ends
									double dotURegionHideThresholded = Math.abs(dotU) - 0.625;
									if (dotURegionHideThresholded > 0) {
										value += dotURegionHideThresholded * dotURegionHideThresholded * 8.0;
									}

									//Threshold
									if (value > threshold) {
										continue;
									}
									
									/*
									 * TODO whenever: Opportunity for efficiency.
									 * 
									 * A fast derivative magnitude upper-bound can be determined from just the corners' values and derivatives.
									 * Compute it above, next to the first "opportunity for efficiency" and check it here.
									 * 
									 * Can especially help because the lava rooms at the bottom are all far from water but they're big so they're
									 * calling upon this derivative computation a lot.
									 */
									
									//We only interpolate the derivatives when needed for water proximity attenuation of caves
									if (!derivativeCalculationsX) {
										hC00X_Fx = h[ih+C000+F] * I8[jx+I8_P0+I8_D] + h[ih+C000+Fx] * I8[jx+I8_M0+I8_D] + h[ih+C001+F] * I8[jx+I8_P1+I8_D] + h[ih+C001+Fx] * I8[jx+I8_M1+I8_D];
										hC01X_Fx = h[ih+C010+F] * I8[jx+I8_P0+I8_D] + h[ih+C010+Fx] * I8[jx+I8_M0+I8_D] + h[ih+C011+F] * I8[jx+I8_P1+I8_D] + h[ih+C011+Fx] * I8[jx+I8_M1+I8_D];
										hC10X_Fx = h[ih+C100+F] * I8[jx+I8_P0+I8_D] + h[ih+C100+Fx] * I8[jx+I8_M0+I8_D] + h[ih+C101+F] * I8[jx+I8_P1+I8_D] + h[ih+C101+Fx] * I8[jx+I8_M1+I8_D];
										hC11X_Fx = h[ih+C110+F] * I8[jx+I8_P0+I8_D] + h[ih+C110+Fx] * I8[jx+I8_M0+I8_D] + h[ih+C111+F] * I8[jx+I8_P1+I8_D] + h[ih+C111+Fx] * I8[jx+I8_M1+I8_D];
										hC00X_Fxy = h[ih+C000+Fy] * I8[jx+I8_P0+I8_D] + h[ih+C000+Fxy] * I8[jx+I8_M0+I8_D] + h[ih+C001+Fy] * I8[jx+I8_P1+I8_D] + h[ih+C001+Fxy] * I8[jx+I8_M1+I8_D];
										hC01X_Fxy = h[ih+C010+Fy] * I8[jx+I8_P0+I8_D] + h[ih+C010+Fxy] * I8[jx+I8_M0+I8_D] + h[ih+C011+Fy] * I8[jx+I8_P1+I8_D] + h[ih+C011+Fxy] * I8[jx+I8_M1+I8_D];
										hC10X_Fxy = h[ih+C100+Fy] * I8[jx+I8_P0+I8_D] + h[ih+C100+Fxy] * I8[jx+I8_M0+I8_D] + h[ih+C101+Fy] * I8[jx+I8_P1+I8_D] + h[ih+C101+Fxy] * I8[jx+I8_M1+I8_D];
										hC11X_Fxy = h[ih+C110+Fy] * I8[jx+I8_P0+I8_D] + h[ih+C110+Fxy] * I8[jx+I8_M0+I8_D] + h[ih+C111+Fy] * I8[jx+I8_P1+I8_D] + h[ih+C111+Fxy] * I8[jx+I8_M1+I8_D];
										hC00X_Fxz = h[ih+C000+Fz] * I8[jx+I8_P0+I8_D] + h[ih+C000+Fxz] * I8[jx+I8_M0+I8_D] + h[ih+C001+Fz] * I8[jx+I8_P1+I8_D] + h[ih+C001+Fxz] * I8[jx+I8_M1+I8_D];
										hC01X_Fxz = h[ih+C010+Fz] * I8[jx+I8_P0+I8_D] + h[ih+C010+Fxz] * I8[jx+I8_M0+I8_D] + h[ih+C011+Fz] * I8[jx+I8_P1+I8_D] + h[ih+C011+Fxz] * I8[jx+I8_M1+I8_D];
										hC10X_Fxz = h[ih+C100+Fz] * I8[jx+I8_P0+I8_D] + h[ih+C100+Fxz] * I8[jx+I8_M0+I8_D] + h[ih+C101+Fz] * I8[jx+I8_P1+I8_D] + h[ih+C101+Fxz] * I8[jx+I8_M1+I8_D];
										hC11X_Fxz = h[ih+C110+Fz] * I8[jx+I8_P0+I8_D] + h[ih+C110+Fxz] * I8[jx+I8_M0+I8_D] + h[ih+C111+Fz] * I8[jx+I8_P1+I8_D] + h[ih+C111+Fxz] * I8[jx+I8_M1+I8_D];
										hC00X_Fxyz = h[ih+C000+Fyz] * I8[jx+I8_P0+I8_D] + h[ih+C000+Fxyz] * I8[jx+I8_M0+I8_D] + h[ih+C001+Fyz] * I8[jx+I8_P1+I8_D] + h[ih+C001+Fxyz] * I8[jx+I8_M1+I8_D];
										hC01X_Fxyz = h[ih+C010+Fyz] * I8[jx+I8_P0+I8_D] + h[ih+C010+Fxyz] * I8[jx+I8_M0+I8_D] + h[ih+C011+Fyz] * I8[jx+I8_P1+I8_D] + h[ih+C011+Fxyz] * I8[jx+I8_M1+I8_D];
										hC10X_Fxyz = h[ih+C100+Fyz] * I8[jx+I8_P0+I8_D] + h[ih+C100+Fxyz] * I8[jx+I8_M0+I8_D] + h[ih+C101+Fyz] * I8[jx+I8_P1+I8_D] + h[ih+C101+Fxyz] * I8[jx+I8_M1+I8_D];
										hC11X_Fxyz = h[ih+C110+Fyz] * I8[jx+I8_P0+I8_D] + h[ih+C110+Fxyz] * I8[jx+I8_M0+I8_D] + h[ih+C111+Fyz] * I8[jx+I8_P1+I8_D] + h[ih+C111+Fxyz] * I8[jx+I8_M1+I8_D];
										derivativeCalculationsX = true;
									}
									if (!derivativeCalculationsZ) {
										hC0ZX_Fx = hC00X_Fx * I8[jz+I8_P0] + hC00X_Fxz * I8[jz+I8_M0] + hC01X_Fx * I8[jz+I8_P1] + hC01X_Fxz * I8[jz+I8_M1];
										hC1ZX_Fx = hC10X_Fx * I8[jz+I8_P0] + hC10X_Fxz * I8[jz+I8_M0] + hC11X_Fx * I8[jz+I8_P1] + hC11X_Fxz * I8[jz+I8_M1];
										hC0ZX_Fz = hC00X_F * I8[jz+I8_P0+I8_D] + hC00X_Fz * I8[jz+I8_M0+I8_D] + hC01X_F * I8[jz+I8_P1+I8_D] + hC01X_Fz * I8[jz+I8_M1+I8_D];
										hC1ZX_Fz = hC10X_F * I8[jz+I8_P0+I8_D] + hC10X_Fz * I8[jz+I8_M0+I8_D] + hC11X_F * I8[jz+I8_P1+I8_D] + hC11X_Fz * I8[jz+I8_M1+I8_D];
										hC0ZX_Fxy = hC00X_Fxy * I8[jz+I8_P0] + hC00X_Fxyz * I8[jz+I8_M0] + hC01X_Fxy * I8[jz+I8_P1] + hC01X_Fxyz * I8[jz+I8_M1];
										hC1ZX_Fxy = hC10X_Fxy * I8[jz+I8_P0] + hC10X_Fxyz * I8[jz+I8_M0] + hC11X_Fxy * I8[jz+I8_P1] + hC11X_Fxyz * I8[jz+I8_M1];
										hC0ZX_Fyz = hC00X_Fy * I8[jz+I8_P0+I8_D] + hC00X_Fyz * I8[jz+I8_M0+I8_D] + hC01X_Fy * I8[jz+I8_P1+I8_D] + hC01X_Fyz * I8[jz+I8_M1+I8_D];
										hC1ZX_Fyz = hC10X_Fy * I8[jz+I8_P0+I8_D] + hC10X_Fyz * I8[jz+I8_M0+I8_D] + hC11X_Fy * I8[jz+I8_P1+I8_D] + hC11X_Fyz * I8[jz+I8_M1+I8_D];
										derivativeCalculationsZ = true;
									}
									double hCYZX_Fx = hC0ZX_Fx * I8[jy+I8_P0] + hC0ZX_Fxy * I8[jy+I8_M0] + hC1ZX_Fx * I8[jy+I8_P1] + hC1ZX_Fxy * I8[jy+I8_M1];
									double hCYZX_Fy = hC0ZX_F * I8[jy+I8_P0+I8_D] + hC0ZX_Fy * I8[jy+I8_M0+I8_D] + hC1ZX_F * I8[jy+I8_P1+I8_D] + hC1ZX_Fy * I8[jy+I8_M1+I8_D];
									double hCYZX_Fz = hC0ZX_Fz * I8[jy+I8_P0] + hC0ZX_Fyz * I8[jy+I8_M0] + hC1ZX_Fz * I8[jy+I8_P1] + hC1ZX_Fyz * I8[jy+I8_M1];
									
									//Figure out effective point on surface, estimated from noise value and derivative attenuation. X and Z are lumped together into one positive horizontal coordinate.
									double magFxFyFzSq = (hCYZX_Fx * hCYZX_Fx + hCYZX_Fy * hCYZX_Fy + hCYZX_Fz * hCYZX_Fz);
									double effectiveDistSqFromSurface = hCYZX_F * hCYZX_F / magFxFyFzSq;
									
									//If it's above water, take the tangent at the estimated surface and find where it intersects the water level.
									double effectiveDistSqFromWater = effectiveDistSqFromSurface;
									double altitude = blockY - settings.seaLevel;
									double effectiveSurfaceY = altitude - hCYZX_Fy * hCYZX_F / magFxFyFzSq;
									if (effectiveSurfaceY > 0) {
										double magFxFz = Math.sqrt(hCYZX_Fx * hCYZX_Fx + hCYZX_Fz * hCYZX_Fz);
										double effectiveSurfaceXZ = magFxFz * hCYZX_F / magFxFyFzSq;
										
										//Horizontal distance to approximated terrain water surface intersection
										double effectiveWaterXZ = effectiveSurfaceXZ - effectiveSurfaceY * hCYZX_Fy / magFxFz;
										
										//Vertical distance is the altitude. From that we get the full deal.
										effectiveDistSqFromWater = altitude * altitude + effectiveWaterXZ * effectiveWaterXZ;
									}
									
									//Close off caves as we get too close to water
									//Even better might be to steer caves around the water,
									//but that would require a lot more derivative evaluations, and thus would be costlier
									//~KdotJPG
									double waterAttenuateThreshold = 1.0 - effectiveDistSqFromWater / CAVE_WATER_ATTENUATION_BUFFER_SQ;
									if (waterAttenuateThreshold > 0) {
										value += waterAttenuateThreshold * waterAttenuateThreshold * 1.0 / 16.0;
										if (value > threshold) {
											continue;
										}
									}
									
									caveOpening[chunkBlockIndex] = true;
									
								} else if (iy_8 + jy < settings.seaLevel) {
									primer.setBlockState(ix * 8 + jx, iy_8 + jy, iz * 8 + jz, liquid);
								}
							}
						}
					}	
				}
			}
		}
	}

	private void generateHeightmap(int x, int y, int z)
	{
		float horizScale = settings.coordinateScale;
		float vertScale = settings.heightScale;
		for (int i = 0; i < noiseMainValues.length; i++) noiseMainValues[i] = 0;
		for (int i = 0; i < noiseL1Values.length; i++) noiseL1Values[i] = 0;
		for (int i = 0; i < noiseL2Values.length; i++) noiseL2Values[i] = 0;

		noiseMainGen.populate_D3C1(noiseMainValues, x, y, z,
				8, 8, 8,
				3, 33, 3,
				horizScale / settings.mainNoiseScaleX / 128.0d / BLEND_NOISE_WAVELENGTH_SCALE,
				vertScale / settings.mainNoiseScaleY / 128.0d / BLEND_NOISE_WAVELENGTH_SCALE,
				horizScale / settings.mainNoiseScaleZ / 128.0d / BLEND_NOISE_WAVELENGTH_SCALE,
				1.0 / noiseMainGen.max);
		noiseLGen.populate_D3C1(noiseLValuesArray, x, y, z,
				8, 8, 8,
				3, 33, 3,
				horizScale / 32768.0d / HEIGHT_NOISE_WAVELENGTH_SCALE,
				vertScale / 32768.0d / HEIGHT_NOISE_WAVELENGTH_SCALE,
				horizScale / 32768.0d / HEIGHT_NOISE_WAVELENGTH_SCALE,
				1.0 / noiseLGen.max);
		
		//Turn scale settings into amplitudes 
		double a = HEIGHT_VARIATION_SCALE * 65536.0 / settings.lowerLimitScale;
		double b = HEIGHT_VARIATION_SCALE * 65536.0 / settings.upperLimitScale;
		
		int i = 0;
		//int j = 0;
		
		for (int k = 0; k < 3; ++k)
		{
			for (int l = 0; l < 3; ++l)
			{

				/*
				 * TODO whenever: Opportunity for efficiency.
				 *
				 * Usually, the exponent = 2, for all biomes relevant. When this is the case, we can use faster,
				 * more specialized code. We can also omit the smoothing altogether when entirely inside one biome.
				 */

				//Smoothing exponent determines the steepness of biome transitions
				float exponentPre = 0.0F;
				float exponentPreFx = 0.0F;
				float exponentPreFz = 0.0F;
				float exponentPreFxz = 0.0F;
				float totalExponentWeight = 0.0F;
				float totalExponentWeightFx = 0.0F;
				float totalExponentWeightFz = 0.0F;
				float totalExponentWeightFxz = 0.0F;

				//First get the smoothing exponent and all relevant derivatives
				for (int j1 = -BIOME_BLEND_RADIUS; j1 <= BIOME_BLEND_RADIUS; ++j1)
				{
					for (int k1 = -BIOME_BLEND_RADIUS; k1 <= BIOME_BLEND_RADIUS; ++k1)
					{
						BiomeGenesis thisBiome = (BiomeGenesis) biomes[2 * k + j1 + BIOME_BLEND_RADIUS + (2 * l + k1 + BIOME_BLEND_RADIUS) * BIOME_BLEND_FULL_RANGE];
						float thisExponent = thisBiome.getSmoothingExponent();

						int weightIndex = (j1 + BIOME_BLEND_RADIUS + (k1 + BIOME_BLEND_RADIUS) * BIOME_BLEND_DIAMETER) * B_STEP;

						float thisWeight = BIOME_DISTANCE_WEIGHTS[weightIndex + B_F];
						float thisWeightFx = BIOME_DISTANCE_WEIGHTS[weightIndex + B_Fx];
						float thisWeightFz = BIOME_DISTANCE_WEIGHTS[weightIndex + B_Fz];
						float thisWeightFxz = BIOME_DISTANCE_WEIGHTS[weightIndex + B_Fxz];

						exponentPre += thisExponent * thisWeight;
						exponentPreFx += thisExponent * thisWeightFx;
						exponentPreFz += thisExponent * thisWeightFz;
						exponentPreFxz += thisExponent * thisWeightFxz;
						totalExponentWeight += thisWeight;
						totalExponentWeightFx += thisWeightFx;
						totalExponentWeightFz += thisWeightFz;
						totalExponentWeightFxz += thisWeightFxz;
					}
				}

				//Normalize smoothing exponent weighted sum
				float exponent = exponentPre / totalExponentWeight;
				float exponentFx = (totalExponentWeight * exponentPreFx - exponentPre * totalExponentWeightFx) / (totalExponentWeight * totalExponentWeight);
				float exponentFz = (totalExponentWeight * exponentPreFz - exponentPre * totalExponentWeightFz) / (totalExponentWeight * totalExponentWeight);
				float exponentFxz = (exponentPreFxz * totalExponentWeight * totalExponentWeight + 2 * exponentPre * totalExponentWeightFx * totalExponentWeightFz - totalExponentWeight * (exponentPreFx * totalExponentWeightFz + exponentPreFz * totalExponentWeightFx + exponentPre * totalExponentWeightFxz)) / (totalExponentWeight * totalExponentWeight * totalExponentWeight);

				//Actual biome characterizations to smooth
				float heightVariationPre = 0.0F;
				float heightVariationPreFx = 0.0F;
				float heightVariationPreFz = 0.0F;
				float heightVariationPreFxz = 0.0F;
				float baseHeightPre = 0.0F;
				float baseHeightPreFx = 0.0F;
				float baseHeightPreFz = 0.0F;
				float baseHeightPreFxz = 0.0F;
				float totalWeight = 0.0F;
				float totalWeightFx = 0.0F;
				float totalWeightFz = 0.0F;
				float totalWeightFxz = 0.0F;

				//Smooth biome characterizations using exponent, keeping track of all necessary derivatives
				for (int j1 = -BIOME_BLEND_RADIUS; j1 <= BIOME_BLEND_RADIUS; ++j1)
				{
					for (int k1 = -BIOME_BLEND_RADIUS; k1 <= BIOME_BLEND_RADIUS; ++k1)
					{
						BiomeGenesis thisBiome = (BiomeGenesis) biomes[2 * k + j1 + BIOME_BLEND_RADIUS + (2 * l + k1 + BIOME_BLEND_RADIUS) * BIOME_BLEND_FULL_RANGE];
						float thisBaseHeight = settings.biomeDepthOffSet + thisBiome.getBaseHeight() * settings.biomeDepthWeight;
						float thisHeightVariation = settings.biomeScaleOffset + thisBiome.getHeightVariation() * settings.biomeScaleWeight;

						if (world.getWorldInfo().getTerrainType() == WorldType.AMPLIFIED && thisBaseHeight > 0.0F)
						{
							thisBaseHeight = 1.0F + thisBaseHeight * 2.0F;
							thisHeightVariation = 1.0F + thisHeightVariation * 4.0F;
						}

						int weightIndex = (j1 + BIOME_BLEND_RADIUS + (k1 + BIOME_BLEND_RADIUS) * BIOME_BLEND_DIAMETER) * B_STEP;

						float attn = BIOME_DISTANCE_WEIGHTS[weightIndex + B_attn];
						float attnLog = BIOME_DISTANCE_WEIGHTS[weightIndex + B_attnLog];
						float fxPart = BIOME_DISTANCE_WEIGHTS[weightIndex + B_FxPart];
						float fzPart = BIOME_DISTANCE_WEIGHTS[weightIndex + B_FzPart];
						float fxzPart = BIOME_DISTANCE_WEIGHTS[weightIndex + B_FxzPart];
						float fxMultiplier = (exponentFx * attnLog + exponent * fxPart);
						float fzMultiplier = (exponentFz * attnLog + exponent * fzPart);
						float thisWeight = (float) Math.pow(attn, exponent);
						float thisWeightFx = thisWeight * fxMultiplier;
						float thisWeightFz = thisWeight * fzMultiplier;
						float thisWeightFxz = thisWeight * (
							fxMultiplier * fzMultiplier
							+ exponentFx * fzPart + exponentFz * fxPart
							+ exponentFxz * attnLog - exponent * fxzPart
						);

						float thisSmoothingWeightModifier = thisBiome.getSmoothingWeight();
						thisWeight *= thisSmoothingWeightModifier;
						thisWeightFx *= thisSmoothingWeightModifier;
						thisWeightFz *= thisSmoothingWeightModifier;
						thisWeightFxz *= thisSmoothingWeightModifier;

						heightVariationPre += thisHeightVariation * thisWeight;
						heightVariationPreFx += thisHeightVariation * thisWeightFx;
						heightVariationPreFz += thisHeightVariation * thisWeightFz;
						heightVariationPreFxz += thisHeightVariation * thisWeightFxz;
						baseHeightPre += thisBaseHeight * thisWeight;
						baseHeightPreFx += thisBaseHeight * thisWeightFx;
						baseHeightPreFz += thisBaseHeight * thisWeightFz;
						baseHeightPreFxz += thisBaseHeight * thisWeightFxz;
						totalWeight += thisWeight;
						totalWeightFx += thisWeightFx;
						totalWeightFz += thisWeightFz;
						totalWeightFxz += thisWeightFxz;
					}
				}

				//Normalize those
				double heightVariation = heightVariationPre / totalWeight;
				double heightVariationFx = (totalWeight * heightVariationPreFx - heightVariationPre * totalWeightFx) / (totalWeight * totalWeight);
				double heightVariationFz = (totalWeight * heightVariationPreFz - heightVariationPre * totalWeightFz) / (totalWeight * totalWeight);
				double heightVariationFxz = (heightVariationPreFxz * totalWeight * totalWeight + 2 * heightVariationPre * totalWeightFx * totalWeightFz - totalWeight * (heightVariationPreFx * totalWeightFz + heightVariationPreFz * totalWeightFx + heightVariationPre * totalWeightFxz)) / (totalWeight * totalWeight * totalWeight);
				double baseHeight = baseHeightPre / totalWeight;
				double baseHeightFx = (totalWeight * baseHeightPreFx - baseHeightPre * totalWeightFx) / (totalWeight * totalWeight);
				double baseHeightFz = (totalWeight * baseHeightPreFz - baseHeightPre * totalWeightFz) / (totalWeight * totalWeight);
				double baseHeightFxz = (baseHeightPreFxz * totalWeight * totalWeight + 2 * baseHeightPre * totalWeightFx * totalWeightFz - totalWeight * (baseHeightPreFx * totalWeightFz + baseHeightPreFz * totalWeightFx + baseHeightPre * totalWeightFxz)) / (totalWeight * totalWeight * totalWeight);
				
				//Why weren't... the biomes... just originally like this? I guess to simplify manual value assignment
				heightVariation = heightVariation * 0.9F + 0.1F;
				heightVariationFx *= 0.9f;
				heightVariationFz *= 0.9f;
				heightVariationFxz *= 0.9f;

				baseHeight = (baseHeight * 4.0 - 1.0) / 8.0;
				baseHeightFx /= 2.0;
				baseHeightFz /= 2.0;
				baseHeightFxz /= 2.0;

				double base = (1 + baseHeight * BASE_HEIGHT_SCALE + BASE_HEIGHT_OFFSET) * settings.baseSize;
				double baseFx = baseHeightFx * BASE_HEIGHT_SCALE * settings.baseSize;
				double baseFz = baseHeightFz * BASE_HEIGHT_SCALE * settings.baseSize;
				double baseFxz = baseHeightFxz * BASE_HEIGHT_SCALE * settings.baseSize;
				
				for (int iy = 0; iy < 33; iy++) {
					
					//Two noises, one heightmap for each scale
					double na = noiseL1Values[i * D3C1.STEP + D3C1.F];
					double naFx = noiseL1Values[i * D3C1.STEP + D3C1.Fx];
					double naFy = noiseL1Values[i * D3C1.STEP + D3C1.Fy];
					double naFz = noiseL1Values[i * D3C1.STEP + D3C1.Fz];
					double naFxy = noiseL1Values[i * D3C1.STEP + D3C1.Fxy];
					double naFxz = noiseL1Values[i * D3C1.STEP + D3C1.Fxz];
					double naFyz = noiseL1Values[i * D3C1.STEP + D3C1.Fyz];
					double naFxyz = noiseL1Values[i * D3C1.STEP + D3C1.Fxyz];
					double nb = noiseL2Values[i * D3C1.STEP + D3C1.F];
					double nbFx = noiseL2Values[i * D3C1.STEP + D3C1.Fx];
					double nbFy = noiseL2Values[i * D3C1.STEP + D3C1.Fy];
					double nbFz = noiseL2Values[i * D3C1.STEP + D3C1.Fz];
					double nbFxy = noiseL2Values[i * D3C1.STEP + D3C1.Fxy];
					double nbFxz = noiseL2Values[i * D3C1.STEP + D3C1.Fxz];
					double nbFyz = noiseL2Values[i * D3C1.STEP + D3C1.Fyz];
					double nbFxyz = noiseL2Values[i * D3C1.STEP + D3C1.Fxyz];

					//Curve them to make the heightmaps smoothly not go as far down as they go up (second edition)
					double na2 = ((na + 1) * (na + 1) * (na + 5) - 5) / 24.0;
					double na2Fx = naFx * (na + 1) * (3 * na + 11) / 24.0;
					double na2Fy = naFy * (na + 1) * (3 * na + 11) / 24.0;
					double na2Fz = naFz * (na + 1) * (3 * na + 11) / 24.0;
					double na2Fxy = (naFx * naFy * (6 * na + 14) + naFxy * (3 * na * na + 14 * na + 11)) / 24.0;
					double na2Fxz = (naFx * naFz * (6 * na + 14) + naFxz * (3 * na * na + 14 * na + 11)) / 24.0;
					double na2Fyz = (naFy * naFz * (6 * na + 14) + naFyz * (3 * na * na + 14 * na + 11)) / 24.0;
					double na2Fxyz = (naFx * naFy * naFz * 6 + (naFx * naFyz + naFy * naFxz + naFz * naFxy) * (6 * na + 14) + naFxyz * (3 * na * na + 14 * na + 11)) / 24.0;
					double nb2 = ((nb + 1) * (nb + 1) * (nb + 5) - 5) / 24.0;
					double nb2Fx = nbFx * (nb + 1) * (3 * nb + 11) / 24.0;
					double nb2Fy = nbFy * (nb + 1) * (3 * nb + 11) / 24.0;
					double nb2Fz = nbFz * (nb + 1) * (3 * nb + 11) / 24.0;
					double nb2Fxy = (nbFx * nbFy * (6 * nb + 14) + nbFxy * (3 * nb * nb + 14 * nb + 11)) / 24.0;
					double nb2Fxz = (nbFx * nbFz * (6 * nb + 14) + nbFxz * (3 * nb * nb + 14 * nb + 11)) / 24.0;
					double nb2Fyz = (nbFy * nbFz * (6 * nb + 14) + nbFyz * (3 * nb * nb + 14 * nb + 11)) / 24.0;
					double nb2Fxyz = (nbFx * nbFy * nbFz * 6 + (nbFx * nbFyz + nbFy * nbFxz + nbFz * nbFxy) * (6 * nb + 14) + nbFxyz * (3 * nb * nb + 14 * nb + 11)) / 24.0;

					//Scale it as determined by settings, the biome / biome blending, and the variation padding
					na2Fxyz = na2Fy * heightVariationFxz * a + na2Fyz * heightVariationFx * a + na2Fxyz * (heightVariation * a + VARIATION_PADDING);
					na2Fxy = na2Fy * heightVariationFx * a + na2Fxy * (heightVariation * a + VARIATION_PADDING);
					na2Fxz = na2 * heightVariationFxz * a + na2Fz * heightVariationFx * a + na2Fx * heightVariationFz * a + na2Fxz * (heightVariation * a + VARIATION_PADDING);
					na2Fyz = na2Fy * heightVariationFz * a + na2Fyz * (heightVariation * a + VARIATION_PADDING);
					na2Fx = na2 * heightVariationFx * a + na2Fx * (heightVariation * a + VARIATION_PADDING);
					na2Fy = na2Fy * (heightVariation * a + VARIATION_PADDING);
					na2Fz = na2 * heightVariationFz * a + na2Fz * (heightVariation * a + VARIATION_PADDING);
					na2 = na2 * (heightVariation * a + VARIATION_PADDING);
					nb2Fxyz = nb2Fy * heightVariationFxz * b + nb2Fyz * heightVariationFx * b + nb2Fxyz * (heightVariation * b + VARIATION_PADDING);
					nb2Fxy = nb2Fy * heightVariationFx * b + nb2Fxy * (heightVariation * b + VARIATION_PADDING);
					nb2Fxz = nb2 * heightVariationFxz * b + nb2Fz * heightVariationFx * b + nb2Fx * heightVariationFz * b + nb2Fxz * (heightVariation * b + VARIATION_PADDING);
					nb2Fyz = nb2Fy * heightVariationFz * b + nb2Fyz * (heightVariation * b + VARIATION_PADDING);
					nb2Fx = nb2 * heightVariationFx * b + nb2Fx * (heightVariation * b + VARIATION_PADDING);
					nb2Fy = nb2Fy * (heightVariation * b + VARIATION_PADDING);
					nb2Fz = nb2 * heightVariationFz * b + nb2Fz * (heightVariation * b + VARIATION_PADDING);
					nb2 = nb2 * (heightVariation * b + VARIATION_PADDING);
					
					//Interpolate between them using another noise.
					double noise = noiseMainValues[i * D3C1.STEP + D3C1.F] * .5 + .5;
					double noiseFx = noiseMainValues[i * D3C1.STEP + D3C1.Fx] * .5;
					double noiseFy = noiseMainValues[i * D3C1.STEP + D3C1.Fy] * .5;
					double noiseFz = noiseMainValues[i * D3C1.STEP + D3C1.Fz] * .5;
					double noiseFxy = noiseMainValues[i * D3C1.STEP + D3C1.Fxy] * .5;
					double noiseFxz = noiseMainValues[i * D3C1.STEP + D3C1.Fxz] * .5;
					double noiseFyz = noiseMainValues[i * D3C1.STEP + D3C1.Fyz] * .5;
					double noiseFxyz = noiseMainValues[i * D3C1.STEP + D3C1.Fxyz] * .5;
					double value = (1 - noise) * na2 + noise * nb2;
					double valueFx = (1 - noise) * na2Fx + -noiseFx * na2 + noise * nb2Fx + noiseFx * nb2;
					double valueFy = (1 - noise) * na2Fy + -noiseFy * na2 + noise * nb2Fy + noiseFy * nb2;
					double valueFz = (1 - noise) * na2Fz + -noiseFz * na2 + noise * nb2Fz + noiseFz * nb2;
					double valueFxy = (1 - noise) * na2Fxy + -noiseFy * na2Fx + -noiseFx * na2Fy + -noiseFxy * na2
							+ noise * nb2Fxy + noiseFy * nb2Fx + noiseFx * nb2Fy + noiseFxy * nb2;
					double valueFxz = (1 - noise) * na2Fxz + -noiseFz * na2Fx + -noiseFx * na2Fz + -noiseFxz * na2
							+ noise * nb2Fxz + noiseFz * nb2Fx + noiseFx * nb2Fz + noiseFxz * nb2;
					double valueFyz = (1 - noise) * na2Fyz + -noiseFz * na2Fy + -noiseFy * na2Fz + -noiseFyz * na2
							+ noise * nb2Fyz + noiseFz * nb2Fy + noiseFy * nb2Fz + noiseFyz * nb2;
					double valueFxyz = (1 - noise) * na2Fxyz + -noiseFy * na2Fxz + -noiseFx * na2Fyz + -noiseFxy * na2Fz
							+ noise * nb2Fxyz + noiseFy * nb2Fxz + noiseFx * nb2Fyz + noiseFxy * nb2Fz
							+ -noiseFz * na2Fxy + -noiseFyz * na2Fx + -noiseFxz * na2Fy + -noiseFxyz * na2
							+ noiseFz * nb2Fxy + noiseFyz * nb2Fx + noiseFxz * nb2Fy + noiseFxyz * nb2;
					
					//This setting increases/decreases the variation
					value /= settings.stretchY;
					valueFx /= settings.stretchY;
					valueFy /= settings.stretchY;
					valueFz /= settings.stretchY;
					valueFxy /= settings.stretchY;
					valueFxz /= settings.stretchY;
					valueFyz /= settings.stretchY;
					valueFxyz /= settings.stretchY;
					
					//Now add the base as determined by the biome / biome blending
					value += base;
					valueFx += baseFx;
					valueFz += baseFz;
					valueFxz += baseFxz;
					
					//Decreases at a flat rate as Y goes up.
					//If the noise stayed constant in Y, it would be equivalent to a heightmap.
					//This is a generalization of the concept of a heightmap that enables cliffs and overhangs.
					value -= iy;
					valueFy -= 1.0 / 8.0;
					
					//"heightmap" is a 3D "density map".
					heightmap[i * D3C1.STEP + D3C1.F] = value;
					heightmap[i * D3C1.STEP + D3C1.Fx] = valueFx;
					heightmap[i * D3C1.STEP + D3C1.Fy] = valueFy;
					heightmap[i * D3C1.STEP + D3C1.Fz] = valueFz;
					heightmap[i * D3C1.STEP + D3C1.Fxy] = valueFxy;
					heightmap[i * D3C1.STEP + D3C1.Fxz] = valueFxz;
					heightmap[i * D3C1.STEP + D3C1.Fyz] = valueFyz;
					heightmap[i * D3C1.STEP + D3C1.Fxyz] = valueFxyz;
					
					i++;
				}
				//j++;
				
			}
		}
	}

	public void replaceBiomeBlocks(int blockX, int blockZ, ChunkPrimer chunkPrimer, Biome[] biomes)
	{
		if (!ForgeEventFactory.onReplaceBiomeBlocks(this, blockX, blockZ, chunkPrimer, world))
			return;

		double d0 = 0.03125 * 2 * 1.5;

		for (int k = 0; k < 16; ++k)
		{
			for (int l = 0; l < 16; ++l)
			{
				Biome biomegenbase = biomes[l + k * 16];
				stoneNoise[l + k * 16] = stoneNoiseGen.eval((blockX * 16 + k) * d0, (blockZ * 16 + l) * d0);
				biomegenbase.genTerrainBlocks(world, rand, chunkPrimer, blockX * 16 + k, blockZ * 16 + l, stoneNoise[l + k * 16]);
			}
		}
	}

	@Override
	public Chunk provideChunk(int x, int z)
	{	
		rand.setSeed(x * 341873128712L + z * 132897987541L);
		ChunkPrimer primer = new ChunkPrimer();
		setBlocksInChunk(x, z, primer);
		biomes = world.getBiomeProvider().getBiomes(biomes, x * 16, z * 16, 16, 16);
		replaceBiomeBlocks(x, z, primer, biomes);
		
		//Now that the biome block placement has occurred, it is now safe to go through and process cave block removal.
		for (int iz = 0; iz < 16; iz++) {
			for (int ix = 0; ix < 16; ix++) {
				boolean hitSurface = false;
				BiomeGenesis biome = (BiomeGenesis) biomes[iz * 16 + ix];
				for (int iy = 255; iy >= 1; iy--) {

					//Get the block currently at this point.
					IBlockState thisBlockState = primer.getBlockState(ix, iy, iz);
					Block thisBlock = thisBlockState.getBlock();

					//The block needs to be either in the list or be a biome block to be diggable.
					if (!CAVE_DIGGABLE_BLOCKS.contains(thisBlock)
							&& thisBlock != biome.topBlock.getBlock()
							&& thisBlock != biome.fillerBlock.getBlock())
					{
						if (thisBlock != Blocks.AIR)
							hitSurface = true;
						continue;
					}
					
					int chunkBlockIndex = ix * 256 * 16 + iz * 256 + iy;
					if (!caveOpening[chunkBlockIndex]) {
						hitSurface = true;
						continue;
					}
					
					//Replace blocks above and below if we should.
					//Vary the height where we stop by re-using the stone noise
					if (iy > 60 + 3 * stoneNoise[iz + ix * 16])
					{
						//Block above
						if (iy != 255) {
							IBlockState upTransition = CAVE_ABOVE_BLOCK_REPLACEMENTS.getOrDefault(primer.getBlockState(ix, iy + 1, iz), null);
							if (upTransition != null) {
								primer.setBlockState(ix, iy + 1, iz, upTransition);
							}
						}

						//Block below (biome top block shift-down)
						if (biome.fillerBlock.equals(primer.getBlockState(ix, iy - 1, iz))) {
							IBlockState state = hitSurface ? biome.fillerBlock : biome.getTopBlock(rand);
							primer.setBlockState(ix, iy - 1, iz, state);
						}
					}

					//Dig this block, it's part of a cave. Don't set hitSurface - this block is no longer there
					primer.setBlockState(ix, iy, iz, CAVE_LEVEL_REPLACEMENT_BLOCKS[iy]);
				}
			}
		}

		Chunk chunk = new Chunk(world, primer, x, z);
		byte[] abyte = chunk.getBiomeArray();

		for (int k = 0; k < abyte.length; ++k)
		{
			abyte[k] = (byte) Biome.getIdForBiome(biomes[k]);
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
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return world.getBiome(pos).getSpawnableList(creatureType);
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z)
	{
	}
}
