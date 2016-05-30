package genesis.common;

import java.awt.Color;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class GenesisPotions {
	
	public static Potion radiation; 

	public static void register()
	{
		Potion pr = new PotionRadiation();
		Potion.potionRegistry.register(Potion.potionRegistry.getKeys().size()+1, new ResourceLocation("genesis:radiation"), pr);
		radiation = pr;
	}
	
	public static class PotionRadiation extends Potion
	{
		protected PotionRadiation()
		{
			super(true, Color.GREEN.getRGB());
			this.setEffectiveness(0.25D);
			this.setPotionName("potion.radiation");
		}
		
		//TODO Add an icon(https://trello.com/c/WKzk4NGU/609-do-texture-for-status-effect-radiation)
	}
}
