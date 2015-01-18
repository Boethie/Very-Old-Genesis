package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.item.IMetadata;
import genesis.util.MetadataUtils;
import net.minecraft.item.ItemStack;

public enum EnumFern implements IMetadata {
    ZYGOPTERIS("zygopteris"),
    RUFFORDIA("ruffordia"),
    ASTRALOPTERIS("astralopteris"),
    MATONIDIUM("matonidium");

    private final String name;
    private final String unlocalizedName;

    EnumFern(String name) {
        this(name, name);
    }

    EnumFern(String name, String unlocalizedName) {
        this.name = name;
        this.unlocalizedName = unlocalizedName;
        MetadataUtils.addMeta(getClass(), this);
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
        return new ItemStack(GenesisBlocks.fern, amount, getMetadata());
    }
}
