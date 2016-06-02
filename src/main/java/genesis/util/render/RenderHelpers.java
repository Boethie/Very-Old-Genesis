package genesis.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;

import org.lwjgl.opengl.GL11;

public class RenderHelpers
{
	public static void renderEntityBounds(Entity entity, float partialTick)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		if (!mc.gameSettings.hideGUI)
		{
			// Render bounding box around entity if it is being looked at.
			RayTraceResult lookingAt = mc.objectMouseOver;
			
			if (lookingAt != null && lookingAt.typeOfHit == RayTraceResult.Type.ENTITY && lookingAt.entityHit == entity)
			{
				GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
				
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.color(0, 0, 0, 0.4F);
				GL11.glLineWidth(2);
				GlStateManager.disableTexture2D();
				GlStateManager.depthMask(false);
				float expand = entity.getCollisionBorderSize() + 0.002F;
				
				double offX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTick;
				double offY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTick;
				double offZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTick;
				RenderGlobal.drawSelectionBoundingBox(entity.getEntityBoundingBox().expand(expand, expand, expand).offset(-offX, -offY, -offZ));
				
				GlStateManager.depthMask(true);
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();
				
				GL11.glPopAttrib();
			}
		}
	}
	
	public static void renderEntityBounds(Entity entity, double x, double y, double z, float partialTick)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		renderEntityBounds(entity, partialTick);
		GlStateManager.popMatrix();
	}
	
	public static void drawTextureWithTessellator(int x, int y, int zLevel, int sizeX, int sizeY, ResourceLocation texture, float alpha)
	{
		Tessellator tess = Tessellator.getInstance();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		if(alpha >= 0)
			GlStateManager.alphaFunc(GL11.GL_ALPHA, alpha);
		
		VertexBuffer vb = tess.getBuffer();
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		vb.pos(x, y+sizeY, 1).tex(0, 1).endVertex();
		
		vb.pos(x+sizeX, y+sizeY, 1).tex(1, 1).endVertex();
		
		vb.pos(x+sizeX, y, 1).tex(1, 0).endVertex();
		
		vb.pos(x, y, 1).tex(0, 0).endVertex();
		
		tess.draw();
	}
}
