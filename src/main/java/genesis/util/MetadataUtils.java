package genesis.util;

import genesis.item.IMetadata;
import net.minecraft.util.MathHelper;

import java.lang.reflect.Field;

public class MetadataUtils {
    public static <T extends IMetadata> T byMetadata(Class<? extends T> clazz, int metadata) {
        T[] values;

        try {
            Field field = clazz.getDeclaredField("META_LOOKUP");
            field.setAccessible(true);
            values = (T[]) field.get(null);
        } catch (Exception exception) {
            exception.printStackTrace();
            values = values(clazz);
        }

        return values[MathHelper.clamp_int(metadata, 0, values.length - 1)];
    }
    public static <T extends IMetadata> T[] values(Class<? extends T> clazz) {
        return clazz.getEnumConstants();
    }
}
