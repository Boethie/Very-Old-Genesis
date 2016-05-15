package genesis.util;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

public class GenesisDataSerializers
{
	public static interface DataSerializerBase<T> extends DataSerializer<T>
	{
		@Override
		public default DataParameter<T> createKey(int id)
		{
			return new DataParameter<T>(id, this);
		}
	}
	
	public static class EnumSerializer<V extends Enum<V>> implements DataSerializerBase<V>
	{
		protected final Class<V> clazz;
		
		private EnumSerializer(Class<V> clazz)
		{
			this.clazz = clazz;
		}
		
		@Override
		public void write(PacketBuffer buf, V value)
		{
			buf.writeEnumValue(value);
		}
		
		@Override
		public V read(PacketBuffer buf)
		{
			return buf.readEnumValue(clazz);
		}
	}
	
	public static <T extends Enum<T>> EnumSerializer<T> createEnum(Class<T> clazz)
	{
		return new EnumSerializer<>(clazz);
	}
}
