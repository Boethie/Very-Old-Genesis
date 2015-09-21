package genesis.common;

import java.util.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.*;

import genesis.block.tileentity.*;
import genesis.block.tileentity.crafting.*;
import genesis.metadata.*;
import genesis.metadata.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.metadata.ToolItems.ToolObjectType;
import genesis.metadata.ToolTypes.ToolType;
import genesis.util.*;
import genesis.util.render.*;
import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
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
			return GenesisBlocks.ores.getDrop(EnumOre.QUARTZ);
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

	protected static void addToolRecipe(ItemStack toolHead, ToolObjectType<?, ?> type, EnumToolMaterial material, Object... shape)
	{
		toolHead = toolHead.copy();
		ItemStack toolChipped = GenesisItems.tools.getBadStack(type, material);
		//ItemStack toolGood = GenesisItems.tools.getGoodStack(type, material);
		
		for (ItemStack billet : GenesisBlocks.trees.getSubItems(TreeBlocksAndItems.BILLET))
		{
			Object[] paramsBase = ArrayUtils.addAll(shape,
					'B', billet,
					'T', toolHead);
			Object[] paramsFiber = ArrayUtils.addAll(paramsBase,
					'S', GenesisItems.sphenophyllum_fiber);
			Object[] paramsString = ArrayUtils.addAll(paramsBase,
					'S', Items.string);
			
			GameRegistry.addShapedRecipe(toolChipped, paramsFiber);
			GameRegistry.addShapedRecipe(toolChipped, paramsString);
		}
	}
	
	protected static void addToolRecipe(ItemStack toolHead, ToolObjectType<?, ?> type, EnumToolMaterial material)
	{
		addToolRecipe(toolHead, type, material,
				" T ",
				"SBS",
				" B ");
	}
	
	public static void addRecipes()
	{
		FuelHandler.initialize();

		//torches
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.calamites_bundle), "CCC", "CCC", "CCC", 'C', GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4), "X", "Y", 'X', Items.coal, 'Y', GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4), "X", "Y", 'X', new ItemStack(Items.coal, 1, 1), 'Y', GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4), "X", "Y", 'X', GenesisItems.resin, 'Y', GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_tall_torch, 2), "X", "Y", "Y", 'X', Items.coal, 'Y' , GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_tall_torch, 2), "X", "Y", "Y", 'X', new ItemStack(Items.coal, 1, 1), 'Y' , GenesisItems.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_tall_torch, 2), "X", "Y", "Y", 'X', GenesisItems.resin, 'Y' , GenesisItems.calamites);

		GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.calamites, 9), GenesisBlocks.calamites_bundle);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.programinis_bundle), "CCC", "CCC", "CCC", 'C', GenesisItems.programinis);
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.programinis, 9), GenesisBlocks.programinis_bundle);
		
		EnumToolMaterial[] flintMaterials = {EnumToolMaterial.BLACK_FLINT, EnumToolMaterial.BROWN_FLINT};
		
		// Flint and marcasite recipe
		for (EnumToolMaterial mat : flintMaterials)
		{
			GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite),
					GenesisItems.nodules.getStack(EnumNodule.MARCASITE),
					GenesisItems.tools.getStack(ToolItems.PEBBLE, mat));	// Pebble
			GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite),
					GenesisItems.nodules.getStack(EnumNodule.MARCASITE),
					GenesisItems.nodules.getStack(EnumNodule.fromToolMaterial(mat)));	// Nodule
		}
		
		// All recipes with only logs, and one constant output.
		for (Item log : GenesisBlocks.trees.getItems(TreeBlocksAndItems.LOG))
		{
			GameRegistry.addSmelting(new ItemStack(log, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.coal, 1, 1), 0.15F);
		}
		
		// All recipes involving billets.
		for (EnumTree variant : GenesisBlocks.trees.getSharedValidVariants(TreeBlocksAndItems.LOG, TreeBlocksAndItems.BILLET))
		{
			ItemStack logStack = GenesisBlocks.trees.getStack(TreeBlocksAndItems.LOG, variant, 1);
			ItemStack billetStack = GenesisBlocks.trees.getStack(TreeBlocksAndItems.BILLET, variant, 4);
			
			if (variant == EnumTree.SIGILLARIA || variant == EnumTree.LEPIDODENDRON)
			{
				billetStack.stackSize = 1;
			}
			
			// Logs -> billets
			GameRegistry.addShapelessRecipe(billetStack, logStack);
			
			// All recipes after this point use 1 billet.
			billetStack = GenesisBlocks.trees.getStack(TreeBlocksAndItems.BILLET, variant, 1);
			
			// Storage box
			GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.storage_box),
					"BBB",
					"B B",
					"BBB",
					'B', billetStack);
			
			// Wattle fence
			GameRegistry.addShapedRecipe(GenesisBlocks.trees.getStack(TreeBlocksAndItems.WATTLE_FENCE, variant, 3),
					"BBB",
					"BBB",
					'B', billetStack);
			
			// Vanilla torch
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.torch, 4),
					"c",
					"B",
					'c', new ItemStack(Items.coal, 1, OreDictionary.WILDCARD_VALUE),
					'B', billetStack);
			
			// Campfire
			for (ItemStack pebble : GenesisItems.tools.getSubItems(ToolItems.PEBBLE))
			{	// TODO: Add variants for pebbles and billets.
				GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.campfire),
						"III",
						"I I",
						"ooo",
						'I', billetStack,
						'o', pebble);
			}
		}
		
		// Register knapping tools and
		// Register workbench recipes.
		for (ItemStack pebble : GenesisItems.tools.getSubItems(ToolItems.PEBBLE))
		{
			pebble.setItemDamage(OreDictionary.WILDCARD_VALUE);
			KnappingRecipeRegistry.registerKnappingTool(pebble);
			
			for (ItemStack log : GenesisBlocks.trees.getSubItems(TreeBlocksAndItems.LOG))
			{
				GameRegistry.addRecipe(new ItemStack(GenesisBlocks.workbench),
						"o",
						"L",
						'o', pebble,
						'L', log);
			}
		}
		
		// Register knapping materials.
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
		
		// Add knapping recipes from map created earlier.
		for (EnumToolMaterial material : EnumToolMaterial.values())
		{
			ItemStack axeHead = GenesisItems.tools.getBadStack(ToolItems.AXE_HEAD, material, 1);
			addToolRecipe(axeHead, ToolItems.AXE, material);
			
			ItemStack pickHead = GenesisItems.tools.getBadStack(ToolItems.PICK_HEAD, material, 1);
			addToolRecipe(pickHead, ToolItems.PICK, material);
			
			ItemStack hoeHead = GenesisItems.tools.getBadStack(ToolItems.HOE_HEAD, material, 1);
			addToolRecipe(hoeHead, ToolItems.HOE, material);
			
			ItemStack knifeHead = GenesisItems.tools.getBadStack(ToolItems.KNIFE_HEAD, material, 1);
			addToolRecipe(knifeHead, ToolItems.KNIFE, material,
					" T",
					"SB");
			
			ItemStack spearHead = GenesisItems.tools.getBadStack(ToolItems.SPEAR_HEAD, material, 1);
			addToolRecipe(spearHead, ToolItems.SPEAR, material);
			
			for (Pair<ItemStack, Integer> pair : materials.get(material))
			{
				ItemStack matStack = pair.getLeft();
				int stackSize = pair.getRight();
				float xp = 0.5F * stackSize;
				
				// Axe
				axeHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(axeHead,
						3, 3,
						matStack, xp,
						true,	false,	false,
						true,	true,	true,
						true,	false,	false);
				
				// Pickaxe
				pickHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(pickHead,
						3, 1,
						matStack, xp,
						true,	true,	true);
				
				// Hoe
				hoeHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(hoeHead,
						3, 2,
						matStack, xp,
						true,	true,	true,
						false,	false,	true);
				
				// Knife
				knifeHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(knifeHead,
						1, 2,
						matStack, xp,
						true,
						true);
				
				// Spear
				spearHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(spearHead,
						3, 3,
						matStack, xp,
						false,	true,	false,
						true,	true,	true,
						true,	true,	true);
			}
		}
		
		// Dung storage
		for (EnumDung variant : EnumDung.values())
		{
			GameRegistry.addRecipe(GenesisBlocks.dungs.getStack(DungBlocksAndItems.DUNG_BLOCK, variant), "CC", "CC", 'C', GenesisBlocks.dungs.getStack(DungBlocksAndItems.DUNG, variant));
		}
		
		// Smelting
		for (EnumOre ore : GenesisBlocks.ores.getSharedValidVariants(OreBlocks.ORE, OreBlocks.DROP))
		{
			ItemStack oreStack = GenesisBlocks.ores.getOreStack(ore);
			ItemStack dropStack = GenesisBlocks.ores.getDrop(ore);
			GameRegistry.addSmelting(oreStack, dropStack, ore.getSmeltingExperience());
		}
		
		GameRegistry.addSmelting(GenesisBlocks.ores.getOreStack(EnumOre.MARCASITE), GenesisItems.nodules.getStack(EnumNodule.MARCASITE), EnumOre.MARCASITE.getSmeltingExperience());
		
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
		
		ItemStack mabelia = GenesisBlocks.plants.getPlantStack(EnumPlant.MABELIA);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.RED), mabelia, mabelia);
		
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.ORANGE), calamites, mabelia);
		
		ItemStack odontHead = new ItemStack(GenesisItems.odontopteris_fiddlehead);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(GenesisDye.get(EnumDyeColor.LIME)), odontHead, odontHead);
		
		ItemStack protoFlesh = new ItemStack(GenesisItems.prototaxites_flesh);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(GenesisDye.get(EnumDyeColor.BROWN)), protoFlesh, protoFlesh);
		
		// Cooking pot recipes with vanilla items
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.ORANGE), calamites, new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()));
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.ORANGE), mabelia, new ItemStack(Items.dye, 1, EnumDyeColor.YELLOW.getDyeDamage()));
		
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.PINK), mabelia, new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()));
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
			GenesisDye dye = (GenesisDye) variant;
			// TODO: Doesn't seem to work. Must fix sometime.
			makeSubstituteCraftingItem(new ItemStack(Items.dye, 1, dye.getColor().getDyeDamage()),
					GenesisItems.bowls.getStack(dye));
		}
	}
}
