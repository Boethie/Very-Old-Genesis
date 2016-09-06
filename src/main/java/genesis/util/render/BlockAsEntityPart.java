package genesis.util.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

			if (texture != null)
			{
				model = ModelHelpers.getCubeProjectedBakedModel(state, model, texture, pos);
			}

			Tessellator tess = Tessellator.getInstance();
			VertexBuffer buff = tess.getBuffer();

			GlStateManager.pushMatrix();
			GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());

			RenderHelper.disableStandardItemLighting();

			buff.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

			if (noColor)
				buff.noColor();

			if (ambientOcclusion)
				ModelHelpers.getBlockRenderer().renderModelSmooth(world, model, state, pos, buff, false, MathHelper.getPositionRandom(pos));
			else
				ModelHelpers.getBlockRenderer().renderModelFlat(world, model, state, pos, buff, false, MathHelper.getPositionRandom(pos));

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
