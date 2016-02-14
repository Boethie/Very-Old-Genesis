package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenAuxForestM extends BiomeGenAuxForest
{
	public BiomeGenAuxForestM(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Forest M");
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
