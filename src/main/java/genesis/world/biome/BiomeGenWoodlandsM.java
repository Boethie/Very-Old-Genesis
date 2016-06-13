package genesis.world.biome;

import genesis.block.BlockMoss;
import genesis.block.BlockMoss.EnumSoil;
import genesis.combo.variant.EnumDebrisOther;
import genesis.common.GenesisBlocks;
import genesis.util.random.f.FloatRange;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenRockBoulders;
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
		addDecoration(new WorldGenSplash((s, w, p) -> s.getBlock() == GenesisBlocks.moss, GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.SOIL, EnumSoil.HUMUS)).setDryRadius(-1).setPatchRadius(11), 2.55F);
		addDecoration(new WorldGenDebris(), 20);
		addDecoration(new WorldGenDebris(EnumDebrisOther.TYRANNOSAURUS_FEATHER).setPatchCount(1, 2), 0.1F);
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.4F);
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.rhyolite.getDefaultState()).setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.075F);
		addDecoration(new WorldGenRockBoulders(GenesisBlocks.dolerite.getDefaultState()).setWaterRequired(false).setRadius(FloatRange.create(0.75F, 1.5F), FloatRange.create(0.5F, 1)), 0.075F);
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
