package genesis.world.biome;

import net.minecraft.util.Vec3;

public interface IBiomeGenFog
{
	public float getFogDensity(int x, int y, int z);
	public int getFogColor(int x, int y, int z);
	public Vec3 getFogColor();
	public float getNightFogModifier();
}
