package genesis.client.render;

import org.lwjgl.opengl.GL11;

import genesis.util.GenesisMath;
import genesis.world.WorldProviderGenesis;
import genesis.world.biome.IBiomeGenFog;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderFog
{
	private static double fogX = 0.0D;
	private static double fogZ = 0.0D;
	private static boolean densityInit = false;
	private static boolean colorInit = false;
	private static float planeDistance = 0.0F;
	
	public static double curRed = 0.0D;
	public static double curGreen = 0.0D;
	public static double curBlue = 0.0D;
	
	public static double targetRed = 0.0D;
	public static double targetGreen = 0.0D;
	public static double targetBlue = 0.0D;
	
	@SubscribeEvent
	public void onGetFogColor(FogColors event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			World world = player.worldObj;
			BiomeGenBase biome = world.getBiomeGenForCoords(player.getPosition());
			
			if (biome instanceof IBiomeGenFog)
			{
				IBiomeGenFog fogBiome = (IBiomeGenFog) biome;
				long time = world.getWorldTime();
				float partialTicks = (float) event.renderPartialTicks;
				
				double red = fogBiome.getFogColor().xCoord;
				double green = fogBiome.getFogColor().yCoord;
				double blue = fogBiome.getFogColor().zCoord;
				
				double nRed = fogBiome.getFogColorNight().xCoord;
				double nGreen = fogBiome.getFogColorNight().yCoord;
				double nBlue = fogBiome.getFogColorNight().zCoord;
				
				Block blockAtEyes = ActiveRenderInfo.getBlockAtEntityViewpoint(world, event.entity, partialTicks);
				
				float percent = getDayNightFactor(time, partialTicks);
				
				red = GenesisMath.lerp(red, nRed, 1 - percent);
				green = GenesisMath.lerp(green, nGreen, 1 - percent);
				blue = GenesisMath.lerp(blue, nBlue, 1 - percent);
				
				if (!colorInit)
				{
					curRed = red;
					curGreen = green;
					curBlue = blue;
					colorInit = true;
				}
				
				if (blockAtEyes.getMaterial() == Material.water)
				{
					int waterColorMultiplier = biome.getWaterColorMultiplier();
					red = (waterColorMultiplier % 0xFF0000) >> 16;
					green = (waterColorMultiplier % 0x00FF00) >> 8;
					blue = (waterColorMultiplier % 0x0000FF);
					
					red *= 0.160784314D;
					green *= 0.384313725D;
					blue *= 0.749019608D;
					
					red *= 0.0008D;
					green *= 0.0008D;
					blue *= 0.0008D;
				}
				
				if (blockAtEyes.getMaterial() == Material.lava)
				{
					red = 0.8D;
					green = 0.482352941D;
					blue = 0.17254902D;
				}
				
				targetRed = red;
				targetGreen = green;
				targetBlue = blue;
				
				curRed += (targetRed - curRed) * 0.01D;
				curGreen += (targetGreen - curGreen) * 0.01D;
				curBlue += (targetBlue - curBlue) * 0.01D;
				
				event.red = (float) curRed;
				event.green = (float) curGreen;
				event.blue = (float) curBlue;
			}
		}
	}
	
	private static float getDayNightFactor(long time, float partialTicks)
	{
		float timeF = (time + partialTicks) % WorldProviderGenesis.DAY_LENGTH / WorldProviderGenesis.DAY_LENGTH;
		float factor = 1;
		
		if (timeF > WorldProviderGenesis.NIGHT_END)
		{	// Sunrise
			factor = ((timeF - WorldProviderGenesis.NIGHT_END) / WorldProviderGenesis.LATE_TWILIGHT_TIME);
			if (factor > 1)
				factor = 1;
		}
		else if (timeF > WorldProviderGenesis.NIGHT_START)
		{	// Night
			factor = 0;
		}
		else if (time > WorldProviderGenesis.TWILIGHT_START)
		{	// Sunset
			factor = 1 - ((timeF - WorldProviderGenesis.TWILIGHT_START) / WorldProviderGenesis.EARLY_TWILIGHT_TIME);
			if (factor < 0)
				factor = 0;
		}
		
		return MathHelper.clamp_float(factor, 0, 1);
	}
	
	@SubscribeEvent
	public void onRenderFog(EntityViewRenderEvent.RenderFogEvent event)
	{
		Entity entity = event.entity;
		World world = entity.worldObj;
		
		long time = world.getWorldTime();
		
		int playerX = MathHelper.floor_double(entity.posX);
		int playerY = MathHelper.floor_double(entity.posY);
		int playerZ = MathHelper.floor_double(entity.posZ);
		
		if (playerX == fogX && playerZ == fogZ && densityInit)
		{
			renderFog(event.fogMode, planeDistance, 0.75f);
			return;
		}
		
		densityInit = true;
		
		int distance = 20;
		float fogDistance = 0F;
		float fogDensity = 0;
		
		for (int x = -distance; x <= distance; ++x)
		{
			for (int z = -distance; z <= distance; ++z)
			{
				BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(playerX + x, 60, playerZ + z));
				
				if (biome instanceof IBiomeGenFog)
				{
					IBiomeGenFog fogBiome = (IBiomeGenFog) biome;
					float distancePart = fogBiome.getFogDensity(playerX + x, playerY, playerZ + z);
					float weightPart = 1;
					
					distancePart *= 1 - (fogBiome.getNightFogModifier() * (1 - getDayNightFactor(time, (float) event.renderPartialTicks)));
					
					if (x == -distance)
					{
						double xDistance = 1 - (entity.posX - playerX);
						distancePart *= xDistance;
						weightPart *= xDistance;
					}
					else if (x == distance)
					{
						double xDistance = (entity.posX - playerX);
						distancePart *= xDistance;
						weightPart *= xDistance;
					}
					
					if (z == -distance)
					{
						double zDistance = 1 - (entity.posZ - playerZ);
						distancePart *= zDistance;
						weightPart *= zDistance;
					}
					else if (z == distance)
					{
						double zDistance = (entity.posZ - playerZ);
						distancePart *= zDistance;
						weightPart *= zDistance;
					}
					
					fogDistance += distancePart;
					fogDensity += weightPart;
				}
			}
		}
		
		float weightMixed = (distance * 2) * (distance * 2);
		float weightDefault = weightMixed - fogDensity;
		
		float fogDistanceAvg = (fogDensity == 0) ? 0 : fogDistance / fogDensity;
		
		float farPlaneDistance = (fogDistance * 240 + event.farPlaneDistance * weightDefault) / weightMixed;
		float farPlaneDistanceScaleBiome = (0.1f * (1 - fogDistanceAvg) + 0.75f * fogDistanceAvg);
		float farPlaneDistanceScale = (farPlaneDistanceScaleBiome * fogDensity + 0.75f * weightDefault) / weightMixed;
		
		fogX = entity.posX;
		fogZ = entity.posZ;
		planeDistance = Math.min(farPlaneDistance, event.farPlaneDistance);
		
		renderFog(event.fogMode, planeDistance, farPlaneDistanceScale);
	}
	
	private static void renderFog(int fogMode, float farPlaneDistance, float farPlaneDistanceScale)
	{
		if (fogMode < 0)
		{
			GL11.glFogf(GL11.GL_FOG_START, 0);
			GL11.glFogf(GL11.GL_FOG_END, farPlaneDistance);
		}
		else
		{
			GL11.glFogf(GL11.GL_FOG_START, farPlaneDistance * farPlaneDistanceScale);
			GL11.glFogf(GL11.GL_FOG_END, farPlaneDistance);
		}
	}
}
