package genesis.util;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.ClassUtils;

public class ReflectionUtils
{
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Object[] invokeArgs)
	{
		for (Constructor<?> declared : clazz.getConstructors())
		{
			Constructor<T> constructor = null;
			
			try
			{
				constructor = clazz.getConstructor(declared.getParameterTypes());
			}
			catch (NoSuchMethodException e) {}
			
			if (constructor != null)
			{
				Class<?>[] constructorTypes = constructor.getParameterTypes();
				
				if (constructorTypes.length == invokeArgs.length)
				{
					boolean correct = true;
					
					for (int i = 0; i < invokeArgs.length; i++)
					{
						Class<?> constructorClass = constructorTypes[i];
						Class<?> invokeClass = invokeArgs[i].getClass();
						
						if (!ClassUtils.isAssignable(invokeClass, constructorClass))
						{
							correct = false;
							break;
						}
					}
					
					if (correct)
						return constructor;
				}
			}
		}
		
		throw new RuntimeException(new NoSuchMethodException(clazz.getName() + " has no constructor with parameters " + Stringify.stringifyArray(invokeArgs) + "."));
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
	
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <T> Class<T> getClass(T... array)
	{
		return (Class<T>) array.getClass().getComponentType();
	}
	
	public static <T> T nullSafeCast(Class<T> clazz, Object value)
	{
		return clazz == null ? null : clazz.cast(value);
	}
}
