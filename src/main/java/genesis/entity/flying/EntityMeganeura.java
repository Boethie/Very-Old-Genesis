package genesis.entity.flying;

import java.util.*;

import genesis.util.*;
import genesis.util.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class EntityMeganeura extends EntityLiving
{
	public static final int IDLE = 17;
	
	protected double negateGravity = 0.5;
	protected double speed = 1.5;
	
	@SideOnly(Side.CLIENT)
	public float roll = 0;
	@SideOnly(Side.CLIENT)
	public float prevRoll = 0;
	
	@SideOnly(Side.CLIENT)
	public float wingSwing = 0;
	@SideOnly(Side.CLIENT)
	public float prevWingSwing = 0;
	@SideOnly(Side.CLIENT)
	protected boolean wingSwingDown = true;
	@SideOnly(Side.CLIENT)
	protected float wingSwingMax = 1;
	@SideOnly(Side.CLIENT)
	protected float wingSwingSpeed = 0.25F;
	@SideOnly(Side.CLIENT)
	protected float wingSwingEpsilon = 0.0001F;
	
	public EntityMeganeura(World world)
	{
		super(world);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		
		getDataWatcher().addObject(IDLE, (byte) 0);
	}
	
	public boolean isIdle()
	{
		return getDataWatcher().getWatchableObjectByte(IDLE) != 0;
	}
	
	public void setIdle(boolean idle)
	{
		getDataWatcher().updateObject(IDLE, idle ? (byte) 1 : (byte) 0);
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
        motionY += 0.08 * negateGravity;	// Negate some of the 0.08 gravity of EntityLivingBase.
        
        if (worldObj.isRemote)
        {
        	float diffYaw = rotationYaw - prevRotationYaw;
        	prevRoll = roll;
        	roll = MathHelper.clamp_float(diffYaw, -15, 15) / 15;
        	roll = prevRoll + (roll - prevRoll) * 0.25F;
        	
        	wingSwingMax = 0.5F;
        	wingSwingSpeed = 1F;
        	wingSwingEpsilon = 0.01F;
        	
        	float target = wingSwingMax;
        	
        	if (wingSwingDown)
        	{
        		target *= -1;
        	}
        	
        	prevWingSwing = wingSwing;
    		wingSwing += (target - wingSwing) * wingSwingSpeed;
    		float diff = target - wingSwing;
    		
    		if (diff >= -wingSwingEpsilon && diff <= wingSwingEpsilon)
    		{
    			wingSwingDown = !wingSwingDown;
    		}
        }
	}
	
	protected Vec3 targetLocation = null;
	
	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();
		
		if (targetLocation != null)
		{
			BlockPos targetPos = new BlockPos(targetLocation);
			
			if (!worldObj.isAirBlock(targetPos) || targetPos.getY() <= 0)
			{
				targetLocation = null;
			}
		}
		
		Vec3 ourPos = getPositionVector();
		
		Vec3 outMotion = new Vec3(motionX, motionY, motionZ);
		
		BlockPos ground = new BlockPos(this);
		
		while (worldObj.isAirBlock(ground))
		{
			ground = ground.down();
		}
		
		double altitude = posY - ground.getY();
		RandomDoubleRange vertRange = new RandomDoubleRange(2, 8);
		double toGroundLevel = MathHelper.clamp_double(vertRange.getRandom(rand) - altitude, -1, 1);
		
		if (outMotion.squareDistanceTo(new Vec3(0, 0, 0)) < 0.1)
		{System.out.println("starter");
			RandomDoubleRange starter = new RandomDoubleRange(-7, 7);
			targetLocation = ourPos.addVector(starter.getRandom(rand), toGroundLevel, starter.getRandom(rand));
		}
		
		if (targetLocation == null || rand.nextInt(30) == 0 || targetLocation.squareDistanceTo(ourPos) <= 1)
		{
			RandomDoubleRange horizRange = new RandomDoubleRange(-7, 7);
			
			targetLocation = ourPos.addVector(horizRange.getRandom(rand), toGroundLevel, horizRange.getRandom(rand));
		}

		//System.out.println(targetLocation);
		BlockPos targetPos = new BlockPos(targetLocation);
		
		Vec3 moveVec = targetLocation.subtract(ourPos);
		float targetYaw = (float) (Math.toDegrees(Math.atan2(moveVec.zCoord, moveVec.xCoord))) - 90;
		float diffYaw = MathHelper.wrapAngleTo180_float(targetYaw - rotationYaw);
		
		final double maneuverability = 0.5;
		final double maxTurn = 10;
		double turn = MathHelper.clamp_double(diffYaw * maneuverability, -maxTurn, maxTurn);
		rotationYaw += turn;
		
		motionY += (moveVec.yCoord - motionY) * 0.25;
		
		double rads = Math.toRadians(rotationYaw);
		double forwardX = Math.cos(rads);
		double forwardZ = Math.sin(rads);
		speed = 1.5;
		forwardX *= speed;
		forwardZ *= speed;
		//System.out.println(new Vec3(forwardX, 0, forwardZ).distanceTo(new Vec3(0, 0, 0)));
		motionX += (forwardX - motionX) * 0.9;
		motionZ += (forwardZ - motionZ) * 0.9;
		//System.out.println(new Vec3(motionX, motionY, motionZ).distanceTo(new Vec3(0, 0, 0)));
		
		//motionX = 0;
		//motionZ = 0;
		
		/*if (rand.nextInt(100) == 0 && worldObj.getBlockState(blockpos1).getBlock().isNormalCube())
		{
			setIsBatHanging(true);
		}*/
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compund)
	{
		super.readEntityFromNBT(compund);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
	}
	
	@Override
	public boolean canBePushed()
	{
		return false;
	}
	
	@Override
	protected void collideWithEntity(Entity other)
	{
		// Do nothing, this prevents the meganeura being pushed by any other entities.
	}
	
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public static class Render extends RenderLiving
	{
		public static class Model extends ModelBase
		{
		    public final EntityPart main;
		    
		    public final EntityPart body;
		    
		    public final EntityPart head;
		    public final EntityPart eyeLeft;
		    public final EntityPart eyeRight;
		    public final EntityPart headUpper;
		    public final EntityPart mouth1;
		    public final EntityPart mouth2;
		    public final EntityPart mandibleLeft;
		    public final EntityPart mandibleRight;
		    
		    public final EntityPart legFrontLeftUpper;
		    public final EntityPart legFrontLeftLower;
		    public final EntityPart legFrontLeftFoot;
		    
		    public final EntityPart legFrontRightUpper;
		    public final EntityPart legFrontRightLower;
		    public final EntityPart legFrontRightFoot;
		    
		    public final EntityPart legMiddleLeftUpper;
		    public final EntityPart legMiddleLeftLower;
		    public final EntityPart legMiddleLeftFoot;
		    
		    public final EntityPart legMiddleRightUpper;
		    public final EntityPart legMiddleRightLower;
		    public final EntityPart legMiddleRightFoot;
		    
		    public final EntityPart legRearLeftUpper;
		    public final EntityPart legRearLeftLower;
		    public final EntityPart legRearLeftFoot;
		    
		    public final EntityPart legRearRightUpper;
		    public final EntityPart legRearRightLower;
		    public final EntityPart legRearRightFoot;
		    
		    public final EntityPart wingLeft;
		    public final EntityPart wingUpperLeft;
		    public final EntityPart wingLowerLeft;

		    public final EntityPart wingRight;
		    public final EntityPart wingLowerRight;
		    public final EntityPart wingUpperRight;
		    
		    public final EntityPart tail1;
		    public final EntityPart tail2;
		    public final EntityPart tail3;
		    public final EntityPart tail4;
		    public final EntityPart tailTipLeft;
		    public final EntityPart tailTipRight;
			
			public Model()
			{
		        this.textureWidth = 180;
		        this.textureHeight = 100;
		        
		        List<EntityPart> parts = new ArrayList<EntityPart>();
		        EntityPart.addPartsTo(this, parts);
		        
		        // ~~~~~~~~~~~~~~~~~~~~~~
		        // ~~~~==== Body ====~~~~
		        this.main = new EntityPart(this);
		        this.main.setRotationPoint(0.0F, 16.0F, 0.0F);
		        
		        this.body = new EntityPart(this, 0, 0);
		        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
		        this.body.addBox(-3.0F, -3.0F, -5.0F, 6, 6, 9, 0.0F);
		        this.body.setRotation(0, (float) -Math.PI / 2, 0);
		        
		        // ==== Head ====
		        this.head = new EntityPart(this, 19, 19);
		        this.head.setRotationPoint(0.0F, 1.0F, -4.0F);
		        this.head.addBox(-1.5F, -1.5F, -5.0F, 3, 3, 5, 0.0F);
		        
		        // Eyes
		        this.eyeLeft = new EntityPart(this, 0, 30);
		        this.eyeLeft.setRotationPoint(1.0F, -1.5F, -3.4F);
		        this.eyeLeft.addBox(0.0F, -1.5F, -1.5F, 2, 3, 3, 0.0F);
		        this.setRotateAngle(eyeLeft, 0.0F, 0.5009094953223726F, 0.0F);
		        
		        this.eyeRight = new EntityPart(this, 0, 30);
		        this.eyeRight.setRotationPoint(-1.0F, -1.5F, -3.4F);
		        this.eyeRight.addBox(-2.0F, -1.5F, -1.5F, 2, 3, 3, 0.0F);
		        this.setRotateAngle(eyeRight, 0.0F, -0.5009094953223726F, 0.0F);
		        
		        // Head part
		        this.headUpper = new EntityPart(this, 0, 20);
		        this.headUpper.setRotationPoint(0.0F, -2.0F, -1.0F);
		        this.headUpper.addBox(-2.0F, -1.5F, -4.0F, 4, 3, 4, 0.0F);
		        
		        // Mouth
		        this.mouth1 = new EntityPart(this, 39, 21);
		        this.mouth1.setRotationPoint(0.0F, -1.0F, -2.9F);
		        this.mouth1.addBox(-1.5F, 0.0F, -2.0F, 3, 3, 2, 0.0F);

		        this.mouth2 = new EntityPart(this, 53, 22);
		        this.mouth2.setRotationPoint(0.0F, 3.0F, 0.0F);
		        this.mouth2.addBox(-1.0F, 0.0F, -2.0F, 2, 1, 2, 0.0F);
		        
		        this.mandibleLeft = new EntityPart(this, 65, 23);
		        this.mandibleLeft.setRotationPoint(0.5F, 0.8F, -1.0F);
		        this.mandibleLeft.addBox(-0.5F, 0.0F, -1.0F, 1, 1, 1, 0.0F);
		        this.setRotateAngle(mandibleLeft, -0.22759093446006054F, 0.0F, -0.27314402793711257F);

		        this.mandibleRight = new EntityPart(this, 65, 23);
		        this.mandibleRight.setRotationPoint(-0.5F, 0.8F, -1.0F);
		        this.mandibleRight.addBox(-0.5F, 0.0F, -1.0F, 1, 1, 1, 0.0F);
		        this.setRotateAngle(mandibleRight, -0.22759093446006054F, 0.0F, 0.27314402793711257F);
		        
		        // ==== Legs ====
		        // Front Left
		        this.legFrontLeftUpper = new EntityPart(this, 61, 0);
		        this.legFrontLeftUpper.setRotationPoint(1.4F, 2.8F, -3.8F);
		        this.legFrontLeftUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
		        this.setRotateAngle(legFrontLeftUpper, 0.0F, 0.0F, -0.36425021489121656F);
		        this.legFrontLeftLower = new EntityPart(this, 61, 10);
		        this.legFrontLeftLower.setRotationPoint(0.1F, 2.7F, 0.0F);
		        this.legFrontLeftLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
		        this.setRotateAngle(legFrontLeftLower, 0.0F, 0.0F, 0.7285004297824331F);
		        this.legFrontLeftFoot = new EntityPart(this, 61, 16);
		        this.legFrontLeftFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
		        this.legFrontLeftFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
		        this.setRotateAngle(legFrontLeftFoot, 0.0F, 0.0F, -0.36425021489121656F);
		        
		        // Front Right
		        this.legFrontRightUpper = new EntityPart(this, 61, 0);
		        this.legFrontRightUpper.setRotationPoint(-1.4F, 2.8F, -3.8F);
		        this.legFrontRightUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
		        this.setRotateAngle(legFrontRightUpper, 0.0F, 0.0F, 0.36425021489121656F);
		        this.legFrontRightLower = new EntityPart(this, 61, 10);
		        this.legFrontRightLower.setRotationPoint(-0.1F, 2.7F, 0.0F);
		        this.legFrontRightLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
		        this.setRotateAngle(legFrontRightLower, 0.0F, 0.0F, -0.7285004297824331F);
		        this.legFrontRightFoot = new EntityPart(this, 61, 16);
		        this.legFrontRightFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
		        this.legFrontRightFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
		        this.setRotateAngle(legFrontRightFoot, 0.0F, 0.0F, 0.36425021489121656F);
		        
		        // Middle Left
		        this.legMiddleLeftUpper = new EntityPart(this, 61, 0);
		        this.legMiddleLeftUpper.setRotationPoint(1.4F, 2.8F, -0.3F);
		        this.legMiddleLeftUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
		        this.setRotateAngle(legMiddleLeftUpper, 0.0F, 0.0F, -0.36425021489121656F);
		        this.legMiddleLeftLower = new EntityPart(this, 61, 10);
		        this.legMiddleLeftLower.setRotationPoint(0.1F, 2.7F, 0.0F);
		        this.legMiddleLeftLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
		        this.setRotateAngle(legMiddleLeftLower, 0.0F, 0.0F, 0.7285004297824331F);
		        this.legMiddleLeftFoot = new EntityPart(this, 61, 16);
		        this.legMiddleLeftFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
		        this.legMiddleLeftFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
		        this.setRotateAngle(legMiddleLeftFoot, 0.0F, 0.0F, -0.36425021489121656F);
		        
		        // Middle Right
		        this.legMiddleRightUpper = new EntityPart(this, 61, 0);
		        this.legMiddleRightUpper.setRotationPoint(-1.4F, 2.8F, -0.3F);
		        this.legMiddleRightUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
		        this.setRotateAngle(legMiddleRightUpper, 0.0F, 0.0F, 0.36425021489121656F);
		        this.legMiddleRightLower = new EntityPart(this, 61, 10);
		        this.legMiddleRightLower.setRotationPoint(-0.1F, 2.7F, 0.0F);
		        this.legMiddleRightLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
		        this.setRotateAngle(legMiddleRightLower, 0.0F, 0.0F, -0.7285004297824331F);
		        this.legMiddleRightFoot = new EntityPart(this, 61, 16);
		        this.legMiddleRightFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
		        this.legMiddleRightFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
		        this.setRotateAngle(legMiddleRightFoot, 0.0F, 0.0F, 0.36425021489121656F);
		        
		        // Rear Left
		        this.legRearLeftUpper = new EntityPart(this, 61, 0);
		        this.legRearLeftUpper.setRotationPoint(1.4F, 2.8F, 3.0F);
		        this.legRearLeftUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
		        this.setRotateAngle(legRearLeftUpper, 0.0F, 0.0F, -0.36425021489121656F);
		        this.legRearLeftLower = new EntityPart(this, 61, 10);
		        this.legRearLeftLower.setRotationPoint(0.1F, 2.7F, 0.0F);
		        this.legRearLeftLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
		        this.setRotateAngle(legRearLeftLower, 0.0F, 0.0F, 0.7285004297824331F);
		        this.legRearLeftFoot = new EntityPart(this, 61, 16);
		        this.legRearLeftFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
		        this.legRearLeftFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
		        this.setRotateAngle(legRearLeftFoot, 0.0F, 0.0F, -0.36425021489121656F);
		        
		        // Rear Right
		        this.legRearRightUpper = new EntityPart(this, 61, 0);
		        this.legRearRightUpper.setRotationPoint(-1.4F, 2.8F, 3.0F);
		        this.legRearRightUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
		        this.setRotateAngle(legRearRightUpper, 0.0F, 0.0F, 0.36425021489121656F);
		        this.legRearRightLower = new EntityPart(this, 61, 10);
		        this.legRearRightLower.setRotationPoint(-0.1F, 2.7F, 0.0F);
		        this.legRearRightLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
		        this.setRotateAngle(legRearRightLower, 0.0F, 0.0F, -0.7285004297824331F);
		        this.legRearRightFoot = new EntityPart(this, 61, 16);
		        this.legRearRightFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
		        this.legRearRightFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
		        this.setRotateAngle(legRearRightFoot, 0.0F, 0.0F, 0.36425021489121656F);
		        
		        // ==== Wings ====
		        // Right
		        this.wingRight = new EntityPart(this);
		        
		        this.wingUpperRight = new EntityPart(this, 76, 0);
		        this.wingUpperRight.setRotationPoint(-1.3F, -2.3F, 0.5F);
		        this.wingUpperRight.addBox(-38.0F, 0.0F, -4.0F, 38, 0, 8, 0.0F);
		        this.setRotateAngle(wingUpperRight, 0.0F, -0.31869712141416456F, 0.0F);
		        
		        this.wingLowerRight = new EntityPart(this, 73, 28);
		        this.wingLowerRight.setRotationPoint(-1.3F, -1.5F, 5.4F);
		        this.wingLowerRight.addBox(-38.0F, 0.0F, -3.0F, 38, 0, 10, 0.0F);
		        this.setRotateAngle(wingLowerRight, 0.0F, 0.091106186954104F, 0.0F);
		        
		        // Left
		        this.wingLeft = new EntityPart(this);
		        
		        this.wingUpperLeft = new EntityPart(this, 73, 28);
		        this.wingUpperLeft.setRotationPoint(1.3F, -1.5F, 5.4F);
		        this.wingUpperLeft.addBox(0.0F, 0.0F, -3.0F, 38, 0, 10, 0.0F);
		        this.setRotateAngle(wingUpperLeft, 0.0F, -0.045553093477052F, 0.0F);
		        
		        this.wingLowerLeft = new EntityPart(this, 76, 0);
		        this.wingLowerLeft.setRotationPoint(1.3F, -2.3F, 0.5F);
		        this.wingLowerLeft.addBox(0.0F, 0.0F, -4.0F, 38, 0, 8, 0.0F);
		        this.setRotateAngle(wingLowerLeft, 0.0F, 0.31869712141416456F, 0.0F);
		        
		        // ==== Tail ====
		        this.tail1 = new EntityPart(this, 0, 40);
		        this.tail1.setRotationPoint(0.0F, -0.2F, 3.6F);
		        this.tail1.addBox(-2.0F, -2.5F, 0.0F, 4, 5, 9, 0.0F);
		        this.tail2 = new EntityPart(this, 30, 40);
		        this.tail2.setRotationPoint(0.0F, 0.0F, 8.6F);
		        this.tail2.addBox(-1.5F, -2.0F, 0.0F, 3, 4, 10, 0.0F);
		        this.tail3 = new EntityPart(this, 60, 40);
		        this.tail3.setRotationPoint(0.0F, 0.0F, 9.7F);
		        this.tail3.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 11, 0.0F);
		        this.tail4 = new EntityPart(this, 90, 40);
		        this.tail4.setRotationPoint(0.0F, 0.0F, 11.0F);
		        this.tail4.addBox(-0.5F, -1.0F, 0.0F, 1, 2, 12, 0.0F);
		        this.tailTipLeft = new EntityPart(this, 25, 0);
		        this.tailTipLeft.setRotationPoint(0.0F, 0.0F, 10.0F);
		        this.tailTipLeft.addBox(0.0F, -0.5F, -0.5F, 9, 1, 1, 0.0F);
		        this.setRotateAngle(tailTipLeft, 0.0F, -1.2747884856566583F, 0.0F);
		        this.tailTipRight = new EntityPart(this, 25, 0);
		        this.tailTipRight.setRotationPoint(0.0F, 0.0F, 10.0F);
		        this.tailTipRight.addBox(-9.0F, -0.5F, -0.5F, 9, 1, 1, 0.0F);
		        this.setRotateAngle(tailTipRight, 0.0F, 1.2747884856566583F, 0.0F);
		        
		        this.main.addChild(this.body);
			        this.body.addChild(this.head);
			        	this.head.addChild(this.eyeLeft);
			        	this.head.addChild(this.eyeRight);
			        	this.head.addChild(this.headUpper);
			        		this.headUpper.addChild(this.mouth1);
			        			this.mouth1.addChild(this.mouth2);
			        				this.mouth2.addChild(this.mandibleLeft);
			        				this.mouth2.addChild(this.mandibleRight);
			        
			        // wings
			        this.body.addChild(this.wingLeft);
		        		this.wingLeft.addChild(this.wingUpperLeft);
		        		this.wingLeft.addChild(this.wingLowerLeft);
				    this.body.addChild(this.wingRight);
		        		this.wingRight.addChild(this.wingUpperRight);
		        		this.wingRight.addChild(this.wingLowerRight);
			        
			        // legs
			        this.body.addChild(this.legFrontRightUpper);
			        	this.legFrontRightUpper.addChild(this.legFrontRightLower);
			        		this.legFrontRightLower.addChild(this.legFrontRightFoot);
			        
			        this.body.addChild(this.legFrontLeftUpper);
			        	this.legFrontLeftUpper.addChild(this.legFrontLeftLower);
			        		this.legFrontLeftLower.addChild(this.legFrontLeftFoot);
			        
			        this.body.addChild(this.legMiddleRightUpper);
			        	this.legMiddleRightUpper.addChild(this.legMiddleRightLower);
			        		this.legMiddleRightLower.addChild(this.legMiddleRightFoot);
			        
			        this.body.addChild(this.legMiddleLeftUpper);
			        	this.legMiddleLeftUpper.addChild(this.legMiddleLeftLower);
			        		this.legMiddleLeftLower.addChild(this.legMiddleLeftFoot);
			        
			        this.body.addChild(this.legRearRightUpper);
			        	this.legRearRightUpper.addChild(this.legRearRightLower);
			        		this.legRearRightLower.addChild(this.legRearRightFoot);
			        
			        this.body.addChild(this.legRearLeftUpper);
			        	this.legRearLeftUpper.addChild(this.legRearLeftLower);
			        		this.legRearLeftLower.addChild(this.legRearLeftFoot);
			        
			        // tail
			        this.body.addChild(this.tail1);
			        	this.tail1.addChild(this.tail2);
			        		this.tail2.addChild(this.tail3);
			        			this.tail3.addChild(this.tail4);
			        				this.tail4.addChild(this.tailTipLeft);
			        				this.tail4.addChild(this.tailTipRight);
		        
		        EntityPart.stopAddingParts();
		        
		        for (EntityPart part : parts)
		        {
		        	part.setDefaultState();
		        }
			}
			
			@Override
			public void render(Entity entity, float p1, float p2, float p3, float p4, float p5, float p6)
			{
				EntityMeganeura meganeura = (EntityMeganeura) entity;
				
				main.render(p6);
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

				float pitch = meganeura.prevRotationPitch + (meganeura.rotationPitch - meganeura.prevRotationPitch) * partialTick;
				this.main.rotateAngleZ += pitch;
				
				float roll = meganeura.prevRoll + (meganeura.roll - meganeura.prevRoll) * partialTick;
				this.main.rotateAngleX += roll;
				
				float wingSwing = meganeura.prevWingSwing + (meganeura.wingSwing - meganeura.prevWingSwing) * partialTick;
				this.wingLeft.rotateAngleZ += -wingSwing;
				this.wingRight.rotateAngleZ += wingSwing;
				//System.out.println(wingSwing);
			}
			
		    public void setRotateAngle(EntityPart part, float x, float y, float z)
		    {
		        part.rotateAngleX = x;
		        part.rotateAngleY = y;
		        part.rotateAngleZ = z;
		    }
		}
		
		public static final ResourceLocation texture = new ResourceLocation(Constants.ASSETS_PREFIX + "textures/entity/meganeura");
		
		public Render()
		{
			super(Minecraft.getMinecraft().getRenderManager(), new Model(), 1);
		}
		
		@Override
		public void doRender(EntityLiving entity, double x, double y, double z, float yaw, float partialTicks)
		{
			mainModel = new Model();
			
			super.doRender(entity, x, y, z, yaw, partialTicks);
			
			EntityMeganeura meganeura = (EntityMeganeura) entity;
			
			/*if (meganeura.targetLocation != null)
			{
				Vec3 position = meganeura.getPositionEyes(partialTicks).subtract(0, meganeura.getEyeHeight(), 0);
				Vec3 offset = meganeura.targetLocation.subtract(position);
				
				super.doRender(entity, offset.xCoord, offset.yCoord, offset.zCoord, yaw, partialTicks);
			}*/
		}
		
		@Override
		protected ResourceLocation getEntityTexture(Entity entity)
		{
			return texture;
		}
	}
}
