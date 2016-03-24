package genesis.common;

import genesis.block.*;
import genesis.block.tileentity.*;
import genesis.block.tileentity.BlockGenesisFlowerPot.IFlowerPotPlant;
import genesis.block.tileentity.portal.*;
import genesis.block.tileentity.portal.render.TileEntityGenesisPortalRenderer;
import genesis.block.tileentity.render.*;
import genesis.client.*;
import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumAquaticPlant;
import genesis.combo.variant.EnumCoral;
import genesis.combo.variant.EnumMaterial;
import genesis.combo.variant.EnumMenhirPart;
import genesis.combo.variant.EnumPlant;
import genesis.combo.variant.EnumSeeds;
import genesis.combo.variant.IMetadata;
import genesis.entity.extendedproperties.GenesisEntityData;
import genesis.item.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;
import genesis.util.random.drops.blocks.BlockDrops;
import genesis.util.random.drops.blocks.BlockStackDrop;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;

public final class GenesisBlocks
{
	/* Portal */
	public static final VariantsCombo<EnumMenhirPart, BlockMenhir, ItemMenhir> menhirs =
			new VariantsCombo<EnumMenhirPart, BlockMenhir, ItemMenhir>(
					new ObjectType<BlockMenhir, ItemMenhir>("menhir", Unlocalized.PREFIX + "menhir", BlockMenhir.class, ItemMenhir.class)
							.setUseSeparateVariantJsons(false).setShouldRegisterVariantModels(false),
					EnumMenhirPart.class, EnumMenhirPart.values()
			);
	public static final BlockGenesisPortal portal = (BlockGenesisPortal) new BlockGenesisPortal().setUnlocalizedName(Unlocalized.MISC + "portal");
	
	/* Moss */
	public static final BlockMoss moss = (BlockMoss) new BlockMoss().setUnlocalizedName(Unlocalized.PREFIX + "moss");
	
