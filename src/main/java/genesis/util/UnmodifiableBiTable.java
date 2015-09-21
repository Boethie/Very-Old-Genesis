package genesis.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Table;

public class UnmodifiableBiTable<R, C, V> implements BiTable<R, C, V>
{
	protected final BiTable<R, C, V> backingTable;
	
	public UnmodifiableBiTable(BiTable<R, C, V> backingTable)
	{
		this.backingTable = backingTable;
	}
	
	@Override
	public boolean contains(Object rowKey, Object columnKey)
	{
		return backingTable.contains(rowKey, columnKey);
	}
	
	@Override
	public boolean containsRow(Object rowKey)
	{
		return backingTable.containsRow(rowKey);
	}
	
	@Override
	public boolean containsColumn(Object columnKey)
	{
		return backingTable.containsColumn(columnKey);
	}
	
	@Override
	public boolean containsValue(Object value)
	{
		return backingTable.containsValue(value);
	}
	
	@Override
	public V get(Object rowKey, Object columnKey)
	{
		return backingTable.get(rowKey, columnKey);
	}

	@Override
	public V get(genesis.util.BiTable.Key<?, ?> key)
	{
		return backingTable.get(key);
	}
	
	@Override
	public boolean isEmpty()
	{
		return backingTable.isEmpty();
	}
	
	@Override
	public int size()
	{
		return backingTable.size();
	}
	
	@Deprecated
	@Override
	public void clear()
	{
		throw new UnsupportedOperationException("unmodifiable");
	}

	@Deprecated
	@Override
	public V put(R rowKey, C columnKey, V value)
	{
		throw new UnsupportedOperationException("unmodifiable");
	}

	@Deprecated
	@Override
	public void putAll(Table<? extends R, ? extends C, ? extends V> table)
	{
		throw new UnsupportedOperationException("unmodifiable");
	}

	@Deprecated
	@Override
	public V remove(Object rowKey, Object columnKey)
	{
		throw new UnsupportedOperationException("unmodifiable");
	}
	
	@Deprecated
	@Override
	public Key<R, C> removeValue(Object value)
	{
		throw new UnsupportedOperationException("unmodifiable");
	}
	
	@Override
	public Map<C, V> row(R rowKey)
	{
		return Collections.unmodifiableMap(backingTable.row(rowKey));
	}
	
	@Override
	public Map<R, V> column(C columnKey)
	{
		return Collections.unmodifiableMap(backingTable.column(columnKey));
	}
	
	@Override
	public Set<Cell<R, C, V>> cellSet()
	{
		return Collections.unmodifiableSet(backingTable.cellSet());
	}
	
	@Override
	public Set<R> rowKeySet()
	{
		return Collections.unmodifiableSet(backingTable.rowKeySet());
	}
	
	@Override
	public Set<C> columnKeySet()
	{
		return Collections.unmodifiableSet(backingTable.columnKeySet());
	}
	
	@Override
	public Collection<V> values()
	{
		return Collections.unmodifiableCollection(backingTable.values());
	}
	
	@Override
	public Map<R, Map<C, V>> rowMap()
	{
		return Collections.unmodifiableMap(backingTable.rowMap());
	}
	
	@Override
	public Map<C, Map<R, V>> columnMap()
	{
		return Collections.unmodifiableMap(backingTable.columnMap());
	}
	
	@Override
	public Key<R, C> getKey(Object value)
	{
		return backingTable.getKey(value);
	}
	
	@Override
	public String toString()
	{
		return backingTable.toString();
	}
}
