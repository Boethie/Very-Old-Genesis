package genesis.common;

import java.util.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.*;

import genesis.block.tileentity.*;
import genesis.block.tileentity.crafting.*;
import genesis.combo.*;
import genesis.combo.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.combo.ToolItems.ToolObjectType;
import genesis.combo.variant.*;
import genesis.combo.variant.MultiMetadataList.MultiMetadata;
import genesis.item.*;
import genesis.recipes.*;
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
		ArrayList<IRecipe> replacedRecipes = new ArrayList<>();
		
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
					ArrayList<ItemStack> replacedItems = new ArrayList<>();
					for (ItemStack stack : shapelessRecipe.recipeItems)
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
					'S', GenesisItems.materials.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER));
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

		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.smooth_limestone, 4),
				"LL",
				"LL",
				'L', GenesisBlocks.limestone);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.trap_floor),
				"xxx",
				"///",
				'x', GenesisItems.materials.getStack(EnumMaterial.CLADOPHLEBIS_FROND),
				'/', GenesisBlocks.calamites);
		
		//Food
		GameRegistry.addShapelessRecipe(GenesisItems.seeds.getStack(EnumSeeds.ARAUCARIOXYLON_SEEDS), GenesisBlocks.trees.getStack(TreeBlocksAndItems.FRUIT, EnumTree.ARAUCARIOXYLON));
		
		//Torches
		ItemStack resin = GenesisItems.materials.getStack(EnumMaterial.RESIN);

		GameRegistry.addShapedRecipe(new ItemStack(Blocks.torch, 4),
				"o",
				"|",
				'o', resin, '|', Items.stick);

		// Calamites torch
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4),
				"o",
				"|",
				'o', Items.coal, '|', GenesisBlocks.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4),
				"o",
				"|",
				'o', new ItemStack(Items.coal, 1, 1), '|', GenesisBlocks.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch, 4),
				"o",
				"|",
				'o', resin, '|', GenesisBlocks.calamites);
		
		// Tall calamites torch
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch_tall, 2),
				"o",
				"|",
				"|",
				'o', Items.coal, '|', GenesisBlocks.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch_tall, 2),
				"o",
				"|",
				"|",
				'o', new ItemStack(Items.coal, 1, 1), '|', GenesisBlocks.calamites);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.calamites_torch_tall, 2),
				"o",
				"|",
				"|",
				'o', resin, '|', GenesisBlocks.calamites);
		
		// Bundles
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.calamites_bundle),
				"CCC",
				"CCC",
				"CCC",
				'C', GenesisBlocks.calamites);
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisBlocks.calamites, 9), GenesisBlocks.calamites_bundle);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.programinis_bundle),
				"CCC",
				"CCC",
				"CCC",
				'C', GenesisItems.materials.getStack(EnumMaterial.PROGRAMINIS));
		GameRegistry.addShapelessRecipe(GenesisItems.materials.getStack(EnumMaterial.PROGRAMINIS, 9), GenesisBlocks.programinis_bundle);

		// Roofs
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.calamites_roof, 4),
				"#  ",
				"## ",
				"###",
				'#', GenesisBlocks.calamites);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.calamites_roof, 4),
				"  #",
				" ##",
				"###",
				'#', GenesisBlocks.calamites);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.programinis_roof, 4),
				"#  ",
				"## ",
				"###",
				'#', GenesisBlocks.programinis);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.programinis_roof, 4),
				"  #",
				" ##",
				"###",
				'#', GenesisBlocks.programinis);
		
		// Silt to siltstone recipes
		for (EnumSilt mat: EnumSilt.values())
		{
			GameRegistry.addShapedRecipe(GenesisBlocks.silt.getStack(SiltBlocks.SILTSTONE, mat),
					"ss",
					"ss",
					's', GenesisBlocks.silt.getStack(SiltBlocks.SILT, mat));
		}
		
		// Flint and marcasite recipe
		EnumToolMaterial[] flintMaterials = {EnumToolMaterial.BLACK_FLINT, EnumToolMaterial.BROWN_FLINT};

		for (EnumToolMaterial mat : flintMaterials)
		{
			GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite),
					GenesisItems.nodules.getStack(EnumNodule.MARCASITE),
					GenesisItems.tools.getStack(ToolItems.PEBBLE, mat));	// Pebble
			GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.flint_and_marcasite),
					GenesisItems.nodules.getStack(EnumNodule.MARCASITE),
					GenesisItems.nodules.getStack(EnumNodule.fromToolMaterial(mat)));	// Nodule
		}
		
		for (EnumTree tree : EnumTree.values())
		{
			// All recipes involving debris
			if (tree.hasDebris())
			{
				// Leaves -> Debris
				GameRegistry.addShapelessRecipe(GenesisBlocks.debris.getStack(tree, 4),
						GenesisBlocks.trees.getStack(TreeBlocksAndItems.LEAVES, tree));
				
				// Humus
				GameRegistry.addShapelessRecipe(new ItemStack(GenesisBlocks.humus),
						new ItemStack(Blocks.dirt),
						GenesisBlocks.debris.getStack(tree));
			}
			
			// Porridge
			ItemStack porridge = null;
			
			switch (tree)
			{
			case GINKGO:
				porridge = GenesisItems.bowls.getStack(EnumDish.PORRIDGE_GINKGO);
				break;
			case FICUS:
				porridge = GenesisItems.bowls.getStack(EnumDish.PORRIDGE_FIG);
				break;
			case LAUROPHYLLUM:
				porridge = GenesisItems.bowls.getStack(EnumDish.PORRIDGE_LAUROPHYLLUM);
				break;
			default:
				break;
			}
			
			if (porridge != null)
			{
				CookingPotRecipeRegistry.registerShapeless(porridge,
						GenesisItems.materials.getStack(EnumMaterial.PROGRAMINIS),
						GenesisBlocks.trees.getStack(TreeBlocksAndItems.FRUIT, tree));
			}
		}
		
		GameRegistry.addShapelessRecipe(GenesisBlocks.debris.getStack(EnumDebrisOther.CALAMITES), GenesisBlocks.calamites);
		
		// All recipes with only logs, and one constant output.
		for (Item log : GenesisBlocks.trees.getItems(TreeBlocksAndItems.LOG))
		{
			GameRegistry.addSmelting(new ItemStack(log, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.coal, 1, 1), 0.15F);
		}
		
		// Logs -> billets
		for (EnumTree variant : GenesisBlocks.trees.getSharedValidVariants(TreeBlocksAndItems.LOG, TreeBlocksAndItems.BILLET))
		{
			ItemStack logStack = GenesisBlocks.trees.getStack(TreeBlocksAndItems.LOG, variant, 1);
			ItemStack billetStack = GenesisBlocks.trees.getStack(TreeBlocksAndItems.BILLET, variant, 4);
			
			if (variant == EnumTree.SIGILLARIA || variant == EnumTree.LEPIDODENDRON)
			{
				billetStack.stackSize = 1;
			}
			
			GameRegistry.addShapelessRecipe(billetStack, logStack);
		}

		// Branch -> billet
		for (EnumTree variant : GenesisBlocks.trees.getSharedValidVariants(TreeBlocksAndItems.BRANCH, TreeBlocksAndItems.BILLET)) {
			ItemStack branchStack = GenesisBlocks.trees.getStack(TreeBlocksAndItems.BRANCH, variant, 1);
			ItemStack billetStack = GenesisBlocks.trees.getStack(TreeBlocksAndItems.BILLET, variant, 1);

			GameRegistry.addShapelessRecipe(billetStack, branchStack);
		}
		
		// All recipes involving billets.
		for (EnumTree variant : GenesisBlocks.trees.getValidVariants(TreeBlocksAndItems.BILLET))
		{
			ItemStack billetStack = GenesisBlocks.trees.getStack(TreeBlocksAndItems.BILLET, variant, 1);

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
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.torch, 4),
					"r",
					"B",
					'r', GenesisItems.materials.getStack(EnumMaterial.RESIN),
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
			
			//Rack
			GameRegistry.addShapedRecipe(GenesisBlocks.trees.getStack(TreeBlocksAndItems.RACK, variant),
					"XX",
					'X', billetStack);

			// Rope ladder
			GameRegistry.addRecipe(new ItemStack(GenesisBlocks.rope_ladder, 3),
					"S S",
					"BBB",
					"S S",
					'S', GenesisItems.materials.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER),
					'B', billetStack);

			// Bow
			if (variant.hasBow())
			{
				GameRegistry.addRecipe(GenesisItems.bows.getStack(EnumBowType.SELF, variant),
						" S",
						"BS",
						" S",
						'S', GenesisItems.materials.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER),
						'B', billetStack);
				GameRegistry.addRecipe(GenesisItems.bows.getStack(EnumBowType.SELF, variant),
						"S ",
						"SB",
						"S ",
						'S', GenesisItems.materials.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER),
						'B', billetStack);
			}
		}

		// All rubble recipes
		for (EnumRubble variant : EnumRubble.values())
		{
			ItemStack rubbleStack = GenesisBlocks.rubble.getStack(variant);

			// Stone -> Rubble
			ItemStack modelStack = variant.getModelStack();
			GameRegistry.addShapelessRecipe(rubbleStack, modelStack);

			// Wall
			Block wallBlock = null;

			switch (variant)
			{
			case GRANITE:
				wallBlock = GenesisBlocks.granite_rubble_wall;
				break;
			case MOSSY_GRANITE:
				wallBlock = GenesisBlocks.mossy_granite_rubble_wall;
				break;
			case RHYOLITE:
				wallBlock = GenesisBlocks.rhyolite_rubble_wall;
				break;
			case DOLERITE:
				wallBlock = GenesisBlocks.dolerite_rubble_wall;
				break;
			}

			if (wallBlock != null)
			{
				GameRegistry.addRecipe(new ItemStack(wallBlock, 6),
						"RRR",
						"RRR",
						'R', rubbleStack);
			}
		}

		// All slab recipes
		for (ObjectType<EnumSlab, ?, ?> type : GenesisBlocks.slabs.getTypes())
		{
			for (EnumSlab variant : GenesisBlocks.slabs.getValidVariants(type))
			{
				ItemStack slabStack = GenesisBlocks.slabs.getStack(type, variant, 6);
				ItemStack modelStack = variant.getModelStack();
				GameRegistry.addRecipe(slabStack, "SSS", 'S', modelStack);
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
		
		for (EnumToolMaterial material : EnumToolMaterial.values())
		{
			ISpriteUVs sprite = getToolCraftingMaterialSprite(material);
			
			ItemStack stack;
			ItemStack flake = GenesisItems.tools.getStack(ToolItems.FLAKE, material);
			
			// Pebble
			if (GenesisItems.tools.containsVariant(ToolItems.PEBBLE, material))
			{
				stack = GenesisItems.tools.getStack(ToolItems.PEBBLE, material);
				
				if (stack != null)
				{
					materials.put(material, Pair.of(stack, 1));
					KnappingRecipeRegistry.registerMaterialData(stack,
							15, 1, 1,
							flake, 0.5F,
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
							flake, 0.5F,
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
						flake, 1,
						sprite);
			}
		}
		
		// Add knapping recipes from map created earlier.
		for (EnumToolMaterial material : EnumToolMaterial.values())
		{
			ItemStack axeHead = GenesisItems.tools.getBadStack(ToolItems.AXE_HEAD, material, 1);
			addToolRecipe(axeHead, ToolItems.AXE, material);
			
			ItemStack shovelHead = GenesisItems.tools.getBadStack(ToolItems.SHOVEL_HEAD, material, 1);
			addToolRecipe(shovelHead, ToolItems.SHOVEL, material);
			
			ItemStack pickHead = GenesisItems.tools.getBadStack(ToolItems.PICKAXE_HEAD, material, 1);
			addToolRecipe(pickHead, ToolItems.PICKAXE, material);
			
			ItemStack hoeHead = GenesisItems.tools.getBadStack(ToolItems.HOE_HEAD, material, 1);
			addToolRecipe(hoeHead, ToolItems.HOE, material);
			
			ItemStack knifeHead = GenesisItems.tools.getBadStack(ToolItems.KNIFE_HEAD, material, 1);
			addToolRecipe(knifeHead, ToolItems.KNIFE, material,
					" T",
					"SB");
			
			ItemStack clubHead = GenesisItems.tools.getBadStack(ToolItems.CLUB_HEAD, material, 1);
			addToolRecipe(clubHead, ToolItems.CLUB, material);
			
			ItemStack spearHead = GenesisItems.tools.getBadStack(ToolItems.SPEAR_HEAD, material, 1);
			addToolRecipe(spearHead, ToolItems.SPEAR, material);
			
			for (Pair<ItemStack, Integer> pair : materials.get(material))
			{
				ItemStack matStack = pair.getLeft();
				int stackSize = pair.getRight();
				float xp = 0.5F * stackSize;
				
				// Axe
				axeHead = axeHead.copy();
				axeHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(axeHead,
						3, 3,
						matStack, xp,
						true,	false,	false,
						true,	true,	true,
						true,	false,	false);
				
				// Shovel
				shovelHead = shovelHead.copy();
				shovelHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(shovelHead,
						2, 3,
						matStack, xp,
						true,	true,
						true,	true,
						true,	true);
				
				// Pickaxe
				pickHead = pickHead.copy();
				pickHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(pickHead,
						3, 1,
						matStack, xp,
						true,	true,	true);
				
				// Hoe
				hoeHead = hoeHead.copy();
				hoeHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(hoeHead,
						3, 2,
						matStack, xp,
						true,	true,	true,
						false,	false,	true);
				
				// Knife
				knifeHead = knifeHead.copy();
				knifeHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(knifeHead,
						1, 2,
						matStack, xp,
						true,
						true);
				
				// Club
				clubHead = clubHead.copy();
				clubHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(clubHead,
						2, 2,
						matStack, xp,
						true,	true,
						true,	true);
				
				// Spear
				spearHead = spearHead.copy();
				spearHead.stackSize = stackSize;
				KnappingRecipeRegistry.registerRecipe(spearHead,
						3, 3,
						matStack, xp,
						false,	true,	false,
						true,	true,	true,
						true,	true,	true);
			}
		}
		
		// Crafting dyes
		GameRegistry.addShapelessRecipe(GenesisItems.powders.getStack(EnumPowder.LIMESTONE, 4), GenesisBlocks.limestone);
		
		for (EnumPowder powder : EnumPowder.values())
		{
			if (powder.getCraftingOreDrop() != null)
			{
				GameRegistry.addShapelessRecipe(GenesisItems.powders.getStack(powder, 2), GenesisBlocks.ores.getDrop(powder.getCraftingOreDrop()));
			}
		}
		
		// Dung storage
		for (EnumDung variant : EnumDung.values())
		{
			ItemStack dungBlock = GenesisBlocks.dungs.getStack(DungBlocksAndItems.DUNG_BLOCK, variant);
			ItemStack dungItems = GenesisBlocks.dungs.getStack(DungBlocksAndItems.DUNG, variant, 9);
			// Storing
			GameRegistry.addRecipe(dungBlock,
					"DDD",
					"DDD",
					"DDD",
					'D', dungItems);
			// Breaking down
			GameRegistry.addShapelessRecipe(dungItems, dungBlock);
		}
		
		// Dung brick
		ItemStack dungBrick = GenesisItems.materials.getStack(EnumMaterial.DUNG_BRICK);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.dung_brick),
				"CCC",
				"CCC",
				"CCC",
				'C', dungBrick);
		
		// Dung recipes
		TileEntityCampfire.registerAllowedOutput(dungBrick);
		
		for (ItemDung dungItem : GenesisBlocks.dungs.getItems(DungBlocksAndItems.DUNG))
		{
			for (ItemBlockMulti<EnumTree> fenceItem : GenesisBlocks.trees.getItems(TreeBlocksAndItems.WATTLE_FENCE))
			{
				GameRegistry.addRecipe(new ItemStack(GenesisBlocks.wattle_and_daub),
						"PDP",
						"DWD",
						"PDP",
						'P', GenesisItems.materials.getStack(EnumMaterial.PROGRAMINIS),
						'D', new ItemStack(dungItem, 1, OreDictionary.WILDCARD_VALUE),
						'W', new ItemStack(fenceItem, 1, OreDictionary.WILDCARD_VALUE));
			}
			
			GameRegistry.addSmelting(dungItem, dungBrick, 0.3F);
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
		for (EnumFood food : EnumFood.values())
		{
			if (food.hasCookedVariant())
			{
				GameRegistry.addSmelting(GenesisItems.foods.getRawStack(food), GenesisItems.foods.getCookedStack(food), 0.35F);
			}
		}
		
		ItemStack ash = GenesisItems.materials.getStack(EnumMaterial.VEGETAL_ASH);
		TileEntityCampfire.registerAllowedOutput(ash);
		GameRegistry.addSmelting(GenesisBlocks.plants.getPlantStack(EnumPlant.LEPACYCLOTES), ash, 0.15F);
		GameRegistry.addSmelting(GenesisBlocks.trees.getStack(TreeBlocksAndItems.SAPLING, EnumTree.CORDAITES), ash, 0.15F);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.materials.getStack(EnumMaterial.SALT), true, ash);
		
		// Pottery
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.red_clay), "CC", "CC", 'C', GenesisItems.red_clay_ball);
		GameRegistry.addSmelting(GenesisBlocks.red_clay, new ItemStack(Blocks.stained_hardened_clay, 1, EnumDyeColor.WHITE.getMetadata()), 0.3F);
		
		GameRegistry.addRecipe(new ItemStack(GenesisItems.red_clay_bowl), "C C", " C ", 'C', GenesisItems.red_clay_ball);
		GameRegistry.addSmelting(GenesisItems.red_clay_bowl, TileEntityCampfire.registerAllowedOutput(GenesisItems.bowls.getStack(EnumCeramicBowls.BOWL)), 0.3F);
		
		GameRegistry.addRecipe(new ItemStack(GenesisItems.red_clay_bucket), "X X", "X X", " X ", 'X', GenesisItems.red_clay_ball);
		GameRegistry.addSmelting(GenesisItems.red_clay_bucket, TileEntityCampfire.registerAllowedOutput(new ItemStack(GenesisItems.ceramic_bucket)), 0.3F);
		
		// Cooking pot recipes
		ItemStack calamites = new ItemStack(GenesisBlocks.calamites);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.YELLOW), calamites, calamites);
		
		ItemStack mabelia = GenesisBlocks.plants.getPlantStack(EnumPlant.MABELIA);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.RED), mabelia, mabelia);
		
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.ORANGE), calamites, mabelia);
		
		ItemStack palaeoaster = GenesisBlocks.plants.getPlantStack(EnumPlant.PALAEOASTER);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.RED), palaeoaster, palaeoaster);
		
		ItemStack odontHead = GenesisItems.materials.getStack(EnumMaterial.ODONTOPTERIS_FIDDLEHEAD);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(GenesisDye.get(EnumDyeColor.LIME)), odontHead, odontHead);
		
		ItemStack protoFlesh = GenesisItems.seeds.getStack(EnumSeeds.PROTOTAXITES_FLESH);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(GenesisDye.get(EnumDyeColor.BROWN)), protoFlesh, protoFlesh);
		
		ItemStack lauroBerry = GenesisBlocks.trees.getStack(TreeBlocksAndItems.FRUIT, EnumTree.LAUROPHYLLUM);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(GenesisDye.get(EnumDyeColor.MAGENTA)), lauroBerry, lauroBerry);
		
		ItemStack spirifer = GenesisItems.foods.getRawStack(EnumFood.SPIRIFER);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.foods.getCookedStack(EnumFood.SPIRIFER), spirifer, spirifer);
		
		ItemStack gryphaea = GenesisItems.foods.getRawStack(EnumFood.GRYPHAEA);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.foods.getCookedStack(EnumFood.GRYPHAEA), gryphaea, gryphaea);
		
		ItemStack ceratites = GenesisItems.foods.getRawStack(EnumFood.CERATITES);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.foods.getCookedStack(EnumFood.CERATITES), ceratites, ceratites);
		
		// Cooking pot recipes with vanilla items
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.ORANGE), calamites, new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()));
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.ORANGE), mabelia, new ItemStack(Items.dye, 1, EnumDyeColor.YELLOW.getDyeDamage()));
		
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(EnumDyeColor.PINK), mabelia, new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()));
		
		// Dish cooking pot recipes
		for (MultiMetadata variant : GenesisItems.bowls.getValidVariants(ItemsCeramicBowls.DISH))
		{
			EnumDish dish = (EnumDish) variant.getOriginal();
			ArrayBuilder<ItemStack> ingredients = new ArrayBuilder<>(new ItemStack[2]);
			
			switch (dish)
			{
			case PORRIDGE:
			case PORRIDGE_ARAUCARIOXYLON:
			case PORRIDGE_ODONTOPTERIS:
				ingredients.add(GenesisItems.materials.getStack(EnumMaterial.PROGRAMINIS));
				break;
			case STEW_ARCHAEOMARASMIUS:
			case STEW_CLIMATIUS:
			case STEW_CHEIROLEPIS:
			case STEW_MEGANEURA:
			case STEW_APHTHOROBLATINNA:
			case STEW_ERYOPS:
			case STEW_CERATITES:
			case STEW_LIOPLEURODON:
			case STEW_TYRANNOSAURUS:
				ingredients.add(GenesisItems.materials.getStack(EnumMaterial.SALT));
				ingredients.add(GenesisItems.seeds.getStack(EnumSeeds.ZINGIBEROPSIS_RHIZOME));
				break;
			default:
				break;
			}
			
			switch (dish)
			{
			case PORRIDGE:
				break;
			case PORRIDGE_ARAUCARIOXYLON:
				ingredients.add(GenesisItems.seeds.getStack(EnumSeeds.ARAUCARIOXYLON_SEEDS));
				break;
			case PORRIDGE_ODONTOPTERIS:
				ingredients.add(GenesisItems.seeds.getStack(EnumSeeds.ODONTOPTERIS_SEEDS));
				break;
			case STEW_ARCHAEOMARASMIUS:
				ingredients.add(new ItemStack(GenesisBlocks.archaeomarasmius));
				break;
			case STEW_CLIMATIUS:
				ingredients.add(GenesisItems.foods.getRawStack(EnumFood.CLIMATIUS));
				break;
			case STEW_CHEIROLEPIS:
				ingredients.add(GenesisItems.foods.getRawStack(EnumFood.CHEIROLEPIS));
				break;
			case STEW_MEGANEURA:
				ingredients.add(GenesisItems.foods.getRawStack(EnumFood.MEGANEURA));
				break;
			case STEW_APHTHOROBLATINNA:
				ingredients.add(GenesisItems.foods.getRawStack(EnumFood.APHTHOROBLATINNA));
				break;
			case STEW_ERYOPS:
				ingredients.add(GenesisItems.foods.getRawStack(EnumFood.ERYOPS_LEG));
				break;
			case STEW_CERATITES:
				ingredients.add(GenesisItems.foods.getRawStack(EnumFood.CERATITES));
				break;
			case STEW_LIOPLEURODON:
				ingredients.add(GenesisItems.foods.getRawStack(EnumFood.LIOPLEURODON));
				break;
			case STEW_TYRANNOSAURUS:
				ingredients.add(GenesisItems.foods.getRawStack(EnumFood.TYRANNOSAURUS));
				break;
			case MASHED_ZINGIBEROPSIS:
				ingredients.addAll(GenesisItems.seeds.getStack(EnumSeeds.ZINGIBEROPSIS_RHIZOME, 2),
									GenesisItems.materials.getStack(EnumMaterial.SALT));
				break;
			default:
				break;
			}
			
			if (ingredients.size() > 0)
				CookingPotRecipeRegistry.registerShapeless(GenesisItems.bowls.getStack(dish), ingredients.build());
		}
		
		// Dye cooking pot recipes
		CookingPotRecipeRegistry.registerRecipe(new DyeCookingRecipe());
		
		// Armor recipes
		for (EnumClothing clothing : EnumClothing.values())
		{
			ItemStack ingredient = null;
			
			switch (clothing)
			{
			case CHITIN:
				ingredient = GenesisItems.materials.getStack(EnumMaterial.ARTHROPLEURA_CHITIN);
				break;
			case CAMOUFLAGE:
				ingredient = GenesisItems.materials.getStack(EnumMaterial.CLADOPHLEBIS_FROND);
				break;
			}
			
			if (ingredient != null)
			{
				GameRegistry.addRecipe(GenesisItems.clothing.getHelmet(clothing),
						"XXX",
						"X X",
						'X', ingredient);
				GameRegistry.addRecipe(GenesisItems.clothing.getChestplate(clothing),
						"X X",
						"XXX",
						"XXX",
						'X', ingredient);
				GameRegistry.addRecipe(GenesisItems.clothing.getLeggings(clothing),
						"XXX",
						"X X",
						"X X",
						'X', ingredient);
				GameRegistry.addRecipe(GenesisItems.clothing.getBoots(clothing),
						"X X",
						"X X",
						'X', ingredient);
			}
		}
	}
	
	/**
	 * Called as late as possible so as to have all other recipes registered already.
	 */
	public static void doSubstitutes()
	{
		// Fiber for string
		makeSubstituteCraftingItem(new ItemStack(Items.string), GenesisItems.materials.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER), Blocks.wool);
		
		// Ceramic buckets for vanilla buckets
		makeSubstituteCraftingItem(new ItemStack(Items.bucket), new ItemStack(GenesisItems.ceramic_bucket));
		makeSubstituteCraftingItem(new ItemStack(Items.water_bucket), new ItemStack(GenesisItems.ceramic_bucket_water));
		makeSubstituteCraftingItem(new ItemStack(Items.milk_bucket), new ItemStack(GenesisItems.ceramic_bucket_milk));
		
		// Dyes
		GameRegistry.addRecipe(new SubstituteRecipe(RecipesArmorDyes.class,
				(s) -> {
					IMetadata<?> variant = GenesisItems.bowls.getVariant(s).getOriginal();
					return variant instanceof GenesisDye ? new ItemStack(Items.dye, s.stackSize, ((GenesisDye) variant).getColor().getDyeDamage()) : null;
				}));
		
		for (ItemDyeBowl item : GenesisItems.bowls.getItems(ItemsCeramicBowls.DYE))
		{
			OreDictionary.registerOre("dye", new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
		}
		
		for (MultiMetadata variant : GenesisItems.bowls.getValidVariants(ItemsCeramicBowls.DYE))
		{
			GenesisDye dye = (GenesisDye) variant.getOriginal();
			String name = dye.getOreDictName();
			
			if (OreDictionary.getOres(name).isEmpty())
			{
				Genesis.logger.warn("Dye " + dye + " has not been previously added to the OreDictionary under the name " + name + ".");
			}
			
			OreDictionary.registerOre(name, GenesisItems.bowls.getStack(dye));
		}
	}
}
