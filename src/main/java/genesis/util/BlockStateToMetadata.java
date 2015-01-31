package genesis.util;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableSet;

public class BlockStateToMetadata
{
	public static final BitwiseMask MAXMETAVALUE = new BitwiseMask(15);

	public static class BitwiseMask
	{
		private int mask;
		private int bits;

		/**
		 * Create a bitwise mask from the number of values it must be able to represent.
		 */
		public BitwiseMask(int valueCount)
		{
			// Find last used bit
			int maxBit = 0;
			int curBit = 1;

			while ((valueCount / (float) curBit) >= 1)
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
	}

	public static final HashBiMap<Comparable, Comparable> valueToMetaRemap = HashBiMap.create();

	static
	{
		valueToMetaRemap.put(false, true);
		valueToMetaRemap.put(true, false);
	}

	/**
	 * Gets the IBlockState represented by the metadata passed to the function, filtered by an array of properties.
	 * 
	 * @param state The state to convert to metadata.
	 * @param properties The properties to store in the metadata, in the desired order.
	 * @return The metadata to represent the IBlockState.
	 */
	public static int getMetaForBlockState(IBlockState state, IProperty... properties)
	{
		int metadata = 0;
		int offset = 0;

		for (IProperty property : properties)
		{
			ImmutableSet values = (ImmutableSet) property.getAllowedValues();

			Comparable value = state.getValue(property);
			
			// Remap undesirable values to desired values (false = metadata 0, true = metadata 1)
			if (valueToMetaRemap.containsKey(value))
			{
				value = valueToMetaRemap.get(value);
			}

			int index = values.asList().indexOf(value);

			BitwiseMask mask = new BitwiseMask(values.size() - 1);
			metadata |= (index & mask.getMask()) << offset;

			offset += mask.getBitCount();
		}

		if (offset > MAXMETAVALUE.getBitCount())
		{
			throw new RuntimeException("Attempted to store an IBlockState that requires " + offset + " bits in " + MAXMETAVALUE.getBitCount() + " bits of metadata");
		}

		return metadata;
	}

	/**
	 * Gets the IBlockState represented by the metadata passed to the function.
	 * 
	 * @param state The state to convert to metadata.
	 * @return The metadata to represent the IBlockState.
	 */
	public static int getMetaForBlockState(IBlockState state)
	{
		return getMetaForBlockState(state, (IProperty[]) state.getProperties().keySet().toArray(new IProperty[0]));
	}

	/**
	 * Gets the IBlockState represented by the metadata passed to the function, filtered by an array of properties.
	 * 
	 * @param state The state to base the new state off of (will usually be Block.getDefaultState()).
	 * @param metadata The metadata to restore to an IBlockState.
	 * @param properties The properties to restore from the metadata, in the order they were passed to getMetaForBlockState.
	 * @return The restored IBlockState.
	 */
	public static IBlockState getBlockStateFromMeta(IBlockState state, int metadata, IProperty... properties)
	{
		int offset = 0;

		for (IProperty property : properties)
		{
			ImmutableSet values = (ImmutableSet) property.getAllowedValues();

			BitwiseMask mask = new BitwiseMask(values.size() - 1);
			int metaValue = (metadata & (mask.getMask() << offset)) >> offset;

			Comparable propValue = (Comparable) values.asList().get(metaValue);

			// Remap undesirable values to desired values (metadata 0 = false, metadata 1 = true)
			if (valueToMetaRemap.containsValue(propValue))
			{
				propValue = valueToMetaRemap.inverse().get(propValue);
			}

			state = state.withProperty(property, propValue);

			offset += mask.getBitCount();
		}

		if (offset > MAXMETAVALUE.getBitCount())
		{
			throw new RuntimeException("Attempted to retrieve a property from an IBlockState past " + MAXMETAVALUE.getBitCount() + " bits, the maximum metadata bit count.");
		}

		return state;
	}

	/**
	 * Gets the IBlockState represented by the metadata passed to the function.
	 * 
	 * @param state The state to base the new state off of (will usually be Block.getDefaultState()).
	 * @param metadata The metadata to restore to an IBlockState.
	 * @return The restored IBlockState.
	 */
	public static IBlockState getBlockStateFromMeta(IBlockState state, int metadata)
	{
		return getBlockStateFromMeta(state, metadata, (IProperty[]) state.getProperties().keySet().toArray(new IProperty[0]));
	}
}
