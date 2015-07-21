package genesis.world.biome;

import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenRiver extends BiomeGenBaseGenesis
{
	public BiomeGenRiver(int id)
	{
		super(id);
		this.biomeName = "River";
		this.setHeight(BiomeGenBase.height_ShallowWaters);
	}
	
	@Override
	public Vec3 getSkyColor()
	{
		return new Vec3(0.294117647D, 0.474509804D, 0.501960784D);
	}
}
