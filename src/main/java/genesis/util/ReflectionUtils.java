package genesis.util;

import java.lang.reflect.Constructor;

public class ReflectionUtils
{
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Object[] args)
	{
		for (Constructor<?> declared : clazz.getConstructors())
		{
			Constructor<T> actual = null;
			
			try
			{
				actual = clazz.getConstructor(declared.getParameterTypes());
			}
			catch (NoSuchMethodException e) {}
			
			if (actual != null)
			{
				Class<?>[] parameterTypes = actual.getParameterTypes();
				
				if (parameterTypes.length == args.length)
				{
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
						return actual;
					}
				}
			}
		}
		
		throw new RuntimeException(new NoSuchMethodException(clazz.getName() + " has no constructor with parameters " + Stringify.stringifyArray(args) + "."));
	}
	
	public static <T> T construct(Class<T> clazz, Object[] args)
	{
		try
		{
			return getConstructor(clazz, args).newInstance(args);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> convertClass(Class<? super T> clazz)
	{
		return (Class<T>) clazz;
	}
}
