package genesis.common;

import genesis.block.*;
import genesis.block.tileentity.*;
import genesis.block.tileentity.BlockGenesisFlowerPot.IFlowerPotPlant;
import genesis.block.tileentity.portal.*;
import genesis.block.tileentity.portal.render.TileEntityGenesisPortalRenderer;
import genesis.block.tileentity.render.*;
import genesis.client.Colorizers;
import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.*;
import genesis.common.sounds.GenesisSoundEvents;
import genesis.common.sounds.GenesisSoundTypes;
import genesis.item.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;
import genesis.util.random.drops.blocks.*;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;

public final class GenesisBlocks
{
	/* Portal */
	public static final VariantsCombo<EnumMenhirPart, BlockMenhir, ItemMenhir> menhirs =
			new VariantsCombo<>(
					"menhirs",
					new ObjectType<>(EnumMenhirPart.class, "menhir", "menhir", BlockMenhir.class, ItemMenhir.class)
							.setUseSeparateVariantJsons(false).setShouldRegisterVariantModels(false),
					EnumMenhirPart.class, EnumMenhirPart.values())
			.setNames(Constants.MOD_ID, Unlocalized.PREFIX);
	public static final BlockGenesisPortal portal = (BlockGenesisPortal) new BlockGenesisPortal().setUnlocalizedName(Unlocalized.MISC + "portal");
	
	/* Moss */
	public static final BlockMoss moss = (BlockMoss) new BlockMoss().setUnlocalizedName(Unlocalized.PREFIX + "moss");
	
	/* Humus */
	public static final Block humus = new BlockHumus().setUnlocalizedName(Unlocalized.PREFIX + "humus");
	public static final Block humus_path = new BlockGenesisPath(humus.getDefaultState()).setUnlocalizedName(Unlocalized.PREFIX + "humusPath");
	
