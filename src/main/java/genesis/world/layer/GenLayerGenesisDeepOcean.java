package genesis.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import genesis.common.GenesisBiomes;

public class GenLayerGenesisDeepOcean extends GenLayerGenesis
{

	public GenLayerGenesisDeepOcean(long p_i45472_1_, GenLayer p_i45472_3_)
	{
		super(p_i45472_1_);
		this.parent = p_i45472_3_;
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
				int k2 = aint[j2 + 1 + (i2 + 1 - 1) * (areaWidth + 2)];
				int l2 = aint[j2 + 1 + 1 + (i2 + 1) * (areaWidth + 2)];
				int i3 = aint[j2 + 1 - 1 + (i2 + 1) * (areaWidth + 2)];
				int j3 = aint[j2 + 1 + (i2 + 1 + 1) * (areaWidth + 2)];
				int k3 = aint[j2 + 1 + (i2 + 1) * k1];
				int l3 = 0;

				if (k2 == 0)
				{
					++l3;
				}

				if (l2 == 0)
				{
					++l3;
				}

				if (i3 == 0)
				{
					++l3;
				}

				if (j3 == 0)
				{
					++l3;
				}

				if (k3 == 0 && l3 > 3)
				{
					// TODO: Change this to a genesis deep ocean.
					aint1[j2 + i2 * areaWidth] = GenesisBiomes.shallowOcean.biomeID;
				}
				else
				{
					aint1[j2 + i2 * areaWidth] = k3;
				}
			}
		}

		return aint1;
	}

}
