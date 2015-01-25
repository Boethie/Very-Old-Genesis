package genesis.common;

import genesis.metadata.EnumDung;
import genesis.metadata.EnumNodule;
import genesis.metadata.EnumPebble;
import genesis.metadata.IMetaMulti;
import genesis.util.FuelHandler;
import genesis.util.Metadata;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class GenesisRecipes
{
	public static void addRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.red_clay), "CC", "CC", 'C', GenesisItems.red_clay_ball);
		GameRegistry.addRecipe(new ItemStack(GenesisItems.red_clay_bowl), "C C", " C ", 'C', GenesisItems.red_clay_ball);
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), Metadata.newStack(EnumNodule.MARCASITE), Metadata.newStack(EnumPebble.BROWN_FLINT));
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), Metadata.newStack(EnumNodule.MARCASITE), Metadata.newStack(EnumNodule.BROWN_FLINT));
		GameRegistry.addSmelting(GenesisBlocks.red_clay, new ItemStack(Blocks.stained_hardened_clay, 1, EnumDyeColor.WHITE.getMetadata()), 0.3F);
		GameRegistry.addSmelting(GenesisBlocks.quartz_ore, new ItemStack(GenesisItems.quartz), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.zircon_ore, new ItemStack(GenesisItems.zircon), 0.1F);
		GameRegistry.addSmelting(GenesisBlocks.garnet_ore, new ItemStack(GenesisItems.garnet), 0.1F);
		GameRegistry.addSmelting(GenesisBlocks.manganese_ore, new ItemStack(GenesisItems.manganese), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.hematite_ore, new ItemStack(GenesisItems.hematite), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.malachite_ore, new ItemStack(GenesisItems.malachite), 0.2F);
		GameRegistry.addSmelting(GenesisBlocks.olivine_ore, new ItemStack(GenesisItems.olivine), 0.3F);
		GameRegistry.addSmelting(GenesisBlocks.brown_flint_ore, Metadata.newStack(EnumNodule.BROWN_FLINT), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.marcasite_ore, Metadata.newStack(EnumNodule.MARCASITE), 0.05F);
		GameRegistry.addSmelting(GenesisItems.red_clay_bowl, new ItemStack(GenesisItems.ceramic_bowl), 0.3F);
		GameRegistry.addSmelting(GenesisItems.aphthoroblattina, new ItemStack(GenesisItems.cooked_aphthoroblattina), 0.35F);
		GameRegistry.addSmelting(GenesisItems.eryops_leg, new ItemStack(GenesisItems.cooked_eryops_leg), 0.35F);
		GameRegistry.registerFuelHandler(FuelHandler.INSTANCE);

		for (IMetaMulti meta : EnumDung.values())
		{
			GameRegistry.addRecipe(Metadata.newStack(meta, true), "CC", "CC", 'C', Metadata.newStack(meta));
		}
	}
}
