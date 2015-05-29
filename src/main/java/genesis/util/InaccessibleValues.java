package genesis.util;

import java.lang.reflect.*;
import java.util.*;

import net.minecraft.item.ItemBucket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class InaccessibleValues
{
	private static Map<Class, String> teClassToNameMap;
	
	public static Map<Class, String> getTEClassToNameMap()
	{
		if (teClassToNameMap != null)
		{
			return teClassToNameMap;
		}
		
		return teClassToNameMap = ReflectionHelper.getPrivateValue(TileEntity.class, null, "classToNameMap", "field_145853_j");
	}
}
