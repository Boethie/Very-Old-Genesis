package genesis.world.biome;

import net.minecraft.util.math.Vec3d;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;

public class BiomeGenRedBeach extends BiomeGenBeachGenesis
{
	public BiomeGenRedBeach(int id)
	{
		super(id);
		setBiomeName("Red Beach");
		setTemperatureRainfall(2.0F, 0.0F);
		setDisableRain();
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
	}
	
	@Override
	public Vec3d getFogColor()
	{
		float red = 0.917647059F;
		float green = 0.650980392F;
		float blue = 0.309803922F;
		
		return new Vec3d(red, green, blue);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
