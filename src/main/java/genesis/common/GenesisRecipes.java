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
    }
}
