package genesis.common;

import java.util.ArrayList;

import genesis.block.BlockCalamites;
import genesis.block.BlockCoral;
import genesis.block.BlockDung;
import genesis.block.BlockFern;
import genesis.block.BlockGenesis;
import genesis.block.BlockGenesisOre;
import genesis.block.BlockGenesisRock;
import genesis.block.BlockGrowingPlant;
import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms;
import genesis.block.BlockMetadata;
import genesis.block.BlockMoss;
import genesis.block.BlockNewPermafrost;
import genesis.block.BlockOctaedrite;
import genesis.block.BlockOdontopterisCustoms;
import genesis.block.BlockPermafrost;
import genesis.block.BlockPlant;
import genesis.block.BlockPrototaxites;
import genesis.block.BlockPrototaxitesMycelium;
import genesis.block.BlockRedClay;
import genesis.block.BlockSphenophyllumCustoms;
import genesis.item.ItemBlockColored;
import genesis.item.ItemBlockMetadata;
import genesis.metadata.EnumCoral;
import genesis.metadata.EnumDung;
import genesis.metadata.EnumFern;
import genesis.metadata.EnumNodule;
import genesis.metadata.EnumPlant;
import genesis.util.Constants;
import genesis.util.RandomItemDrop;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.GameData;

public final class GenesisBlocks
{
	public static final Block moss = new BlockMoss().setUnlocalizedName(Constants.PREFIX + "moss");

	/* Rocks */
	public static final BlockGenesisRock granite = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName("granite");
	public static final BlockGenesisRock mossy_granite = new BlockGenesisRock(2.1F, 10.0F).setUnlocalizedName("mossyGranite");
	public static final BlockGenesisRock rhyolite = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName("rhyolite");
	public static final BlockGenesisRock dolerite = new BlockGenesisRock(1.2F, 10.0F).setUnlocalizedName("dolerite");
	public static final BlockGenesisRock komatiite = new BlockGenesisRock(1.95F, 10.0F).setUnlocalizedName("komatiite");
	public static final BlockGenesisRock trondhjemite = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName("trondhjemite");
	public static final BlockGenesisRock faux_amphibolite = new BlockGenesisRock(1.5F, 10.0F).setUnlocalizedName("fauxAmphibolite");
	public static final BlockGenesisRock gneiss = new BlockGenesisRock(1.65F, 10.0F).setUnlocalizedName("gneiss");
	public static final BlockGenesisRock quartzite = new BlockGenesisRock(1.95F, 10.0F).setUnlocalizedName("quartzite");
	public static final BlockGenesisRock limestone = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName("limestone");
	public static final BlockGenesisRock shale = new BlockGenesisRock(0.75F, 8.7F).setUnlocalizedName("shale");
	public static final BlockRedClay red_clay = new BlockRedClay().setUnlocalizedName("redClay");
	public static final BlockGenesisRock octaedrite = new BlockOctaedrite().setUnlocalizedName("octaedrite");

	public static final BlockGenesis permafrost = new BlockPermafrost().setUnlocalizedName("permafrost");
	public static final BlockGenesis new_permafrost = new BlockNewPermafrost().setUnlocalizedName("permafrost");
	public static final BlockPrototaxitesMycelium prototaxites_mycelium = new BlockPrototaxitesMycelium().setUnlocalizedName("prototaxitesMycelium");

