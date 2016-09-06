package genesis.world.biome;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.gen.feature.WorldGenTreeBjuvia;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

public class BiomeRedBeach extends BiomeBeachGenesis
{
	public BiomeRedBeach(Biome.BiomeProperties properties)
	{
		super(properties);
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
	}
	
	protected void addDecorations()
	{
		getDecorator().setFlowerCount(5);
		addFlower(WorldGenPlant.create(EnumPlant.LEPACYCLOTES).setNextToWater(true).setPatchCount(4), 4);
		
		getDecorator().setTreeCount(0.4F);
		addTree(new WorldGenTreeBjuvia(4, 6, true), 1);
	}
	
	@Override
	public float getFogDensity()
	{
		return 0.3F;
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.7F;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		float red = 0.755039216F;
		float green = 0.676607843F;
		float blue = 0.582490196F;
		
		return new Vec3d(red, green, blue);
	}
	
	@Override
	public Vec3d getFogColorNight()
	{
		float red = 0.070941176F;
		float green = 0.070941176F;
		float blue = 0.070941176F;
		
		return new Vec3d(red, green, blue);
	}
	
	@Override
	public int getSkyColorByTemp(float temperature)
	{
		return 0xB28F77;
	}
}
