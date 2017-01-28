package genesis.block.tileentity.portal.render;

import org.lwjgl.opengl.GL11;

import genesis.block.tileentity.portal.TileEntityGenesisPortal;
import genesis.block.tileentity.portal.TileEntityGenesisPortal.PortalParticle;
import genesis.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class TileEntityGenesisPortalRenderer extends TileEntitySpecialRenderer<TileEntityGenesisPortal>
{
	public static final ResourceLocation PORTAL = new ResourceLocation(Constants.MOD_ID, "entity/portal/portal");
	public static final ResourceLocation SPHERE = new ResourceLocation(Constants.MOD_ID, "entity/portal/sphere");
	public static TextureAtlasSprite portal;
	public static TextureAtlasSprite sphere;
	
	@Override
	public void renderTileEntityAt(TileEntityGenesisPortal te, double x, double y, double z, float partialTick, int destroyStage)
	{
		final float minUP = portal.getInterpolatedU(0.1);
		final float minVP = portal.getInterpolatedV(0.1);
		final float maxUP = portal.getInterpolatedU(15.9);
		final float maxVP = portal.getInterpolatedV(15.9);
		final float halfVS = sphere.getInterpolatedV(8);
		final float minUS = sphere.getInterpolatedU(0.1);
		final float minVS = sphere.getInterpolatedV(0.1);
		final float maxUS = sphere.getInterpolatedU(15.9);
		final float maxVS = sphere.getInterpolatedV(15.9);
		GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
		GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		Minecraft mc = Minecraft.getMinecraft();
		RenderManager manager = mc.getRenderManager();
		float r = te.getRadius(partialTick);
		GlStateManager.disableCull();
		GlStateManager.depthMask(true);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.color(0.9F, 1, 1, 1);
		boolean top = te.getPos().getY() + 0.5 < manager.renderViewEntity.getPositionEyes(partialTick).yCoord;
		float rc = r * 0.21875f;
		
		GlStateManager.pushMatrix();
		float rp = r * .375f;
		GlStateManager.scale(rp, rp, rp);
		GlStateManager.disableTexture2D();
		GL11.glPointSize(5);
		GlStateManager.glBegin(GL11.GL_POINTS);
		for (int i = 0; i < te.particles.size(); i++)
		{
        	PortalParticle p = te.particles.get(i);
        	Vec3d vec = p.getVec(partialTick);
        	GlStateManager.glVertex3f((float) vec.xCoord, (float) vec.yCoord, (float) vec.zCoord);
        }
		GlStateManager.glEnd();
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();
		
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180.0F - manager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(manager.options.thirdPersonView == 2 ? manager.playerViewX : -manager.playerViewX, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(rc, rc, 0);
		GlStateManager.glBegin(GL11.GL_QUADS);
		if (top)
		{
        	GlStateManager.glTexCoord2f(minUS, minVS);
        	GlStateManager.glVertex3f(-1, -1, 0);
        	GlStateManager.glTexCoord2f(maxUS, minVS);
        	GlStateManager.glVertex3f(1, -1, 0);
        	GlStateManager.glTexCoord2f(maxUS, halfVS);
        	GlStateManager.glVertex3f(1, 0, 0);
        	GlStateManager.glTexCoord2f(minUS, halfVS);
        	GlStateManager.glVertex3f(-1, 0, 0);
		}
		else
		{
        	GlStateManager.glTexCoord2f(minUS, halfVS);
        	GlStateManager.glVertex3f(-1, 0, 0);
        	GlStateManager.glTexCoord2f(maxUS, halfVS);
        	GlStateManager.glVertex3f(1, 0, 0);
        	GlStateManager.glTexCoord2f(maxUS, maxVS);
        	GlStateManager.glVertex3f(1, 1, 0);
        	GlStateManager.glTexCoord2f(minUS, maxVS);
        	GlStateManager.glVertex3f(-1, 1, 0);
		}
		GlStateManager.glEnd();
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.rotate(te.getAngle(partialTick), 0, -1, 0);
		GlStateManager.glBegin(GL11.GL_QUADS);
		GlStateManager.glTexCoord2f(minUP, minVP);
		GlStateManager.glVertex3f(-r, 0, -r);
		GlStateManager.glTexCoord2f(maxUP, minVP);
		GlStateManager.glVertex3f(r, 0, -r);
		GlStateManager.glTexCoord2f(maxUP, maxVP);
		GlStateManager.glVertex3f(r, 0, r);
		GlStateManager.glTexCoord2f(minUP, maxVP);
		GlStateManager.glVertex3f(-r, 0, r);
		GlStateManager.glEnd();
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180.0F - manager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(manager.options.thirdPersonView == 2 ? manager.playerViewX : -manager.playerViewX, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(rc, rc, 0);
		GlStateManager.glBegin(GL11.GL_QUADS);
		if (top)
		{
        	GlStateManager.glTexCoord2f(minUS, halfVS);
        	GlStateManager.glVertex3f(-1, 0, 0);
        	GlStateManager.glTexCoord2f(maxUS, halfVS);
        	GlStateManager.glVertex3f(1, 0, 0);
        	GlStateManager.glTexCoord2f(maxUS, maxVS);
        	GlStateManager.glVertex3f(1, 1, 0);
        	GlStateManager.glTexCoord2f(minUS, maxVS);
        	GlStateManager.glVertex3f(-1, 1, 0);
		}
		else
		{
        	GlStateManager.glTexCoord2f(minUS, minVS);
        	GlStateManager.glVertex3f(-1, -1, 0);
        	GlStateManager.glTexCoord2f(maxUS, minVS);
        	GlStateManager.glVertex3f(1, -1, 0);
        	GlStateManager.glTexCoord2f(maxUS, halfVS);
        	GlStateManager.glVertex3f(1, 0, 0);
        	GlStateManager.glTexCoord2f(minUS, halfVS);
        	GlStateManager.glVertex3f(-1, 0, 0);
		}
		GlStateManager.glEnd();
		GlStateManager.popMatrix();
		
		GlStateManager.popMatrix();
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
		GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}
}
