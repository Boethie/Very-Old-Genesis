package genesis.stats;

import java.util.ArrayList;
import java.util.List;

import genesis.combo.ToolItems;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumToolMaterial;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import genesis.util.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class GenesisAchievements
{
	static AchievementPage genesisAchievementPage;
	private static List<Achievement> genesisAchievements = new ArrayList<Achievement>();
	
	public static Achievement enterGenesis;
	public static Achievement gettingPebble;
	public static Achievement gettingChoppingTool;
	public static Achievement gettingLog;
	public static Achievement workbench;
	public static Achievement campfire;
	public static Achievement knappingPickaxe;
	
	public static void initAchievements()
	{
		enterGenesis = createAchievement("enter", 0, 0, new ItemStack(GenesisBlocks.portal), null, true);
		
		gettingPebble = createAchievement("pebble", 2, 1, GenesisItems.tools.getStack(ToolItems.PEBBLE, EnumToolMaterial.GRANITE), enterGenesis, false);
		
		gettingChoppingTool = createAchievement("choppingtool", 4, 1, GenesisItems.tools.getStack(ToolItems.CHOPPING_TOOL, EnumToolMaterial.GRANITE), gettingPebble, false);
		
		gettingLog = createAchievement("log", 6, 1, GenesisBlocks.trees.getStack(TreeBlocksAndItems.LOG, EnumTree.SIGILLARIA), gettingChoppingTool, false);
		
		workbench = createAchievement("workbench", 8, -1, new ItemStack(GenesisBlocks.workbench), gettingLog, false);
		
		campfire = createAchievement("campfire", 10, -1, new ItemStack(GenesisBlocks.campfire), workbench, false);
		
		registerAchievements();
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
		Achievement[] achievements = new Achievement[genesisAchievements.size()];
		achievements = genesisAchievements.toArray(achievements);
		
		genesisAchievementPage = new AchievementPage("Genesis", achievements);
		AchievementPage.registerAchievementPage(genesisAchievementPage);
	}
}
