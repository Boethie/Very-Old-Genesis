package genesis.block.tileentity.gui.render;

import genesis.block.tileentity.*;
import genesis.block.tileentity.gui.*;
import genesis.block.tileentity.gui.ContainerBase.UIArea;
import genesis.util.Constants;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;

public class GuiContainerCampfire extends GuiContainerBase
{
	public static final ResourceLocation CAMPFIRE_TEX = new ResourceLocation(Constants.ASSETS_PREFIX + "textures/gui/campfire.png");
	
	protected final TileEntityCampfire camp;
	protected final ContainerCampfire contCamp = (ContainerCampfire) container;
	
	public GuiContainerCampfire(EntityPlayer player, TileEntityCampfire te)
	{
		super(new ContainerCampfire(player, te), te);
		
		camp = te;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
		
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(CAMPFIRE_TEX);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0);
		
		// BURNER PROGRESS
		// Render burner background.
		final int burnerW = 14;
		final int burnerH = 14;
		drawTexBetweenSlots(0, 0, burnerW, burnerH,
							0, 0, burnerW, burnerH,
							contCamp.input, contCamp.fuel);
		// Render burner fire or water overlay.
		int burnerU = camp.isWet() ? burnerW * 2 : burnerW;
		
		final int burnH = camp.getBurnTimeLeftScaled(burnerH - 1);
		final int burnY = burnerH - burnH;
		drawTexBetweenSlots(0, burnY / 2, burnerW, burnH,
							burnerU, burnY, burnerW, burnH,
							contCamp.input, contCamp.fuel);
		
		// COOKING PROGRESS
		int x = (contCamp.ingredients[contCamp.ingredients.length - 1].xDisplayPosition + contCamp.output.xDisplayPosition - 8) / 2;
		int y = contCamp.output.yDisplayPosition;
		
		// Render cook progress bar background.
		final int cookerW = 22;
		final int cookerH = 16;
		drawTextureUVPx(x, y, cookerW, cookerH,
						0, burnerH, cookerW, cookerH);
		// Render cook progress bar overlay.
		final int cookW = camp.getCookProgressScaled(cookerW);
		drawTextureUVPx(x, y, cookW, cookerH,
						cookerW, burnerH, cookW, cookerH,
						false, false);
		
		GlStateManager.popMatrix();
	}
}
