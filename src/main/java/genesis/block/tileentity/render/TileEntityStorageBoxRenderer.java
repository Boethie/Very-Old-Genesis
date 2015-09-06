package genesis.block.tileentity.render;

import genesis.block.tileentity.BlockStorageBox;
import genesis.block.tileentity.TileEntityStorageBox;
import genesis.client.GenesisClient;
import genesis.common.Genesis;
import genesis.util.*;
import genesis.util.render.*;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityStorageBoxRenderer extends TileEntitySpecialRenderer
{
	public static class ModelStorageBox extends ModelBase
	{
		public BlockAsEntityPart lid = new BlockAsEntityPart(this);
		
		public ModelStorageBox()
		{
			lid.setDefaultState();
		}

		public void renderAll()
		{
			lid.render(0.0625F);
		}
	}
	
	public static final ResourceLocation LID = new ResourceLocation(Constants.ASSETS_PREFIX + "storage_box_lid");
	
	ModelStorageBox model = new ModelStorageBox();
	
	public TileEntityStorageBoxRenderer(final BlockStorageBox block)
	{
		Genesis.proxy.registerPreInitCall(new SidedFunction()
		{
			@Override
			public void client(GenesisClient client)
			{
				ModelHelpers.forceModelLoading(block, LID);
			}
		});
	}
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage)
	{
		model = new ModelStorageBox();
		
		// Translate to the proper coordinates.
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);

		// Get data about the block in the world.
		TileEntityStorageBox box = (TileEntityStorageBox) te;
		World world = box.getWorld();
		BlockPos pos = box.getPos();
		IBlockState state = world.getBlockState(pos);
		
		float px = 0.0625F;
		
		// Get animation values.
		final float rotateTime = 0.6F;
		
		float open = box.getOpenAnimation(partialTick);
		float rotateAmt = MathHelper.clamp_float(open / rotateTime, 0, 1);
		float translateAmt = MathHelper.clamp_float((open - (rotateAmt * rotateTime)) / (1 - rotateTime), 0, 1);
		translateAmt = (float) Math.pow(translateAmt, 1.5);
		
		// Reset model state.
		model.lid.resetState();
		
		// Set model state before rendering.
		String props = ModelHelpers.getPropertyString(state.getProperties());
		model.lid.setModelLocation(ModelHelpers.getLocationWithProperties(LID, props), world, pos);
		
		EnumFacing openDirection = box.getOpenDirection();
		
		float pointY = 14 * px;
		float pointHoriz = 7 * px;
		
		float rotation = rotateAmt * 90;
		float translation = (rotateAmt * px) + (translateAmt * 12 * px);
		
		model.lid.rotationPointY += pointY;
		model.lid.rotationPointX += pointHoriz * openDirection.getFrontOffsetX();
		model.lid.rotationPointZ += pointHoriz * openDirection.getFrontOffsetZ();
		
		model.lid.offsetY -= translation;
		
		switch (openDirection)
		{
		case NORTH:
			rotation *= -1;
		case SOUTH:
			model.lid.rotateAngleX += rotation;
			break;
		case EAST:
			rotation *= -1;
		case WEST:
			model.lid.rotateAngleZ += rotation;
			break;
		default:
			break;
		}
		
		// Render model.
		model.renderAll();
		
		// Pop the matrix to clear all our transforms in here.
		GlStateManager.popMatrix();
	}
}
