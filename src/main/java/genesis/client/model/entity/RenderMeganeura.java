package genesis.client.model.entity;

import genesis.entity.living.flying.EntityMeganeura;
import genesis.util.*;
import genesis.util.render.*;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;

public class RenderMeganeura extends RenderLiving<EntityMeganeura>
{
	public static class Model extends ModelBase
	{
		public EntityPart body;
		public EntityPart bodyTop;
		
		// Head
		public EntityPart neck;
		public EntityPart mouth;
		public EntityPart face;
		public EntityPart eyeLeft;
		public EntityPart eyeRight;
		
		// Wings
		public EntityPart wingBoneLeft;
		public EntityPart wingForeLeft;
		public EntityPart wingRearLeft;
		
		public EntityPart wingBoneRight;
		public EntityPart wingForeRight;
		public EntityPart wingRearRight;
		
		// Legs
		public EntityPart legFrontLeftUpper;
		public EntityPart legFrontLeftLower;
		public EntityPart legFrontRightUpper;
		public EntityPart legFrontRightLower;
		
		public EntityPart legMiddleLeftUpper;
		public EntityPart legMiddleLeftLower;
		public EntityPart legMiddleRightUpper;
		public EntityPart legMiddleRightLower;
		
		public EntityPart legRearLeftUpper;
		public EntityPart legRearLeftLower;
		public EntityPart legRearRightUpper;
		public EntityPart legRearRightLower;
		
		// Tail
		public EntityPart tail1;
		public EntityPart tail2;
		public EntityPart tail3;
		public EntityPart drill;
		public EntityPart spikeLeft;
		public EntityPart spikeRight;
		
