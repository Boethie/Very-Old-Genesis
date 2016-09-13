package genesis.common;

import genesis.util.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class GenesisLoot
{
	public static final ResourceLocation CHESTS_HUT = register("chests/hut");
	
	public static final ResourceLocation CHESTS_CAMP_AUX_FOREST = register("chests/camp/aux_forest");
	
	public static final ResourceLocation CHESTS_CAMP_META_FOREST = register("chests/camp/meta_forest");
	
	public static final ResourceLocation CHESTS_CAMP_WOODLANDS = register("chests/camp/woodlands");

	public static final ResourceLocation ITEM_RACK_HUT = register("item_rack/hut");
	
	public static final ResourceLocation ENTITY_MEGANEURA = register("entities/meganeura");

	private static ResourceLocation register(String id)
	{
		return LootTableList.register(new ResourceLocation(Constants.MOD_ID, id));
	}
}
