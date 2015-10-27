package genesis.block.tileentity.gui.render;

import java.util.*;

import com.google.common.collect.ImmutableList;

import genesis.block.tileentity.gui.*;
import genesis.block.tileentity.gui.ContainerBase.UIArea;
import genesis.util.*;
import genesis.util.render.ISpriteUVs;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.renderer.*;
import net.minecraft.inventory.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class GuiContainerBase extends GuiContainer
{
	public static final ResourceLocation TEX = new ResourceLocation(Constants.ASSETS_PREFIX + "textures/gui/base.png");
	
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
		final int r = 43;
		final int g = 39;
		final int b = 15;
		final int color = (r << 16) | (g << 8) | b;
		
		drawDisplayName(color);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), container.getPlayerInventoryArea().left, container.getPlayerInventoryTextY(), color);
	}
	
	protected void drawDisplayName(int color)
	{
		String displayName = namer.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(displayName, xSize / 2 - fontRendererObj.getStringWidth(displayName) / 2, 6, color);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(TEX);

		final int borderW = container.borderW;
		final int borderH = container.borderH;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0);

		// Top left corner
		drawTextureUVPx(0, 0, borderW, borderH,
						0, 0, borderW, borderH);
		// Top border
		drawTextureUVPx(borderW, 0, xSize - borderW * 2, borderH,
						borderW, 0, 1, borderH);
		// Top right corner
		drawTextureUVPx(xSize - borderW, 0, borderW, borderH,
						borderW + 1, 0, borderW, borderH);
		// Right border
		drawTextureUVPx(xSize - borderW, borderH, borderW, ySize - borderH * 2,
						borderW + 1, borderW, borderW, 1);
		// Bottom right corner
		drawTextureUVPx(xSize - borderW, ySize - borderH, borderW, borderH,
						borderW + 1, borderH + 1, borderW, borderH);
		// Bottom border
		drawTextureUVPx(borderW, ySize - borderH, xSize - borderW * 2, borderH,
						borderW, borderH + 1, 1, borderH);
		// Bottom right corner
		drawTextureUVPx(0, ySize - borderH, borderW, borderH,
						0, borderH + 1, borderW, borderH);
		// Left border
		drawTextureUVPx(0, borderW, borderW, ySize - borderH * 2,
						0, borderH, borderW, 1);
		// Center
		drawTextureUVPx(borderW, borderH, xSize - borderW * 2, ySize - borderH * 2,
						borderW, borderH, 1, 1);
		
		for (Slot slot : (List<Slot>) inventorySlots.inventorySlots)
		{
			int slotU = -1;
			int slotV = -1;
			int slotW = container.slotW;
			int slotH = container.slotH;
			
			if (container.isBigSlot(slot))
			{
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
			}

			int offsetX = (slotW - 16) / 2;
			int offsetY = (slotH - 16) / 2;
			drawTextureUVPx(slot.xDisplayPosition - offsetX, slot.yDisplayPosition - offsetY, slotW, slotW,
							slotU, slotV, slotW, slotW);
		}
		
		GlStateManager.popMatrix();
	}
	
	protected void bindTexture(ResourceLocation texture)
	{
		mc.getTextureManager().bindTexture(texture);
	}
	
	protected boolean isPointInSlot(Slot slot, int x, int y)
	{
		return isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, x, y);
	}
	
	protected Slot getSlot(int x, int y)
	{
		for (Slot slot : (List<Slot>) container.inventorySlots)
		{
			if (isPointInSlot(slot, x, y))
			{
				return slot;
			}
		}
		
		return null;
	}
	
	protected void drawTextureUVs(int x, int y, int w, int h, float minU, float minV, float maxU, float maxV)
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.startDrawingQuads();
		renderer.addVertexWithUV(x,		y + h,	zLevel,	minU, maxV);
		renderer.addVertexWithUV(x + w,	y + h,	zLevel,	maxU, maxV);
		renderer.addVertexWithUV(x + w,	y,		zLevel,	maxU, minV);
		renderer.addVertexWithUV(x,		y,		zLevel,	minU, minV);
		tessellator.draw();
	}
	
	protected void drawTextureUVSize(int x, int y, int w, int h, float u, float v, float uvW, float uvH)
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.startDrawingQuads();
		renderer.addVertexWithUV(x,		y + h,	zLevel,	u,			(v + uvH));
		renderer.addVertexWithUV(x + w,	y + h,	zLevel,	(u + uvW),	(v + uvH));
		renderer.addVertexWithUV(x + w,	y,		zLevel,	(u + uvW),	v);
		renderer.addVertexWithUV(x,		y,		zLevel,	u,			v);
		tessellator.draw();
	}
	
	protected void drawTextureUVPx(int x, int y, int w, int h, int u, int v, int uvW, int uvH)
	{
		final float px = 1 / 256F;
		//                Pos         UVs
		drawTextureUVSize(x, y, w, h, u * px, v * px, uvW * px, uvH * px);
	}
	
	protected void drawSprite(int x, int y, int w, int h, ISpriteUVs uvs)
	{
		bindTexture(uvs.getTexture());
		drawTextureUVs(x, y, w, h, uvs.getMinU(), uvs.getMinV(), uvs.getMaxU(), uvs.getMaxV());
	}

	protected void drawTexBetweenSlots(int x, int y, int w, int h, int u, int v, int uvW, int uvH, boolean centerX, boolean centerY, Collection<Slot> slots)
	{
		if (centerX || centerY)
		{
			x -= centerX ? w / 2 : 0;
			y -= centerY ? h / 2 : 0;
			drawTexBetweenSlots(x, y, w, h, u, v, uvW, uvH, false, false, slots);
		}
		else
		{
			UIArea area = container.getSlotsArea(slots);
			x += (area.left + area.right) / 2;
			y += (area.top + area.bottom) / 2;
			
			drawTextureUVPx(x, y, w, h, u, v, uvW, uvH);
		}
	}
	
	protected void drawTexBetweenSlots(int x, int y, int w, int h, int u, int v, int uvW, int uvH, boolean centerX, boolean centerY, Slot... slots)
	{
		drawTexBetweenSlots(x, y, w, h, u, v, uvW, uvH, centerX, centerY, ImmutableList.copyOf(slots));
	}
	
	protected void drawTexBetweenSlots(int x, int y, int w, int h, int u, int v, int uvW, int uvH, Collection<Slot> slots)
	{
		drawTexBetweenSlots(x, y, w, h, u, v, uvW, uvH, true, true, slots);
	}
	
	protected void drawTexBetweenSlots(int x, int y, int w, int h, int u, int v, int uvW, int uvH, Slot... slots)
	{
		drawTexBetweenSlots(x, y, w, h, u, v, uvW, uvH, true, true, slots);
	}
}
