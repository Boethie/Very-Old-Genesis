package genesis.common;

import genesis.block.*;
import genesis.block.tileentity.*;
import genesis.block.tileentity.BlockGenesisFlowerPot.IFlowerPotPlant;
import genesis.block.tileentity.portal.*;
import genesis.block.tileentity.portal.render.TileEntityGenesisPortalRenderer;
import genesis.block.tileentity.render.TileEntityCampfireRenderer;
import genesis.block.tileentity.render.TileEntityRackRenderer;
import genesis.block.tileentity.render.TileEntityStorageBoxRenderer;
import genesis.client.Colorizers;
import genesis.client.model.MetadataModelDefinition;
import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.TypeNamePosition;
import genesis.combo.variant.*;
import genesis.common.sounds.GenesisSoundEvents;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.item.*;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import genesis.util.FlexibleStateMap;
import genesis.util.ReflectionUtils;
import genesis.util.random.drops.blocks.BlockDrops;
import genesis.util.random.drops.blocks.BlockRandomDrop;
import genesis.util.random.drops.blocks.BlockStackDrop;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class GenesisBlocks
{
	/* Portal */
	public static final VariantsCombo<EnumMenhirPart, BlockMenhir, ItemMenhir> MENHIRS =
			new VariantsCombo<>(
					"menhirs",
					new ObjectType<>(EnumMenhirPart.class, "menhir", "menhir", BlockMenhir.class, ItemMenhir.class)
							.setUseSeparateVariantJsons(false).setShouldRegisterVariantModels(false),
					EnumMenhirPart.class, EnumMenhirPart.values())
			.setNames(Constants.MOD_ID, Unlocalized.PREFIX);
	public static final BlockGenesisPortal PORTAL = (BlockGenesisPortal) new BlockGenesisPortal().setUnlocalizedName(Unlocalized.MISC + "portal");

	/* Moss */
	public static final BlockMoss MOSS = (BlockMoss) new BlockMoss().setUnlocalizedName(Unlocalized.PREFIX + "moss");

	/* Humus */
	public static final Block HUMUS = new BlockHumus().setUnlocalizedName(Unlocalized.PREFIX + "humus");
	public static final Block HUMUS_PATH = new BlockGenesisPath(HUMUS.getDefaultState()).setUnlocalizedName(Unlocalized.PREFIX + "humusPath");

	/* Rocks */
	public static final Block GRANITE = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "granite");
	public static final Block MOSSY_GRANITE = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "mossyGranite");
	public static final Block RADIOACTIVE_TRACES = new BlockRadioactiveTraces().setUnlocalizedName(Unlocalized.ROCK + "radioactiveTraces");
	public static final Block RHYOLITE = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "rhyolite");
	public static final Block DOLERITE = new BlockGenesisRock(1.2F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "dolerite");
	public static final VariantsCombo<EnumRubble, BlockRubble, ItemBlockMulti<EnumRubble>> RUBBLE =
			new VariantsCombo<>(
					"rubble",
					new ObjectType<EnumRubble, BlockRubble, ItemBlockMulti<EnumRubble>>(EnumRubble.class, "rubble", Unlocalized.Section.ROCK + "rubble", BlockRubble.class, null)
							.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.POSTFIX),
					EnumRubble.class, EnumRubble.values())
			.setNames(Constants.MOD_ID, Unlocalized.PREFIX);
	public static final Block KOMATIITE = new BlockGenesisRock(1.95F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "komatiite");
	public static final Block ANORTHOSITE = new BlockGenesisRock(1.2F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "anorthosite");
	public static final Block TRONDHJEMITE = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "trondhjemite");
	public static final Block FAUX_AMPHIBOLITE = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "fauxAmphibolite");
	public static final Block GNEISS = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "gneiss");
	public static final Block LIMESTONE = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName(Unlocalized.ROCK + "limestone");
	public static final Block SMOOTH_LIMESTONE = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName(Unlocalized.ROCK + "smoothLimestone");
	public static final Block OCTAEDRITE = new BlockGenesisRock(1.0F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "octaedrite");

	public static final Block ANCIENT_SMOKER = new BlockGenesisRock(0.65F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "ancientSmoker");
	
	/* Slabs */
	public static final SlabBlocks SLABS = new SlabBlocks();

	/* Rubble Walls */
	public static final Block GRANITE_RUBBLE_WALL = new BlockRubbleWall(EnumRubble.GRANITE).setUnlocalizedName(Unlocalized.WALL + "rubble.granite");
	public static final Block MOSSY_GRANITE_RUBBLE_WALL = new BlockRubbleWall(EnumRubble.MOSSY_GRANITE).setUnlocalizedName(Unlocalized.WALL + "rubble.mossyGranite");
	public static final Block RHYOLITE_RUBBLE_WALL = new BlockRubbleWall(EnumRubble.RHYOLITE).setUnlocalizedName(Unlocalized.WALL + "rubble.rhyolite");
	public static final Block DOLERITE_RUBBLE_WALL = new BlockRubbleWall(EnumRubble.DOLERITE).setUnlocalizedName(Unlocalized.WALL + "rubble.dolerite");

	/* Soft */
	public static final Block RED_CLAY = new BlockRedClay().setUnlocalizedName(Unlocalized.PREFIX + "redClay");
	public static final Block OOZE = new BlockOoze().setUnlocalizedName(Unlocalized.PREFIX + "ooze");
	public static final Block PEAT = new BlockPeat().setUnlocalizedName(Unlocalized.PREFIX + "peat");
	public static final SiltBlocks SILT = new SiltBlocks();

	/* Permafrost */
	public static final Block PERMAFROST = new BlockPermafrost().setUnlocalizedName(Unlocalized.PREFIX + "permafrost");
	public static final Block ANCIENT_PERMAFROST = new BlockAncientPermafrost().setUnlocalizedName(Unlocalized.PREFIX + "ancientPermafrost");

	/* Ores */
	public static final OreBlocks ORES = new OreBlocks();

	/* Trees */
	public static final TreeBlocksAndItems TREES = new TreeBlocksAndItems();
	public static final DebrisBlocks DEBRIS = new DebrisBlocks();
	public static final Block ROOTS = new BlockRoots().setUnlocalizedName(Unlocalized.PREFIX + "roots");

	/* Crafting */
	public static final BlockKnapper WORKBENCH = (BlockKnapper) new BlockKnapper().setUnlocalizedName(Unlocalized.CONTAINER_BLOCK + "workbench");
	public static final BlockCampfire CAMPFIRE = (BlockCampfire) new BlockCampfire().setUnlocalizedName(Unlocalized.CONTAINER_BLOCK + "campfire");
	public static final BlockStorageBox STORAGE_BOX = (BlockStorageBox) new BlockStorageBox().setUnlocalizedName(Unlocalized.CONTAINER_BLOCK + "storageBox");
	public static final BlockRottenStorageBox ROTTEN_STORAGE_BOX = (BlockRottenStorageBox) new BlockRottenStorageBox().setUnlocalizedName(Unlocalized.CONTAINER_BLOCK + "rottenStorageBox");

	/* Plants */
	public static final PlantBlocks PLANTS = new PlantBlocks();
	public static final Block ASPLENIUM = new BlockAsplenium().setUnlocalizedName(Unlocalized.PREFIX + "fern.asplenium");
	public static final BlockCalamites CALAMITES = (BlockCalamites) new BlockCalamites(true, 15, 7)
			.setGrowth(6, 1, 1, 1)
			.setUnlocalizedName(Unlocalized.PLANT + "calamites");
	public static final BlockAnkyropteris ANKYROPTERIS = (BlockAnkyropteris) new BlockAnkyropteris().setUnlocalizedName(Unlocalized.PREFIX + "ankyropteris");
	public static final BlockCobbania COBBANIA = (BlockCobbania) new BlockCobbania().setUnlocalizedName(Unlocalized.PREFIX + "cobbania");
	public static final VariantsCombo<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>> AQUATIC_PLANTS =
			new VariantsCombo<>(
					"aquatic_plants",
					new ObjectType<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>>(EnumAquaticPlant.class, "aquatic_plant", "aquaticPlant", BlockAquaticPlant.class, null)
							.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE),
					EnumAquaticPlant.class, EnumAquaticPlant.values());
	public static final BlockFrullania FRULLANIA = (BlockFrullania) new BlockFrullania().setUnlocalizedName(Unlocalized.PREFIX + "ankyropteris");

	/* Crops */
	protected static final SurviveOnDirtCustoms SURVIVE_ON_DIRT = new SurviveOnDirtCustoms();

	public static final BlockGrowingPlant SPHENOPHYLLUM = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true)
			.setPlantSoilTypes(EnumPlantType.Plains)
			.setGrowth(0.05F, 1, 3, 1.5F)
			.setPlantSize(0, 0.2F, 0.75F)
			.setCustoms(new BlockWaterSpreadingPlantCustoms(GenesisItems.MATERIALS.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER)))
			.setSoundType(GenesisSoundTypes.PLANT)
			.setUnlocalizedName(Unlocalized.PLANT + "sphenophyllum");
	public static final BlockGrowingPlant ODONTOPTERIS = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true).setBreakAllTogether(true)
			.setPlantSoilTypes(EnumPlantType.Crop)
			.setGrowth(0.05F, 1.5F, 2.5F, 1.05F)
			.setPlantSize(0, 0.2F, 0.75F)
			.setCustoms(new BlockOdontopterisCustoms())
			.setSoundType(GenesisSoundTypes.FERN)
			.setUnlocalizedName(Unlocalized.CROP + "odontopteris");
	public static final BlockGrowingPlant CLADOPHLEBIS = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true)
			.setPlantSoilTypes(EnumPlantType.Plains)
			.setGrowth(0.05F, 1, 2.5F, 1.05F)
			.setPlantSize(0, 0.2F, 0.75F)
			.setCustoms(new BlockWaterSpreadingPlantCustoms(GenesisItems.MATERIALS.getStack(EnumMaterial.CLADOPHLEBIS_FROND)))
			.setSoundType(GenesisSoundTypes.FERN)
			.setUnlocalizedName(Unlocalized.PLANT + "cladophlebis")
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	public static final BlockGrowingPlant PROGRAMINIS = (BlockGrowingPlant) new BlockGrowingPlant(false, 7, 1).setTopPosition(1)
			.setPlantSoilTypes(EnumPlantType.Crop)
			.setGrowthOnFarmland(0.75F)
			.setPlantSize(0, 0.1F, 0.75F)
			.setCustoms(SURVIVE_ON_DIRT)
			.setSoundType(GenesisSoundTypes.PLANT)
			.setUnlocalizedName(Unlocalized.CROP + "programinis");
	public static final BlockGrowingPlant ZINGIBEROPSIS = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true).setBreakAllTogether(true)
			.setPlantSize(0, 0.2F, 0.5F)
			.setPlantSoilTypes(EnumPlantType.Crop)
			.setGrowthOnFarmland(0.75F)
			.setCustoms(SURVIVE_ON_DIRT)
			.setSoundType(GenesisSoundTypes.PLANT)
			.setUnlocalizedName(Unlocalized.CROP + "zingiberopsis");

	public static final Block RESIN = new BlockResin().setUnlocalizedName(Unlocalized.PREFIX + "resin");

	/* Fluids */
	public static final BlockKomatiiticLava KOMATIITIC_LAVA = (BlockKomatiiticLava) new BlockKomatiiticLava(GenesisFluids.KOMATIITIC_LAVA).setUnlocalizedName(Unlocalized.PREFIX + "komatiiticLava");

	/* Other Decorative */
	public static final BlockGenesisFlowerPot FLOWER_POT = (BlockGenesisFlowerPot) new BlockGenesisFlowerPot().setUnlocalizedName(Unlocalized.PREFIX + "flowerPot");

	public static final Block CALAMITES_BUNDLE = new BlockCalamitesBundle().setUnlocalizedName(Unlocalized.PREFIX + "calamitesBundle");
	public static final Block CALAMITES_ROOF = new BlockGenesisStairs(CALAMITES_BUNDLE.getDefaultState()).setUnlocalizedName(Unlocalized.PREFIX + "calamitesRoof");
	public static final Block PROGRAMINIS_BUNDLE = new BlockPrograminisBundle().setUnlocalizedName(Unlocalized.PREFIX + "programinisBundle");
	public static final Block PROGRAMINIS_ROOF = new BlockGenesisStairs(PROGRAMINIS_BUNDLE.getDefaultState()).setUnlocalizedName(Unlocalized.PREFIX + "programinisRoof");

	public static final Block ROPE_LADDER = new BlockRopeLadder().setUnlocalizedName(Unlocalized.PREFIX + "ropeLadder");
	public static final Block CALAMITES_TORCH = new BlockCalamitesTorch().setUnlocalizedName(Unlocalized.PREFIX + "calamitesTorch");
	public static final Block CALAMITES_TORCH_TALL = new BlockTallTorch().setUnlocalizedName(Unlocalized.PREFIX + "calamitesTorch.tall");

	public static final BlockBenchSeat BENCH_SEAT = (BlockBenchSeat) new BlockBenchSeat().setUnlocalizedName(Unlocalized.PREFIX + "benchSeat");

	public static final DungBlocksAndItems DUNGS = new DungBlocksAndItems();
	public static final Block DUNG_BRICK = new BlockGenesis(Material.ROCK, SoundType.STONE)
			.setHardness(0.7F)
			.setUnlocalizedName(Unlocalized.PREFIX + "dungBrick");
	public static final BlockGenesisWall WATTLE_AND_DAUB = (BlockGenesisWall) new BlockGenesisWall(Material.WOOD, 0.375F, 1.0F, -1).setUnlocalizedName(Unlocalized.WALL + "wattleAndDaub");
	public static final Block PEAT_BRICK = new BlockGenesis(Material.ROCK, SoundType.STONE)
			.setHardness(0.7F)
			.setUnlocalizedName(Unlocalized.PREFIX + "peatBrick");

	public static final Block SMOKER = new BlockSmoker().setUnlocalizedName(Constants.Unlocalized.PREFIX + "smoker");

	/* Mechanisms */
	public static final BlockTrapFloor TRAP_FLOOR = (BlockTrapFloor) new BlockTrapFloor().setUnlocalizedName(Unlocalized.PREFIX + "trapFloor");

	/* Misc */
	public static final Block PALAEOAGARACITES = new BlockGenesisMushroom(BlockGenesisMushroom.MushroomGrowType.GROW_SIDE)
			.setBoundsSize(0.3125F, 0.5625F, 0.1875F)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
			.setUnlocalizedName(Unlocalized.PREFIX + "palaeoagaracites");
	public static final Block ARCHAEOMARASMIUS = new BlockGenesisMushroom(BlockGenesisMushroom.MushroomGrowType.GROW_TOP)
			.setBoundsSize(0.375F, 0.75F, 0)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
			.setUnlocalizedName(Unlocalized.PREFIX + "archaeomarasmius");
	public static final Block STEMONITIS = new BlockStemonitis().setUnlocalizedName(Unlocalized.PREFIX + "stemonitis");
	public static final BlockPrototaxites PROTOTAXITES = (BlockPrototaxites) new BlockPrototaxites().setUnlocalizedName(Unlocalized.PREFIX + "prototaxites");

	public static final VariantsCombo<EnumCoral, BlockGenesisVariants<EnumCoral>, ItemBlockMulti<EnumCoral>> CORAL =
			VariantsCombo.create(
					"corals",
					new ObjectType<EnumCoral, BlockGenesisVariants<EnumCoral>, ItemBlockMulti<EnumCoral>>(EnumCoral.class, "coral", ReflectionUtils.convertClass(BlockGenesisVariants.class), null)
							.setConstructedFunction((b, i) -> b.setHardness(0.75F).setResistance(8.5F))
							.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
							.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
							.setBlockArguments(Material.CORAL, GenesisSoundTypes.CORAL),
					EnumCoral.class, EnumCoral.values());

	private static ResourceLocation name(String path)
	{
		return new ResourceLocation(Constants.MOD_ID, path);
	}

	public static void preInitCommon()
	{
		// --- Building blocks ---
		// - Surface -
		Genesis.proxy.registerBlock(MOSS, new ItemMoss(MOSS), name("moss"), false);

		for (BlockMoss.EnumSoil soil : BlockMoss.EnumSoil.values())
		{
			Genesis.proxy.registerModel(MOSS, soil.getMetadata(), name("moss_" + soil.getName()));
		}

		Genesis.proxy.registerBlock(HUMUS, name("humus"));
		Genesis.proxy.registerBlock(HUMUS_PATH, name("humus_path"));

		// - Stone -
		Genesis.proxy.registerBlock(GRANITE, name("granite"));
		Genesis.proxy.registerBlock(MOSSY_GRANITE, name("mossy_granite"));
		Genesis.proxy.registerBlock(RADIOACTIVE_TRACES, name("radioactive_traces"));
		Genesis.proxy.registerBlock(RHYOLITE, name("rhyolite"));
		Genesis.proxy.registerBlock(DOLERITE, name("dolerite"));
		RUBBLE.registerAll();
		Genesis.proxy.registerBlock(KOMATIITE, name("komatiite"));
		Genesis.proxy.registerBlock(ANORTHOSITE, name("anorthosite"));
		Genesis.proxy.registerBlock(TRONDHJEMITE, name("trondhjemite"));
		Genesis.proxy.registerBlock(FAUX_AMPHIBOLITE, name("faux_amphibolite"));
		Genesis.proxy.registerBlock(GNEISS, name("gneiss"));
		Genesis.proxy.registerBlock(LIMESTONE, name("limestone"));
		Genesis.proxy.registerBlock(SMOOTH_LIMESTONE, name("smooth_limestone"));
		Genesis.proxy.registerBlock(OCTAEDRITE, name("octaedrite"));
		
		Genesis.proxy.registerBlock(ANCIENT_SMOKER, name("ancient_smoker"));

		// - Slabs -
		SLABS.registerAll();

		// - Rubble Walls -
		Genesis.proxy.registerBlock(GRANITE_RUBBLE_WALL, name("granite_rubble_wall"));
		Genesis.proxy.registerBlock(MOSSY_GRANITE_RUBBLE_WALL, name("mossy_granite_rubble_wall"));
		Genesis.proxy.registerBlock(RHYOLITE_RUBBLE_WALL, name("rhyolite_rubble_wall"));
		Genesis.proxy.registerBlock(DOLERITE_RUBBLE_WALL, name("dolerite_rubble_wall"));

		// - Soft -
		Genesis.proxy.registerBlock(RED_CLAY, name("red_clay"));
		Genesis.proxy.registerBlock(OOZE, name("ooze"));
		Genesis.proxy.registerBlock(PEAT, name("peat_block"));
		SILT.registerAll();

		// - Permafrost -
		Genesis.proxy.registerBlock(PERMAFROST, name("permafrost"));
		Genesis.proxy.registerBlock(ANCIENT_PERMAFROST, name("ancient_permafrost"));

		// - Ores -
		ORES.registerVariants(OreBlocks.ORE);

		// - Full Block Woody -
		TREES.registerVariants(TreeBlocksAndItems.LOG);
		TREES.registerVariants(TreeBlocksAndItems.BRANCH);

		Genesis.proxy.registerBlock(CALAMITES_BUNDLE, name("calamites_bundle"));
		Genesis.proxy.registerBlock(CALAMITES_ROOF, name("calamites_roof"));
		Genesis.proxy.registerBlock(PROGRAMINIS_BUNDLE, name("programinis_bundle"));
		Genesis.proxy.registerBlock(PROGRAMINIS_ROOF, name("programinis_roof"));

		// - Dungs -
		DUNGS.registerVariants(DungBlocksAndItems.DUNG_BLOCK);

		Genesis.proxy.registerBlock(DUNG_BRICK, name("dung_brick_block"));
		DUNG_BRICK.setHarvestLevel("pickaxe", 0);
		Blocks.FIRE.setFireInfo(DUNG_BRICK, 5, 5);

		Genesis.proxy.registerBlock(WATTLE_AND_DAUB, name("wattle_and_daub"));
		WATTLE_AND_DAUB.setHarvestLevel("axe", 0);
		WATTLE_AND_DAUB.setHardness(3);

		Genesis.proxy.registerBlock(PEAT_BRICK, name("peat_brick_block"));
		PEAT_BRICK.setHarvestLevel("pickaxe", 0);
		Blocks.FIRE.setFireInfo(PEAT_BRICK, 5, 5);

		// --- Mechanisms ---
		Genesis.proxy.registerBlock(TRAP_FLOOR, new ItemColored(TRAP_FLOOR, false), name("trap_floor"));
		Genesis.proxy.registerModel(TRAP_FLOOR, 0, name("trap_floor"));

		// --- Decorative ---
		MENHIRS.registerAll();
		GameRegistry.registerTileEntity(TileEntityMenhirGlyph.class, Constants.ASSETS_PREFIX + "menhir_glyph");
		GameRegistry.registerTileEntity(TileEntityMenhirReceptacle.class, Constants.ASSETS_PREFIX + "menhir_receptacle");

		Genesis.proxy.registerBlock(PORTAL, name("portal"), false);
		Genesis.proxy.registerModel(PORTAL, 0, name("portal/portal"));
		GameRegistry.registerTileEntity(TileEntityGenesisPortal.class, Constants.ASSETS_PREFIX + "portal");

		Genesis.proxy.registerBlock(SMOKER, name("smoker"));

		TREES.registerAll();

		DEBRIS.registerAll();

		Genesis.proxy.registerBlock(ROOTS, name("roots"));

		// - Resin -
		Genesis.proxy.registerBlock(RESIN, name("resin_block"));

		// - Containers -
		// Workbench
		Genesis.proxy.registerBlock(WORKBENCH, name("workbench"));
		GameRegistry.registerTileEntity(TileEntityKnapper.class, Constants.ASSETS_PREFIX + "workbench");

		// Campfire
		Genesis.proxy.registerBlock(CAMPFIRE, name("campfire"));
		Item.getItemFromBlock(CAMPFIRE).setMaxStackSize(1);
		GameRegistry.registerTileEntity(TileEntityCampfire.class, Constants.ASSETS_PREFIX + "campfire");
		// Lighter items
		CAMPFIRE.registerLighterItem(Items.FLINT_AND_STEEL, SoundEvents.ITEM_FLINTANDSTEEL_USE);
		CAMPFIRE.registerLighterItem(GenesisItems.FLINT_AND_MARCASITE, GenesisSoundEvents.ITEM_FLINT_AND_MARCASITE_USE);

		// Storage boxes
		Genesis.proxy.registerBlock(STORAGE_BOX, name("storage_box"));
		GameRegistry.registerTileEntity(TileEntityStorageBox.class, Constants.ASSETS_PREFIX + "storage_box");

		Genesis.proxy.registerBlock(ROTTEN_STORAGE_BOX, name("rotten_storage_box"));
		GameRegistry.registerTileEntity(TileEntityRottenStorageBox.class, Constants.ASSETS_PREFIX + "rotten_storage_box");

		// Rack
		GameRegistry.registerTileEntity(TileEntityRack.class, Constants.ASSETS_PREFIX + "rack");

		// - Bench seat -
		Genesis.proxy.registerBlock(BENCH_SEAT, new ItemBenchSeat(BENCH_SEAT), name("bench_seat"));

		// - Rope ladder -
		Genesis.proxy.registerBlock(ROPE_LADDER, name("rope_ladder"));

		// - Torches -
		Genesis.proxy.registerBlock(CALAMITES_TORCH, name("calamites_torch"));
		Genesis.proxy.registerBlock(CALAMITES_TORCH_TALL, name("calamites_torch_tall"));

		// - Plants -
		PLANTS.setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
		PLANTS.registerVariants(PlantBlocks.PLANT);
		PLANTS.registerVariants(PlantBlocks.DOUBLE_PLANT);

		// Calamites
		Genesis.proxy.registerBlock(CALAMITES, name("calamites"));
		CALAMITES.setDrops(new BlockDrops(CALAMITES, 1));
		CALAMITES.setCropDrops(new BlockDrops(CALAMITES, 1));

		// Ferns
		PLANTS.registerVariants(PlantBlocks.FERN);
		Genesis.proxy.registerBlock(ASPLENIUM, new ItemColored(ASPLENIUM, false), name("asplenium"));
		PLANTS.registerVariants(PlantBlocks.DOUBLE_FERN);

		// - Growing Plants -

		// Sphenophyllum
		Genesis.proxy.registerBlock(SPHENOPHYLLUM, new ItemColored(SPHENOPHYLLUM, false), name("sphenophyllum"));

		// Odontopteris
		Genesis.proxy.registerBlock(ODONTOPTERIS, null, name("odontopteris"));
		ItemStack drop = GenesisItems.SEEDS.getStack(EnumSeeds.ODONTOPTERIS_SEEDS);
		ODONTOPTERIS.setDrops(new BlockDrops(drop, 1, 1));
		ODONTOPTERIS.setCropDrops(new BlockDrops(drop, 1, 3));
		ODONTOPTERIS.setPickedStack(drop);

		// Cladophlebis
		Genesis.proxy.registerBlock(CLADOPHLEBIS, new ItemColored(CLADOPHLEBIS, false), name("cladophlebis"));
		Genesis.proxy.registerModel(CLADOPHLEBIS, 0, name("cladophlebis"));

		// Programinis
		Genesis.proxy.registerBlock(PROGRAMINIS, null, name("programinis"));
		drop = GenesisItems.SEEDS.getStack(EnumSeeds.PROGRAMINIS_SEEDS);
		PROGRAMINIS.setDrops(new BlockDrops(drop, 1, 1));
		PROGRAMINIS.setCropDrops(
				new BlockDrops(
					new BlockStackDrop(drop, 0, 3),
					new BlockStackDrop(GenesisItems.MATERIALS.getStack(EnumMaterial.PROGRAMINIS), 1)));
		PROGRAMINIS.setPickedStack(drop);

		// Zingiberopsis
		Genesis.proxy.registerBlock(ZINGIBEROPSIS, null, name("zingiberopsis"));
		drop = GenesisItems.SEEDS.getStack(EnumSeeds.ZINGIBEROPSIS_RHIZOME);
		ZINGIBEROPSIS.setDrops(new BlockDrops(drop, 1, 1));
		ZINGIBEROPSIS.setCropDrops(new BlockDrops(
				new BlockStackDrop(drop, 1, 3),
				new BlockRandomDrop(new BlockStackDrop(GenesisItems.ROTTEN_ZINGIBEROPSIS_RHIZOME, 1), 0.02F)
			));
		ZINGIBEROPSIS.setPickedStack(drop);

		// Ankyropteris
		Genesis.proxy.registerBlock(ANKYROPTERIS, new ItemColored(ANKYROPTERIS, false), name("ankyropteris"));
		Genesis.proxy.registerBlock(FRULLANIA, name("frullania"));

		// Flower pot
		Genesis.proxy.registerBlock(FLOWER_POT, null, name("flower_pot"));
		GameRegistry.registerTileEntity(TileEntityGenesisFlowerPot.class, Constants.ASSETS_PREFIX + "flower_pot");

		// - Mushrooms -
		Genesis.proxy.registerBlock(PALAEOAGARACITES, name("palaeoagaracites"));
		Genesis.proxy.registerBlock(ARCHAEOMARASMIUS, name("archaeomarasmius"));
		Genesis.proxy.registerBlock(STEMONITIS, name("stemonitis"));
		Genesis.proxy.registerBlock(PROTOTAXITES, name("prototaxites"));

		// - Water Plants -
		// Cobbania
		Genesis.proxy.registerBlock(COBBANIA, new ItemBlockFloating(COBBANIA), name("cobbania"));

		// Aquatic plants
		AQUATIC_PLANTS.setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
		AQUATIC_PLANTS.registerAll();

		// Corals
		CORAL.setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
		CORAL.registerAll();

		// --- Liquids ---
		Genesis.proxy.registerFluidBlock(KOMATIITIC_LAVA, name("komatiitic_lava"));

		IFlowerPotPlant plantCustoms = (c, w, p) -> PLANTS.getVariant(c).getColorMultiplier(w, p);

		BlockGenesisFlowerPot.registerPlantsForPot(PLANTS, PlantBlocks.PLANT, plantCustoms);
		BlockGenesisFlowerPot.registerPlantsForPot(PLANTS, PlantBlocks.FERN, plantCustoms);
		BlockGenesisFlowerPot.registerPlantsForPot(TREES, TreeBlocksAndItems.SAPLING, null);

		BlockGenesisFlowerPot.registerPlantForPot(new ItemStack(ARCHAEOMARASMIUS), "archaeomarasmius");
		FLOWER_POT.afterAllRegistered();
	}

	@SideOnly(Side.CLIENT)
	public static void preInitClient()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRack.class, new TileEntityRackRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGenesisPortal.class, new TileEntityGenesisPortalRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCampfire.class, new TileEntityCampfireRenderer(CAMPFIRE));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStorageBox.class, new TileEntityStorageBoxRenderer(STORAGE_BOX));

		ModelLoader.setCustomStateMapper(PORTAL, new FlexibleStateMap().setPrefix("portal/portal", ""));

		for (EnumMenhirPart part : EnumMenhirPart.ORDERED)
		{
			ItemStack stack = MENHIRS.getStack(part);

			if (part == EnumMenhirPart.GLYPH)
			{
				BlockMenhir block = MENHIRS.getBlock(part);

				for (EnumGlyph glyph : EnumGlyph.values())
				{
					ItemStack glyphStack = block.getGlyphStack(glyph);
					Genesis.proxy.registerModel(glyphStack.getItem(), glyphStack.getMetadata(), name("portal/glyph_" + glyph.getName()));
				}
			}
			else
			{
				Genesis.proxy.registerModel(stack.getItem(), stack.getMetadata(), name("portal/" + part.getName()));
			}
		}

		for (Item dung : DUNGS.getItems(DungBlocksAndItems.DUNG_BLOCK))
		{
			Genesis.proxy.registerModel(dung,
					MetadataModelDefinition.from(dung,
							(s) -> {
								StringBuilder name = new StringBuilder(DungBlocksAndItems.DUNG_BLOCK.getVariantName(DUNGS.getVariant(s)));
								int layers = BlockDung.HEIGHT_MASK.decode(s.getMetadata());

								if (layers == BlockDung.MAX_HEIGHT - BlockDung.MIN_HEIGHT)
									name.append("_full");
								else
									name.append("_layer");

								return name(name.toString());
							}));
		}
	}

	private static void registerColors(BlockColors blockColors, ItemColors itemColors, IBlockColor color, Block... blocks)
	{
		blockColors.registerBlockColorHandler(color, blocks);
		itemColors.registerItemColorHandler(
				(s, t) -> color.colorMultiplier(Block.getBlockFromItem(s.getItem()).getDefaultState(), null, null, t),
				blocks);
	}

	@SideOnly(Side.CLIENT)
	public static void initClient()
	{
		BlockColors blockCol = Minecraft.getMinecraft().getBlockColors();
		ItemColors itemCol = Minecraft.getMinecraft().getItemColors();

		// Moss
		registerColors(blockCol, itemCol,
				(s, w, p, t) -> s.getValue(BlockGrass.SNOWY)
						? 0xFFFFFF
						: Colorizers.BLOCK_MOSS.colorMultiplier(s, w, p, t),
				MOSS);

		// Leaves
		registerColors(blockCol, itemCol, Colorizers.BLOCK_LEAVES,
				TREES.getBlocks(TreeBlocksAndItems.LEAVES, TreeBlocksAndItems.LEAVES_FRUIT)
						.stream().toArray(Block[]::new));
		registerColors(blockCol, itemCol, Colorizers.BLOCK_LEAVES_TINT_1,
				TREES.getBlocks(TreeBlocksAndItems.BRANCH)
						.stream().toArray(Block[]::new));
		registerColors(blockCol, itemCol, Colorizers.BLOCK_GRASS, TRAP_FLOOR);

		// Plants
		BlockPlant<?>[] plantsArray =
				PLANTS.getBlocks(
						PlantBlocks.PLANT,
						PlantBlocks.DOUBLE_PLANT,
						PlantBlocks.FERN,
						PlantBlocks.DOUBLE_FERN)
				.stream().toArray(BlockPlant[]::new);
		blockCol.registerBlockColorHandler(
				(s, w, p, t) -> PLANTS.getVariant(s).getColorMultiplier(w, p),
				plantsArray);
		itemCol.registerItemColorHandler((s, t) -> PLANTS.getVariant(s).getColorMultiplier(null, null), plantsArray);

		registerColors(blockCol, itemCol, Colorizers.BLOCK_GRASS,
				COBBANIA, CLADOPHLEBIS, ANKYROPTERIS, ASPLENIUM, SPHENOPHYLLUM);

		blockCol.registerBlockColorHandler(Colorizers.BLOCK_GRASS,
				ODONTOPTERIS, PROGRAMINIS);
		
		// Flower Pots
		blockCol.registerBlockColorHandler(Colorizers.BLOCK_FLOWER_POT, FLOWER_POT);
		
	}
}
