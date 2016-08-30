package genesis.world;

import genesis.client.render.RenderFog;
import genesis.client.render.SkyRenderer;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisDimensions;
import genesis.util.GenesisMath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderGenesis extends WorldProvider
{
	public static final int DAY_LENGTH = 34000;
	
	public static final float TWILIGHT_START = 0.5F;
	public static final float EARLY_TWILIGHT_TIME = 0.088F;
	public static final float NIGHT_START = TWILIGHT_START + EARLY_TWILIGHT_TIME;
	
	public static final float TWILIGHT_END = 1F;
	public static final float LATE_TWILIGHT_TIME = 0.088F;
	public static final float NIGHT_END = TWILIGHT_END - LATE_TWILIGHT_TIME;
	
	protected float[] colorsSunriseSunset = new float[4];
	
	@Override
	protected void generateLightBrightnessTable()
	{
		for (int i = 0; i <= 15; ++i)
		{
			float levelF = 1 - i / 15F;
			lightBrightnessTable[i] = (1 - levelF) / (levelF * 3 + 1);
		}
	}
	
	@Override
	public String getWelcomeMessage()
	{
		return TextFormatting.ITALIC + "You feel yourself forgetting your knowledge of crafting...";
	}
	
	@Override
	protected void createBiomeProvider()
	{
		biomeProvider = new BiomeProviderGenesis(worldObj);
	}
	
	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new ChunkGeneratorGenesis(worldObj,
				worldObj.getSeed(),
				worldObj.getWorldInfo().isMapFeaturesEnabled(),
				worldObj.getWorldInfo().getGeneratorOptions());
	}
	
	@Override
	public boolean canCoordinateBeSpawn(int x, int z)
	{
		return worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)) == GenesisBlocks.moss;
	}
	
	@Override
	public void setWorldTime(long time)
	{
		GenesisWorldData.get(worldObj).setTime(time);
	}
	
	@Override
	public long getWorldTime()
	{
		return GenesisWorldData.get(worldObj).getTime();
	}
	
	@Override
	public float calculateCelestialAngle(long time, float partialTick)
	{
		time = worldObj.getWorldTime();
		float angle = ((time + partialTick) % DAY_LENGTH) / DAY_LENGTH - 0.25F;
		
		if (angle < 0)
			angle++;
		
		angle = 1 - (MathHelper.cos(angle * (float) Math.PI) + 1) / 2;
		return angle;
	}
	
	@Override
	public int getMoonPhase(long time)
	{
		time = worldObj.getWorldTime();
		return (int) (time / DAY_LENGTH % 8);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
	{
		float f2 = 0.4F;
		float f3 = MathHelper.cos(celestialAngle * (float) Math.PI * 2.0F) - 0.0F;
		float f4 = -0.0F;
		
		if (f3 >= f4 - f2 && f3 <= f4 + f2)
		{
			float strength = (f3 - f4) / f2 * 0.5F + 0.5F;
			float alpha = 1.0F - (1.0F - MathHelper.sin(strength * (float) Math.PI)) * 0.99F;
			alpha *= alpha;
			colorsSunriseSunset[0] = strength * 0.3F + 0.7F;
			colorsSunriseSunset[1] = strength * strength * 0.7F + 0.2F;
			colorsSunriseSunset[2] = strength * strength * 0.0F + 0.2F;
			colorsSunriseSunset[3] = alpha;
			return colorsSunriseSunset;
		}
		
		return null;
	}
	
	@Override
	public Vec3d getFogColor(float angle, float partialTicks)
	{
		return GenesisMath.lerp(RenderFog.INSTANCE.prevColor, RenderFog.INSTANCE.color, partialTicks);
	}
	
	@Override
	public DimensionType getDimensionType()
	{
		return GenesisDimensions.GENESIS_DIMENSION;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer()
	{
		IRenderHandler skyRenderer = super.getSkyRenderer();
		
		if (skyRenderer == null)
		{
			skyRenderer = new SkyRenderer();
			setSkyRenderer(skyRenderer);
		}
		
		return skyRenderer;
	}
}
