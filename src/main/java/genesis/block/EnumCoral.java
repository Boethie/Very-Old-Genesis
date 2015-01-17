package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.item.IMetadata;
import net.minecraft.item.ItemStack;

public enum EnumCoral implements IMetadata {
    FAVOSITES("favosites"),
    HELIOLITES("heliolites"),
    HALYSITES("halysites");

    private static EnumCoral[] META_LOOKUP = new EnumCoral[values().length];
    private final String name;
    private final String unlocalizedName;

    EnumCoral(String name) {
        this(name, name);
    }

    EnumCoral(String name, String unlocalizedName) {
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
        return new ItemStack(GenesisBlocks.coral, amount, getMetadata());
    }

    static {
        for (int metadata = 0; metadata < values().length; metadata++) {
            META_LOOKUP[metadata] = values()[metadata];
        }
    }
}
