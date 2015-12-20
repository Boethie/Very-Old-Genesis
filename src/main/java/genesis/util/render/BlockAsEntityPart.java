package genesis.util.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockAsEntityPart extends CustomEntityPart
{
	// Values
	private IBlockState state;
	private IBlockAccess world;
	private BlockPos pos;
	
	private boolean ambientOcclusion = true;
	private boolean noColor;
	
	private TextureAtlasSprite texture = null;
	
	// Defaults
	private IBlockState stateDef;
	private IBlockAccess worldDef;
	private BlockPos posDef;
	
	private boolean ambientOcclusionDef = true;
	private boolean noColorDef;
	
	private TextureAtlasSprite textureDef = null;
	
	public BlockAsEntityPart(ModelBase model)
	{
		super(model);
		
		rotationPointX += 0.5F;
		rotationPointZ += 0.5F;
		setDefaultState();
	}
	
	@Override
	public BlockAsEntityPart setDefaultState()
	{
		super.setDefaultState();

		stateDef = state;
		worldDef = world;
		posDef = pos;
		
		ambientOcclusionDef = ambientOcclusion;
		noColorDef = noColor;
		
		textureDef = texture;
		
		return this;
	}
	
	@Override
	public void resetState()
	{
		super.resetState();
		
		state = stateDef;
		world = worldDef;
		pos = posDef;
		
		ambientOcclusion = ambientOcclusionDef;
		noColor = noColorDef;
		
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
	
	public void noColor(boolean noColor)
	{
		this.noColor = noColor;
		if (noColor)
			setAmbientOcclusion(false);
	}
	
	public void setTextureNoColor(TextureAtlasSprite texture)
	{
		setTexture(texture);
		noColor(true);
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
				model = ModelHelpers.getCubeProjectedBakedModel(model, texture);
			}
			
			Tessellator tess = Tessellator.getInstance();
			WorldRenderer wr = tess.getWorldRenderer();
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
			
			RenderHelper.disableStandardItemLighting();
			
			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			
			if (noColor)
				wr.markDirty();
			
			if (ambientOcclusion)
				ModelHelpers.getBlockRenderer().renderModelAmbientOcclusion(world, model, state.getBlock(), pos, wr, false);
			else
				ModelHelpers.getBlockRenderer().renderModelStandard(world, model, state.getBlock(), pos, wr, false);
			
			tess.draw();
			
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public void renderWithRotation(float pxSize)
	{
		render(pxSize);
	}
}
