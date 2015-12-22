package genesis.world;

import genesis.common.GenesisBlocks;
import genesis.util.GenesisMath;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
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
		float f = 0.0F;
		
		for (int i = 0; i <= 15; ++i)
		{
			float f1 = 1 - i / 15F;
			this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}
	}
	
	@Override
	public String getDimensionName()
	{
		return "Genesis";
	}
	
	@Override
	public String getInternalNameSuffix()
	{
		return "";
	}
	
	@Override
	public String getWelcomeMessage()
	{
		return EnumChatFormatting.ITALIC + "You feel yourself forgetting your knowledge of crafting...";
	}
	
	@Override
	protected void registerWorldChunkManager()
	{
		this.worldChunkMgr = new WorldChunkManagerGenesis(this.worldObj);
	}
	
	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new ChunkGeneratorGenesis(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getWorldInfo().getGeneratorOptions());
	}
	
	@Override
	public float calculateCelestialAngle(long time, float partialTick)
	{
		float dayTime = (time + partialTick) % DAY_LENGTH;
		float angle = dayTime / DAY_LENGTH - 0.25F;
		
		if (angle < 0)
			angle++;
		
		if (angle > 1)
			System.out.println("this really shouldn't happen");
			//angle--;
		
		float target = angle;
		angle = 1 - (MathHelper.cos(angle * (float) Math.PI) + 1) / 2;
		angle = GenesisMath.lerp(target, angle, 0.3333F);
		return angle;
	}
	
	@Override
	public int getMoonPhase(long time)
	{
		return (int) (time / DAY_LENGTH % 8 + 8) % 8;
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
			float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
			float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * (float) Math.PI)) * 0.99F;
			f6 *= f6;
			colorsSunriseSunset[0] = f5 * 0.3F + 0.7F;
			colorsSunriseSunset[1] = f5 * f5 * 0.7F + 0.2F;
			colorsSunriseSunset[2] = f5 * f5 * 0.0F + 0.2F;
			colorsSunriseSunset[3] = f6;
			return colorsSunriseSunset;
		}
		
		return null;
	}
	
	@Override
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
	{
		float time = (worldObj.getWorldTime() + partialTicks) % DAY_LENGTH / DAY_LENGTH;
		
		double red = 0.29411764705882352941176470588235D;
		double green = 0.47450980392156862745098039215686D;
		double blue = 0.1960784313725490196078431372549D;
		//Green sky everywhere - To change if we ever need different color by biome
		/*
		if (getBiomeGenForCoords(new BlockPos(cameraEntity.posX, cameraEntity.posY, cameraEntity.posZ)) instanceof BiomeGenBaseGenesis)
		{
			BiomeGenBaseGenesis biome = (BiomeGenBaseGenesis)getBiomeGenForCoords(new BlockPos(cameraEntity.posX, cameraEntity.posY, cameraEntity.posZ));
			red = biome.getSkyColor().xCoord;
			green = biome.getSkyColor().yCoord;
			blue = biome.getSkyColor().zCoord;
		}
		*/
		
		if (time > NIGHT_END)
		{	// Sunrise
			float percent = ((time - NIGHT_END) / LATE_TWILIGHT_TIME);
			
			if (percent > 1)
				percent = 1;
			
			red *= percent;
			green *= percent;
			blue *= percent;
		}
		else if (time > NIGHT_START)
		{	// Night
			red = 0;
			green = 0;
			blue = 0;
		}
		else if (time > TWILIGHT_START)
		{	// Sunset
			float percent = 1 - ((time - TWILIGHT_START) / EARLY_TWILIGHT_TIME);
			
			if (percent < 0)
				percent = 0;
			
			red *= percent;
			green *= percent;
			blue *= percent;
		}
		
		return new Vec3(red, green, blue);
	}
	
	@Override
	public void setWorldTime(long time)
	{
		worldObj.getWorldInfo().setWorldTime(time);
	}
	
	@Override
	public long getWorldTime()
	{
		return worldObj.getWorldInfo().getWorldTime();
	}
	
	@Override
	public boolean canCoordinateBeSpawn(int x, int z)
	{
		return worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)) == GenesisBlocks.moss;
	}
}
