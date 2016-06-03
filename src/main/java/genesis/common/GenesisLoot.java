package genesis.common;

import genesis.util.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class GenesisLoot
{
	public static final ResourceLocation CHESTS_HUT = register("chests/hut");

	private static ResourceLocation register(String id)
	{
		return LootTableList.register(new ResourceLocation(Constants.MOD_ID, id));
	}
}
