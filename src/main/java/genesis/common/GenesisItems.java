package genesis.common;

import genesis.Genesis;
import genesis.item.*;
import genesis.util.Constants;
import net.minecraft.item.Item;

public final class GenesisItems {
    public static final Item pebble = new ItemGenesisMetadata(EnumPebble.values()).setUnlocalizedName(Constants.MOD_ID + ".pebble");
    public static final Item nodule = new ItemGenesisMetadata(EnumNodule.values()).setUnlocalizedName(Constants.MOD_ID + ".nodule");
    public static final Item quartz = new ItemGenesis().setUnlocalizedName(Constants.MOD_ID + ".quartz");
    public static final Item zircon = new ItemGenesis().setUnlocalizedName(Constants.MOD_ID + ".zircon");
    public static final Item garnet = new ItemGenesis().setUnlocalizedName(Constants.MOD_ID + ".garnet");
    public static final Item manganese = new ItemGenesis().setUnlocalizedName(Constants.MOD_ID + ".manganese");
    public static final Item hematite = new ItemGenesis().setUnlocalizedName(Constants.MOD_ID + ".hematite");
    public static final Item malachite = new ItemGenesis().setUnlocalizedName(Constants.MOD_ID + ".malachite");
    public static final Item olivine = new ItemGenesis().setUnlocalizedName(Constants.MOD_ID + ".olivine");
    public static final Item resin = new ItemGenesis().setUnlocalizedName(Constants.MOD_ID + ".resin");
    public static final Item aphthoroblattina = new ItemGenesisFood(1, 0.2F).setUnlocalizedName(Constants.MOD_ID + ".aphthoroblattinaRaw");
    public static final Item cooked_aphthoroblattina = new ItemGenesisFood(2, 0.8F).setUnlocalizedName(Constants.MOD_ID + ".aphthoroblattinaCooked");
    public static final Item eryops_leg = new ItemGenesisFood(2, 0.8F).setUnlocalizedName(Constants.MOD_ID + ".eryopsLegRaw");
    public static final Item cooked_eryops_leg = new ItemGenesisFood(5, 6.0F).setUnlocalizedName(Constants.MOD_ID + ".eryopsLegCooked");
    public static final Item flint_and_marcasite = new ItemFlintAndMarcasite().setUnlocalizedName(Constants.MOD_ID + ".flintAndMarcasite");

    public static void registerItems() {
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
