package genesis.util;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

public class Stringify
{
	public static String stringify(Iterable<Object> list)
	{
		String output = "{ ";
		
		for (Object obj : list)
		{
			output += stringify(obj) + ", ";
		}
		
		output = output.substring(0, output.length() - 2) + " }";
		
		return output;
	}
	
	public static String stringify(Object[] objArray)
	{
		return stringify(Arrays.asList(objArray));
	}
	
	public static String stringify(Object obj)
	{
		if (obj instanceof Object[])
		{
			return stringify((Object[]) obj);
		}
		
		if (obj instanceof Iterable)
		{
			return stringify((Iterable) obj);
		}
		
		if (obj instanceof IStringSerializable)
		{
			return ((IStringSerializable) obj).getName();
		}
		
		String data = "";
		
		if (obj instanceof Item)
		{
			data += "name = " + ((Item) obj).getUnlocalizedName();
		}
		else if (obj instanceof Block)
		{
			data += "name = " + ((Block) obj).getUnlocalizedName();
		}
		else if (obj instanceof Class)
		{
			Class clazz = (Class)obj;
			String name = clazz.getSimpleName();
			
			if (clazz.isAnonymousClass())
			{
				name = clazz.getSuperclass().getSimpleName();
			}
			
			data += "name = " + name;
		}
		
		return obj.getClass().getSimpleName() + (data.length() > 0 ? "(" + data + ")" : "");
	}
}
