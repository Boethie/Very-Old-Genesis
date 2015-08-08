package genesis.util.render;

import java.util.List;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;

public class EntityPart extends ModelRenderer
{
	protected static ModelBase addingToModel = null;
	protected static List<EntityPart> addingToList = null;
	
	public static void addPartsTo(ModelBase model, List<EntityPart> parts)
	{
		addingToModel = model;
		addingToList = parts;
	}
	
	public static void stopAddingParts()
	{
		addingToModel = null;
		addingToList = null;
	}
	
	protected static void addPart(ModelBase model, EntityPart part)
	{
		if (addingToModel == model)
		{
			addingToList.add(part);
		}
		else
		{
			throw new RuntimeException("Tried to create a model part for a model when adding model parts to another model's list.");
		}
	}
	
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
}
