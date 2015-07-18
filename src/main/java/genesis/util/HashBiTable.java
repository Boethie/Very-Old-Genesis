package genesis.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.*;

public class HashBiTable<R, C, V> implements BiTable<R, C, V>
{
	public class Key implements BiTable.Key<R, C>
	{
		protected final R row;
		protected final C column;
		
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
	
	protected final HashBiMap<Key, V> backingMap = HashBiMap.create();
	protected ImmutableSet<R> rowSet;
	protected ImmutableSet<C> columnSet;
	protected final CellSet cellSet = new CellSet();
	protected RowMap rowMap;
	protected HashMap<C, Column> columnsMap = new HashMap();
	protected int modCount = 0;
	
	public HashBiTable()
	{
		onChanged();
	}

	@Override
	public boolean containsRow(Object row)
	{
		return rowSet.contains(row);
	}

	@Override
	public boolean containsColumn(Object column)
	{
		return columnKeySet().contains(column);
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
	public V get(BiTable.Key<?, ?> key)
	{
		if (key == null)
		{
			return null;
		}
		
		return get(key.getRow(), key.getColumn());
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
	
	protected void onChanged()
	{
	    modCount++;

	    ImmutableSet.Builder<R> rowBuilder = ImmutableSet.builder();
	    ImmutableSet.Builder<C> colBuilder = ImmutableSet.builder();
		
		for (Key key : backingMap.keySet())
		{
			rowBuilder.add(key.getRow());
			colBuilder.add(key.getColumn());
		}
		
		rowSet = rowBuilder.build();
		columnSet = colBuilder.build();
	}

	@Override
	public V put(R rowKey, C columnKey, V value)
	{
	    checkNotNull(rowKey);
	    checkNotNull(columnKey);
	    checkNotNull(value);
	    
	    V old = backingMap.put(new Key(rowKey, columnKey), value);
	    onChanged();
		return old;
	}

	@Override
	public V remove(Object rowKey, Object columnKey)
	{
		if (isValidKey(rowKey, columnKey))
		{
			V old = backingMap.remove(new Key((R) rowKey, (C) columnKey));
		    onChanged();
			return old;
		}
		
		return null;
	}
	
	@Override
	public Key removeValue(Object value)
	{
		Key key = getKey(value);
		if (key != null)
			remove(key.getRow(), key.getColumn());
		return key;
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
		return rowMap().get(rowKey);
	}

	@Override
	public Map<R, V> column(C columnKey)
	{
		if (containsRow(columnKey))
		{
			Column column = columnsMap.get(columnKey);
			
			if (column == null)
			{
				column = new Column(columnKey);
			}
			
			return column;
		}
		
		return null;
	}

	@Override
	public Set<Cell<R, C, V>> cellSet()
	{
		return cellSet;
	}

	@Override
	public Set<R> rowKeySet()
	{
		return rowSet;
	}

	@Override
	public Set<C> columnKeySet()
	{
		return columnSet;
	}

	@Override
	public Set<V> values()
	{
		return backingMap.values();
	}

	@Override
	public Map<R, Map<C, V>> rowMap()
	{
		if (rowMap == null)
			rowMap = new RowMap();
		return rowMap;
	}

	@Override
	public Map<C, Map<R, V>> columnMap()
	{
		throw new UnsupportedOperationException("columnMap");
	}
	
	@Override
	public String toString()
	{
		return rowMap().toString();
	}
	
	public class CellSet extends AbstractSet<Cell<R, C, V>>
	{
		@Override
		public int size()
		{
			return HashBiTable.this.size();
		}

		@Override
		public boolean contains(Object o)
		{
			if (o instanceof Cell<?, ?, ?>)
			{
				Cell<?, ?, ?> cell = (Cell<?, ?, ?>) o;
				Object row = cell.getRowKey();
				Object column = cell.getColumnKey();
				Object value = cell.getValue();
				V gotValue = HashBiTable.this.get(row, column);
				
				return value == gotValue ? true : (value != null ? value.equals(gotValue) : false);
			}
			
			return false;
		}

		@Override
		public Iter iterator()
		{
			return new Iter();
		}

		@Override
		public boolean add(Cell<R, C, V> cell)
		{
			return HashBiTable.this.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue()) == null;
		}

		@Override
		public boolean remove(Object o)
		{
			if (contains(o))
			{
				Cell<?, ?, ?> cell = (Cell<?, ?, ?>) o;
				return HashBiTable.this.remove(cell.getRowKey(), cell.getColumnKey()) != null;
			}
			
			return false;
		}
		
		public class Iter extends SimpleIterator<Cell<R, C, V>>
		{
			protected final Iterator<Entry<Key, V>> iter = HashBiTable.this.backingMap.entrySet().iterator();
			
			@Override protected Cell<R, C, V> computeNext()
			{
				if (iter.hasNext())
				{
					final Entry<Key, V> entry = iter.next();
					return Tables.immutableCell(entry.getKey().getRow(), entry.getKey().getColumn(), entry.getValue());
				}
				
				setDone();
				return null;
			}
			@Override public void remove()
			{
				Cell<R, C, V> current = peek();
				HashBiTable.this.remove(current.getRowKey(), current.getColumnKey());
			}
		}
	}
	
	public class Row extends AbstractMap<C, V>
	{
		protected final R rowKey;
		protected final KeySet keySet;
		protected final EntrySet entrySet;
		protected final Values values;
		
		public Row(R rowKey)
		{
			this.rowKey = rowKey;
			this.keySet = new KeySet();
			this.entrySet = new EntrySet();
			this.values = new Values();
		}
		
		@Override public int size() { return entrySet().size(); }
		@Override public boolean isEmpty() { return entrySet().isEmpty(); }
		@Override public boolean containsKey(Object columnKey) { return HashBiTable.this.contains(rowKey, columnKey); }
		@Override public V get(Object columnKey) { return HashBiTable.this.get(rowKey, columnKey); }
		@Override public V put(C columnKey, V value) { return HashBiTable.this.put(rowKey, columnKey, value); }
		@Override public V remove(Object columnKey) { return HashBiTable.this.remove(rowKey, columnKey); }
		@Override public void clear()
		{
			for (C columnKey : HashBiTable.this.columnKeySet())
				remove(columnKey);
		}
		@Override public KeySet keySet() { return keySet; }
		@Override public EntrySet entrySet() { return entrySet; }
		@Override public Collection<V> values() { return values; }
		
		public class KeySet extends AbstractSet<C>
		{
			@Override public int size()
			{
				int size = 0;
				
				for (C columnKey : HashBiTable.this.columnKeySet())
				{
					if (contains(columnKey))
					{
						size++;
					}
				}
				
				return size;
			}
			
			@Override public void clear() { Row.this.clear(); }
			@Override public boolean contains(Object columnKey) { return Row.this.containsKey(columnKey); }
			@Override public boolean remove(Object columnKey) { return Row.this.remove(columnKey) != null; }
			
			@Override public Iter iterator() { return new Iter(); }
			
			public class Iter extends SimpleIterator<C>
			{
				protected final Iterator<C> iter = HashBiTable.this.columnKeySet().iterator();
				
				@Override protected C computeNext()
				{
					while (iter.hasNext())
					{
						C check = iter.next();
						
						if (Row.this.containsKey(check))
							return check;
					}
					
					setDone();
					return null;
				}
				@Override public void remove() { Row.this.remove(peek()); }
			}
		}
		
		public class EntrySet extends AbstractSet<Entry<C, V>>
		{
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
				return Row.this.containsKey(columnKey);
			}

			@Override
			public Iter iterator()
			{
				return new Iter();
			}
			
			public class Iter extends SimpleIterator<Entry<C, V>>
			{
				protected final Iterator<C> iter = HashBiTable.this.columnKeySet().iterator();
				
				@Override protected Entry<C, V> computeNext()
				{
					while (iter.hasNext())
					{
						final C nextColumn = iter.next();
						
						if (Row.this.containsKey(nextColumn))
						{
							return new Entry<C, V>()
							{
								@Override public C getKey() { return nextColumn; }
								@Override public V getValue() { return Row.this.get(nextColumn); }
								@Override public V setValue(V value) { return Row.this.put(nextColumn, value); }
							};
						}
					}
					
					setDone();
					return null;
				}
				@Override public void remove() { Row.this.remove(peek().getKey()); }
			}

			@Override
			public boolean add(Entry<C, V> entry)
			{
				boolean out = contains(entry.getKey());
				Row.this.put(entry.getKey(), entry.getValue());
				return out;
			}

			@Override
			public boolean remove(Object o)
			{
				if (o instanceof Entry)
				{
					Entry<?, ?> entry = (Entry<?, ?>) o;
					boolean has = entry.equals(Row.this.get(entry.getKey()));
					if (has)
						Row.this.remove(entry.getKey());
					return has;
				}
				
				return false;
			}
		}
		
		public class Values extends AbstractSet<V>
		{
			@Override public Iterator<V> iterator() { return new Iter(); }
			@Override public int size() { return Row.this.size(); }
			
			public class Iter extends SimpleIterator<V>
			{
				protected final PeekingIterator<Entry<C, V>> iter = Row.this.entrySet().iterator();
				
				@Override
				protected V computeNext()
				{
					if (iter.hasNext())
						return iter.next().getValue();
					
					setDone();
					return null;
				}
				@Override public void remove() { Row.this.remove(iter.peek().getKey()); }
			}
			
			@Override public boolean contains(Object value) { return Row.this.containsValue(value); }
			@Override public boolean remove(Object value) { return HashBiTable.this.removeValue(value) != null; }
		}
	}
	
