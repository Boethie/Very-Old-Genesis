package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.item.IMetadata;
import genesis.util.MetadataUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum EnumCoral implements IMetadata {
    FAVOSITES("favosites"),
    HELIOLITES("heliolites"),
    HALYSITES("halysites");

    private final String name;
    private final String unlocalizedName;

    EnumCoral(String name) {
        this(name, name);
    }

    EnumCoral(String name, String unlocalizedName) {
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
    public Item getItem() {
        return Item.getItemFromBlock(GenesisBlocks.coral);
    }
}
