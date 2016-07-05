package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenBoulders;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenMetaForestM extends BiomeGenMetaForest
{
	public BiomeGenMetaForestM(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
		
		super.addDecorations();
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenBoulders(1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.25F, 0.75F)), 0.153F);
		addDecoration(new WorldGenBoulders(GenesisBlocks.rhyolite.getDefaultState(), 1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.25F, 0.75F)), 0.075F);
		addDecoration(new WorldGenBoulders(GenesisBlocks.dolerite.getDefaultState(), 1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.25F, 0.75F)), 0.075F);
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
