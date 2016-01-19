package genesis.block.tileentity.render;

import java.util.*;

import org.lwjgl.opengl.GL11;

import genesis.block.tileentity.BlockCampfire;
import genesis.block.tileentity.TileEntityCampfire;
import genesis.client.GenesisClient;
import genesis.common.Genesis;
import genesis.util.*;
import genesis.util.render.BlockAsEntityPart;
import genesis.util.render.ItemAsEntityPart;
import genesis.util.render.ModelHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityCampfireRenderer extends TileEntitySpecialRenderer<TileEntityCampfire>
{
	public static final ModelResourceLocation FIRE = new ModelResourceLocation(Constants.ASSETS_PREFIX + "campfire_fire");
	public static final ModelResourceLocation STICK = new ModelResourceLocation(Constants.ASSETS_PREFIX + "campfire_stick");
	public static final ModelResourceLocation COOKING_POT = new ModelResourceLocation(Constants.ASSETS_PREFIX + "campfire_cooking_pot");
	public static final ModelResourceLocation COOKING_ITEM = new ModelResourceLocation(Constants.ASSETS_PREFIX + "campfire_cooking_item");
	public static final ModelResourceLocation FUEL = new ModelResourceLocation(Constants.ASSETS_PREFIX + "campfire_fuel_item");
	
	public static class ModelCampfire extends ModelBase
	{
		public BlockAsEntityPart fire = new BlockAsEntityPart(this);
		public BlockAsEntityPart fuel = new BlockAsEntityPart(this);
		public BlockAsEntityPart cookingItem = new BlockAsEntityPart(this);
		
		public BlockAsEntityPart stick = new BlockAsEntityPart(this);
		public BlockAsEntityPart cookingPot = new BlockAsEntityPart(this);
		public ItemAsEntityPart stickItem = new ItemAsEntityPart(this);
		
		public ModelCampfire()
		{
			stick.rotationPointY = 1 + MathHelper.cos((float) Math.PI / 2) * 0.0625F;
			stick.setAmbientOcclusion(false);
			stick.setDefaultState();
			
			cookingPot.setDefaultState();
			
			stick.addChild(cookingPot);
			stick.addChild(stickItem);
		}

		public void renderAll()
		{
			RenderHelper.disableStandardItemLighting();
			fire.render(0.0625F);
			RenderHelper.enableStandardItemLighting();
			
			stick.render(0.0625F);
			fuel.render(0.0625F);
			cookingItem.render(0.0625F);
		}
	}
	
	public static TileEntityCampfireRenderer INSTANCE;
	
	public static boolean hasCookingItemModel(ItemStack input)
	{
		return INSTANCE.getVariantNameForCookingItem(input) != null;
	}
	
	protected ModelCampfire model = new ModelCampfire();
	protected Set<String> fireModels;
	protected Set<String> fuelModels;
	protected Set<String> cookingItemModels;
	
	public TileEntityCampfireRenderer(final BlockCampfire block)
	{
		INSTANCE = this;
		
		Genesis.proxy.registerPreInitCall(new SidedFunction()
		{
			@Override
			public void client(GenesisClient client)
			{
				// Get defined variants of the fire model.
				Set<String> old = ModelHelpers.getBlockstatesVariants(FIRE).keySet();
				fireModels = new HashSet<String>();
				String key = "fire";
				
				for (String str : old)
				{
					if (!str.startsWith(key + "="))
					{
						throw new RuntimeException("Invalid property name in " + FUEL.toString() + " blockstates json. The property name must be \"fire\".");
					}
					
					fireModels.add(str.substring(key.length() + 1));
				}
				fireModels.add("covered");	// Force loading of actual fire models so that attempting to render it doesn't crash the game.
				fireModels.add("uncovered");
				ModelHelpers.forceModelLoading(key, fireModels, FIRE);
				
				// Force loading of fuel models.
				old = ModelHelpers.getBlockstatesVariants(FUEL).keySet();
				fuelModels = new HashSet<String>();
				key = "item";
				
				for (String str : old)
				{
					if (!str.startsWith(key + "="))
					{
						throw new RuntimeException("Invalid property name in " + FUEL.toString() + " blockstates json.");
					}
					
					fuelModels.add(str.substring(key.length() + 1));
				}
				
				ModelHelpers.forceModelLoading(key, fuelModels, FUEL);
				
				// Force loading of cooking item models.
				old = ModelHelpers.getBlockstatesVariants(COOKING_ITEM).keySet();
				cookingItemModels = new HashSet<String>();
				
				for (String str : old)
				{
					if (!str.startsWith(key + "="))
					{
						throw new RuntimeException("Invalid property name in " + FUEL.toString() + " blockstates json.");
					}
					
					cookingItemModels.add(str.substring(key.length() + 1));
				}
				
				ModelHelpers.forceModelLoading(key, cookingItemModels, COOKING_ITEM);
				
				ModelHelpers.forceModelLoading(block, STICK);
				ModelHelpers.forceModelLoading(block, COOKING_POT);
			}
		});
	}
	
	public String getVariantNameForCookingItem(ItemStack input)
	{
		if (input == null)
			return null;
		
		ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
		
		if (output == null)
			return null;
		
		String[] fallbacks = output.getItem() == Items.coal ?
				new String[]{"generic_wood"} : new String[]{};
		
		return ModelHelpers.getStringIDInSetForStack(input, cookingItemModels, fallbacks);
	}
	
	@Override
	public void renderTileEntityAt(TileEntityCampfire campfire, double x, double y, double z, float partialTick, int destroyStage)
	{
		// Translate to the proper coordinates.
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		// Get data about the block in the world.
		World world = campfire.getWorld();
		BlockPos pos = campfire.getPos();
		IBlockState state = world.getBlockState(pos);
		
		if (!(state.getBlock() instanceof BlockCampfire))
		{
			return;
		}
		
		EnumAxis axis = state.getValue(BlockCampfire.FACING);
		
		// Clear all rotation on the model parts before setting new rotation angles.
		model.fire.resetState();
		model.stick.resetState();
		model.cookingPot.resetState();
		model.stickItem.resetState();
		model.cookingItem.resetState();
		model.fuel.resetState();
		
		// Construct the proper ModelResourceLocation from STICK_LOC and the variant string.
		String properties = ModelHelpers.getPropertyString(state);
		ModelResourceLocation stickLoc = ModelHelpers.getLocationWithProperties(STICK, properties);
		model.stick.setModel(stickLoc, world, pos);
		
		float stickRot = campfire.prevRot + (campfire.rot - campfire.prevRot) * partialTick;
		model.stick.rotateAngleZ += stickRot;
		
		if (axis == EnumAxis.Z)
		{
			model.stick.rotateAngleY += 90;
			model.cookingItem.rotateAngleY += 90;
		}
		
		boolean burning = campfire.isBurning();
		
		// Set fire model location.
		if (burning)
			model.fire.setModel(ModelHelpers.getLocationWithProperties(FIRE, "fire=uncovered"), world, pos);
		else if (campfire.isWet())
		{
			if (fireModels.contains("wet"))
				model.fire.setModel(ModelHelpers.getLocationWithProperties(FIRE, "fire=wet"), world, pos);
		}
		else
		{
			if (fireModels.contains("none"))
				model.fire.setModel(ModelHelpers.getLocationWithProperties(FIRE, "fire=none"), world, pos);
		}
		
		if (campfire.hasCookingPot())
		{
			// Show only the cooking pot model.
			model.stickItem.showModel = false;
			model.cookingPot.showModel = true;
			
			model.cookingPot.setModel(ModelHelpers.getLocationWithProperties(COOKING_POT, properties), world, pos);
		}
		else
		{
			ItemStack input = campfire.getInput();
			
			if (input != null)
			{
				String itemID = getVariantNameForCookingItem(input);
				
				if (itemID != null)
				{
					// Change fire model to a "covered" version so that it doesn't clip through the cooking item.
					if (burning)
					{
						model.fire.setModel(ModelHelpers.getLocationWithProperties(FIRE, "fire=covered"), world, pos);
					}
					
					model.cookingItem.setModel(ModelHelpers.getLocationWithProperties(COOKING_ITEM, "item=" + itemID), world, pos);
				}
				else
				{
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
			}
		}
		
		// Render a fuel model in the campfire (with possibility for custom models for individual items).
		// Will try to fall back to variant "item=generic_fuel" if no model definition is found.
		ItemStack fuel = campfire.getFuel();
		
		if (fuel != null)
		{
			String itemID = ModelHelpers.getStringIDInSetForStack(fuel, fuelModels, "generic_fuel");
			
			if (itemID != null)
			{
				model.fuel.setModel(ModelHelpers.getLocationWithProperties(FUEL, "item=" + itemID), world, pos);
			}
		}
		
		ModelHelpers.bindAtlasTexture();
		
		if (destroyStage >= 0)
		{
			Tessellator tess = Tessellator.getInstance();
			WorldRenderer wr = tess.getWorldRenderer();
			
			RenderHelper.disableStandardItemLighting();
			Minecraft.getMinecraft().entityRenderer.disableLightmap();
			
			TextureAtlasSprite breakTexture = ModelHelpers.getDestroyBlockIcon(destroyStage);
			
			
			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			wr.noColor();
			wr.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
			ModelHelpers.getBlockRenderer().renderModelStandard(world,
					ModelHelpers.getCubeProjectedBakedModel(ModelHelpers.getBakedBlockModel(state, world, pos), breakTexture),
					state.getBlock(), pos, wr, true);
			wr.setTranslation(0, 0, 0);
			GlStateManager.color(1, 1, 1, 0.0F);
			tess.draw();
			
			model.fire.setTextureNoColor(breakTexture);
			model.fuel.setTextureNoColor(breakTexture);
			model.cookingItem.setTextureNoColor(breakTexture);
			model.stick.setTextureNoColor(breakTexture);
			model.cookingPot.setTextureNoColor(breakTexture);
		}
		
		// Render the model.
		model.renderAll();
		
		if (destroyStage >= 0)
		{
			RenderHelper.enableStandardItemLighting();
			Minecraft.getMinecraft().entityRenderer.enableLightmap();
		}
		
		GlStateManager.popMatrix();
	}
}
