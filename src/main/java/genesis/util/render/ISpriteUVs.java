package genesis.util.render;

import net.minecraft.util.ResourceLocation;

public interface ISpriteUVs
{
	ResourceLocation getTexture();
	float getMinU();
	float getMinV();
	float getMaxU();
	float getMaxV();
}
