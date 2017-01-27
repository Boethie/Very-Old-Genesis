package genesis.block.tileentity.gui.render;

import genesis.block.tileentity.*;
import genesis.block.tileentity.gui.*;
import genesis.util.Constants;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiContainerCampfire extends GuiContainerBase
{
	public static final ResourceLocation CAMPFIRE_TEX = new ResourceLocation(Constants.MOD_ID, "textures/gui/campfire.png");
	
	protected final TileEntityCampfire camp;
	protected final ContainerCampfire contCamp = (ContainerCampfire) container;
	
	public GuiContainerCampfire(EntityPlayer player, TileEntityCampfire te)
	{
		super(new ContainerCampfire(player, te), te);
		
		camp = te;
	}
	
	private static final int BURNER_W = 14;
	private static final int BURNER_H = 14;
	
	private static final int COOKER_W = 22;
	private static final int COOKER_H = 16;
	
	private static final int BOWL_U = 0;
	private static final int BOWL_V = 30;
	private static final int BOWL_W = 16;
	private static final int BOWL_H = 16;
	
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
		drawTexBetweenSlots(0, 0, BURNER_W, BURNER_H,
							0, 0, BURNER_W, BURNER_H,
							contCamp.input, contCamp.fuel);
		// Render burner fire or water overlay.
		int burnerU = camp.isWet() ? BURNER_W * 2 : BURNER_W;
		
		final int burnH = camp.getBurnTimeLeftScaled(BURNER_H - 1);
		final int burnY = BURNER_H - burnH;
		drawTexBetweenSlots(0, burnY / 2, BURNER_W, burnH,
							burnerU, burnY, BURNER_W, burnH,
							contCamp.input, contCamp.fuel);
		
		// COOKING PROGRESS
		int x = (contCamp.ingredients[contCamp.ingredients.length - 1].xDisplayPosition + contCamp.output.xDisplayPosition - 8) / 2;
		int y = (contCamp.output.yDisplayPosition + contCamp.inputWaste.yDisplayPosition) / 2;
		
		// Render cook progress bar background.
		drawTextureUVPx(x, y, COOKER_W, COOKER_H,
						0, BURNER_H, COOKER_W, COOKER_H);
		// Render cook progress bar overlay.
		final int cookW = camp.getCookProgressScaled(COOKER_W);
		drawTextureUVPx(x, y, cookW, COOKER_H,
						COOKER_W, BURNER_H, cookW, COOKER_H,
						false, false);
		
		GlStateManager.enableBlend();
		Slot waste = contCamp.inputWaste;
		drawTexturedModalRect(waste.xDisplayPosition, waste.yDisplayPosition, BOWL_U, BOWL_V, BOWL_W, BOWL_H);
		GlStateManager.disableBlend();
		
		GlStateManager.popMatrix();
	}
}
