package genesis.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.util.EnumHelper;

public class ParticleUtils
{
	public static EnumParticleTypes registerParticleSystem(String enumName, String name, int id, boolean ignoreRange, int argumentCount, IParticleFactory factory)
	{
		for (EnumParticleTypes existingParticle : EnumParticleTypes.values())
		{
			if (existingParticle.getParticleID() == id)
			{
				throw new RuntimeException("Something attempted to register a particle with the same integer ID as " + existingParticle.getParticleName() + " (" + existingParticle + ").");
			}
		}
		
		EnumParticleTypes particle = EnumHelper.addEnum(EnumParticleTypes.class, enumName,
				new Class[]{String.class, int.class, boolean.class, int.class},
				new Object[]{name, id, ignoreRange, argumentCount});
		Minecraft.getMinecraft().effectRenderer.func_178929_a(id, factory);
		
		return particle;
	}
}
