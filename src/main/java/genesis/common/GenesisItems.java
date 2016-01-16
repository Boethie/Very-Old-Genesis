package genesis.common;

import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.EnumMaterial;
import genesis.combo.variant.EnumMenhirActivator;
import genesis.combo.variant.EnumNodule;
import genesis.combo.variant.EnumPowder;
import genesis.combo.variant.EnumSeeds;
import genesis.entity.fixed.EntityMeganeuraEgg;
import genesis.item.*;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
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
					ObjectType.<EnumNodule>createItem("nodule"),
					EnumNodule.class, EnumNodule.values());
	public static final VariantsCombo<EnumPowder, Block, ItemMulti<EnumPowder>> powders =
			VariantsCombo.create(
					ObjectType.<EnumPowder>createItem("powder"),
					EnumPowder.class, EnumPowder.values());
	
	public static final VariantsCombo<EnumMaterial, Block, ItemMulti<EnumMaterial>> materials =
			VariantsCombo.create(
					ObjectType.<EnumMaterial>createItem("material").setResourceName(""),
					EnumMaterial.class, EnumMaterial.values());
	
	/* Eggs */
	public static final ItemGenesisEgg<EntityMeganeuraEgg> meganeura_egg = new ItemGenesisEgg<EntityMeganeuraEgg>(EntityMeganeuraEgg.class)
			.setUnlocalizedName(Unlocalized.EGG + "meganeura");
	
	/* Seeds */
	public static final VariantsCombo<EnumSeeds, Block, ItemGenesisSeeds> seeds =
			VariantsCombo.create(
					ObjectType.createItem("seeds", ItemGenesisSeeds.class).setResourceName(""),
					EnumSeeds.class, EnumSeeds.values());
	
	/* Foods */
	public static final FoodItems foods = new FoodItems();
	
	/* Tools */
	public static final ItemFlintAndMarcasite flint_and_marcasite = (ItemFlintAndMarcasite) new ItemFlintAndMarcasite().setUnlocalizedName(Unlocalized.PREFIX + Unlocalized.Section.TOOL + "flintAndMarcasite");
	public static final ToolItems tools = new ToolItems();
	
	/* Armor */
	private static final ItemArmor.ArmorMaterial chitin = EnumHelper.addArmorMaterial("Chitin", "genesis:chitin", 10, new int[]{2, 4, 3, 2}, 10);
	public static final ItemArmor chitinHelmet = (ItemArmor) new ItemArmor(chitin, -1, 0).setUnlocalizedName(Unlocalized.ARMOR + "chitinHelmet").setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ItemArmor chitinChestplate = (ItemArmor) new ItemArmor(chitin, -1, 1).setUnlocalizedName(Unlocalized.ARMOR + "chitinChestplate").setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ItemArmor chitinLeggings = (ItemArmor) new ItemArmor(chitin, -1, 2).setUnlocalizedName(Unlocalized.ARMOR + "chitinLeggings").setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ItemArmor chitinBoots = (ItemArmor) new ItemArmor(chitin, -1, 3).setUnlocalizedName(Unlocalized.ARMOR + "chitinBoots").setCreativeTab(GenesisCreativeTabs.COMBAT);
	
	/* Misc */
	public static final Item ceramic_bucket = new ItemGenesisBucket(Blocks.air).setUnlocalizedName(Unlocalized.MISC + "ceramicBucket");
	public static final Item ceramic_bucket_water = new ItemGenesisBucket(Blocks.flowing_water).setUnlocalizedName(Unlocalized.MISC + "ceramicBucketWater").setContainerItem(ceramic_bucket);
	public static final Item ceramic_bucket_milk = new ItemGenesisBucketMilk().setUnlocalizedName(Unlocalized.MISC + "ceramicBucketMilk").setContainerItem(ceramic_bucket);
	public static Item bucket_komatiitic_lava;
	
	public static final VariantsCombo<EnumMenhirActivator, Block, ItemMulti<EnumMenhirActivator>> menhir_activators =
			new VariantsCombo<EnumMenhirActivator, Block, ItemMulti<EnumMenhirActivator>>(
					new ObjectType<Block, ItemMulti<EnumMenhirActivator>>("menhir_activator", Unlocalized.MISC + "menhirActivator", null, null)
							.setCreativeTab(GenesisCreativeTabs.MISC)
							.setResourceName(""),
					EnumMenhirActivator.class, EnumMenhirActivator.values());
	
	public static void registerItems()
	{
		// --- Materials ---
		// Tool materials
		tools.registerVariants(ToolItems.PEBBLE);
		tools.registerVariants(ToolItems.FLAKE);
		
		// Clay
		Genesis.proxy.registerItem(red_clay_ball, "red_clay_ball");
		Genesis.proxy.registerItem(red_clay_bowl, "red_clay_bowl");
		bowls.registerAll();
		Genesis.proxy.registerItem(red_clay_bucket, "red_clay_bucket");
		
		// Ores
		GenesisBlocks.ores.registerVariants(OreBlocks.DROP);
		
		powders.setUnlocalizedPrefix(Unlocalized.MATERIAL);
		powders.registerAll();
		
		nodules.setUnlocalizedPrefix(Unlocalized.MATERIAL);
		nodules.registerAll();
		
		// Billets
		GenesisBlocks.trees.registerVariants(TreeBlocksAndItems.BILLET);
		
		// Random materials
		GenesisBlocks.dungs.registerVariants(DungBlocksAndItems.DUNG);
		
		materials.setUnlocalizedPrefix(Unlocalized.PREFIX);
		materials.registerAll();
		
		Genesis.proxy.registerItem(meganeura_egg, "meganeura_egg");
		
		seeds.setUnlocalizedPrefix(Unlocalized.PREFIX);
		seeds.registerAll();
		
		// Foods
		foods.registerAll();
		
		// --- Begin tools ---
		Genesis.proxy.registerItem(flint_and_marcasite, "flint_and_marcasite");
		
		// All tools
		tools.registerAll();
		
		// Armor
		Genesis.proxy.registerItem(chitinHelmet, "chitin_helmet");
		Genesis.proxy.registerItem(chitinChestplate, "chitin_chestplate");
		Genesis.proxy.registerItem(chitinLeggings, "chitin_leggings");
		Genesis.proxy.registerItem(chitinBoots, "chitin_boots");
		
		// --- Misc ---
		Genesis.proxy.registerItem(ceramic_bucket, "ceramic_bucket");
		Genesis.proxy.registerItem(ceramic_bucket_water, "ceramic_bucket_water");
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluid("water"),
				new ItemStack(ceramic_bucket_water), new ItemStack(ceramic_bucket));
		Genesis.proxy.registerItem(ceramic_bucket_milk, "ceramic_bucket_milk");
		
		bucket_komatiitic_lava = new ItemGenesisBucket(GenesisBlocks.komatiitic_lava).setUnlocalizedName(Unlocalized.MISC + "bucketKomatiiticLava");
		Genesis.proxy.registerItem(bucket_komatiitic_lava, "bucket_komatiitic_lava");
		
		menhir_activators.registerAll();
	}
}