	public class RowMap extends AbstractMap<R, Map<C, V>>
	{
		protected final HashMap<R, Row> map = new HashMap<R, Row>();
		
		@Override public int size() { return HashBiTable.this.size(); }
		@Override public boolean isEmpty() { return HashBiTable.this.isEmpty(); }
		@Override public boolean containsKey(Object key) { return HashBiTable.this.rowSet.contains(key); }
		
		@Override public boolean containsValue(Object value)
		{
			if (value instanceof Map)
			{
				Map<?, ?> map = (Map<?, ?>) value;
				
				for (Map<C, V> ourMap : values())
					if (map.equals(ourMap))
						return true;
			}
			
			return false;
		}

		@Override public Map<C, V> get(Object key)
		{
			if (containsKey(key))
			{
				R rowKey = (R) key;
				Row row = map.get(key);
				if (!map.containsKey(row))
				{
					row = new Row(rowKey);
					map.put(rowKey, row);
				}
				return row;
			}
			return null;
		}

		@Override public Map<C, V> put(R key, Map<C, V> value)
		{
			Map<C, V> out = remove(key);
			for (Entry<C, V> e : value.entrySet())
				HashBiTable.this.put(key, e.getKey(), e.getValue());
			return out;
		}

		@Override public Map<C, V> remove(Object key)
		{
			Map<C, V> row = get(key);
			ImmutableMap<C, V> out = ImmutableMap.copyOf(row);
			row.clear();
			return out;
		}

