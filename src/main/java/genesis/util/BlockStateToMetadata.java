package genesis.util;

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
@SuppressWarnings("unchecked")
public class BlockStateToMetadata
{
	public static final BitMask MAXMETAVALUE = BitMask.forValueCount(16);
	
	private static final Map<Collection<IProperty>, List<IProperty>> SORTED_PROPERTIES = Maps.newHashMap();
	
	private static final Map<Collection<? extends Comparable<?>>, Comparator<?>> VALUES_SORTERS = Maps.newHashMap();
	private static final Map<Collection<? extends Comparable<?>>, List<? extends Comparable<?>>> VALUES_SORTED = Maps.newHashMap();
	
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
					// Special case "variant" properties to always be last so that if we add variants it doesn't mess up loading old worlds.
					boolean prop1Var = prop1.getName().equals("variant");
					boolean prop2Var = prop2.getName().equals("variant");
					
					if (prop1Var && prop2Var)
						return 0;
					else if (prop1Var)
						return 1;
					else if (prop2Var)
						return -1;
					
					return prop1.getName().compareTo(prop2.getName());
				}
			});
			output = ImmutableList.copyOf(output);
			SORTED_PROPERTIES.put(ImmutableList.copyOf(properties), output);
		}
		
		return output;
	}
	
	public static <T extends Comparable<? super T>> void setSorter(Collection<T> values, Comparator<T> sorter)
	{
		VALUES_SORTERS.put(ImmutableSet.copyOf(values), sorter);
	}
	
	public static <T extends Comparable<? super T>> List<T> getSortedValues(IProperty property)
	{
		Collection<T> unsortedValues = property.getAllowedValues();
		Collection<T> unsortedValuesKey = ImmutableSet.copyOf(unsortedValues);
		List<T> sortedValues = (List<T>) VALUES_SORTED.get(unsortedValues);
		
		if (sortedValues == null)
		{
			boolean hasSorter = VALUES_SORTERS.containsKey(unsortedValues);
			
			if (unsortedValues instanceof List<?> && hasSorter)
			{	// Values have an order that we can use.
				VALUES_SORTED.put(unsortedValuesKey, ImmutableList.copyOf(unsortedValues));
			}
			else
			{	// We have to sort the values.
				sortedValues = new ArrayList<T>(unsortedValues);
				
				if (hasSorter)
				{
					Collections.sort(sortedValues, (Comparator<T>) VALUES_SORTERS.get(unsortedValuesKey));
				}
				else
				{
					Collections.sort(sortedValues);
				}
				
				sortedValues = ImmutableList.copyOf(sortedValues);
				VALUES_SORTED.put(unsortedValues, sortedValues);
			}
		}
		
		return sortedValues;
	}
	
	private static final class MetadataStruct
	{
		int metadata = 0;
		int offset = 0;
	}
	
	private static <T extends Comparable<? super T>> void addMetaForProperty(MetadataStruct struct, IBlockState state, IProperty property)
	{
		T value = (T) state.getValue(property);
		List<T> values = getSortedValues(property);
		int index = values.indexOf(value);
		
		BitMask mask = BitMask.forValueCount(values.size(), struct.offset);
		struct.metadata = mask.encode(struct.metadata, index);
		
		struct.offset += mask.getBitCount();
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
		MetadataStruct struct = new MetadataStruct();
		
		for (IProperty property : properties)
		{
			addMetaForProperty(struct, state, property);
		}
		
		if (struct.offset > MAXMETAVALUE.getBitCount())
		{
			throw new RuntimeException("Attempted to store an IBlockState that requires " + struct.offset + " bits in " + MAXMETAVALUE.getBitCount() + " bits of metadata");
		}
		
		return struct.metadata;
	}
	
	/**
	 * Gets the IBlockState represented by the metadata passed to the function.
	 * 
	 * @param state The state to convert to metadata.
	 * @return The metadata to represent the IBlockState.
	 */
	public static int getMetaForBlockState(IBlockState state)
	{
		return getMetaForBlockState(state, (IProperty[]) getSortedProperties(state.getProperties().keySet()).toArray(new IProperty[0]));
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
			
			BitMask mask = BitMask.forValueCount(values.size(), offset);
			int metaValue = mask.decode(metadata);
			
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
		return getBlockStateFromMeta(state, metadata, (IProperty[]) getSortedProperties(state.getProperties().keySet()).toArray(new IProperty[0]));
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
			BitMask mask = BitMask.forValueCount(property.getAllowedValues().size());
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
