package genesis.util;

import java.lang.reflect.Constructor;

public class ReflectionHelper
{
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Object[] args)
	{
		for (Constructor constructor : clazz.getDeclaredConstructors())
		{
			Class[] parameterTypes = constructor.getParameterTypes();
			boolean correct = true;
			
			for (int i = 0; i < args.length; i++)
			{
				if (!parameterTypes[i].isAssignableFrom(args[i].getClass()))
				{
					correct = false;
				}
			}
			
			if (correct)
			{
				return constructor;
			}
		}
		
		return null;
	}
	
	public static <T> T construct(Class<T> clazz, Object[] args)
	{
		try
		{
			return getConstructor(clazz, args).newInstance(args);
		}
		catch (ReflectiveOperationException e)
		{
			throw new RuntimeException(e);
		}
	}
}
