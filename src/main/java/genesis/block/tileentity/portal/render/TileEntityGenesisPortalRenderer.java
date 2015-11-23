package genesis.block.tileentity.portal.render;

import org.lwjgl.opengl.GL11;

import genesis.block.tileentity.portal.TileEntityGenesisPortal;
import genesis.util.GenesisMath;
import genesis.util.render.BlockAsEntityPart;
import genesis.util.render.ModelHelpers;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGenesisPortalRenderer extends TileEntitySpecialRenderer
{
	public static final class ModelPortal extends ModelBase
	{
		public BlockAsEntityPart portal = new BlockAsEntityPart(this);
		
		public ModelPortal()
		{
			portal.setAmbientOcclusion(false);
			portal.setDefaultState();
		}
		
		public void renderAll()
		{
			portal.render(0.0625F);
		}
	}
	
	protected ModelPortal model = new ModelPortal();
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage)
	{
		TileEntityGenesisPortal tePortal = (TileEntityGenesisPortal) te;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0001F);
		
		RenderHelper.disableStandardItemLighting();
		
		ModelHelpers.bindAtlasTexture();
		model.portal.resetState();
		model.portal.setModel(te.getWorld().getBlockState(te.getPos()), te.getWorld(), te.getPos());
		model.portal.rotateAngleY = -GenesisMath.lerp(tePortal.prevRotation, tePortal.rotation, partialTick);
		model.renderAll();
		
		RenderHelper.enableStandardItemLighting();
		
		GlStateManager.disableBlend();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		
		GlStateManager.popMatrix();
	}
}
