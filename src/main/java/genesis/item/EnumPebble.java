package genesis.item;

import genesis.common.IMetadata;

public enum EnumPebble implements IMetadata {
    GRANITE("granite", "granite"),
    RHYOLITE("rhyolite", "rhyolite"),
    DOLERITE("dolerite", "dolerite"),
    QUARTZITE("quartzite", "quartzite"),
    BROWN_FLINT("brown_flint", "brownFlint");

    private final String name;
    private final String unlocalizedName;

    EnumPebble(String name, String unlocalizedName) {
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
