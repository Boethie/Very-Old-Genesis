package genesis.common;

import genesis.util.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class GenesisLoot
{
	public static final ResourceLocation CHESTS_HUT = register("chests/hut");
	
	public static final ResourceLocation CHESTS_CAMP_AUXFOREST = register("chests/camp/auxforest");
	
	public static final ResourceLocation CHESTS_CAMP_METAFOREST = register("chests/camp/metaforest");
	
	public static final ResourceLocation CHESTS_CAMP_WOODLANDS = register("chests/camp/woodlands");

	private static ResourceLocation register(String id)
	{
		return LootTableList.register(new ResourceLocation(Constants.MOD_ID, id));
	}
}
