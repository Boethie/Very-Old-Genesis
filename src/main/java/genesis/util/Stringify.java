package genesis.util;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

public class Stringify
{
	public static String stringifyIterator(Iterator<?> iterator)
	{
		String output = "{";
		
		while (iterator.hasNext())
		{
			Object obj = iterator.next();
			output += stringify(obj) + (iterator.hasNext() ? ", " : "");
		}
		
		output += "}";
		
		return output;
	}

	public static String stringifyIterable(Iterable<?> iterable)
	{
		return stringifyIterator(iterable.iterator());
	}
	
	public static <T> String stringifyArray(T[] objArray)
	{
		return stringifyIterator(Iterators.forArray(objArray));
	}
	
	public static String stringify(Object obj)
	{
		if (obj instanceof Object[])
		{
			return stringifyArray((Object[]) obj);
		}
		
		if (obj instanceof Iterable<?>)
		{
			return stringifyIterable((Iterable<?>) obj);
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
			Class<?> clazz = (Class<?>) obj;
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
