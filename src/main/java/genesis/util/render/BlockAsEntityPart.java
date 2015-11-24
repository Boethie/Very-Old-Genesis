package genesis.util.render;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("deprecation")
public class BlockAsEntityPart extends CustomEntityPart
{
	// Values
	public IBlockState state;
	public IBlockAccess world;
	public BlockPos pos;
	
	public boolean ambientOcclusion = true;
	
	public TextureAtlasSprite texture = null;
	
	// Defaults
	public IBlockState stateDef;
	public IBlockAccess worldDef;
	public BlockPos posDef;
	
	public boolean ambientOcclusionDef = true;
	
	public TextureAtlasSprite textureDef = null;
	
	protected final Set<Pair<Class<? extends Exception>, String>> exceptionsCaught = new HashSet<Pair<Class<? extends Exception>, String>>();
	
	public BlockAsEntityPart(ModelBase model)
	{
		super(model);
		
		rotationPointX += 0.5F;
		rotationPointZ += 0.5F;
		setDefaultState();
	}
	
	public BlockAsEntityPart setDefaultState()
	{
		super.setDefaultState();

		stateDef = state;
		worldDef = world;
		posDef = pos;
		
		ambientOcclusionDef = ambientOcclusion;
		
		textureDef = texture;
		
		return this;
	}
	
	public void resetState()
	{
		super.resetState();
		
		state = stateDef;
		world = worldDef;
		pos = posDef;
		
		ambientOcclusion = ambientOcclusionDef;
		
		texture = textureDef;
	}
	
	public void setModel(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		this.state = state;
		this.world = world;
		this.pos = pos;
	}
	
	public void setModel(ModelResourceLocation location, IBlockAccess world, BlockPos pos)
	{
		this.state = ModelHelpers.getFakeState(location);
		this.world = world;
		this.pos = pos;
	}

	public void setAmbientOcclusion(boolean ambientOcclusion)
	{
		this.ambientOcclusion = ambientOcclusion;
	}
	
	public void setTexture(TextureAtlasSprite texture)
	{
		this.texture = texture;
	}
	
	@Override
	public void doChildModelRender(float pxSize)
	{
		super.doChildModelRender(pxSize);
	}
	
	@Override
	public void render(float pxSize)
	{
		super.render(pxSize);
		
		if (!isHidden && showModel)
		{
			RenderHelper.enableStandardItemLighting();
		}
	}
	
	@Override
	public void doRender(float pxSize)
	{
		if (state != null)
		{
			float scale = pxSize * 16;
			GlStateManager.scale(scale, scale, scale);
			
			IBakedModel model = ModelHelpers.getBakedBlockModel(state, world, pos);
			IBlockState state = world.getBlockState(pos);
			
			if (texture != null)
			{
				model = ModelHelpers.getRetexturedBakedModel(model, texture);
			}
			
			if (ambientOcclusion)
			{
				Tessellator tess = Tessellator.getInstance();
				WorldRenderer wr = tess.getWorldRenderer();
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
				
				RenderHelper.disableStandardItemLighting();
				
				wr.func_181668_a(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
				
				ModelHelpers.getBlockRenderer().renderModel(world, model, state, pos, wr, false);
				
				tess.draw();
				
				GlStateManager.popMatrix();
			}
			else
			{
				GlStateManager.pushMatrix();
				GlStateManager.rotate(-90, 0, 1, 0);	// Screwy vanilla methods. >.>
				ModelHelpers.getBlockRenderer().renderModelBrightness(model, state, 1, true);
				GlStateManager.popMatrix();
			}
		}
	}
	
	@Override
	public void renderWithRotation(float pxSize)
	{
		render(pxSize);
	}
}
