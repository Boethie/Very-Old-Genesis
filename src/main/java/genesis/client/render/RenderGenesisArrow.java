package genesis.client.render;

import genesis.combo.variant.ArrowTypes.ArrowType;
import genesis.common.GenesisItems;
import genesis.entity.EntityGenesisArrow;
import genesis.util.Constants;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.util.ResourceLocation;

public class RenderGenesisArrow extends RenderArrow<EntityGenesisArrow>
{
	public RenderGenesisArrow(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGenesisArrow entityGenesisArrow)
	{
		ArrowType type = GenesisItems.ARROWS.getVariant(entityGenesisArrow.getArrowStack());
		if (type == null || type.shaft == null || type.tip == null) return RenderTippedArrow.RES_ARROW;
		return new ResourceLocation(Constants.MOD_ID, "textures/entity/projectiles/arrow_" + type.shaft.name() + "_" + type.tip.name() + ".png");
	}
}
