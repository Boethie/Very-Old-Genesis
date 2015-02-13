package genesis.world;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.CAVE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.MINESHAFT;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.RAVINE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.SCATTERED_FEATURE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.STRONGHOLD;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.VILLAGE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;
import genesis.common.GenesisBlocks;
import genesis.world.gen.feature.WorldGenLakesGenesis;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class ChunkProviderGenesis implements IChunkProvider 
{
	private final double[] field_147434_q;
	private final float[] parabolicField;
	/**
	 * A NoiseGeneratorOctaves used in generating terrain
	 */
	public NoiseGeneratorOctaves octaves3;
	/**
	 * A NoiseGeneratorOctaves used in generating terrain
	 */
	public NoiseGeneratorOctaves octaves4;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	double[] noise2;
	double[] noise0;
	double[] noise1;
	double[] noise4;
	
	/**
	 * RNG.
	 */
	private Random rand;
	private NoiseGeneratorOctaves octaves0;
	private NoiseGeneratorOctaves octaves1;
	private NoiseGeneratorOctaves octaves2;
	private NoiseGeneratorPerlin perlin;
	private NoiseGeneratorPerlin perlinLF;
	private NoiseGeneratorPerlin perlinLR;
	/**
	 * Reference to the World object.
	 */
	private World worldObj;
	/**
	 * are map structures going to be generated (e.g. strongholds)
	 */
	private WorldType worldType;
	private double[] stoneNoise = new double[256];
	private double[] lavaFloorNoise = new double[256];
	private double[] lavaRoofNoise = new double[256];
	private MapGenBase caveGenerator = new MapGenCavesGenesis();
	private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
	/**
	 * Holds ravine generator
	 */
	private MapGenBase ravineGenerator = new MapGenRavineGenesis();
	/**
	 * The biomes that are used to generate the chunk
	 */
	private BiomeGenBase[] biomesForGeneration;

	public ChunkProviderGenesis(World world, long par2) 
	{
		this.worldObj = world;
		this.worldType = world.getWorldInfo().getTerrainType();
		this.rand = new Random(par2);
		this.octaves0 = new NoiseGeneratorOctaves(this.rand, 16);
		this.octaves1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.octaves2 = new NoiseGeneratorOctaves(this.rand, 8);
		this.perlin = new NoiseGeneratorPerlin(this.rand, 4);
		this.perlinLF = new NoiseGeneratorPerlin(this.rand, 4);
		this.perlinLR = new NoiseGeneratorPerlin(this.rand, 4);
		this.octaves3 = new NoiseGeneratorOctaves(this.rand, 10);
		this.octaves4 = new NoiseGeneratorOctaves(this.rand, 16);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.field_147434_q = new double[825];
		this.parabolicField = new float[25];

		for (int j = -2; j <= 2; ++j) 
		{
			for (int k = -2; k <= 2; ++k) 
			{
				float f = 10.0F / MathHelper.sqrt_float((float) (j * j + k * k) + 0.2F);
				this.parabolicField[j + 2 + (k + 2) * 5] = f;
			}
		}
	}

	public void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer) 
	{
		byte b0 = 63;
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, chunkX * 4 - 2, chunkZ * 4 - 2, 10, 10);
		this.func_147423_a(chunkX * 4, 0, chunkZ * 4);

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
					double d1 = this.field_147434_q[k1 + k2];
					double d2 = this.field_147434_q[l1 + k2];
					double d3 = this.field_147434_q[i2 + k2];
					double d4 = this.field_147434_q[j2 + k2];
					double d5 = (this.field_147434_q[k1 + k2 + 1] - d1) * d0;
					double d6 = (this.field_147434_q[l1 + k2 + 1] - d2) * d0;
					double d7 = (this.field_147434_q[i2 + k2 + 1] - d3) * d0;
					double d8 = (this.field_147434_q[j2 + k2 + 1] - d4) * d0;

					for (int l2 = 0; l2 < 8; ++l2) 
					{
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int i3 = 0; i3 < 4; ++i3) 
						{
							int j3 = i3 + k * 4 << 12 | 0 + j1 * 4 << 8 | k2 * 8 + l2;
							short short1 = 256;
							j3 -= short1;
							double d14 = 0.25D;
							double d16 = (d11 - d10) * d14;
							double d15 = d10 - d16;

							for (int k3 = 0; k3 < 4; ++k3) 
							{
								if ((d15 += d16) > 0.0D) 
								{
									primer.setBlockState(j3 += short1, Blocks.stone.getDefaultState());
								} 
								else if (k2 * 8 + l2 < b0) 
								{
									primer.setBlockState(j3 += short1, Blocks.water.getDefaultState());
								} 
								else 
								{
									primer.setBlockState(j3 += short1, null);
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

	public void replaceBlocksForBiome(int p_147422_1_, int p_147422_2_, ChunkPrimer primer, BiomeGenBase[] p_147422_5_) 
	{
		double d0 = 0.03125D;
		this.stoneNoise = this.perlin.func_151599_a(this.stoneNoise, (double) (p_147422_1_ * 16), (double) (p_147422_2_ * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);
		this.lavaFloorNoise = this.perlinLF.func_151599_a(this.lavaFloorNoise, (double) (p_147422_1_ * 16), (double) (p_147422_2_ * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);
		this.lavaRoofNoise = this.perlinLR.func_151599_a(this.lavaRoofNoise, (double) (p_147422_1_ * 16), (double) (p_147422_2_ * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

		for (int k = 0; k < 16; ++k) 
		{
			for (int l = 0; l < 16; ++l) 
			{
				BiomeGenBase biomegenbase = p_147422_5_[l + k * 16];
				biomegenbase.genTerrainBlocks(this.worldObj, this.rand, primer, p_147422_1_ * 16 + k, p_147422_2_ * 16 + l, this.stoneNoise[l + k * 16]);

				int i1 = (p_147422_1_ * 16 + k) & 15;
				int j1 = (p_147422_2_ * 16 + l) & 15;
				int k1 = 65536 / 256;

				Block stoneType;
				Block lowerStoneType;

				//Default Stone Layer
				if (Math.abs(this.stoneNoise[l + k * 16]) > 3.25D) 
				{
					stoneType = GenesisBlocks.quartzite;
				} 
				else if (Math.abs(this.stoneNoise[l + k * 16]) > 0.75D) 
				{
					stoneType = GenesisBlocks.granite;
				} 
				else 
				{
					stoneType = GenesisBlocks.quartzite;
				}

				//Low Stone Layer
				if (this.stoneNoise[l + k * 16] > 1.75D) 
				{
					lowerStoneType = GenesisBlocks.faux_amphibolite;
				} 
				else if (this.stoneNoise[l + k * 16] < -1.75D) 
				{
					lowerStoneType = GenesisBlocks.gneiss;
				} 
				else 
				{
					lowerStoneType = stoneType;
				}

				for (int l1 = 255; l1 >= 0; --l1) 
				{
					int i2 = (j1 * 16 + i1) * k1 + l1;

					if (primer.getBlockState(i2).getBlock() == Blocks.stone) 
					{
						double lavaFloorNoise = this.lavaFloorNoise[l + k * 16];
						if (lavaFloorNoise < 0) 
						{
							lavaFloorNoise = 0;
						}

						if (l1 < 6 + lavaFloorNoise)
						{
							primer.setBlockState(i2, GenesisBlocks.komatiite.getDefaultState());
						} 
						else if (l1 < 9) 
						{
							primer.setBlockState(i2,  /*TODO: Replace with komatiitic*/ Blocks.lava.getDefaultState());
						} 
						else if (l1 < 16 + this.lavaRoofNoise[l + k * 16]) 
						{
							primer.setBlockState(i2, Blocks.air.getDefaultState());
						} 
						else if (l1 < 24 + rand.nextInt(4)) 
						{
							primer.setBlockState(i2, GenesisBlocks.komatiite.getDefaultState());
						} 
						else if (l1 < 36 + rand.nextInt(4)) 
						{
							primer.setBlockState(i2, lowerStoneType.getDefaultState());
						} 
						else 
						{
							primer.setBlockState(i2, stoneType.getDefaultState());
						}
					} 
					else if (primer.getBlockState(i2).getBlock() == GenesisBlocks.limestone) 
					{
						if ((primer.getBlockState(i2+1).getBlock() == Blocks.water || primer.getBlockState(i2+1).getBlock() == Blocks.flowing_water || primer.getBlockState(i2+1).getBlock() == GenesisBlocks.shale) && this.stoneNoise[l + k * 16] > 1.25D) 
						{
							primer.setBlockState(i2, GenesisBlocks.shale.getDefaultState());
						} 
						else if ((primer.getBlockState(i2+1).getBlock() == Blocks.water || primer.getBlockState(i2+1).getBlock() == Blocks.flowing_water || primer.getBlockState(i2+1).getBlock() == Blocks.dirt) && this.stoneNoise[l + k * 16] < -1.25D) 
						{
							primer.setBlockState(i2, Blocks.dirt.getDefaultState());
						}
					}
				}
			}
		}
	}

	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	public Chunk loadChunk(int par1, int par2) 
	{
		return this.provideChunk(par1, par2);
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
	 * specified chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(int x, int z) 
	{
		this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
		ChunkPrimer chunkPrimer = new ChunkPrimer();
		this.setBlocksInChunk(x, z, chunkPrimer);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
		this.replaceBlocksForBiome(x, z, chunkPrimer, this.biomesForGeneration);
		this.caveGenerator.func_175792_a(this, this.worldObj, x, z, chunkPrimer);
		this.ravineGenerator.func_175792_a(this, this.worldObj, x, z, chunkPrimer);

		this.scatteredFeatureGenerator.func_175792_a(this, this.worldObj, x, z, chunkPrimer);

		Chunk chunk = new Chunk(this.worldObj, chunkPrimer, x, z);
		byte[] abyte1 = chunk.getBiomeArray();

		for (int k = 0; k < abyte1.length; ++k) 
		{
			abyte1[k] = (byte) this.biomesForGeneration[k].biomeID;
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	private void func_147423_a(int x, int y, int z) 
	{
		double d0 = 684.412D;
		double d1 = 684.412D;
		double d2 = 512.0D;
		double d3 = 512.0D;
		this.noise4 = this.octaves4.generateNoiseOctaves(this.noise4, x, z, 5, 5, 200.0D, 200.0D, 0.5D);
		this.noise2 = this.octaves2.generateNoiseOctaves(this.noise2, x, y, z, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
		this.noise0 = this.octaves0.generateNoiseOctaves(this.noise0, x, y, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
		this.noise1 = this.octaves1.generateNoiseOctaves(this.noise1, x, y, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
		boolean flag1 = false;
		boolean flag = false;
		int l = 0;
		int i1 = 0;
		double d4 = 8.5D;

		for (int j1 = 0; j1 < 5; ++j1) 
		{
			for (int k1 = 0; k1 < 5; ++k1)
			{
				float f = 0.0F;
				float f1 = 0.0F;
				float f2 = 0.0F;
				byte b0 = 2;
				BiomeGenBase biomegenbase = this.biomesForGeneration[j1 + 2 + (k1 + 2) * 10];

				for (int l1 = -b0; l1 <= b0; ++l1) 
				{
					for (int i2 = -b0; i2 <= b0; ++i2) 
					{
						BiomeGenBase biomegenbase1 = this.biomesForGeneration[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
						float f3 = biomegenbase1.minHeight;
						float f4 = biomegenbase1.maxHeight;

						if (this.worldType == WorldType.AMPLIFIED && f3 > 0.0F) 
						{
							f3 = 1.0F + f3 * 2.0F;
							f4 = 1.0F + f4 * 4.0F;
						}

						float f5 = this.parabolicField[l1 + 2 + (i2 + 2) * 5] / (f3 + 2.0F);

						if (biomegenbase1.minHeight > biomegenbase.maxHeight) 
						{
							f5 /= 2.0F;
						}

						f += f4 * f5;
						f1 += f3 * f5;
						f2 += f5;
					}
				}

				f /= f2;
				f1 /= f2;
				f = f * 0.9F + 0.1F;
				f1 = (f1 * 4.0F - 1.0F) / 8.0F;
				double d13 = this.noise4[i1] / 1000.0D;

				if (d13 < 0.0D) 
				{
					d13 = -d13 * 0.3D;
				}

				d13 = d13 * 3.0D - 2.0D;

				if (d13 < 0.0D) 
				{
					d13 /= 2.0D;

					if (d13 < -1.0D) 
					{
						d13 = -1.0D;
					}

					d13 /= 1.4D;
					d13 /= 2.0D;
				} 
				else 
				{
					if (d13 > 1.0D) 
					{
						d13 = 1.0D;
					}

					d13 /= 8.0D;
				}

				++i1;
				double d12 = (double) f1;
				double d14 = (double) f;
				d12 += d13 * 0.2D;
				d12 = d12 * 8.5D / 8.0D;
				double d5 = 8.5D + d12 * 4.0D;

				for (int j2 = 0; j2 < 33; ++j2) 
				{
					double d6 = ((double) j2 - d5) * 12.0D * 128.0D / 256.0D / d14;

					if (d6 < 0.0D) 
					{
						d6 *= 4.0D;
					}

					double d7 = this.noise0[l] / 512.0D;
					double d8 = this.noise1[l] / 512.0D;
					double d9 = (this.noise2[l] / 10.0D + 1.0D) / 2.0D;
					double d10 = MathHelper.denormalizeClamp(d7, d8, d9) - d6;

					if (j2 > 29) 
					{
						double d11 = (double) ((float) (j2 - 29) / 3.0F);
						d10 = d10 * (1.0D - d11) + -10.0D * d11;
					}

					this.field_147434_q[l] = d10;
					++l;
				}
			}
		}
	}

	/**
	 * Checks to see if a chunk exists at x, y
	 */
	 public boolean chunkExists(int par1, int par2) 
	 {
		 return true;
	 }

	/**
	 * Populates chunk with ores etc etc
	 */
	 public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) 
	 {
		BlockFalling.fallInstantly = true;
		int k = par2 * 16;
		int l = par3 * 16;
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(k + 16, 0, l + 16));
		this.rand.setSeed(this.worldObj.getSeed());
		long i1 = this.rand.nextLong() / 2L * 2L + 1L;
		long j1 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long) par2 * i1 + (long) par3 * j1 ^ this.worldObj.getSeed());

		this.scatteredFeatureGenerator./*generateStructuresInChunk*/func_175794_a(this.worldObj, rand, new ChunkCoordIntPair(par2, par3));

		int k1;
		int l1;
		int i2;

		if (this.rand.nextInt(2) == 0) 
		{
			k1 = k + this.rand.nextInt(16) + 8;
			l1 = this.rand.nextInt(256);
			i2 = l + this.rand.nextInt(16) + 8;
			(new WorldGenLakesGenesis(Blocks.flowing_water)).generate(this.worldObj, this.rand, new BlockPos(k1, l1, i2));
		}

		if (this.rand.nextInt(8) == 0) 
		{
			k1 = k + this.rand.nextInt(16) + 8;
			l1 = this.rand.nextInt(this.rand.nextInt(248) + 8);
			i2 = l + this.rand.nextInt(16) + 8;

			if (l1 < 63 || this.rand.nextInt(10) == 0) 
			{
				//TODO: Change the lava below to komatiitic lava
				(new WorldGenLakesGenesis(Blocks.flowing_lava)).generate(this.worldObj, this.rand, new BlockPos(k1, l1, i2));
			}
		}

		biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(k, 0, l));
		SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
		k += 8;
		l += 8;

		for (k1 = 0; k1 < 16; k1++) 
		{
			for (l1 = 0; l1 < 16; ++l1) 
			{
				BlockPos top = this.worldObj.getPrecipitationHeight(new BlockPos(k + k1, 0, l + l1));

				if (this.worldObj.canBlockFreeze(top.add(0, -1, 0), false)) 
				{
					this.worldObj.setBlockState(top.add(0, -1, 0), Blocks.ice.getDefaultState(), 2);
				}

				if (this.worldObj.canSnowAt(top, true)) 
				{
					this.worldObj.setBlockState(top, Blocks.snow_layer.getDefaultState(), 2);
				}
			}
		}

		BlockFalling.fallInstantly = false;
	 }

	 /**
	  * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
	  * Return true if all chunks have been saved.
	  */
	 public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) 
	 {
		 return true;
	 }

	 /**
	  * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
	  * unimplemented.
	  */
	 public void saveExtraData() 
	 {
	 }

	 /**
	  * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
	  */
	 public boolean unloadQueuedChunks() 
	 {
		 return false;
	 }

	 /**
	  * Returns if the IChunkProvider supports saving.
	  */
	 public boolean canSave() 
	 {
		 return true;
	 }

	 /**
	  * Converts the instance data to a readable string.
	  */
	 public String makeString() 
	 {
		 return "RandomLevelSource";
	 }

	 /**
	  * Returns a list of creatures of the specified type that can spawn at the given location.
	  */
	 public List func_177458_a(EnumCreatureType par1EnumCreatureType, BlockPos pos) 
	 {
		 BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
		 return par1EnumCreatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.func_175798_a(pos) ? this.scatteredFeatureGenerator.getScatteredFeatureSpawnList() : biomegenbase.getSpawnableList(par1EnumCreatureType);
	 }

	 public BlockPos getStrongholdGen(World worldIn, String p_180513_2_, BlockPos p_180513_3_) 
	 {
		 return null;
	 }

	 public int getLoadedChunkCount() 
	 {
		 return 0;
	 }

	 public void recreateStructures(Chunk chunk, int x, int z) 
	 {
		 this.scatteredFeatureGenerator.func_175792_a(this, this.worldObj, x, z, (ChunkPrimer)null);
	 }

	 @Override
	 public Chunk provideChunk(BlockPos blockPosIn) 
	 {
		 return provideChunk(blockPosIn.getX(), blockPosIn.getZ());
	 }

	 @Override
	 public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) 
	 {
		 //This seems to have something to do with the ocean monument.
		 return false;
	 }
}
