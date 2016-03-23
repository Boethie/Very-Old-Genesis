package genesis.world.biome;

import genesis.combo.variant.EnumDebrisOther;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenRockBoulders;

public class BiomeGenAuxForestM extends BiomeGenAuxForest
{
	public BiomeGenAuxForestM(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Forest M");
		setHeight(2.2F, 0.4F);
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).setRarity(2).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).addBlocks(GenesisBlocks.rhyolite.getDefaultState()).setRarity(7).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(3).addBlocks(GenesisBlocks.dolerite.getDefaultState()).setRarity(7).setCountPerChunk(1));
		addDecoration(new WorldGenDebris().addAdditional(GenesisBlocks.debris.getBlockState(EnumDebrisOther.COELOPHYSIS_FEATHER)).setCountPerChunk(20));
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
