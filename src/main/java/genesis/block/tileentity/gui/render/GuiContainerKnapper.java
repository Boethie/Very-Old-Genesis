package genesis.block.tileentity.gui.render;

import java.io.IOException;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

import genesis.block.tileentity.*;
import genesis.block.tileentity.TileEntityKnapper.KnappingState;
import genesis.block.tileentity.crafting.KnappingRecipeRegistry;
import genesis.block.tileentity.crafting.KnappingRecipeRegistry.IMaterialData;
import genesis.block.tileentity.gui.*;
import genesis.block.tileentity.gui.ContainerKnapper.SlotKnapping;
import genesis.util.Constants;
import genesis.util.render.ModelHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.util.ResourceLocation;

public class GuiContainerKnapper extends GuiContainerBase
{
	public static final ResourceLocation KNAPPER_TEX = new ResourceLocation(Constants.MOD_ID, "textures/gui/workbench.png");
	
	protected final TileEntityKnapper knapper;
	protected final ContainerKnapper containerKnapper = (ContainerKnapper) container;
	
	public GuiContainerKnapper(EntityPlayer player, TileEntityKnapper te)
	{
		super(new ContainerKnapper(player, te), te);
		
		knapper = te;
	}
	
	protected boolean isKnapping()
	{
		return containerKnapper.workbench.isKnappingEnabled();
	}
	
	protected void renderBlockTexture(IBlockState state, int x, int y)
	{
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
		drawTexturedModalRect(x, y, sprite, 16, 16);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 1);
		bindTexture(KNAPPER_TEX);
		
		Slot slotLocked = containerKnapper.knappingInputSlotLocked;
		
		if (slotLocked.getHasStack() && isPointInSlot(slotLocked, mouseX, mouseY))
		{
			GlStateManager.disableDepth();
			drawTexturedModalRect(slotLocked.xDisplayPosition, slotLocked.yDisplayPosition, 0, 0, 16, 16);
			GlStateManager.enableDepth();
		}
		
		GlStateManager.popMatrix();
	}

	protected List<Slot> leftSlots = null;
	protected List<Slot> rightSlots = null;
	
	private static final int TOOL_U = 0;
	private static final int TOOL_V = 31;
	private static final int TOOL_W = 16;
	private static final int TOOL_H = 16;
	
	private static final int ARROW_W = 22;
	private static final int ARROW_H = 15;
	private static final int ARROW_U = 0;
	private static final int ARROW_V = 16;
	
	private static final int WASTE_U = 16;
	private static final int WASTE_V = 31;
	private static final int WASTE_W = 16;
	private static final int WASTE_H = 16;
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0);
		GlStateManager.color(1, 1, 1, 1);
		
		bindTexture(KNAPPER_TEX);
		
		// Backgrounds
		
		// Tool slot
		final Slot toolSlot = containerKnapper.knappingToolSlot;
		
		if (!toolSlot.getHasStack())
			drawTexturedModalRect(toolSlot.xDisplayPosition, toolSlot.yDisplayPosition, TOOL_U, TOOL_V, TOOL_W, TOOL_H);
		
		// Damaged tool slot
		final Slot toolSlotDamaged = containerKnapper.knappingToolSlotDamaged;
		
		if (!toolSlotDamaged.getHasStack())
			drawTexturedModalRect(toolSlotDamaged.xDisplayPosition, toolSlotDamaged.yDisplayPosition, TOOL_U, TOOL_V, TOOL_W, TOOL_H);
		
		// Output waste slot
		if (!containerKnapper.outputSlotWaste.getHasStack())
		{
			final Slot wasteSlot = containerKnapper.outputSlotWaste;
			drawTexturedModalRect(wasteSlot.xDisplayPosition, wasteSlot.yDisplayPosition, WASTE_U, WASTE_V, WASTE_W, WASTE_H);
		}
		
		if (leftSlots == null)
		{
			leftSlots = new ImmutableList.Builder<Slot>()
					.addAll(containerKnapper.leftCraftingSlots)
					.add(containerKnapper.knappingInputSlotLocked)
					.add(containerKnapper.knappingToolSlotDamaged)
					.build();
			rightSlots = new ImmutableList.Builder<Slot>()
					.addAll(containerKnapper.rightCraftingSlots)
					.add(containerKnapper.outputSlotMain)
					.add(containerKnapper.outputSlotWaste)
					.build();
		}
		
		// Arrows
		drawTexBetweenSlots(0, 0, ARROW_W, ARROW_H, ARROW_U, ARROW_V, ARROW_W, ARROW_H, true, true, leftSlots);
		drawTexBetweenSlots(0, 0, ARROW_W, ARROW_H, ARROW_U, ARROW_V, ARROW_W, ARROW_H, true, true, rightSlots);
		
		if (isKnapping())
		{
			float alpha = containerKnapper.workbench.areKnappingSlotsLocked() ? 0.9F : 1;
			
			for (SlotKnapping slot : containerKnapper.craftingSlots)
			{
				KnappingState knapState = containerKnapper.workbench.getKnappingSlotState(slot.getSlotIndex());
				
				if (!knapState.isKnapped())
				{
					IMaterialData data = KnappingRecipeRegistry.getMaterialData(containerKnapper.workbench.getKnappingRecipeMaterial());
					
					if (data != null)
					{
						if (alpha < 1)
						{
							GlStateManager.color(1, 1, 1, alpha);
							GlStateManager.enableBlend();
						}
						else
						{
							GlStateManager.disableBlend();
						}
						
						drawSprite(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, data.getTexture());
					}
					
					if (knapState.getProgress() > 0)
					{
						bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
						
						GlStateManager.tryBlendFuncSeparate(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, 1, 0);
						GlStateManager.enableBlend();
						GlStateManager.color(1, 1, 1, alpha * 0.8F);
						
						TextureAtlasSprite sprite = ModelHelpers.getDestroyBlockIcon(knapState.getProgressFloat());
						drawTexturedModalRect(slot.xDisplayPosition, slot.yDisplayPosition, sprite, 16, 16);
						
						GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
					}
				}
			}
		}
		
		GlStateManager.disableBlend();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}
	
	protected boolean switchBreakingSlot(Slot slot)
	{
		int index = slot == null || !containerKnapper.craftingSlots.contains(slot) ? -1 : slot.getSlotIndex();
		return containerKnapper.workbench.switchBreakingSlot(Minecraft.getMinecraft().thePlayer, index);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException
	{
		if (button != 0 || !switchBreakingSlot(getSlot(x, y)))
		{
			super.mouseClicked(x, y, button);
		}
	}
	
	@Override
	protected void mouseClickMove(int x, int y, int button, long lastClick)
	{
		if (button != 0 || !switchBreakingSlot(getSlot(x, y)))
		{
			super.mouseClickMove(x, y, button, lastClick);
		}
	}
	
	@Override
	protected void mouseReleased(int x, int y, int button)
	{
		if (button != 0 || !switchBreakingSlot(null))
		{
			super.mouseReleased(x, y, button);
		}
	}
}
