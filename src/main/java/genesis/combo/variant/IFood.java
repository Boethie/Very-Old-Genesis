package genesis.combo.variant;

import net.minecraft.potion.PotionEffect;

public interface IFood
{
	int getFoodAmount();
	float getSaturationModifier();
	Iterable<PotionEffect> getEffects();
}
