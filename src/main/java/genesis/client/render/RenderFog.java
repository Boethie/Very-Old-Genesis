package genesis.client.render;

import org.lwjgl.opengl.GL11;

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
			EntityPlayer player = (EntityPlayer)event.entity;
			World world = player.worldObj;
			
			int playerX = MathHelper.floor_double(player.posX);
			int playerZ = MathHelper.floor_double(player.posZ);
			
			BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(playerX, 60, playerZ));
			
			if (biome instanceof IBiomeGenFog)
			{
				long time = world.getWorldTime()%34000;
				
				double red = ((IBiomeGenFog)biome).getFogColor().xCoord;
				double green = ((IBiomeGenFog)biome).getFogColor().yCoord;
				double blue = ((IBiomeGenFog)biome).getFogColor().zCoord;
				
				double nRed = ((IBiomeGenFog)biome).getFogColorNight().xCoord;
				double nGreen = ((IBiomeGenFog)biome).getFogColorNight().yCoord;
				double nBlue = ((IBiomeGenFog)biome).getFogColorNight().zCoord;
				
				Block blockAtEyes = ActiveRenderInfo.getBlockAtEntityViewpoint(world, event.entity, (float)event.renderPartialTicks);
				
				float percent = getDayNightFactor(time);
				
				red = applyColorTransition(red, nRed, 1.0f - percent);
				green = applyColorTransition(green, nGreen, 1.0f - percent);
				blue = applyColorTransition(blue, nBlue, 1.0f - percent);
				
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
					red = (waterColorMultiplier % 0xff0000) >> 16;
					green = (waterColorMultiplier % 0x00ff00) >> 8;
					blue = (waterColorMultiplier % 0x0000ff);
					
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
				
				event.red = (float)curRed;
				event.green = (float)curGreen;
				event.blue = (float)curBlue;
			}
		}
	}
	
	private double applyColorTransition(double origin, double destiny, float percentage)
	{
		return origin + ((destiny - origin) * percentage);
	}
	
	private float getDayNightFactor(long time)
	{
		float factor = 1.0F;
		
		//Night time
		if (time > 20000 && time <= 31000)
		{
			factor = 0;
		}
		//Sunset
		else if (time > 17000 && time <= 20000)
		{
			factor = 1.0f - (((float)time - 17000f) / 3000f);
			if (factor < 0.0f)
				factor = 0.0f;
		}
		//Sunrise
		else if (time > 31000 && time <= 34000)
		{
			factor = (((float)time - 31000f) / 3000f);
			if (factor > 1.0f)
				factor = 1.0f;
		}
		
		return factor;
	}
	
	@SubscribeEvent
	public void onRenderFog(EntityViewRenderEvent.RenderFogEvent event)
	{
		Entity entity = event.entity;
		World world = entity.worldObj;
		
		long time = world.getWorldTime()%34000;
		
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
					float distancePart = ((IBiomeGenFog)biome).getFogDensity(playerX + x, playerY, playerZ + z);
					float weightPart = 1;
					
					distancePart *= 1.0f - (((IBiomeGenFog)biome).getNightFogModifier() * (1.0f - getDayNightFactor(time)));
					
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
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, farPlaneDistance);
		}
		else
		{
			GL11.glFogf(GL11.GL_FOG_START, farPlaneDistance * farPlaneDistanceScale);
			GL11.glFogf(GL11.GL_FOG_END, farPlaneDistance);
		}
	}
}
