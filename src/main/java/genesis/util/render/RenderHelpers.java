package genesis.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

import org.lwjgl.opengl.GL11;

public class RenderHelpers
{
	public static void renderEntityBounds(Entity entity, float partialTick)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		if (!mc.gameSettings.hideGUI)
		{
			// Render bounding box around entity if it is being looked at.
			MovingObjectPosition lookingAt = mc.objectMouseOver;
			
			if (lookingAt != null && lookingAt.typeOfHit == MovingObjectType.ENTITY && lookingAt.entityHit == entity)
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
				RenderGlobal.drawOutlinedBoundingBox(entity.getEntityBoundingBox().expand(expand, expand, expand).offset(-offX, -offY, -offZ), -1);
				
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
}
