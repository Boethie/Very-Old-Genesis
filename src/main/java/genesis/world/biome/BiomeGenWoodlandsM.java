package genesis.world.biome;

import genesis.block.BlockMoss;
import genesis.block.BlockMoss.EnumSoil;
import genesis.combo.variant.EnumDebrisOther;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenBoulders;
import genesis.world.biome.decorate.WorldGenSplash;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenWoodlandsM extends BiomeGenWoodlands
{
	public BiomeGenWoodlandsM(BiomeGenBase.BiomeProperties properties)
	{
		super(properties);
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(WorldGenSplash.createHumusSplash(), 2.55F);
		addDecoration(new WorldGenBoulders(1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.4F);
		addDecoration(new WorldGenBoulders(GenesisBlocks.rhyolite.getDefaultState(), 1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.075F);
		addDecoration(new WorldGenBoulders(GenesisBlocks.dolerite.getDefaultState(), 1, 0, 0).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.075F);
		
		addPostDecoration(new WorldGenDebris(), 20);
		addPostDecoration(new WorldGenDebris(EnumDebrisOther.TYRANNOSAURUS_FEATHER).setPatchCount(1, 2), 0.1F);
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
