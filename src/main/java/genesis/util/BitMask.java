package genesis.util;

/**
 * @author Zaggy1024
 */
public class BitMask
{
	public static BitMask forValueCount(int values, int shift)
	{
		return new BitMask(values == 1 ? 0 : (values - 1) | 1, shift);
	}
	
	public static BitMask forValueCount(int values)
	{
		return forValueCount(values, 0);
	}
	
	protected final int shift;
	protected final int mask;
	protected final int bits;
	
	/**
	 * Creates a BitwiseMask from this mask and shift position. This will set all bits between the highest and lowest of the mask input to
	 * make sure that the mask is contiguous.
	 */
	public BitMask(int maskIn, int shift)
	{
		if (maskIn == 0)
		{
			this.mask = maskIn;
			this.shift = shift;
			this.bits = 0;
		}
		else
		{
			int firstBit = Integer.lowestOneBit(maskIn);
			int lastBit = Integer.highestOneBit(maskIn);
			int firstBitPos = Integer.numberOfTrailingZeros(firstBit);
			
			this.mask = (((lastBit - 1) | lastBit) & -firstBit) >> firstBitPos;
			this.shift = shift + firstBitPos;
			this.bits = Integer.bitCount(mask);
		}
	}
	
	public BitMask(int mask)
	{
		this(mask, 0);
	}
	
	/**
	 * Get the bitwise mask.
	 */
	public int getMask()
	{
		return mask;
	}
	
	/**
	 * Get the number of bits used by the mask.
	 */
	public int getBitCount()
	{
		return bits;
	}
	
	public int getShifted(int value)
	{
		return value << shift;
	}
	
	public int getShiftedMask()
	{
		return getShifted(mask);
	}
	
	/**
	 * Returns the baseValue encoded with maskValue at this bit mask's location.
	 */
	public int encode(int baseValue, int maskValue)
	{
		int shiftMask = getShiftedMask();
		return (baseValue & ~shiftMask) | (getShifted(maskValue) & shiftMask);
	}
	
	/**
	 * Gets the value encoded by this bitwise mask.
	 */
	public int decode(int value)
	{
		return (value >> shift) & mask;
	}
	
	/*// Unit tests
	protected static void throwIf(boolean value)
	{
		if (value)
		{
			throw new RuntimeException("Unit tests failed.");
		}
	}
	
	static
	{
		int test = 2436;
		int testMask = 1023;
		int encTest = 546;
		BitwiseMask testMaskObj = new BitwiseMask(test);
		throwIf(testMaskObj.getBitCount() != 10);
		throwIf(testMaskObj.getMask() != testMask);
		throwIf(testMaskObj.getMask() != testMask);
		throwIf(testMaskObj.decode(testMaskObj.encode(0, encTest)) != encTest);
		
		test = 32;
		testMask = 1;
		encTest = 1;
		testMaskObj = new BitwiseMask(test);
		throwIf(testMaskObj.getBitCount() != 1);
		throwIf(testMaskObj.getMask() != testMask);
		throwIf(testMaskObj.getMask() != testMask);
		throwIf(testMaskObj.decode(testMaskObj.encode(0, encTest)) != encTest);

		test = 402980;
		testMask = 131071;
		encTest = 1;
		testMaskObj = new BitwiseMask(test);
		throwIf(testMaskObj.getBitCount() != 17);
		throwIf(testMaskObj.getMask() != testMask);
		throwIf(testMaskObj.getMask() != testMask);
		throwIf(testMaskObj.decode(testMaskObj.encode(0, encTest)) != encTest);
	}*/
}