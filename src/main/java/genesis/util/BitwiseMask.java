package genesis.util;

public class BitwiseMask
{
	protected final int shift;
	protected final int mask;
	protected final int bits;
	
	public BitwiseMask(int shift, int mask)
	{
		this.shift = shift;
		this.mask = mask;
		this.bits = Integer.bitCount(mask);
	}
	
	/**
	 * Create a bitwise mask from the number of values it must be able to represent.
	 */
	public BitwiseMask(int valueCount)
	{
		// Find last used bit
		int maxBit = 0;
		int curBit = 1;
		
		while (((valueCount - 1) / (float) curBit) >= 1)
		{
			maxBit = curBit;
			curBit *= 2;
		}
		
		// Add up all used bits
		int allBits = 0;
		int bitCount = 0;
		
		for (int i = maxBit; i >= 1; i /= 2)
		{
			allBits += i;
			bitCount += 1;
		}
		
		shift = 0;
		mask = allBits;
		bits = bitCount;
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
	
	/**
	 * Gets the value encoded by this bitwise mask.
	 */
	public int encode(int baseValue, int maskValue)
	{
		return baseValue | ((maskValue & mask) << shift);
	}
	
	/**
	 * Gets the value decoded by this bitwise mask.
	 */
	public int decode(int value)
	{
		return (value >> shift) & mask;
	}
}