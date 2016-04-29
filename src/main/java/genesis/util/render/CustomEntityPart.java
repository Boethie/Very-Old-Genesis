package genesis.util.render;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;

public abstract class CustomEntityPart extends EntityPart
{
	public CustomEntityPart(ModelBase model)
	{
		super(model);
		
		resetState();
	}
	
	@Override
	public CustomEntityPart setDefaultState()
	{
		super.setDefaultState();
		
		return this;
	}
	
	@Override
	public void resetState()
	{
		super.resetState();
	}
	
	@Override
	public EntityPart setTextureOffset(int x, int y)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ModelRenderer addBox(String offset, float x, float y, float z, int w, int h, int d)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ModelRenderer addBox(float x, float y, float z, int w, int h, int d)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ModelRenderer addBox(float x, float y, float z, int w, int h, int d, boolean mirror)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addBox(float x, float y, float z, int w, int h, int d, float inset)
	{
		throw new UnsupportedOperationException();
	}
	
	public abstract void doRender(float pxSize);
	
	public void doChildModelRender(float pxSize)
	{
		if (childModels != null)
		{
			for (ModelRenderer childModel : childModels)
			{
				childModel.render(pxSize);
			}
		}
	}
	
	@Override
	public void render(float pxSize)
	{
		if (!isHidden && showModel)
		{
			GlStateManager.pushMatrix();
			
			GlStateManager.translate(offsetX, offsetY, offsetZ);
			
			GlStateManager.translate(rotationPointX, rotationPointY, rotationPointZ);
			
			GlStateManager.rotate(rotateAngleY, 0, 1, 0);
			GlStateManager.rotate(rotateAngleX, 1, 0, 0);
			GlStateManager.rotate(rotateAngleZ, 0, 0, 1);
			
			GlStateManager.translate(-rotationPointX, -rotationPointY, -rotationPointZ);
			
			GlStateManager.scale(scaleX, scaleY, scaleZ);
			
			doChildModelRender(pxSize);
			doRender(pxSize);
			
			GlStateManager.popMatrix();
			RenderHelper.enableStandardItemLighting();
		}
	}
	
	@Override
	public void renderWithRotation(float pxSize)
	{
		render(pxSize);
	}
}
