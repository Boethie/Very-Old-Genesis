package genesis.util;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

public class Stringify
{
	public static String stringify(Collection<Object> list)
	{
		String output = "{ ";
		
		for (Object obj : list)
		{
			output += stringify(obj) + ", ";
		}
		
		output = output.substring(0, output.length() - 2) + " }";
		
		return output;
	}
	
	public static String stringify(Object obj)
	{
		if (obj instanceof IStringSerializable)
		{
			return ((IStringSerializable)obj).getName();
		}
		
		String data = "";
		
		if (obj instanceof Item)
		{
			data += "name = " + ((Item)obj).getUnlocalizedName();
		}
		else if (obj instanceof Block)
		{
			data += "name = " + ((Block)obj).getUnlocalizedName();
		}
		
		return obj.getClass().getSimpleName() + (data.length() > 0 ? "(" + data + ")" : "");
	}
}
