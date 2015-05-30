package genesis.client;

import net.minecraft.util.EnumParticleTypes;
import genesis.entity.effect.*;
import genesis.util.Constants;
import genesis.util.ParticleUtils;

public class GenesisParticles
{
	public static EnumParticleTypes WATER_SPLASH;
	
	public static void createParticles()
	{
		WATER_SPLASH = ParticleUtils.registerParticleSystem(Constants.MOD_ID.toUpperCase() + "_WATER_SPLASH",
				Constants.ASSETS + "water_splash", 1257, false, 0, new EntitySplashInsideBlockFX.Factory());
	}
}
