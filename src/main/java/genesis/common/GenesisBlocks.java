package genesis.common;

import genesis.common.GenesisEntityData.*;
import genesis.block.*;
import genesis.block.tileentity.*;
import genesis.block.tileentity.render.*;
import genesis.client.*;
import genesis.item.*;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;
import genesis.util.random.drops.*;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;

public final class GenesisBlocks
{
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
	public static final Block geyserite = new BlockGenesisRock(1.35F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "geyserite");
	public static final Block octaedrite = new BlockGenesisRock(1.0F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "octaedrite");
	public static final Block red_clay = new BlockRedClay().setUnlocalizedName(Unlocalized.PREFIX + "redClay");
	public static final Block ooze = new BlockOoze().setUnlocalizedName(Unlocalized.PREFIX + "ooze");
	public static final Block peat = new BlockPeat().setUnlocalizedName(Unlocalized.PREFIX + "peat");
	public static final SiltBlocks silt = new SiltBlocks();
	
	public static final Block permafrost = new BlockPermafrost().setUnlocalizedName(Unlocalized.PREFIX + "permafrost");
	public static final Block ancient_permafrost = new BlockAncientPermafrost().setUnlocalizedName(Unlocalized.PREFIX + "ancientPermafrost");
	
	/* Granite Ores */
	public static final GraniteOreBlocks graniteOres = new GraniteOreBlocks();
	
	/* Limestone Ores */
	public static final Block flint_ore = new BlockGenesisOre(1.5F, 4.35F, 1, 0)
			.setDrops(new BlockDrops(new BlockMultiDrop(new StackDrop(GenesisItems.nodules.getStack(EnumNodule.BROWN_FLINT), 1), new StackDrop(GenesisItems.nodules.getStack(EnumNodule.BLACK_FLINT), 1)))).setUnlocalizedName(Unlocalized.ORE + "flint");
	public static final Block marcasite_ore = new BlockGenesisOre(1.5F, 4.35F, 1, 0).setDrops(new BlockDrops(GenesisItems.nodules.getStack(EnumNodule.MARCASITE), 1)).setUnlocalizedName(Unlocalized.ORE + "marcasite");
	
	/* Trees */
	public static final TreeBlocksAndItems trees = new TreeBlocksAndItems();
	
	/* Crafting */
	public static final BlockKnapper workbench = (BlockKnapper) new BlockKnapper().setUnlocalizedName(Unlocalized.PREFIX + "workbench");
	public static final BlockCampfire campfire = (BlockCampfire) new BlockCampfire().setUnlocalizedName(Unlocalized.PREFIX + "campfire");
	
