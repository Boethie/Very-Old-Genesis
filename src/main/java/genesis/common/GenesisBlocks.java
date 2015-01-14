package genesis.common;

import genesis.Genesis;
import genesis.block.BlockGenesisOre;
import genesis.block.BlockGenesisRock;
import genesis.block.BlockMoss;
import genesis.item.EnumNodule;
import genesis.item.ItemBlockMoss;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class GenesisBlocks {
    public static final Block moss = new BlockMoss().setHardness(0.6F).setStepSound(GenesisSounds.MOSS).setUnlocalizedName(Constants.MOD_ID + ".moss");

    /* Rocks */
    public static final Block granite = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".granite").setHardness(2.1F).setResistance(10.0F);
    public static final Block mossy_granite = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".mossyGranite").setHardness(2.1F).setResistance(10.0F);
    public static final Block rhyolite = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".rhyolite").setHardness(1.65F).setResistance(10.0F);
    public static final Block dolerite = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".dolerite").setHardness(1.2F).setResistance(10.0F);
    public static final Block komatiite = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".komatiite").setHardness(1.95F).setResistance(10.0F);
    public static final Block trondhjemite = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".trondhjemite").setHardness(1.5F).setResistance(10.0F);
    public static final Block faux_amphibolite = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".fauxAmphibolite").setHardness(1.5F).setResistance(10.0F);
    public static final Block gneiss = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".gneiss").setHardness(1.65F).setResistance(10.0F);
    public static final Block quartzite = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".quartzite").setHardness(1.95F).setResistance(10.0F);
    public static final Block limestone = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".limestone").setHardness(0.75F).setResistance(8.7F);
    public static final Block shale = new BlockGenesisRock().setUnlocalizedName(Constants.MOD_ID + ".shale").setHardness(0.75F).setResistance(8.7F);

    /* Granite Ores */
    public static final Block quartz_ore = new BlockGenesisOre(1, 1).setDrop(GenesisItems.quartz).setUnlocalizedName(Constants.MOD_ID + ".oreQuartz").setHardness(4.2F).setResistance(5.0F);
    public static final Block zircon_ore = new BlockGenesisOre(2, 1).setDrop(GenesisItems.zircon).setUnlocalizedName(Constants.MOD_ID + ".oreZircon").setHardness(4.2F).setResistance(5.0F);
    public static final Block garnet_ore = new BlockGenesisOre(2, 1).setDrop(GenesisItems.garnet).setUnlocalizedName(Constants.MOD_ID + ".oreGarnet").setHardness(4.2F).setResistance(5.0F);
    public static final Block manganese_ore = new BlockGenesisOre(1, 1).setDrop(GenesisItems.manganese).setUnlocalizedName(Constants.MOD_ID + ".oreManganese").setHardness(4.2F).setResistance(5.0F);
    public static final Block hematite_ore = new BlockGenesisOre(1, 1).setDrop(GenesisItems.hematite).setUnlocalizedName(Constants.MOD_ID + ".oreHematite").setHardness(4.2F).setResistance(5.0F);
    public static final Block malachite_ore = new BlockGenesisOre(1, 2, 1).setDrop(GenesisItems.malachite).setUnlocalizedName(Constants.MOD_ID + ".oreMalachite").setHardness(4.2F).setResistance(5.0F);
    public static final Block olivine_ore = new BlockGenesisOre(3, 5, 1).setDrop(GenesisItems.olivine).setUnlocalizedName(Constants.MOD_ID + ".oreOlivine").setHardness(4.2F).setResistance(5.0F);

    /* Limestone Ores */
    public static final Block brown_flint_ore = new BlockGenesisOre(1, 0).setDrop(EnumNodule.BROWN_FLINT.createStack(1)).setUnlocalizedName(Constants.MOD_ID + ".oreBrownFlint").setHardness(1.5F).setResistance(4.35F);
    public static final Block marcasite_ore = new BlockGenesisOre(1, 0).setDrop(EnumNodule.MARCASITE.createStack(1)).setUnlocalizedName(Constants.MOD_ID + ".oreMarcasite").setHardness(1.5F).setResistance(4.35F);

    public static void registerBlocks() {
        // Special registration, must manually register in GenesisClient
        GameRegistry.registerBlock(moss, ItemBlockMoss.class, "moss");

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
        Genesis.proxy.registerBlock(quartz_ore, "quartz_ore");
        Genesis.proxy.registerBlock(zircon_ore, "zircon_ore");
        Genesis.proxy.registerBlock(garnet_ore, "garnet_ore");
        Genesis.proxy.registerBlock(manganese_ore, "manganese_ore");
        Genesis.proxy.registerBlock(hematite_ore, "hematite_ore");
        Genesis.proxy.registerBlock(malachite_ore, "malachite_ore");
        Genesis.proxy.registerBlock(olivine_ore, "olivine_ore");
        Genesis.proxy.registerBlock(brown_flint_ore, "brown_flint_ore");
        Genesis.proxy.registerBlock(marcasite_ore, "marcasite_ore");
    }
}
