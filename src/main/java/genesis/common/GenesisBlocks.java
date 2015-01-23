package genesis.common;

import genesis.block.BlockCoral;
import genesis.block.BlockFern;
import genesis.block.BlockGenesisOre;
import genesis.block.BlockGenesisRock;
import genesis.block.BlockMoss;
import genesis.block.BlockNewPermafrost;
import genesis.block.BlockOctaedrite;
import genesis.block.BlockPermafrost;
import genesis.block.BlockPlant;
import genesis.block.BlockRedClay;
import genesis.block.EnumCoral;
import genesis.block.EnumFern;
import genesis.block.EnumPlant;
import genesis.client.GenesisSounds;
import genesis.item.EnumNodule;
import genesis.item.ItemBlockColored;
import genesis.item.ItemBlockMetadata;
import genesis.util.Constants;
import genesis.util.Metadata;
import net.minecraft.block.Block;

public final class GenesisBlocks
{
	public static final Block moss = new BlockMoss().setUnlocalizedName(Constants.PREFIX + "moss");

	/* Rocks */
	public static final Block granite = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName("granite");
	public static final Block mossy_granite = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName("mossyGranite");
	public static final Block rhyolite = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName("rhyolite");
	public static final Block dolerite = new BlockGenesisRock(1.2F, 10.0F).setUnlocalizedName("dolerite");
	public static final Block komatiite = new BlockGenesisRock(1.95F, 10.0F).setUnlocalizedName("komatiite");
	public static final Block trondhjemite = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName("trondhjemite");
	public static final Block faux_amphibolite = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName("fauxAmphibolite");
	public static final Block gneiss = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName("gneiss");
	public static final Block quartzite = new BlockGenesisRock(1.95F, 10.0F).setUnlocalizedName("quartzite");
	public static final Block limestone = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName("limestone");
	public static final Block shale = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName("shale");
	public static final Block red_clay = new BlockRedClay().setUnlocalizedName(Constants.PREFIX + "redClay");
	public static final Block octaedrite = new BlockOctaedrite().setUnlocalizedName("octaedrite");

	public static final Block permafrost = new BlockPermafrost().setUnlocalizedName("permafrost");
	public static final Block new_permafrost = new BlockNewPermafrost().setUnlocalizedName("permafrost");

	/* Granite Ores */
	public static final Block quartz_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(GenesisItems.quartz).setUnlocalizedName(Constants.PREFIX + "oreQuartz");
	public static final Block zircon_ore = new BlockGenesisOre(4.2F, 5.0F, 2, 1).setDrop(GenesisItems.zircon).setUnlocalizedName(Constants.PREFIX + "oreZircon");
	public static final Block garnet_ore = new BlockGenesisOre(4.2F, 5.0F, 2, 1).setDrop(GenesisItems.garnet).setUnlocalizedName(Constants.PREFIX + "oreGarnet");
	public static final Block manganese_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(GenesisItems.manganese).setUnlocalizedName(Constants.PREFIX + "oreManganese");
	public static final Block hematite_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(GenesisItems.hematite).setUnlocalizedName(Constants.PREFIX + "oreHematite");
	public static final Block malachite_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 2, 1).setDrop(GenesisItems.malachite).setUnlocalizedName(Constants.PREFIX + "oreMalachite");
	public static final Block olivine_ore = new BlockGenesisOre(4.2F, 5.0F, 3, 5, 1).setDrop(GenesisItems.olivine).setUnlocalizedName(Constants.PREFIX + "oreOlivine");

	/* Limestone Ores */
	public static final Block brown_flint_ore = new BlockGenesisOre(1.5F, 4.35F, 1, 0).setDrop(EnumNodule.BROWN_FLINT).setUnlocalizedName(Constants.PREFIX + "oreBrownFlint");
	public static final Block marcasite_ore = new BlockGenesisOre(1.5F, 4.35F, 1, 0).setDrop(EnumNodule.MARCASITE).setUnlocalizedName(Constants.PREFIX + "oreMarcasite");

	/* Misc */
	public static final Block plant = new BlockPlant().setUnlocalizedName(Constants.PREFIX + "plant");
	public static final Block fern = new BlockFern().setUnlocalizedName(Constants.PREFIX + "fern");
	public static final Block coral = new BlockCoral().setUnlocalizedName("coral");

	public static void registerBlocks()
	{
		// Initializes values
		EnumPlant.values();
		EnumFern.values();
		EnumCoral.values();

		Genesis.proxy.registerBlock(moss, "moss", BlockMoss.ItemMoss.class);
		Genesis.proxy.registerBlock(granite, "granite");
		Genesis.proxy.registerBlock(mossy_granite, "mossy_granite");
		Genesis.proxy.registerBlock(rhyolite, "rhyolite");
		Genesis.proxy.registerBlock(dolerite, "dolerite");
		Genesis.proxy.registerBlock(komatiite, "komatiite");
		Genesis.proxy.registerBlock(trondhjemite, "trondhjemite");
		Genesis.proxy.registerBlock(faux_amphibolite, "faux_amphibolite");
		Genesis.proxy.registerBlock(gneiss, "gneiss");
		Genesis.proxy.registerBlock(quartzite, "quartzite");
		Genesis.proxy.registerBlock(limestone, "limestone");
		Genesis.proxy.registerBlock(shale, "shale");
		Genesis.proxy.registerBlock(red_clay, "red_clay");
		Genesis.proxy.registerBlock(octaedrite, "octaedrite");
		Genesis.proxy.registerBlock(permafrost, "permafrost");
		Genesis.proxy.registerBlock(new_permafrost, "new_permafrost");
		Genesis.proxy.registerBlock(quartz_ore, "quartz_ore");
		Genesis.proxy.registerBlock(zircon_ore, "zircon_ore");
		Genesis.proxy.registerBlock(garnet_ore, "garnet_ore");
		Genesis.proxy.registerBlock(manganese_ore, "manganese_ore");
		Genesis.proxy.registerBlock(hematite_ore, "hematite_ore");
		Genesis.proxy.registerBlock(malachite_ore, "malachite_ore");
		Genesis.proxy.registerBlock(olivine_ore, "olivine_ore");
		Genesis.proxy.registerBlock(brown_flint_ore, "brown_flint_ore");
		Genesis.proxy.registerBlock(marcasite_ore, "marcasite_ore");
		Genesis.proxy.registerBlock(plant, "plant", ItemBlockMetadata.class, EnumPlant.class);
		Genesis.proxy.registerBlock(fern, "fern", ItemBlockColored.class, EnumFern.class);
		Genesis.proxy.registerBlock(coral, "coral", ItemBlockMetadata.class, EnumCoral.class);
	}
}
