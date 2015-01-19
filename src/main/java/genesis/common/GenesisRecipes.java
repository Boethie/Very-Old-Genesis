package genesis.common;

import genesis.item.EnumNodule;
import genesis.item.EnumPebble;
import genesis.util.Metadata;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class GenesisRecipes {
    public static void addRecipes() {
        GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), Metadata.newStack(EnumNodule.MARCASITE), Metadata.newStack(EnumPebble.BROWN_FLINT));
        GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), Metadata.newStack(EnumNodule.MARCASITE), Metadata.newStack(EnumNodule.BROWN_FLINT));
        GameRegistry.addSmelting(GenesisBlocks.quartz_ore, new ItemStack(GenesisItems.quartz), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.zircon_ore, new ItemStack(GenesisItems.zircon), 0.1F);
        GameRegistry.addSmelting(GenesisBlocks.garnet_ore, new ItemStack(GenesisItems.garnet), 0.1F);
        GameRegistry.addSmelting(GenesisBlocks.manganese_ore, new ItemStack(GenesisItems.manganese), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.hematite_ore, new ItemStack(GenesisItems.hematite), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.malachite_ore, new ItemStack(GenesisItems.malachite), 0.2F);
        GameRegistry.addSmelting(GenesisBlocks.olivine_ore, new ItemStack(GenesisItems.olivine), 0.3F);
        GameRegistry.addSmelting(GenesisBlocks.brown_flint_ore, Metadata.newStack(EnumNodule.BROWN_FLINT), 0.05F);
        GameRegistry.addSmelting(GenesisBlocks.marcasite_ore, Metadata.newStack(EnumNodule.MARCASITE), 0.05F);
        GameRegistry.addSmelting(GenesisItems.aphthoroblattina, new ItemStack(GenesisItems.cooked_aphthoroblattina), 0.35F);
        GameRegistry.addSmelting(GenesisItems.eryops_leg, new ItemStack(GenesisItems.cooked_eryops_leg), 0.35F);
    }
}
