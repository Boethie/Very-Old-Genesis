package genesis.block.tileentity.gui.render;

import java.util.*;

import genesis.block.tileentity.gui.*;
import genesis.util.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.renderer.*;
import net.minecraft.inventory.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class GuiContainerBase extends GuiContainer
{
	public static final ResourceLocation TEX = new ResourceLocation(Constants.ASSETS + "textures/gui/base.png");
	
	protected ContainerBase container;
	protected IWorldNameable namer;

	public GuiContainerBase(ContainerBase container, IWorldNameable namer)
	{
		super(container);
		
		this.container = container;
		
		this.namer = namer;
		this.xSize = container.width;
		this.ySize = container.height;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String displayName = namer.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(displayName, xSize / 2 - fontRendererObj.getStringWidth(displayName) / 2, 6, 14737632);
		
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 14737632);
	}
	
	public void drawTex(int x, int y, int w, int h, int u, int v, int uvW, int uvH)
	{
		final float px = 1 / 256F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.startDrawingQuads();
		renderer.addVertexWithUV(x, 	y + h,	zLevel,	u * px, 			(v + uvH) * px);
		renderer.addVertexWithUV(x + w,	y + h,	zLevel,	(u + uvW) * px,	(v + uvH) * px);
		renderer.addVertexWithUV(x + w,	y, 		zLevel,	(u + uvW) * px,	v * px);
		renderer.addVertexWithUV(x, 	y, 		zLevel,	u * px, 			v * px);
		tessellator.draw();
	}
	
	public void drawTexBetweenSlots(int x, int y, int w, int h, int u, int v, int uvW, int uvH, Slot... slots)
	{
		int minX = -1;
		int maxX = -1;
		int minY = -1;
		int maxY = -1;
		
		for (Slot slot : slots)
		{
			if (minX == -1)
			{
				minX = slot.xDisplayPosition;
				maxX = slot.xDisplayPosition;
				minY = slot.yDisplayPosition;
				maxY = slot.yDisplayPosition;
			}
			else
			{
				minX = Math.min(minX, slot.xDisplayPosition);
				maxX = Math.max(maxX, slot.xDisplayPosition);
				minY = Math.min(minY, slot.yDisplayPosition);
				maxY = Math.max(maxY, slot.yDisplayPosition);
			}
		}

		x += (minX + maxX + container.SLOT_W) / 2 - 1;
		y += (minY + maxY + container.SLOT_H) / 2 - 1;
		
		drawTex(x, y, w, h, u, v, uvW, uvH);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
	{
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(TEX);

		final int borderW = container.BORDER_W;
		final int borderH = container.BORDER_H;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0);

		// Top left corner
		drawTex(0, 0, borderW, borderH,
				0, 0, borderW, borderH);
		// Top border
		drawTex(borderW, 0, xSize - borderW * 2, borderH,
				borderW, 0, 1, borderH);
		// Top right corner
		drawTex(xSize - borderW, 0, borderW, borderH,
				borderW + 1, 0, borderW, borderH);
		// Right border
		drawTex(xSize - borderW, borderH, borderW, ySize - borderH * 2,
				borderW + 1, borderW, borderW, 1);
		// Bottom right corner
		drawTex(xSize - borderW, ySize - borderH, borderW, borderH,
				borderW + 1, borderH + 1, borderW, borderH);
		// Bottom border
		drawTex(borderW, ySize - borderH, xSize - borderW * 2, borderH,
				borderW, borderH + 1, 1, borderH);
		// Bottom right corner
		drawTex(0, ySize - borderH, borderW, borderH,
				0, borderH + 1, borderW, borderH);
		// Left border
		drawTex(0, borderW, borderW, ySize - borderH * 2,
				0, borderH, borderW, 1);
		// Center
		drawTex(borderW, borderH, xSize - borderW * 2, ySize - borderH * 2,
				borderW, borderH, 1, 1);
		
		for (Slot slot : (List<Slot>) inventorySlots.inventorySlots)
		{
			int slotX = slot.xDisplayPosition;
			int slotY = slot.yDisplayPosition;
			int slotU = -1;
			int slotV = -1;
			int slotW = container.SLOT_W;
			int slotH = container.SLOT_H;
			
			if (container.isBigSlot(slot))
			{
				slotX += 4;
				slotY += 4;
				slotW += 8;
				slotH += 8;

				if (slot.canBeHovered())
				{
					slotU = 27;
					slotV = 0;
				}
				else
				{
					slotU = 27;
					slotV = 26;
				}
				/*drawTex(slot.xDisplayPosition - 5, slot.yDisplayPosition - 5, slotW + 8, slotW + 8,
						27, 0, slotW + 8, slotW + 8);*/
			}
			else
			{
				if (slot.canBeHovered())
				{
					slotU = 9;
					slotV = 0;
				}
				else
				{
					slotU = 9;
					slotV = 18;
				}
				/*drawTex(slot.xDisplayPosition - 1, slot.yDisplayPosition - 1, slotW, slotW,
						9, 0, slotW, slotW);*/
			}

			int offsetX = (slotW - 16) / 2;
			int offsetY = (slotH - 16) / 2;
			drawTex(slot.xDisplayPosition - offsetX, slot.yDisplayPosition - offsetY, slotW, slotW,
					slotU, slotV, slotW, slotW);
		}
		
		GlStateManager.popMatrix();
	}
}
