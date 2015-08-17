package genesis.util.render;

import net.minecraft.util.ResourceLocation;

public interface ISpriteUVs
{
	public ResourceLocation getTexture();
	public float getMinU();
	public float getMinV();
	public float getMaxU();
	public float getMaxV();
}
