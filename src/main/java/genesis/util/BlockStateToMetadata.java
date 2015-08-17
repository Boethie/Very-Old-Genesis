package genesis.util;

import genesis.metadata.*;
import io.netty.buffer.ByteBuf;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.IBlockState;

import com.google.common.collect.*;

/**
 * @author Zaggy1024
 * 
 * Utilities pertaining to storage of block states and stuff.
 */
public class BlockStateToMetadata
{
	public static final BitwiseMask MAXMETAVALUE = new BitwiseMask(16);
	
	private static final HashMap<Collection<IProperty>, List<IProperty>> SORTED_PROPERTIES = Maps.newHashMap();
	private static final HashMap<IProperty, List<Comparable<?>>> SORTED_VALUES = Maps.newHashMap();
	
	public static List<IProperty> getSortedProperties(Collection<IProperty> properties)
	{
		List<IProperty> output = SORTED_PROPERTIES.get(properties);
		
		if (output == null)
		{
			output = new ArrayList<IProperty>(properties);
			Collections.sort(output, new Comparator<IProperty>()
			{
				@Override
				public int compare(IProperty prop1, IProperty prop2)
				{
					return prop1.getName().compareTo(prop2.getName());
				}
			});
			output = ImmutableList.copyOf(output);
			SORTED_PROPERTIES.put(ImmutableList.copyOf(properties), output);
		}
		
		return output;
	}
	
	public static <T extends Comparable<? super T>> List<T> getSortedValues(IProperty property)
	{
		List<T> values = (List<T>) SORTED_VALUES.get(property);
		
		if (values == null)
		{
			values = new ArrayList<T>(property.getAllowedValues());
			Collections.sort(values);
			values = ImmutableList.copyOf(values);
			SORTED_VALUES.put(property, (List<Comparable<?>>) values);
		}
		
		return values;
	}
	
	/**
	 * Gets the IBlockState represented by the metadata passed to the function, filtered by an array of properties.
	 * 
	 * @param state The state to convert to metadata.
	 * @param properties The properties to store in the metadata, in the desired order.
	 * @return The metadata to represent the IBlockState.
	 */
	public static <T extends Comparable<? super T>> int getMetaForBlockState(IBlockState state, IProperty... properties)
	{
		int metadata = 0;
		int offset = 0;
		
		for (IProperty property : properties)
		{
			List<T> values = getSortedValues(property);
			
			T value = (T) state.getValue(property);
			
			int index = values.indexOf(value);
			
			BitwiseMask mask = new BitwiseMask(values.size());
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
		return getMetaForBlockState(state, getSortedProperties(state.getProperties().keySet()).toArray(new IProperty[0]));
	}
	
	/**
	 * Gets the IBlockState represented by the metadata passed to the function, filtered by an array of properties.
	 * 
	 * @param state The state to base the new state off of (will usually be Block.getDefaultState()).
	 * @param metadata The metadata to restore to an IBlockState.
	 * @param properties The properties to restore from the metadata, in the order they were passed to getMetaForBlockState.
	 * @return The restored IBlockState.
	 */
	public static <T extends Comparable<? super T>> IBlockState getBlockStateFromMeta(IBlockState state, int metadata, IProperty... properties)
	{
		int offset = 0;
		
		for (IProperty property : properties)
		{
			List<T> values = getSortedValues(property);
			
			BitwiseMask mask = new BitwiseMask(values.size());
			int metaValue = (metadata & (mask.getMask() << offset)) >> offset;
			
			T propValue = values.get(metaValue);
			
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
		return getBlockStateFromMeta(state, metadata, getSortedProperties(state.getProperties().keySet()).toArray(new IProperty[0]));
	}
	
	/**
	 * Gets the number of possible values after the provided properties have been stored in metadata.
	 * Used to determine how many variants a block can store after storing other properties (like facing direction).
	 * 
	 * @param properties The properties that must be stored.
	 * @return Number of possible values.
	 */
	public static int getMetadataLeftAfter(IProperty... properties)
	{
		int bitsLeft = MAXMETAVALUE.getBitCount();
		
		for (IProperty property : properties)
		{
			BitwiseMask mask = new BitwiseMask(property.getAllowedValues().size());
			bitsLeft -= mask.getBitCount();
		}
		
		if (bitsLeft > 0)
		{
			int vals = (int) Math.pow(2, bitsLeft);
			return vals;
		}
		
		return 1;
	}
	
	public static void serializeBlockState(IBlockState inputState, ByteBuf buf)
	{
		int stateID = Block.getStateId(inputState);
		buf.writeInt(stateID);
		IBlockState metaState = Block.getStateById(stateID);
		IBlockState defaultState = inputState.getBlock().getDefaultState();
		
		List<IProperty> propertyList = getSortedProperties(defaultState.getProperties().keySet());
		
		for (IProperty property : propertyList)
		{
			if (metaState.getValue(property).equals(defaultState.getValue(property)))
			{
				buf.writeInt(getSortedValues(property).indexOf(inputState.getValue(property)));
			}
		}
	}
	
	public static IBlockState deserializeBlockState(ByteBuf buf)
	{
		IBlockState metaState = Block.getStateById(buf.readInt());
		IBlockState outState = metaState;
		IBlockState defaultState = metaState.getBlock().getDefaultState();

		List<IProperty> propertyList = getSortedProperties(defaultState.getProperties().keySet());
		
		for (IProperty property : (Collection<IProperty>) metaState.getProperties().keySet())
		{
			if (metaState.getValue(property).equals(defaultState.getValue(property)))
			{
				outState = outState.withProperty(property, getSortedValues(property).get(buf.readInt()));
			}
		}
		
		return outState;
	}
}
