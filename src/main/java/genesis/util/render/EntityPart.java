package genesis.util.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class EntityPart extends ModelRenderer
{
	public float scaleX = 1;
	public float scaleY = 1;
	public float scaleZ = 1;
	
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

	protected int textureOffsetX = 0;
	protected int textureOffsetY = 0;
	
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
