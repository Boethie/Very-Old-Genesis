package genesis.common;

import genesis.client.render.RenderFog;
import genesis.command.CommandInterceptor;
import genesis.event.GenesisEventHandler;
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
		
		MinecraftForge.EVENT_BUS.register(GenesisBlocks.roots);
		MinecraftForge.EVENT_BUS.register(new CommandInterceptor());
		MinecraftForge.EVENT_BUS.register(new GenesisEventHandler());
	}
	
	private static void registerClientEvents()
	{
		MinecraftForge.EVENT_BUS.register(RenderFog.INSTANCE);
	}
}
