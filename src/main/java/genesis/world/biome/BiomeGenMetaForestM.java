package genesis.world.biome;

import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenMetaForestM extends BiomeGenMetaForest
{
	public BiomeGenMetaForestM(int id)
	{
		super(id);
		setBiomeName("Metasequoia Forest M");
		setHeight(0.4F, 0.7F);
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(4).setCountPerChunk(1));
	}
	
	@Override
	public float getFogDensity()
	{
		return 0.75F;
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
