package genesis.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.google.common.base.Objects;
import com.google.common.collect.*;

public class HashBiTable<R, C, V> implements BiTable<R, C, V>
{
	public class RowKeyIterator implements Iterator<C>
	{
		protected final R rowKey;
		protected final Iterator<C> iter;
		protected C current;
		protected C next;
		
		protected RowKeyIterator(R rowKey)
		{
			this.rowKey = rowKey;
			this.iter = HashBiTable.this.columnSet.iterator();
			prepareNext();
		}
		
		protected void prepareNext()
		{
			C next = null;
			
			while (iter.hasNext())
			{
				next = iter.next();
				
				if (HashBiTable.this.contains(rowKey, next))
				{
					break;
				}
			}
			
			this.current = this.next;
			this.next = next;
		}
		
		@Override
		public boolean hasNext()
		{
			return next != null;
		}

		@Override
		public C next()
		{
			prepareNext();
			return current;
		}
		
		@Override
		public void remove()
		{
			HashBiTable.this.remove(rowKey, current);
		}
	}

	public class RowKeySet extends AbstractSet<C>
	{
		protected final R rowKey;
		
		protected RowKeySet(R rowKey)
		{
			this.rowKey = rowKey;
		}
		
		public int size()
		{
			int size = 0;
			
			for (C columnKey : HashBiTable.this.columnSet)
			{
				if (HashBiTable.this.contains(rowKey, columnKey))
				{
					size++;
				}
			}
			
			return size;
		}
		
		public void clear()
		{
			HashBiTable.this.clear();
		}
		
		public RowKeyIterator iterator()
		{
			return new RowKeyIterator(rowKey);
		}
		
		public boolean contains(Object columnKey)
		{
			return HashBiTable.this.contains(rowKey, columnKey);
		}
		
		public boolean remove(Object columnKey)
		{
			return HashBiTable.this.remove(rowKey, columnKey) != null;
		}
		
		public Spliterator<C> spliterator()
		{
			return null;
		}
		
		public void forEach(Consumer<? super C> action)
		{
			int oldModCount = HashBiTable.this.modCount;
			RowKeyIterator iter = iterator();
			
			while (iter.hasNext())
			{
				C columnKey = iter.next();
				action.accept(columnKey);
			}
			
			if (oldModCount != HashBiTable.this.modCount)
			{
				throw new ConcurrentModificationException();
			}
		}
	}
	
	public class RowEntry implements Map.Entry<C, V>
	{
		protected final R rowKey;
		protected final C columnKey;
		protected final V value;
		
		protected RowEntry(R rowKey, C columnKey)
		{
			this.rowKey = rowKey;
			this.columnKey = columnKey;
			this.value = HashBiTable.this.get(rowKey, columnKey);
		}
		
		@Override
		public C getKey()
		{
			return columnKey;
		}

		@Override
		public V getValue()
		{
			return value;
		}

		@Override
		public V setValue(V newValue)
		{
			return HashBiTable.this.put(rowKey, columnKey, newValue);
		}
	}
	
	public class RowEntryIterator implements Iterator<Map.Entry<C, V>>
	{
		protected final R rowKey;
		protected final Iterator<C> iter;
		protected RowEntry current = null;
		protected RowEntry next = null;
		
		protected RowEntryIterator(R rowKey)
		{
			this.rowKey = rowKey;
			this.iter = HashBiTable.this.columnSet.iterator();
			prepareNext();
		}
		
		protected void prepareNext()
		{
			RowEntry nextEntry = null;
			
			while (iter.hasNext())
			{
				C nextCol = iter.next();
				
				if (HashBiTable.this.contains(rowKey, nextCol))
				{
					nextEntry = new RowEntry(rowKey, nextCol);
					break;
				}
			}
			
			this.current = this.next;
			this.next = nextEntry;
		}
		
		@Override
		public boolean hasNext()
		{
			return next != null;
		}

		@Override
		public RowEntry next()
		{
			prepareNext();
			return current;
		}
		
		@Override
		public void remove()
		{
			HashBiTable.this.remove(rowKey, current.getKey());
		}
	}
	
	public class RowEntrySet extends AbstractSet<Entry<C, V>>
	{
		protected final R rowKey;
		
		protected RowEntrySet(R rowKey)
		{
			this.rowKey = rowKey;
		}
		
		@Override
		public int size()
		{
			int size = 0;
			
			for (Entry<C, V> entry : this)
			{
				size++;
			}
			
			return size;
		}

		@Override
		public boolean contains(Object columnKey)
		{
			return HashBiTable.this.contains(rowKey, columnKey);
		}

