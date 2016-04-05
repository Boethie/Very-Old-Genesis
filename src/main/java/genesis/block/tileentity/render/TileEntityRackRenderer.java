package genesis.block.tileentity.render;

import genesis.block.BlockRack;
import genesis.block.tileentity.BlockStorageBox;
import genesis.block.tileentity.TileEntityRack;
import genesis.common.Genesis;
import genesis.util.Constants;
import genesis.util.render.ModelHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TileEntityRackRenderer extends TileEntitySpecialRenderer<TileEntityRack>
{

    public static final ResourceLocation LID = new ResourceLocation(Constants.ASSETS_PREFIX + "rack");

    public TileEntityRackRenderer(final BlockRack block)
    {
        Genesis.proxy.clientPreInitCall((c) -> ModelHelpers.forceModelLoading(block, LID));
    }

    @Override
    public void renderTileEntityAt(TileEntityRack te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (te.getStackInSlot(0) != null)
        {
            ItemStack item = te.getStackInSlot(0).copy();

            float rot = 0.0F;

            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            float scale = 2F;

            float pixel = 1.0F / 16;

            switch (te.getDirection())
            {
                case 0:
                    GlStateManager.translate(x + (24 * pixel), y + (24 * pixel), z + (2.5 * pixel));
                    GlStateManager.rotate(-180, 0, 0, 1);
                    GlStateManager.rotate(rot, 1, 1, 0);
                    GlStateManager.translate(1, 0, 0);
                    break;
                case 1:
                    GlStateManager.translate(x + (13.5 * pixel), y + (8 * pixel), z + (8 * pixel));
                    GlStateManager.rotate(-180, 1, 0, 1);
                    GlStateManager.rotate(rot, 1, 1, 0);
                    GlStateManager.translate(0, -1, 0);
                    break;
                case 2:
                    GlStateManager.translate(x + (24 * pixel), y + (24 * pixel), z + (13.5 * pixel));
                    GlStateManager.rotate(-180, 0, 0, 1);
                    GlStateManager.rotate(rot, 1, 1, 0);
                    GlStateManager.translate(1, 0, 0);
                    break;
                case 3:
                    GlStateManager.translate(x + (2.5 * pixel), y + (8 * pixel), z + (8 * pixel));
                    GlStateManager.rotate(-180, 1, 0, 1);
                    GlStateManager.rotate(rot, 1, 1, 0);
                    GlStateManager.translate(0, -1, 0);
                    break;
            }

            GlStateManager.scale(scale, scale, scale);

            EntityItem entityItem = new EntityItem(Minecraft.getMinecraft().theWorld, 0D, 0D, 0D, item);
            entityItem.hoverStart = 0.0F;

            Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityItem, 0.0D, 0.0D, 0.0D, 0F, 0.0F,
                    true);

            GlStateManager.enableLighting();

            GlStateManager.popMatrix();
        }
    }
}
