package genesis.util.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

@SuppressWarnings("deprecation")
public class BlockAsEntityPart extends CustomEntityPart
{
	// Values
	public ModelResourceLocation modelLocation;
	public IBlockAccess world;
	public BlockPos pos;
	
	public TextureAtlasSprite texture = null;
	
	// Defaults
	public ModelResourceLocation modelLocationDef;
	public IBlockAccess worldDef;
	public BlockPos posDef;
	
	public TextureAtlasSprite textureDef = null;
	
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

		modelLocationDef = modelLocation;
		worldDef = world;
		posDef = pos;
		
		textureDef = texture;
		
		return this;
	}
	
	public void resetState()
	{
		super.resetState();
		
		modelLocation = modelLocationDef;
		world = worldDef;
		pos = posDef;
		
		texture = textureDef;
	}
	
	public void setModelLocation(ModelResourceLocation modelLocation, IBlockAccess world, BlockPos pos)
	{
		this.modelLocation = modelLocation;
		this.world = world;
		this.pos = pos;
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
	public void doRender(float pxSize)
	{
		if (modelLocation != null)
		{
			float scale = pxSize * 16;
			GlStateManager.scale(scale, scale, scale);
			
			IBakedModel model = ModelHelpers.getBakedBlockModel(modelLocation, world, pos);
			
			if (texture != null)
			{
				model = ModelHelpers.getRetexturedBakedModel(model, texture);
			}
			
			ModelHelpers.renderBakedModel(model);
		}
	}
	
	@Override
	public void renderWithRotation(float pxSize)
	{
		render(pxSize);
	}
}
