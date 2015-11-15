package genesis.world.biome;

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
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
