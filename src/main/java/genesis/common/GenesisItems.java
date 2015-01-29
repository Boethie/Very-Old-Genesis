package genesis.common;

import genesis.item.ItemFlintAndMarcasite;
import genesis.item.ItemGenesis;
import genesis.item.ItemGenesisFood;
import genesis.item.ItemGenesisMetadata;
import genesis.metadata.EnumDung;
import genesis.metadata.EnumNodule;
import genesis.metadata.EnumPebble;
import genesis.util.Constants;
import net.minecraft.item.Item;

public final class GenesisItems
{
	/* Materials */
	public static final Item pebble = new ItemGenesisMetadata(EnumPebble.class).setUnlocalizedName("pebble");
	public static final Item red_clay_ball = new ItemGenesis().setUnlocalizedName("redClay");
	public static final Item octaedrite_shard = new ItemGenesis().setUnlocalizedName("octaedriteShard");
	public static final Item red_clay_bowl = new ItemGenesis().setUnlocalizedName("bowlRedClay");
	public static final Item ceramic_bowl = new ItemGenesis().setUnlocalizedName("bowlCeramic");
	public static final Item nodule = new ItemGenesisMetadata(EnumNodule.class).setUnlocalizedName("nodule");
	public static final Item quartz = new ItemGenesis().setUnlocalizedName("quartz");
	public static final Item zircon = new ItemGenesis().setUnlocalizedName("zircon");
	public static final Item garnet = new ItemGenesis().setUnlocalizedName("garnet");
	public static final Item manganese = new ItemGenesis().setUnlocalizedName("manganese");
	public static final Item hematite = new ItemGenesis().setUnlocalizedName("hematite");
	public static final Item malachite = new ItemGenesis().setUnlocalizedName("malachite");
	public static final Item olivine = new ItemGenesis().setUnlocalizedName("olivine");
	public static final Item dung = new ItemGenesisMetadata(EnumDung.class).setUnlocalizedName("dung");
	public static final Item resin = new ItemGenesis().setUnlocalizedName("resin");
	public static final Item prototaxites_flesh = new ItemGenesis().setUnlocalizedName("prototaxitesFlesh");
	public static final Item tyrannosaurus_saliva = new ItemGenesis().setUnlocalizedName("tyrannosaurusSaliva");
	public static final Item tyrannosaurus_tooth = new ItemGenesis().setUnlocalizedName("tyrannosaurusTooth");

	/* Food */
	public static final Item aphthoroblattina = new ItemGenesisFood(1, 0.2F).setUnlocalizedName(Constants.PREFIX + "aphthoroblattinaRaw");
	public static final Item cooked_aphthoroblattina = new ItemGenesisFood(2, 0.8F).setUnlocalizedName(Constants.PREFIX + "aphthoroblattinaCooked");
	public static final Item climatius = new ItemGenesisFood(2, 0.4F).setUnlocalizedName(Constants.PREFIX + "climatiusRaw");
	public static final Item cooked_climatius = new ItemGenesisFood(5, 6.0F).setUnlocalizedName(Constants.PREFIX + "climatiusCooked");
	public static final Item eryops_leg = new ItemGenesisFood(2, 0.8F).setUnlocalizedName(Constants.PREFIX + "eryopsLegRaw");
	public static final Item cooked_eryops_leg = new ItemGenesisFood(5, 6.0F).setUnlocalizedName(Constants.PREFIX + "eryopsLegCooked");
	public static final Item tyrannosaurus = new ItemGenesisFood(4, 2.8F).setUnlocalizedName(Constants.PREFIX + "tyrannosaurusRaw");
	public static final Item cooked_tyrannosaurus = new ItemGenesisFood(16, 19.8F).setUnlocalizedName(Constants.PREFIX + "tyrannosaurusCooked");

	/* Misc */
	public static final Item flint_and_marcasite = new ItemFlintAndMarcasite().setUnlocalizedName(Constants.PREFIX + "flintAndMarcasite");

	public static void registerItems()
	{
		// Initializes values
		EnumPebble.values();
		EnumNodule.values();
		EnumDung.values();

		Genesis.proxy.registerItem(pebble, "pebble");
		Genesis.proxy.registerItem(red_clay_ball, "red_clay_ball");
		Genesis.proxy.registerItem(octaedrite_shard, "octaedrite_shard");
		Genesis.proxy.registerItem(red_clay_bowl, "red_clay_bowl");
		Genesis.proxy.registerItem(ceramic_bowl, "ceramic_bowl");
		Genesis.proxy.registerItem(quartz, "quartz");
		Genesis.proxy.registerItem(zircon, "zircon");
		Genesis.proxy.registerItem(garnet, "garnet");
		Genesis.proxy.registerItem(manganese, "manganese");
		Genesis.proxy.registerItem(hematite, "hematite");
		Genesis.proxy.registerItem(malachite, "malachite");
		Genesis.proxy.registerItem(olivine, "olivine");
		Genesis.proxy.registerItem(nodule, "nodule");
		Genesis.proxy.registerItem(dung, "dung");
		Genesis.proxy.registerItem(resin, "resin");
		Genesis.proxy.registerItem(prototaxites_flesh, "prototaxites_flesh");
		Genesis.proxy.registerItem(tyrannosaurus_saliva, "tyrannosaurus_saliva");
		Genesis.proxy.registerItem(tyrannosaurus_tooth, "tyrannosaurus_tooth");
		Genesis.proxy.registerItem(aphthoroblattina, "aphthoroblattina");
		Genesis.proxy.registerItem(cooked_aphthoroblattina, "cooked_aphthoroblattina");
		Genesis.proxy.registerItem(climatius, "climatius");
		Genesis.proxy.registerItem(cooked_climatius, "cooked_climatius");
		Genesis.proxy.registerItem(eryops_leg, "eryops_leg");
		Genesis.proxy.registerItem(cooked_eryops_leg, "cooked_eryops_leg");
		Genesis.proxy.registerItem(tyrannosaurus, "tyrannosaurus");
		Genesis.proxy.registerItem(cooked_tyrannosaurus, "cooked_tyrannosaurus");
		Genesis.proxy.registerItem(flint_and_marcasite, "flint_and_marcasite");
	}
}
