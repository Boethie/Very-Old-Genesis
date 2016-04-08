package genesis.world.biome;

import genesis.combo.variant.EnumDebrisOther;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenDeciduousForestM extends BiomeGenDeciduousForest
{
	public BiomeGenDeciduousForestM(BiomeProperties properties)
	{
		super(properties);
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(1).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.rhyolite.getDefaultState()).setWaterRequired(false).setMaxHeight(3).setRarity(7).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.dolerite.getDefaultState()).setWaterRequired(false).setMaxHeight(3).setRarity(7).setCountPerChunk(1));
		addDecoration(new WorldGenDebris().setCountPerChunk(20));
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
