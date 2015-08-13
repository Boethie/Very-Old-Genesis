package genesis.world.layer;

import genesis.common.GenesisBiomes;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerGenesisRiverMix extends GenLayerGenesis
{
	private GenLayer biomePatternGeneratorChain;
	private GenLayer riverPatternGeneratorChain;
	
	public GenLayerGenesisRiverMix(long seed, GenLayer biomeLayer, GenLayer riverLayer)
	{
		super(seed);
		this.biomePatternGeneratorChain = biomeLayer;
		this.riverPatternGeneratorChain = riverLayer;
	}
	
	/**
	 * Initialize layer's local worldGenSeed based on its own baseSeed and the world's global seed (passed in as an
	 * argument).
	 */
	@Override
	public void initWorldGenSeed(long seed)
	{
		this.biomePatternGeneratorChain.initWorldGenSeed(seed);
		this.riverPatternGeneratorChain.initWorldGenSeed(seed);
		super.initWorldGenSeed(seed);
	}
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] aint = this.biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
		int[] aint1 = this.riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
		int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
		
		for (int i1 = 0; i1 < areaWidth * areaHeight; ++i1)
		{
			if (!isBiomeOceanic(aint[i1]))
			{
				if (aint1[i1] == GenesisBiomes.river.biomeID)
				{
					aint2[i1] = aint1[i1] & 255;
				}
				else
				{
					aint2[i1] = aint[i1];
				}
			}
			else
			{
				aint2[i1] = aint[i1];
			}
		}
		return aint2;
	}
}
