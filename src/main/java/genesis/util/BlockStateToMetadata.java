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

		/* Create a bitwise mask from the number of values it must be able to represent. */
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

		/* Get the bitwise mask. */
		public int getMask()
		{
			return mask;
		}

		/* Get the number of bits used by the mask. */
		public int getBitCount()
		{
			return bits;
		}
	}

	public static final HashBiMap<Comparable, Integer> valueToMetaRemap = HashBiMap.create();

	static
	{
		valueToMetaRemap.put(false, 0);
		valueToMetaRemap.put(true, 1);
	}

	public static int getMetaForBlockState(IBlockState state, IProperty[] properties)
	{
		int metadata = 0;
		int offset = 0;

		for (IProperty property : properties)
		{
			ImmutableSet values = (ImmutableSet) property.getAllowedValues();

			Comparable value = state.getValue(property);

			int index = 0;

			// Remap undesirable values to desired values (false = metadata 0, true = metadata 1)
			if (valueToMetaRemap.containsKey(value))
			{
				index = valueToMetaRemap.get(value);
			}
			else
			{
				index = values.asList().indexOf(value);
			}

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

	public static int getMetaForBlockState(IBlockState state)
	{
		return getMetaForBlockState(state, (IProperty[]) state.getProperties().keySet().toArray(new IProperty[0]));
	}

	public static IBlockState getBlockStateFromMeta(IBlockState state, IProperty[] properties, int metadata)
	{
		int offset = 0;

		for (IProperty property : properties)
		{
			ImmutableSet values = (ImmutableSet) property.getAllowedValues();

			BitwiseMask mask = new BitwiseMask(values.size() - 1);
			int metaValue = (metadata & (mask.getMask() << offset)) >> offset;

			Comparable propValue;

			// Remap undesirable values to desired values (metadata 0 = false, metadata 1 = true)
			if (valueToMetaRemap.containsValue(metaValue))
			{
				propValue = valueToMetaRemap.inverse().get(metaValue);
			}
			else
			{
				propValue = (Comparable) values.asList().get(metaValue);
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

	public static IBlockState getBlockStateFromMeta(IBlockState state, int metadata)
	{
		return getBlockStateFromMeta(state, (IProperty[]) state.getProperties().keySet().toArray(new IProperty[0]), metadata);
	}
}
