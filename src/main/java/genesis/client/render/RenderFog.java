package genesis.client.render;

import org.lwjgl.opengl.GL11;

import genesis.util.GenesisMath;
import genesis.util.WorldUtils;
import genesis.world.WorldProviderGenesis;
import genesis.world.biome.IBiomeGenFog;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
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
	private static float planeDistance = 0.0F;
	
	@SubscribeEvent
	public void onGetFogColor(FogColors event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			World world = player.worldObj;

			float partialTicks = (float) event.renderPartialTicks;
			Block blockAtEyes = ActiveRenderInfo.getBlockAtEntityViewpoint(world, event.entity, partialTicks);
			
			float red = 0;
			float green = 0;
			float blue = 0;
			int samples = 0;
			
			if (blockAtEyes.getMaterial() == Material.lava)
			{
				event.red = 0.8F;
				event.green = 0.482352941F;
				event.blue = 0.17254902F;
				samples = 1;
			}
			else
			{
				int areaSize = 10;
				int supersamples = 1;
				float sampleStep = 1 / (float) supersamples;
				
				for (float x = -areaSize; x <= areaSize; x += sampleStep)
				{
					for (float z = -areaSize; z <= areaSize; z += sampleStep)
					{
						if (x * x + z * z < areaSize * areaSize)
						{
							BlockPos samplePos = new BlockPos(player.posX + x, player.posY, player.posZ + z);
							BiomeGenBase biome = world.getBiomeGenForCoords(samplePos);
							
							if (blockAtEyes.getMaterial() == Material.water)
							{
								int waterColorMultiplier = biome.getWaterColorMultiplier();
								event.red = (waterColorMultiplier % 0xFF0000) >> 16;
								event.green = (waterColorMultiplier % 0x00FF00) >> 8;
								event.blue = (waterColorMultiplier % 0x0000FF);
								
								event.red *= 0.160784314F;
								event.green *= 0.384313725F;
								event.blue *= 0.749019608F;
								
								event.red *= 0.0008F;
								event.green *= 0.0008F;
								event.blue *= 0.0008F;
							}
							else if (biome instanceof IBiomeGenFog)
							{
								IBiomeGenFog fogBiome = (IBiomeGenFog) biome;
								long time = world.getWorldTime();
								
								float percent = getDayNightFactor(time, partialTicks);
								
								Vec3 biomeColor = GenesisMath.lerp(fogBiome.getFogColor(), fogBiome.getFogColorNight(), 1 - percent);
								red += (float) biomeColor.xCoord;
								green += (float) biomeColor.yCoord;
								blue += (float) biomeColor.zCoord;
							}
							else
							{
								red += event.red;
								green += event.green;
								blue += event.blue;
							}
							
							samples++;
						}
					}
				}
			}
			
			event.red = red / samples;
			event.green = green / samples;
			event.blue = blue / samples;
			
			/*BiomeGenBase biome = world.getBiomeGenForCoords(player.getPosition());
			
			if (biome instanceof IBiomeGenFog)
			{
				IBiomeGenFog fogBiome = (IBiomeGenFog) biome;
				long time = world.getWorldTime();
				
				double red = fogBiome.getFogColor().xCoord;
				double green = fogBiome.getFogColor().yCoord;
				double blue = fogBiome.getFogColor().zCoord;
				
				double nRed = fogBiome.getFogColorNight().xCoord;
				double nGreen = fogBiome.getFogColorNight().yCoord;
				double nBlue = fogBiome.getFogColorNight().zCoord;
				
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
				else
				{
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
			}*/
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
