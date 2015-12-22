package genesis.entity.living;

import net.minecraft.entity.EntityLivingBase;

public interface IEntityPreferredBiome
{
	boolean shouldEntityPreferBiome(EntityLivingBase entity);
}
