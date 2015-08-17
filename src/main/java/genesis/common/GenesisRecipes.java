package genesis.common;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.*;

import genesis.block.tileentity.*;
import genesis.block.tileentity.crafting.*;
import genesis.metadata.*;
import genesis.metadata.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.metadata.ToolTypes.ToolType;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;
import genesis.util.render.*;
import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.*;

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
	
	protected static ItemStack getFullBlockToolCraftingMaterial(EnumToolMaterial material)
	{
		switch (material)
		{
		case OCTAEDRITE:
			return new ItemStack(GenesisBlocks.octaedrite);
		case DOLERITE:
			return new ItemStack(GenesisBlocks.dolerite);
		case RHYOLITE:
			return new ItemStack(GenesisBlocks.rhyolite);
		case GRANITE:
			return new ItemStack(GenesisBlocks.granite);
		case QUARTZ:
			return new ItemStack(GenesisItems.quartz);
		case BROWN_FLINT:
		case BLACK_FLINT:
		}
		
		return null;
	}
	
	protected static ISpriteUVs getToolCraftingMaterialSprite(EnumToolMaterial material)
	{
		switch (material)
		{
		case OCTAEDRITE:
			return new BlockSpriteUVs(GenesisBlocks.octaedrite);
		case DOLERITE:
			return new BlockSpriteUVs(GenesisBlocks.dolerite);
		case RHYOLITE:
			return new BlockSpriteUVs(GenesisBlocks.rhyolite);
		case GRANITE:
			return new BlockSpriteUVs(GenesisBlocks.granite);
		case QUARTZ:
			return new SpriteUVs(new ResourceLocation(Constants.ASSETS_PREFIX + "textures/blocks/quartz_block.png"), 0, 0, 1, 1);
		case BROWN_FLINT:
			return new SpriteUVs(new ResourceLocation(Constants.ASSETS_PREFIX + "textures/blocks/brown_flint.png"), 0, 0, 1, 1);
		case BLACK_FLINT:
			return new SpriteUVs(new ResourceLocation(Constants.ASSETS_PREFIX + "textures/blocks/black_flint.png"), 0, 0, 1, 1);
		}
		
		return null;
	}

	public static void addRecipes()
	{
		FuelHandler.initialize();
		
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.calamites_bundle), "CCC", "CCC", "CCC", 'C', GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4), "X", "Y", 'X', Items.coal, 'Y', GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4), "X", "Y", 'X', new ItemStack(Items.coal, 1, 1), 'Y', GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4), "X", "Y", 'X', GenesisItems.resin, 'Y', GenesisItems.calamites);
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), GenesisItems.nodules.getStack(EnumNodule.MARCASITE), GenesisItems.tools.getStack(GenesisItems.tools.PEBBLE, EnumToolMaterial.BROWN_FLINT));
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite), GenesisItems.nodules.getStack(EnumNodule.MARCASITE), GenesisItems.tools.getStack(GenesisItems.tools.PEBBLE, EnumToolMaterial.BLACK_FLINT));
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.calamites, 9), GenesisBlocks.calamites_bundle);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.programinis_bundle), "CCC", "CCC", "CCC", 'C', GenesisItems.programinis);
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.programinis, 9), GenesisBlocks.programinis_bundle);
		
		for (EnumDung variant : EnumDung.values())
		{
			GameRegistry.addRecipe(GenesisBlocks.dungs.getStack(DungBlocksAndItems.DUNG_BLOCK, variant), "CC", "CC", 'C', GenesisBlocks.dungs.getStack(GenesisBlocks.dungs.DUNG, variant));
		}
		
		for (EnumTree treeVariant : GenesisBlocks.trees.getValidVariants(TreeBlocksAndItems.BILLET))
		{
			for (ToolType pebbleVariant : GenesisItems.tools.getValidVariants(ToolItems.PEBBLE))
			{
				GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.campfire),
						"III",
						"I I",
						"ooo",
						'I', GenesisBlocks.trees.getStack(TreeBlocksAndItems.BILLET, treeVariant),
						'o', GenesisItems.tools.getStack(ToolItems.PEBBLE, pebbleVariant));
			}
		}
		
		// Smelting
		GameRegistry.addSmelting(GenesisBlocks.quartz_ore, new ItemStack(GenesisItems.quartz), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.zircon_ore, new ItemStack(GenesisItems.zircon), 0.1F);
		GameRegistry.addSmelting(GenesisBlocks.garnet_ore, new ItemStack(GenesisItems.garnet), 0.1F);
		GameRegistry.addSmelting(GenesisBlocks.hematite_ore, new ItemStack(Items.iron_ingot), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.manganese_ore, new ItemStack(GenesisItems.manganese), 0.05F);
		GameRegistry.addSmelting(GenesisBlocks.malachite_ore, new ItemStack(GenesisItems.malachite), 0.2F);
		GameRegistry.addSmelting(GenesisBlocks.olivine_ore, new ItemStack(GenesisItems.olivine), 0.3F);
		GameRegistry.addSmelting(GenesisBlocks.marcasite_ore, GenesisItems.nodules.getStack(EnumNodule.MARCASITE), 0.05F);
		
		// Food
		GameRegistry.addSmelting(GenesisItems.climatius, new ItemStack(GenesisItems.cooked_climatius), 0.35F);
		GameRegistry.addSmelting(GenesisItems.meganeura, new ItemStack(GenesisItems.cooked_meganeura), 0.35F);
		GameRegistry.addSmelting(GenesisItems.aphthoroblattina, new ItemStack(GenesisItems.cooked_aphthoroblattina), 0.35F);
		GameRegistry.addSmelting(GenesisItems.eryops_leg, new ItemStack(GenesisItems.cooked_eryops_leg), 0.35F);
		GameRegistry.addSmelting(GenesisItems.liopleurodon, new ItemStack(GenesisItems.cooked_liopleurodon), 0.35F);
		GameRegistry.addSmelting(GenesisItems.tyrannosaurus, new ItemStack(GenesisItems.cooked_tyrannosaurus), 0.35F);
		
		// Pottery
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.red_clay), "CC", "CC", 'C', GenesisItems.red_clay_ball);
		GameRegistry.addSmelting(GenesisBlocks.red_clay, new ItemStack(Blocks.stained_hardened_clay, 1, EnumDyeColor.WHITE.getMetadata()), 0.3F);
		
		GameRegistry.addRecipe(new ItemStack(GenesisItems.red_clay_bowl), "C C", " C ", 'C', GenesisItems.red_clay_ball);
		GameRegistry.addSmelting(GenesisItems.red_clay_bowl, TileEntityCampfire.registerAllowedOutput(GenesisItems.bowls.getStack(EnumCeramicBowls.BOWL)), 0.3F);
		
		GameRegistry.addRecipe(new ItemStack(GenesisItems.red_clay_bucket), "X X", "X X", " X ", 'X', GenesisItems.red_clay_ball);
		GameRegistry.addSmelting(GenesisItems.red_clay_bucket, TileEntityCampfire.registerAllowedOutput(new ItemStack(GenesisItems.ceramic_bucket)), 0.3F);
		
		// Cooking pot recipes
		ItemStack calamites = new ItemStack(GenesisItems.calamites);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.YELLOW), calamites, calamites);
		
		ItemStack mabelia = GenesisBlocks.plants.getStack(EnumPlant.MABELIA);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.RED), mabelia, mabelia);
		
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.ORANGE), calamites, mabelia);
		
		ItemStack odontHead = new ItemStack(GenesisItems.odontopteris_fiddlehead);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDye.get(EnumDyeColor.LIME)), odontHead, odontHead);
		
		ItemStack protoFlesh = new ItemStack(GenesisItems.prototaxites_flesh);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDye.get(EnumDyeColor.BROWN)), protoFlesh, protoFlesh);
		
		// Cooking pot recipes with vanilla items
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.ORANGE), calamites, new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()));
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.ORANGE), mabelia, new ItemStack(Items.dye, 1, EnumDyeColor.YELLOW.getDyeDamage()));
		
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.PINK), mabelia, new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()));
		
		// Knapping recipes
		for (ItemStack tool : GenesisItems.tools.getSubItems(ToolItems.PEBBLE))
		{
			tool.setItemDamage(OreDictionary.WILDCARD_VALUE);
			KnappingRecipeRegistry.registerKnappingTool(tool);
		}
		
		Multimap<EnumToolMaterial, Pair<ItemStack, Integer>> materials = HashMultimap.create();
		List<ToolType> validPebbles = GenesisItems.tools.getValidVariants(ToolItems.PEBBLE);
		
		for (EnumToolMaterial material : EnumToolMaterial.values())
		{
			ISpriteUVs sprite = getToolCraftingMaterialSprite(material);
			
			ItemStack stack;
			
			// Pebble
			if (validPebbles.contains(ToolTypes.getToolHead(material, ToolItems.PEBBLE.getSoleQuality())))
			{
				stack = GenesisItems.tools.getStack(ToolItems.PEBBLE, material);
				
				if (stack != null)
				{
					materials.put(material, Pair.of(stack, 1));
					KnappingRecipeRegistry.registerMaterialData(stack,
							15, 1, 1,
							GenesisItems.tools.getStack(ToolItems.FLAKE, material, 1),
							sprite);
				}
			}
			
			// Nodule
			EnumNodule nodule = EnumNodule.fromToolMaterial(material);
			
			if (nodule != null)
			{
				stack = GenesisItems.nodules.getStack(nodule);
				
				if (stack != null)
				{
					materials.put(material, Pair.of(stack, 1));
					KnappingRecipeRegistry.registerMaterialData(stack,
							20, 1, 1,
							GenesisItems.tools.getStack(ToolItems.FLAKE, material, 1),
							sprite);
				}
			}
			
			// Full block
			stack = getFullBlockToolCraftingMaterial(material);
			
			if (stack != null)
			{
				materials.put(material, Pair.of(stack, 2));
				KnappingRecipeRegistry.registerMaterialData(stack,
						40, 1, 2,
						GenesisItems.tools.getStack(ToolItems.FLAKE, material, 2),
						sprite);
			}
		}
		
		for (EnumToolMaterial material : EnumToolMaterial.values())
		{
			for (Pair<ItemStack, Integer> pair : materials.get(material))
			{
				EnumToolQuality quality = EnumToolQuality.CHIPPED;
				
				// Axe
				ItemStack toolHead = GenesisItems.tools.getStack(ToolItems.AXE_HEAD, material, quality, pair.getRight());
				KnappingRecipeRegistry.registerRecipe(toolHead,
						3, 3,
						pair.getLeft(),
						true,	false,	false,
						true,	true,	true,
						true,	false,	false);
				
				// Pickaxe
				toolHead = GenesisItems.tools.getStack(ToolItems.PICK_HEAD, material, quality, pair.getRight());
				KnappingRecipeRegistry.registerRecipe(toolHead,
						3, 1,
						pair.getLeft(),
						true,	true,	true);
				
				// Hoe
				toolHead = GenesisItems.tools.getStack(ToolItems.HOE_HEAD, material, quality, pair.getRight());
				KnappingRecipeRegistry.registerRecipe(toolHead,
						3, 2,
						pair.getLeft(),
						true,	true,	true,
						false,	false,	true);
				
				// Knife
				toolHead = GenesisItems.tools.getStack(ToolItems.KNIFE_HEAD, material, quality, pair.getRight());
				KnappingRecipeRegistry.registerRecipe(toolHead,
						1, 2,
						pair.getLeft(),
						true,
						true);
				
				// Spear
				toolHead = GenesisItems.tools.getStack(ToolItems.SPEAR_HEAD, material, quality, pair.getRight());
				KnappingRecipeRegistry.registerRecipe(toolHead,
						3, 3,
						pair.getLeft(),
						false,	true,	false,
						true,	true,	true,
						true,	true,	true);
			}
		}
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
		for (IMetadata variant : GenesisItems.bowls.getValidVariants(ItemsCeramicBowls.DYES))
		{
			EnumDye dye = (EnumDye) variant;
			// TODO: Doesn't seem to work. Must fix sometime.
			makeSubstituteCraftingItem(new ItemStack(Items.dye, 1, dye.getColor().getDyeDamage()),
					GenesisItems.bowls.getStack(dye));
		}
	}
}
