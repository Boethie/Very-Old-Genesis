package genesis.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public abstract class GenLayerGenesis extends GenLayer
{
	public GenLayerGenesis(long seed)
	{
		super(seed);
	}
	
	public static GenLayer[] initializeAllBiomeGenerators(long seed)
	{
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
		
		//ChunkProviderSettings chunkprovidersettings = null;
		int biomesize = 4;
		
		// j = getModdedBiomeSize(p_180781_2_, j);
		
		GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer2, 0);
		GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
		
		GenLayer ret = new GenLayerGenesisBiome(200L, genlayer2);
		ret = GenLayerZoom.magnify(1000L, ret, 2);
		ret = new GenLayerGenesisBiomeEdge(1000L, ret);
		
		GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
		GenLayerGenesisHills genlayergenesishills = new GenLayerGenesisHills(1000L, ret, genlayer1);
		genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
		genlayer = GenLayerZoom.magnify(1000L, genlayer, biomesize);
		GenLayerGenesisRiver genlayerriver = new GenLayerGenesisRiver(1L, genlayer);
		GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
		GenLayer object = new GenLayerRareBiome(1001L, genlayergenesishills);
		
		for (int l = 0; l < biomesize; ++l)
		{
			object = new GenLayerZoom(1000 + l, object);
			
			if (l == 0)
			{
				object = new GenLayerAddIsland(3L, object);
			}
			
			if (l == 1 || biomesize == 1)
			{
				object = new GenLayerGenesisShore(1000L, object);
			}
		}
		
		GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, object);
		GenLayerGenesisRiverMix genlayerrivermix = new GenLayerGenesisRiverMix(100L, genlayersmooth1, genlayersmooth);
		GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
		GenLayerReplaceOcean genlayerrivermixreplaced = new GenLayerReplaceOcean(100L, genlayerrivermix);
		GenLayerReplaceOcean genlayervoronoizoomreplaced = new GenLayerReplaceOcean(100L, genlayervoronoizoom);
		genlayerrivermix.initWorldGenSeed(seed);
		genlayervoronoizoom.initWorldGenSeed(seed);
		return new GenLayer[]{ genlayerrivermixreplaced, genlayervoronoizoomreplaced, genlayerrivermixreplaced };
	}
}
