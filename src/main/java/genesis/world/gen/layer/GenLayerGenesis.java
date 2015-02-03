package genesis.world.gen.layer;

import genesis.world.WorldProviderGenesis;
import genesis.world.WorldTypeGenesis;

import java.util.concurrent.Callable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.GenLayerDeepOcean;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;

public class GenLayerGenesis extends GenLayer {
	/**
	 * parent GenLayer that was provided via the constructor
	 */
	protected GenLayer parent;
	/**
	 * base seed to the LCG prng provided via the constructor
	 */
	protected long baseSeed;
	/**
	 * seed from World#getWorldSeed that is used in the LCG prng
	 */
	private long worldGenSeed;
	/**
	 * final part of the LCG prng that uses the chunk X, Z coords along with the other two seeds to generate
	 * pseudorandom numbers
	 */
	private long chunkSeed;

	//TODO Add configurable biome size
	public static byte biomeSize = 4;

	public GenLayerGenesis(long seed) {
		super(seed);
		this.baseSeed = seed;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += seed;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += seed;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += seed;
	}

	/**
	 * the first array item is a linked list of the bioms, the second is the zoom function, the third is the same as the
	 * first.
	 */
	public static GenLayer[] initializeAllBiomeGenerators(long seed, WorldType worldType) {
		GenLayerIsland genlayerisland = new GenLayerIsland(1L);
		GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
		GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayerfuzzyzoom);
		GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
		genlayeraddisland = new GenLayerAddIsland(2L, genlayerzoom);
		genlayeraddisland = new GenLayerAddIsland(50L, genlayeraddisland);
		genlayeraddisland = new GenLayerAddIsland(70L, genlayeraddisland);
		GenLayerRemoveTooMuchOcean genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland);
		GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
		genlayeraddisland = new GenLayerAddIsland(3L, genlayeraddsnow);
		GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland, GenLayerEdge.Mode.COOL_WARM);
		genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
		genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
		genlayerzoom = new GenLayerZoom(2002L, genlayeredge);
		genlayerzoom = new GenLayerZoom(2003L, genlayerzoom);
		genlayeraddisland = new GenLayerAddIsland(4L, genlayerzoom);
		
		GenLayerGenesisDeepOcean genlayerdeepocean = new GenLayerGenesisDeepOcean(4L, genlayeraddisland);
		GenLayer genlayer2 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
		byte b0 = biomeSize; 

		GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer2, 0);
		GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
		GenLayer mainBiomeLayer = new GenLayerBiomeGenesis(200L, genlayer2);//worldType.getBiomeLayer(seed, genlayer2, "");//new GenLayerBiomeEdgeGenesis(1000L, GenLayerZoom.magnify(1000L, new GenLayerBiomeGenesis(200L, genlayer2), 2));        

		GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
		GenLayerHillsGenesis genlayerhillsgenesis = new GenLayerHillsGenesis(1000L, (GenLayer) mainBiomeLayer, genlayer1);
		genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
		genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
		GenLayerRiverGenesis genlayerriver = new GenLayerRiverGenesis(1L, genlayer);
		GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
		Object object = new GenLayerRareBiome(1001L, genlayerhillsgenesis);

		for (int j = 0; j < b0; ++j) {
			object = new GenLayerZoom((long) (1000 + j), (GenLayer) object);

			if (j == 0) {
				object = new GenLayerAddIsland(3L, (GenLayer) object);
			}

			if (j == 1) {
				object = new GenLayerShoreGenesis(1000L, (GenLayer) object);
			}
		}

		GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer) object);
		GenLayerRiverMixGenesis genlayerrivermix = new GenLayerRiverMixGenesis(100L, genlayersmooth1, genlayersmooth);
		GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
		genlayerrivermix.initWorldGenSeed(seed);
		genlayervoronoizoom.initWorldGenSeed(seed);
		return new GenLayer[]{genlayerrivermix, genlayervoronoizoom, genlayerrivermix};
	}

	//Not Used

