package genesis.util;

import java.util.Arrays;

public class ArrayBuilder<T>
{
	private static final int INITIAL_SIZE = 4;
	
	public static <T> ArrayBuilder<T> create(T[] array)
	{
		return new ArrayBuilder<T>(array);
	}
	
	private T[] array;
	private int size = 0;
	
	public ArrayBuilder(T[] array)
	{
		int initSize = Math.max(array.length, INITIAL_SIZE);
		
		if (array.length > 0)
			initSize = Math.max(initSize, new BitMask(array.length).bits);
		
		this.array = Arrays.copyOf(array, initSize);
	}
	
	private void ensureCapacity(int capacity)
	{
		if (array.length < capacity)
			array = Arrays.copyOf(array, Math.max(capacity, array.length + array.length >> 1));	// Expands to approximately 1.5x length.
	}
	
	public void add(T value)
	{
		ensureCapacity(size + 1);
		array[size++] = value;
	}
	
	public void addAll(T... values)
	{
		ensureCapacity(size + values.length);
		System.arraycopy(values, 0, array, size, values.length);
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
