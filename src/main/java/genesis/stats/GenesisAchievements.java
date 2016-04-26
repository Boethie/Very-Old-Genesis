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
	public static AchievementPage genesisAchievementPage;
	private static List<Achievement> genesisAchievements = new ArrayList<Achievement>();
	
	public static Achievement menhirActivator;
	public static Achievement enterGenesis;
	public static Achievement gettingChoppingTool;
	public static Achievement gettingLog;
	public static Achievement workbench;
	public static Achievement knappingHoe;
	public static Achievement knappingSpear;
	public static Achievement shootingBow;
	public static Achievement knappingPickaxe;
	public static Achievement polishingPickaxe;
	public static Achievement firelighting;
	public static Achievement miningOre;
	
	public static void initAchievements()
	{
		menhirActivator = createAchievement("menhirActivator", 0, 0, GenesisItems.menhir_activators.getStack(EnumMenhirActivator.ANCIENT_AMBER), null, false);
		
		enterGenesis = createAchievement("enter", 2, 0, new ItemStack(GenesisBlocks.portal), menhirActivator, true);
		
		gettingChoppingTool = createAchievement("choppingTool", 4, 1, GenesisItems.tools.getStack(ToolItems.CHOPPING_TOOL, EnumToolMaterial.GRANITE), enterGenesis, false);
		
		gettingLog = createAchievement("log", 6, 1, GenesisBlocks.trees.getStack(TreeBlocksAndItems.LOG, EnumTree.SIGILLARIA), gettingChoppingTool, false);
		
		workbench = createAchievement("workbench", 8, -1, new ItemStack(GenesisBlocks.workbench), gettingLog, false);
		
		knappingHoe = createAchievement("chippedHoe", 6, -3, GenesisItems.tools.getStack(ToolItems.HOE, EnumToolMaterial.GRANITE, EnumToolQuality.CHIPPED), workbench, false);
		
		knappingSpear = createAchievement("chippedSpear", 10, -1, GenesisItems.tools.getStack(ToolItems.SPEAR, EnumToolMaterial.GRANITE, EnumToolQuality.CHIPPED), workbench, false);
		
		shootingBow = createAchievement("bow", 12, -1, GenesisItems.bows.getStack(EnumBowType.SELF, EnumTree.DRYOPHYLLUM), knappingSpear, true);
		
		knappingPickaxe = createAchievement("chippedPickaxe", 8, 2, GenesisItems.tools.getStack(ToolItems.PICKAXE, EnumToolMaterial.GRANITE, EnumToolQuality.CHIPPED), workbench, false);
		
		polishingPickaxe = createAchievement("polishedPickaxe", 10, 2, GenesisItems.tools.getStack(ToolItems.PICKAXE, EnumToolMaterial.GRANITE, EnumToolQuality.POLISHED), knappingPickaxe, false);
		
		firelighting = createAchievement("fire", 6, 2, new ItemStack (GenesisItems.flint_and_marcasite), knappingPickaxe, false);
		
		miningOre = createAchievement("ore", 8, 4, GenesisBlocks.ores.getOreStack(EnumOre.BLACK_DIAMOND), knappingPickaxe, false);
		
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
			
			if (GenesisItems.tools.isStackOf(stack, ToolItems.HOE))
			{
				switch (GenesisItems.tools.getVariant(stack).quality)
				{
				case CHIPPED:
					player.addStat(knappingHoe, 1);
					break;
				default:
					break;
				}
			}
			
			if (GenesisItems.tools.isStackOf(stack, ToolItems.SPEAR))
			{
				switch (GenesisItems.tools.getVariant(stack).quality)
				{
				case CHIPPED:
					player.addStat(knappingSpear, 1);
					break;
				default:
					break;
				}
			}
			
			if (GenesisItems.bows.isStackOf(stack))
				player.addStat(shootingBow, 1);
			
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
		}
		
		@SubscribeEvent
		public void onItemPickup(EntityItemPickupEvent event)
		{
			doItemAchievement(event.getItem().getEntityItem(), event.getEntityPlayer());
		}
		
		@SubscribeEvent
		public void onItemCrafted(ItemCraftedEvent event)
		{
			doItemAchievement(event.crafting, event.player);
		}
	}
}
