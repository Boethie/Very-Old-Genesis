package genesis.util.blocks;

import java.util.*;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

public class FacingProperties<V extends Comparable<V>> implements Iterable<FacingProperties.Entry<V>>
{
	public static <V extends Comparable<V>> FacingProperties<V> create(Function<EnumFacing, IProperty<V>> function)
	{
		return new FacingProperties<>(function);
	}
	
	public static <V extends Comparable<V>> FacingProperties<V> createHorizontal(Function<EnumFacing, IProperty<V>> function)
	{
		return new FacingProperties<>((f) -> f.getAxis() != EnumFacing.Axis.Y ? function.apply(f) : null);
	}
	
	private final Map<EnumFacing, IProperty<V>> map;
	
	public final IProperty<V> up;
	public final IProperty<V> down;
	public final IProperty<V> north;
	public final IProperty<V> south;
	public final IProperty<V> east;
	public final IProperty<V> west;
	
	private FacingProperties(Function<EnumFacing, IProperty<V>> propertyFunc)
	{
		ImmutableMap.Builder<EnumFacing, IProperty<V>> builder = ImmutableMap.builder();
		
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			IProperty<V> property = propertyFunc.apply(facing);
			
			if (property != null)
				builder.put(facing, property);
		}
		
		map = builder.build();
		
		up = map.get(EnumFacing.UP);
		down = map.get(EnumFacing.DOWN);
		north = map.get(EnumFacing.NORTH);
		south = map.get(EnumFacing.SOUTH);
		east = map.get(EnumFacing.EAST);
		west = map.get(EnumFacing.WEST);
	}
	
	public boolean has(EnumFacing facing)
	{
		return map.containsKey(facing);
	}
	
	public boolean has(IProperty<V> property)
	{
		return map.containsValue(property);
	}
	
	public IProperty<V> get(EnumFacing facing)
	{
		return map.get(facing);
	}
	
	public IBlockState stateWith(IBlockState state, Function<EnumFacing, V> valueFunc)
	{
		for (Entry<V> entry : this)
		{
			state = state.withProperty(entry.property, valueFunc.apply(entry.facing));
		}
		
		return state;
	}
	
	public IBlockState stateWith(IBlockState state, V value)
	{
		return stateWith(state, (f) -> value);
	}
	
	public IBlockState rotate(IBlockState state, Rotation rotation)
	{
		IBlockState out = state;
		
		for (Entry<V> entry : this)
			out = out.withProperty(get(rotation.rotate(entry.facing)), state.getValue(entry.property));
		
		return out;
	}
	
	public IBlockState mirror(IBlockState state, Mirror mirror)
	{
		switch (mirror)
		{
		case LEFT_RIGHT:
			return state.withProperty(north, state.getValue(south)).withProperty(south, state.getValue(north));
		case FRONT_BACK:
			return state.withProperty(east, state.getValue(west)).withProperty(west, state.getValue(east));
		default:
			return state;
		}
	}
	
	public Collection<IProperty<V>> properties()
	{
		return map.values();
	}
	
	public Set<EnumFacing> facings()
	{
		return map.keySet();
	}
	
	@Override
	public Iterator<Entry<V>> iterator()
	{
		return map.entrySet().stream().map((e) -> new Entry<>(e.getKey(), e.getValue())).iterator();
	}
	
	@SafeVarargs
	public final IProperty<V>[] toArray(IProperty<V>... array)
	{
		return properties().toArray(array);
	}
	
	public IProperty<?>[] toArrayWith(IProperty<?>... properties)
	{
		return ArrayUtils.addAll(toArray(), properties);
	}
	
	public static class Entry<V extends Comparable<V>>
	{
		public final EnumFacing facing;
		public final IProperty<V> property;
		
		private Entry(EnumFacing facing, IProperty<V> property)
		{
			this.facing = facing;
			this.property = property;
		}
	}
}
