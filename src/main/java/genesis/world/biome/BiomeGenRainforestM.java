package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenRainforestM extends BiomeGenRainforest
{
	public BiomeGenRainforestM(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(2).setStretch(2).setRarity(3).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.rhyolite.getDefaultState()).setWaterRequired(false).setRadius(2).setStretch(2).setRarity(8).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.dolerite.getDefaultState()).setWaterRequired(false).setRadius(2).setStretch(2).setRarity(8).setCountPerChunk(1));
		addDecoration(new WorldGenDebris().setCountPerChunk(28));
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
