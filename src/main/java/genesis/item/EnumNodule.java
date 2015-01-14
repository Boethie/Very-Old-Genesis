package genesis.item;

import genesis.common.GenesisItems;
import net.minecraft.item.ItemStack;

public enum EnumNodule implements IMetadataItem {
    BROWN_FLINT("brown_flint", "brownFlint"),
    MARCASITE("marcasite", "marcasite");

    private final String name;
    private final String unlocalizedName;

    EnumNodule(String name, String unlocalizedName) {
        this.name = name;
        this.unlocalizedName = unlocalizedName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    @Override
    public int getMetadata() {
        return ordinal();
    }

    @Override
    public ItemStack createStack(int amount) {
        return new ItemStack(GenesisItems.nodule, amount, getMetadata());
    }
}
