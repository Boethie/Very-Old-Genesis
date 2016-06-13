package genesis.common;

import genesis.client.Colorizers;
import genesis.client.model.MetadataModelDefinition;
import genesis.combo.*;
import genesis.combo.variant.*;
import genesis.entity.fixed.EntityMeganeuraEgg;
import genesis.item.*;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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

public final class GenesisItems
{
	/* Materials */
	public static final Item red_clay_ball = new ItemGenesis().setUnlocalizedName(Unlocalized.MATERIAL + "redClay");
	public static final Item red_clay_bowl = new ItemGenesis().setUnlocalizedName(Unlocalized.MATERIAL + "redClayBowl");
	
	public static final ItemsCeramicBowls bowls = new ItemsCeramicBowls();
	
	public static final Item red_clay_bucket = new ItemGenesis().setUnlocalizedName(Unlocalized.MATERIAL + "redClayBucket");
	
	public static final VariantsCombo<EnumNodule, Block, ItemMulti<EnumNodule>> nodules =
			VariantsCombo.create(
					"nodules",
					ObjectType.createItem(EnumNodule.class, "nodule"),
					EnumNodule.class, EnumNodule.values());
	public static final VariantsCombo<EnumPowder, Block, ItemMulti<EnumPowder>> powders =
			VariantsCombo.create(
					"powders",
					ObjectType.createItem(EnumPowder.class, "powder"),
					EnumPowder.class, EnumPowder.values());
	
	public static final VariantsCombo<EnumMaterial, Block, ItemMaterial> materials =
			VariantsCombo.create(
					"materials",
					ObjectType.createItem(EnumMaterial.class, "material", ItemMaterial.class).setResourceName(""),
					EnumMaterial.class, EnumMaterial.values());
	
	/* Eggs */
	public static final ItemGenesisEgg<EntityMeganeuraEgg> meganeura_egg = new ItemGenesisEgg<>(EntityMeganeuraEgg.class)
			.setUnlocalizedName(Unlocalized.EGG + "meganeura");
	
	/* Seeds */
	public static final VariantsCombo<EnumSeeds, Block, ItemGenesisSeeds> seeds =
			VariantsCombo.create(
					"seeds",
					ObjectType.createItem(EnumSeeds.class, "seed", ItemGenesisSeeds.class).setResourceName(""),
					EnumSeeds.class, EnumSeeds.values());
	public static final Item rotten_zingiberopsis_rhizome = new ItemFood(1, 0.7F, false)
			.setPotionEffect(new PotionEffect(MobEffects.poison, 100, 0), 0.6F)
			.setUnlocalizedName(Unlocalized.FOOD + "rottenZingiberopsisRhizome")
			.setCreativeTab(GenesisCreativeTabs.FOOD);
	
	/* Foods */
	public static final FoodItems foods = new FoodItems();
	
	/* Tools */
	public static final ItemFlintAndMarcasite flint_and_marcasite = (ItemFlintAndMarcasite) new ItemFlintAndMarcasite().setUnlocalizedName(Unlocalized.PREFIX + Unlocalized.Section.TOOL + "flintAndMarcasite");
	public static final ToolItems tools = new ToolItems();
	
	/* Armor */
	public static final ClothingItems clothing = new ClothingItems();
	
	/* Misc */
	public static final Item ceramic_bucket = new ItemGenesisBucket(Blocks.air.getDefaultState()).setUnlocalizedName(Unlocalized.MISC + "ceramicBucket");
	public static final Item ceramic_bucket_water = new ItemGenesisBucket(Blocks.flowing_water.getDefaultState()).setUnlocalizedName(Unlocalized.MISC + "ceramicBucketWater").setContainerItem(ceramic_bucket);
	public static final Item ceramic_bucket_milk = new ItemGenesisBucketMilk().setUnlocalizedName(Unlocalized.MISC + "ceramicBucketMilk").setContainerItem(ceramic_bucket);
	public static Item bucket_komatiitic_lava;
	
