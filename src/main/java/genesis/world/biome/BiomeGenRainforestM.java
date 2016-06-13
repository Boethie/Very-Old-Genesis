package genesis.world.biome;

import genesis.block.BlockMoss;
import genesis.block.BlockMoss.EnumSoil;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenSplash;
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
		addDecoration(new WorldGenSplash((s, w, p) -> s.getBlock() == GenesisBlocks.moss, GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.DIRT, EnumSoil.HUMUS)).setPatchRadius(6), 2.1F);
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.4F);
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.rhyolite.getDefaultState()).setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.075F);
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.dolerite.getDefaultState()).setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.075F);
		addDecoration(new WorldGenDebris(), 28);
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
