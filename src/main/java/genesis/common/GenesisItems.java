package genesis.common;

import genesis.item.*;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.Constants;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public final class GenesisItems
{
	/* Materials */
	public static final ItemGenesis red_clay_ball = new ItemGenesis().setUnlocalizedName("redClay");
	public static final ItemGenesis octaedrite_shard = new ItemGenesis().setUnlocalizedName("octaedriteShard");
	public static final ItemGenesis red_clay_bowl = new ItemGenesis().setUnlocalizedName("bowlRedClay");
	public static final ItemsCeramicBowls ceramic_bowls = new ItemsCeramicBowls();
	public static final ItemGenesis red_clay_bucket = new ItemGenesis().setUnlocalizedName("redClayBucket");
	public static final VariantsCombo nodules = new VariantsCombo(new ObjectType("nodule", null, null).setNamePosition(ObjectNamePosition.PREFIX), EnumNodule.values());
	public static final ItemGenesis quartz = new ItemGenesis().setUnlocalizedName("quartz");
	public static final ItemGenesis zircon = new ItemGenesis().setUnlocalizedName("zircon");
	public static final ItemGenesis garnet = new ItemGenesis().setUnlocalizedName("garnet");
	public static final ItemGenesis manganese = new ItemGenesis().setUnlocalizedName("manganese");
	public static final ItemGenesis hematite = new ItemGenesis().setUnlocalizedName("hematite");
	public static final ItemGenesis malachite = new ItemGenesis().setUnlocalizedName("malachite");
	public static final ItemGenesis olivine = new ItemGenesis().setUnlocalizedName("olivine");
	public static final ItemGenesis resin = new ItemGenesis().setUnlocalizedName("resin");
	public static final ItemGenesisSeeds calamites = new ItemGenesisSeeds().setUnlocalizedName("calamites");
	public static final ItemGenesis sphenophyllum_fiber = new ItemGenesis().setUnlocalizedName("sphenophyllumFiber");
	public static final ItemGenesis odontopteris_fiddlehead = new ItemGenesis().setUnlocalizedName("odontopterisFiddlehead");
	public static final ItemGenesis prototaxites_flesh = new ItemGenesis().setUnlocalizedName("prototaxitesFlesh");
	public static final ItemGenesis arthropleura_chitin = new ItemGenesis().setUnlocalizedName("arthropleuraChitin");
	public static final ItemGenesis tyrannosaurus_saliva = new ItemGenesis().setUnlocalizedName("tyrannosaurusSaliva");
	public static final ItemGenesis tyrannosaurus_tooth = new ItemGenesis().setUnlocalizedName("tyrannosaurusTooth");
	
	/* Food */
	public static final ItemGenesisFood aphthoroblattina = new ItemGenesisFood(1, 0.2F).setUnlocalizedName("aphthoroblattinaRaw");
	public static final ItemGenesisFood cooked_aphthoroblattina = new ItemGenesisFood(2, 0.8F).setUnlocalizedName("aphthoroblattinaCooked");
	public static final ItemGenesisFood climatius = new ItemGenesisFood(2, 0.4F).setUnlocalizedName("climatiusRaw");
	public static final ItemGenesisFood cooked_climatius = new ItemGenesisFood(5, 6.0F).setUnlocalizedName("climatiusCooked");
	public static final ItemGenesisFood eryops_leg = new ItemGenesisFood(2, 0.8F).setUnlocalizedName("eryopsLegRaw");
	public static final ItemGenesisFood cooked_eryops_leg = new ItemGenesisFood(5, 6.0F).setUnlocalizedName("eryopsLegCooked");
	public static final ItemGenesisFood tyrannosaurus = new ItemGenesisFood(4, 2.8F).setUnlocalizedName("tyrannosaurusRaw");
	public static final ItemGenesisFood cooked_tyrannosaurus = new ItemGenesisFood(16, 19.8F).setUnlocalizedName("tyrannosaurusCooked");
	public static final ItemGenesisSeedFood zingiberopsis_rhizome = new ItemGenesisSeedFood(2, 1.2F).setUnlocalizedName("zingiberopsisRhizome");
	public static final ItemGenesisSeedFood odontopteris_seeds = new ItemGenesisSeedFood(1, 0.8F).setUnlocalizedName("odontopterisSeeds");
	
	/* Tools */
	public static final ToolItems tools = new ToolItems();
	
	/* Misc */
	public static final ItemFlintAndMarcasite flint_and_marcasite = new ItemFlintAndMarcasite().setUnlocalizedName("flintAndMarcasite");
	public static final ItemGenesisBucket ceramic_bucket = new ItemGenesisBucket(Blocks.air).setUnlocalizedName("ceramicBucket").setContainerItem(GenesisItems.ceramic_bucket);
	public static final ItemGenesisBucket ceramic_bucket_water = new ItemGenesisBucket(Blocks.flowing_water).setUnlocalizedName("ceramicBucketWater");
	public static final ItemGenesisBucketMilk ceramic_bucket_milk = new ItemGenesisBucketMilk().setUnlocalizedName("ceramicBucketMilk");

	public static void registerItems()
	{
		tools.registerVariants(tools.PEBBLE);
		Genesis.proxy.registerItem(red_clay_ball, "red_clay_ball");
		Genesis.proxy.registerItem(red_clay_bowl, "red_clay_bowl");
		//Genesis.proxy.registerItem(ceramic_bowl, "ceramic_bowl");
		//Genesis.proxy.registerItem(ceramic_bowl_water, "ceramic_bowl_water");
		ceramic_bowls.registerAll();
		Genesis.proxy.registerItem(red_clay_bucket, "red_clay_bucket");
		Genesis.proxy.registerItem(octaedrite_shard, "octaedrite_shard");
		Genesis.proxy.registerItem(quartz, "quartz");
		Genesis.proxy.registerItem(zircon, "zircon");
		Genesis.proxy.registerItem(garnet, "garnet");
		Genesis.proxy.registerItem(manganese, "manganese");
		Genesis.proxy.registerItem(hematite, "hematite");
		Genesis.proxy.registerItem(malachite, "malachite");
		Genesis.proxy.registerItem(olivine, "olivine");
		nodules.registerAll();
		tools.registerVariants(tools.POINT_FLAKE);
		GenesisBlocks.trees.registerVariants(GenesisBlocks.trees.BILLET);
		Genesis.proxy.registerItem(resin, "resin");
		Genesis.proxy.registerItem(calamites, "calamites");
		Genesis.proxy.registerItem(sphenophyllum_fiber, "sphenophyllum_fiber");
		Genesis.proxy.registerItem(odontopteris_fiddlehead, "odontopteris_fiddlehead");
		Genesis.proxy.registerItem(prototaxites_flesh, "prototaxites_flesh");
		GenesisBlocks.dungs.registerVariants(GenesisBlocks.dungs.DUNG);
		Genesis.proxy.registerItem(arthropleura_chitin, "arthropleura_chitin");
		Genesis.proxy.registerItem(tyrannosaurus_saliva, "tyrannosaurus_saliva");
		Genesis.proxy.registerItem(tyrannosaurus_tooth, "tyrannosaurus_tooth");
		Genesis.proxy.registerItem(zingiberopsis_rhizome, "zingiberopsis_rhizome");
		Genesis.proxy.registerItem(odontopteris_seeds, "odontopteris_seeds");

		Genesis.proxy.registerItem(aphthoroblattina, "aphthoroblattina");
		Genesis.proxy.registerItem(cooked_aphthoroblattina, "cooked_aphthoroblattina");
		Genesis.proxy.registerItem(climatius, "climatius");
		Genesis.proxy.registerItem(cooked_climatius, "cooked_climatius");
		Genesis.proxy.registerItem(eryops_leg, "eryops_leg");
		Genesis.proxy.registerItem(cooked_eryops_leg, "cooked_eryops_leg");
		Genesis.proxy.registerItem(tyrannosaurus, "tyrannosaurus");
		Genesis.proxy.registerItem(cooked_tyrannosaurus, "cooked_tyrannosaurus");
		
		tools.registerAll();
		
		Genesis.proxy.registerItem(flint_and_marcasite, "flint_and_marcasite");
		
		Genesis.proxy.registerItem(ceramic_bucket, "ceramic_bucket");
		Genesis.proxy.registerItem(ceramic_bucket_water, "ceramic_bucket_water");
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluid("water"),
				new ItemStack(ceramic_bucket_water), new ItemStack(ceramic_bucket));
		Genesis.proxy.registerItem(ceramic_bucket_milk, "ceramic_bucket_milk");

	}
}
