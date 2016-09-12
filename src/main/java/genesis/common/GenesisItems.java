package genesis.common;

import genesis.client.Colorizers;
import genesis.client.model.MetadataModelDefinition;
import genesis.combo.*;
import genesis.combo.variant.ArrowTypes.ArrowType;
import genesis.combo.variant.*;
import genesis.entity.fixed.EntityMeganeuraEgg;
import genesis.item.*;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class GenesisItems
{
	/* Materials */
	public static final Item RED_CLAY_BALL = new ItemGenesis().setUnlocalizedName(Unlocalized.MATERIAL + "redClay");
	public static final Item RED_CLAY_BOWL = new ItemGenesis().setUnlocalizedName(Unlocalized.MATERIAL + "redClayBowl");

	public static final ItemsCeramicBowls BOWLS = new ItemsCeramicBowls();

	public static final Item RED_CLAY_BUCKET = new ItemGenesis().setUnlocalizedName(Unlocalized.MATERIAL + "redClayBucket");

	public static final VariantsCombo<EnumNodule, Block, ItemMulti<EnumNodule>> NODULES =
			VariantsCombo.create(
					"nodules",
					ObjectType.createItem(EnumNodule.class, "nodule"),
					EnumNodule.class, EnumNodule.values());
	public static final VariantsCombo<EnumPowder, Block, ItemMulti<EnumPowder>> POWDERS =
			VariantsCombo.create(
					"powders",
					ObjectType.createItem(EnumPowder.class, "powder"),
					EnumPowder.class, EnumPowder.values());

	public static final VariantsCombo<EnumMaterial, Block, ItemMaterial> MATERIALS =
			VariantsCombo.create(
					"materials",
					ObjectType.createItem(EnumMaterial.class, "material", ItemMaterial.class).setResourceName(""),
					EnumMaterial.class, EnumMaterial.values());

	/* Eggs */
	public static final ItemGenesisEgg<EntityMeganeuraEgg> MEGANEURA_EGG = new ItemGenesisEgg<>(EntityMeganeuraEgg.class)
			.setUnlocalizedName(Unlocalized.EGG + "meganeura");

	/* Seeds */
	public static final VariantsCombo<EnumSeeds, Block, ItemGenesisSeeds> SEEDS =
			VariantsCombo.create(
					"seeds",
					ObjectType.createItem(EnumSeeds.class, "seed", ItemGenesisSeeds.class).setResourceName(""),
					EnumSeeds.class, EnumSeeds.values());
	public static final Item ROTTEN_ZINGIBEROPSIS_RHIZOME = new ItemFood(1, 0.7F, false)
			.setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 0.6F)
			.setUnlocalizedName(Unlocalized.FOOD + "rottenZingiberopsisRhizome")
			.setCreativeTab(GenesisCreativeTabs.FOOD);

	/* Foods */
	public static final FoodItems FOODS = new FoodItems();

	/* Tools */
	public static final ItemFlintAndMarcasite FLINT_AND_MARCASITE = (ItemFlintAndMarcasite) new ItemFlintAndMarcasite().setUnlocalizedName(Unlocalized.PREFIX + Unlocalized.Section.TOOL + "flintAndMarcasite");
	public static final ToolItems TOOLS = new ToolItems();

	/* Armor */
	public static final ClothingItems CLOTHING = new ClothingItems();

	/* Misc */
	public static final Item CERAMIC_BUCKET = new ItemGenesisBucket(Blocks.AIR.getDefaultState()).setUnlocalizedName(Unlocalized.MISC + "ceramicBucket");
	public static final Item CERAMIC_BUCKET_WATER = new ItemGenesisBucket(Blocks.FLOWING_WATER.getDefaultState()).setUnlocalizedName(Unlocalized.MISC + "ceramicBucketWater").setContainerItem(CERAMIC_BUCKET);
	public static final Item CERAMIC_BUCKET_MILK = new ItemGenesisBucketMilk().setUnlocalizedName(Unlocalized.MISC + "ceramicBucketMilk").setContainerItem(CERAMIC_BUCKET);
	public static Item bucket_komatiitic_lava;

	public static final VariantsCombo<EnumMenhirActivator, Block, ItemMulti<EnumMenhirActivator>> MENHIR_ACTIVATORS =
					new VariantsCombo<>(
									"menhir_activators",
									new ObjectType<EnumMenhirActivator, Block, ItemMulti<EnumMenhirActivator>>(EnumMenhirActivator.class, "menhir_activator", Unlocalized.MISC + "menhirActivator", null, null)
													.setCreativeTab(GenesisCreativeTabs.MISC)
													.setResourceName(""),
									EnumMenhirActivator.class, EnumMenhirActivator.values())
			.setNames(Constants.MOD_ID, "");

	public static final BowItems BOWS = new BowItems();
	public static final ArrowItems ARROWS = new ArrowItems();

	private static ResourceLocation name(String path)
	{
		return new ResourceLocation(Constants.MOD_ID, path);
	}

	public static void preInitCommon()
	{
		// --- Materials ---
		// Tool materials
		TOOLS.registerVariants(ToolItems.PEBBLE);
		TOOLS.registerVariants(ToolItems.FLAKE);

		// Clay
		Genesis.proxy.registerItem(RED_CLAY_BALL, name("red_clay_ball"));
		Genesis.proxy.registerItem(RED_CLAY_BOWL, name("red_clay_bowl"));
		BOWLS.registerAll();
		Genesis.proxy.registerItem(RED_CLAY_BUCKET, name("red_clay_bucket"));

		// Ores
		GenesisBlocks.ORES.registerVariants(OreBlocks.DROP);

		POWDERS.setNames(Constants.MOD_ID, Unlocalized.MATERIAL);
		POWDERS.registerAll();

		NODULES.setNames(Constants.MOD_ID, Unlocalized.MATERIAL);
		NODULES.registerAll();

		// Billets
		GenesisBlocks.TREES.registerVariants(TreeBlocksAndItems.BILLET);

		// Random materials
		GenesisBlocks.DUNGS.registerVariants(DungBlocksAndItems.DUNG);

		MATERIALS.setNames(Constants.MOD_ID, Unlocalized.PREFIX);
		MATERIALS.registerAll();

		Genesis.proxy.registerItem(MEGANEURA_EGG, name("meganeura_egg"));

		SEEDS.setNames(Constants.MOD_ID, Unlocalized.PREFIX);
		SEEDS.registerAll();
		Genesis.proxy.registerItem(ROTTEN_ZINGIBEROPSIS_RHIZOME, name("rotten_zingiberopsis_rhizome"));

		// Foods
		FOODS.registerAll();

		// --- Begin tools ---
		Genesis.proxy.registerItem(FLINT_AND_MARCASITE, name("flint_and_marcasite"));

		// All tools
		TOOLS.registerAll();

		// Armor
		CLOTHING.registerAll();

		// --- Misc ---
		Genesis.proxy.registerItem(CERAMIC_BUCKET, name("ceramic_bucket"));
		Genesis.proxy.registerItem(CERAMIC_BUCKET_WATER, name("ceramic_bucket_water"));
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluid("water"),
				new ItemStack(CERAMIC_BUCKET_WATER), new ItemStack(CERAMIC_BUCKET));
		Genesis.proxy.registerItem(CERAMIC_BUCKET_MILK, name("ceramic_bucket_milk"));

		bucket_komatiitic_lava = new ItemGenesisBucket(GenesisBlocks.KOMATIITIC_LAVA.getDefaultState()).setUnlocalizedName(Unlocalized.MISC + "bucketKomatiiticLava");
		Genesis.proxy.registerItem(bucket_komatiitic_lava, name("bucket_komatiitic_lava"));

		MENHIR_ACTIVATORS.registerAll();

		BOWS.registerAll();
		ARROWS.registerAll();
	}

	@SideOnly(Side.CLIENT)
	public static void preInitClient()
	{
		for (Item bow : BOWS.getItems())
			Genesis.proxy.registerModel(bow, MetadataModelDefinition.from(bow, (s) -> name(BOWS.getVariant(s).getName())));

		for (Item arrow : ARROWS.getItems())
		{
			Genesis.proxy.registerModel(arrow,
					MetadataModelDefinition.from(arrow,
							(stack) -> {
								ArrowType variant = ARROWS.getVariant(stack);
								return new ModelResourceLocation(name(ARROWS.getName()),
										"shaft=" + variant.getShaft().getName()
										+ ",tip=" + variant.getTip().getName());
							}));
		}
	}

	@SideOnly(Side.CLIENT)
	public static void initClient()
	{
		ItemColors colors = Minecraft.getMinecraft().getItemColors();

		colors.registerItemColorHandler(Colorizers.ITEM_ARMOR,
				CLOTHING.getItems(
						ClothingItems.HELMET,
						ClothingItems.CHESTPLATE,
						ClothingItems.LEGGINGS,
						ClothingItems.BOOTS)
						.toArray(new Item[0]));
	}
}
