package genesis.world.biome;

import net.minecraft.util.Vec3;

public interface IBiomeGenFog
{
	float getFogDensity(int x, int y, int z);
	float getNightFogModifier();
	Vec3 getFogColor();
	Vec3 getFogColorNight();
}
