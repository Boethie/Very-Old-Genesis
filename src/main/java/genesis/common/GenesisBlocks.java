package genesis.common;

import genesis.block.BlockGenesisOre;
import genesis.block.BlockGenesisRock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public final class GenesisBlocks {
    public static Block limestone;
    public static Block brown_flint_limestone;
    public static Block marcasite_ore;

    protected static void registerBlocks() {
        limestone = new BlockGenesisRock().setUnlocalizedName("limestone").setHardness(0.75F).setResistance(8.5F);
        brown_flint_limestone = new BlockGenesisOre(GenesisItems.nodule).setUnlocalizedName("limestoneBrownFlint").setHardness(1.5F).setResistance(4.25F);
        marcasite_ore = new BlockGenesisOre(new ItemStack(GenesisItems.nodule, 1, 1)).setUnlocalizedName("oreMarcasite").setHardness(1.5F).setResistance(4.25F);

        Genesis.getProxy().registerBlock(limestone, "limestone");
        Genesis.getProxy().registerBlock(brown_flint_limestone, "brown_flint_limestone");
        Genesis.getProxy().registerBlock(marcasite_ore, "marcasite_ore");
    }
}