		public Model()
		{
			textureWidth = 64;
			textureHeight = 32;
			
			// ~~~~~~~~~~~~~~~~~~~~~~
			// ~~~~==== Body ====~~~~
			body = new EntityPart(this, 0, 0);
			body.setRotationPoint(3, 20, 0);
			body.addBox(-3, -1, -1, 5, 2, 2);
			
			// Body Top
			bodyTop = new EntityPart(this, 15, 0);
			bodyTop.addBox(-1.5F, -1.5F, -0.5F, 3, 1, 1);
			
			// ==== Head ====
			// Neck
			neck = new EntityPart(this, 47, 0);
			neck.setRotationPoint(2, 0, 0);
			neck.addBox(0, 0, -0.5F, 1, 1, 1);
			neck.setRotation(0, 0, (float) Math.toRadians(15));
			
			// Mouth
			mouth = new EntityPart(this, 47, 3);
			mouth.setRotationPoint(1.5F, 1, 0);
			mouth.addBox(-0.5F, -1, -0.5F, 1, 1, 1);
			mouth.setRotation(0, 0, (float) Math.toRadians(-10));
			
			// Face
			face = new EntityPart(this, 47, 6);
			face.setRotationPoint(0.5F, -1, 0);
			face.addBox(-1, -1, -1, 1, 1, 2);
			
			// Eyes
			float eyeRot = (float) Math.toRadians(10);
			
			eyeLeft = new EntityPart(this, 47, 10);
			eyeLeft.setRotationPoint(-1.5F, -1.5F, -1);
			eyeLeft.addBox(0, 0, 0, 1, 2, 1);
			eyeLeft.setRotation(-eyeRot, eyeRot, eyeRot);

			eyeRight = new EntityPart(this, 47, 14);
			eyeRight.setRotationPoint(-1.5F, -1.5F, 1);
			eyeRight.addBox(0, 0, -1, 1, 2, 1);
			eyeRight.setRotation(eyeRot, -eyeRot, eyeRot);
			
			// ==== Wings ====
			float frontVOff = -0.005F;
			// Left
			wingBoneLeft = new EntityPart(this);
			wingBoneLeft.setRotationPoint(0.5F, -1.5F, 0.5F);
			
			wingForeLeft = new EntityPart(this, 24, 0);
			wingForeLeft.setRotationPoint(1, frontVOff, 0);
			wingForeLeft.addElement(new ModelPlane(wingForeLeft, EnumAxis.Y, -4, 0, 0, 13, 4));
			wingForeLeft.setRotation(0, (float) Math.toRadians(20), 0);
			
			wingRearLeft = new EntityPart(this, 24, 14);
			wingRearLeft.setRotationPoint(-1, 0, 0);
			wingRearLeft.addElement(new ModelPlane(wingRearLeft, EnumAxis.Y, -4, 0, 0, 12, 4));
			wingRearLeft.setRotation(0, (float) Math.toRadians(-5), 0);
			
			// Right
			wingBoneRight = new EntityPart(this);
			wingBoneRight.setRotationPoint(0.5F, -1.5F, -0.5F);
			
			wingForeRight = new EntityPart(this, 32, 0);
			wingForeRight.setRotationPoint(1, frontVOff, 0);
			wingForeRight.addElement(new ModelPlane(wingForeRight, EnumAxis.Y, -4, 0, -13, 13, 4));
			wingForeRight.setRotation(0, (float) Math.toRadians(-20), 0);
			
			wingRearRight = new EntityPart(this, 32, 14);
			wingRearRight.setRotationPoint(-1, 0, 0);
			wingRearRight.addElement(new ModelPlane(wingRearRight, EnumAxis.Y, -4, 0, -12, 12, 4));
			wingRearRight.setRotation(0, (float) Math.toRadians(5), 0);
			
			// ==== Legs ====
			// -- Front --
			// Left
			legFrontLeftUpper = new EntityPart(this, 41, 0);
			legFrontLeftUpper.setRotationPoint(1.5F, 1, 0.5F);
			legFrontLeftUpper.addElement(new ModelPlane(legFrontLeftUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legFrontLeftUpper.setRotation((float) Math.toRadians(60), (float) Math.toRadians(-10), 0);

			legFrontLeftLower = new EntityPart(this, 41, 2);
			legFrontLeftLower.setRotationPoint(0, 2, 0);
			legFrontLeftLower.addElement(new ModelPlane(legFrontLeftLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legFrontLeftLower.setRotation((float) Math.toRadians(-80), 0, 0);
			
			// Right
			legFrontRightUpper = new EntityPart(this, 44, 0);
			legFrontRightUpper.setRotationPoint(1.5F, 1, -0.5F);
			legFrontRightUpper.addElement(new ModelPlane(legFrontRightUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legFrontRightUpper.setRotation((float) Math.toRadians(-60), (float) Math.toRadians(10), 0);

			legFrontRightLower = new EntityPart(this, 44, 2);
			legFrontRightLower.setRotationPoint(0, 2, 0);
			legFrontRightLower.addElement(new ModelPlane(legFrontRightLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legFrontRightLower.setRotation((float) Math.toRadians(80), 0, 0);
			
			// -- Middle --
			// Left
			legMiddleLeftUpper = new EntityPart(this, 41, 5);
			legMiddleLeftUpper.setRotationPoint(0.5F, 1, 0.5F);
			legMiddleLeftUpper.addElement(new ModelPlane(legMiddleLeftUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legMiddleLeftUpper.setRotation((float) Math.toRadians(60), (float) Math.toRadians(-30), 0);
			
			legMiddleLeftLower = new EntityPart(this, 41, 7);
			legMiddleLeftLower.setRotationPoint(0, 2, 0);
			legMiddleLeftLower.addElement(new ModelPlane(legMiddleLeftLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legMiddleLeftLower.setRotation((float) Math.toRadians(-80), 0, 0);

			// Right
			legMiddleRightUpper = new EntityPart(this, 44, 5);
			legMiddleRightUpper.setRotationPoint(0.5F, 1, -0.5F);
			legMiddleRightUpper.addElement(new ModelPlane(legMiddleRightUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legMiddleRightUpper.setRotation((float) Math.toRadians(-60), (float) Math.toRadians(30), 0);
			
			legMiddleRightLower = new EntityPart(this, 44, 7);
			legMiddleRightLower.setRotationPoint(0, 2, 0);
			legMiddleRightLower.addElement(new ModelPlane(legMiddleRightLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legMiddleRightLower.setRotation((float) Math.toRadians(80), 0, 0);
			
			// -- Rear --
			// Left
			legRearLeftUpper = new EntityPart(this, 41, 10);
			legRearLeftUpper.setRotationPoint(-0.5F, 1, 0.5F);
			legRearLeftUpper.addElement(new ModelPlane(legRearLeftUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legRearLeftUpper.setRotation((float) Math.toRadians(60), (float) Math.toRadians(-50), 0);
			
			legRearLeftLower = new EntityPart(this, 41, 12);
			legRearLeftLower.setRotationPoint(0, 2, 0);
			legRearLeftLower.addElement(new ModelPlane(legRearLeftLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legRearLeftLower.setRotation((float) Math.toRadians(-80), 0, 0);
			
			// Right
			legRearRightUpper = new EntityPart(this, 44, 10);
			legRearRightUpper.setRotationPoint(-0.5F, 1, -0.5F);
			legRearRightUpper.addElement(new ModelPlane(legRearRightUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legRearRightUpper.setRotation((float) Math.toRadians(-60), (float) Math.toRadians(50), 0);
			
			legRearRightLower = new EntityPart(this, 44, 12);
			legRearRightLower.setRotationPoint(0, 2, 0);
			legRearRightLower.addElement(new ModelPlane(legRearRightLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
			legRearRightLower.setRotation((float) Math.toRadians(80), 0, 0);
			
			// ==== Tail ====
			tail1 = new EntityPart(this, 0, 5);
			tail1.setRotationPoint(-3, 0, 0);
			tail1.addBox(-3, -0.5F, -0.5F, 4, 1, 1);
			tail1.setRotation(0, 0, (float) Math.toRadians(2.5));
			
			tail2 = new EntityPart(this, 0, 8);
			tail2.setRotationPoint(-3, -0.5F, 0);
			tail2.addBox(-3, 0, -0.5F, 3, 1, 1);
			tail2.setRotation(0, 0, (float) Math.toRadians(-5));
			
			tail3 = new EntityPart(this, 0, 11);
			tail3.setRotationPoint(-3, 0, 0);
			tail3.addBox(-3, 0, -0.5F, 3, 1, 1);
			tail3.setRotation(0, 0, (float) Math.toRadians(-2.5));
			
			drill = new EntityPart(this, 9, 8);
			drill.setRotationPoint(-3, 1, 0);
			drill.addElement(new ModelPlane(drill, EnumAxis.Y, -2, 0, -0.5F, 1, 2));
			
			spikeLeft = new EntityPart(this, 9, 10);
			spikeLeft.setRotationPoint(-3, 0.5F, 0.5F);
			spikeLeft.addElement(new ModelPlane(spikeLeft, EnumAxis.Z, -2, -0.5F, 0, 2, 1));
			spikeLeft.setRotation(0, (float) Math.toRadians(30), 0);
			
			spikeRight = new EntityPart(this, 9, 12);
			spikeRight.setRotationPoint(-3, 0.5F, -0.5F);
			spikeRight.addElement(new ModelPlane(spikeRight, EnumAxis.Z, -2, -0.5F, 0, 2, 1));
			spikeRight.setRotation(0, (float) Math.toRadians(-30), 0);
			
			// Head
			body.addChild(neck);
				neck.addChild(mouth);
					mouth.addChild(face);
						face.addChild(eyeLeft);
						face.addChild(eyeRight);
			
			// Wings
			body.addChild(bodyTop);
				bodyTop.addChild(wingBoneLeft);
					wingBoneLeft.addChild(wingForeLeft);
					wingBoneLeft.addChild(wingRearLeft);
				
				bodyTop.addChild(wingBoneRight);
					wingBoneRight.addChild(wingForeRight);
					wingBoneRight.addChild(wingRearRight);
			
			// Legs
			// Front
			body.addChild(legFrontLeftUpper);
				legFrontLeftUpper.addChild(legFrontLeftLower);
			
			body.addChild(legFrontRightUpper);
				legFrontRightUpper.addChild(legFrontRightLower);
			
			// Middle
			body.addChild(legMiddleLeftUpper);
				legMiddleLeftUpper.addChild(legMiddleLeftLower);
			
			body.addChild(legMiddleRightUpper);
				legMiddleRightUpper.addChild(legMiddleRightLower);
			
			// Rear
			body.addChild(legRearLeftUpper);
				legRearLeftUpper.addChild(legRearLeftLower);
			
			body.addChild(legRearRightUpper);
				legRearRightUpper.addChild(legRearRightLower);
			
			// Tail
			body.addChild(tail1);
				tail1.addChild(tail2);
					tail2.addChild(tail3);
						tail3.addChild(drill);
						tail3.addChild(spikeLeft);
						tail3.addChild(spikeRight);
			
			body.setDefaultState(true);
		}
		
		@Override
		public void render(Entity entity, float p1, float p2, float p3, float p4, float p5, float p6)
		{
			GlStateManager.enableBlend();
			//EntityMeganeura meganeura = (EntityMeganeura) entity;
			body.render(p6);
			GlStateManager.disableBlend();
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
			EntityMeganeura meganeura = (EntityMeganeura) entity;
			
			body.resetState(true);
			
			float pitch = GenesisMath.lerp(meganeura.prevRotationPitch, meganeura.rotationPitch, partialTick);
			body.rotateAngleZ += Math.toRadians(pitch);
			
			float roll = meganeura.prevRoll + (meganeura.roll - meganeura.prevRoll) * partialTick;
			body.rotateAngleX += Math.toRadians(roll);
			
			float wingSwing = GenesisMath.lerp(meganeura.prevWingSwing, meganeura.wingSwing, partialTick);
			wingBoneLeft.rotateAngleX += -wingSwing;
			wingBoneRight.rotateAngleX += wingSwing;
			
			float legAmt = GenesisMath.lerp(meganeura.prevLegPosition, meganeura.legPosition, partialTick);
			
			float legTopLand = (float) Math.toRadians(10);
			float legBotLand = (float) Math.toRadians(0);
			
			float legTopFly = (float) Math.toRadians(15);
			float legBotFly = (float) Math.toRadians(-50);
			
			float legTopRot = GenesisMath.lerp(legTopLand, legTopFly, legAmt);
			float legBotRot = GenesisMath.lerp(legBotLand, legBotFly, legAmt);
			
			// - Top -
			// Left
			legFrontLeftUpper.rotateAngleX += legTopRot;
			legMiddleLeftUpper.rotateAngleX += legTopRot;
			legRearLeftUpper.rotateAngleX += legTopRot;
			
			// Right
			legFrontRightUpper.rotateAngleX += -legTopRot;
			legMiddleRightUpper.rotateAngleX += -legTopRot;
			legRearRightUpper.rotateAngleX += -legTopRot;
			
			// - Bottom -
			// Left
			legFrontLeftLower.rotateAngleX += legBotRot;
			legMiddleLeftLower.rotateAngleX += legBotRot;
			legRearLeftLower.rotateAngleX += legBotRot;
			
			// Right
			legFrontRightLower.rotateAngleX += -legBotRot;
			legMiddleRightLower.rotateAngleX += -legBotRot;
			legRearRightLower.rotateAngleX += -legBotRot;
			
			// Laying egg animation.
			if (meganeura.getDataManager().get(EntityMeganeura.STATE) == EntityMeganeura.State.PLACING_EGG)
			{
				float eggTimer = meganeura.prevEggPlaceTimer + (meganeura.eggPlaceTimer - meganeura.prevEggPlaceTimer) * partialTick;
				eggTimer = MathHelper.clamp_float(eggTimer, 0, 1);
				
				float eggAnimation = MathHelper.sin(eggTimer * (float) Math.PI);
				
				final float transitionTime = 0.3F;
				eggAnimation *= transitionTime + 1;
				float drillAnimationAmt = Math.max(eggAnimation - transitionTime, 0);
				eggAnimation -= drillAnimationAmt;
				eggAnimation /= transitionTime;
				
				float eggAnimation2 = (float) Math.pow(eggAnimation, 2);
				
				double bodyRot1 = Math.toRadians(5);
				
				double tail1Rot1 = Math.toRadians(30);
				double tail2Rot1 = Math.toRadians(-30);
				double tail3Rot1 = Math.toRadians(-50);
				
				body.offsetY += -0.02 * eggAnimation;
				body.rotateAngleZ += bodyRot1 * eggAnimation;
				
				tail1.rotateAngleZ += tail1Rot1 * eggAnimation;
				tail2.rotateAngleZ += tail2Rot1 * eggAnimation2;
				tail3.rotateAngleZ += tail3Rot1 * eggAnimation2;
				
				if (drillAnimationAmt > 0)
				{
					drillAnimationAmt = MathHelper.sqrt_float(drillAnimationAmt);
					
					double tail1Rot2 = Math.toRadians(2.5);
					double tail2Rot2 = Math.toRadians(0);
					double tail3Rot2 = Math.toRadians(-3.5);
					tail1Rot2 *= drillAnimationAmt;
					tail2Rot2 *= drillAnimationAmt;
					tail3Rot2 *= drillAnimationAmt;
					float speed = 60;
					float drillAnimation = MathHelper.sin(eggTimer * (float) Math.PI * speed);
					
					tail1.rotateAngleZ += tail1Rot2 * drillAnimation;
					tail2.rotateAngleZ += tail2Rot2 * drillAnimation;
					tail3.rotateAngleZ += tail3Rot2 * drillAnimation;
				}
			}
		}
		
		public void setRotateAngle(EntityPart part, float x, float y, float z)
		{
			part.rotateAngleX = x;
			part.rotateAngleY = y;
			part.rotateAngleZ = z;
		}
	}
	
	public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.ASSETS_PREFIX + "textures/entity/meganeura/texture.png");
	
	public RenderMeganeura(RenderManager manager)
	{
		super(manager, new Model(), 0.4F);
	}
	
	@Override
	public void doRender(EntityMeganeura entity, double x, double y, double z, float yaw, float partialTicks)
	{
		//Minecraft.getMinecraft().getTextureManager().loadTexture(texture, new net.minecraft.client.renderer.texture.SimpleTexture(texture));
		
		super.doRender(entity, x, y, z, yaw, partialTicks);
		
		//RenderHelpers.renderEntityBounds(entity, x, y, z, partialTicks);
		
		/*EntityMeganeura meganeura = (EntityMeganeura) entity;
		
		if (meganeura.getTargetLocation() != null)
		{
			Vec3d pos = meganeura.getPositionEyes(partialTicks).subtract(0, meganeura.getEyeHeight(), 0);
			Vec3d renderPos = new Vec3d(x, y, z);
			
			Vec3d offset = renderPos.subtract(pos);
			Vec3d target = meganeura.getTargetLocation().add(offset);
			
			super.doRender(entity, target.xCoord, target.yCoord, target.zCoord, yaw, partialTicks);
		}*/
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityMeganeura entity)
	{
		return TEXTURE;
	}
}
