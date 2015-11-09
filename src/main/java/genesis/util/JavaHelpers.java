package genesis.util;

public class JavaHelpers
{
	@SuppressWarnings("unchecked")
	public static <T> Class<T> convertClass(Class<? super T> clazz)
	{
		return (Class<T>) clazz;
	}
}