		@Override
		public RowEntryIterator iterator()
		{
			return new RowEntryIterator(rowKey);
		}

		@Override
		public boolean add(Map.Entry<C, V> entry)
		{
			return HashBiTable.this.put(rowKey, entry.getKey(), entry.getValue()) == null;
		}

		@Override
		public boolean remove(Object o)
		{
			if (o instanceof HashBiTable.RowEntry)
			{
				RowEntry entry = (RowEntry) o;
				return HashBiTable.this.remove(rowKey, entry.getKey()) != null;
			}
			
			return false;
		}
	}
	
	public class Row implements Map<C, V>
	{
		protected final R rowKey;
		protected RowEntrySet entrySet;
		protected RowKeySet keySet;
		
		public Row(R rowKey)
		{
			this.rowKey = rowKey;
		}
		
		@Override
		public int size()
		{
			return 0;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public boolean containsKey(Object columnKey)
		{
			return HashBiTable.this.containsColumn(columnKey);
		}

		@Override
		public boolean containsValue(Object value)
		{
			return HashBiTable.this.containsValue(value);
		}

		@Override
		public V get(Object columnKey)
		{
			return HashBiTable.this.get(rowKey, columnKey);
		}

		@Override
		public V put(C columnKey, V value)
		{
			return HashBiTable.this.put(rowKey, columnKey, value);
		}

		@Override
		public V remove(Object columnKey)
		{
			return HashBiTable.this.remove(rowKey, columnKey);
		}

		@Override
		public void putAll(Map<? extends C, ? extends V> map)
		{
			for (Map.Entry<? extends C, ? extends V> entry : map.entrySet())
			{
				put(entry.getKey(), entry.getValue());
			}
		}

		@Override
		public void clear()
		{
			for (C columnKey : HashBiTable.this.columnSet)
			{
				remove(columnKey);
			}
		}

		@Override
		public RowKeySet keySet()
		{
			return new RowKeySet(rowKey);
		}

		@Override
		public RowEntrySet entrySet()
		{
			return new RowEntrySet(rowKey);
		}

		@Override
		public Collection<V> values()
		{
			return HashBiTable.this.backingMap.values();
		}
	}
	
	public class ColumnKeyIterator implements Iterator<R>
	{
		protected final C columnKey;
		protected final Iterator<R> iter;
		protected R current;
		protected R next;
		
		protected ColumnKeyIterator(C columnKey)
		{
			this.columnKey = columnKey;
			this.iter = HashBiTable.this.rowSet.iterator();
			prepareNext();
		}
		
		protected void prepareNext()
		{
			R next = null;
			
			while (iter.hasNext())
			{
				next = iter.next();
				
				if (HashBiTable.this.contains(next, columnKey))
				{
					break;
				}
			}
			
			this.current = this.next;
			this.next = next;
		}
		
		@Override
		public boolean hasNext()
		{
			return next != null;
		}

		@Override
		public R next()
		{
			prepareNext();
			return current;
		}
		
		@Override
		public void remove()
		{
			HashBiTable.this.remove(current, columnKey);
		}
	}

	public class ColumnKeySet extends AbstractSet<R>
	{
		protected final C columnKey;
		
		protected ColumnKeySet(C columnKey)
		{
			this.columnKey = columnKey;
		}
		
		public int size()
		{
			int size = 0;
			
			for (R rowKey : HashBiTable.this.rowSet)
			{
				if (HashBiTable.this.contains(rowKey, columnKey))
				{
					size++;
				}
			}
			
			return size;
		}
		
		public void clear()
		{
			HashBiTable.this.clear();
		}
		
		public ColumnKeyIterator iterator()
		{
			return new ColumnKeyIterator(columnKey);
		}
		
		public boolean contains(Object rowKey)
		{
			return HashBiTable.this.contains(rowKey, columnKey);
		}
		
		public boolean remove(Object rowKey)
		{
			return HashBiTable.this.remove(rowKey, columnKey) != null;
		}
		
		public Spliterator<R> spliterator()
		{
			return null;
		}
		
		public void forEach(Consumer<? super R> action)
		{
			int oldModCount = HashBiTable.this.modCount;
			ColumnKeyIterator iter = iterator();
			
			while (iter.hasNext())
			{
				R rowKey = iter.next();
				action.accept(rowKey);
			}
			
			if (oldModCount != HashBiTable.this.modCount)
			{
				throw new ConcurrentModificationException();
			}
		}
	}
	