	/* Rocks */
	public static final Block granite = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "granite");
	public static final Block mossy_granite = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "mossyGranite");
	public static final Block rhyolite = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "rhyolite");
	public static final Block dolerite = new BlockGenesisRock(1.2F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "dolerite");
	public static final Block komatiite = new BlockGenesisRock(1.95F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "komatiite");
	public static final Block anorthosite = new BlockGenesisRock(1.2F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "anorthosite");
	public static final Block trondhjemite = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "trondhjemite");
	public static final Block faux_amphibolite = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "fauxAmphibolite");
	public static final Block gneiss = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "gneiss");
	public static final Block limestone = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName(Unlocalized.ROCK + "limestone");
	public static final Block octaedrite = new BlockGenesisRock(1.0F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "octaedrite");
	public static final Block red_clay = new BlockRedClay().setUnlocalizedName(Unlocalized.PREFIX + "redClay");
	public static final Block ooze = new BlockOoze().setUnlocalizedName(Unlocalized.PREFIX + "ooze");
	public static final Block peat = new BlockPeat().setUnlocalizedName(Unlocalized.PREFIX + "peat");
	public static final SiltBlocks silt = new SiltBlocks();
	
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
	public static final BlockCalamites calamites = (BlockCalamites) new BlockCalamites(true, 15, 7)
			.setGrowth(6, 1, 1, 1)
			.setUnlocalizedName(Unlocalized.PLANT + "calamites");
	public static final BlockAnkyropteris ankyropteris = (BlockAnkyropteris) new BlockAnkyropteris().setUnlocalizedName(Unlocalized.PREFIX + "ankyropteris");
	public static final Block cobbania = new BlockCobbania().setUnlocalizedName(Unlocalized.PREFIX + "cobbania");
	public static final VariantsCombo<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>> aquatic_plants =
			new VariantsCombo<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>>(
					new ObjectType<BlockAquaticPlant, ItemBlockMulti<EnumAquaticPlant>>("aquatic_plant", "aquaticPlant", BlockAquaticPlant.class, null)
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
			.setUnlocalizedName(Unlocalized.CROP + "odontopteris");
	public static final BlockGrowingPlant cladophlebis = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true)
			.setUseBiomeColor(true)
			.setPlantSoilTypes(EnumPlantType.Plains)
			.setGrowth(0.05F, 1, 2.5F, 1.05F)
			.setPlantSize(0, 0.2F, 0.75F)
			.setCustoms(new BlockWaterSpreadingPlantCustoms(GenesisItems.materials.getStack(EnumMaterial.CLADOPHLEBIS_FROND)))
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
	
	/* Fluids */
	public static final BlockKomatiiticLava komatiitic_lava = (BlockKomatiiticLava) new BlockKomatiiticLava(GenesisFluids.KOMATIITIC_LAVA).setUnlocalizedName(Unlocalized.PREFIX + "komatiiticLava");
	
	/* Other Decorative */
	public static final BlockGenesisFlowerPot flower_pot = (BlockGenesisFlowerPot) new BlockGenesisFlowerPot().setUnlocalizedName(Unlocalized.PREFIX + "flowerPot");
	
	public static final Block calamites_bundle = new BlockCalamitesBundle().setUnlocalizedName(Unlocalized.PREFIX + "calamitesBundle");
	public static final Block calamites_roof = new BlockGenesisStairs(calamites_bundle.getDefaultState()).setUnlocalizedName(Unlocalized.PREFIX + "calamitesRoof");
	public static final Block programinis_bundle = new BlockPrograminisBundle().setUnlocalizedName(Unlocalized.PREFIX + "programinisBundle");
	public static final Block programinis_roof = new BlockGenesisStairs(programinis_bundle.getDefaultState()).setUnlocalizedName(Unlocalized.PREFIX + "programinisRoof");
	
	public static final Block calamites_torch = new BlockCalamitesTorch().setUnlocalizedName(Unlocalized.PREFIX + "calamitesTorch");
	public static final Block calamites_torch_tall = new BlockTallTorch().setUnlocalizedName(Unlocalized.PREFIX + "calamitesTorch.tall");
	public static final Block prototaxites_mycelium = new BlockPrototaxitesMycelium().setUnlocalizedName(Unlocalized.PREFIX + "prototaxitesMycelium");
	
	public static final DungBlocksAndItems dungs = new DungBlocksAndItems();
	public static final Block dung_brick_block = new BlockGenesis(Material.rock)
			.setHardness(0.7F)
			.setStepSound(Block.soundTypePiston)
			.setUnlocalizedName(Unlocalized.PREFIX + "dungBrickBlock");
	public static final BlockGenesisWall wattle_and_daub = (BlockGenesisWall) new BlockGenesisWall(Material.wood, 0.375F, 1.0F, -1).setUnlocalizedName(Unlocalized.PREFIX + "wattleAndDaub");
	
	/* Mechanisms */
	public static final BlockTrapFloor trap_floor = (BlockTrapFloor) new BlockTrapFloor().setUnlocalizedName(Unlocalized.PREFIX + "trapFloor");
	
	/* Misc */
	public static final Block palaeoagaracites = new BlockGenesisMushroom().setUnlocalizedName(Unlocalized.PREFIX + "palaeoagaracites")
			.setGrowType(BlockGenesisMushroom.MushroomGrowType.GROW_SIDE)
			.setBoundsSize(0.3125F, 0.5625F, 0.1875F)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	public static final Block archaeomarasmius = new BlockGenesisMushroom().setUnlocalizedName(Unlocalized.PREFIX + "archaeomarasmius")
			.setBoundsSize(0.375F, 0.75F, 0)
			.setGrowType(BlockGenesisMushroom.MushroomGrowType.GROW_TOP)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	public static final BlockPrototaxites prototaxites = (BlockPrototaxites) new BlockPrototaxites()
			.setUnlocalizedName(Unlocalized.PREFIX + "prototaxites");
	
	@SuppressWarnings("unchecked")
	public static final VariantsCombo<EnumCoral, BlockGenesisVariants<EnumCoral>, ItemBlockMulti<EnumCoral>> corals =
			VariantsCombo.create(
					new ObjectType<BlockGenesisVariants<EnumCoral>, ItemBlockMulti<EnumCoral>>("coral", (Class<BlockGenesisVariants<EnumCoral>>) ((Class<?>) BlockGenesisVariants.class), null)
							{
								@Override
								public <V extends IMetadata<V>> void afterConstructed(BlockGenesisVariants<EnumCoral> block, ItemBlockMulti<EnumCoral> item, List<V> variants)
								{
									super.afterConstructed(block, item, variants);
									
									block.setHardness(0.75F);
									block.setResistance(8.5F);
									block.setStepSound(GenesisSounds.CORAL);
								}
							}.setUseSeparateVariantJsons(false).setTypeNamePosition(TypeNamePosition.NONE)
							.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
							.setBlockArguments(Material.coral),
					EnumCoral.class, EnumCoral.values());
	
	public static void registerBlocks()
	{
		// --- Building blocks ---
		// - Surface -
		Genesis.proxy.registerBlock(moss, "moss", ItemMoss.class, false);
		
		for (int mossStage = 0; mossStage <= BlockMoss.STAGE_LAST; mossStage++)
		{
			Genesis.proxy.registerModel(moss, mossStage, "moss_" + mossStage);
		}
		
		// - Stone -
		Genesis.proxy.registerBlock(granite, "granite");
		Genesis.proxy.registerBlock(mossy_granite, "mossy_granite");
		Genesis.proxy.registerBlock(rhyolite, "rhyolite");
		Genesis.proxy.registerBlock(dolerite, "dolerite");
		Genesis.proxy.registerBlock(komatiite, "komatiite");
		Genesis.proxy.registerBlock(anorthosite, "anorthosite");
		Genesis.proxy.registerBlock(trondhjemite, "trondhjemite");
		Genesis.proxy.registerBlock(faux_amphibolite, "faux_amphibolite");
		Genesis.proxy.registerBlock(gneiss, "gneiss");
		Genesis.proxy.registerBlock(limestone, "limestone");
		Genesis.proxy.registerBlock(octaedrite, "octaedrite");
		
		// - Soft -
		Genesis.proxy.registerBlock(red_clay, "red_clay");
		Genesis.proxy.registerBlock(ooze, "ooze");
		Genesis.proxy.registerBlock(peat, "peat");
		silt.registerAll();
		
		// - Permafrost -
		Genesis.proxy.registerBlock(permafrost, "permafrost");
		Genesis.proxy.registerBlock(ancient_permafrost, "ancient_permafrost");
		
		// - Ores -
		ores.registerVariants(OreBlocks.ORE);
		
		// - Full Block Woody -
		trees.registerVariants(TreeBlocksAndItems.LOG);
		trees.registerVariants(TreeBlocksAndItems.BRANCH);

		Genesis.proxy.registerBlock(calamites_bundle, "calamites_bundle");
		Genesis.proxy.registerBlock(calamites_roof, "calamites_roof");
		Genesis.proxy.registerBlock(programinis_bundle, "programinis_bundle");
		Genesis.proxy.registerBlock(programinis_roof, "programinis_roof");
		
		Genesis.proxy.registerBlock(prototaxites_mycelium, "prototaxites_mycelium");
		
		// - Dungs -
		dungs.registerVariants(DungBlocksAndItems.DUNG_BLOCK);
		
		Genesis.proxy.registerBlock(dung_brick_block, "dung_brick_block");
		dung_brick_block.setHarvestLevel("pickaxe", 0);
		Blocks.fire.setFireInfo(dung_brick_block, 5, 5);
		
		Genesis.proxy.registerBlock(wattle_and_daub, "wattle_and_daub");
		wattle_and_daub.setHarvestLevel("axe", 0);
		wattle_and_daub.setHardness(3);
		
		// --- Mechanisms ---
		Genesis.proxy.registerBlockWithItem(trap_floor, "trap_floor", new ItemColored(trap_floor, false));
		Genesis.proxy.registerModel(trap_floor, 0, "trap_floor");
		
		// --- Decorative ---
		menhirs.registerAll();
		GameRegistry.registerTileEntity(TileEntityMenhirGlyph.class, Constants.ASSETS_PREFIX + "menhir_glyph");
		GameRegistry.registerTileEntity(TileEntityMenhirReceptacle.class, Constants.ASSETS_PREFIX + "menhir_receptacle");
		
		Genesis.proxy.registerBlock(portal, "portal", false);
		Genesis.proxy.callSided(new SidedFunction()
		{
			@SideOnly(Side.CLIENT)
			@Override
			public void client(GenesisClient client)
			{
				client.registerModelStateMap(portal, new FlexibleStateMap().setPrefix("portal/portal", ""));
			}
		});
		Genesis.proxy.registerModel(portal, 0, "portal/portal");
		GameRegistry.registerTileEntity(TileEntityGenesisPortal.class, Constants.ASSETS_PREFIX + "portal");
		Genesis.proxy.callSided(new SidedFunction()
		{
				@SideOnly(Side.CLIENT)
				@Override
				public void client(GenesisClient client)
				{
					client.registerTileEntityRenderer(TileEntityGenesisPortal.class, new TileEntityGenesisPortalRenderer());
				}
		});
		
		trees.registerAll();
		//trees.getBlock(TreeBlocksAndItems.LEAVES, EnumTree.ARAUCARIOXYLON).setRareDrop(new ItemStack(GenesisItems.materials.getStack(EnumMaterial.ARAUCARIOXYLON_CONE)), .015);
		// The above code does not work, all leaves contained by the block instance containing araucarioxylon leaves will drop cones.
		
		debris.registerAll();
		
		Genesis.proxy.registerBlock(roots, "roots");
		
		// - Containers -
		// Workbench
		Genesis.proxy.registerBlock(workbench, "workbench");
		GameRegistry.registerTileEntity(TileEntityKnapper.class, Constants.ASSETS_PREFIX + "workbench");
		GenesisEntityData.registerProperty(EntityPlayer.class, TileEntityKnapper.KNAPPING_TIME);
		
		// Campfire
		Genesis.proxy.registerBlock(campfire, "campfire");
		Item.getItemFromBlock(campfire).setMaxStackSize(1);
		GameRegistry.registerTileEntity(TileEntityCampfire.class, Constants.ASSETS_PREFIX + "campfire");
		Genesis.proxy.callSided(new SidedFunction()
		{
				@SideOnly(Side.CLIENT)
				@Override
				public void client(GenesisClient client)
				{
						client.registerTileEntityRenderer(TileEntityCampfire.class, new TileEntityCampfireRenderer(campfire));
				}
		});
		
		// Storage boxes
		Genesis.proxy.registerBlock(storage_box, "storage_box");
		GameRegistry.registerTileEntity(TileEntityStorageBox.class, Constants.ASSETS_PREFIX + "storage_box");
		Genesis.proxy.callSided(new SidedFunction()
		{
				@SideOnly(Side.CLIENT)
				@Override
				public void client(GenesisClient client)
				{
						client.registerTileEntityRenderer(TileEntityStorageBox.class, new TileEntityStorageBoxRenderer(storage_box));
				}
		});
		
		Genesis.proxy.registerBlock(rotten_storage_box, "rotten_storage_box");
		GameRegistry.registerTileEntity(TileEntityRottenStorageBox.class, Constants.ASSETS_PREFIX + "rotten_storage_box");
		
		// - Torches -
		Genesis.proxy.registerBlock(calamites_torch, "calamites_torch");
		Genesis.proxy.registerBlock(calamites_torch_tall, "calamites_torch_tall");
		
		// - Plants -
		plants.setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
		plants.registerVariants(PlantBlocks.PLANT);
		plants.registerVariants(PlantBlocks.DOUBLE_PLANT);
		
		// Calamites
		Genesis.proxy.registerBlock(calamites, "calamites");
		calamites.setDrops(new BlockDrops(calamites, 1));
		calamites.setCropDrops(new BlockDrops(calamites, 1));
		
		// Ferns
		plants.registerVariants(PlantBlocks.FERN);
		plants.registerVariants(PlantBlocks.DOUBLE_FERN);
		
		// - Growing Plants -
		
		// Sphenophyllum
		Genesis.proxy.registerBlock(sphenophyllum, "sphenophyllum");
		
		// Odontopteris
		Genesis.proxy.registerBlock(odontopteris, "odontopteris", null);
		ItemStack drop = GenesisItems.seeds.getStack(EnumSeeds.ZINGIBEROPSIS_RHIZOME);
		odontopteris.setDrops(new BlockDrops(drop, 1, 1));
		odontopteris.setCropDrops(new BlockDrops(drop, 1, 3));
		odontopteris.setPickedStack(drop);
		
		// Cladophlebis
		Genesis.proxy.registerBlockWithItem(cladophlebis, "cladophlebis", new ItemColored(cladophlebis, false));
		Genesis.proxy.registerModel(cladophlebis, 0, "cladophlebis");
		
		// Programinis
		Genesis.proxy.registerBlock(programinis, "programinis", null);
		drop = GenesisItems.seeds.getStack(EnumSeeds.PROGRAMINIS_SEEDS);
		programinis.setDrops(new BlockDrops(drop, 1, 1));
		programinis.setCropDrops(
				new BlockDrops(
					new BlockStackDrop(drop, 0, 3),
					new BlockStackDrop(GenesisItems.materials.getStack(EnumMaterial.PROGRAMINIS), 1)));
		programinis.setPickedStack(drop);
		
		// Zingiberopsis
		Genesis.proxy.registerBlock(zingiberopsis, "zingiberopsis", null);
		drop = GenesisItems.seeds.getStack(EnumSeeds.ZINGIBEROPSIS_RHIZOME);
		zingiberopsis.setDrops(new BlockDrops(drop, 1, 1));
		zingiberopsis.setCropDrops(new BlockDrops(drop, 1, 3));
		zingiberopsis.setPickedStack(drop);
		
		// Ankyropteris
		Genesis.proxy.registerBlockWithItem(ankyropteris, "ankyropteris", new ItemColored(ankyropteris, false));
		Genesis.proxy.registerModel(ankyropteris, 0, "ankyropteris");
		
		// Flower pot
		Genesis.proxy.registerBlock(flower_pot, "flower_pot", null);
		GameRegistry.registerTileEntity(TileEntityGenesisFlowerPot.class, Constants.ASSETS_PREFIX + "flower_pot");
		
		// - Mushrooms -
		Genesis.proxy.registerBlock(palaeoagaracites, "palaeoagaracites");
		Genesis.proxy.registerBlock(archaeomarasmius, "archaeomarasmius");
		Genesis.proxy.registerBlock(prototaxites, "prototaxites");
		
		// - Water Plants -
		// Cobbania
		Genesis.proxy.registerBlock(cobbania, "cobbania", ItemBlockCobbania.class);
		
		// Aquatic plants
		aquatic_plants.setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
		aquatic_plants.registerAll();
		
		// Corals
		corals.setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
		corals.registerAll();
		
		// --- Liquids ---
		Genesis.proxy.registerFluidBlock(komatiitic_lava, "komatiitic_lava");
		
		IFlowerPotPlant plantCustoms = new IFlowerPotPlant()
		{
			@Override
			public int getColorMultiplier(ItemStack contents, IBlockAccess world, BlockPos pos)
			{
				EnumPlant variant = plants.getVariant(contents);
				return variant.getColorMultiplier(world, pos);
			}
		};
		
		flower_pot.registerPlantsForPot(plants, PlantBlocks.PLANT, plantCustoms);
		flower_pot.registerPlantsForPot(plants, PlantBlocks.FERN, plantCustoms);
		flower_pot.registerPlantsForPot(trees, TreeBlocksAndItems.SAPLING, null);
		
		flower_pot.registerPlantForPot(new ItemStack(archaeomarasmius), "archaeomarasmius");
		flower_pot.afterAllRegistered();
	}
}
