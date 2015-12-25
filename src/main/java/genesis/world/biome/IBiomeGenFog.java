package genesis.world.biome;

import net.minecraft.util.Vec3;

public interface IBiomeGenFog
{
	float getFogDensity();
	float getNightFogModifier();
	Vec3 getFogColor();
	Vec3 getFogColorNight();
}
