package genesis.world.biome;

public class BiomeGenAuxForestM extends BiomeGenAuxForest
{
	public BiomeGenAuxForestM(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Forest M");
		setHeight(0.4F, 0.7F);
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
