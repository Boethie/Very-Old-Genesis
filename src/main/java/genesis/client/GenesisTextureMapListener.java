package genesis.client;

import genesis.block.tileentity.portal.render.TileEntityGenesisPortalRenderer;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GenesisTextureMapListener
{
	@SubscribeEvent
	public void onTextureStitchPre(TextureStitchEvent.Pre event)
	{
		TileEntityGenesisPortalRenderer.portal = event.getMap().registerSprite(TileEntityGenesisPortalRenderer.PORTAL);
		TileEntityGenesisPortalRenderer.sphere = event.getMap().registerSprite(TileEntityGenesisPortalRenderer.SPHERE);
	}
	
	//@SubscribeEvent
	public void onTextureStitchPost(TextureStitchEvent.Post event) {} // for future use in generating model data
}