		@Override public void putAll(Map<? extends R, ? extends Map<C, V>> m)
		{
			for (Entry<? extends R, ? extends Map<C, V>> e : m.entrySet())
				put(e.getKey(), e.getValue());
		}

		@Override public void clear() { HashBiTable.this.clear(); }
		@Override public Set<R> keySet() { return HashBiTable.this.rowKeySet(); }
		
		protected Values values;
		
		@Override
		public Collection<Map<C, V>> values()
		{
			if (values == null)
				values = new Values();
			return values;
		}
		
		public class Values extends AbstractCollection<Map<C, V>>
		{
			public class Iter implements Iterator<Map<C, V>>
			{
				protected final Iterator<R> iter = HashBiTable.this.rowSet.iterator();
				@Override public boolean hasNext() { return iter.hasNext(); }
				@Override public Map<C, V> next() { return HashBiTable.this.row(iter.next()); }
				@Override public void remove() { iter.remove(); }
			}
			
			@Override public Iterator<Map<C, V>> iterator() { return new Iter(); }
			@Override public int size() { return HashBiTable.this.rowSet.size(); }
		}
		
		@Override
		public Set<Entry<R, Map<C, V>>> entrySet()
		{
			return new EntrySet();
		}
		
		public class EntrySet extends AbstractSet<Entry<R, Map<C, V>>>
		{
			public class Iter implements Iterator<Entry<R, Map<C, V>>>
			{
				protected final Iterator<R> iter;
				
				protected Iter()
				{
					this.iter = HashBiTable.this.rowSet.iterator();
				}
				
				@Override public boolean hasNext() { return iter.hasNext(); }
				@Override public Entry<R, Map<C, V>> next()
				{
					final R row = iter.next();
					final Map<C, V> map = HashBiTable.this.row(row);
					return new Entry<R, Map<C, V>>()
					{
						@Override public R getKey() { return row; }
						@Override public Map<C, V> getValue() { return map; }
						@Override public Map<C, V> setValue(Map<C, V> value) { return RowMap.this.put(getKey(), value); }
					};
				}
				@Override public void remove() { iter.remove(); }
			}
			
