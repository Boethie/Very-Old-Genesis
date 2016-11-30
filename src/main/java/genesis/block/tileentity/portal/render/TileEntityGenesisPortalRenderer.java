package genesis.block.tileentity.portal.render;

import genesis.block.tileentity.portal.TileEntityGenesisPortal;
import genesis.client.render.RenderPortals;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityGenesisPortalRenderer extends TileEntitySpecialRenderer<TileEntityGenesisPortal>
{
	public static final float PORTALRAD = 2;
	@Override
	public void renderTileEntityAt(TileEntityGenesisPortal te, double x, double y, double z, float partialTick, int destroyStage)
	{
		RenderPortals.portals.add(new Float[] {(float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f, PORTALRAD});
	}
}