	public class ColumnEntry implements Map.Entry<R, V>
	{
		protected final C columnKey;
		protected final R rowKey;
		protected final V value;
		
		protected ColumnEntry(C columnKey, R rowKey)
		{
			this.columnKey = columnKey;
			this.rowKey = rowKey;
			this.value = HashBiTable.this.get(rowKey, columnKey);
		}
		
		@Override
		public R getKey()
		{
			return rowKey;
		}

		@Override
		public V getValue()
		{
			return value;
		}

		@Override
		public V setValue(V newValue)
		{
			return HashBiTable.this.put(rowKey, columnKey, newValue);
		}
	}
	
	public class ColumnEntryIterator implements Iterator<Map.Entry<R, V>>
	{
		protected final C columnKey;
		protected final Iterator<R> iter;
		protected ColumnEntry current = null;
		protected ColumnEntry next = null;
		
		protected ColumnEntryIterator(C columnKey)
		{
			this.columnKey = columnKey;
			this.iter = HashBiTable.this.rowSet.iterator();
			prepareNext();
		}
		
		protected void prepareNext()
		{
			ColumnEntry nextEntry = null;
			
			while (iter.hasNext())
			{
				R nextRow = iter.next();
				
				if (HashBiTable.this.contains(nextRow, columnKey))
				{
					nextEntry = new ColumnEntry(columnKey, nextRow);
				}
			}
			
			this.current = this.next;
			this.next = nextEntry;
		}
		
		@Override
		public boolean hasNext()
		{
			return next != null;
		}

		@Override
		public ColumnEntry next()
		{
			prepareNext();
			return current;
		}
		
		@Override
		public void remove()
		{
			HashBiTable.this.remove(current.getKey(), columnKey);
		}
	}
	
	public class ColumnEntrySet extends AbstractSet<Entry<R, V>>
	{
		protected final C columnKey;
		
		protected ColumnEntrySet(C columnKey)
		{
			this.columnKey = columnKey;
		}
		
		@Override
		public int size()
		{
			int size = 0;
			ColumnEntryIterator iter = iterator();
			
			while (iter.hasNext())
			{
				iter.next();
				size++;
			}
			
			return size;
		}

		@Override
		public boolean contains(Object rowKey)
		{
			return HashBiTable.this.contains(rowKey, columnKey);
		}

		@Override
		public ColumnEntryIterator iterator()
		{
			return new ColumnEntryIterator(columnKey);
		}

		@Override
		public boolean add(Map.Entry<R, V> entry)
		{
			return HashBiTable.this.put(entry.getKey(), columnKey, entry.getValue()) == null;
		}

		@Override
		public boolean remove(Object o)
		{
			if (o instanceof HashBiTable.ColumnEntry)
			{
				ColumnEntry entry = (ColumnEntry) o;
				return HashBiTable.this.remove(entry.getKey(), columnKey) != null;
			}
			
			return false;
		}
	}
	
	public class Column implements Map<R, V>
	{
		protected final C columnKey;
		protected ColumnEntrySet entrySet;
		protected ColumnKeySet keySet;
		
		public Column(C columnKey)
		{
			this.columnKey = columnKey;
		}
		
		@Override
		public int size()
		{
			return 0;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public boolean containsKey(Object rowKey)
		{
			return HashBiTable.this.containsRow(rowKey);
		}

		@Override
		public boolean containsValue(Object value)
		{
			return HashBiTable.this.containsValue(value);
		}

		@Override
		public V get(Object rowKey)
		{
			return HashBiTable.this.get(rowKey, columnKey);
		}

		@Override
		public V put(R rowKey, V value)
		{
			return HashBiTable.this.put(rowKey, columnKey, value);
		}

		@Override
		public V remove(Object rowKey)
		{
			return HashBiTable.this.remove(rowKey, columnKey);
		}

		@Override
		public void putAll(Map<? extends R, ? extends V> map)
		{
			for (Map.Entry<? extends R, ? extends V> entry : map.entrySet())
			{
				put(entry.getKey(), entry.getValue());
			}
		}

		@Override
		public void clear()
		{
			for (R rowKey : HashBiTable.this.rowSet)
			{
				remove(rowKey);
			}
		}

		@Override
		public ColumnKeySet keySet()
		{
			return new ColumnKeySet(columnKey);
		}

		@Override
		public ColumnEntrySet entrySet()
		{
			return new ColumnEntrySet(columnKey);
		}

		@Override
		public Collection<V> values()
		{
			return HashBiTable.this.backingMap.values();
		}
	}

	public class Key implements BiTable.Key<R, C>
	{
		protected R row;
		protected C column;
		
