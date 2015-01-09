package genesis.item;

import genesis.common.IMetadata;

public enum EnumNodule implements IMetadata {
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
}
