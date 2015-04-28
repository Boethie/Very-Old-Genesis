package genesis.util;

import com.google.common.collect.*;

public interface BiTable<R, C, V> extends Table<R, C, V>
{
	public static interface Key<R, C>
	{
		public R getRow();
		public C getColumn();
	}
	
	public Key<R, C> getKey(Object value);
}
