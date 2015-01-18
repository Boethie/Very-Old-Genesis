package genesis.util;

import com.google.common.collect.Maps;
import genesis.item.IMetadata;

import java.util.ArrayList;
import java.util.HashMap;

public class MetadataUtils {
    private static final HashMap<Class, ArrayList> META_LOOKUP = Maps.newHashMap();

    public static <T extends IMetadata> ArrayList<T> getMetaLookup(Class<? extends T> clazz) {
        if (!IMetadata.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " is not a subclass of IMetadata!");
        }

        ArrayList<T> enums = META_LOOKUP.get(clazz);

        if (enums == null) {
            META_LOOKUP.put(clazz, enums = new ArrayList<T>());
        }

        return enums;
    }

    public static void addMeta(Class<? extends IMetadata> clazz, IMetadata meta) {
        getMetaLookup(clazz).add(meta);
    }

    public static <T extends IMetadata> T byMetadata(Class<? extends T> clazz, int metadata) {
        ArrayList<T> enums = getMetaLookup(clazz);
        T meta;

        try {
            meta = enums.get(metadata);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            meta = enums.get(0);
        }

        return meta;
    }
}
