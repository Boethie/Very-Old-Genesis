package genesis.world.layer;

import genesis.common.GenesisBiomes;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerGenesisBiomeEdge extends GenLayerGenesis
{
	public GenLayerGenesisBiomeEdge(long seed, GenLayer parent)
	{
		super(seed);
		this.parent = parent;
	}
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
		int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
		
		for (int i1 = 0; i1 < areaHeight; ++i1)
		{
			for (int j1 = 0; j1 < areaWidth; ++j1)
			{
				this.initChunkSeed(j1 + areaX, i1 + areaY);
				int k1 = aint[j1 + 1 + (i1 + 1) * (areaWidth + 2)];
				
				if (!this.replaceBiomeEdge(aint, aint1, j1, i1, areaWidth, k1, BiomeGenBase.getIdForBiome(GenesisBiomes.deepOcean), BiomeGenBase.getIdForBiome(GenesisBiomes.ocean)))
				{
					aint1[j1 + i1 * areaWidth] = k1;
				}
			}
		}
		
		return aint1;
	}
	
	protected boolean replaceBiomeEdgeIfNecessary(int[] group1, int[] group2, int coordX, int coordY , int zoom, int biomeId1, int biomeId2, int defaultId)
	{
		if (!biomesEqualOrMesaPlateau(biomeId1, biomeId2))
		{
			return false;
		}
		
		int k1 = group1[coordX + 1 + (coordY + 1 - 1) * (zoom + 2)];
		int l1 = group1[coordX + 1 + 1 + (coordY + 1) * (zoom + 2)];
		int i2 = group1[coordX + 1 - 1 + (coordY + 1) * (zoom + 2)];
		int j2 = group1[coordX + 1 + (coordY + 1 + 1) * (zoom + 2)];
		
		if (this.canBiomesBeNeighbors(k1, biomeId2) && this.canBiomesBeNeighbors(l1, biomeId2)
				&& this.canBiomesBeNeighbors(i2, biomeId2) && this.canBiomesBeNeighbors(j2, biomeId2))
		{
			group2[coordX + coordY * zoom] = biomeId1;
		}
		else
		{
			group2[coordX + coordY * zoom] = defaultId;
		}
		
		return true;
	}
	
	protected boolean replaceBiomeEdge(int[] p_151635_1_, int[] p_151635_2_, int p_151635_3_, int p_151635_4_, int p_151635_5_, int p_151635_6_, int p_151635_7_, int p_151635_8_)
	{
		if (p_151635_6_ != p_151635_7_)
		{
			return false;
		}
		
		int k1 = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 - 1) * (p_151635_5_ + 2)];
		int l1 = p_151635_1_[p_151635_3_ + 1 + 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
		int i2 = p_151635_1_[p_151635_3_ + 1 - 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
		int j2 = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 + 1) * (p_151635_5_ + 2)];
		
		if (biomesEqualOrMesaPlateau(k1, p_151635_7_) && biomesEqualOrMesaPlateau(l1, p_151635_7_) && biomesEqualOrMesaPlateau(i2, p_151635_7_)
				&& biomesEqualOrMesaPlateau(j2, p_151635_7_))
		{
			p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_6_;
		}
		else
		{
			p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_8_;
		}
		
		return true;
	}
	
	protected boolean canBiomesBeNeighbors(int p_151634_1_, int p_151634_2_)
	{
		if (biomesEqualOrMesaPlateau(p_151634_1_, p_151634_2_))
		{
			return true;
		}
		
		BiomeGenBase biomegenbase = BiomeGenBase.getBiome(p_151634_1_);
		BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(p_151634_2_);
		
		if (biomegenbase != null && biomegenbase1 != null)
		{
			BiomeGenBase.TempCategory tempcategory = biomegenbase.getTempCategory();
			BiomeGenBase.TempCategory tempcategory1 = biomegenbase1.getTempCategory();
			return tempcategory == tempcategory1 || tempcategory == BiomeGenBase.TempCategory.MEDIUM
					|| tempcategory1 == BiomeGenBase.TempCategory.MEDIUM;
		}
		
		return false;
	}
}