	/* Rocks */
	public static final Block granite = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "granite");
	public static final Block mossy_granite = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "mossyGranite");
	public static final Block radioactive_granite = new BlockRadioactiveGranite().setUnlocalizedName(Unlocalized.ROCK + "radioactiveGranite");
	public static final Block rhyolite = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "rhyolite");
	public static final Block dolerite = new BlockGenesisRock(1.2F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "dolerite");
	public static final VariantsCombo<EnumRubble, BlockRubble, ItemBlockMulti<EnumRubble>> rubble =
			new VariantsCombo<>(
					"rubble",
					new ObjectType<EnumRubble, BlockRubble, ItemBlockMulti<EnumRubble>>(EnumRubble.class, "rubble", Unlocalized.Section.ROCK + "rubble", BlockRubble.class, null)
							.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.POSTFIX),
					EnumRubble.class, EnumRubble.values())
			.setNames(Constants.MOD_ID, Unlocalized.PREFIX);
	public static final Block komatiite = new BlockGenesisRock(1.95F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "komatiite");
	public static final Block anorthosite = new BlockGenesisRock(1.2F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "anorthosite");
	public static final Block trondhjemite = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "trondhjemite");
	public static final Block faux_amphibolite = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "fauxAmphibolite");
	public static final Block gneiss = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "gneiss");
	public static final Block limestone = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName(Unlocalized.ROCK + "limestone");
	public static final Block smooth_limestone = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName(Unlocalized.ROCK + "smoothLimestone");
	public static final Block octaedrite = new BlockGenesisRock(1.0F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "octaedrite");
	
	/* Slabs */
	public static final SlabBlocks slabs = new SlabBlocks();
	
	/* Rubble Walls */
	public static final Block granite_rubble_wall = new BlockRubbleWall(EnumRubble.GRANITE).setUnlocalizedName(Unlocalized.WALL + "rubble.granite");
	public static final Block mossy_granite_rubble_wall = new BlockRubbleWall(EnumRubble.MOSSY_GRANITE).setUnlocalizedName(Unlocalized.WALL + "rubble.mossyGranite");
	public static final Block rhyolite_rubble_wall = new BlockRubbleWall(EnumRubble.RHYOLITE).setUnlocalizedName(Unlocalized.WALL + "rubble.rhyolite");
	public static final Block dolerite_rubble_wall = new BlockRubbleWall(EnumRubble.DOLERITE).setUnlocalizedName(Unlocalized.WALL + "rubble.dolerite");
	
	/* Soft */
	public static final Block red_clay = new BlockRedClay().setUnlocalizedName(Unlocalized.PREFIX + "redClay");
	public static final Block ooze = new BlockOoze().setUnlocalizedName(Unlocalized.PREFIX + "ooze");
	public static final Block peat = new BlockPeat().setUnlocalizedName(Unlocalized.PREFIX + "peat");
	public static final SiltBlocks silt = new SiltBlocks();
	
	/* Permafrost */
	public static final Block permafrost = new BlockPermafrost().setUnlocalizedName(Unlocalized.PREFIX + "permafrost");
	public static final Block ancient_permafrost = new BlockAncientPermafrost().setUnlocalizedName(Unlocalized.PREFIX + "ancientPermafrost");
	
	/* Ores */
	public static final OreBlocks ores = new OreBlocks();
	
	/* Trees */
	public static final TreeBlocksAndItems trees = new TreeBlocksAndItems();
	public static final DebrisBlocks debris = new DebrisBlocks();
	public static final Block roots = new BlockRoots().setUnlocalizedName(Unlocalized.PREFIX + "roots");
	
	/* Crafting */
	public static final BlockKnapper workbench = (BlockKnapper) new BlockKnapper().setUnlocalizedName(Unlocalized.CONTAINER_BLOCK + "workbench");
	public static final BlockCampfire campfire = (BlockCampfire) new BlockCampfire().setUnlocalizedName(Unlocalized.CONTAINER_BLOCK + "campfire");
	public static final BlockStorageBox storage_box = (BlockStorageBox) new BlockStorageBox().setUnlocalizedName(Unlocalized.CONTAINER_BLOCK + "storageBox");
	public static final BlockRottenStorageBox rotten_storage_box = (BlockRottenStorageBox) new BlockRottenStorageBox().setUnlocalizedName(Unlocalized.CONTAINER_BLOCK + "rottenStorageBox");
	
	/* Plants */
	public static final PlantBlocks plants = new PlantBlocks();
	public static final Block asplenium = new BlockAsplenium().setUnlocalizedName(Unlocalized.PREFIX + "fern.asplenium");
	public static final BlockCalamites calamites = (BlockCalamites) new BlockCalamites(true, 15, 7)
			.setGrowth(6, 1, 1, 1)
			.setUnlocalizedName(Unlocalized.PLANT + "calamites");
	public static final BlockAnkyropteris ankyropteris = (BlockAnkyropteris) new BlockAnkyropteris().setUnlocalizedName(Unlocalized.PREFIX + "ankyropteris");
	public static final BlockCobbania cobbania = (BlockCobbania) new BlockCobbania().setUnlocalizedName(Unlocalized.PREFIX + "cobbania");
	public static final VariantsCombo<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>> aquatic_plants =
			new VariantsCombo<>(
					"aquatic_plants",
					new ObjectType<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>>(EnumAquaticPlant.class, "aquatic_plant", "aquaticPlant", BlockAquaticPlant.class, null)
							.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE),
					EnumAquaticPlant.class, EnumAquaticPlant.values());
	
	/* Crops */
	protected static final SurviveOnDirtCustoms surviveOnDirt = new SurviveOnDirtCustoms();
	
	public static final BlockGrowingPlant sphenophyllum = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true)
			.setPlantSoilTypes(EnumPlantType.Plains)
			.setGrowth(0.05F, 1, 3, 1.5F)
			.setPlantSize(0, 0.2F, 0.75F)
			.setCustoms(new BlockWaterSpreadingPlantCustoms(GenesisItems.materials.getStack(EnumMaterial.SPHENOPHYLLUM_FIBER)))
			.setUnlocalizedName(Unlocalized.PLANT + "sphenophyllum");
	public static final BlockGrowingPlant odontopteris = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true).setBreakAllTogether(true)
			.setPlantSoilTypes(EnumPlantType.Crop)
			.setGrowth(0.05F, 1.5F, 2.5F, 1.05F)
			.setUseBiomeColor(true)
			.setPlantSize(0, 0.2F, 0.75F)
			.setCustoms(new BlockOdontopterisCustoms())
			.setSoundType(GenesisSoundTypes.FERN)
			.setUnlocalizedName(Unlocalized.CROP + "odontopteris");
	public static final BlockGrowingPlant cladophlebis = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true)
			.setUseBiomeColor(true)
			.setPlantSoilTypes(EnumPlantType.Plains)
			.setGrowth(0.05F, 1, 2.5F, 1.05F)
			.setPlantSize(0, 0.2F, 0.75F)
			.setCustoms(new BlockWaterSpreadingPlantCustoms(GenesisItems.materials.getStack(EnumMaterial.CLADOPHLEBIS_FROND)))
			.setSoundType(GenesisSoundTypes.FERN)
			.setUnlocalizedName(Unlocalized.PLANT + "cladophlebis")
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	public static final BlockGrowingPlant programinis = (BlockGrowingPlant) new BlockGrowingPlant(false, 7, 1).setTopPosition(1)
			.setPlantSoilTypes(EnumPlantType.Crop)
			.setGrowthOnFarmland(0.75F)
			.setUseBiomeColor(true)
			.setPlantSize(0, 0.1F, 0.75F)
			.setCustoms(surviveOnDirt)
			.setUnlocalizedName(Unlocalized.CROP + "programinis");
	public static final BlockGrowingPlant zingiberopsis = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true).setBreakAllTogether(true)
			.setPlantSize(0, 0.2F, 0.5F)
			.setPlantSoilTypes(EnumPlantType.Crop)
			.setGrowthOnFarmland(0.75F)
			.setCustoms(surviveOnDirt)
			.setUnlocalizedName(Unlocalized.CROP + "zingiberopsis");
	
	public static final Block resin = new BlockResin().setUnlocalizedName(Unlocalized.PREFIX + "resin");
	
	/* Fluids */
	public static final BlockKomatiiticLava komatiitic_lava = (BlockKomatiiticLava) new BlockKomatiiticLava(GenesisFluids.KOMATIITIC_LAVA).setUnlocalizedName(Unlocalized.PREFIX + "komatiiticLava");
	
	/* Other Decorative */
	public static final BlockGenesisFlowerPot flower_pot = (BlockGenesisFlowerPot) new BlockGenesisFlowerPot().setUnlocalizedName(Unlocalized.PREFIX + "flowerPot");
	
	public static final Block calamites_bundle = new BlockCalamitesBundle().setUnlocalizedName(Unlocalized.PREFIX + "calamitesBundle");
	public static final Block calamites_roof = new BlockGenesisStairs(calamites_bundle.getDefaultState()).setUnlocalizedName(Unlocalized.PREFIX + "calamitesRoof");
	public static final Block programinis_bundle = new BlockPrograminisBundle().setUnlocalizedName(Unlocalized.PREFIX + "programinisBundle");
	public static final Block programinis_roof = new BlockGenesisStairs(programinis_bundle.getDefaultState()).setUnlocalizedName(Unlocalized.PREFIX + "programinisRoof");
	
	public static final Block rope_ladder = new BlockRopeLadder().setUnlocalizedName(Unlocalized.PREFIX + "ropeLadder");
	public static final Block calamites_torch = new BlockCalamitesTorch().setUnlocalizedName(Unlocalized.PREFIX + "calamitesTorch");
	public static final Block calamites_torch_tall = new BlockTallTorch().setUnlocalizedName(Unlocalized.PREFIX + "calamitesTorch.tall");
	public static final Block prototaxites_mycelium = new BlockPrototaxitesMycelium().setUnlocalizedName(Unlocalized.PREFIX + "prototaxitesMycelium");
	
	public static final Block bench_seat = new BlockBenchSeat().setUnlocalizedName(Unlocalized.PREFIX + "benchSeat");
	
	public static final DungBlocksAndItems dungs = new DungBlocksAndItems();
	public static final Block dung_brick = new BlockGenesis(Material.rock, SoundType.STONE)
			.setHardness(0.7F)
			.setUnlocalizedName(Unlocalized.PREFIX + "dungBrick");
	public static final BlockGenesisWall wattle_and_daub = (BlockGenesisWall) new BlockGenesisWall(Material.wood, 0.375F, 1.0F, -1).setUnlocalizedName(Unlocalized.WALL + "wattleAndDaub");
	
	/* Mechanisms */
	public static final BlockTrapFloor trap_floor = (BlockTrapFloor) new BlockTrapFloor().setUnlocalizedName(Unlocalized.PREFIX + "trapFloor");
	
	/* Misc */
	public static final Block palaeoagaracites = new BlockGenesisMushroom(BlockGenesisMushroom.MushroomGrowType.GROW_SIDE)
			.setBoundsSize(0.3125F, 0.5625F, 0.1875F)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
			.setUnlocalizedName(Unlocalized.PREFIX + "palaeoagaracites");
	public static final Block archaeomarasmius = new BlockGenesisMushroom(BlockGenesisMushroom.MushroomGrowType.GROW_TOP)
			.setBoundsSize(0.375F, 0.75F, 0)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
			.setUnlocalizedName(Unlocalized.PREFIX + "archaeomarasmius");
	public static final Block stemonitis = new BlockStemonitis().setUnlocalizedName(Unlocalized.PREFIX + "stemonitis");
	public static final BlockPrototaxites prototaxites = (BlockPrototaxites) new BlockPrototaxites().setUnlocalizedName(Unlocalized.PREFIX + "prototaxites");
	
	public static final VariantsCombo<EnumCoral, BlockGenesisVariants<EnumCoral>, ItemBlockMulti<EnumCoral>> coral =
			VariantsCombo.create(
					"corals",
					new ObjectType<EnumCoral, BlockGenesisVariants<EnumCoral>, ItemBlockMulti<EnumCoral>>(EnumCoral.class, "coral", ReflectionUtils.convertClass(BlockGenesisVariants.class), null)
							.setConstructedFunction((b, i) -> b.setHardness(0.75F).setResistance(8.5F))
							.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
							.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
							.setBlockArguments(Material.coral, GenesisSoundTypes.CORAL),
					EnumCoral.class, EnumCoral.values());
	
	private static ResourceLocation name(String path)
	{
		return new ResourceLocation(Constants.MOD_ID, path);
	}
	
	public static void preInitCommon()
	{
		// --- Building blocks ---
		// - Surface -
		Genesis.proxy.registerBlock(moss, new ItemMoss(moss), name("moss"), false);
		
		for (BlockMoss.EnumSoil soil : BlockMoss.EnumSoil.values())
		{
			Genesis.proxy.registerModel(moss, soil.getMetadata(), name("moss_" + soil.getName()));;
		}
		
		Genesis.proxy.registerBlock(humus, name("humus"));
		Genesis.proxy.registerBlock(humus_path, name("humus_path"));
		
		// - Stone -
		Genesis.proxy.registerBlock(granite, name("granite"));
		Genesis.proxy.registerBlock(mossy_granite, name("mossy_granite"));
		Genesis.proxy.registerBlock(radioactive_granite, name("radioactive_granite"));
		Genesis.proxy.registerBlock(rhyolite, name("rhyolite"));
		Genesis.proxy.registerBlock(dolerite, name("dolerite"));
		rubble.registerAll();
		Genesis.proxy.registerBlock(komatiite, name("komatiite"));
		Genesis.proxy.registerBlock(anorthosite, name("anorthosite"));
		Genesis.proxy.registerBlock(trondhjemite, name("trondhjemite"));
		Genesis.proxy.registerBlock(faux_amphibolite, name("faux_amphibolite"));
		Genesis.proxy.registerBlock(gneiss, name("gneiss"));
		Genesis.proxy.registerBlock(limestone, name("limestone"));
		Genesis.proxy.registerBlock(smooth_limestone, name("smooth_limestone"));
		Genesis.proxy.registerBlock(octaedrite, name("octaedrite"));
		
		// - Slabs -
		slabs.registerAll();
		
		// - Rubble Walls -
		Genesis.proxy.registerBlock(granite_rubble_wall, name("granite_rubble_wall"));
		Genesis.proxy.registerBlock(mossy_granite_rubble_wall, name("mossy_granite_rubble_wall"));
		Genesis.proxy.registerBlock(rhyolite_rubble_wall, name("rhyolite_rubble_wall"));
		Genesis.proxy.registerBlock(dolerite_rubble_wall, name("dolerite_rubble_wall"));
		
		// - Soft -
		Genesis.proxy.registerBlock(red_clay, name("red_clay"));
		Genesis.proxy.registerBlock(ooze, name("ooze"));
		Genesis.proxy.registerBlock(peat, name("peat"));
		silt.registerAll();
		
		// - Permafrost -
		Genesis.proxy.registerBlock(permafrost, name("permafrost"));
		Genesis.proxy.registerBlock(ancient_permafrost, name("ancient_permafrost"));
		
		// - Ores -
		ores.registerVariants(OreBlocks.ORE);
		
		// - Full Block Woody -
		trees.registerVariants(TreeBlocksAndItems.LOG);
		trees.registerVariants(TreeBlocksAndItems.BRANCH);
		
		Genesis.proxy.registerBlock(calamites_bundle, name("calamites_bundle"));
		Genesis.proxy.registerBlock(calamites_roof, name("calamites_roof"));
		Genesis.proxy.registerBlock(programinis_bundle, name("programinis_bundle"));
		Genesis.proxy.registerBlock(programinis_roof, name("programinis_roof"));
		
		Genesis.proxy.registerBlock(prototaxites_mycelium, name("prototaxites_mycelium"));
		
		// - Dungs -
		dungs.registerVariants(DungBlocksAndItems.DUNG_BLOCK);
		
		Genesis.proxy.registerBlock(dung_brick, name("dung_brick_block"));
		dung_brick.setHarvestLevel("pickaxe", 0);
		Blocks.fire.setFireInfo(dung_brick, 5, 5);
		
		Genesis.proxy.registerBlock(wattle_and_daub, name("wattle_and_daub"));
		wattle_and_daub.setHarvestLevel("axe", 0);
		wattle_and_daub.setHardness(3);
		
		// --- Mechanisms ---
		Genesis.proxy.registerBlock(trap_floor, new ItemColored(trap_floor, false), name("trap_floor"));
		Genesis.proxy.registerModel(trap_floor, 0, name("trap_floor"));
		
		// --- Decorative ---
		menhirs.registerAll();
		GameRegistry.registerTileEntity(TileEntityMenhirGlyph.class, Constants.ASSETS_PREFIX + "menhir_glyph");
		GameRegistry.registerTileEntity(TileEntityMenhirReceptacle.class, Constants.ASSETS_PREFIX + "menhir_receptacle");
		
		Genesis.proxy.registerBlock(portal, name("portal"), false);
		Genesis.proxy.registerModel(portal, 0, name("portal/portal"));
		GameRegistry.registerTileEntity(TileEntityGenesisPortal.class, Constants.ASSETS_PREFIX + "portal");
		
		trees.registerAll();
		
		debris.registerAll();
		
		Genesis.proxy.registerBlock(roots, name("roots"));
		
		Genesis.proxy.registerBlock(bench_seat, name("bench_seat"));
		
		//Resin
		Genesis.proxy.registerBlock(resin, name("resin_block"));
		
		// - Containers -
		// Workbench
		Genesis.proxy.registerBlock(workbench, name("workbench"));
		GameRegistry.registerTileEntity(TileEntityKnapper.class, Constants.ASSETS_PREFIX + "workbench");
		
		// Campfire
		Genesis.proxy.registerBlock(campfire, name("campfire"));
		Item.getItemFromBlock(campfire).setMaxStackSize(1);
		GameRegistry.registerTileEntity(TileEntityCampfire.class, Constants.ASSETS_PREFIX + "campfire");
		// Lighter items
		campfire.registerLighterItem(Items.flint_and_steel, SoundEvents.item_flintandsteel_use);
		campfire.registerLighterItem(GenesisItems.flint_and_marcasite, GenesisSoundEvents.item_flint_and_marcasite_use);
		
		// Storage boxes
		Genesis.proxy.registerBlock(storage_box, name("storage_box"));
		GameRegistry.registerTileEntity(TileEntityStorageBox.class, Constants.ASSETS_PREFIX + "storage_box");
		
		Genesis.proxy.registerBlock(rotten_storage_box, name("rotten_storage_box"));
		GameRegistry.registerTileEntity(TileEntityRottenStorageBox.class, Constants.ASSETS_PREFIX + "rotten_storage_box");
		
		// Rack
		GameRegistry.registerTileEntity(TileEntityRack.class, Constants.ASSETS_PREFIX + "rack");
		
		// - Rope ladder -
		Genesis.proxy.registerBlock(rope_ladder, name("rope_ladder"));
		
		// - Torches -
		Genesis.proxy.registerBlock(calamites_torch, name("calamites_torch"));
		Genesis.proxy.registerBlock(calamites_torch_tall, name("calamites_torch_tall"));
		
		// - Plants -
		plants.setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
		plants.registerVariants(PlantBlocks.PLANT);
		plants.registerVariants(PlantBlocks.DOUBLE_PLANT);
		
		// Calamites
		Genesis.proxy.registerBlock(calamites, name("calamites"));
		calamites.setDrops(new BlockDrops(calamites, 1));
		calamites.setCropDrops(new BlockDrops(calamites, 1));
		
		// Ferns
		plants.registerVariants(PlantBlocks.FERN);
		Genesis.proxy.registerBlock(asplenium, new ItemColored(asplenium, false), name("asplenium"));
		plants.registerVariants(PlantBlocks.DOUBLE_FERN);
		
		// - Growing Plants -
		
		// Sphenophyllum
		Genesis.proxy.registerBlock(sphenophyllum, name("sphenophyllum"));
		
		// Odontopteris
		Genesis.proxy.registerBlock(odontopteris, null, name("odontopteris"));
		ItemStack drop = GenesisItems.seeds.getStack(EnumSeeds.ODONTOPTERIS_SEEDS);
		odontopteris.setDrops(new BlockDrops(drop, 1, 1));
		odontopteris.setCropDrops(new BlockDrops(drop, 1, 3));
		odontopteris.setPickedStack(drop);
		
		// Cladophlebis
		Genesis.proxy.registerBlock(cladophlebis, new ItemColored(cladophlebis, false), name("cladophlebis"));
		Genesis.proxy.registerModel(cladophlebis, 0, name("cladophlebis"));
		
		// Programinis
		Genesis.proxy.registerBlock(programinis, null, name("programinis"));
		drop = GenesisItems.seeds.getStack(EnumSeeds.PROGRAMINIS_SEEDS);
		programinis.setDrops(new BlockDrops(drop, 1, 1));
		programinis.setCropDrops(
				new BlockDrops(
					new BlockStackDrop(drop, 0, 3),
					new BlockStackDrop(GenesisItems.materials.getStack(EnumMaterial.PROGRAMINIS), 1)));
		programinis.setPickedStack(drop);
		
		// Zingiberopsis
		Genesis.proxy.registerBlock(zingiberopsis, null, name("zingiberopsis"));
		drop = GenesisItems.seeds.getStack(EnumSeeds.ZINGIBEROPSIS_RHIZOME);
		zingiberopsis.setDrops(new BlockDrops(drop, 1, 1));
		zingiberopsis.setCropDrops(new BlockDrops(
				new BlockStackDrop(drop, 1, 3),
				new BlockRandomDrop(new BlockStackDrop(GenesisItems.rotten_zingiberopsis_rhizome, 1), 0.02D)
			));
		zingiberopsis.setPickedStack(drop);
		
		// Ankyropteris
		Genesis.proxy.registerBlock(ankyropteris, new ItemColored(ankyropteris, false), name("ankyropteris"));
		
		// Flower pot
		Genesis.proxy.registerBlock(flower_pot, null, name("flower_pot"));
		GameRegistry.registerTileEntity(TileEntityGenesisFlowerPot.class, Constants.ASSETS_PREFIX + "flower_pot");
		
		// - Mushrooms -
		Genesis.proxy.registerBlock(palaeoagaracites, name("palaeoagaracites"));
		Genesis.proxy.registerBlock(archaeomarasmius, name("archaeomarasmius"));
		Genesis.proxy.registerBlock(stemonitis, name("stemonitis"));
		Genesis.proxy.registerBlock(prototaxites, name("prototaxites"));
		
		// - Water Plants -
		// Cobbania
		Genesis.proxy.registerBlock(cobbania, new ItemBlockFloating(cobbania), name("cobbania"));
		
		// Aquatic plants
		aquatic_plants.setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
		aquatic_plants.registerAll();
		
		// Corals
		coral.setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
		coral.registerAll();
		
		// --- Liquids ---
		Genesis.proxy.registerFluidBlock(komatiitic_lava, name("komatiitic_lava"));
		
		IFlowerPotPlant plantCustoms = (c, w, p) -> plants.getVariant(c).getColorMultiplier(w, p);
		
		BlockGenesisFlowerPot.registerPlantsForPot(plants, PlantBlocks.PLANT, plantCustoms);
		BlockGenesisFlowerPot.registerPlantsForPot(plants, PlantBlocks.FERN, plantCustoms);
		BlockGenesisFlowerPot.registerPlantsForPot(trees, TreeBlocksAndItems.SAPLING, null);
		
		BlockGenesisFlowerPot.registerPlantForPot(new ItemStack(archaeomarasmius), "archaeomarasmius");
		flower_pot.afterAllRegistered();
	}
	
	@SideOnly(Side.CLIENT)
	public static void preInitClient()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRack.class, new TileEntityRackRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGenesisPortal.class, new TileEntityGenesisPortalRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCampfire.class, new TileEntityCampfireRenderer(campfire));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStorageBox.class, new TileEntityStorageBoxRenderer(storage_box));
		
		ModelLoader.setCustomStateMapper(portal, new FlexibleStateMap().setPrefix("portal/portal", ""));
		
		for (EnumMenhirPart part : EnumMenhirPart.ORDERED)
		{
			ItemStack stack = menhirs.getStack(part);
			
			if (part == EnumMenhirPart.GLYPH)
			{
				BlockMenhir block = menhirs.getBlock(part);
				
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
		registerColors(blockCol, itemCol, Colorizers.BLOCK_MOSS, moss);
		
		// Leaves
		Block[] leaves =
				trees.getBlocks(
						TreeBlocksAndItems.LEAVES,
						TreeBlocksAndItems.LEAVES_FRUIT,
						TreeBlocksAndItems.BRANCH)
				.toArray(new Block[0]);
		registerColors(blockCol, itemCol, Colorizers.BLOCK_LEAVES, leaves);
		registerColors(blockCol, itemCol, Colorizers.BLOCK_GRASS, trap_floor);
		
		// Plants
		BlockPlant<?>[] plantsArray =
				plants.getBlocks(
						PlantBlocks.PLANT,
						PlantBlocks.DOUBLE_PLANT,
						PlantBlocks.FERN,
						PlantBlocks.DOUBLE_FERN)
				.toArray(new BlockPlant<?>[0]);
		blockCol.registerBlockColorHandler(
				(s, w, p, t) -> plants.getVariant(s).getColorMultiplier(w, p),
				plantsArray);
		itemCol.registerItemColorHandler((s, t) -> plants.getVariant(s).getColorMultiplier(null, null), plantsArray);
		
		registerColors(blockCol, itemCol, Colorizers.BLOCK_GRASS,
				cobbania, cladophlebis, ankyropteris, asplenium);
		
		blockCol.registerBlockColorHandler(Colorizers.BLOCK_GRASS,
				odontopteris, programinis);
	}
}
