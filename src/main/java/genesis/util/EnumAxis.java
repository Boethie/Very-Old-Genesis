package genesis.util;

import genesis.combo.variant.IMetadata;
import net.minecraft.util.*;

public enum EnumAxis implements IMetadata<EnumAxis>
{
	X("x"),
	Y("y"),
	Z("z"),
	NONE("none");

	public static final EnumAxis[] VALID = {X, Y, Z};
	public static final EnumAxis[] HORIZ = {X, Z};
	
	protected final String name;
	protected final String unlocName;
	
	EnumAxis(String name, String unlocName)
	{
		this.name = name;
		this.unlocName = unlocName;
	}
	
	EnumAxis(String name)
	{
		this(name, name);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getUnlocalizedName()
	{
		return unlocName;
	}
	
	public static EnumAxis getForAngle(float rotationYaw)
	{
		rotationYaw = MathHelper.wrapAngleTo180_float(rotationYaw);
		
		if (rotationYaw < 0)
		{
			rotationYaw += 180;
		}
		
		if (rotationYaw >= 45 && rotationYaw < 135)
		{
			return X;
		}
		
		return Z;
	}
}
