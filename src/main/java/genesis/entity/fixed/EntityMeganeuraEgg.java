package genesis.entity.fixed;

import org.lwjgl.opengl.GL11;

import genesis.common.GenesisBlocks;
import genesis.entity.flying.EntityMeganeura;
import genesis.util.Constants;
import genesis.util.render.EntityPart;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMeganeuraEgg extends EntityEgg
{
	public EntityMeganeuraEgg(World world)
	{
		super(world);
	}
	
	public EntityMeganeuraEgg(World world, Vec3 position)
	{
		super(world, position);
	}
	
	@Override
	protected void setMaxAge()
	{
		maxAge = 1200;
		//System.out.println(worldObj.isRemote + " spawned egg");
	}
	
	@Override
	public void spawnBaby()
	{
		EntityMeganeura meganeura = new EntityMeganeura(worldObj);
		meganeura.setPositionAndUpdate(posX, posY, posZ);
		meganeura.setState(EntityMeganeura.State.PLACING_EGG);
		worldObj.spawnEntityInWorld(meganeura);
	}

	@Override
	protected boolean isValid()
	{
		return worldObj.getBlockState(fixedTo).getBlock() == GenesisBlocks.calamites;
	}
	
	@Override
	public void setDead()
	{
		if (!isDead)
		{
			// TODO: Drop meganeura eggs as items.
		}
		
		super.setDead();
	}
	
	@SideOnly(Side.CLIENT)
	public static class EggRender extends Render
	{
		public static class Model extends ModelBase
		{
			public final EntityPart egg;
			
			public Model()
			{
				textureWidth = 180;
				textureHeight = 100;
				
				// ~~~~~~~~~~~~~~~~~~~~~~
				// ~~~~==== Body ====~~~~
				egg = new EntityPart(this);
				egg.addBox(-1, 0, -1, 2, 2, 2);
				
				egg.setDefaultState(true);
			}
			
			@Override
			public void render(Entity entity, float p1, float p2, float p3, float p4, float p5, float p6)
			{
				//EntityMeganeuraEgg meganeura = (EntityMeganeuraEgg) entity;
				
				egg.render(p6);
			}
			
			protected float partialTick = 0;
			
			@Override
			public void setLivingAnimations(EntityLivingBase entity, float p_78086_2_, float p_78086_3_, float partialTick)
			{
				this.partialTick = partialTick;
			}
			
			@Override
			public void setRotationAngles(float p1, float p2, float p3, float p4, float p5, float p6, Entity entity)
			{
				//EntityMeganeuraEgg meganeura = (EntityMeganeuraEgg) entity;
				
				egg.resetState(true);
			}
		}
		
		public static final ResourceLocation texture = new ResourceLocation(Constants.ASSETS_PREFIX + "textures/entity/meganeura_egg");
		protected Model model = new Model();
		
		public EggRender()
		{
			super(Minecraft.getMinecraft().getRenderManager());
			
	        shadowSize = 0;
		}
		
		@Override
		public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks)
		{
			model = new Model();
			
	        GlStateManager.pushMatrix();
	        bindEntityTexture(entity);
	        GlStateManager.translate(x, y, z);
			
	        model.render(entity, 0, 0, 0, 0, 0, 0.0625F);
	        
	        // Render bounding box around entity if it is being looked at.
			MovingObjectPosition lookingAt = Minecraft.getMinecraft().objectMouseOver;
			
			if (lookingAt != null && lookingAt.typeOfHit == MovingObjectType.ENTITY && lookingAt.entityHit == entity)
			{
	            GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	            GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
	            GL11.glLineWidth(2);
	            GlStateManager.disableTexture2D();
	            GlStateManager.depthMask(false);
	            float expand = entity.getCollisionBorderSize() + 0.002F;
	            
                double offX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
                double offY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
                double offZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
                RenderGlobal.drawOutlinedBoundingBox(entity.getEntityBoundingBox().expand(expand, expand, expand).offset(-offX, -offY, -offZ), -1);
                
	            GlStateManager.depthMask(true);
	            GlStateManager.enableTexture2D();
	            GlStateManager.disableBlend();
			}
			
			GlStateManager.popMatrix();
			
			super.doRender(entity, x, y, z, yaw, partialTicks);
		}
		
		@Override
		protected ResourceLocation getEntityTexture(Entity entity)
		{
			return texture;
		}
	}
}
