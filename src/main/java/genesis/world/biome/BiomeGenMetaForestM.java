package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenMetaForestM extends BiomeGenMetaForest
{
	public BiomeGenMetaForestM(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(2).setStretch(2).setRarity(6).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.rhyolite.getDefaultState()).setWaterRequired(false).setRadius(2).setStretch(2).setRarity(10).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.dolerite.getDefaultState()).setWaterRequired(false).setRadius(2).setStretch(2).setRarity(10).setCountPerChunk(1));
		addDecoration(new WorldGenDebris().setCountPerChunk(7));
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
