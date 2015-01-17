package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.item.IMetadata;
import net.minecraft.item.ItemStack;

public enum EnumPlant implements IMetadata {
    COOKSONIA("cooksonia"),
    BARAGWANATHIA("baragwanathia"),
    SCIADOPHYTON("sciadophyton"),
    PSILOPHYTON("psilophyton"),
    NOTHIA("nothia"),
    RHYNIA("rhynia"),
    ARCHAEAMPHORA("archaeamphora"),
    MABELIA("mabelia");

    private static final EnumPlant[] META_LOOKUP = new EnumPlant[values().length];
    private final String name;
    private final String unlocalizedName;

    EnumPlant(String name) {
        this(name, name);
    }

    EnumPlant(String name, String unlocalizedName) {
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
        return new ItemStack(GenesisBlocks.plant, amount, getMetadata());
    }

    static {
        for (int metadata = 0; metadata < values().length; metadata++) {
            META_LOOKUP[metadata] = values()[metadata];
        }
    }
}
