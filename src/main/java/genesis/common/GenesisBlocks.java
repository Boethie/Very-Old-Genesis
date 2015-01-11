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
    public static Block limestone;
    public static Block granite;
    public static Block mossy_granite;
    public static Block rhyolite;
    public static Block dolerite;
    public static Block komatiite;
    public static Block trondhjemite;
    public static Block faux_amphibolite;
    public static Block gneiss;
    public static Block quartzite;
    public static Block shale;
    public static Block brown_flint_limestone;
    public static Block marcasite_ore;

    protected static void registerBlocks() {
        if (!hasPreInit) {
            moss = new BlockMoss().setHardness(0.6F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("moss");

            granite = new BlockGenesisRock().setUnlocalizedName("granite").setHardness(2.1F).setResistance(10F);
            mossy_granite = new BlockGenesisRock().setUnlocalizedName("mossyGranite").setHardness(2.1F).setResistance(10F);
            rhyolite = new BlockGenesisRock().setUnlocalizedName("rhyolite").setHardness(1.65F).setResistance(10F);
            dolerite = new BlockGenesisRock().setUnlocalizedName("dolerite").setHardness(1.2F).setResistance(10F);
            komatiite = new BlockGenesisRock().setUnlocalizedName("komatiite").setHardness(1.95F).setResistance(10F);
            trondhjemite = new BlockGenesisRock().setUnlocalizedName("trondhjemite").setHardness(1.5F).setResistance(10F);
            faux_amphibolite = new BlockGenesisRock().setUnlocalizedName("fauxAmphibolite").setHardness(1.5F).setResistance(10F);
            gneiss = new BlockGenesisRock().setUnlocalizedName("gneiss").setHardness(1.65F).setResistance(10F);
            quartzite = new BlockGenesisRock().setUnlocalizedName("quartzite").setHardness(1.95F).setResistance(10F);
            limestone = new BlockGenesisRock().setUnlocalizedName("limestone").setHardness(0.75F).setResistance(8.7F);
            shale = new BlockGenesisRock().setUnlocalizedName("shale").setHardness(0.75F).setResistance(8.7F);

            brown_flint_limestone = new BlockGenesisOre(1).setUnlocalizedName("limestoneBrownFlint").setHardness(1.5F).setResistance(4.25F);
            marcasite_ore = new BlockGenesisOre(1).setUnlocalizedName("oreMarcasite").setHardness(1.5F).setResistance(4.25F);

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

            Genesis.getProxy().registerBlock(brown_flint_limestone, "brown_flint_limestone");
            Genesis.getProxy().registerBlock(marcasite_ore, "marcasite_ore");

            hasPreInit = true;
        } else {
            // This is done after items are initialized, which prevents null items
            setDrop(brown_flint_limestone, new ItemStack(GenesisItems.nodule, 1, EnumNodule.BROWN_FLINT.getMetadata()));
            setDrop(marcasite_ore, new ItemStack(GenesisItems.nodule, 1, EnumNodule.MARCASITE.getMetadata()));
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
