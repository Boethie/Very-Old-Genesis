package genesis.common;

import genesis.entity.fixed.EntityMeganeuraEgg;
import genesis.item.*;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
					EnumNodule.values());
	public static final VariantsCombo<EnumPowder, Block, ItemMulti<EnumPowder>> powders =
			VariantsCombo.create(
					ObjectType.<EnumPowder>createItem("powder"),
					EnumPowder.values());
	
	public static final VariantsCombo<EnumMaterial, Block, ItemMulti<EnumMaterial>> materials =
			VariantsCombo.create(
					ObjectType.<EnumMaterial>createItem("material").setResourceName(""),
					EnumMaterial.values());
	
	/* Eggs */
	public static final ItemGenesisEgg<EntityMeganeuraEgg> meganeura_egg = new ItemGenesisEgg<EntityMeganeuraEgg>(EntityMeganeuraEgg.class)
			.setUnlocalizedName(Unlocalized.EGG + "meganeura");
	
	/* Seeds */
	public static final ItemGenesisSeeds programinis_seeds = (ItemGenesisSeeds) new ItemGenesisSeeds().setUnlocalizedName(Unlocalized.MATERIAL + "programinisSeeds");
	
	/* Foods */
	public static final ItemGenesisSeedFood araucarioxylon_seeds = (ItemGenesisSeedFood) new ItemGenesisSeedFood(1, 0.6F).setUnlocalizedName(Unlocalized.FOOD + "araucarioxylonSeeds");
	public static final ItemGenesisSeedFood odontopteris_seeds = (ItemGenesisSeedFood) new ItemGenesisSeedFood(1, 0.8F).setUnlocalizedName(Unlocalized.FOOD + "odontopterisSeeds");
	public static final ItemGenesisSeedFood neuropteridium_rhizome = (ItemGenesisSeedFood) new ItemGenesisSeedFood(2, 1.4F).setUnlocalizedName(Unlocalized.FOOD + "neuropteridiumRhizome");
	public static final ItemGenesisSeedFood zingiberopsis_rhizome = (ItemGenesisSeedFood) new ItemGenesisSeedFood(2, 1.2F).setUnlocalizedName(Unlocalized.FOOD + "zingiberopsisRhizome");
	
	public static final FoodItems foods = new FoodItems();
	
	/* Tools */
	public static final ItemFlintAndMarcasite flint_and_marcasite = (ItemFlintAndMarcasite) new ItemFlintAndMarcasite().setUnlocalizedName(Unlocalized.PREFIX + Unlocalized.Section.TOOL + "flintAndMarcasite");
	public static final ToolItems tools = new ToolItems();
	
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
					EnumMenhirActivator.values());
	
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
		
		Genesis.proxy.registerItem(programinis_seeds, "programinis_seeds");
		
		materials.setUnlocalizedPrefix(Unlocalized.PREFIX);
		materials.registerAll();
		
		Genesis.proxy.registerItem(meganeura_egg, "meganeura_egg");
		
		// --- Foods ---
		Genesis.proxy.registerItem(araucarioxylon_seeds, "araucarioxylon_seeds");
		Genesis.proxy.registerItem(odontopteris_seeds, "odontopteris_seeds");
		Genesis.proxy.registerItem(neuropteridium_rhizome, "neuropteridium_rhizome");
		Genesis.proxy.registerItem(zingiberopsis_rhizome, "zingiberopsis_rhizome");
		
		foods.registerAll();
		
		// --- Begin tools ---
		Genesis.proxy.registerItem(flint_and_marcasite, "flint_and_marcasite");
		
		// All tools
		tools.registerAll();
		
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
