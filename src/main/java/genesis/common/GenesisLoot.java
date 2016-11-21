package genesis.common;

import genesis.util.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class GenesisLoot
{
	public static final ResourceLocation STORAGE_BOX_HUT = register("storage_box/hut");
	public static final ResourceLocation STORAGE_BOX_METASEQUOIA_HOUSE = register("storage_box/metasequoia_house");
	
	public static final ResourceLocation STORAGE_BOX_CAMP_AUX_FOREST = register("storage_box/camp/aux_forest");
	public static final ResourceLocation STORAGE_BOX_CAMP_META_FOREST = register("storage_box/camp/meta_forest");
	public static final ResourceLocation STORAGE_BOX_CAMP_WOODLANDS = register("storage_box/camp/woodlands");
	
	public static final ResourceLocation STORAGE_BOX_ROTTEN_ANCIENT_HUT = register("storage_box/rotten/ancient_hut");
	
	public static final ResourceLocation ITEM_RACK_HUT = register("item_rack/hut");
	
	public static final ResourceLocation ENTITY_MEGANEURA = register("entities/meganeura");
	
	private static ResourceLocation register(String id)
	{
		return LootTableList.register(new ResourceLocation(Constants.MOD_ID, id));
	}
}
