package genesis.util;

import genesis.util.HashBiTable.Key;

import com.google.common.collect.*;

public interface BiTable<R, C, V> extends Table<R, C, V>
{
	public static interface Key<R, C>
	{
		public R getRow();
		public C getColumn();
	}
	
	public V get(Key<?, ?> key);
	
	public Key<R, C> getKey(Object value);
	
	public Key<R, C> removeValue(Object value);
}
