package genesis.util.render;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class BlockSpriteUVs implements ISpriteUVs
{
	protected final IBlockState state;
	
	public BlockSpriteUVs(IBlockState state)
	{
		this.state = state;
	}
	
	public BlockSpriteUVs(Block block)
	{
		this(block.getDefaultState());
	}

	protected TextureAtlasSprite sprite = null;
	
	protected void getSprite()
	{
		sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
	}
	
	@Override
	public ResourceLocation getTexture()
	{
		getSprite();
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
	
	@Override
	public float getMinU()
	{
		return sprite.getMinU();
	}
	
	@Override
	public float getMinV()
	{
		return sprite.getMinV();
	}
	
	@Override
	public float getMaxU()
	{
		return sprite.getMaxU();
	}
	
	@Override
	public float getMaxV()
	{
		return sprite.getMaxV();
	}
}
