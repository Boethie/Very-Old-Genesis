package genesis.stats;

import java.util.ArrayList;
import java.util.List;

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
	public static Achievement workbench;
	public static Achievement campfire;
	public static Achievement campfireCeramicBowl;
	public static Achievement campfirePorridge;
	public static Achievement hoeHeadChipped;
	public static Achievement pickaxeHeadChipped;
	public static Achievement pickaxeHeadChippedOctaedrite;
	public static Achievement pickaxeHeadChippedBlackDiamond;
	public static Achievement spearHeadChipped;
	
	public static void initAchievements()
	{
		enterGenesis = createAchievement("enter", 0, 0, new ItemStack(GenesisBlocks.portal), null, true);
		
		gettingPebble = createAchievement("pebble", 2, 1, new ItemStack(GenesisBlocks.ancient_permafrost), enterGenesis, false);
		
		workbench = createAchievement("workbench", -2, 1, new ItemStack(GenesisBlocks.workbench), enterGenesis, false);
		
		campfire = createAchievement("campfire", 0, 3, new ItemStack(GenesisBlocks.campfire), enterGenesis, true);
		campfireCeramicBowl = createAchievement("ceramicbowl", 2, 3, new ItemStack(GenesisItems.red_clay_bowl), campfire, false);
		campfirePorridge = createAchievement("porridge", 3, 4, new ItemStack(GenesisItems.red_clay_bowl), campfireCeramicBowl, false);
		
		hoeHeadChipped = createAchievement("hoeheadchipped", -3, 3, new ItemStack(GenesisItems.ceramic_bucket), workbench, false);
		
		pickaxeHeadChipped = createAchievement("pickaxeheadchipped", -4, 5, new ItemStack(GenesisBlocks.portal), workbench, true);
		pickaxeHeadChippedOctaedrite = createAchievement("pickaxeheadchippedoctaedrite", -5, 6, new ItemStack(GenesisBlocks.portal), pickaxeHeadChipped, false);
		pickaxeHeadChippedBlackDiamond = createAchievement("pickaxeheadchippedblackdiamond", -5, 8, new ItemStack(GenesisBlocks.portal), pickaxeHeadChippedOctaedrite, false);
		
		spearHeadChipped = createAchievement("spearheadchipped", -2, -2, new ItemStack(GenesisBlocks.portal), workbench,
				false);
		
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
		
		genesisAchievementPage = new AchievementPage(Constants.MOD_ID, achievements);
		AchievementPage.registerAchievementPage(genesisAchievementPage);
	}
	
}
