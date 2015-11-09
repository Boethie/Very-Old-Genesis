package genesis.client;

import java.io.IOException;

import genesis.common.Genesis;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

public class ColorizerDryMoss implements IResourceManagerReloadListener
{
	private static int[] grassBuffer = new int[65536];

	public static void setColorMap(int[] grassBufferIn)
	{
		grassBuffer = grassBufferIn;
	}

	public static int getColor(double temperature, double humidity)
	{
		humidity *= temperature;
		int temperatureOff = (int)((1 - temperature) * 255);
		int humidityOff = (int)((1 - humidity) * 255);
		int bufferIndex = humidityOff << 8 | temperatureOff;

		return bufferIndex > grassBuffer.length ? -65281 : grassBuffer[bufferIndex];
	}
	
	private static final ResourceLocation DRY_MOSS_RES = new ResourceLocation("genesis:textures/colormap/dry_moss.png");

	@Override
	public void onResourceManagerReload(IResourceManager resManager)
	{
		try
		{
			setColorMap(TextureUtil.readImageData(resManager, DRY_MOSS_RES));
		}
		catch (IOException e)
		{
			Genesis.logger.warn(new RuntimeException("Could not load dry moss color map.", e));
		}
	}
}
