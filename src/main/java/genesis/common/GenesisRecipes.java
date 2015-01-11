package genesis.common;

import genesis.item.EnumNodule;
import genesis.item.EnumPebble;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class GenesisRecipes {
    protected static void addRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), EnumNodule.MARCASITE.createStack(1), EnumPebble.BROWN_FLINT.createStack(1));
        GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), EnumNodule.MARCASITE.createStack(1), EnumNodule.BROWN_FLINT.createStack(1));
        GameRegistry.addSmelting(GenesisBlocks.quartz_ore, new ItemStack(GenesisItems.quartz), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.zircon_ore, new ItemStack(GenesisItems.zircon), 0.1F);
        GameRegistry.addSmelting(GenesisBlocks.garnet_ore, new ItemStack(GenesisItems.garnet), 0.1F);
        GameRegistry.addSmelting(GenesisBlocks.manganese_ore, new ItemStack(GenesisItems.manganese), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.hematite_ore, new ItemStack(GenesisItems.hematite), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.malachite_ore, new ItemStack(GenesisItems.malachite), 0.2F);
        GameRegistry.addSmelting(GenesisBlocks.olivine_ore, new ItemStack(GenesisItems.olivine), 0.3F);
        GameRegistry.addSmelting(GenesisBlocks.brown_flint_ore, EnumNodule.BROWN_FLINT.createStack(1), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.marcasite_ore, EnumNodule.MARCASITE.createStack(1), 0.05F);
    }
}
