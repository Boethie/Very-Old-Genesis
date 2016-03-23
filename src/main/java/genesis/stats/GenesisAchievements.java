package genesis.stats;

import java.util.ArrayList;
import java.util.List;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import net.minecraft.init.Blocks;
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
		enterGenesis = createAchievement("enter", 0, 0, new ItemStack(GenesisBlocks.portal), null, true, true);
		
		gettingPebble = createAchievement("pebble", 2, 1, new ItemStack(GenesisBlocks.ancient_permafrost), enterGenesis, false, false);
		
		workbench = createAchievement("workbench", -2, 1, new ItemStack(GenesisBlocks.workbench), enterGenesis, false, false);
		
		campfire = createAchievement("campfire", 0, 3, new ItemStack(GenesisBlocks.campfire), enterGenesis, true, false);
		campfireCeramicBowl = createAchievement("ceramicbowl", 2, 3, new ItemStack(GenesisItems.red_clay_bowl), campfire, false, false);
		campfirePorridge = createAchievement("porridge", 3, 4, new ItemStack(GenesisItems.red_clay_bowl), campfireCeramicBowl, false, false);
		
		hoeHeadChipped = createAchievement("hoeheadchipped", -3, 3, new ItemStack(GenesisItems.ceramic_bucket), workbench, false, false);
		
		pickaxeHeadChipped = createAchievement("pickaxeheadchipped", -4, 5, new ItemStack(GenesisBlocks.portal), workbench, true, false);
		pickaxeHeadChippedOctaedrite = createAchievement("pickaxeheadchippedoctaedrite", -5, 6, new ItemStack(GenesisBlocks.portal), pickaxeHeadChipped, false, false);
		pickaxeHeadChippedBlackDiamond = createAchievement("pickaxeheadchippedblackdiamond", -5, 7, new ItemStack(GenesisBlocks.portal), pickaxeHeadChippedOctaedrite, false, false);
		
		spearHeadChipped = createAchievement("spearheadchipped", -2, -2, new ItemStack(GenesisBlocks.portal), workbench, false, false);
		
		registerAchievements();
	}
	
	private static Achievement createAchievement(String name, int coloum, int row, ItemStack icon, Achievement parent, boolean special, boolean independent)
	{
		Achievement create = new Achievement("achievement.genesis."+name, "genesis."+name, coloum, row, icon , parent);
		if(special)
			create = create.setSpecial();
		if(independent)
			create = create.initIndependentStat();
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
