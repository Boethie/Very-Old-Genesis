package genesis.common;

import genesis.client.render.RenderFog;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

public class GenesisEvent
{
	public static void init(Side side)
	{
		if (side == Side.CLIENT)
		{
			registerClientEvents();
		}
	}
	
	private static void registerClientEvents()
	{
		MinecraftForge.EVENT_BUS.register(new RenderFog());
	}
}
