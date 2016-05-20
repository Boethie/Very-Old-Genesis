package genesis.util.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BlockTexture
{
	public static TextureAtlasSprite getTextureFromBlock(IBlockState state)
	{
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
	}
	
	public static TextureAtlasSprite getTextureFromBlock(Block block)
	{
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
				.getTexture(block.getDefaultState());
	}
}
