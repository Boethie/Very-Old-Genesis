package genesis.util;

import java.util.Arrays;

public final class ArrayBuilder<T>
{
	private static final int INITIAL_SIZE = 0;
	
	public static <T> ArrayBuilder<T> create(T[] array)
	{
		return new ArrayBuilder<T>(array);
	}
	
	private T[] array;
	private int size = 0;
	
	public ArrayBuilder(T[] array)
	{
		this.array = Arrays.copyOf(array, Math.max(array.length, INITIAL_SIZE));
	}
	
	private int  grow(int capacity)
	{
		int index = size;
		size = capacity;
		
		if (array.length < capacity)
		{
			capacity = Math.max(capacity, array.length);
			capacity = capacity + (capacity >> 1);	// Expands to approximately 1.5x length.
			array = Arrays.copyOf(array, capacity);
		}
		
		return index;
	}
	
	public ArrayBuilder<T> add(T value)
	{
		int index = grow(size + 1);
		array[index] = value;
		return this;
	}
	
	@SafeVarargs
	public final ArrayBuilder<T> addAll(T... values)
	{
		int index = grow(size + values.length);
		System.arraycopy(values, 0, array, index, values.length);
		return this;
	}
	
	public int size()
	{
		return size;
	}
	
	public T[] build()
	{
		return Arrays.copyOf(array, size);
	}
}
