package genesis.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** A particle emitted by Radioactive Traces.
 *
 * Opacity is random 0.1-0.2.
 *
 * Mostly identical to ParticleSmokeNormal barring the radioactive-green colour and transparency.
 */
public class ParticleRadioactivity extends ParticleSmokeNormal {
	private ParticleRadioactivity(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46347_8_, double p_i46347_10_, double p_i46347_12_)
	{
		this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46347_8_, p_i46347_10_, p_i46347_12_, 1.0F);
	}

	protected ParticleRadioactivity(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46348_8_, double p_i46348_10_, double p_i46348_12_, float p_i46348_14_)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46348_8_, p_i46348_10_, p_i46348_12_, p_i46348_14_);

		this.particleRed = 108f / 255f;
		this.particleGreen = 114f / 255f;
		this.particleBlue = 102f / 255f;
		this.particleAlpha = 0.325f + (float)(Math.random() * 0.1);
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory
	{
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
		{
			return new ParticleRadioactivity(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		}
	}
}
