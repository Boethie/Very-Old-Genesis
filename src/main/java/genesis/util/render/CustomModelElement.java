package genesis.util.render;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.WorldRenderer;

public abstract class CustomModelElement extends ModelBox
{
	public CustomModelElement(ModelRenderer part)
	{
		super(part, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	public CustomModelElement(ModelRenderer part, int u, int v, float x, float y, float z, int xW, int h, int zW, float expand, boolean flip)
	{
		super(part, u, v, x, y, z, xW, h, zW, expand, flip);
	}
	
	@Override
	public abstract void render(WorldRenderer renderer, float scale);
}