			@Override public Iterator<Entry<R, Map<C, V>>> iterator() { return new Iter(); }
			@Override public int size() { return HashBiTable.this.rowSet.size(); }
		}
	}
	
	public class Column extends AbstractMap<R, V>
	{
		protected final C columnKey;
		protected final KeySet keySet;
		protected final EntrySet entrySet;
		protected final Values values;
		
		public Column(C columnKey)
		{
			this.columnKey = columnKey;
			this.keySet = new KeySet();
			this.entrySet = new EntrySet();
			this.values = new Values();
		}
		
		@Override public int size() { return entrySet().size(); }
		@Override public boolean isEmpty() { return entrySet().isEmpty(); }
		@Override public boolean containsKey(Object rowKey) { return HashBiTable.this.contains(rowKey, columnKey); }
		@Override public V get(Object rowKey) { return HashBiTable.this.get(rowKey, columnKey); }
		@Override public V put(R rowKey, V value) { return HashBiTable.this.put(rowKey, columnKey, value); }
		@Override public V remove(Object rowKey) { return HashBiTable.this.remove(rowKey, columnKey); }
		@Override public void clear()
		{
			for (C columnKey : HashBiTable.this.columnKeySet())
				remove(columnKey);
		}
		@Override public KeySet keySet() { return keySet; }
		@Override public EntrySet entrySet() { return entrySet; }
		@Override public Collection<V> values() { return values; }
		
		public class KeySet extends AbstractSet<R>
		{
			@Override public int size()
			{
				int size = 0;
				
				for (C columnKey : HashBiTable.this.columnKeySet())
				{
					if (contains(columnKey))
					{
						size++;
					}
				}
				
				return size;
			}
			@Override public void clear() { Column.this.clear(); }
			@Override public boolean contains(Object rowKey) { return Column.this.containsKey(rowKey); }
			@Override public boolean remove(Object rowKey) { return Column.this.remove(rowKey) != null; }
			
			@Override public Iter iterator() { return new Iter(); }
			
			public class Iter extends SimpleIterator<R>
			{
				protected final Iterator<R> iter = HashBiTable.this.rowSet.iterator();
				
				@Override protected R computeNext()
				{
					while (iter.hasNext())
					{
						R check = iter.next();
						
						if (Column.this.containsKey(check))
							return check;
					}
					
					setDone();
					return null;
				}
				@Override public void remove() { Column.this.remove(peek()); }
			}
		}
		
		public class EntrySet extends AbstractSet<Entry<R, V>>
		{
			@Override
			public int size()
			{
				int size = 0;
				
				for (Entry<R, V> entry : this)
				{
					size++;
				}
				
				return size;
			}

			@Override
			public boolean contains(Object rowKey)
			{
				return Column.this.containsKey(rowKey);
			}

			@Override
			public Iter iterator()
			{
				return new Iter();
			}
			
			public class Iter extends SimpleIterator<Entry<R, V>>
			{
				protected final Iterator<R> iter = HashBiTable.this.rowSet.iterator();
				
				@Override protected Entry<R, V> computeNext()
				{
					while (iter.hasNext())
					{
						final R nextRow = iter.next();
						
						if (Column.this.containsKey(nextRow))
						{
							return new Entry<R, V>()
							{
								@Override public R getKey() { return nextRow; }
								@Override public V getValue() { return Column.this.get(nextRow); }
								@Override public V setValue(V value) { return Column.this.put(nextRow, value); }
							};
						}
					}
					
					setDone();
					return null;
				}
				@Override public void remove() { Column.this.remove(peek().getKey()); }
			}

			@Override
			public boolean add(Entry<R, V> entry)
			{
				boolean out = contains(entry.getKey());
				Column.this.put(entry.getKey(), entry.getValue());
				return out;
			}

			@Override
			public boolean remove(Object o)
			{
				if (o instanceof Entry)
				{
					Entry<?, ?> entry = (Entry<?, ?>) o;
					boolean has = entry.equals(Column.this.get(entry.getKey()));
					if (has)
						Column.this.remove(entry.getKey());
					return has;
				}
				
				return false;
			}
		}
		
		public class Values extends AbstractSet<V>
		{
			@Override public Iterator<V> iterator() { return new Iter(); }
			@Override public int size() { return Column.this.size(); }
			
