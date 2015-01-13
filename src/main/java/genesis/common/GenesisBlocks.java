package genesis.common;

import genesis.block.BlockGenesisOre;
import genesis.block.BlockGenesisRock;
import genesis.block.BlockMoss;
import genesis.item.EnumNodule;
import genesis.item.ItemBlockMoss;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class GenesisBlocks {
    private static boolean hasPreInit = false;
    public static final Block moss = new BlockMoss().setHardness(0.6F).setStepSound(GenesisSounds.MOSS).setUnlocalizedName("moss");

    /* Rocks */
    public static final Block granite = new BlockGenesisRock().setUnlocalizedName("granite").setHardness(2.1F).setResistance(10.0F);
    public static final Block mossy_granite = new BlockGenesisRock().setUnlocalizedName("mossyGranite").setHardness(2.1F).setResistance(10.0F);
    public static final Block rhyolite = new BlockGenesisRock().setUnlocalizedName("rhyolite").setHardness(1.65F).setResistance(10.0F);
    public static final Block dolerite = new BlockGenesisRock().setUnlocalizedName("dolerite").setHardness(1.2F).setResistance(10.0F);
    public static final Block komatiite = new BlockGenesisRock().setUnlocalizedName("komatiite").setHardness(1.95F).setResistance(10.0F);
    public static final Block trondhjemite = new BlockGenesisRock().setUnlocalizedName("trondhjemite").setHardness(1.5F).setResistance(10.0F);
    public static final Block faux_amphibolite = new BlockGenesisRock().setUnlocalizedName("fauxAmphibolite").setHardness(1.5F).setResistance(10.0F);
    public static final Block gneiss = new BlockGenesisRock().setUnlocalizedName("gneiss").setHardness(1.65F).setResistance(10.0F);
    public static final Block quartzite = new BlockGenesisRock().setUnlocalizedName("quartzite").setHardness(1.95F).setResistance(10.0F);
    public static final Block limestone = new BlockGenesisRock().setUnlocalizedName("limestone").setHardness(0.75F).setResistance(8.7F);
    public static final Block shale = new BlockGenesisRock().setUnlocalizedName("shale").setHardness(0.75F).setResistance(8.7F);

    /* Granite Ores */
    public static final Block quartz_ore = new BlockGenesisOre(1).setUnlocalizedName("oreQuartz").setHardness(4.2F).setResistance(5.0F);
    public static final Block zircon_ore = new BlockGenesisOre(2).setUnlocalizedName("oreZircon").setHardness(4.2F).setResistance(5.0F);
    public static final Block garnet_ore = new BlockGenesisOre(2).setUnlocalizedName("oreGarnet").setHardness(4.2F).setResistance(5.0F);
    public static final Block manganese_ore = new BlockGenesisOre(1).setUnlocalizedName("oreManganese").setHardness(4.2F).setResistance(5.0F);
    public static final Block hematite_ore = new BlockGenesisOre(1).setUnlocalizedName("oreHematite").setHardness(4.2F).setResistance(5.0F);
    public static final Block malachite_ore = new BlockGenesisOre(1, 2).setUnlocalizedName("oreMalachite").setHardness(4.2F).setResistance(5.0F);
    public static final Block olivine_ore = new BlockGenesisOre(3, 5).setUnlocalizedName("oreOlivine").setHardness(4.2F).setResistance(5.0F);

    /* Limestone Ores */
    public static final Block brown_flint_ore = new BlockGenesisOre(0, 1, 0).setUnlocalizedName("oreBrownFlint").setHardness(1.5F).setResistance(4.35F);
    public static final Block marcasite_ore = new BlockGenesisOre(0, 1, 0).setUnlocalizedName("oreMarcasite").setHardness(1.5F).setResistance(4.35F);

    protected static void registerBlocks() {
        if (!hasPreInit) {
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

            hasPreInit = true;
        } else {
            // This is done after items are initialized, which prevents null items
            setDrop(quartz_ore, GenesisItems.quartz);
            setDrop(zircon_ore, GenesisItems.zircon);
            setDrop(garnet_ore, GenesisItems.garnet);
            setDrop(manganese_ore, GenesisItems.manganese);
            setDrop(hematite_ore, GenesisItems.hematite);
            setDrop(malachite_ore, GenesisItems.malachite);
            setDrop(olivine_ore, GenesisItems.olivine);
            setDrop(brown_flint_ore, EnumNodule.BROWN_FLINT.createStack(1));
            setDrop(marcasite_ore, EnumNodule.MARCASITE.createStack(1));
        }
    }

    public static void setDrop(Block block, Block blockDrop) {
        setDrop(block, new ItemStack(blockDrop));
    }

    public static void setDrop(Block block, Item itemDrop) {
        setDrop(block, new ItemStack(itemDrop));
    }

    private static void setDrop(Block block, ItemStack stack) {
        if (block instanceof BlockGenesisOre) {
            ((BlockGenesisOre) block).setDrop(stack);
        }
    }
}
