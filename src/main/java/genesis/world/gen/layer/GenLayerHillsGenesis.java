package genesis.world.gen.layer;

import genesis.common.GenesisBiomes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerHillsGenesis extends GenLayer 
{
	private static final Logger logger = LogManager.getLogger();
	private GenLayer layer;
	private static final String __OBFID = "CL_00000563";

	public GenLayerHillsGenesis(long seed, GenLayer parent, GenLayer layer)
	{
		super(seed);
		this.parent = parent;
		this.layer = layer;
	}

	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
		int[] aint1 = this.layer.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
		int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);

		for (int i1 = 0; i1 < areaHeight; ++i1)
		{
			for (int j1 = 0; j1 < areaWidth; ++j1)
			{
				this.initChunkSeed((long)(j1 + areaX), (long)(i1 + areaY));
				int k1 = aint[j1 + 1 + (i1 + 1) * (areaWidth + 2)];
				int l1 = aint1[j1 + 1 + (i1 + 1) * (areaWidth + 2)];
				boolean flag = (l1 - 2) % 29 == 0;

				if (k1 > 255)
				{
					logger.debug("old! " + k1);
				}

				if (k1 != 0 && l1 >= 2 && (l1 - 2) % 29 == 1 && k1 < 128)
				{
					if (BiomeGenBase.getBiome(k1 + 128) != null)
					{
						aint2[j1 + i1 * areaWidth] = k1 + 128;
					}
					else
					{
						aint2[j1 + i1 * areaWidth] = k1;
					}
				}
				else if (this.nextInt(3) != 0 && !flag)
				{
					aint2[j1 + i1 * areaWidth] = k1;
				}
				else
				{
					int i2 = k1;
					int j2;

					/* Add hills versions of biomes here in the form:

                    if (k1 == BiomeGenBase.desert.biomeID)
                    {
                        i2 = BiomeGenBase.desertHills.biomeID;
                    }
					 */

					//This generates random "biome" islands in the ocean.

					if (k1 == BiomeGenBase.deepOcean.biomeID && this.nextInt(3) == 0)
					{
						j2 = this.nextInt(2);

						if (j2 == 0)
						{
							i2 = GenesisBiomes.araucarioxylonForestId;
						}
						else
						{
							i2 = GenesisBiomes.rainforestEdgeId;
						}
					}

					if (flag && i2 != k1)
					{
						if (BiomeGenBase.getBiome(i2 + 128) != null)
						{
							i2 += 128;
						}
						else
						{
							i2 = k1;
						}
					}

					if (i2 == k1)
					{
						aint2[j1 + i1 * areaWidth] = k1;
					}
					else
					{
						j2 = aint[j1 + 1 + (i1 + 1 - 1) * (areaWidth + 2)];
						int k2 = aint[j1 + 1 + 1 + (i1 + 1) * (areaWidth + 2)];
						int l2 = aint[j1 + 1 - 1 + (i1 + 1) * (areaWidth + 2)];
						int i3 = aint[j1 + 1 + (i1 + 1 + 1) * (areaWidth + 2)];
						int j3 = 0;

						if (biomesEqualOrMesaPlateau(j2, k1))
						{
							++j3;
						}

						if (biomesEqualOrMesaPlateau(k2, k1))
						{
							++j3;
						}

						if (biomesEqualOrMesaPlateau(l2, k1))
						{
							++j3;
						}

						if (biomesEqualOrMesaPlateau(i3, k1))
						{
							++j3;
						}

						if (j3 >= 3)
						{
							aint2[j1 + i1 * areaWidth] = i2;
						}
						else
						{
							aint2[j1 + i1 * areaWidth] = k1;
						}
					}
				}
			}
		}
		return aint2;
	}
}
