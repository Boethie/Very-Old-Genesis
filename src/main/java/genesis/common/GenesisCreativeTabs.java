package genesis.common;

import genesis.util.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public final class GenesisCreativeTabs {
    public static final CreativeTabs BLOCK = new CreativeTabs(Constants.MOD_ID + ".buildingBlocks") {
        @Override
        public Item getTabIconItem() {
            return GenesisItems.pebble;//moss
        }
    };

    public static final CreativeTabs DECORATIONS = new CreativeTabs(Constants.MOD_ID + ".decorations") {
        @Override
        public Item getTabIconItem() {
            return GenesisItems.pebble;//sigillaria sapling
        }
    };

    public static final CreativeTabs MISC = new CreativeTabs(Constants.MOD_ID + ".misc") {
        @Override
        public Item getTabIconItem() {
            return GenesisItems.pebble;//komatiitic lava bucket
        }
    };

    public static final CreativeTabs FOOD = new CreativeTabs(Constants.MOD_ID + ".food") {
        @Override
        public Item getTabIconItem() {
            return GenesisItems.cooked_eryops_leg;
        }
    };

    public static final CreativeTabs TOOLS = new CreativeTabs(Constants.MOD_ID + ".tools") {
        @Override
        public Item getTabIconItem() {
            return GenesisItems.pebble;//chipped granite axe
        }
    };

    public static final CreativeTabs COMBAT = new CreativeTabs(Constants.MOD_ID + ".combat") {
        @Override
        public Item getTabIconItem() {
            return GenesisItems.pebble;//chipped bone spear
        }
    };

    public static final CreativeTabs MATERIALS = new CreativeTabs(Constants.MOD_ID + ".materials") {
        @Override
        public Item getTabIconItem() {
            return GenesisItems.manganese;
        }
    };
}
