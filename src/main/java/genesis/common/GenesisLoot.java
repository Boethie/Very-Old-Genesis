package genesis.common;

import genesis.util.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class GenesisLoot {

	public static ResourceLocation HUT = LootTableList.register(new ResourceLocation(Constants.MOD_ID, "chests/hut"));
}
