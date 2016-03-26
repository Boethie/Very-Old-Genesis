package genesis.stats;

import java.util.*;

import genesis.combo.*;
import genesis.combo.variant.*;
import genesis.common.*;
import net.minecraft.block.Block;
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
		
		gettingChoppingTool = createAchievement("choppingTool", 4, 1, GenesisItems.tools.getStack(ToolItems.CHOPPING_TOOL, EnumToolMaterial.GRANITE), enterGenesis, false);
		
		gettingLog = createAchievement("log", 6, 1, GenesisBlocks.trees.getStack(TreeBlocksAndItems.LOG, EnumTree.SIGILLARIA), gettingChoppingTool, false);
		
		workbench = createAchievement("workbench", 8, -1, new ItemStack(GenesisBlocks.workbench), gettingLog, false);
		
		campfire = createAchievement("campfire", 10, -1, new ItemStack(GenesisBlocks.campfire), workbench, false);
		
		knappingHoe = createAchievement("chippedHoe", 6, -3, GenesisItems.tools.getStack(ToolItems.HOE, EnumToolMaterial.GRANITE, EnumToolQuality.CHIPPED), workbench, false);
		
		polishingHoe = createAchievement("polishedHoe", 6, -5, GenesisItems.tools.getStack(ToolItems.HOE, EnumToolMaterial.GRANITE, EnumToolQuality.POLISHED), knappingHoe, false);
		
		knappingPickaxe = createAchievement("chippedPickaxe", 8, 2, GenesisItems.tools.getStack(ToolItems.PICKAXE, EnumToolMaterial.GRANITE, EnumToolQuality.CHIPPED), workbench, false);
		
		polishingPickaxe = createAchievement("polishedPickaxe", 10, 2, GenesisItems.tools.getStack(ToolItems.PICKAXE, EnumToolMaterial.GRANITE, EnumToolQuality.POLISHED), knappingPickaxe, false);
		
		octaedrite = createAchievement("octaedrite", 8, 4, new ItemStack(GenesisBlocks.octaedrite), knappingPickaxe, false);
		
		blackDiamond = createAchievement("blackDiamond", 6, 4, GenesisBlocks.ores.getStack(OreBlocks.DROP, EnumOre.BLACK_DIAMOND), octaedrite, false);
		
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
		private static boolean isBlock(ItemStack stack, Block block)
		{
			return stack.getItem() == Item.getItemFromBlock(block);
		}
		
		private static void doItemAchievement(ItemStack stack, EntityPlayer player)
		{
			if (GenesisItems.menhir_activators.isStackOf(stack))
				player.addStat(menhirActivator, 1);
			
			if (GenesisBlocks.trees.isStackOf(stack, TreeBlocksAndItems.LOG))
				player.addStat(gettingLog, 1);
			
			if (isBlock(stack, GenesisBlocks.workbench))
				player.addStat(workbench, 1);
			
			if (isBlock(stack, GenesisBlocks.campfire))
				player.addStat(campfire, 1);
			
			if (GenesisItems.tools.isStackOf(stack, ToolItems.HOE))
			{
				switch (GenesisItems.tools.getVariant(stack).quality)
				{
				case CHIPPED:
					player.addStat(knappingHoe, 1);
					break;
				case POLISHED:
					player.addStat(polishingHoe, 1);
					break;
				default:
					break;
				}
			}
			
			if (GenesisItems.tools.isStackOf(stack, ToolItems.PICKAXE))
			{
				switch (GenesisItems.tools.getVariant(stack).quality)
				{
				case CHIPPED:
					player.addStat(knappingPickaxe, 1);
					break;
				case POLISHED:
					player.addStat(polishingPickaxe, 1);
					break;
				default:
					break;
				}
			}
			
			if (isBlock(stack, GenesisBlocks.octaedrite))
				player.addStat(octaedrite, 1);
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
