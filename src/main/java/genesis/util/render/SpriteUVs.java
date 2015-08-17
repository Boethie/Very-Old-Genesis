package genesis.util.render;

import net.minecraft.util.ResourceLocation;

public class SpriteUVs implements ISpriteUVs
{
	protected final ResourceLocation texture;
	protected final float minU;
	protected final float minV;
	protected final float maxU;
	protected final float maxV;
	
	public SpriteUVs(ResourceLocation texture, float minU, float minV, float maxU, float maxV)
	{
		this.texture = texture;
		this.minU = minU;
		this.minV = minV;
		this.maxU = maxU;
		this.maxV = maxV;
	}
	
	@Override
	public ResourceLocation getTexture()
	{
		return texture;
	}
	
	@Override
	public float getMinU()
	{
		return minU;
	}

	@Override
	public float getMinV()
	{
		return minV;
	}

	@Override
	public float getMaxU()
	{
		return maxU;
	}

	@Override
	public float getMaxV()
	{
		return maxV;
	}
}
