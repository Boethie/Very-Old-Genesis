package genesis.util.render;

import java.lang.reflect.Field;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class BlockAsEntityPart extends CustomEntityPart
{
	public ModelResourceLocation modelLocation;
	public IBlockAccess world;
	public BlockPos pos;
	
	public ModelResourceLocation modelLocationDef;
	public IBlockAccess worldDef;
	public BlockPos posDef;
	
	public BlockAsEntityPart(ModelBase model)
	{
		super(model);
		
		rotationPointX += 0.5F;
		rotationPointZ += 0.5F;
		setDefaultState();
	}
	
	public BlockAsEntityPart setDefaultState()
	{
		super.setDefaultState();

		modelLocationDef = modelLocation;
		worldDef = world;
		posDef = pos;
		
		return this;
	}
	
	public void resetState()
	{
		super.resetState();
		
		modelLocation = modelLocationDef;
		world = worldDef;
		pos = posDef;
	}
	
	public void setModelLocation(ModelResourceLocation modelLocation, IBlockAccess world, BlockPos pos)
	{
		this.modelLocation = modelLocation;
		this.world = world;
		this.pos = pos;
	}
	
	@Override
	public void doChildModelRender(float pxSize)
	{
		super.doChildModelRender(pxSize);
	}
	
	@Override
	public void doRender(float pxSize)
	{
		if (modelLocation != null)
		{
			float scale = pxSize * 16;
			GlStateManager.scale(scale, scale, scale);
			ModelHelpers.renderBlockModel(modelLocation, world, pos);
		}
	}
	
	@Override
	public void renderWithRotation(float pxSize)
	{
		render(pxSize);
	}
}
