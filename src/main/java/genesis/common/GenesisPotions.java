package genesis.common;

import genesis.potion.PotionGenesis;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GenesisPotions
{
	/** The radiation potion effect is caused by radioactive traces.
	 *
	 * It has several severity states, based on the amplifier:
	 *   >=0 - the player walked near a radioactive trace, only causing poisoning.
	 *         This is incremented each time the potion effect applies, eventually intensifying up to 5.
	 *   5   - the player destroyed a radioactive trace, also causing nausea
	 */
	public static final Potion RADIATION = new PotionGenesis(true, 0x00FF00)
			.setIcon(new ResourceLocation(Constants.MOD_ID, "textures/gui/radiation.png"))
			.setEffectiveness(0.25D)
			.setPotionName(Unlocalized.EFFECT + "radiation");

	private static ResourceLocation name(String path)
	{
		return new ResourceLocation(Constants.MOD_ID, path);
	}

	public static void register()
	{
		GameRegistry.register(RADIATION, name("radiation"));
	}
}
