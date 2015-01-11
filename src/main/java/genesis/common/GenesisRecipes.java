package genesis.common;

import genesis.item.EnumNodule;
import genesis.item.EnumPebble;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class GenesisRecipes {
    protected static void addRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), new ItemStack(GenesisItems.nodule, 1, EnumNodule.MARCASITE.getMetadata()), new ItemStack(GenesisItems.pebble, 1, EnumPebble.BROWN_FLINT.getMetadata()));
        GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), new ItemStack(GenesisItems.nodule, 1, EnumNodule.MARCASITE.getMetadata()), new ItemStack(GenesisItems.nodule, 1, EnumNodule.BROWN_FLINT.getMetadata()));

        GameRegistry.addSmelting(GenesisBlocks.brown_flint_limestone, new ItemStack(GenesisItems.nodule, 1, EnumNodule.BROWN_FLINT.getMetadata()), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.marcasite_ore, new ItemStack(GenesisItems.nodule, 1, EnumNodule.MARCASITE.getMetadata()), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.quartz_granite_ore, new ItemStack(GenesisItems.quartz), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.zircon_granite_ore, new ItemStack(GenesisItems.zircon), 0.1F);
        GameRegistry.addSmelting(GenesisBlocks.garnet_granite_ore, new ItemStack(GenesisItems.garnet), 0.1F);
        GameRegistry.addSmelting(GenesisBlocks.manganese_granite_ore, new ItemStack(GenesisItems.manganese), 0.1F);
        GameRegistry.addSmelting(GenesisBlocks.hematite_granite_ore, new ItemStack(GenesisItems.hematite), 0.1F);
        GameRegistry.addSmelting(GenesisBlocks.malachite_granite_ore, new ItemStack(GenesisItems.malachite), 0.15F);
        GameRegistry.addSmelting(GenesisBlocks.olivine_granite_ore, new ItemStack(GenesisItems.olivine), 0.3F);
    }
}
