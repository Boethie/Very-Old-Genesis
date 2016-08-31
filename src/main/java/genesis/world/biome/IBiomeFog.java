package genesis.world.biome;

import net.minecraft.util.math.Vec3d;

public interface IBiomeFog
{
	float getFogDensity();
	float getNightFogModifier();
	Vec3d getFogColor();
	Vec3d getFogColorNight();
}
