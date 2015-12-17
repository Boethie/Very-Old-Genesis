package genesis.world.biome;

public class BiomeGenMetaForestM extends BiomeGenMetaForest
{
	public BiomeGenMetaForestM(int id)
	{
		super(id);
		setBiomeName("Metasequoia Forest M");
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
