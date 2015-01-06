package genesis.common;

import genesis.item.ItemGenesis;
import genesis.item.ItemGenesisFood;
import net.minecraft.item.Item;

public final class GenesisItems {
    public static Item brown_flint_pebble;
    public static Item quartz;
    public static Item zircon;
    public static Item garnet;
    public static Item manganese;
    public static Item hematite;
    public static Item malachite;
    public static Item olivine;
    public static Item eryops_leg;
    public static Item cooked_eryops_leg;

    protected static void registerItems() {
        brown_flint_pebble = new ItemGenesis().setUnlocalizedName("brownFlintPebble").setCreativeTab(GenesisCreativeTabs.MATERIALS);
        quartz = new ItemGenesis().setUnlocalizedName("quartz").setCreativeTab(GenesisCreativeTabs.MATERIALS);
        zircon = new ItemGenesis().setUnlocalizedName("zircon").setCreativeTab(GenesisCreativeTabs.MATERIALS);
        garnet = new ItemGenesis().setUnlocalizedName("garnet").setCreativeTab(GenesisCreativeTabs.MATERIALS);
        manganese = new ItemGenesis().setUnlocalizedName("manganese").setCreativeTab(GenesisCreativeTabs.MATERIALS);
        hematite = new ItemGenesis().setUnlocalizedName("hematite").setCreativeTab(GenesisCreativeTabs.MATERIALS);
        malachite = new ItemGenesis().setUnlocalizedName("malachite").setCreativeTab(GenesisCreativeTabs.MATERIALS);
        olivine = new ItemGenesis().setUnlocalizedName("olivine").setCreativeTab(GenesisCreativeTabs.MATERIALS);
        eryops_leg = new ItemGenesisFood(3, 1.8F).setUnlocalizedName("eryopsLegRaw");
        cooked_eryops_leg = new ItemGenesisFood(8, 12.8F).setUnlocalizedName("eryopsLegCooked");

        Genesis.getProxy().registerItem(brown_flint_pebble, "brown_flint_pebble");
        Genesis.getProxy().registerItem(quartz, "quartz");
        Genesis.getProxy().registerItem(zircon, "zircon");
        Genesis.getProxy().registerItem(garnet, "garnet");
        Genesis.getProxy().registerItem(manganese, "manganese");
        Genesis.getProxy().registerItem(hematite, "hematite");
        Genesis.getProxy().registerItem(malachite, "malachite");
        Genesis.getProxy().registerItem(olivine, "olivine");
        Genesis.getProxy().registerItem(eryops_leg, "eryops_leg");
        Genesis.getProxy().registerItem(cooked_eryops_leg, "cooked_eryops_leg");
    }
}
