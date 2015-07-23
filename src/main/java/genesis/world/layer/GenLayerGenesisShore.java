package genesis.world.layer;

import genesis.common.GenesisBiomes;
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
	public int[] getInts(int par1, int par2, int par3, int par4)
	{
		int[] var5 = this.parent.getInts(par1 - 1, par2 - 1, par3 + 2, par4 + 2);
		int[] var6 = IntCache.getIntCache(par3 * par4);
		
		for (int var7 = 0; var7 < par4; ++var7)
		{
			for (int var8 = 0; var8 < par3; ++var8)
			{
				this.initChunkSeed(var8 + par1, var7 + par2);
				int var9 = var5[var8 + 1 + (var7 + 1) * (par3 + 2)];
				
				if (var9 == GenesisBiomes.rainforest.biomeID)
				{
					var6 = getIntsForBiome(var5, var6, var8, var7, var9, par3, GenesisBiomes.rainforest.biomeID, GenesisBiomes.rainforestEdge.biomeID);
				}
				else if (var9 == GenesisBiomes.rainforestM.biomeID)
				{
					var6 = getIntsForBiome(var5, var6, var8, var7, var9, par3, GenesisBiomes.rainforestM.biomeID, GenesisBiomes.rainforestEdgeM.biomeID);
				}
				else if (var9 == GenesisBiomes.auxForest.biomeID)
				{
					var6 = getIntsForBiome(var5, var6, var8, var7, var9, par3, GenesisBiomes.auxForest.biomeID, GenesisBiomes.auxForestEdge.biomeID);
				}
				else if (var9 == GenesisBiomes.auxForestM.biomeID)
				{
					var6 = getIntsForBiome(var5, var6, var8, var7, var9, par3, GenesisBiomes.auxForestM.biomeID, GenesisBiomes.auxForestEdgeM.biomeID);
				}
				else if (var9 == GenesisBiomes.shallowOcean.biomeID)
				{
					var6 = getIntsForBiome(var5, var6, var8, var7, var9, par3, GenesisBiomes.shallowOcean.biomeID, GenesisBiomes.limestoneBeach.biomeID);
				}
				else
				{
					var6[var8 + var7 * par3] = var9;
				}
			}
		}
		return var6;
	}
	
	private int[] getIntsForBiome(int[] var5, int[] var6, int var8, int var7, int var9, int par3, int biomeId1, int biomeId2)
	{
		int var10 = var5[var8 + 1 + (var7 + 1 - 1) * (par3 + 2)];
		int var11 = var5[var8 + 1 + 1 + (var7 + 1) * (par3 + 2)];
		int var12 = var5[var8 + 1 - 1 + (var7 + 1) * (par3 + 2)];
		int var13 = var5[var8 + 1 + (var7 + 1 + 1) * (par3 + 2)];
		
		if (var10 == biomeId1)
		{
			if (var11 == biomeId1)
			{
				if (var12 == biomeId1)
				{
					if (var13 == biomeId1)
					{
						var6[var8 + var7 * par3] = var9;
					}
					else
					{
						var6[var8 + var7 * par3] = biomeId2;
					}
				}
				else
				{
					var6[var8 + var7 * par3] = biomeId2;
				}
			}
			else
			{
				var6[var8 + var7 * par3] = biomeId2;
			}
		}
		else
		{
			var6[var8 + var7 * par3] = biomeId2;
		}
		
		return var6;
	}
}
