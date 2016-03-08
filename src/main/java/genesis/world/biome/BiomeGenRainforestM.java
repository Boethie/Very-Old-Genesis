package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenRainforestM extends BiomeGenRainforest
{
	public BiomeGenRainforestM(int id)
	{
		super(id);
		setBiomeName("Rainforest M");
		setHeight(2.2F, 0.4F);
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(4).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(9).addBlocks(GenesisBlocks.rhyolite.getDefaultState()).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(9).addBlocks(GenesisBlocks.dolerite.getDefaultState()).setCountPerChunk(1));
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
