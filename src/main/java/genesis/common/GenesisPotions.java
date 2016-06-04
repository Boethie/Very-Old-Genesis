package genesis.common;

import genesis.potion.PotionGenesis;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GenesisPotions
{
	public static final Potion radiation = new PotionGenesis(true, 0x00FF00)
			.setIcon(new ResourceLocation(Constants.MOD_ID, "textures/gui/radiation.png"))
			.setEffectiveness(0.25D)
			.setPotionName(Unlocalized.EFFECT + "radiation")
			.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "F6956859-8345-4763-B99B-DD5021DB8E1A", -5.0D, 0)
			.registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, "B556E666-6D13-49FA-9DB8-4F25D2566CFB", -5.0D, 0);
	
	private static ResourceLocation name(String path)
	{
		return new ResourceLocation(Constants.MOD_ID, path);
	}
	
	public static void register()
	{
		GameRegistry.register(radiation, name("radiation"));
	}
}
