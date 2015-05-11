package genesis.common;

import java.util.*;

import genesis.block.tileentity.crafting.CookingPotRecipeRegistry;
import genesis.metadata.*;
import genesis.metadata.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.util.FuelHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

public final class GenesisRecipes
{
	public static void makeSubstituteCraftingItem(ItemStack vanillaItem, ItemStack modItem, Object... outputExclusions)
	{
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		ArrayList<IRecipe> replacedRecipes = new ArrayList<IRecipe>();

		for (IRecipe recipe : recipes)
		{
			ItemStack output = recipe.getRecipeOutput();
			boolean outputOK = true;

			for (Object obj : outputExclusions)
			{
				ItemStack stack = null;

				if (obj instanceof ItemStack)
				{
					stack = (ItemStack) obj;
				}
				else if (obj instanceof Item)
				{
					stack = new ItemStack((Item) obj);
				}
				else if (obj instanceof Block)
				{
					stack = new ItemStack((Block) obj);
				}

				if (stack != null && stack.isItemEqual(output))
				{
					outputOK = false;
				}
			}

			if (outputOK)
			{
				if (recipe instanceof ShapedRecipes)
				{
					ShapedRecipes shapedRecipe = (ShapedRecipes) recipe;

					boolean replaced = false;
					ItemStack[] replacedItems = new ItemStack[shapedRecipe.recipeItems.length];
					int i = 0;

					for (ItemStack stack : shapedRecipe.recipeItems)
					{
						if (vanillaItem.isItemEqual(stack))
						{
							ItemStack replacedStack = modItem.copy();
							replacedStack.stackSize = stack.stackSize;
							replacedItems[i] = replacedStack;
							replaced = true;
						}
						else
						{
							replacedItems[i] = stack;
						}

						i++;
					}

					if (replaced)
					{
						replacedRecipes.add(new ShapedRecipes(shapedRecipe.recipeWidth, shapedRecipe.recipeHeight, replacedItems, output));
					}
				}
				else if (recipe instanceof ShapedOreRecipe)
				{
					ShapedOreRecipe shapedOreRecipe = (ShapedOreRecipe) recipe;

					Object[] input = shapedOreRecipe.getInput();
					boolean replaced = false;
					Object[] replacedInput = new Object[input.length];

					for (int i = 0; i < input.length; i++)
					{
						Object replaceObj = input[i];

						if (replaceObj instanceof ItemStack)
						{
							ItemStack stack = (ItemStack) replaceObj;

							if (vanillaItem.isItemEqual(stack))
							{
								ItemStack replacedStack = modItem.copy();
								replacedStack.stackSize = stack.stackSize;
								replaceObj = replacedStack;
								replaced = true;
							}
						}

						replacedInput[i] = replaceObj;
					}

					if (replaced)
					{
						int width = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shapedOreRecipe, "width");
						int height = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shapedOreRecipe, "height");
						boolean mirrored = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shapedOreRecipe, "mirrored");

						ShapedOreRecipe replacedRecipe = new ShapedOreRecipe(output, mirrored, "z", 'z', Items.apple);
						ReflectionHelper.setPrivateValue(ShapedOreRecipe.class, replacedRecipe, width, "width");
						ReflectionHelper.setPrivateValue(ShapedOreRecipe.class, replacedRecipe, height, "height");
						ReflectionHelper.setPrivateValue(ShapedOreRecipe.class, replacedRecipe, replacedInput, "input");
						replacedRecipes.add(replacedRecipe);
					}
				}
				else if (recipe instanceof ShapelessRecipes)
				{
					ShapelessRecipes shapelessRecipe = (ShapelessRecipes) recipe;

					boolean replaced = false;
					ArrayList<ItemStack> replacedItems = new ArrayList<ItemStack>();
					for (ItemStack stack : (List<ItemStack>) shapelessRecipe.recipeItems)
					{
						if (stack.isItemEqual(vanillaItem))
						{
							ItemStack replacedStack = modItem.copy();
							replacedStack.stackSize = stack.stackSize;
							replacedItems.add(replacedStack);
							replaced = true;
						}
						else
						{
							replacedItems.add(stack);
						}
					}

					if (replaced)
					{
						replacedRecipes.add(new ShapelessRecipes(output, replacedItems));
					}
				}
			}
		}

		recipes.addAll(replacedRecipes);
	}

	public static void addRecipes()
	{
		FuelHandler.initialize();
		
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.red_clay), "CC", "CC", 'C', GenesisItems.red_clay_ball);
		GameRegistry.addRecipe(new ItemStack(GenesisItems.red_clay_bowl), "C C", " C ", 'C', GenesisItems.red_clay_ball);
		GameRegistry.addRecipe(new ItemStack(GenesisItems.red_clay_bucket), "X X", "X X", " X ", 'X', GenesisItems.red_clay_ball);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.calamites_bundle), "CCC", "CCC", "CCC", 'C', GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4), "x", "y", 'x', Items.coal, 'y', GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4), "x", "y", 'x', new ItemStack(Items.coal, 1, 1), 'y', GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4), "x", "y", 'x', GenesisItems.resin, 'y', GenesisItems.calamites);
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), GenesisItems.nodules.getStack(EnumNodule.MARCASITE), GenesisItems.tools.getStack(GenesisItems.tools.PEBBLE, EnumToolMaterial.BROWN_FLINT));
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.calamites, 9), GenesisBlocks.calamites_bundle);
		GameRegistry.addSmelting(GenesisBlocks.red_clay, new ItemStack(Blocks.stained_hardened_clay, 1, EnumDyeColor.WHITE.getMetadata()), 0.3F);
		GameRegistry.addSmelting(GenesisItems.red_clay_bucket, new ItemStack(GenesisItems.ceramic_bucket), 0.3F);
		GameRegistry.addSmelting(GenesisBlocks.quartz_ore, new ItemStack(GenesisItems.quartz), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.zircon_ore, new ItemStack(GenesisItems.zircon), 0.1F);
		GameRegistry.addSmelting(GenesisBlocks.garnet_ore, new ItemStack(GenesisItems.garnet), 0.1F);
		GameRegistry.addSmelting(GenesisBlocks.manganese_ore, new ItemStack(GenesisItems.manganese), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.hematite_ore, new ItemStack(GenesisItems.hematite), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.malachite_ore, new ItemStack(GenesisItems.malachite), 0.2F);
		GameRegistry.addSmelting(GenesisBlocks.olivine_ore, new ItemStack(GenesisItems.olivine), 0.3F);
		GameRegistry.addSmelting(GenesisBlocks.brown_flint_ore, GenesisItems.nodules.getStack(EnumNodule.BROWN_FLINT), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.marcasite_ore, GenesisItems.nodules.getStack(EnumNodule.MARCASITE), 0.05F);
		GameRegistry.addSmelting(GenesisItems.red_clay_bowl, GenesisItems.ceramic_bowls.getStack(EnumCeramicBowls.BOWL), 0.3F);
		GameRegistry.addSmelting(GenesisItems.aphthoroblattina, new ItemStack(GenesisItems.cooked_aphthoroblattina), 0.35F);
		GameRegistry.addSmelting(GenesisItems.eryops_leg, new ItemStack(GenesisItems.cooked_eryops_leg), 0.35F);

		for (EnumDung variant : EnumDung.values())
		{
			GameRegistry.addRecipe(GenesisBlocks.dungs.getStack(DungBlocksAndItems.DUNG_BLOCK, variant), "CC", "CC", 'C', GenesisBlocks.dungs.getStack(GenesisBlocks.dungs.DUNG, variant));
		}
		
		CookingPotRecipeRegistry.registerShapeless(new ItemStack(GenesisItems.cooked_tyrannosaurus),
				new ItemStack(GenesisItems.tyrannosaurus), new ItemStack(GenesisItems.eryops_leg));
	}
	
	/**
	 * Called as late as possible so as to have all other recipes registered already.
	 */
	public static void doSubstitutes()
	{
		// Fiber for string
		makeSubstituteCraftingItem(new ItemStack(Items.string), new ItemStack(GenesisItems.sphenophyllum_fiber), Blocks.wool);
		
		// Ceramic buckets for vanilla buckets
		makeSubstituteCraftingItem(new ItemStack(Items.bucket), new ItemStack(GenesisItems.ceramic_bucket));
		makeSubstituteCraftingItem(new ItemStack(Items.water_bucket), new ItemStack(GenesisItems.ceramic_bucket_water));
		makeSubstituteCraftingItem(new ItemStack(Items.milk_bucket), new ItemStack(GenesisItems.ceramic_bucket_milk));
		
		// Dyes
		for (IMetadata variant : GenesisItems.ceramic_bowls.getValidVariants(ItemsCeramicBowls.dyes))
		{
			EnumDye dye = (EnumDye) variant;
			// TODO: Doesn't seem to work. Must fix sometime.
			makeSubstituteCraftingItem(new ItemStack(Items.dye, 1, dye.getColor().getDyeDamage()),
					GenesisItems.ceramic_bowls.getStack(dye));
		}
	}
}
