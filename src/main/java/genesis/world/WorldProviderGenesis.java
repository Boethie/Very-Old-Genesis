package genesis.world;

import genesis.common.Genesis;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderGenesis extends WorldProvider {

	@Override
	public String getDimensionName() {
		return "Genesis";
	}

	@Override
	public String getInternalNameSuffix() {
		return "genesis";
	}

	@Override
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerGenesis(worldObj.getSeed(), WorldTypeGenesis.instance);
		this.dimensionId = Genesis.dimensionId;
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderGenesis(worldObj, worldObj.getSeed());
	}

	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
	{
		float[] colorsSunriseSunset = super.calcSunriseSunsetColors(celestialAngle, partialTicks);

		float f2 = 0.4F;
		float f3 = MathHelper.cos(celestialAngle * (float)Math.PI * 2.0F) - 0.0F;
		float f4 = -0.0F;

		if (f3 >= f4 - f2 && f3 <= f4 + f2)
		{
			float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
			float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * (float)Math.PI)) * 0.99F;
			f6 *= f6;
			colorsSunriseSunset[0] = f5 * f5 * 0.4F + 0.1F;
			colorsSunriseSunset[1] = f5 * f5 * 0.0F + 0.1F;
			colorsSunriseSunset[2] = f5 * f5 * 0.3F + 0.6F;
			colorsSunriseSunset[3] = f6;
			return colorsSunriseSunset;
		}
		else
		{
			return null;
		}
	}

}
