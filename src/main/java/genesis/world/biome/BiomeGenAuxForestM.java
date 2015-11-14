package genesis.world.biome;

import net.minecraft.util.Vec3;

public class BiomeGenAuxForestM extends BiomeGenAuxForest
{
	public BiomeGenAuxForestM(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Forest M");
		setHeight(0.4F, 0.7F);
	}
	
	@Override
	public float getFogDensity(int x, int y, int z)
	{
		return 0.75F;
	}
	
	@Override
	public Vec3 getFogColor()
	{
		float red = 0.533333333F;
		float green = 0.647058824F;
		float blue = 0.474509804F;
		
		return new Vec3(red, green, blue);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
	}
}
