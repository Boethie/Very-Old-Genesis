package genesis.util.render;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;

public class EntityPart extends ModelRenderer
{
	public float scaleX = 1;
	public float scaleY = 1;
	public float scaleZ = 1;
	
	private boolean showModelDef = true;
	private boolean isHiddenDef = false;
	private float offsetXDef = 0;
	private float offsetYDef = 0;
	private float offsetZDef = 0;
	private float rotationPointXDef = 0;
	private float rotationPointYDef = 0;
	private float rotationPointZDef = 0;
	private float rotateAngleXDef = 0;
	private float rotateAngleYDef = 0;
	private float rotateAngleZDef = 0;
	private float scaleXDef = 1;
	private float scaleYDef = 1;
	private float scaleZDef = 1;

	private int textureOffsetX = 0;
	private int textureOffsetY = 0;
	
	public EntityPart(ModelBase model)
	{
		super(model);
		
		resetState();
	}
	
	public EntityPart(ModelBase model, int offsetX, int offsetY)
	{
		this(model);
		
		setTextureOffset(offsetX, offsetY);
	}
	
	public EntityPart setDefaultState()
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
	
	public EntityPart setDefaultState(boolean children)
	{
		setDefaultState();

		if (children && childModels != null)
		{
			for (Object child : childModels)
			{
				if (child instanceof EntityPart)
				{
					((EntityPart) child).setDefaultState(true);
				}
			}
		}
		
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
	
	public EntityPart resetState(boolean children)
	{
		resetState();
		
		if (children && childModels != null)
		{
			for (Object child : childModels)
			{
				if (child instanceof EntityPart)
				{
					((EntityPart) child).resetState(true);
				}
			}
		}
		
		return this;
	}
	
	public EntityPart setOffset(float x, float y, float z)
	{
		offsetX = x;
		offsetY = y;
		offsetZ = z;
		
		return this;
	}
	
	public EntityPart setRotation(float x, float y, float z)
	{
		rotateAngleX = x;
		rotateAngleY = y;
		rotateAngleZ = z;
		
		return this;
	}
	
	public EntityPart setScale(float x, float y, float z)
	{
		scaleX = x;
		scaleY = y;
		scaleZ = z;
		
		return this;
	}
	
	@Override
	public void render(float scale)
	{
		GlStateManager.scale(scaleX, scaleY, scaleZ);
		super.render(scale);
		GlStateManager.scale(1 / scaleX, 1 / scaleY, 1 / scaleZ);
	}
	
	@Override
	public EntityPart setTextureOffset(int u, int v)
	{
		super.setTextureOffset(u, v);
		
		textureOffsetX = u;
		textureOffsetY = v;
		
		return this;
	}

	public void addElement(CustomModelElement element)
	{
		cubeList.add(element);
	}
	
	public int getTextureOffsetX()
	{
		return textureOffsetX;
	}
	
	public int getTextureOffsetY()
	{
		return textureOffsetY;
	}
}
