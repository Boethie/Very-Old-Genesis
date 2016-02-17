package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenMetaForestM extends BiomeGenMetaForest
{
	public BiomeGenMetaForestM(int id)
	{
		super(id);
		setBiomeName("Metasequoia Forest M");
		setHeight(0.45F, 0.45F);
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(4).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(9).addBlocks(GenesisBlocks.rhyolite.getDefaultState()).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(9).addBlocks(GenesisBlocks.dolerite.getDefaultState()).setCountPerChunk(1));
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