//    /**
//     * returns true if the biomeIDs are equal, or returns the result of the comparison as per BiomeGenBase.isEqualTo
//     */
//    protected static boolean biomesEqualOrMesaPlateau(int biomeIDA, int biomeIDB) {
//        
//        if (biomeIDA == biomeIDB) {
//            return true;
//        } else if (biomeIDA != BiomeGenBase.mesaPlateau_F.biomeID && biomeIDA != BiomeGenBase.mesaPlateau.biomeID) {
//            try {
//                
//                final BiomeGenBase biomeA = BiomeGenBase.getBiome(biomeIDA);
//                final BiomeGenBase biomeB = BiomeGenBase.getBiome(biomeIDB);
//                
//                return biomeA != null && biomeB != null ? biomeA.isEqualTo(biomeB) : false;
//                
//            } catch (Throwable throwable) {
//                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Comparing biomes");
//                CrashReportCategory crashreportcategory = crashreport.makeCategory("Biomes being compared");
//                crashreportcategory.addCrashSection("Biome A ID", biomeIDA);
//                crashreportcategory.addCrashSection("Biome B ID", biomeIDB);
//                crashreportcategory.addCrashSectionCallable("Biome A", new Callable() {
//                    private static final String __OBFID = "CL_00000560";
//
//                    public String call() {
//                        return String.valueOf(biomeA);
//                    }
//                });
//                crashreportcategory.addCrashSectionCallable("Biome B", new Callable() {
//                    private static final String __OBFID = "CL_00000561";
//
//                    public String call() {
//                        return String.valueOf(biomeB);
//                    }
//                });
//                throw new ReportedException(crashreport);
//            }
//        } else {
//            return biomeIDB == BiomeGenBase.mesaPlateau_F.biomeID || biomeIDB == BiomeGenBase.mesaPlateau.biomeID;
//        }
//    }

	//Not used

	//    /**
	//     * returns true if the biomeId is one of the various ocean biomes.
	//     */
	//    protected static boolean isBiomeOceanic(int p_151618_0_) {
	//        return p_151618_0_ == BiomeGenBase.ocean.biomeID || p_151618_0_ == BiomeGenBase.deepOcean.biomeID || p_151618_0_ == BiomeGenBase.frozenOcean.biomeID;
	//    }

	/**
	 * Initialize layer's local worldGenSeed based on its own baseSeed and the world's global seed (passed in as an
	 * argument).
	 */
	public void initWorldGenSeed(long seed) {
		this.worldGenSeed = seed;

		if (this.parent != null) {
			this.parent.initWorldGenSeed(seed);
		}

		this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldGenSeed += this.baseSeed;
		this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldGenSeed += this.baseSeed;
		this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldGenSeed += this.baseSeed;
	}

	/**
	 * Initialize layer's current chunkSeed based on the local worldGenSeed and the (x,z) chunk coordinates.
	 */
	public void initChunkSeed(long x, long z) {
		this.chunkSeed = this.worldGenSeed;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += x;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += z;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += x;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += z;
	}

	/**
	 * returns a LCG pseudo random number from [0, x). Args: int x
	 */
	protected int nextInt(int max) {
		int j = (int) ((this.chunkSeed >> 24) % (long) max);

		if (j < 0) {
			j += max;
		}

		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += this.worldGenSeed;
		return j;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
	 * amounts, or biomeList[] indices based on the particular GenLayer subclass.
	 */
	public int[] getInts(int var1, int var2, int var3, int var4) {
		return null;
	}

	/**
	 * selects a random integer from a set of provided integers
	 */
	protected int selectRandom(int... ints) {
		return ints[this.nextInt(ints.length)];
	}

	/**
	 * returns the most frequently occurring number of the set, or a random number from those provided
	 */
	protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_) {
		return p_151617_2_ == p_151617_3_ && p_151617_3_ == p_151617_4_ ? p_151617_2_ : (p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_3_ ? p_151617_1_ : (p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_3_ && p_151617_1_ == p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_2_ && p_151617_3_ != p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_3_ && p_151617_2_ != p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_4_ && p_151617_2_ != p_151617_3_ ? p_151617_1_ : (p_151617_2_ == p_151617_3_ && p_151617_1_ != p_151617_4_ ? p_151617_2_ : (p_151617_2_ == p_151617_4_ && p_151617_1_ != p_151617_3_ ? p_151617_2_ : (p_151617_3_ == p_151617_4_ && p_151617_1_ != p_151617_2_ ? p_151617_3_ : this.selectRandom(p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_))))))))));
	}

	//    public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer) {
	//        GenLayer ret = new GenLayerBiomeGenesis(200L, parentLayer);
	//        ret = GenLayerZoom.magnify(1000L, ret, 2);
	//        ret = new GenLayerBiomeEdgeGenesis(1000L, ret);
	//        return ret;
	//    }
}