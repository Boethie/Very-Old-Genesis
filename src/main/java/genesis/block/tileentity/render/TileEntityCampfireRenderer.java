package genesis.block.tileentity.render;

import genesis.block.tileentity.BlockCampfire;
import genesis.block.tileentity.TileEntityCampfire;
import genesis.client.GenesisClient;
import genesis.client.GenesisCustomModelLoader;
import genesis.common.GenesisBlocks;
import genesis.util.*;
import genesis.util.render.BlockAsEntityPart;
import genesis.util.render.ItemAsEntityPart;
import genesis.util.render.ModelHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;

public class TileEntityCampfireRenderer extends TileEntitySpecialRenderer
{
	public static final ModelResourceLocation STICK = new ModelResourceLocation("genesis:campfire_stick");
	public static final ModelResourceLocation COOKING_POT = new ModelResourceLocation("genesis:campfire_cooking_pot");
	
	public static class ModelCampfire extends ModelBase
	{
		public BlockAsEntityPart stick = new BlockAsEntityPart(this);
		public BlockAsEntityPart cookingPot = new BlockAsEntityPart(this);
		public ItemAsEntityPart stickItem = new ItemAsEntityPart(this);
		
		public ModelCampfire()
		{
			stick.offsetY = 1 + MathHelper.cos(45) * 0.0625F;
			stick.setDefaultState();
			
			stick.addChild(cookingPot);
			stick.addChild(stickItem);
		}

		public void renderAll()
		{
			stick.render(0.0625F);
		}
	};
	
	protected ModelCampfire model = new ModelCampfire();
	
	public TileEntityCampfireRenderer(BlockCampfire block)
	{
		ModelHelpers.forceModelLoading(block, STICK);
		ModelHelpers.forceModelLoading(block, COOKING_POT);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick, int destroyStage)
	{
		TileEntityCampfire campfire = (TileEntityCampfire) te;
		World world = te.getWorld();
		BlockPos pos = te.getPos();
		IBlockState state = world.getBlockState(pos);
		
		GlStateManager.pushMatrix();
		// Translate to the proper coordinates.
		GlStateManager.translate(x, y, z);
		
		EnumAxis axis = (EnumAxis) state.getValue(BlockCampfire.FACING);
		
		// Construct the proper ModelResourceLocation from STICK_LOC and the variant string.
		String properties = ModelHelpers.getPropertyString(state.getProperties());
		ModelResourceLocation stickLoc = new ModelResourceLocation(STICK.getResourceDomain() + ":" + STICK.getResourcePath(), properties);
		model.stick.setModelLocation(stickLoc, world, pos);
		
		// Clear all rotation on the model parts before setting new rotation angles.
		model.stick.resetState();
		model.cookingPot.resetState();
		model.stickItem.resetState();

		boolean hasCookingPot = campfire.hasCookingPot();
		
		float stickRot = campfire.prevRot + (campfire.rot - campfire.prevRot) * partialTick;
		model.stick.rotateAngleZ += stickRot;
		
		if (axis == EnumAxis.Z)
		{
			model.stick.rotateAngleY += 90;
		}
		
		if (hasCookingPot)
		{
			// Show only the cooking pot model.
			model.stickItem.showModel = false;
			model.cookingPot.showModel = true;
			
			ModelResourceLocation potLoc = new ModelResourceLocation(COOKING_POT.getResourceDomain() + ":" + COOKING_POT.getResourcePath(), properties);;
			model.cookingPot.setModelLocation(potLoc, world, pos);
		}
		else
		{
			ItemStack input = campfire.getInput();
			
			// Show only the impaled item.
			model.stickItem.showModel = true;
			model.cookingPot.showModel = false;
			
			// Set the stack to render on the stick.
			model.stickItem.setStack(input);
			// Reset item's transformations.
			model.stickItem.rotateAngleX += 90;
			
			if (ModelHelpers.isGeneratedItemModel(input))
			{
				// Offset the item to prevent Z-fighting.
				model.stickItem.offsetX -= 0.0001F;
				
				// Scale the item to half size.
				model.stickItem.scaleX *= 0.5F;
				model.stickItem.scaleY *= 0.5F;
				model.stickItem.scaleZ *= 0.5F;
				
				// Scale the item to be thicker, and actually appear to be impaled.
				model.stickItem.scaleZ *= 3;
			}
		}
		
		// Render the model.
		model.renderAll();
		
		GlStateManager.popMatrix();
	}
}
