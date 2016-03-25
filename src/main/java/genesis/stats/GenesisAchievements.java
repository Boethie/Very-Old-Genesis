package genesis.stats;

import java.util.ArrayList;
import java.util.List;

import genesis.combo.OreBlocks;
import genesis.combo.ToolItems;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumDish;
import genesis.combo.variant.EnumMenhirActivator;
import genesis.combo.variant.EnumOre;
import genesis.combo.variant.EnumToolMaterial;
import genesis.combo.variant.EnumToolQuality;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class GenesisAchievements
{
	static AchievementPage genesisAchievementPage;
	private static List<Achievement> genesisAchievements = new ArrayList<Achievement>();
	
	public static Achievement menhirActivator;
	public static Achievement enterGenesis;
	public static Achievement gettingPebble;
	public static Achievement gettingChoppingTool;
	public static Achievement gettingLog;
	public static Achievement workbench;
	public static Achievement campfire;
	public static Achievement knappingHoe;
	public static Achievement polishingHoe;
	public static Achievement knappingPickaxe;
	public static Achievement polishingPickaxe;
	public static Achievement octaedrite;
	public static Achievement blackDiamond;
	
	public static void initAchievements()
	{
		menhirActivator = createAchievement("menhirActivator", 0, 0, GenesisItems.menhir_activators.getStack(EnumMenhirActivator.ANCIENT_AMBER), null, false);
		
		enterGenesis = createAchievement("enter", 2, 0, new ItemStack(GenesisBlocks.portal), menhirActivator, true);
		
		gettingPebble = createAchievement("pebble", 4, 1, GenesisItems.tools.getStack(ToolItems.PEBBLE, EnumToolMaterial.GRANITE), enterGenesis, false);
		
		gettingChoppingTool = createAchievement("choppingTool", 6, 1, GenesisItems.tools.getStack(ToolItems.CHOPPING_TOOL, EnumToolMaterial.GRANITE), gettingPebble, false);
		
		gettingLog = createAchievement("log", 8, 1, GenesisBlocks.trees.getStack(TreeBlocksAndItems.LOG, EnumTree.SIGILLARIA), gettingChoppingTool, false);
		
		workbench = createAchievement("workbench", 10, -1, new ItemStack(GenesisBlocks.workbench), gettingLog, false);
		
		campfire = createAchievement("campfire", 12, -1, new ItemStack(GenesisBlocks.campfire), workbench, false);
		
		knappingHoe = createAchievement("chippedHoe", 8, -3, GenesisItems.tools.getStack(ToolItems.HOE, EnumToolMaterial.GRANITE, EnumToolQuality.CHIPPED), workbench, false);
		
		polishingHoe = createAchievement("polishedHoe", 8, -5, GenesisItems.tools.getStack(ToolItems.HOE, EnumToolMaterial.GRANITE, EnumToolQuality.POLISHED), knappingHoe, false);
		
		knappingPickaxe = createAchievement("chippedPickaxe", 10, 2, GenesisItems.tools.getStack(ToolItems.PICKAXE, EnumToolMaterial.GRANITE, EnumToolQuality.CHIPPED), workbench, false);
		
		polishingPickaxe = createAchievement("polishedPickaxe", 12, 2, GenesisItems.tools.getStack(ToolItems.PICKAXE, EnumToolMaterial.GRANITE, EnumToolQuality.POLISHED), knappingPickaxe, false);
		
		octaedrite = createAchievement("octaedrite", 9, 4, new ItemStack(GenesisBlocks.octaedrite), knappingPickaxe, false);
		
		blackDiamond = createAchievement("blackDiamond", 7, 4, GenesisBlocks.ores.getStack(OreBlocks.DROP, EnumOre.BLACK_DIAMOND), octaedrite, false);
		
		registerAchievements();
		
		MinecraftForge.EVENT_BUS.register(new Handler());
	}
	
	private static Achievement createAchievement(String name, int coloum, int row, ItemStack icon, Achievement parent, boolean special)
	{
		Achievement create = new Achievement("achievement.genesis." + name, "genesis." + name, coloum, row, icon, parent);
		if (special)
			create = create.setSpecial();
		if (parent == null)
			create = create.initIndependentStat();
		create.registerStat();
		genesisAchievements.add(create);
		return create;
	}
	
	private static void registerAchievements()
	{
		genesisAchievementPage = new AchievementPage("Genesis",
				genesisAchievements.toArray(new Achievement[genesisAchievements.size()]));
		AchievementPage.registerAchievementPage(genesisAchievementPage);
	}
	
	public static class Handler
	{
		private static void doItemAchievement(ItemStack stack, EntityPlayer player)
		{
			if (GenesisItems.tools.isStackOf(stack, ToolItems.PEBBLE))
				player.addStat(gettingPebble, 1);
			
			if (GenesisBlocks.trees.isStackOf(stack, TreeBlocksAndItems.LOG))
				player.addStat(gettingLog, 1);
			
			if (stack.getItem() == Item.getItemFromBlock(GenesisBlocks.workbench))
				player.addStat(workbench, 1);
			
			if (stack.getItem() == Item.getItemFromBlock(GenesisBlocks.campfire))
				player.addStat(campfire, 1);
		}
		
		@SubscribeEvent
		public void onItemPickup(EntityItemPickupEvent event)
		{
			doItemAchievement(event.item.getEntityItem(), event.entityPlayer);
		}
		
		@SubscribeEvent
		public void onItemCrafted(ItemCraftedEvent event)
		{
			doItemAchievement(event.crafting, event.player);
		}
	}
}
