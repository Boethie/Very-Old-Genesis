package genesis.block.tileentity.render;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import genesis.block.tileentity.TileEntityRack;
import genesis.util.render.ItemAsEntityPart;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityRackRenderer extends TileEntitySpecialRenderer<TileEntityRack>
{
	//public static final ResourceLocation LID = new ResourceLocation(Constants.ASSETS_PREFIX + "rack");
	
	public static class ModelRack extends ModelBase
	{
		private static final float ITEM_OFFSET = 0.5F - (1.5F / 16);
		
		public final Map<EnumFacing, ItemAsEntityPart> items;
		
		public ModelRack()
		{
			ImmutableMap.Builder<EnumFacing, ItemAsEntityPart> builder = ImmutableMap.builder();
			
			for (EnumFacing side : EnumFacing.HORIZONTALS)
			{
				ItemAsEntityPart item = new ItemAsEntityPart(this);
				
				item.setRotation(0, side.getHorizontalAngle(), 0);
				item.setOffset(side.getFrontOffsetX() * ITEM_OFFSET,
								side.getFrontOffsetY() * ITEM_OFFSET + 0.3125F,
								side.getFrontOffsetZ() * ITEM_OFFSET);
				item.setDefaultState();
				
				builder.put(side, item);
			}
			
			items = builder.build();
		}
		
		public void renderAll()
		{
			for (ItemAsEntityPart item : items.values())
			{
				item.render(0.0625F);
			}
		}
	}
	
	protected ModelRack model = new ModelRack();
	
	public TileEntityRackRenderer()
	{
	}
	
	@Override
	public void renderTileEntityAt(TileEntityRack te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		GlStateManager.disableLighting();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y, z + 0.5);
		
		for (EnumFacing side : EnumFacing.HORIZONTALS)
		{
			ItemStack stack = te.getStackInSide(side);
			model.items.get(side).setStack(stack);
		}
		
		model.renderAll();
		
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
	}
}
