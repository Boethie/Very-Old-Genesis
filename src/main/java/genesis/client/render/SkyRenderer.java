package genesis.client.render;

import genesis.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

public class SkyRenderer extends IRenderHandler
{
	private static final ResourceLocation MOON_PHASES_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/environment/moon_phases.png");
	private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc)
	{
		GlStateManager.disableTexture2D();
		Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
		float f = (float)vec3d.xCoord;
		float f1 = (float)vec3d.yCoord;
		float f2 = (float)vec3d.zCoord;
		
		if (mc.gameSettings.anaglyph)
		{
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}
		
		GlStateManager.color(f, f1, f2);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		GlStateManager.depthMask(false);
		GlStateManager.enableFog();
		GlStateManager.color(f, f1, f2);
		
		if (OpenGlHelper.useVbo())
		{
			mc.renderGlobal.skyVBO.bindBuffer();
			GlStateManager.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GlStateManager.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
			mc.renderGlobal.skyVBO.drawArrays(GL11.GL_QUADS);
			mc.renderGlobal.skyVBO.unbindBuffer();
			GlStateManager.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		}
		else
		{
			GlStateManager.callList(mc.renderGlobal.glSkyList);
		}
		
		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.disableStandardItemLighting();
		float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
		
		if (afloat != null)
		{
			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
			float f6 = afloat[0];
			float f7 = afloat[1];
			float f8 = afloat[2];
			
			if (mc.gameSettings.anaglyph)
			{
				float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
				float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
				float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
				f6 = f9;
				f7 = f10;
				f8 = f11;
			}
			
			vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			vertexbuffer.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3]).endVertex();
			int j = 16;
			
			for (int l = 0; l <= 16; ++l)
			{
				float f21 = (float)l * ((float)Math.PI * 2F) / 16.0F;
				float f12 = MathHelper.sin(f21);
				float f13 = MathHelper.cos(f21);
				vertexbuffer.pos((double)(f12 * 120.0F), (double)(f13 * 120.0F), (double)(-f13 * 40.0F * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
			}
			
			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.shadeModel(GL11.GL_FLAT);
		}
		
		GlStateManager.enableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		float f16 = 1.0F - world.getRainStrength(partialTicks);
		GlStateManager.color(1.0F, 1.0F, 1.0F, f16);
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
		float f17 = 30.0F;
		mc.getTextureManager().bindTexture(SUN_LOCATION);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos((double)(-f17), 100.0D, (double)(-f17)).tex(0.0D, 0.0D).endVertex();
		vertexbuffer.pos((double)f17, 100.0D, (double)(-f17)).tex(1.0D, 0.0D).endVertex();
		vertexbuffer.pos((double)f17, 100.0D, (double)f17).tex(1.0D, 1.0D).endVertex();
		vertexbuffer.pos((double)(-f17), 100.0D, (double)f17).tex(0.0D, 1.0D).endVertex();
		tessellator.draw();
		f17 = 20.0F;
		mc.getTextureManager().bindTexture(MOON_PHASES_LOCATION);
		int i = world.getMoonPhase();
		int k = i % 4;
		int i1 = i / 4 % 2;
		float f22 = (float)(k + 0) / 4.0F;
		float f23 = (float)(i1 + 0) / 2.0F;
		float f24 = (float)(k + 1) / 4.0F;
		float f14 = (float)(i1 + 1) / 2.0F;
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos((double)(-f17), -100.0D, (double)f17).tex((double)f24, (double)f14).endVertex();
		vertexbuffer.pos((double)f17, -100.0D, (double)f17).tex((double)f22, (double)f14).endVertex();
		vertexbuffer.pos((double)f17, -100.0D, (double)(-f17)).tex((double)f22, (double)f23).endVertex();
		vertexbuffer.pos((double)(-f17), -100.0D, (double)(-f17)).tex((double)f24, (double)f23).endVertex();
		tessellator.draw();
		GlStateManager.disableTexture2D();
		float f15 = world.getStarBrightness(partialTicks) * f16;
		
		if (f15 > 0.0F)
		{
			GlStateManager.color(f15, f15, f15, f15);
			
			if (OpenGlHelper.useVbo())
			{
				mc.renderGlobal.starVBO.bindBuffer();
				GlStateManager.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				GlStateManager.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
				mc.renderGlobal.starVBO.drawArrays(GL11.GL_QUADS);
				mc.renderGlobal.starVBO.unbindBuffer();
				GlStateManager.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			}
			else
			{
				GlStateManager.callList(mc.renderGlobal.starGLCallList);
			}
		}
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableFog();
		GlStateManager.popMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.color(0.0F, 0.0F, 0.0F);
		double d0 = mc.thePlayer.getPositionEyes(partialTicks).yCoord - world.getHorizon();
		
		if (d0 < 0.0D)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 12.0F, 0.0F);
			
			if (OpenGlHelper.useVbo())
			{
				mc.renderGlobal.sky2VBO.bindBuffer();
				GlStateManager.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				GlStateManager.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
				mc.renderGlobal.sky2VBO.drawArrays(GL11.GL_QUADS);
				mc.renderGlobal.sky2VBO.unbindBuffer();
				GlStateManager.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			}
			else
			{
				GlStateManager.callList(mc.renderGlobal.glSkyList2);
			}
			
			GlStateManager.popMatrix();
			float f18 = 1.0F;
			float f19 = -((float)(d0 + 65.0D));
			float f20 = -1.0F;
			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			vertexbuffer.pos(-1.0D, (double)f19, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, (double)f19, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, (double)f19, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, (double)f19, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, (double)f19, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, (double)f19, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, (double)f19, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, (double)f19, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			tessellator.draw();
		}
		
		if (world.provider.isSkyColored())
		{
			GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
		}
		else
		{
			GlStateManager.color(f, f1, f2);
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, -((float)(d0 - 16.0D)), 0.0F);
		GlStateManager.callList(mc.renderGlobal.glSkyList2);
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
	}
}
