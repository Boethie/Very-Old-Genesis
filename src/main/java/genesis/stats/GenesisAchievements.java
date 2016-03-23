package genesis.stats;

import java.util.ArrayList;
import java.util.List;

import genesis.common.GenesisBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class GenesisAchievements
{
	static AchievementPage genesisAchievementPage;
	private static List<Achievement> genesisAchievements = new ArrayList<Achievement>();
	
	public static Achievement enterGenesis;
	
	public static void initAchievements()
	{
		enterGenesis = createAchievement("enter", 0, 0, new ItemStack(GenesisBlocks.portal), null, true, true);
		
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
