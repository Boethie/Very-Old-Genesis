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
import genesis.common.GenesisBlocks;
import genesis.util.Constants;
import genesis.util.render.ModelHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiContainerKnapper extends GuiContainerBase
{
	public static final ResourceLocation KNAPPER_TEX = new ResourceLocation(Constants.ASSETS_PREFIX + "textures/gui/workbench.png");
	
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
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0);
		GlStateManager.color(1, 1, 1, 1);
		
		bindTexture(KNAPPER_TEX);
		
		if (!containerKnapper.knappingToolSlot.getHasStack())
		{
			final int toolU = 0;
			final int toolV = 31;
			final int toolW = 16;
			final int toolH = 16;
			final Slot toolSlot = containerKnapper.knappingToolSlot;
			drawTexturedModalRect(toolSlot.xDisplayPosition, toolSlot.yDisplayPosition, toolU, toolV, toolW, toolH);
		}
		
		if (!containerKnapper.outputSlotWaste.getHasStack())
		{
			final int wasteU = 16;
			final int wasteV = 31;
			final int wasteW = 16;
			final int wasteH = 16;
			final Slot wasteSlot = containerKnapper.outputSlotWaste;
			drawTexturedModalRect(wasteSlot.xDisplayPosition, wasteSlot.yDisplayPosition, wasteU, wasteV, wasteW, wasteH);
		}
		
		if (leftSlots == null)
		{
			leftSlots = new ImmutableList.Builder<Slot>()
					.addAll(containerKnapper.leftCraftingSlots)
					.add(containerKnapper.knappingInputSlot)
					.add(containerKnapper.knappingInputSlotLocked)
					.add(containerKnapper.knappingToolSlot)
					.build();
		}
		
		if (rightSlots == null)
		{
			rightSlots = new ImmutableList.Builder<Slot>()
					.addAll(containerKnapper.rightCraftingSlots)
					.add(containerKnapper.outputSlotMain)
					.add(containerKnapper.outputSlotWaste)
					.build();
		}
		
		int arrowW = 22;
		int arrowH = 15;
		int arrowU = 0;
		int arrowV = 16;
		drawTexBetweenSlots(0, 0, arrowW, arrowH, arrowU, arrowV, arrowW, arrowH, true, true, leftSlots);
		drawTexBetweenSlots(0, 0, arrowW, arrowH, arrowU, arrowV, arrowW, arrowH, true, true, rightSlots);
		
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
						bindTexture(TextureMap.locationBlocksTexture);
						
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
