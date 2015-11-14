package genesis.world.biome;

import net.minecraft.util.Vec3;

public class BiomeGenRainforestEdgeM extends BiomeGenRainforestEdge
{
	public BiomeGenRainforestEdgeM(int id)
	{
		super(id);
		setBiomeName("Rainforest Edge M");
		setHeight(0.4F, 0.6F);
	}
	
	@Override
	public float getFogDensity(int x, int y, int z)
	{
		return 0.75F;
	}
	
	@Override
	public Vec3 getFogColor()
	{
		float red = 0.533333333F;
		float green = 0.647058824F;
		float blue = 0.474509804F;
		
		return new Vec3(red, green, blue);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
