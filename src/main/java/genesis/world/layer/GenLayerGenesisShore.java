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
	
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
        
        for (int i1 = 0; i1 < areaHeight; ++i1)
        {
            for (int j1 = 0; j1 < areaWidth; ++j1)
            {
                this.initChunkSeed((long)(j1 + areaX), (long)(i1 + areaY));
                int k1 = aint[j1 + 1 + (i1 + 1) * (areaWidth + 2)];
                int l1;
                int i2;
                int j2;
                int k2;
                
                if (
                		k1 != GenesisBiomes.auxForest.biomeID 
                		&& k1 != GenesisBiomes.auxForestM.biomeID 
                		&& k1 != GenesisBiomes.auxForestEdge.biomeID 
                		&& k1 != GenesisBiomes.auxForestEdgeM.biomeID 
                		&& k1 != GenesisBiomes.auxForestHills.biomeID
                		&& k1 != GenesisBiomes.rainforest.biomeID
                		&& k1 != GenesisBiomes.rainforestM.biomeID
                		&& k1 != GenesisBiomes.rainforestEdge.biomeID
                		&& k1 != GenesisBiomes.rainforestEdgeM.biomeID
                		&& k1 != GenesisBiomes.rainforestHills.biomeID
                		&& k1 != GenesisBiomes.archaeopterisForest.biomeID
                		&& k1 != GenesisBiomes.archaeopterisForestHills.biomeID)
                {
                	if (!isBiomeOceanic(k1) && k1 != GenesisBiomes.swampRainForest.biomeID /*&& k1 != GenesisBiomes.river.biomeID */)
                    {
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (areaWidth + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (areaWidth + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (areaWidth + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (areaWidth + 2)];
                        
                        if (
                        		!isBiomeOceanic(l1) 
                        		&& !isBiomeOceanic(i2) 
                        		&& !isBiomeOceanic(j2) 
                        		&& !isBiomeOceanic(k2))
                        {
                            aint1[j1 + i1 * areaWidth] = k1;
                        }
                        else
                        {
                            aint1[j1 + i1 * areaWidth] = GenesisBiomes.genesisBeach.biomeID;
                        }
                    }
                    else
                    {
                        aint1[j1 + i1 * areaWidth] = k1;
                    }
                }
                else
                {
                    this.func_151632_a(aint, aint1, j1, i1, areaWidth, k1, GenesisBiomes.limestoneBeach.biomeID);
                }
            }
        }
        return aint1;
    }
	
	private void func_151632_a(int[] p_151632_1_, int[] p_151632_2_, int p_151632_3_, int p_151632_4_, int p_151632_5_, int p_151632_6_, int p_151632_7_)
    {
        if (isBiomeOceanic(p_151632_6_))
        {
            p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
        }
        else
        {
            int j1 = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 - 1) * (p_151632_5_ + 2)];
            int k1 = p_151632_1_[p_151632_3_ + 1 + 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            int l1 = p_151632_1_[p_151632_3_ + 1 - 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            int i2 = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 + 1) * (p_151632_5_ + 2)];

            if (!isBiomeOceanic(j1) && !isBiomeOceanic(k1) && !isBiomeOceanic(l1) && !isBiomeOceanic(i2))
            {
                p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
            }
            else
            {
                p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_7_;
            }
        }
    }
}
