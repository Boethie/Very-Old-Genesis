package genesis.entity.living;

import net.minecraft.entity.EntityLivingBase;

public interface IEntityPreferredBiome
{
	public boolean shouldEntityPreferBiome(EntityLivingBase entity);
}
