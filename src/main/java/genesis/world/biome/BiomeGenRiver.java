package genesis.world.biome;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenRiver extends BiomeGenBaseGensis
{

	public BiomeGenRiver(int id)
	{
		super(id);
		this.biomeName = "River";
		this.setHeight(BiomeGenBase.height_ShallowWaters);
	}

}
