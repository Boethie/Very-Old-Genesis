package genesis.world.layer;

import genesis.common.GenesisBiomes;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerGenesisShore extends GenLayerGenesis
{
	public GenLayerGenesisShore(long seed, GenLayer parent)
	{
		super(seed);
		this.parent = parent;
	}
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
		int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
		
		for (int y = 0; y < areaHeight; ++y)
		{
			for (int x = 0; x < areaWidth; ++x)
			{
				this.initChunkSeed(x + areaX, y + areaY);
				int biomeID = aint[x + 1 + (y + 1) * (areaWidth + 2)];
				int l1;
				int i2;
				int j2;
				int k2;
				
				if (
						biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.auxForestM)
						&& biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.auxForestHills)
						&& biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.woodlandsM)
						&& biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.woodlandsHills)
						&& biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.deciduousForestM)
						&& biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.deciduousForestHills)
						&& biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.rainforestM)
						&& biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.rainforestHills)
						&& biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.metaForestM)
						&& biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.metaForestHills))
				{
					if (!isBiomeOceanic(biomeID) && biomeID != BiomeGenBase.getIdForBiome(GenesisBiomes.swampRainForest))
					{
						l1 = aint[x + 1 + (y + 1 - 1) * (areaWidth + 2)];
						i2 = aint[x + 1 + 1 + (y + 1) * (areaWidth + 2)];
						j2 = aint[x + 1 - 1 + (y + 1) * (areaWidth + 2)];
						k2 = aint[x + 1 + (y + 1 + 1) * (areaWidth + 2)];
						
						if (
								(!isBiomeOceanic(l1) || l1 == BiomeGenBase.getIdForBiome(GenesisBiomes.swampRainForest))
								&& (!isBiomeOceanic(i2) || i2 == BiomeGenBase.getIdForBiome(GenesisBiomes.swampRainForest))
								&& (!isBiomeOceanic(j2) || j2 == BiomeGenBase.getIdForBiome(GenesisBiomes.swampRainForest))
								&& (!isBiomeOceanic(k2) || k2 == BiomeGenBase.getIdForBiome(GenesisBiomes.swampRainForest)))
						{
							aint1[x + y * areaWidth] = biomeID;
						}
						else
						{
							aint1[x + y * areaWidth] = BiomeGenBase.getIdForBiome(GenesisBiomes.genesisBeach);
						}
					}
					else
					{
						aint1[x + y * areaWidth] = biomeID;
					}
				}
				else
				{
					this.func_151632_a(aint, aint1, x, y, areaWidth, biomeID, BiomeGenBase.getIdForBiome(GenesisBiomes.limestoneBeach));
				}
			}
		}
		return aint1;
	}
	
	protected void func_151632_a(int[] aint, int[] aint1, int x, int y, int areaWidth, int biomeID, int p_151632_7_)
	{
		if (isBiomeOceanic(biomeID))
		{
			aint1[x + y * areaWidth] = biomeID;
		}
		else
		{
			int j1 = aint[x + 1 + (y + 1 - 1) * (areaWidth + 2)];
			int k1 = aint[x + 1 + 1 + (y + 1) * (areaWidth + 2)];
			int l1 = aint[x + 1 - 1 + (y + 1) * (areaWidth + 2)];
			int i2 = aint[x + 1 + (y + 1 + 1) * (areaWidth + 2)];

			if (
					(!isBiomeOceanic(j1) || j1 == BiomeGenBase.getIdForBiome(GenesisBiomes.swampRainForest)) 
					&& (!isBiomeOceanic(k1) || k1 == BiomeGenBase.getIdForBiome(GenesisBiomes.swampRainForest)) 
					&& (!isBiomeOceanic(l1) || l1 == BiomeGenBase.getIdForBiome(GenesisBiomes.swampRainForest)) 
					&& (!isBiomeOceanic(i2) || i2 == BiomeGenBase.getIdForBiome(GenesisBiomes.swampRainForest)))
			{
				aint1[x + y * areaWidth] = biomeID;
			}
			else
			{
				aint1[x + y * areaWidth] = p_151632_7_;
			}
		}
	}
}
