package genesis.common;

import genesis.potion.PotionGenesis;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GenesisPotions
{
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
