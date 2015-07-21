package genesis.common;

import genesis.block.BlockAncientPermafrost;
import genesis.block.BlockAquaticPlant;
import genesis.block.BlockCalamites;
import genesis.block.BlockCalamitesBundle;
import genesis.block.BlockCalamitesTorch;
import genesis.block.BlockCobbania;
import genesis.block.BlockFern;
import genesis.block.BlockGenesisFlowerPot;
import genesis.block.BlockGenesisMushroom;
import genesis.block.BlockGenesisOre;
import genesis.block.BlockGenesisRock;
import genesis.block.BlockGenesisVariants;
import genesis.block.BlockGrowingPlant;
import genesis.block.BlockKomatiiticLava;
import genesis.block.BlockMoss;
import genesis.block.BlockOdontopterisCustoms;
import genesis.block.BlockOoze;
import genesis.block.BlockPeat;
import genesis.block.BlockPermafrost;
import genesis.block.BlockPlant;
import genesis.block.BlockPrograminisBundle;
import genesis.block.BlockPrototaxites;
import genesis.block.BlockPrototaxitesMycelium;
import genesis.block.BlockRedClay;
import genesis.block.BlockSphenophyllumCustoms;
import genesis.block.tileentity.BlockCampfire;
import genesis.block.tileentity.TileEntityCampfire;
import genesis.block.tileentity.render.TileEntityCampfireRenderer;
import genesis.client.GenesisClient;
import genesis.client.GenesisSounds;
import genesis.item.ItemBlockCobbania;
import genesis.item.ItemBlockColored;
import genesis.item.ItemBlockMulti;
import genesis.metadata.DungBlocksAndItems;
import genesis.metadata.EnumAquaticPlant;
import genesis.metadata.EnumCoral;
import genesis.metadata.EnumFern;
import genesis.metadata.EnumNodule;
import genesis.metadata.EnumPlant;
import genesis.metadata.IMetadata;
import genesis.metadata.TreeBlocksAndItems;
import genesis.metadata.VariantsCombo;
import genesis.metadata.VariantsOfTypesCombo.ObjectNamePosition;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants.Unlocalized;
import genesis.util.RandomDrop;
import genesis.util.RandomDrop.RandomStackDrop;
import genesis.util.SidedFunction;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class GenesisBlocks
{
	public static final Block moss = new BlockMoss().setUnlocalizedName(Unlocalized.PREFIX + "moss");

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
	public static final Block quartzite = new BlockGenesisRock(1.95F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "quartzite");
	public static final Block limestone = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName(Unlocalized.ROCK + "limestone");
	public static final Block octaedrite = new BlockGenesisRock(1.0F, 10.0F).setUnlocalizedName(Unlocalized.ROCK + "octaedrite");
	public static final Block red_clay = new BlockRedClay().setUnlocalizedName(Unlocalized.PREFIX + "redClay");
	public static final Block ooze = new BlockOoze().setUnlocalizedName(Unlocalized.PREFIX + "ooze");
	public static final Block peat = new BlockPeat().setUnlocalizedName(Unlocalized.PREFIX + "peat");

	public static final Block permafrost = new BlockPermafrost().setUnlocalizedName(Unlocalized.PREFIX + "permafrost");
	public static final Block ancient_permafrost = new BlockAncientPermafrost().setUnlocalizedName(Unlocalized.PREFIX + "ancientPermafrost");

	/* Granite Ores */
	public static final Block quartz_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(new RandomDrop(GenesisItems.quartz)).setUnlocalizedName(Unlocalized.ORE + "quartz");
	public static final Block zircon_ore = new BlockGenesisOre(4.2F, 5.0F, 2, 1).setDrop(new RandomDrop(GenesisItems.zircon)).setUnlocalizedName(Unlocalized.ORE + "zircon");
	public static final Block garnet_ore = new BlockGenesisOre(4.2F, 5.0F, 2, 1).setDrop(new RandomDrop(GenesisItems.garnet)).setUnlocalizedName(Unlocalized.ORE + "garnet");
	public static final Block hematite_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(new RandomDrop(GenesisItems.hematite)).setUnlocalizedName(Unlocalized.ORE + "hematite");
	public static final Block manganese_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(new RandomDrop(GenesisItems.manganese)).setUnlocalizedName(Unlocalized.ORE + "manganese");
	public static final Block malachite_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 2, 1).setDrop(new RandomDrop(GenesisItems.malachite)).setUnlocalizedName(Unlocalized.ORE + "malachite");
	public static final Block olivine_ore = new BlockGenesisOre(4.2F, 5.0F, 3, 5, 1).setDrop(new RandomDrop(GenesisItems.olivine)).setUnlocalizedName(Unlocalized.ORE + "olivine");

	/* Limestone Ores */
	public static final Block flint_ore = new BlockGenesisOre(1.5F, 4.35F, 1, 0).setDrop(new RandomDrop(new RandomStackDrop(GenesisItems.nodules.getStack(EnumNodule.BROWN_FLINT)), new RandomStackDrop(GenesisItems.nodules.getStack(EnumNodule.BLACK_FLINT)))).setUnlocalizedName(Unlocalized.ORE + "flint");
	public static final Block marcasite_ore = new BlockGenesisOre(1.5F, 4.35F, 1, 0).setDrop(new RandomDrop(GenesisItems.nodules.getStack(EnumNodule.MARCASITE))).setUnlocalizedName(Unlocalized.ORE + "marcasite");
	
	/* Trees */
	public static final TreeBlocksAndItems trees = new TreeBlocksAndItems();
	
	/* Crafting */
	public static final Block campfire = new BlockCampfire().setUnlocalizedName(Unlocalized.PREFIX + "campfire");
	
	/* Plants */
	public static final VariantsCombo<EnumPlant, BlockPlant, ItemBlockMulti> plants = new VariantsCombo(new ObjectType("plant", BlockPlant.class, null).setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE), EnumPlant.values());
	public static final BlockCalamites calamites = (BlockCalamites) new BlockCalamites(true, 15, 10)
			.setGrowthChanceMult(6, 1, 1)
			.setUnlocalizedName(Unlocalized.PLANT + "calamites");
	public static final VariantsCombo<EnumFern, BlockFern, ItemBlockMulti> ferns = new VariantsCombo(new ObjectType("fern", BlockFern.class, null).setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE), EnumFern.values());
	public static final Block cobbania = new BlockCobbania().setUnlocalizedName(Unlocalized.PREFIX + "cobbania");
	public static final VariantsCombo<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti> aquatic_plants = new VariantsCombo(new ObjectType("aquatic_plant", "aquaticPlant", BlockAquaticPlant.class, null).setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE), EnumAquaticPlant.values());
	
	/* Crops */
	public static final BlockGrowingPlant zingiberopsis = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true).setBreakAllTogether(true)
			.setPlantType(EnumPlantType.Crop)
			.setUnlocalizedName(Unlocalized.CROP + "zingiberopsis");
	public static final BlockGrowingPlant sphenophyllum = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true)
			.setPlantType(EnumPlantType.Plains)
			.setGrowthChanceMult(5, 1, 1)
			.setCustomsInterface(new BlockSphenophyllumCustoms())
			.setUnlocalizedName(Unlocalized.PLANT + "sphenophyllum");
	public static final BlockGrowingPlant odontopteris = (BlockGrowingPlant) new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true).setBreakAllTogether(true)
			.setPlantType(EnumPlantType.Crop)
			.setGrowthChanceMult(16, 0.4F, 0.95F)
			.setUseBiomeColor(true)
			.setCustomsInterface(new BlockOdontopterisCustoms())
			.setUnlocalizedName(Unlocalized.CROP + "odontopteris");
	public static final BlockGrowingPlant programinis = (BlockGrowingPlant) new BlockGrowingPlant(false, 7, 1).setTopPosition(1)
			.setPlantType(EnumPlantType.Crop)
			.setUseBiomeColor(true)
			.setUnlocalizedName(Unlocalized.CROP + "odontopteris");
	
	/* Fluids */
	public static BlockKomatiiticLava komatiitic_lava;
	
	/* Other Decorative */
	public static final BlockGenesisFlowerPot flower_pot = (BlockGenesisFlowerPot) new BlockGenesisFlowerPot().setUnlocalizedName(Unlocalized.PREFIX + "flowerPot");;
	public static final Block calamites_bundle = new BlockCalamitesBundle().setUnlocalizedName(Unlocalized.PREFIX + "calamitesBundle");
	public static final Block programinis_bundle = new BlockPrograminisBundle().setUnlocalizedName(Unlocalized.PREFIX + "programinisBundle");
	public static final Block calamites_torch = new BlockCalamitesTorch().setUnlocalizedName(Unlocalized.PREFIX + "calamitesTorch");
	public static final Block archaeomarasmius = new BlockGenesisMushroom().setUnlocalizedName(Unlocalized.PREFIX + "archaeomarasmius").setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	public static final Block prototaxites_mycelium = new BlockPrototaxitesMycelium().setUnlocalizedName(Unlocalized.PREFIX + "prototaxitesMycelium");
	public static final DungBlocksAndItems dungs = new DungBlocksAndItems();
	
	/* Misc */
	public static final Block prototaxites = new BlockPrototaxites().setUnlocalizedName(Unlocalized.PREFIX + "prototaxites");
	public static final VariantsCombo<EnumCoral, BlockGenesisVariants, ItemBlockMulti> corals =
			new VariantsCombo(
				new ObjectType<BlockGenesisVariants, ItemBlockMulti>("coral", BlockGenesisVariants.class, null)
				{
					@Override
					public void afterConstructed(BlockGenesisVariants block, ItemBlockMulti item, List<IMetadata> variants)
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
		Genesis.proxy.registerBlock(quartzite, "quartzite");
		Genesis.proxy.registerBlock(limestone, "limestone");
		Genesis.proxy.registerBlock(octaedrite, "octaedrite");
		Genesis.proxy.registerBlock(red_clay, "red_clay");
		Genesis.proxy.registerBlock(ooze, "ooze");
		Genesis.proxy.registerBlock(peat, "peat");
		Genesis.proxy.registerBlock(permafrost, "permafrost");
		Genesis.proxy.registerBlock(ancient_permafrost, "ancient_permafrost");
		Genesis.proxy.registerBlock(quartz_ore, "quartz_ore");
		Genesis.proxy.registerBlock(zircon_ore, "zircon_ore");
		Genesis.proxy.registerBlock(garnet_ore, "garnet_ore");
		Genesis.proxy.registerBlock(hematite_ore, "hematite_ore");
		Genesis.proxy.registerBlock(manganese_ore, "manganese_ore");
		Genesis.proxy.registerBlock(malachite_ore, "malachite_ore");
		Genesis.proxy.registerBlock(olivine_ore, "olivine_ore");
		Genesis.proxy.registerBlock(flint_ore, "flint_ore");
		Genesis.proxy.registerBlock(marcasite_ore, "marcasite_ore");
		trees.registerVariants(trees.LOG);
		Genesis.proxy.registerBlock(calamites_bundle, "calamites_bundle");
		Genesis.proxy.registerBlock(programinis_bundle, "programinis_bundle");
		Genesis.proxy.registerBlock(archaeomarasmius, "archaeomarasmius");
		Genesis.proxy.registerBlock(prototaxites_mycelium, "prototaxites_mycelium");
		dungs.registerVariants(dungs.DUNG_BLOCK);
		
		// Begin decorative
		trees.registerAll();
		
		Genesis.proxy.registerBlock(campfire, "campfire");
		GameRegistry.registerTileEntity(TileEntityCampfire.class, Unlocalized.PREFIX + "Campfire");
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
		
		plants.registerAll();

		Genesis.proxy.registerBlock(calamites, "calamites", null);
		calamites.setDrops(new RandomDrop(GenesisItems.calamites, 1, 1));
		calamites.setCropDrops(new RandomDrop(GenesisItems.calamites, 1, 1));
		calamites.setPickedItem(GenesisItems.calamites);
		GenesisItems.calamites.setCrop(calamites);
		
		ferns.registerAll();
		
		Genesis.proxy.registerBlock(zingiberopsis, "zingiberopsis", null);
		zingiberopsis.setPlantSize(0, 0.2F, 0.5F);
		zingiberopsis.setDrops(new RandomDrop(GenesisItems.zingiberopsis_rhizome, 1, 1));
		zingiberopsis.setCropDrops(new RandomDrop(GenesisItems.zingiberopsis_rhizome, 1, 3));
		zingiberopsis.setPickedItem(GenesisItems.zingiberopsis_rhizome);
		GenesisItems.zingiberopsis_rhizome.setCrop(zingiberopsis);
		
		Genesis.proxy.registerBlock(sphenophyllum, "sphenophyllum");
		sphenophyllum.setPlantSize(0, 0.2F, 0.75F);
		
		Genesis.proxy.registerBlock(odontopteris, "odontopteris", null);
		odontopteris.setPlantSize(0, 0.2F, 0.75F);
		odontopteris.setDrops(new RandomDrop(GenesisItems.odontopteris_seeds, 1, 1));
		odontopteris.setCropDrops(new RandomDrop(GenesisItems.odontopteris_seeds, 1, 3));
		odontopteris.setPickedItem(GenesisItems.odontopteris_seeds);
		GenesisItems.odontopteris_seeds.setCrop(odontopteris);
		
		Genesis.proxy.registerBlock(programinis, "programinis", null);
		programinis.setDrops(new RandomDrop(GenesisItems.programinis_seeds, 1, 1));
		programinis.setCropDrops(new RandomDrop(GenesisItems.programinis_seeds, 1, 3), new RandomDrop(GenesisItems.programinis, 1, 1));
		programinis.setPickedItem(GenesisItems.programinis_seeds);
		GenesisItems.programinis_seeds.setCrop(programinis);
		
		Genesis.proxy.registerBlock(flower_pot, "genesis_flower_pot");
		flower_pot.registerPlantsForPot(plants);
		flower_pot.registerPlantsForPot(ferns);
		flower_pot.registerPlantsForPot(trees, trees.SAPLING);
		flower_pot.afterAllRegistered();
		
		komatiitic_lava = (BlockKomatiiticLava) new BlockKomatiiticLava(GenesisFluids.KOMATIITIC_LAVA).setUnlocalizedName(Unlocalized.PREFIX + "komatiiticLava");
		Genesis.proxy.registerFluidBlock(komatiitic_lava, "komatiitic_lava");
		
		Genesis.proxy.registerBlock(prototaxites, "prototaxites");
		Genesis.proxy.registerBlock(cobbania, "cobbania", ItemBlockCobbania.class);
		aquatic_plants.registerAll();

		corals.registerAll();
	}
}