	public static final VariantsCombo<EnumMenhirActivator, Block, ItemMulti<EnumMenhirActivator>> menhir_activators =
			new VariantsCombo<EnumMenhirActivator, Block, ItemMulti<EnumMenhirActivator>>(
					"menhir_activators",
					new ObjectType<EnumMenhirActivator, Block, ItemMulti<EnumMenhirActivator>>(EnumMenhirActivator.class, "menhir_activator", Unlocalized.MISC + "menhirActivator", null, null)
							.setCreativeTab(GenesisCreativeTabs.MISC)
							.setResourceName(""),
					EnumMenhirActivator.class, EnumMenhirActivator.values())
			.setNames(Constants.MOD_ID, "");
	
	public static final BowItems bows = new BowItems();
	
	public static final Item bench_seat = new ItemBenchSeat().setUnlocalizedName(Unlocalized.PREFIX + "benchSeat");
	
	private static ResourceLocation name(String path)
	{
		return new ResourceLocation(Constants.MOD_ID, path);
	}
	
	public static void preInitCommon()
	{
		// --- Materials ---
		// Tool materials
		tools.registerVariants(ToolItems.PEBBLE);
		tools.registerVariants(ToolItems.FLAKE);
		
		// Clay
		Genesis.proxy.registerItem(red_clay_ball, name("red_clay_ball"));
		Genesis.proxy.registerItem(red_clay_bowl, name("red_clay_bowl"));
		bowls.registerAll();
		Genesis.proxy.registerItem(red_clay_bucket, name("red_clay_bucket"));
		
		// Ores
		GenesisBlocks.ores.registerVariants(OreBlocks.DROP);
		
		powders.setNames(Constants.MOD_ID, Unlocalized.MATERIAL);
		powders.registerAll();
		
		nodules.setNames(Constants.MOD_ID, Unlocalized.MATERIAL);
		nodules.registerAll();
		
		// Billets
		GenesisBlocks.trees.registerVariants(TreeBlocksAndItems.BILLET);
		
		// Random materials
		GenesisBlocks.dungs.registerVariants(DungBlocksAndItems.DUNG);
		
		materials.setNames(Constants.MOD_ID, Unlocalized.PREFIX);
		materials.registerAll();
		
		Genesis.proxy.registerItem(meganeura_egg, name("meganeura_egg"));
		
		seeds.setNames(Constants.MOD_ID, Unlocalized.PREFIX);
		seeds.registerAll();
		Genesis.proxy.registerItem(rotten_zingiberopsis_rhizome, name("rotten_zingiberopsis_rhizome"));
		
		// Foods
		foods.registerAll();
		
		// --- Begin tools ---
		Genesis.proxy.registerItem(flint_and_marcasite, name("flint_and_marcasite"));
		
		// All tools
		tools.registerAll();
		
		// Armor
		clothing.registerAll();
		
		// --- Misc ---
		Genesis.proxy.registerItem(ceramic_bucket, name("ceramic_bucket"));
		Genesis.proxy.registerItem(ceramic_bucket_water, name("ceramic_bucket_water"));
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluid("water"),
				new ItemStack(ceramic_bucket_water), new ItemStack(ceramic_bucket));
		Genesis.proxy.registerItem(ceramic_bucket_milk, name("ceramic_bucket_milk"));
		
		bucket_komatiitic_lava = new ItemGenesisBucket(GenesisBlocks.komatiitic_lava.getDefaultState()).setUnlocalizedName(Unlocalized.MISC + "bucketKomatiiticLava");
		Genesis.proxy.registerItem(bucket_komatiitic_lava, name("bucket_komatiitic_lava"));
		
		Genesis.proxy.registerItem(bench_seat, name("item_bench_seat"));
		
		menhir_activators.registerAll();
		
		bows.registerAll();
	}
	
	public static void preInitClient()
	{
		for (Item bow : bows.getItems())
			Genesis.proxy.registerModel(bow, MetadataModelDefinition.from(bow, (s) -> name(bows.getVariant(s).getName())));
	}
	
	public static void initClient()
	{
		ItemColors colors = Minecraft.getMinecraft().getItemColors();
		
		colors.registerItemColorHandler(Colorizers.ITEM_ARMOR,
				clothing.getItems(
						ClothingItems.HELMET,
						ClothingItems.CHESTPLATE,
						ClothingItems.LEGGINGS,
						ClothingItems.BOOTS)
						.toArray(new Item[0]));
	}
}
