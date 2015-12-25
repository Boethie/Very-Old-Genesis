package genesis.world.biome;

public class BiomeGenRainforestM extends BiomeGenRainforest
{
	public BiomeGenRainforestM(int id)
	{
		super(id);
		setBiomeName("Rainforest M");
		setHeight(0.4F, 0.7F);
	}
	
	@Override
	public float getFogDensity()
	{
		return 0.25F;
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.15F;
	}
}
