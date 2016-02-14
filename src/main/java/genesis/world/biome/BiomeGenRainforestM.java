package genesis.world.biome;

import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenRainforestM extends BiomeGenRainforest
{
	public BiomeGenRainforestM(int id)
	{
		super(id);
		setBiomeName("Rainforest M");
		setHeight(0.4F, 0.7F);
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(3).setCountPerChunk(1));
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
