package genesis.util;

import java.util.Arrays;

public class ArrayBuilder<T>
{
	public static <T> ArrayBuilder<T> create(T[] array)
	{
		return new ArrayBuilder<T>(array);
	}
	
	private T[] array;
	private int size = 0;
	private int growSize;
	
	public ArrayBuilder(T[] array)
	{
		if (array.length == 0)
		{
			this.array = Arrays.copyOf(array, 4);
			growSize = 8;
		}
		else
		{
			this.array = array;
			growSize = new BitMask(array.length).bits << 1;
			
			if (growSize <= array.length)
				throw new RuntimeException("ArrayBuilder got wrong growSize.");
		}
	}
	
	private void grow()
	{
		array = Arrays.copyOf(array, growSize);
		growSize <<= 1;
	}
	
	public void add(T value)
	{
		if (size > array.length)
			grow();
		
		array[size++] = value;
	}
	
	public T[] build()
	{
		return Arrays.copyOf(array, size);
	}
}
