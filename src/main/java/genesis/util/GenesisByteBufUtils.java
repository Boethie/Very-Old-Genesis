package genesis.util;

import io.netty.buffer.ByteBuf;

public class GenesisByteBufUtils
{
	public static void writeEnum(ByteBuf buffer, Enum<?> value)
	{
		if (value.getClass().getEnumConstants().length - 1 <= Byte.MAX_VALUE - Byte.MIN_VALUE)
			buffer.writeByte(value.ordinal());
		else
			buffer.writeInt(value.ordinal());
	}
	
	public static <T extends Enum<T>> T readEnum(ByteBuf buffer, Class<T> clazz)
	{
		T[] values = clazz.getEnumConstants();
		
		if (values.length - 1 <= Byte.MAX_VALUE - Byte.MIN_VALUE)
		{
			return values[buffer.readByte()];
		}
		
		return values[buffer.readInt()];
	}
}
