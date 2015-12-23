package genesis.metadata;

import net.minecraft.potion.PotionEffect;

public interface IFood
{
	int getFoodAmount();
	float getSaturationModifier();
	Iterable<PotionEffect> getEffects();
}
