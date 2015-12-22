package genesis.util;

import com.google.common.collect.Table;

public interface BiTable<R, C, V> extends Table<R, C, V>
{
	interface Key<R, C>
	{
		R getRow();
		C getColumn();
	}
	
	V get(Key<?, ?> key);
	
	Key<R, C> getKey(Object value);
	
	Key<R, C> removeValue(Object value);
}
