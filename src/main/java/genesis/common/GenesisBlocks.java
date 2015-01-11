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
    public static Block moss;
    public static Block granite;
    public static Block mossy_granite;
    public static Block rhyolite;
    public static Block dolerite;
    public static Block komatiite;
    public static Block trondhjemite;
    public static Block faux_amphibolite;
    public static Block gneiss;
    public static Block quartzite;
    public static Block limestone;
    public static Block shale;
    public static Block quartz_ore;
    public static Block zircon_ore;
    public static Block garnet_ore;
    public static Block manganese_ore;
    public static Block hematite_ore;
    public static Block malachite_ore;
    public static Block olivine_ore;
    public static Block brown_flint_ore;
    public static Block marcasite_ore;

    protected static void registerBlocks() {
        if (!hasPreInit) {
            moss = new BlockMoss().setHardness(0.6F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("moss");

            /* Rocks */
            granite = new BlockGenesisRock().setUnlocalizedName("granite").setHardness(2.1F).setResistance(10.0F);
            mossy_granite = new BlockGenesisRock().setUnlocalizedName("mossyGranite").setHardness(2.1F).setResistance(10.0F);
            rhyolite = new BlockGenesisRock().setUnlocalizedName("rhyolite").setHardness(1.65F).setResistance(10.0F);
            dolerite = new BlockGenesisRock().setUnlocalizedName("dolerite").setHardness(1.2F).setResistance(10.0F);
            komatiite = new BlockGenesisRock().setUnlocalizedName("komatiite").setHardness(1.95F).setResistance(10.0F);
            trondhjemite = new BlockGenesisRock().setUnlocalizedName("trondhjemite").setHardness(1.5F).setResistance(10.0F);
            faux_amphibolite = new BlockGenesisRock().setUnlocalizedName("fauxAmphibolite").setHardness(1.5F).setResistance(10.0F);
            gneiss = new BlockGenesisRock().setUnlocalizedName("gneiss").setHardness(1.65F).setResistance(10.0F);
            quartzite = new BlockGenesisRock().setUnlocalizedName("quartzite").setHardness(1.95F).setResistance(10.0F);
            limestone = new BlockGenesisRock().setUnlocalizedName("limestone").setHardness(0.75F).setResistance(8.7F);
            shale = new BlockGenesisRock().setUnlocalizedName("shale").setHardness(0.75F).setResistance(8.7F);

            /* Granite Ores */
            quartz_ore = new BlockGenesisOre(1).setUnlocalizedName("oreQuartz").setHardness(4.2F).setResistance(5.0F);
            zircon_ore = new BlockGenesisOre(2).setUnlocalizedName("oreZircon").setHardness(4.2F).setResistance(5.0F);
            garnet_ore = new BlockGenesisOre(2).setUnlocalizedName("oreGarnet").setHardness(4.2F).setResistance(5.0F);
            manganese_ore = new BlockGenesisOre(1).setUnlocalizedName("oreManganese").setHardness(4.2F).setResistance(5.0F);
            hematite_ore = new BlockGenesisOre(1).setUnlocalizedName("oreHematite").setHardness(4.2F).setResistance(5.0F);
            malachite_ore = new BlockGenesisOre(1, 2).setUnlocalizedName("oreMalachite").setHardness(4.2F).setResistance(5.0F);
            olivine_ore = new BlockGenesisOre(3, 5).setUnlocalizedName("oreOlivine").setHardness(4.2F).setResistance(5.0F);

            /* Limestone Ores */
            brown_flint_ore = new BlockGenesisOre(0, 1, 0).setUnlocalizedName("oreBrownFlint").setHardness(1.5F).setResistance(4.35F);
            marcasite_ore = new BlockGenesisOre(0, 1, 0).setUnlocalizedName("oreMarcasite").setHardness(1.5F).setResistance(4.35F);

            // Special registration, must manually register in GenesisClient
            GameRegistry.registerBlock(moss, ItemBlockMoss.class, "moss");

            Genesis.getProxy().registerBlock(granite, "granite");
            Genesis.getProxy().registerBlock(mossy_granite, "mossy_granite");
            Genesis.getProxy().registerBlock(rhyolite, "rhyolite");
            Genesis.getProxy().registerBlock(dolerite, "dolerite");
            Genesis.getProxy().registerBlock(komatiite, "komatiite");
            Genesis.getProxy().registerBlock(trondhjemite, "trondhjemite");
            Genesis.getProxy().registerBlock(faux_amphibolite, "faux_amphibolite");
            Genesis.getProxy().registerBlock(gneiss, "gneiss");
            Genesis.getProxy().registerBlock(quartzite, "quartzite");
            Genesis.getProxy().registerBlock(limestone, "limestone");
            Genesis.getProxy().registerBlock(shale, "shale");
            Genesis.getProxy().registerBlock(quartz_ore, "quartz_ore");
            Genesis.getProxy().registerBlock(zircon_ore, "zircon_ore");
            Genesis.getProxy().registerBlock(garnet_ore, "garnet_ore");
            Genesis.getProxy().registerBlock(manganese_ore, "manganese_ore");
            Genesis.getProxy().registerBlock(hematite_ore, "hematite_ore");
            Genesis.getProxy().registerBlock(malachite_ore, "malachite_ore");
            Genesis.getProxy().registerBlock(olivine_ore, "olivine_ore");
            Genesis.getProxy().registerBlock(brown_flint_ore, "brown_flint_ore");
            Genesis.getProxy().registerBlock(marcasite_ore, "marcasite_ore");

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
