package genesis.common;

import java.util.List;

import genesis.block.*;
import genesis.client.*;
import genesis.item.*;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.block.tileentity.*;
import genesis.block.tileentity.render.TileEntityCampfireRenderer;
import genesis.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class GenesisBlocks
{
	public static final Block moss = new BlockMoss().setUnlocalizedName(Constants.PREFIX + "moss");

	/* Rocks */
	public static final BlockGenesisRock granite = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName("granite");
	public static final BlockGenesisRock mossy_granite = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName("mossyGranite");
	public static final BlockGenesisRock rhyolite = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName("rhyolite");
	public static final BlockGenesisRock dolerite = new BlockGenesisRock(1.2F, 10.0F).setUnlocalizedName("dolerite");
	public static final BlockGenesisRock komatiite = new BlockGenesisRock(1.95F, 10.0F).setUnlocalizedName("komatiite");
	public static final BlockGenesisRock anorthosite = new BlockGenesisRock(1.2F, 10.0F).setUnlocalizedName("anorthosite");
	public static final BlockGenesisRock trondhjemite = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName("trondhjemite");
	public static final BlockGenesisRock faux_amphibolite = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName("fauxAmphibolite");
	public static final BlockGenesisRock gneiss = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName("gneiss");
	public static final BlockGenesisRock quartzite = new BlockGenesisRock(1.95F, 10.0F).setUnlocalizedName("quartzite");
	public static final BlockGenesisRock limestone = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName("limestone");
	public static final BlockGenesisRock shale = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName("shale");
	public static final BlockGenesisRock octaedrite = new BlockGenesisRock(1.0F, 10.0F).setUnlocalizedName("octaedrite");
	public static final BlockRedClay red_clay = new BlockRedClay().setUnlocalizedName("redClay");
	public static final BlockOoze ooze = new BlockOoze().setUnlocalizedName("ooze");
	public static final BlockPeat peat = new BlockPeat().setUnlocalizedName("peat");

	public static final BlockGenesis permafrost = new BlockPermafrost().setUnlocalizedName("permafrost");
	public static final BlockGenesis new_permafrost = new BlockNewPermafrost().setUnlocalizedName("permafrost");

	/* Granite Ores */
	public static final BlockGenesisOre quartz_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(GenesisItems.quartz).setUnlocalizedName("quartz");
	public static final BlockGenesisOre zircon_ore = new BlockGenesisOre(4.2F, 5.0F, 2, 1).setDrop(GenesisItems.zircon).setUnlocalizedName("zircon");
	public static final BlockGenesisOre garnet_ore = new BlockGenesisOre(4.2F, 5.0F, 2, 1).setDrop(GenesisItems.garnet).setUnlocalizedName("garnet");
	public static final BlockGenesisOre hematite_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(GenesisItems.hematite).setUnlocalizedName("hematite");
	public static final BlockGenesisOre manganese_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(GenesisItems.manganese).setUnlocalizedName("manganese");
	public static final BlockGenesisOre malachite_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 2, 1).setDrop(GenesisItems.malachite).setUnlocalizedName("malachite");
	public static final BlockGenesisOre olivine_ore = new BlockGenesisOre(4.2F, 5.0F, 3, 5, 1).setDrop(GenesisItems.olivine).setUnlocalizedName("olivine");

	/* Limestone Ores */
	public static final BlockGenesisOre brown_flint_ore = new BlockGenesisOre(1.5F, 4.35F, 1, 0).setDrop(GenesisItems.nodules.getStack(EnumNodule.BROWN_FLINT)).setUnlocalizedName("brownFlint");
	public static final BlockGenesisOre marcasite_ore = new BlockGenesisOre(1.5F, 4.35F, 1, 0).setDrop(GenesisItems.nodules.getStack(EnumNodule.MARCASITE)).setUnlocalizedName("marcasite");
	
	/* Trees */
	public static final TreeBlocksAndItems trees = new TreeBlocksAndItems();
	
	/* Crafting */
	public static final BlockCampfire campfire = new BlockCampfire().setUnlocalizedName("campfire");
	
	/* Plants */
	public static final VariantsCombo<EnumPlant, BlockPlant, ItemBlockMulti> plants = new VariantsCombo(new ObjectType("plant", BlockPlant.class, null).setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE), EnumPlant.values());
	public static final BlockGrowingPlant calamites = new BlockCalamites(true, 15, 10)
			.setGrowthChanceMult(6, 1, 1)
			.setUnlocalizedName("plant.calamites");
	public static final VariantsCombo<EnumFern, BlockFern, ItemBlockMulti> ferns = new VariantsCombo(new ObjectType("fern", BlockFern.class, null).setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE), EnumFern.values());
	public static final BlockCobbania cobbania = new BlockCobbania();//.setUnlocalizedName("cobbania");
	public static final VariantsCombo<EnumAquaticPlant, BlockAquaticPlant, ItemBlockMulti> aquatic_plants = new VariantsCombo(new ObjectType("aquatic_plant", "aquaticPlant", BlockAquaticPlant.class, null).setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.NONE), EnumAquaticPlant.values());
	
	/* Crops */
	public static final BlockGrowingPlant zingiberopsis = new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true).setBreakAllTogether(true)
			.setPlantType(EnumPlantType.Crop)
			.setUnlocalizedName("crop.zingiberopsis");
	public static final BlockGrowingPlant sphenophyllum = new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true)
			.setPlantType(EnumPlantType.Plains)
			.setGrowthChanceMult(5, 1, 1)
			.setCustomsInterface(new BlockSphenophyllumCustoms())
			.setUnlocalizedName("plant.sphenophyllum");
	public static final BlockGrowingPlant odontopteris = new BlockGrowingPlant(true, 7, 5, 2).setTopPosition(2)
			.setGrowAllTogether(true).setBreakAllTogether(true)
			.setPlantType(EnumPlantType.Crop)
			.setGrowthChanceMult(16, 0.4F, 0.95F)
			.setUseBiomeColor(true)
			.setCustomsInterface(new BlockOdontopterisCustoms())
			.setUnlocalizedName("crop.odontopteris");
	public static final BlockGrowingPlant programinis = new BlockGrowingPlant(false, 7, 1).setTopPosition(1)
			.setPlantType(EnumPlantType.Crop)
			.setUseBiomeColor(true)
			.setUnlocalizedName("crop.odontopteris");
	
	/* Fluids */
	
	public static BlockKomatiiticLava komatiitic_lava;
	
	/* Other Decorative */
	public static final BlockGenesisFlowerPot flower_pot = new BlockGenesisFlowerPot();
	public static final BlockCalamitesBundle calamites_bundle = new BlockCalamitesBundle().setUnlocalizedName("calamitesBundle");
	public static final BlockGenesisTorch calamites_torch = new BlockGenesisTorch().setUnlocalizedName("calamitesTorch");
	public static final BlockPrototaxitesMycelium prototaxites_mycelium = new BlockPrototaxitesMycelium().setUnlocalizedName("prototaxitesMycelium");
	public static final DungBlocksAndItems dungs = new DungBlocksAndItems();
	
	/* Misc */
	public static final BlockGenesis prototaxites = new BlockPrototaxites().setUnlocalizedName("prototaxites");
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
		Genesis.proxy.registerBlock(shale, "shale");
		Genesis.proxy.registerBlock(octaedrite, "octaedrite");
		Genesis.proxy.registerBlock(red_clay, "red_clay");
		Genesis.proxy.registerBlock(ooze, "ooze");
		Genesis.proxy.registerBlock(peat, "peat");
		Genesis.proxy.registerBlock(permafrost, "permafrost");
		Genesis.proxy.registerBlock(new_permafrost, "new_permafrost");
		Genesis.proxy.registerBlock(quartz_ore, "quartz_ore");
		Genesis.proxy.registerBlock(zircon_ore, "zircon_ore");
		Genesis.proxy.registerBlock(garnet_ore, "garnet_ore");
		Genesis.proxy.registerBlock(hematite_ore, "hematite_ore");
		Genesis.proxy.registerBlock(manganese_ore, "manganese_ore");
		Genesis.proxy.registerBlock(malachite_ore, "malachite_ore");
		Genesis.proxy.registerBlock(olivine_ore, "olivine_ore");
		Genesis.proxy.registerBlock(brown_flint_ore, "brown_flint_ore");
		Genesis.proxy.registerBlock(marcasite_ore, "marcasite_ore");
		trees.registerVariants(trees.LOG);
		Genesis.proxy.registerBlock(calamites_bundle, "calamites_bundle");
		Genesis.proxy.registerBlock(prototaxites_mycelium, "prototaxites_mycelium");
		dungs.registerVariants(dungs.DUNG_BLOCK);
		
		// Begin decorative
		trees.registerAll();
		
		Genesis.proxy.registerBlock(campfire, "campfire");
		GameRegistry.registerTileEntity(TileEntityCampfire.class, Constants.PREFIX + "Campfire");
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
		calamites.setDrops(new RandomItemDrop(GenesisItems.calamites, 1, 1));
		calamites.setCropDrops(new RandomItemDrop(GenesisItems.calamites, 1, 1));
		calamites.setPickedItem(GenesisItems.calamites);
		GenesisItems.calamites.setCrop(calamites);
		
		ferns.registerAll();
		
		Genesis.proxy.registerBlock(zingiberopsis, "zingiberopsis", null);
		zingiberopsis.setPlantSize(0, 0.2F, 0.5F);
		zingiberopsis.setDrops(new RandomItemDrop(GenesisItems.zingiberopsis_rhizome, 1, 1));
		zingiberopsis.setCropDrops(new RandomItemDrop(GenesisItems.zingiberopsis_rhizome, 1, 3));
		zingiberopsis.setPickedItem(GenesisItems.zingiberopsis_rhizome);
		GenesisItems.zingiberopsis_rhizome.setCrop(zingiberopsis);
		
		Genesis.proxy.registerBlock(sphenophyllum, "sphenophyllum");
		sphenophyllum.setPlantSize(0, 0.2F, 0.75F);
		
		Genesis.proxy.registerBlock(odontopteris, "odontopteris", null);
		odontopteris.setPlantSize(0, 0.2F, 0.75F);
		odontopteris.setDrops(new RandomItemDrop(GenesisItems.odontopteris_seeds, 1, 1));
		odontopteris.setCropDrops(new RandomItemDrop(GenesisItems.odontopteris_seeds, 1, 3));
		odontopteris.setPickedItem(GenesisItems.odontopteris_seeds);
		GenesisItems.odontopteris_seeds.setCrop(odontopteris);
		
		Genesis.proxy.registerBlock(programinis, "programinis", null);
		programinis.setDrops(new RandomItemDrop(GenesisItems.programinis_seeds, 1, 1));
		programinis.setCropDrops(new RandomItemDrop(GenesisItems.programinis_seeds, 1, 3), new RandomItemDrop(GenesisItems.programinis, 1, 1));
		programinis.setPickedItem(GenesisItems.programinis_seeds);
		GenesisItems.programinis_seeds.setCrop(programinis);
		
		Genesis.proxy.registerBlock(flower_pot, "genesis_flower_pot");
		flower_pot.registerPlantsForPot(plants);
		flower_pot.registerPlantsForPot(ferns);
		flower_pot.registerPlantsForPot(trees, trees.SAPLING);
		flower_pot.afterAllRegistered();
		
		komatiitic_lava = new BlockKomatiiticLava(GenesisFluids.KOMATIITIC_LAVA).setUnlocalizedName("komatiicLava");
		Genesis.proxy.registerFluidBlock(komatiitic_lava, "komatiitic_lava");
		
		Genesis.proxy.registerBlock(prototaxites, "prototaxites");
		Genesis.proxy.registerBlock(cobbania, "cobbania", ItemBlockCobbania.class);
		aquatic_plants.registerAll();

		corals.registerAll();
	}
}
