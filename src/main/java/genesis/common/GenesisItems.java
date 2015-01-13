package genesis.common;

import genesis.item.*;
import net.minecraft.item.Item;

public final class GenesisItems {
    public static final Item pebble = new ItemGenesisMetadata(EnumPebble.values()).setUnlocalizedName("pebble");
    public static final Item nodule = new ItemGenesisMetadata(EnumNodule.values()).setUnlocalizedName("nodule");
    public static final Item quartz = new ItemGenesis().setUnlocalizedName("quartz");
    public static final Item zircon = new ItemGenesis().setUnlocalizedName("zircon");
    public static final Item garnet = new ItemGenesis().setUnlocalizedName("garnet");
    public static final Item manganese = new ItemGenesis().setUnlocalizedName("manganese");
    public static final Item hematite = new ItemGenesis().setUnlocalizedName("hematite");
    public static final Item malachite = new ItemGenesis().setUnlocalizedName("malachite");
    public static final Item olivine = new ItemGenesis().setUnlocalizedName("olivine");
    public static final Item resin = new ItemGenesis().setUnlocalizedName("resin");
    public static final Item aphthoroblattina = new ItemGenesisFood(1, 0.2F).setUnlocalizedName("aphthoroblattinaRaw");
    public static final Item cooked_aphthoroblattina = new ItemGenesisFood(2, 0.8F).setUnlocalizedName("aphthoroblattinaCooked");
    public static final Item eryops_leg = new ItemGenesisFood(2, 0.8F).setUnlocalizedName("eryopsLegRaw");
    public static final Item cooked_eryops_leg = new ItemGenesisFood(5, 6.0F).setUnlocalizedName("eryopsLegCooked");
    public static final Item flint_and_marcasite = new ItemFlintAndMarcasite().setUnlocalizedName("flintAndMarcasite");

    protected static void registerItems() {
        Genesis.proxy.registerItem(pebble, "pebble");
        Genesis.proxy.registerItem(nodule, "nodule");
        Genesis.proxy.registerItem(quartz, "quartz");
        Genesis.proxy.registerItem(zircon, "zircon");
        Genesis.proxy.registerItem(garnet, "garnet");
        Genesis.proxy.registerItem(manganese, "manganese");
        Genesis.proxy.registerItem(hematite, "hematite");
        Genesis.proxy.registerItem(malachite, "malachite");
        Genesis.proxy.registerItem(olivine, "olivine");
        Genesis.proxy.registerItem(resin, "resin");
        Genesis.proxy.registerItem(aphthoroblattina, "aphthoroblattina");
        Genesis.proxy.registerItem(cooked_aphthoroblattina, "cooked_aphthoroblattina");
        Genesis.proxy.registerItem(eryops_leg, "eryops_leg");
        Genesis.proxy.registerItem(cooked_eryops_leg, "cooked_eryops_leg");
        Genesis.proxy.registerItem(flint_and_marcasite, "flint_and_marcasite");
    }
}
