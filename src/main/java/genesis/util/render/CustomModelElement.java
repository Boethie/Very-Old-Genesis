package genesis.util.render;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.WorldRenderer;

public abstract class CustomModelElement extends ModelBox
{
	public CustomModelElement(ModelRenderer part)
	{
		super(part, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	@Override
	public abstract void render(WorldRenderer renderer, float scale);
}
