package genesis.client.render;

import org.lwjgl.opengl.GL11;

import genesis.world.biome.IBiomeGenFog;
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
	private static boolean initialized = false;
	private static float planeDistance = 0.0F;
	
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
				event.red = (float) ((IBiomeGenFog)biome).getFogColor().xCoord;
				event.green = (float) ((IBiomeGenFog)biome).getFogColor().yCoord;
				event.blue = (float) ((IBiomeGenFog)biome).getFogColor().zCoord;
			}
			else
			{
				float red = 0.29411764705882352941176470588235F;
				float green = 0.47450980392156862745098039215686F;
				float blue = 0.1960784313725490196078431372549F;
				
				event.red = red;
				event.green = green;
				event.blue = blue;
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderFog(EntityViewRenderEvent.RenderFogEvent event)
	{
		Entity entity = event.entity;
		World world = entity.worldObj;
		
		int playerX = MathHelper.floor_double(entity.posX);
		int playerY = MathHelper.floor_double(entity.posY);
		int playerZ = MathHelper.floor_double(entity.posZ);
		
		if (playerX == fogX && playerZ == fogZ && initialized)
		{
			renderFog(event.fogMode, planeDistance, 0.75f);
			return;
		}
		
		initialized = true;
		
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
