package genesis.util;

import com.google.common.collect.Maps;
import genesis.item.IMetadata;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class Metadata {
    private static final HashMap<Class, ArrayList> LOOKUP = Maps.newHashMap();

    public static <T extends IMetadata> ArrayList<T> getLookup(Class<? extends T> clazz) {
        if (!IMetadata.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " is not a subclass of IMetadata!");
        }

        ArrayList<T> metaLookup = LOOKUP.get(clazz);

        if (metaLookup == null) {
            LOOKUP.put(clazz, metaLookup = new ArrayList<T>());
        }

        return metaLookup;
    }

    public static void add(Class<? extends IMetadata> clazz, IMetadata meta) {
        getLookup(clazz).add(meta);
    }

    public static <T extends IMetadata> T get(Class<? extends T> clazz, int metadata) {
        ArrayList<T> metaLookup = getLookup(clazz);
        T meta;

        try {
            meta = metaLookup.get(metadata);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            meta = metaLookup.get(0);
        }

        return meta;
    }

    public static int getMetadata(Enum<? extends IMetadata> meta) {
        return meta.ordinal();
    }

    public static ItemStack createStack(Enum<? extends IMetadata> meta) {
        return createStack(meta, 1);
    }

    public static ItemStack createStack(Enum<? extends IMetadata> meta, int amount) {
        return new ItemStack(((IMetadata) meta).getItem(), amount, getMetadata(meta));
    }
}
