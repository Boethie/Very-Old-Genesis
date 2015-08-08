package genesis.util.render;

import genesis.util.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.item.*;

public abstract class CustomEntityPart extends EntityPart
{
	protected float scaleXDef = 1;
	protected float scaleYDef = 1;
	protected float scaleZDef = 1;
	
	public float scaleX = 1;
	public float scaleY = 1;
	public float scaleZ = 1;
	
	public CustomEntityPart(ModelBase model)
	{
		super(model);
		
		resetState();
	}
	
	@Override
	public CustomEntityPart setDefaultState()
	{
		super.setDefaultState();
		
		scaleXDef = scaleX;
		scaleYDef = scaleY;
		scaleZDef = scaleZ;
		
		return this;
	}
	
	public void resetState()
	{
		super.resetState();
		
		scaleX = scaleXDef;
		scaleY = scaleYDef;
		scaleZ = scaleZDef;
	}

	@Override
	public ModelRenderer setTextureOffset(int x, int y)
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
			for (Object childModelObj : childModels)
			{
				((ModelRenderer) childModelObj).render(pxSize);
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
		}
	}
	
	@Override
	public void renderWithRotation(float pxSize)
	{
		render(pxSize);
	}
}
