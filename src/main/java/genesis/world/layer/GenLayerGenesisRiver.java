package genesis.world.layer;

import genesis.common.GenesisBiomes;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerGenesisRiver extends GenLayerGenesis
{
	public GenLayerGenesisRiver(long seed, GenLayer parent)
	{
		super(seed);
		super.parent = parent;
	}
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int i1 = areaX - 1;
		int j1 = areaY - 1;
		int k1 = areaWidth + 2;
		int l1 = areaHeight + 2;
		int[] aint = this.parent.getInts(i1, j1, k1, l1);
		int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
		
		for (int i2 = 0; i2 < areaHeight; ++i2)
		{
			for (int j2 = 0; j2 < areaWidth; ++j2)
			{
				int k2 = func_151630_c(aint[j2 + (i2 + 1) * k1]);
				int l2 = func_151630_c(aint[j2 + 2 + (i2 + 1) * k1]);
				int i3 = func_151630_c(aint[j2 + 1 + (i2) * k1]);
				int j3 = func_151630_c(aint[j2 + 1 + (i2 + 2) * k1]);
				int k3 = func_151630_c(aint[j2 + 1 + (i2 + 1) * k1]);
				
				if (k3 == k2 && k3 == i3 && k3 == l2 && k3 == j3)
				{
					aint1[j2 + i2 * areaWidth] = -1;
				}
				else
				{
					aint1[j2 + i2 * areaWidth] = GenesisBiomes.river.biomeID;
				}
			}
		}
		
		return aint1;
	}
	
	protected int func_151630_c(int p_151630_1_)
	{
		return p_151630_1_ >= 2 ? 2 + (p_151630_1_ & 1) : p_151630_1_;
	}
}
