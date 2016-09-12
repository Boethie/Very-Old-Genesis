package genesis.common;

import java.util.*;
import java.util.stream.Collectors;

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
						
						ShapedOreRecipe replacedRecipe = new ShapedOreRecipe(output, mirrored, "z", 'z', Items.APPLE);
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
			return new ItemStack(GenesisBlocks.OCTAEDRITE);
		case DOLERITE:
			return new ItemStack(GenesisBlocks.DOLERITE);
		case RHYOLITE:
			return new ItemStack(GenesisBlocks.RHYOLITE);
		case GRANITE:
			return new ItemStack(GenesisBlocks.GRANITE);
		case QUARTZ:
			return GenesisBlocks.ORES.getDrop(EnumOre.QUARTZ);
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
			return new BlockSpriteUVs(GenesisBlocks.OCTAEDRITE);
		case DOLERITE:
			return new BlockSpriteUVs(GenesisBlocks.DOLERITE);
		case RHYOLITE:
			return new BlockSpriteUVs(GenesisBlocks.RHYOLITE);
		case GRANITE:
			return new BlockSpriteUVs(GenesisBlocks.GRANITE);
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
		ItemStack toolChipped = GenesisItems.TOOLS.getBadStack(type, material);
		//ItemStack toolGood = GenesisItems.tools.getGoodStack(type, material);
		
		for (ItemStack billet : GenesisBlocks.TREES.getSubItems(TreeBlocksAndItems.BILLET))
		{
			Object[] paramsBase = ArrayUtils.addAll(shape,
					'B', billet,
					'T', toolHead);
			Object[] paramsFiber = ArrayUtils.addAll(paramsBase,
					'S', GenesisItems.MATERIALS.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER));
			Object[] paramsString = ArrayUtils.addAll(paramsBase,
					'S', Items.STRING);
			
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
		
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.SMOOTH_LIMESTONE, 4),
				"LL",
				"LL",
				'L', GenesisBlocks.LIMESTONE);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.TRAP_FLOOR),
				"xxx",
				"///",
				'x', GenesisItems.MATERIALS.getStack(EnumMaterial.CLADOPHLEBIS_FROND),
				'/', GenesisBlocks.CALAMITES);
		
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.BENCH_SEAT),
				"FFF",
				"GGG",
				'F', GenesisItems.MATERIALS.getStack(EnumMaterial.PROGRAMINIS),
				'G', GenesisBlocks.GRANITE);
		
		//Food
		GameRegistry.addShapelessRecipe(GenesisItems.SEEDS.getStack(EnumSeeds.ARAUCARIOXYLON_SEEDS), GenesisBlocks.TREES.getStack(TreeBlocksAndItems.FRUIT, EnumTree.ARAUCARIOXYLON));
		
		//Torches
		ItemStack resin = GenesisItems.MATERIALS.getStack(EnumMaterial.RESIN);
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.TORCH, 4),
				"o",
				"|",
				'o', resin, '|', Items.STICK);
		
		// Calamites TORCH
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.CALAMITES_TORCH, 4),
				"o",
				"|",
				'o', Items.COAL, '|', GenesisBlocks.CALAMITES);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.CALAMITES_TORCH, 4),
				"o",
				"|",
				'o', new ItemStack(Items.COAL, 1, 1), '|', GenesisBlocks.CALAMITES);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.CALAMITES_TORCH, 4),
				"o",
				"|",
				'o', resin, '|', GenesisBlocks.CALAMITES);
		
		// Tall calamites TORCH
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.CALAMITES_TORCH_TALL, 2),
				"o",
				"|",
				"|",
				'o', Items.COAL, '|', GenesisBlocks.CALAMITES);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.CALAMITES_TORCH_TALL, 2),
				"o",
				"|",
				"|",
				'o', new ItemStack(Items.COAL, 1, 1), '|', GenesisBlocks.CALAMITES);
		GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.CALAMITES_TORCH_TALL, 2),
				"o",
				"|",
				"|",
				'o', resin, '|', GenesisBlocks.CALAMITES);
		
		// Bundles
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.CALAMITES_BUNDLE),
				"CCC",
				"CCC",
				"CCC",
				'C', GenesisBlocks.CALAMITES);
		GameRegistry.addShapelessRecipe(new ItemStack(GenesisBlocks.CALAMITES, 9), GenesisBlocks.CALAMITES_BUNDLE);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.PROGRAMINIS_BUNDLE),
				"CCC",
				"CCC",
				"CCC",
				'C', GenesisItems.MATERIALS.getStack(EnumMaterial.PROGRAMINIS));
		GameRegistry.addShapelessRecipe(GenesisItems.MATERIALS.getStack(EnumMaterial.PROGRAMINIS, 9), GenesisBlocks.PROGRAMINIS_BUNDLE);
		
		// Roofs
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(GenesisBlocks.CALAMITES_ROOF, 4),
				"#  ",
				"## ",
				"###",
				'#', GenesisBlocks.CALAMITES));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(GenesisBlocks.PROGRAMINIS_ROOF, 4),
				"#  ",
				"## ",
				"###",
				'#', GenesisItems.MATERIALS.getStack(EnumMaterial.PROGRAMINIS)));
		
		// Silt to siltstone recipes
		for (EnumSilt mat: EnumSilt.values())
		{
			GameRegistry.addShapedRecipe(GenesisBlocks.SILT.getStack(SiltBlocks.SILTSTONE, mat),
					"ss",
					"ss",
					's', GenesisBlocks.SILT.getStack(SiltBlocks.SILT, mat));
		}
		
		// Flint and marcasite recipe
		EnumToolMaterial[] flintMaterials = {EnumToolMaterial.BLACK_FLINT, EnumToolMaterial.BROWN_FLINT};
		
		for (EnumToolMaterial mat : flintMaterials)
		{
			GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.FLINT_AND_MARCASITE),
					GenesisItems.NODULES.getStack(EnumNodule.MARCASITE),
					GenesisItems.TOOLS.getStack(ToolItems.PEBBLE, mat));	// Pebble
			GameRegistry.addShapelessRecipe(new ItemStack(GenesisItems.FLINT_AND_MARCASITE),
					GenesisItems.NODULES.getStack(EnumNodule.MARCASITE),
					GenesisItems.NODULES.getStack(EnumNodule.fromToolMaterial(mat)));	// Nodule
		}
		
		for (EnumTree tree : EnumTree.values())
		{
			// All recipes involving debris
			if (tree.hasDebris())
			{
				// Leaves -> Debris
				GameRegistry.addShapelessRecipe(GenesisBlocks.DEBRIS.getStack(tree, 4),
						GenesisBlocks.TREES.getStack(TreeBlocksAndItems.LEAVES, tree));
				
				// Humus
				GameRegistry.addShapelessRecipe(new ItemStack(GenesisBlocks.HUMUS),
						new ItemStack(Blocks.DIRT),
						GenesisBlocks.DEBRIS.getStack(tree));
			}
			
			// Porridge
			ItemStack porridge = null;
			
			switch (tree)
			{
			case GINKGO:
				porridge = GenesisItems.BOWLS.getStack(EnumDish.PORRIDGE_GINKGO);
				break;
			case FICUS:
				porridge = GenesisItems.BOWLS.getStack(EnumDish.PORRIDGE_FIG);
				break;
			case LAUROPHYLLUM:
				porridge = GenesisItems.BOWLS.getStack(EnumDish.PORRIDGE_LAUROPHYLLUM);
				break;
			default:
				break;
			}
			
			if (porridge != null)
			{
				CookingPotRecipeRegistry.registerShapeless(porridge,
						GenesisItems.MATERIALS.getStack(EnumMaterial.PROGRAMINIS),
						GenesisBlocks.TREES.getStack(TreeBlocksAndItems.FRUIT, tree));
			}
		}
		
		GameRegistry.addShapelessRecipe(GenesisBlocks.DEBRIS.getStack(EnumDebrisOther.CALAMITES), GenesisBlocks.CALAMITES);
		
		// All recipes with only logs, and one constant output.
		for (Item log : GenesisBlocks.TREES.getItems(TreeBlocksAndItems.LOG))
		{
			GameRegistry.addSmelting(new ItemStack(log, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.COAL, 1, 1), 0.15F);
		}
		
		// Logs -> billets
		for (EnumTree variant : GenesisBlocks.TREES.getSharedValidVariants(TreeBlocksAndItems.LOG, TreeBlocksAndItems.BILLET))
		{
			ItemStack logStack = GenesisBlocks.TREES.getStack(TreeBlocksAndItems.LOG, variant, 1);
			ItemStack billetStack = GenesisBlocks.TREES.getStack(TreeBlocksAndItems.BILLET, variant, 4);
			
			if (variant == EnumTree.SIGILLARIA || variant == EnumTree.LEPIDODENDRON)
			{
				billetStack.stackSize = 1;
			}
			
			GameRegistry.addShapelessRecipe(billetStack, logStack);
		}
		
		// Branch -> billet
		for (EnumTree variant : GenesisBlocks.TREES.getSharedValidVariants(TreeBlocksAndItems.BRANCH, TreeBlocksAndItems.BILLET))
		{
			GameRegistry.addShapelessRecipe(
					GenesisBlocks.TREES.getStack(TreeBlocksAndItems.BRANCH, variant, 1),
					GenesisBlocks.TREES.getStack(TreeBlocksAndItems.BILLET, variant, 1));
		}
		
		// Recipes involving billets.
		for (EnumTree variant : GenesisBlocks.TREES.getValidVariants(TreeBlocksAndItems.BILLET))
		{
			ItemStack billetStack = GenesisBlocks.TREES.getStack(TreeBlocksAndItems.BILLET, variant, 1);
			
			// Storage box
			GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.STORAGE_BOX),
					"BBB",
					"B B",
					"BBB",
					'B', billetStack);
			
			// Wattle fence
			GameRegistry.addShapedRecipe(GenesisBlocks.TREES.getStack(TreeBlocksAndItems.WATTLE_FENCE, variant, 3),
					"BBB",
					"BBB",
					'B', billetStack);
			
			// Vanilla TORCH
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.TORCH, 4),
					"c",
					"B",
					'c', new ItemStack(Items.COAL, 1, OreDictionary.WILDCARD_VALUE),
					'B', billetStack);
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.TORCH, 4),
					"r",
					"B",
					'r', GenesisItems.MATERIALS.getStack(EnumMaterial.RESIN),
					'B', billetStack);
			
			// Campfire
			for (ItemStack pebble : GenesisItems.TOOLS.getSubItems(ToolItems.PEBBLE))
			{	// TODO: Add variants for pebbles and billets.
				GameRegistry.addShapedRecipe(new ItemStack(GenesisBlocks.CAMPFIRE),
						"III",
						"I I",
						"ooo",
						'I', billetStack,
						'o', pebble);
			}
			
			//Rack
			GameRegistry.addShapedRecipe(GenesisBlocks.TREES.getStack(TreeBlocksAndItems.RACK, variant),
					"XX",
					'X', billetStack);

			// Rope ladder
			GameRegistry.addRecipe(new ItemStack(GenesisBlocks.ROPE_LADDER, 3),
					"S S",
					"BBB",
					"S S",
					'S', GenesisItems.MATERIALS.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER),
					'B', billetStack);
			
			// Bow
			if (variant.hasBow())
			{
				GameRegistry.addRecipe(GenesisItems.BOWS.getStack(EnumBowType.SELF, variant),
						" S",
						"BS",
						" S",
						'S', GenesisItems.MATERIALS.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER),
						'B', billetStack);
				GameRegistry.addRecipe(GenesisItems.BOWS.getStack(EnumBowType.SELF, variant),
						"S ",
						"SB",
						"S ",
						'S', GenesisItems.MATERIALS.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER),
						'B', billetStack);
			}
		}
		
		// All rubble recipes
		for (EnumRubble variant : EnumRubble.values())
		{
			ItemStack rubbleStack = GenesisBlocks.RUBBLE.getStack(variant);
			
			// Stone -> Rubble
			ItemStack modelStack = variant.getModelStack();
			GameRegistry.addShapelessRecipe(rubbleStack, modelStack);
			
			// Wall
			Block wallBlock = null;
			
			switch (variant)
			{
			case GRANITE:
				wallBlock = GenesisBlocks.GRANITE_RUBBLE_WALL;
				break;
			case MOSSY_GRANITE:
				wallBlock = GenesisBlocks.MOSSY_GRANITE_RUBBLE_WALL;
				break;
			case RHYOLITE:
				wallBlock = GenesisBlocks.RHYOLITE_RUBBLE_WALL;
				break;
			case DOLERITE:
				wallBlock = GenesisBlocks.DOLERITE_RUBBLE_WALL;
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
		for (ObjectType<EnumSlab, ?, ?> type : GenesisBlocks.SLABS.getTypes())
		{
			for (EnumSlab variant : GenesisBlocks.SLABS.getValidVariants(type))
			{
				ItemStack slabStack = GenesisBlocks.SLABS.getStack(type, variant, 6);
				ItemStack modelStack = variant.getModelStack();
				GameRegistry.addRecipe(slabStack, "SSS", 'S', modelStack);
			}
		}
		
		// Register knapping tools and
		// Register workbench recipes.
		for (ItemStack pebble : GenesisItems.TOOLS.getSubItems(ToolItems.PEBBLE))
		{
			pebble.setItemDamage(OreDictionary.WILDCARD_VALUE);
			KnappingRecipeRegistry.registerKnappingTool(pebble);
			
			for (ItemStack log : GenesisBlocks.TREES.getSubItems(TreeBlocksAndItems.LOG))
			{
				GameRegistry.addRecipe(new ItemStack(GenesisBlocks.WORKBENCH),
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
			ItemStack flake = GenesisItems.TOOLS.getStack(ToolItems.FLAKE, material);
			Collection<ItemStack> billets = GenesisBlocks.TREES.getItems(TreeBlocksAndItems.BILLET)
					.stream().map((i) -> new ItemStack(i, 1, OreDictionary.WILDCARD_VALUE)).collect(Collectors.toList());
			ItemStack[] feathers = {
				new ItemStack(Items.FEATHER),
				GenesisItems.MATERIALS.getStack(EnumMaterial.COELOPHYSIS_FEATHER),
				GenesisItems.MATERIALS.getStack(EnumMaterial.EPIDEXIPTERYX_FEATHER),
				GenesisItems.MATERIALS.getStack(EnumMaterial.TYRANNOSAURUS_FEATHER),
			};
			
			for (EnumArrowShaft shaft : EnumArrowShaft.values())
			{
				ItemStack arrow = GenesisItems.ARROWS.getStack(shaft, material, 4);
				
				for (ItemStack billet : billets)
				{
					for (ItemStack feather : feathers)
					{
						GameRegistry.addRecipe(arrow,
								"^",
								"|",
								"/",
								'^', flake,
								'|', billet,
								'/', feather);
					}
				}
			}
			
			// Pebble
			if (GenesisItems.TOOLS.containsVariant(ToolItems.PEBBLE, material))
			{
				stack = GenesisItems.TOOLS.getStack(ToolItems.PEBBLE, material);
				
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
				stack = GenesisItems.NODULES.getStack(nodule);
				
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
			ItemStack axeHead = GenesisItems.TOOLS.getBadStack(ToolItems.AXE_HEAD, material, 1);
			addToolRecipe(axeHead, ToolItems.AXE, material);
			
			ItemStack shovelHead = GenesisItems.TOOLS.getBadStack(ToolItems.SHOVEL_HEAD, material, 1);
			addToolRecipe(shovelHead, ToolItems.SHOVEL, material);
			
			ItemStack pickHead = GenesisItems.TOOLS.getBadStack(ToolItems.PICKAXE_HEAD, material, 1);
			addToolRecipe(pickHead, ToolItems.PICKAXE, material);
			
			ItemStack hoeHead = GenesisItems.TOOLS.getBadStack(ToolItems.HOE_HEAD, material, 1);
			addToolRecipe(hoeHead, ToolItems.HOE, material);
			
			ItemStack knifeHead = GenesisItems.TOOLS.getBadStack(ToolItems.KNIFE_HEAD, material, 1);
			addToolRecipe(knifeHead, ToolItems.KNIFE, material,
					" T",
					"SB");
			
			ItemStack clubHead = GenesisItems.TOOLS.getBadStack(ToolItems.CLUB_HEAD, material, 1);
			addToolRecipe(clubHead, ToolItems.CLUB, material);
			
			ItemStack spearHead = GenesisItems.TOOLS.getBadStack(ToolItems.SPEAR_HEAD, material, 1);
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
		GameRegistry.addShapelessRecipe(GenesisItems.POWDERS.getStack(EnumPowder.LIMESTONE, 4), GenesisBlocks.LIMESTONE);
		
		for (EnumPowder powder : EnumPowder.values())
		{
			if (powder.getCraftingOreDrop() != null)
			{
				GameRegistry.addShapelessRecipe(GenesisItems.POWDERS.getStack(powder, 2), GenesisBlocks.ORES.getDrop(powder.getCraftingOreDrop()));
			}
		}
		
		// Dung storage
		for (EnumDung variant : EnumDung.values())
		{
			ItemStack dungBlock = GenesisBlocks.DUNGS.getStack(DungBlocksAndItems.DUNG_BLOCK, variant);
			ItemStack dungItems = GenesisBlocks.DUNGS.getStack(DungBlocksAndItems.DUNG, variant, 9);
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
		ItemStack dungBrick = GenesisItems.MATERIALS.getStack(EnumMaterial.DUNG_BRICK);
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.DUNG_BRICK),
				"CCC",
				"CCC",
				"CCC",
				'C', dungBrick);
		
		// Dung recipes
		TileEntityCampfire.registerAllowedOutput(dungBrick);
		
		for (ItemDung dungItem : GenesisBlocks.DUNGS.getItems(DungBlocksAndItems.DUNG))
		{
			for (ItemBlockMulti<EnumTree> fenceItem : GenesisBlocks.TREES.getItems(TreeBlocksAndItems.WATTLE_FENCE))
			{
				GameRegistry.addRecipe(new ItemStack(GenesisBlocks.WATTLE_AND_DAUB),
						"PDP",
						"DWD",
						"PDP",
						'P', GenesisItems.MATERIALS.getStack(EnumMaterial.PROGRAMINIS),
						'D', new ItemStack(dungItem, 1, OreDictionary.WILDCARD_VALUE),
						'W', new ItemStack(fenceItem, 1, OreDictionary.WILDCARD_VALUE));
			}
			
			GameRegistry.addSmelting(dungItem, dungBrick, 0.3F);
		}
		
		// Smelting
		for (EnumOre ore : GenesisBlocks.ORES.getSharedValidVariants(OreBlocks.ORE, OreBlocks.DROP))
		{
			ItemStack oreStack = GenesisBlocks.ORES.getOreStack(ore);
			ItemStack dropStack = GenesisBlocks.ORES.getDrop(ore);
			GameRegistry.addSmelting(oreStack, dropStack, ore.getSmeltingExperience());
		}
		
		GameRegistry.addSmelting(GenesisBlocks.ORES.getOreStack(EnumOre.MARCASITE), GenesisItems.NODULES.getStack(EnumNodule.MARCASITE), EnumOre.MARCASITE.getSmeltingExperience());
		
		// Food
		for (EnumFood food : EnumFood.values())
		{
			if (food.hasCookedVariant())
			{
				GameRegistry.addSmelting(GenesisItems.FOODS.getRawStack(food), GenesisItems.FOODS.getCookedStack(food), 0.35F);
			}
		}
		
		ItemStack ash = GenesisItems.MATERIALS.getStack(EnumMaterial.VEGETAL_ASH);
		TileEntityCampfire.registerAllowedOutput(ash);
		GameRegistry.addSmelting(GenesisBlocks.PLANTS.getPlantStack(EnumPlant.LEPACYCLOTES), ash, 0.15F);
		GameRegistry.addSmelting(GenesisBlocks.TREES.getStack(TreeBlocksAndItems.SAPLING, EnumTree.CORDAITES), ash, 0.15F);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.MATERIALS.getStack(EnumMaterial.SALT), true, ash);
		
		// Pottery
		GameRegistry.addRecipe(new ItemStack(GenesisBlocks.RED_CLAY), "CC", "CC", 'C', GenesisItems.RED_CLAY_BALL);
		GameRegistry.addSmelting(GenesisBlocks.RED_CLAY, new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.WHITE.getMetadata()), 0.3F);
		
		GameRegistry.addRecipe(new ItemStack(GenesisItems.RED_CLAY_BOWL), "C C", " C ", 'C', GenesisItems.RED_CLAY_BALL);
		GameRegistry.addSmelting(GenesisItems.RED_CLAY_BOWL, TileEntityCampfire.registerAllowedOutput(GenesisItems.BOWLS.getStack(EnumCeramicBowls.BOWL)), 0.3F);
		
		GameRegistry.addRecipe(new ItemStack(GenesisItems.RED_CLAY_BUCKET), "X X", "X X", " X ", 'X', GenesisItems.RED_CLAY_BALL);
		GameRegistry.addSmelting(GenesisItems.RED_CLAY_BUCKET, TileEntityCampfire.registerAllowedOutput(new ItemStack(GenesisItems.CERAMIC_BUCKET)), 0.3F);
		
		// Cooking pot recipes
		ItemStack calamites = new ItemStack(GenesisBlocks.CALAMITES);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(EnumDyeColor.YELLOW), calamites, calamites);
		
		ItemStack mabelia = GenesisBlocks.PLANTS.getPlantStack(EnumPlant.MABELIA);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(EnumDyeColor.RED), mabelia, mabelia);
		
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(EnumDyeColor.ORANGE), calamites, mabelia);
		
		ItemStack palaeoaster = GenesisBlocks.PLANTS.getPlantStack(EnumPlant.PALAEOASTER);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(EnumDyeColor.RED), palaeoaster, palaeoaster);
		
		ItemStack odontHead = GenesisItems.MATERIALS.getStack(EnumMaterial.ODONTOPTERIS_FIDDLEHEAD);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(GenesisDye.get(EnumDyeColor.LIME)), odontHead, odontHead);
		
		ItemStack protoFlesh = GenesisItems.SEEDS.getStack(EnumSeeds.PROTOTAXITES_FLESH);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(GenesisDye.get(EnumDyeColor.BROWN)), protoFlesh, protoFlesh);
		
		ItemStack lauroBerry = GenesisBlocks.TREES.getStack(TreeBlocksAndItems.FRUIT, EnumTree.LAUROPHYLLUM);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(GenesisDye.get(EnumDyeColor.MAGENTA)), lauroBerry, lauroBerry);
		
		ItemStack spirifer = GenesisItems.FOODS.getRawStack(EnumFood.SPIRIFER);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.FOODS.getCookedStack(EnumFood.SPIRIFER), spirifer, spirifer);
		
		ItemStack gryphaea = GenesisItems.FOODS.getRawStack(EnumFood.GRYPHAEA);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.FOODS.getCookedStack(EnumFood.GRYPHAEA), gryphaea, gryphaea);
		
		ItemStack ceratites = GenesisItems.FOODS.getRawStack(EnumFood.CERATITES);
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.FOODS.getCookedStack(EnumFood.CERATITES), ceratites, ceratites);
		
		// Cooking pot recipes with vanilla items
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(EnumDyeColor.ORANGE), calamites, new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()));
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(EnumDyeColor.ORANGE), mabelia, new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()));
		
		CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(EnumDyeColor.PINK), mabelia, new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
		
		// Dish cooking pot recipes
		for (MultiMetadata variant : GenesisItems.BOWLS.getValidVariants(ItemsCeramicBowls.DISH))
		{
			EnumDish dish = (EnumDish) variant.getOriginal();
			ArrayBuilder<ItemStack> ingredients = new ArrayBuilder<>(new ItemStack[2]);
			
			switch (dish)
			{
			case PORRIDGE:
			case PORRIDGE_ODONTOPTERIS:
			case PORRIDGE_GINKGO:
			case PORRIDGE_ARAUCARIOXYLON:
			case PORRIDGE_FIG:
			case PORRIDGE_ARTOCARPUS:
			case PORRIDGE_LAUROPHYLLUM:
				ingredients.add(GenesisItems.MATERIALS.getStack(EnumMaterial.PROGRAMINIS));
				break;
			case STEW_ARCHAEOMARASMIUS:
			case STEW_CLIMATIUS:
			case STEW_CHEIROLEPIS:
			case STEW_MEGANEURA:
			case STEW_APHTHOROBLATINNA:
			case STEW_ERYOPS:
			case STEW_CERATITES:
			case STEW_COELOPHYSIS:
			case STEW_LIOPLEURODON:
			case STEW_TYRANNOSAURUS:
				ingredients.add(GenesisItems.MATERIALS.getStack(EnumMaterial.SALT));
				ingredients.add(GenesisItems.SEEDS.getStack(EnumSeeds.ZINGIBEROPSIS_RHIZOME));
				break;
			default:
				break;
			}
			
			switch (dish)
			{
			case PORRIDGE:
				break;
			case PORRIDGE_ODONTOPTERIS:
				ingredients.add(GenesisItems.SEEDS.getStack(EnumSeeds.ODONTOPTERIS_SEEDS));
				break;
			case PORRIDGE_GINKGO:
				ingredients.add(GenesisBlocks.TREES.getStack(TreeBlocksAndItems.FRUIT, EnumTree.GINKGO));
				break;
			case PORRIDGE_ARAUCARIOXYLON:
				ingredients.add(GenesisItems.SEEDS.getStack(EnumSeeds.ARAUCARIOXYLON_SEEDS));
				break;
			case PORRIDGE_FIG:
				ingredients.add(GenesisBlocks.TREES.getStack(TreeBlocksAndItems.FRUIT, EnumTree.FICUS));
				break;
			case PORRIDGE_ARTOCARPUS:
				ingredients.add(GenesisBlocks.TREES.getStack(TreeBlocksAndItems.FRUIT, EnumTree.ARTOCARPUS));
				break;
			case PORRIDGE_LAUROPHYLLUM:
				ingredients.add(GenesisBlocks.TREES.getStack(TreeBlocksAndItems.FRUIT, EnumTree.LAUROPHYLLUM));
				break;
			case STEW_ARCHAEOMARASMIUS:
				ingredients.add(new ItemStack(GenesisBlocks.ARCHAEOMARASMIUS));
				break;
			case STEW_CLIMATIUS:
				ingredients.add(GenesisItems.FOODS.getRawStack(EnumFood.CLIMATIUS));
				break;
			case STEW_CHEIROLEPIS:
				ingredients.add(GenesisItems.FOODS.getRawStack(EnumFood.CHEIROLEPIS));
				break;
			case STEW_MEGANEURA:
				ingredients.add(GenesisItems.FOODS.getRawStack(EnumFood.MEGANEURA));
				break;
			case STEW_APHTHOROBLATINNA:
				ingredients.add(GenesisItems.FOODS.getRawStack(EnumFood.APHTHOROBLATINNA));
				break;
			case STEW_ERYOPS:
				ingredients.add(GenesisItems.FOODS.getRawStack(EnumFood.ERYOPS_LEG));
				break;
			case STEW_CERATITES:
				ingredients.add(GenesisItems.FOODS.getRawStack(EnumFood.CERATITES));
				break;
			case STEW_COELOPHYSIS:
				ingredients.add(GenesisItems.FOODS.getRawStack(EnumFood.COELOPHYSIS));
				break;
			case STEW_LIOPLEURODON:
				ingredients.add(GenesisItems.FOODS.getRawStack(EnumFood.LIOPLEURODON));
				break;
			case STEW_TYRANNOSAURUS:
				ingredients.add(GenesisItems.FOODS.getRawStack(EnumFood.TYRANNOSAURUS));
				break;
			case MASHED_ZINGIBEROPSIS:
				ingredients.addAll(GenesisItems.SEEDS.getStack(EnumSeeds.ZINGIBEROPSIS_RHIZOME, 2),
									GenesisItems.MATERIALS.getStack(EnumMaterial.SALT));
				break;
			default:
				break;
			}
			
			if (ingredients.size() > 0)
				CookingPotRecipeRegistry.registerShapeless(GenesisItems.BOWLS.getStack(dish), ingredients.build());
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
				ingredient = GenesisItems.MATERIALS.getStack(EnumMaterial.ARTHROPLEURA_CHITIN);
				break;
			case CAMOUFLAGE:
				ingredient = GenesisItems.MATERIALS.getStack(EnumMaterial.CLADOPHLEBIS_FROND);
				break;
			}
			
			if (ingredient != null)
			{
				GameRegistry.addRecipe(GenesisItems.CLOTHING.getHelmet(clothing),
						"XXX",
						"X X",
						'X', ingredient);
				GameRegistry.addRecipe(GenesisItems.CLOTHING.getChestplate(clothing),
						"X X",
						"XXX",
						"XXX",
						'X', ingredient);
				GameRegistry.addRecipe(GenesisItems.CLOTHING.getLeggings(clothing),
						"XXX",
						"X X",
						"X X",
						'X', ingredient);
				GameRegistry.addRecipe(GenesisItems.CLOTHING.getBoots(clothing),
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
		makeSubstituteCraftingItem(new ItemStack(Items.STRING), GenesisItems.MATERIALS.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER), Blocks.WOOL);
		
		// Ceramic buckets for vanilla buckets
		makeSubstituteCraftingItem(new ItemStack(Items.BUCKET), new ItemStack(GenesisItems.CERAMIC_BUCKET));
		makeSubstituteCraftingItem(new ItemStack(Items.WATER_BUCKET), new ItemStack(GenesisItems.CERAMIC_BUCKET_WATER));
		makeSubstituteCraftingItem(new ItemStack(Items.MILK_BUCKET), new ItemStack(GenesisItems.CERAMIC_BUCKET_MILK));
		
		// Dyes
		GameRegistry.addRecipe(new SubstituteRecipe(RecipesArmorDyes.class,
				(s) -> {
					IMetadata<?> variant = GenesisItems.BOWLS.getVariant(s).getOriginal();
					return variant instanceof GenesisDye ? new ItemStack(Items.DYE, s.stackSize, ((GenesisDye) variant).getColor().getDyeDamage()) : null;
				}));
		
		for (ItemDyeBowl item : GenesisItems.BOWLS.getItems(ItemsCeramicBowls.DYE))
		{
			OreDictionary.registerOre("dye", new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
		}
		
		for (MultiMetadata variant : GenesisItems.BOWLS.getValidVariants(ItemsCeramicBowls.DYE))
		{
			GenesisDye dye = (GenesisDye) variant.getOriginal();
			String name = dye.getOreDictName();
			
			if (OreDictionary.getOres(name).isEmpty())
			{
				Genesis.logger.warn("Dye " + dye + " has not been previously added to the OreDictionary under the name " + name + ".");
			}
			
			OreDictionary.registerOre(name, GenesisItems.BOWLS.getStack(dye));
		}
	}
}
