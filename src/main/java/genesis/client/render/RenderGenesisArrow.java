package genesis.client.render;

import genesis.entity.EntityGenesisArrow;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.util.ResourceLocation;

public class RenderGenesisArrow extends RenderArrow<EntityGenesisArrow> {
	public RenderGenesisArrow(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGenesisArrow entityGenesisArrow) {
		// TODO: Acquire arrow textures so then we can return them, for as long as we don't have them this will return the default vanilla arrow texture
		return RenderTippedArrow.RES_ARROW;
	}
}
