package genesis.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;

public interface IMultiBakedModelOwner
{
	public boolean shouldRenderPart(IBlockState state, IBakedModel model, int part);
}