		public Key(R row, C column)
		{
			this.row = row;
			this.column = column;
		}
		
		public R getRow()
		{
			return row;
		}
		
		public C getColumn()
		{
			return column;
		}
		
		public int hashCode()
		{
			return row.hashCode() + column.hashCode();
		}
		
		public boolean equals(Object other)
		{
			if (this == other)
			{
				return true;
			}
			
			if (other instanceof HashBiTable.Key)
			{
				Key otherKey = (Key) other;
				
				if (Objects.equal(getRow(), otherKey.getRow()) && Objects.equal(getColumn(), otherKey.getColumn()))
				{
					return true;
				}
			}
			
			return false;
		}
	}
	
	protected HashBiMap<Key, V> backingMap = HashBiMap.create();
	protected HashMap<R, Row> rowsMap = new HashMap();
	protected HashMap<C, Column> columnsMap = new HashMap();
	protected HashSet<R> rowSet = new HashSet();
	protected HashSet<C> columnSet = new HashSet();
	protected int modCount = 0;
	
	public HashBiTable()
	{
	}

	@Override
	public boolean containsRow(Object row)
	{
		return rowSet.contains(row);
	}

	@Override
	public boolean containsColumn(Object column)
	{
		return columnSet.contains(column);
	}
	
	protected boolean isValidKey(Object rowKey, Object columnKey)
	{
		return containsRow(rowKey) && containsColumn(columnKey);
	}

	@Override
	public boolean contains(Object rowKey, Object columnKey)
	{
		if (isValidKey(rowKey, columnKey))
		{
			return backingMap.containsKey(new Key((R) rowKey, (C) columnKey));
		}
		
		return false;
	}

	@Override
	public V get(Object rowKey, Object columnKey)
	{
		if (isValidKey(rowKey, columnKey))
		{
			return backingMap.get(new Key((R) rowKey, (C) columnKey));
		}
		
		return null;
	}

	@Override
	public Key getKey(Object value)
	{
		return backingMap.inverse().get(value);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return backingMap.containsValue(value);
	}

	@Override
	public boolean isEmpty()
	{
		return backingMap.isEmpty();
	}

	@Override
	public int size()
	{
		return backingMap.size();
	}

	@Override
	public void clear()
	{
		backingMap.clear();
	}
	
	protected void onChange()
	{
	    modCount++;
	    
		rowSet.clear();
		columnSet.clear();
		
		for (Key key : backingMap.keySet())
		{
			rowSet.add(key.getRow());
			columnSet.add(key.getColumn());
		}
	}

	@Override
	public V put(R rowKey, C columnKey, V value)
	{
	    checkNotNull(rowKey);
	    checkNotNull(columnKey);
	    checkNotNull(value);
	    
	    V old = backingMap.put(new Key(rowKey, columnKey), value);
	    onChange();
		return old;
	}

	@Override
	public V remove(Object rowKey, Object columnKey)
	{
		if (isValidKey(rowKey, columnKey))
		{
			V old = backingMap.remove(new Key((R) rowKey, (C) columnKey));
		    onChange();
			return old;
		}
		
		return null;
	}

	@Override
	public void putAll(Table<? extends R, ? extends C, ? extends V> table)
	{
		for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet())
		{
			put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
		}
	}

	@Override
	public Map<C, V> row(R rowKey)
	{
		if (containsRow(rowKey))
		{
			Row row = rowsMap.get(rowKey);
			
			if (row == null)
			{
				row = new Row((R) rowKey);
			}
			
			return row;
		}
		
		return null;
	}

	@Override
	public Map<R, V> column(C columnKey)
	{
		if (containsRow(columnKey))
		{
			Column column = columnsMap.get(columnKey);
			
			if (column == null)
			{
				column = new Column((C) columnKey);
			}
			
			return column;
		}
		
		return null;
	}

	@Override
	public Set<Cell<R, C, V>> cellSet()
	{
		return null;
	}

	@Override
	public Set<R> rowKeySet()
	{
		return ImmutableSet.copyOf(rowSet);
	}

	@Override
	public Set<C> columnKeySet()
	{
		return ImmutableSet.copyOf(columnSet);
	}

	@Override
	public Set<V> values()
	{
		return backingMap.values();
	}

	@Override
	public Map<R, Map<C, V>> rowMap()
	{
		throw new UnsupportedOperationException("rowMap");
	}

	@Override
	public Map<C, Map<R, V>> columnMap()
	{
		throw new UnsupportedOperationException("columnMap");
	}
}