	/* Granite Ores */
	public static final BlockGenesisOre quartz_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(GenesisItems.quartz).setUnlocalizedName("quartz");
	public static final BlockGenesisOre zircon_ore = new BlockGenesisOre(4.2F, 5.0F, 2, 1).setDrop(GenesisItems.zircon).setUnlocalizedName("zircon");
	public static final BlockGenesisOre garnet_ore = new BlockGenesisOre(4.2F, 5.0F, 2, 1).setDrop(GenesisItems.garnet).setUnlocalizedName("garnet");
	public static final BlockGenesisOre manganese_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(GenesisItems.manganese).setUnlocalizedName("manganese");
	public static final BlockGenesisOre hematite_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 1).setDrop(GenesisItems.hematite).setUnlocalizedName("hematite");
	public static final BlockGenesisOre malachite_ore = new BlockGenesisOre(4.2F, 5.0F, 1, 2, 1).setDrop(GenesisItems.malachite).setUnlocalizedName("malachite");
	public static final BlockGenesisOre olivine_ore = new BlockGenesisOre(4.2F, 5.0F, 3, 5, 1).setDrop(GenesisItems.olivine).setUnlocalizedName("olivine");

	/* Limestone Ores */
	public static final BlockGenesisOre brown_flint_ore = new BlockGenesisOre(1.5F, 4.35F, 1, 0).setDrop(EnumNodule.BROWN_FLINT).setUnlocalizedName("brownFlint");
	public static final BlockGenesisOre marcasite_ore = new BlockGenesisOre(1.5F, 4.35F, 1, 0).setDrop(EnumNodule.MARCASITE).setUnlocalizedName("marcasite");

	/* Plants */
	public static final BlockPlant plant = new BlockPlant().setUnlocalizedName("plant");
	public static final BlockGrowingPlant calamites = new BlockCalamites(true, 15, 10)
			.setGrowthChanceMult(6, 1, 1)
			.setUnlocalizedName("plant.calamites");
	public static final BlockPlant fern = new BlockFern().setUnlocalizedName("fern");

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
	
	/* Misc */
	public static final BlockGenesis prototaxites = new BlockPrototaxites().setUnlocalizedName("prototaxites");
	public static final BlockMetadata coral = new BlockCoral().setUnlocalizedName("coral");
	public static final BlockMetadata dung_block = new BlockDung().setUnlocalizedName("dung");

	public static void registerBlocks()
	{
		Genesis.proxy.registerBlock(moss, "moss", ItemBlockColored.class);
		Genesis.proxy.registerBlock(granite, "granite");
		Genesis.proxy.registerBlock(mossy_granite, "mossy_granite");
		Genesis.proxy.registerBlock(rhyolite, "rhyolite");
		Genesis.proxy.registerBlock(dolerite, "dolerite");
		Genesis.proxy.registerBlock(komatiite, "komatiite");
		Genesis.proxy.registerBlock(trondhjemite, "trondhjemite");
		Genesis.proxy.registerBlock(faux_amphibolite, "faux_amphibolite");
		Genesis.proxy.registerBlock(gneiss, "gneiss");
		Genesis.proxy.registerBlock(quartzite, "quartzite");
		Genesis.proxy.registerBlock(limestone, "limestone");
		Genesis.proxy.registerBlock(shale, "shale");
		Genesis.proxy.registerBlock(red_clay, "red_clay");
		Genesis.proxy.registerBlock(octaedrite, "octaedrite");
		Genesis.proxy.registerBlock(permafrost, "permafrost");
		Genesis.proxy.registerBlock(new_permafrost, "new_permafrost");
		Genesis.proxy.registerBlock(prototaxites_mycelium, "prototaxites_mycelium");
		Genesis.proxy.registerBlock(quartz_ore, "quartz_ore");
		Genesis.proxy.registerBlock(zircon_ore, "zircon_ore");
		Genesis.proxy.registerBlock(garnet_ore, "garnet_ore");
		Genesis.proxy.registerBlock(manganese_ore, "manganese_ore");
		Genesis.proxy.registerBlock(hematite_ore, "hematite_ore");
		Genesis.proxy.registerBlock(malachite_ore, "malachite_ore");
		Genesis.proxy.registerBlock(olivine_ore, "olivine_ore");
		Genesis.proxy.registerBlock(brown_flint_ore, "brown_flint_ore");
		Genesis.proxy.registerBlock(marcasite_ore, "marcasite_ore");
		Genesis.proxy.registerBlock(dung_block, "dung_block", ItemBlockMetadata.class, EnumDung.class);
		Genesis.proxy.registerBlock(plant, "plant", ItemBlockMetadata.class, EnumPlant.class);

		Genesis.proxy.registerBlock(calamites, "calamites", null);
		calamites.setDrops(new RandomItemDrop(GenesisItems.calamites, 1, 1));
		calamites.setCropDrops(new RandomItemDrop(GenesisItems.calamites, 1, 1));
		calamites.setPickedItem(GenesisItems.calamites);
		GenesisItems.calamites.setCrop(calamites);
		
		Genesis.proxy.registerBlock(fern, "fern", ItemBlockColored.class, EnumFern.class);
		
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
		
		Genesis.proxy.registerBlock(prototaxites, "prototaxites");
		Genesis.proxy.registerBlock(coral, "coral", ItemBlockMetadata.class, EnumCoral.class);
	}
}
