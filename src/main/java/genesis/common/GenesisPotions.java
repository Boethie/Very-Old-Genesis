package genesis.common;

import genesis.potion.PotionGenesis;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GenesisPotions
{
	//TODO Add an icon(https://trello.com/c/WKzk4NGU/609-do-texture-for-status-effect-radiation)
	public static final Potion radiation = new PotionGenesis(true, 0x00FF00)
			.setEffectiveness(0.25D)
			.setPotionName(Unlocalized.EFFECT + "radiation");

	private static ResourceLocation name(String path)
	{
		return new ResourceLocation(Constants.MOD_ID, path);
	}

	public static void register()
	{
		GameRegistry.register(radiation, name("radiation"));
	}
}
