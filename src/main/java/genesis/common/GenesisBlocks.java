package genesis.common;

import genesis.block.BlockGenesisOre;
import genesis.block.BlockGenesisRock;
import genesis.block.BlockMoss;
import genesis.item.EnumNodule;
import genesis.item.ItemBlockMoss;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class GenesisBlocks {
    public static Block moss;
    public static Block limestone;
    public static Block brown_flint_limestone;
    public static Block marcasite_ore;

    protected static void registerBlocks() {
        moss = new BlockMoss().setHardness(0.6F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("moss");
        limestone = new BlockGenesisRock().setUnlocalizedName("limestone").setHardness(0.75F).setResistance(8.5F);
        brown_flint_limestone = new BlockGenesisOre(new ItemStack(GenesisItems.nodule, 1, EnumNodule.BROWN_FLINT.getMetadata())).setUnlocalizedName("limestoneBrownFlint").setHardness(1.5F).setResistance(4.25F);
        marcasite_ore = new BlockGenesisOre(new ItemStack(GenesisItems.nodule, 1, EnumNodule.MARCASITE.getMetadata())).setUnlocalizedName("oreMarcasite").setHardness(1.5F).setResistance(4.25F);

        GameRegistry.registerBlock(moss, ItemBlockMoss.class, "moss");

        Genesis.getProxy().registerBlock(limestone, "limestone");
        Genesis.getProxy().registerBlock(brown_flint_limestone, "brown_flint_limestone");
        Genesis.getProxy().registerBlock(marcasite_ore, "marcasite_ore");
    }
}
