package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenBoulders;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenSplash;
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
		addDecoration(WorldGenSplash.createHumusSplash(), 1.95F);
		addDecoration(new WorldGenBoulders(1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.153F);
		addDecoration(new WorldGenBoulders(GenesisBlocks.rhyolite.getDefaultState(), 1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.075F);
		addDecoration(new WorldGenBoulders(GenesisBlocks.dolerite.getDefaultState(), 1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.075F);
		
		addPostDecoration(new WorldGenDebris(), 7);
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
