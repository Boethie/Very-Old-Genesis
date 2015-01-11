package genesis.common;

import genesis.item.*;
import net.minecraft.item.Item;

public final class GenesisItems {
    public static Item pebble;
    public static Item nodule;
    public static Item quartz;
    public static Item zircon;
    public static Item garnet;
    public static Item manganese;
    public static Item hematite;
    public static Item malachite;
    public static Item olivine;
    public static Item resin;
    public static Item aphthoroblattina;
    public static Item cooked_aphthoroblattina;
    public static Item eryops_leg;
    public static Item cooked_eryops_leg;
    public static Item flint_and_marcasite;
    public static Item toolheads;

    protected static void registerItems() {
        pebble = new ItemGenesisMetadata(EnumPebble.values()).setUnlocalizedName("pebble");
        nodule = new ItemGenesisMetadata(EnumNodule.values()).setUnlocalizedName("nodule");
        quartz = new ItemGenesis().setUnlocalizedName("quartz");
        zircon = new ItemGenesis().setUnlocalizedName("zircon");
        garnet = new ItemGenesis().setUnlocalizedName("garnet");
        manganese = new ItemGenesis().setUnlocalizedName("manganese");
        hematite = new ItemGenesis().setUnlocalizedName("hematite");
        malachite = new ItemGenesis().setUnlocalizedName("malachite");
        olivine = new ItemGenesis().setUnlocalizedName("olivine");
        resin = new ItemGenesis().setUnlocalizedName("resin");
        aphthoroblattina = new ItemGenesisFood(1, 0.2F).setUnlocalizedName("aphthoroblattinaRaw");
        cooked_aphthoroblattina = new ItemGenesisFood(2, 0.8F).setUnlocalizedName("aphthoroblattinaCooked");
        eryops_leg = new ItemGenesisFood(2, 0.8F).setUnlocalizedName("eryopsLegRaw");
        cooked_eryops_leg = new ItemGenesisFood(5, 6.0F).setUnlocalizedName("eryopsLegCooked");
        flint_and_marcasite = new ItemFlintAndMarcasite().setUnlocalizedName("flintAndMarcasite");

        Genesis.getProxy().registerItem(pebble, "pebble");
        Genesis.getProxy().registerItem(nodule, "nodule");
        Genesis.getProxy().registerItem(quartz, "quartz");
        Genesis.getProxy().registerItem(zircon, "zircon");
        Genesis.getProxy().registerItem(garnet, "garnet");
        Genesis.getProxy().registerItem(manganese, "manganese");
        Genesis.getProxy().registerItem(hematite, "hematite");
        Genesis.getProxy().registerItem(malachite, "malachite");
        Genesis.getProxy().registerItem(olivine, "olivine");
        Genesis.getProxy().registerItem(resin, "resin");
        Genesis.getProxy().registerItem(aphthoroblattina, "aphthoroblattina");
        Genesis.getProxy().registerItem(cooked_aphthoroblattina, "cooked_aphthoroblattina");
        Genesis.getProxy().registerItem(eryops_leg, "eryops_leg");
        Genesis.getProxy().registerItem(cooked_eryops_leg, "cooked_eryops_leg");
        Genesis.getProxy().registerItem(flint_and_marcasite, "flint_and_marcasite");
    }
}