	/* Plants */
	public static final PlantBlocks plants = new PlantBlocks();
	public static final BlockCalamites calamites = (BlockCalamites) new BlockCalamites(true, 15, 7)
			.setGrowth(6, 1, 1, 1)
			.setUnlocalizedName(Unlocalized.PLANT + "calamites");
	public static final VariantsCombo<EnumFern, BlockFern, ItemBlockMulti> ferns = new VariantsCombo<EnumFern, BlockFern, ItemBlockMulti>(new ObjectType<BlockFern, ItemBlockMulti>("fern", BlockFern.class, null).setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE), EnumFern.values());
	public static final Block cobbania = new BlockCobbania().setUnlocalizedName(Unlocalized.PREFIX + "cobbania");
	public static final VariantsCombo<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti> aquatic_plants = new VariantsCombo<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti>(new ObjectType<BlockAquaticPlant, ItemBlockMulti>("aquatic_plant", "aquaticPlant", BlockAquaticPlant.class, null).setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE), EnumAquaticPlant.values());
	
	/* Crops */
	protected static final SurviveOnDirtCustoms surviveOnDirt = new SurviveOnDirtCustoms();
	public static final BlockGrowingPlant zingiberopsis = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true).setBreakAllTogether(true)
	        .setPlantSize(0, 0.2F, 0.5F)
			.setPlantType(EnumPlantType.Crop)
			.setGrowthOnFarmland(0.75F)
			.setCustoms(surviveOnDirt)
			.setUnlocalizedName(Unlocalized.CROP + "zingiberopsis");
	public static final BlockGrowingPlant sphenophyllum = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true)
			.setPlantType(EnumPlantType.Plains)
			.setGrowth(5, 1, 1, 1)
            .setPlantSize(0, 0.2F, 0.75F)
			.setCustoms(new BlockSphenophyllumCustoms())
			.setUnlocalizedName(Unlocalized.PLANT + "sphenophyllum");
	public static final BlockGrowingPlant odontopteris = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true).setBreakAllTogether(true)
			.setPlantType(EnumPlantType.Crop)
			.setGrowth(0.05F, 1.5F, 2.5F, 1.05F)
			.setUseBiomeColor(true)
            .setPlantSize(0, 0.2F, 0.75F)
			.setCustoms(new BlockOdontopterisCustoms())
			.setUnlocalizedName(Unlocalized.CROP + "odontopteris");
	public static final BlockGrowingPlant programinis = (BlockGrowingPlant) new BlockGrowingPlant(false, 7, 1).setTopPosition(1)
			.setPlantType(EnumPlantType.Crop)
			.setGrowthOnFarmland(0.75F)
			.setUseBiomeColor(true)
	        .setPlantSize(0, 0.1F, 0.75F)
			.setCustoms(surviveOnDirt)
			.setUnlocalizedName(Unlocalized.CROP + "programinis");
	
	/* Fluids */
	public static BlockKomatiiticLava komatiitic_lava;
	
	/* Other Decorative */
	public static final BlockGenesisFlowerPot flower_pot = (BlockGenesisFlowerPot) new BlockGenesisFlowerPot().setUnlocalizedName(Unlocalized.PREFIX + "flowerPot");;
	public static final Block calamites_bundle = new BlockCalamitesBundle().setUnlocalizedName(Unlocalized.PREFIX + "calamitesBundle");
	public static final Block programinis_bundle = new BlockPrograminisBundle().setUnlocalizedName(Unlocalized.PREFIX + "programinisBundle");
	public static final Block calamites_torch = new BlockCalamitesTorch().setUnlocalizedName(Unlocalized.PREFIX + "calamitesTorch");
	public static final Block prototaxites_mycelium = new BlockPrototaxitesMycelium().setUnlocalizedName(Unlocalized.PREFIX + "prototaxitesMycelium");
	public static final DungBlocksAndItems dungs = new DungBlocksAndItems();
	
	/* Misc */
	public static final Block palaeoagaracites = new BlockGenesisMushroom().setUnlocalizedName(Unlocalized.PREFIX + "palaeoagaracites")
			.setGrowType(BlockGenesisMushroom.MushroomGrowType.GROW_SIDE)
			.setBoundsSize(0.3125F, 0.5625F, 0.1875F)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	public static final Block archaeomarasmius = new BlockGenesisMushroom().setUnlocalizedName(Unlocalized.PREFIX + "archaeomarasmius")
			.setBoundsSize(0.375F, 0.75F, 0)
			.setGrowType(BlockGenesisMushroom.MushroomGrowType.GROW_TOP)
			.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	public static final Block prototaxites = new BlockPrototaxites().setUnlocalizedName(Unlocalized.PREFIX + "prototaxites");
	@SuppressWarnings("unchecked")
	public static final VariantsCombo<EnumCoral, BlockGenesisVariants<EnumCoral>, ItemBlockMulti> corals =
			new VariantsCombo<EnumCoral, BlockGenesisVariants<EnumCoral>, ItemBlockMulti>(
				new ObjectType<BlockGenesisVariants<EnumCoral>, ItemBlockMulti>("coral", (Class<BlockGenesisVariants<EnumCoral>>) ((Class<?>) BlockGenesisVariants.class), null)
				{
					@Override
					public void afterConstructed(BlockGenesisVariants<EnumCoral> block, ItemBlockMulti item, List<? extends IMetadata> variants)
					{
						super.afterConstructed(block, item, variants);
						
						block.setHardness(0.75F);
						block.setResistance(8.5F);
						block.setStepSound(GenesisSounds.CORAL);
					}
				}.setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE)
				.setCreativeTab(GenesisCreativeTabs.DECORATIONS)
				.setBlockArguments(Material.coral),
			EnumCoral.values());
	
	public static void registerBlocks()
	{
		// Begin general building blocks
		Genesis.proxy.registerBlock(moss, "moss", ItemBlockColored.class);
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
		Genesis.proxy.registerBlock(geyserite, "geyserite");
		Genesis.proxy.registerBlock(octaedrite, "octaedrite");
		
		Genesis.proxy.registerBlock(red_clay, "red_clay");
		Genesis.proxy.registerBlock(ooze, "ooze");
		Genesis.proxy.registerBlock(peat, "peat");
		silt.registerAll();
		
		Genesis.proxy.registerBlock(permafrost, "permafrost");
		Genesis.proxy.registerBlock(ancient_permafrost, "ancient_permafrost");
		
		// Ores
		graniteOres.registerAll();
		Genesis.proxy.registerBlock(flint_ore, "flint_ore");
		Genesis.proxy.registerBlock(marcasite_ore, "marcasite_ore");
		
		trees.registerVariants(trees.LOG);
		Genesis.proxy.registerBlock(calamites_bundle, "calamites_bundle");
		Genesis.proxy.registerBlock(programinis_bundle, "programinis_bundle");
		Genesis.proxy.registerBlock(prototaxites_mycelium, "prototaxites_mycelium");
		dungs.registerVariants(dungs.DUNG_BLOCK);
		
		// Begin decorative
		trees.registerAll();
		
		Genesis.proxy.registerBlock(workbench, "workbench");
		GameRegistry.registerTileEntity(TileEntityKnapper.class, Constants.ASSETS_PREFIX + "workbench");
		GenesisEntityData.registerProperty(EntityPlayer.class, TileEntityKnapper.KNAPPING_TIME, 0, false);
		
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
		
		Genesis.proxy.registerBlock(calamites_torch, "calamites_torch");
		
		plants.setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
		plants.registerAll();

		Genesis.proxy.registerBlock(calamites, "calamites", null);
		calamites.setDrops(new BlockDrops(GenesisItems.calamites, 1, 1));
		calamites.setCropDrops(new BlockDrops(GenesisItems.calamites, 1, 1));
		calamites.setPickedItem(GenesisItems.calamites);
		GenesisItems.calamites.setCrop(calamites);
		
		ferns.setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
		ferns.registerAll();
		
		Genesis.proxy.registerBlock(zingiberopsis, "zingiberopsis", null);
		zingiberopsis.setDrops(new BlockDrops(GenesisItems.zingiberopsis_rhizome, 1, 1));
		zingiberopsis.setCropDrops(new BlockDrops(GenesisItems.zingiberopsis_rhizome, 1, 3));
		zingiberopsis.setPickedItem(GenesisItems.zingiberopsis_rhizome);
		GenesisItems.zingiberopsis_rhizome.setCrop(zingiberopsis);
		
		Genesis.proxy.registerBlock(sphenophyllum, "sphenophyllum");
		
		Genesis.proxy.registerBlock(odontopteris, "odontopteris", null);
		odontopteris.setDrops(new BlockDrops(GenesisItems.odontopteris_seeds, 1, 1));
		odontopteris.setCropDrops(new BlockDrops(GenesisItems.odontopteris_seeds, 1, 3));
		odontopteris.setPickedItem(GenesisItems.odontopteris_seeds);
		GenesisItems.odontopteris_seeds.setCrop(odontopteris);
		
		Genesis.proxy.registerBlock(programinis, "programinis", null);
		programinis.setDrops(new BlockDrops(GenesisItems.programinis_seeds, 1, 1));
		programinis.setCropDrops(new BlockDrops(new StackDrop(GenesisItems.programinis_seeds, 1, 3), new StackDrop(GenesisItems.programinis, 1, 1)));
		programinis.setPickedItem(GenesisItems.programinis_seeds);
		GenesisItems.programinis_seeds.setCrop(programinis);
		
		Genesis.proxy.registerBlock(flower_pot, "genesis_flower_pot");
		flower_pot.registerPlantsForPot(plants, PlantBlocks.PLANT);
		flower_pot.registerPlantsForPot(ferns);
		flower_pot.registerPlantsForPot(trees, trees.SAPLING);
		flower_pot.afterAllRegistered();
		
		komatiitic_lava = (BlockKomatiiticLava) new BlockKomatiiticLava(GenesisFluids.KOMATIITIC_LAVA).setUnlocalizedName(Unlocalized.PREFIX + "komatiiticLava");
		Genesis.proxy.registerFluidBlock(komatiitic_lava, "komatiitic_lava");
		
		Genesis.proxy.registerBlock(palaeoagaracites, "palaeoagaracites");
		Genesis.proxy.registerBlock(archaeomarasmius, "archaeomarasmius");
		Genesis.proxy.registerBlock(prototaxites, "prototaxites");
		Genesis.proxy.registerBlock(cobbania, "cobbania", ItemBlockCobbania.class);
		aquatic_plants.setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
		aquatic_plants.registerAll();
		
		corals.setUnlocalizedPrefix(Constants.Unlocalized.PREFIX);
		corals.registerAll();
	}
}
