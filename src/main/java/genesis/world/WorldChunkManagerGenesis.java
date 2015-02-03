package genesis.world;

import genesis.world.gen.layer.GenLayerGenesis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldChunkManagerGenesis extends WorldChunkManager {
	private GenLayer genBiomes;
	/**
	 * A GenLayer containing the indices into BiomeGenBase.biomeList[]
	 */
	private GenLayer biomeIndexLayer;
	/**
	 * The BiomeCache object for this world.
	 */
	private BiomeCache biomeCache;
	/**
	 * A list of biomes that the player can spawn in.
	 */
	private List biomesToSpawnIn;

	protected WorldChunkManagerGenesis() {
		this.biomeCache = new BiomeCache(this);
		this.biomesToSpawnIn = new ArrayList();
	}

	public WorldChunkManagerGenesis(long seed, WorldType worldType) {
		this();
		GenLayer[] agenlayer = GenLayerGenesis.initializeAllBiomeGenerators(seed, worldType);
		agenlayer = getModdedBiomeGenerators(worldType, seed, agenlayer);
		this.genBiomes = agenlayer[0];
		this.biomeIndexLayer = agenlayer[1];
	}

	public WorldChunkManagerGenesis(World world) {
		this(world.getSeed(), WorldTypeGenesis.instance);
	}

	/**
	 * Gets the list of valid biomes for the player to spawn in.
	 */
	public List getBiomesToSpawnIn() {
		return this.biomesToSpawnIn;
	}

	//    /**
	//     * Returns the BiomeGenBase related to the x, z position on the world.
	//     */
	//    public BiomeGenBase getBiomeGenAt(int par1, int par2) {
	//        return this.biomeCache.getBiomeGenAt(par1, par2);
	//    }

	/**
	 * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
	 */
	public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length) {
		IntCache.resetIntCache();

		if (listToReuse == null || listToReuse.length < width * length)
		{
			listToReuse = new float[width * length];
		}

		int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);

		for (int i1 = 0; i1 < width * length; ++i1)
		{
			try
			{
				float f = (float)BiomeGenBase.getBiomeFromBiomeList(aint[i1], BiomeGenBase.field_180279_ad).getIntRainfall() / 65536.0F;

				if (f > 1.0F)
				{
					f = 1.0F;
				}

				listToReuse[i1] = f;
			}
			catch (Throwable throwable)
			{
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
				crashreportcategory.addCrashSection("biome id", Integer.valueOf(i1));
				crashreportcategory.addCrashSection("downfalls[] size", Integer.valueOf(listToReuse.length));
				crashreportcategory.addCrashSection("x", Integer.valueOf(x));
				crashreportcategory.addCrashSection("z", Integer.valueOf(z));
				crashreportcategory.addCrashSection("w", Integer.valueOf(width));
				crashreportcategory.addCrashSection("h", Integer.valueOf(length));
				throw new ReportedException(crashreport);
			}
		}

		return listToReuse;
	}

	/**
	 * Return an adjusted version of a given temperature based on the y height
	 */
	@SideOnly(Side.CLIENT)
	public float getTemperatureAtHeight(float par1, int par2) {
		return par1;
	}

	/**
	 * Returns an array of biomes for the location input.
	 */
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
		IntCache.resetIntCache();

		if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5) {
			par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
		}

		int[] aint = this.genBiomes.getInts(par2, par3, par4, par5);

		try {
			for (int i1 = 0; i1 < par4 * par5; ++i1) {
				par1ArrayOfBiomeGenBase[i1] = BiomeGenBase.getBiome(aint[i1]);
			}

			return par1ArrayOfBiomeGenBase;
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
			crashreportcategory.addCrashSection("biomes[] size", par1ArrayOfBiomeGenBase.length);
			crashreportcategory.addCrashSection("x", par2);
			crashreportcategory.addCrashSection("z", par3);
			crashreportcategory.addCrashSection("w", par4);
			crashreportcategory.addCrashSection("h", par5);
			throw new ReportedException(crashreport);
		}
	}

	/**
	 * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
	 * WorldChunkManager Args: oldBiomeList, x, z, width, depth
	 */
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
		return this.getBiomeGenAt(par1ArrayOfBiomeGenBase, par2, par3, par4, par5, true);
	}

	/**
	 * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
	 * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
	 */
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5, boolean par6) {
		IntCache.resetIntCache();

		if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5) {
			par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
		}

		if (par6 && par4 == 16 && par5 == 16 && (par2 & 15) == 0 && (par3 & 15) == 0) {
			BiomeGenBase[] abiomegenbase1 = this.biomeCache.getCachedBiomes(par2, par3);
			System.arraycopy(abiomegenbase1, 0, par1ArrayOfBiomeGenBase, 0, par4 * par5);
			return par1ArrayOfBiomeGenBase;
		} else {
			int[] aint = this.biomeIndexLayer.getInts(par2, par3, par4, par5);

			for (int i1 = 0; i1 < par4 * par5; ++i1) {
				par1ArrayOfBiomeGenBase[i1] = BiomeGenBase.getBiome(aint[i1]);
			}

			return par1ArrayOfBiomeGenBase;
		}
	}

	/**
	 * checks given Chunk's Biomes against List of allowed ones
	 */
	public boolean areBiomesViable(int par1, int par2, int par3, List par4List) {
		IntCache.resetIntCache();
		int l = par1 - par3 >> 2;
			int i1 = par2 - par3 >> 2;
			int j1 = par1 + par3 >> 2;
		int k1 = par2 + par3 >> 2;
		int l1 = j1 - l + 1;
		int i2 = k1 - i1 + 1;
		int[] aint = this.genBiomes.getInts(l, i1, l1, i2);

		try {
			for (int j2 = 0; j2 < l1 * i2; ++j2) {
				BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[j2]);

				if (!par4List.contains(biomegenbase)) {
					return false;
				}
			}

			return true;
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
			crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
			crashreportcategory.addCrashSection("x", par1);
			crashreportcategory.addCrashSection("z", par2);
			crashreportcategory.addCrashSection("radius", par3);
			crashreportcategory.addCrashSection("allowed", par4List);
			throw new ReportedException(crashreport);
		}
	}

	public BlockPos findBiomePosition(int x, int z, int range, List biomes, Random random)
	{
		IntCache.resetIntCache();
		int l = x - range >> 2;
			int i1 = z - range >> 2;
			int j1 = x + range >> 2;
			int k1 = z + range >> 2;
			int l1 = j1 - l + 1;
			int i2 = k1 - i1 + 1;
			int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
			BlockPos blockpos = null;
			int j2 = 0;

			for (int k2 = 0; k2 < l1 * i2; ++k2) {
				int l2 = l + k2 % l1 << 2;
				int i3 = i1 + k2 / l1 << 2;
				BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[k2]);

				if (biomes.contains(biomegenbase) && (blockpos == null || random.nextInt(j2 + 1) == 0)) {
					blockpos = new BlockPos(l2, 0, i3);
					++j2;
				}
			}

			return blockpos;
	}

	/**
	 * Calls the WorldChunkManager's biomeCache.cleanupCache()
	 */
	public void cleanupCache() {
		this.biomeCache.cleanupCache();
	}

	public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original) {
		return original;
	}
}