			public class Iter extends SimpleIterator<V>
			{
				protected final PeekingIterator<Entry<R, V>> iter = Column.this.entrySet().iterator();
				
				@Override
				protected V computeNext()
				{
					if (!iter.hasNext())
					{
						return iter.next().getValue();
					}
					
					setDone();
					return null;
				}
				@Override public void remove() { Column.this.remove(iter.peek().getKey()); }
			}
			
			@Override public boolean contains(Object value) { return Column.this.containsValue(value); }
			@Override public boolean remove(Object value) { return HashBiTable.this.removeValue(value) != null; }
		}
	}
	
	public class ColumnMap extends AbstractMap<C, Map<R, V>>
	{
		protected final HashMap<C, Column> map = new HashMap<C, Column>();
		
		@Override public int size() { return HashBiTable.this.size(); }
		@Override public boolean isEmpty() { return HashBiTable.this.isEmpty(); }
		@Override public boolean containsKey(Object key) { return HashBiTable.this.rowSet.contains(key); }
		
		@Override public boolean containsValue(Object value)
		{
			if (value instanceof Map)
			{
				Map<?, ?> map = (Map<?, ?>) value;
				
				for (Map<R, V> ourMap : values())
					if (map.equals(ourMap))
						return true;
			}
			
			return false;
		}

		@Override public Map<R, V> get(Object key)
		{
			if (containsKey(key))
			{
				C columnKey = (C) key;
				Column row = map.get(key);
				if (!map.containsKey(row))
				{
					row = new Column(columnKey);
					map.put(columnKey, row);
				}
				return row;
			}
			return null;
		}

		@Override public Map<R, V> put(C key, Map<R, V> value)
		{
			Map<R, V> out = remove(key);
			for (Entry<R, V> e : value.entrySet())
				HashBiTable.this.put(e.getKey(), key, e.getValue());
			return out;
		}

		@Override public Map<R, V> remove(Object key)
		{
			Map<R, V> row = get(key);
			ImmutableMap<R, V> out = ImmutableMap.copyOf(row);
			row.clear();
			return out;
		}

		@Override public void putAll(Map<? extends C, ? extends Map<R, V>> m)
		{
			for (Entry<? extends C, ? extends Map<R, V>> e : m.entrySet())
				put(e.getKey(), e.getValue());
		}

		@Override public void clear() { HashBiTable.this.clear(); }
		@Override public Set<C> keySet() { return HashBiTable.this.columnKeySet(); }
		
		protected Values values;
		
		@Override
		public Collection<Map<R, V>> values()
		{
			if (values == null)
				values = new Values();
			return values;
		}
		
		public class Values extends AbstractCollection<Map<R, V>>
		{
			public class Iter implements Iterator<Map<R, V>>
			{
				protected final Iterator<C> iter = HashBiTable.this.columnKeySet().iterator();
				@Override public boolean hasNext() { return iter.hasNext(); }
				@Override public Map<R, V> next() { return HashBiTable.this.column(iter.next()); }
				@Override public void remove() { iter.remove(); }
			}
			
			@Override public Iterator<Map<R, V>> iterator() { return new Iter(); }
			@Override public int size() { return HashBiTable.this.rowSet.size(); }
		}
		
		@Override
		public Set<Entry<C, Map<R, V>>> entrySet()
		{
			return new EntrySet();
		}
		
		public class EntrySet extends AbstractSet<Entry<C, Map<R, V>>>
		{
			public class Iter implements Iterator<Entry<C, Map<R, V>>>
			{
				protected final Iterator<C> iter = HashBiTable.this.columnKeySet().iterator();
				
				@Override public boolean hasNext() { return iter.hasNext(); }
				@Override public Entry<C, Map<R, V>> next()
				{
					final C column = iter.next();
					final Map<R, V> map = ColumnMap.this.get(column);
					return new Entry<C, Map<R, V>>()
					{
						@Override public C getKey() { return column; }
						@Override public Map<R, V> getValue() { return map; }
						@Override public Map<R, V> setValue(Map<R, V> value) { return ColumnMap.this.put(getKey(), value); }
					};
				}
				@Override public void remove() { iter.remove(); }
			}
			
			@Override public Iterator<Entry<C, Map<R, V>>> iterator() { return new Iter(); }
			@Override public int size() { return HashBiTable.this.rowSet.size(); }
		}
	}
}
