package genesis.util.render;

import genesis.util.*;

import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.item.*;

public abstract class CustomEntityPart extends ModelRenderer
{
	protected boolean showModelDef = true;
	protected boolean isHiddenDef = false;
	protected float offsetXDef = 0;
	protected float offsetYDef = 0;
	protected float offsetZDef = 0;
	protected float rotationPointXDef = 0;
	protected float rotationPointYDef = 0;
	protected float rotationPointZDef = 0;
	protected float rotateAngleXDef = 0;
	protected float rotateAngleYDef = 0;
	protected float rotateAngleZDef = 0;
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
	
	public CustomEntityPart setDefaultState()
	{
		showModelDef = showModel;
		isHiddenDef = isHidden;
		offsetXDef = offsetX;
		offsetYDef = offsetY;
		offsetZDef = offsetZ;
		rotationPointXDef = rotationPointX;
		rotationPointYDef = rotationPointY;
		rotationPointZDef = rotationPointZ;
		rotateAngleXDef = rotateAngleX;
		rotateAngleYDef = rotateAngleY;
		rotateAngleZDef = rotateAngleZ;
		scaleXDef = scaleX;
		scaleYDef = scaleY;
		scaleZDef = scaleZ;
		
		return this;
	}
	
	public void resetState()
	{
		showModel = showModelDef;
		isHidden = isHiddenDef;
		offsetX = offsetXDef;
		offsetY = offsetYDef;
		offsetZ = offsetZDef;
		rotationPointX = rotationPointXDef;
		rotationPointY = rotationPointYDef;
		rotationPointZ = rotationPointZDef;
		rotateAngleX = rotateAngleXDef;
		rotateAngleY = rotateAngleYDef;
		rotateAngleZ = rotateAngleZDef;
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
