package genesis.client;

import genesis.client.render.RenderPortals;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GenesisClientEventHandler
{
	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event)
	{
		RenderPortals.drawPortals();
	}
}