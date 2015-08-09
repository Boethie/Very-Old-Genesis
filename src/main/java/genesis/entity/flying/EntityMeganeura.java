package genesis.entity.flying;

import io.netty.buffer.ByteBuf;

import java.util.*;
import java.util.concurrent.Callable;

import com.google.common.base.Function;

import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import static genesis.entity.flying.EntityMeganeura.State.*;
import genesis.util.*;
import genesis.util.render.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.*;

public class EntityMeganeura extends EntityLiving
{
	public static enum State
	{
		FLYING,
		LANDING, IDLE,
		LANDING_CALAMITES, PLACING_EGG;
	}

	public static final int STATE = 17;
	
	protected double negateGravity = 0.5;
	protected double speed = 1.5;

	public float prevEggPlaceTimer = 0;
	public float eggPlaceTimer = 0;
	protected int eggPlaceTicks = 100;
	
	@SideOnly(Side.CLIENT)
	public float roll = 0;
	@SideOnly(Side.CLIENT)
	public float prevRoll = 0;
	
	protected float wingSwingMax = 0.5F;
	@SideOnly(Side.CLIENT)
	protected float wingSwingSpeed = 1;
	@SideOnly(Side.CLIENT)
	protected float wingSwingEpsilon = 0.075F;
	@SideOnly(Side.CLIENT)
	protected float wingSwingIdleMax = 0.25F;
	@SideOnly(Side.CLIENT)
	protected float wingSwingIdleSpeed = 0.05F;
	
	@SideOnly(Side.CLIENT)
	public float wingSwing = 0;
	@SideOnly(Side.CLIENT)
	public float prevWingSwing = 0;
	@SideOnly(Side.CLIENT)
	protected boolean wingSwingDown = true;
	@SideOnly(Side.CLIENT)
	
	public EntityMeganeura(World world)
	{
		super(world);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		
		dataWatcher.addObject(STATE, (byte) IDLE.ordinal());
	}
	
	public State getState()
	{
		return State.values()[dataWatcher.getWatchableObjectByte(STATE)];
	}
	
	public void setState(State state)
	{
		dataWatcher.updateObject(STATE, (byte) state.ordinal());
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
	}
	
	protected void sendUpdateMessage()
	{
		if (!worldObj.isRemote)
		{
			Genesis.network.sendToAllTracking(new MeganeuraUpdateMessage(this), this);
		}
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		State state = getState();
		boolean idle = state == IDLE || state == PLACING_EGG;
		
		double negGrav = negateGravity;
		
		if (!idle)
		{
			motionY += 0.08 * negGrav;	// Negate some of the 0.08 gravity of EntityLivingBase.
		}
		
		if (prevEggPlaceTimer > 0)
		{
			prevEggPlaceTimer = eggPlaceTimer;
		}
		
		if (eggPlaceTimer > 0)
		{
			eggPlaceTimer = Math.max(eggPlaceTimer - (1F / eggPlaceTicks), 0);
			sendUpdateMessage();
		}
		
		if (worldObj.isRemote)
		{
			double diffX = posX - prevPosX;
			double diffY = posY - prevPosY;
			double diffZ = posZ - prevPosZ;
			
			Vec3 posDiff = new Vec3(diffX, diffY, diffZ);
			double rads = Math.toRadians(rotationYaw);
			Vec3 forward = new Vec3(Math.cos(rads), 0, Math.sin(rads));
			double dotSpeed = MathHelper.sqrt_double(posDiff.dotProduct(forward));
			double maxSpeed = speed * (2 / 3.0F);
			
			final float tiltSpeed = 0.5F;
			
			// Calculate new pitch.
			float pitchTarget = 0;
			
			if (state == PLACING_EGG)
			{
				pitchTarget = -90;
			}
			else if (state == LANDING_CALAMITES)
			{
				float amt = (float) (Math.max(maxSpeed - dotSpeed, 0) / maxSpeed);
				pitchTarget = -90 * amt;
			}
			else
			{
				double horizSpeed = MathHelper.sqrt_double((diffX * diffX + diffZ * diffZ));
				
				if (horizSpeed > 0.1)
				{
					float movePitch = (float) -Math.toDegrees(Math.atan2(diffY, horizSpeed));
					pitchTarget = movePitch;
				}
			}
			
			rotationPitch += MathHelper.wrapAngleTo180_float(pitchTarget - rotationPitch) * tiltSpeed;
			
			// Calculate new roll for banking.
			prevRoll = roll;
			
			if (idle)
			{
				roll = 0;
			}
			else
			{
				float diffYaw = rotationYaw - prevRotationYaw;
				roll = MathHelper.clamp_float(diffYaw, -15, 15) / 15;
			}
			
			roll = prevRoll + (roll - prevRoll) * tiltSpeed;
			
			// Calculate wing swing animation.
			float swingMax = wingSwingMax;
			float swingSpeed = wingSwingSpeed;
			
			float target = 0;
			
			if (idle)
			{
				swingMax = wingSwingIdleMax;
				swingSpeed = wingSwingIdleSpeed;
			}
			
			float epsilon = wingSwingEpsilon;
			target = swingMax;
			
			if (wingSwingDown)
			{
				target *= -1;
			}
			
			prevWingSwing = wingSwing;
			wingSwing += (target - wingSwing) * swingSpeed;
			float wingDiff = target - wingSwing;
			
			if (wingDiff >= -epsilon && wingDiff <= epsilon)
			{
				wingSwingDown = !wingSwingDown;
			}
		}
	}
	
	/**
	 * Overrides EntityLivingBase code which receives position updates from the server, so that it doesn't set pitch.
	 */
	@Override
	public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int increments, boolean unknown)
	{
		float oldRotationPitch = rotationPitch;
		float oldPrevRotationPitch = prevRotationPitch;
		super.setPositionAndRotation2(x, y, z, yaw, pitch, increments, unknown);
		newRotationPitch = rotationPitch = oldRotationPitch;
		prevRotationPitch = oldPrevRotationPitch;
	}
	
	public static class MeganeuraUpdateMessage implements IMessage
	{
		protected int entityID;
		protected float eggPlaceTimer;
		protected Vec3 targetLocation;
		
		public MeganeuraUpdateMessage()
		{
		}
		
		public MeganeuraUpdateMessage(int entityID, float eggPlaceTimer, Vec3 targetLocation)
		{
			this.entityID = entityID;
			this.eggPlaceTimer = eggPlaceTimer;
			this.targetLocation = targetLocation;
		}
		
		public MeganeuraUpdateMessage(EntityMeganeura entity)
		{
			this(entity.getEntityId(), entity.eggPlaceTimer, entity.targetLocation);
		}
		
		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeInt(entityID);
			
			buf.writeFloat(eggPlaceTimer);
			
			buf.writeBoolean(targetLocation != null);
			buf.writeDouble(targetLocation.xCoord);
			buf.writeDouble(targetLocation.yCoord);
			buf.writeDouble(targetLocation.zCoord);
		}
		
		@Override
		public void fromBytes(ByteBuf buf)
		{
			entityID = buf.readInt();
			
			eggPlaceTimer = buf.readFloat();
			
			if (buf.readBoolean())
			{
				targetLocation = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
			}
		}
		
		public static class Handler implements IMessageHandler<MeganeuraUpdateMessage, IMessage>
		{
			@Override
			public IMessage onMessage(final MeganeuraUpdateMessage message, final MessageContext ctx)
			{
				final Minecraft mc = Minecraft.getMinecraft();
				mc.addScheduledTask(new Runnable()
				{
					@Override
					public void run()
					{
						World world = mc.theWorld;
						Entity entity = world.getEntityByID(message.entityID);
						
						if (entity instanceof EntityMeganeura)
						{
							EntityMeganeura meganeura = (EntityMeganeura) entity;
							meganeura.eggPlaceTimer = message.eggPlaceTimer;
							meganeura.targetLocation = message.targetLocation;
						}
						else
						{
							Genesis.logger.warn("Client received a MeganeuraUpdateMessage with an entity ID that points to another type of entity.");
							
							if (entity != null)
							{
								Genesis.logger.warn("entity: " + entity + ", entityID: " + message.entityID + " (entity says " + entity.getEntityId() + ")");
							}
							else
							{
								Genesis.logger.warn("entity: " + entity + ", entityID: " + message.entityID);
							}
							
							Thread.dumpStack();
						}
					}
				});
				return null;
			}
		}
	}
	
	protected Vec3 targetLocation = null;
	
	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();
		
		Vec3 ourPos = getPositionVector();
		Vec3 ourMotion = new Vec3(motionX, motionY, motionZ);
		State ourState = getState();
		Vec3 ourOldTarget = targetLocation;
		
		if (targetLocation == null)
		{
			targetLocation = ourPos;
		}
		
		BlockPos targetPos = new BlockPos(targetLocation);
		IBlockState atTarget = worldObj.getBlockState(targetPos);
		
		boolean newTarget = false;
		double targetDistance = targetLocation.distanceTo(ourPos);
		boolean reachedClose = targetDistance <= 0.75;
		boolean reachedFar = targetDistance <= 1;
		
		ourState = getState();
		//setDead();
		
		// Update our state according to whether we've reached our destination.
		switch (ourState)
		{
		case FLYING:
			if (reachedFar)
			{
				newTarget = true;
			}

			boolean checkIdle = rand.nextInt(100) == 0;
			boolean checkCalamites = rand.nextInt(25) == 0;
			double distance = 16;
			
			if (checkCalamites)
			{
				distance = 32;
			}
			
			if (checkIdle || checkCalamites)
			{
				for (int i = 0; i < 16; i++)
				{
					RandomDoubleRange horizRange = new RandomDoubleRange(-1, 1);
					RandomDoubleRange vertRange = new RandomDoubleRange(-1, 0.25);
					Vec3 random = new Vec3(horizRange.getRandom(rand), vertRange.getRandom(rand), horizRange.getRandom(rand)).normalize();
					Vec3 to = ourPos.addVector(random.xCoord * distance, random.yCoord * distance, random.zCoord * distance);
					MovingObjectPosition hit = worldObj.rayTraceBlocks(ourPos, to, false, false, true);
					
					if (hit.typeOfHit == MovingObjectType.BLOCK)
					{
						BlockPos checkPos = hit.getBlockPos();
						BlockPos landingPos = checkPos.up();
						IBlockState checkState = worldObj.getBlockState(checkPos);
						boolean land = false;
						
						if (checkCalamites)
						{
							if (checkState.getBlock() == GenesisBlocks.calamites)
							{
								setState(LANDING_CALAMITES);
								land = true;
							}
						}
						else if (checkIdle)
						{
							if (checkState.getBlock().isSideSolid(worldObj, checkPos, EnumFacing.UP) && worldObj.isAirBlock(landingPos))
							{
								setState(LANDING);
								land = true;
							}
						}
						
						if (land)
						{
							targetLocation = new Vec3(landingPos.getX() + 0.5, landingPos.getY() + (checkCalamites ? 0.5 : 0), landingPos.getZ() + 0.5);
							break;
						}
					}
				}
			}
			
			break;
		case LANDING:
			if (onGround && reachedClose)
			{
				setState(IDLE);
			}
			break;
		case IDLE:
			if (!onGround || rand.nextInt(100) == 0)
			{
				setState(FLYING);
			}
			break;
		case LANDING_CALAMITES:
			boolean calamites = atTarget.getBlock() == GenesisBlocks.calamites;
			
			if (!reachedClose)
			{
				MovingObjectPosition hit = worldObj.rayTraceBlocks(ourPos, targetLocation, false, false, true);
				
				if (hit != null)
				{
					BlockPos hitPos = hit.getBlockPos();
					
					if (worldObj.getBlockState(hitPos).getBlock() == GenesisBlocks.calamites)
					{
						targetLocation = new Vec3(hitPos.getX() + 0.5, hitPos.getY() + 0.5, hitPos.getZ() + 0.5);
						calamites = true;
					}
				}
			}
			else if (reachedClose)
			{
				setState(PLACING_EGG);
				eggPlaceTimer = 1;
			}
			
			if (!calamites)
			{
				setState(FLYING);
			}
			break;
		case PLACING_EGG:
			boolean flyAway = worldObj.getBlockState(targetPos).getBlock() != GenesisBlocks.calamites;
			
			if (!flyAway && eggPlaceTimer <= 0)
			{
				// TODO: ADD MEGANEURA EGGS.
				flyAway = true;
			}
			
			if (flyAway)
			{
				setState(FLYING);
				eggPlaceTimer = 0;
				sendUpdateMessage();
			}
			break;
		}
		
		/*if (ourState != getState())
		{
			System.out.println(ourState + " -> " + getState());
		}*/
		
		ourState = getState();
		
		boolean slowing = ourState == LANDING || ourState == LANDING_CALAMITES;
		boolean inAir = ourState == FLYING || slowing;
		
		// If we've stopped, get us started in a different direction.
		if (!newTarget && inAir)
		{
			if (targetLocation == null)
			{
				newTarget = true;
			}
			else
			{
				MovingObjectPosition hit = worldObj.rayTraceBlocks(ourPos, targetLocation, false, false, true);
				
				if (hit != null && hit.typeOfHit == MovingObjectType.BLOCK)
				{
					if (ourState != LANDING_CALAMITES)
					{
						newTarget = true;
					}
					else if (worldObj.getBlockState(hit.getBlockPos()).getBlock() != GenesisBlocks.calamites)
					{
						newTarget = true;
					}
				}
				else if (slowing)
				{
					if (rand.nextInt(75) == 0)
					{
						newTarget = true;
					}
				}
				else
				{
					if (rand.nextInt(30) == 0)
					{
						newTarget = true;
					}
				}
			}
		}
		
		if (newTarget)
		{
			RandomDoubleRange vertRange = new RandomDoubleRange(4, 8);
			double vertMove = vertRange.getRandom(rand);
			
			if (!isInWater())
			{
				// Start finding ground level
				Vec3 to = new Vec3(posX, 0, posZ);
				MovingObjectPosition hit = worldObj.rayTraceBlocks(ourPos, to, true, false, true);
				
				double ground = 0;
				
				if (hit != null)
				{
					ground = Math.min((hit.hitVec.yCoord + 0.5) - posY, 0);
				}
				
				vertMove += ground;
				
				to = new Vec3(posX, posY + vertMove, posZ);
				hit = worldObj.rayTraceBlocks(ourPos, to, true, false, true);
				
				if (hit != null)
				{
					double ceiling = Math.max((hit.hitVec.yCoord - 0.5) - posY, 0);
					vertMove = Math.min(vertMove, ceiling);
				}
				// End finding ground level
			}
			
			RandomDoubleRange horiz = new RandomDoubleRange(-1, 1);
			Vec3 random = new Vec3(horiz.getRandom(rand), 0, horiz.getRandom(rand)).normalize();
			
			RandomDoubleRange distRange = new RandomDoubleRange(5, 10);
			double distance = distRange.getRandom(rand);
			random = new Vec3(random.xCoord * distance, random.yCoord * distance, random.zCoord * distance);
			random = random.addVector(0, vertMove, 0);
			
			targetLocation = ourPos.add(random);
			setState(FLYING);
		}
		
		double moveX = motionX, moveY = motionY, moveZ = motionZ;
		boolean strafe = false;
		
		if (ourState == PLACING_EGG)
		{
			strafe = true;
		}
		
		if (inAir || strafe)
		{
			Vec3 moveVec = targetLocation.subtract(ourPos).normalize();
			float targetYaw = (float) (Math.toDegrees(Math.atan2(moveVec.zCoord, moveVec.xCoord)));
			float diffYaw = MathHelper.wrapAngleTo180_float(targetYaw - rotationYaw);
			
			final double maneuverability = 0.75;
			final double maxTurn = 30;
			double turn = MathHelper.clamp_double(diffYaw * maneuverability, -maxTurn, maxTurn);
			rotationYaw += turn;
			
			if (strafe)
			{
				moveX = moveVec.xCoord;
				moveZ = moveVec.zCoord;
			}
			else
			{
				double rads = Math.toRadians(rotationYaw);
				moveX = Math.cos(rads);
				moveZ = Math.sin(rads);
			}
			
			moveY = moveVec.yCoord;
		}
		
		double speed = this.speed;
		
		if (slowing || !inAir)
		{
			speed = Math.min(targetLocation.distanceTo(ourPos) / 10, speed);
		}
		
		moveX *= speed;
		moveZ *= speed;
		motionX += (moveX - motionX) * 0.5;
		motionY += (moveY - motionY) * 0.5;
		motionZ += (moveZ - motionZ) * 0.5;
		
		if (ourOldTarget == null ||
			ourOldTarget.xCoord != targetLocation.xCoord ||
			ourOldTarget.yCoord != targetLocation.yCoord ||
			ourOldTarget.zCoord != targetLocation.zCoord)
		{
			sendUpdateMessage();
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		
		String state = compound.getString("state");
		if (state != null && !"".equals(state))
		{
			setState(State.valueOf(state));
		}
		
		NBTTagCompound targetComp = compound.getCompoundTag("target");
		
		if (targetComp.hasKey("x") && targetComp.hasKey("y") && targetComp.hasKey("z"))
		{
			targetLocation = new Vec3(targetComp.getDouble("x"), targetComp.getDouble("y"), targetComp.getDouble("z"));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		
		compound.setString("state", getState().toString());
		
		if (targetLocation != null)
		{
			NBTTagCompound targetComp = new NBTTagCompound();
			targetComp.setDouble("x", targetLocation.xCoord);
			targetComp.setDouble("y", targetLocation.yCoord);
			targetComp.setDouble("z", targetLocation.zCoord);
			compound.setTag("target", targetComp);
		}
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
	
	@Override
	public void fall(float distance, float damageMultiplier)
	{
		// Do nothing, prevents fall damage from occurring.
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
				textureWidth = 180;
				textureHeight = 100;
				
				// ~~~~~~~~~~~~~~~~~~~~~~
				// ~~~~==== Body ====~~~~
				main = new EntityPart(this);
				main.setRotationPoint(0.0F, 16.0F, 0.0F);
				
				body = new EntityPart(this, 0, 0);
				body.setRotationPoint(0.0F, 0.0F, 0.0F);
				body.addBox(-3.0F, -3.0F, -5.0F, 6, 6, 9, 0.0F);
				body.setRotation(0, (float) -Math.PI / 2, 0);
				body.scaleX = body.scaleY = body.scaleZ = 0.5F;
				
				// ==== Head ====
				head = new EntityPart(this, 19, 19);
				head.setRotationPoint(0.0F, 1.0F, -4.0F);
				head.addBox(-1.5F, -1.5F, -5.0F, 3, 3, 5, 0.0F);
				
				// Eyes
				eyeLeft = new EntityPart(this, 0, 30);
				eyeLeft.setRotationPoint(1.0F, -1.5F, -3.4F);
				eyeLeft.addBox(0.0F, -1.5F, -1.5F, 2, 3, 3, 0.0F);
				setRotateAngle(eyeLeft, 0.0F, 0.5009094953223726F, 0.0F);
				
				eyeRight = new EntityPart(this, 0, 30);
				eyeRight.setRotationPoint(-1.0F, -1.5F, -3.4F);
				eyeRight.addBox(-2.0F, -1.5F, -1.5F, 2, 3, 3, 0.0F);
				setRotateAngle(eyeRight, 0.0F, -0.5009094953223726F, 0.0F);
				
				// Head part
				headUpper = new EntityPart(this, 0, 20);
				headUpper.setRotationPoint(0.0F, -2.0F, -1.0F);
				headUpper.addBox(-2.0F, -1.5F, -4.0F, 4, 3, 4, 0.0F);
				
				// Mouth
				mouth1 = new EntityPart(this, 39, 21);
				mouth1.setRotationPoint(0.0F, -1.0F, -2.9F);
				mouth1.addBox(-1.5F, 0.0F, -2.0F, 3, 3, 2, 0.0F);

				mouth2 = new EntityPart(this, 53, 22);
				mouth2.setRotationPoint(0.0F, 3.0F, 0.0F);
				mouth2.addBox(-1.0F, 0.0F, -2.0F, 2, 1, 2, 0.0F);
				
				mandibleLeft = new EntityPart(this, 65, 23);
				mandibleLeft.setRotationPoint(0.5F, 0.8F, -1.0F);
				mandibleLeft.addBox(-0.5F, 0.0F, -1.0F, 1, 1, 1, 0.0F);
				setRotateAngle(mandibleLeft, -0.22759093446006054F, 0.0F, -0.27314402793711257F);

				mandibleRight = new EntityPart(this, 65, 23);
				mandibleRight.setRotationPoint(-0.5F, 0.8F, -1.0F);
				mandibleRight.addBox(-0.5F, 0.0F, -1.0F, 1, 1, 1, 0.0F);
				setRotateAngle(mandibleRight, -0.22759093446006054F, 0.0F, 0.27314402793711257F);
				
				// ==== Legs ====
				// Front Left
				legFrontLeftUpper = new EntityPart(this, 61, 0);
				legFrontLeftUpper.setRotationPoint(1.4F, 2.8F, -3.8F);
				legFrontLeftUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
				setRotateAngle(legFrontLeftUpper, 0.0F, 0.0F, -0.36425021489121656F);
				legFrontLeftLower = new EntityPart(this, 61, 10);
				legFrontLeftLower.setRotationPoint(0.1F, 2.7F, 0.0F);
				legFrontLeftLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
				setRotateAngle(legFrontLeftLower, 0.0F, 0.0F, 0.7285004297824331F);
				legFrontLeftFoot = new EntityPart(this, 61, 16);
				legFrontLeftFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
				legFrontLeftFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
				setRotateAngle(legFrontLeftFoot, 0.0F, 0.0F, -0.36425021489121656F);
				
				// Front Right
				legFrontRightUpper = new EntityPart(this, 61, 0);
				legFrontRightUpper.setRotationPoint(-1.4F, 2.8F, -3.8F);
				legFrontRightUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
				setRotateAngle(legFrontRightUpper, 0.0F, 0.0F, 0.36425021489121656F);
				legFrontRightLower = new EntityPart(this, 61, 10);
				legFrontRightLower.setRotationPoint(-0.1F, 2.7F, 0.0F);
				legFrontRightLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
				setRotateAngle(legFrontRightLower, 0.0F, 0.0F, -0.7285004297824331F);
				legFrontRightFoot = new EntityPart(this, 61, 16);
				legFrontRightFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
				legFrontRightFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
				setRotateAngle(legFrontRightFoot, 0.0F, 0.0F, 0.36425021489121656F);
				
				// Middle Left
				legMiddleLeftUpper = new EntityPart(this, 61, 0);
				legMiddleLeftUpper.setRotationPoint(1.4F, 2.8F, -0.3F);
				legMiddleLeftUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
				setRotateAngle(legMiddleLeftUpper, 0.0F, 0.0F, -0.36425021489121656F);
				legMiddleLeftLower = new EntityPart(this, 61, 10);
				legMiddleLeftLower.setRotationPoint(0.1F, 2.7F, 0.0F);
				legMiddleLeftLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
				setRotateAngle(legMiddleLeftLower, 0.0F, 0.0F, 0.7285004297824331F);
				legMiddleLeftFoot = new EntityPart(this, 61, 16);
				legMiddleLeftFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
				legMiddleLeftFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
				setRotateAngle(legMiddleLeftFoot, 0.0F, 0.0F, -0.36425021489121656F);
				
				// Middle Right
				legMiddleRightUpper = new EntityPart(this, 61, 0);
				legMiddleRightUpper.setRotationPoint(-1.4F, 2.8F, -0.3F);
				legMiddleRightUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
				setRotateAngle(legMiddleRightUpper, 0.0F, 0.0F, 0.36425021489121656F);
				legMiddleRightLower = new EntityPart(this, 61, 10);
				legMiddleRightLower.setRotationPoint(-0.1F, 2.7F, 0.0F);
				legMiddleRightLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
				setRotateAngle(legMiddleRightLower, 0.0F, 0.0F, -0.7285004297824331F);
				legMiddleRightFoot = new EntityPart(this, 61, 16);
				legMiddleRightFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
				legMiddleRightFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
				setRotateAngle(legMiddleRightFoot, 0.0F, 0.0F, 0.36425021489121656F);
				
				// Rear Left
				legRearLeftUpper = new EntityPart(this, 61, 0);
				legRearLeftUpper.setRotationPoint(1.4F, 2.8F, 3.0F);
				legRearLeftUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
				setRotateAngle(legRearLeftUpper, 0.0F, 0.0F, -0.36425021489121656F);
				legRearLeftLower = new EntityPart(this, 61, 10);
				legRearLeftLower.setRotationPoint(0.1F, 2.7F, 0.0F);
				legRearLeftLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
				setRotateAngle(legRearLeftLower, 0.0F, 0.0F, 0.7285004297824331F);
				legRearLeftFoot = new EntityPart(this, 61, 16);
				legRearLeftFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
				legRearLeftFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
				setRotateAngle(legRearLeftFoot, 0.0F, 0.0F, -0.36425021489121656F);
				
				// Rear Right
				legRearRightUpper = new EntityPart(this, 61, 0);
				legRearRightUpper.setRotationPoint(-1.4F, 2.8F, 3.0F);
				legRearRightUpper.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
				setRotateAngle(legRearRightUpper, 0.0F, 0.0F, 0.36425021489121656F);
				legRearRightLower = new EntityPart(this, 61, 10);
				legRearRightLower.setRotationPoint(-0.1F, 2.7F, 0.0F);
				legRearRightLower.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
				setRotateAngle(legRearRightLower, 0.0F, 0.0F, -0.7285004297824331F);
				legRearRightFoot = new EntityPart(this, 61, 16);
				legRearRightFoot.setRotationPoint(-0.03F, 1.8F, 0.0F);
				legRearRightFoot.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
				setRotateAngle(legRearRightFoot, 0.0F, 0.0F, 0.36425021489121656F);
				
				// ==== Wings ====
				// Right
				wingRight = new EntityPart(this);
				
				wingUpperRight = new EntityPart(this, 76, 0);
				wingUpperRight.setRotationPoint(-1.3F, -2.3F, 0.5F);
				wingUpperRight.addBox(-38.0F, 0.0F, -4.0F, 38, 0, 8, 0.0F);
				setRotateAngle(wingUpperRight, 0.0F, -0.31869712141416456F, 0.0F);
				
				wingLowerRight = new EntityPart(this, 73, 28);
				wingLowerRight.setRotationPoint(-1.3F, -1.5F, 5.4F);
				wingLowerRight.addBox(-38.0F, 0.0F, -3.0F, 38, 0, 10, 0.0F);
				setRotateAngle(wingLowerRight, 0.0F, 0.091106186954104F, 0.0F);
				
				// Left
				wingLeft = new EntityPart(this);
				
				wingUpperLeft = new EntityPart(this, 73, 28);
				wingUpperLeft.setRotationPoint(1.3F, -1.5F, 5.4F);
				wingUpperLeft.addBox(0.0F, 0.0F, -3.0F, 38, 0, 10, 0.0F);
				setRotateAngle(wingUpperLeft, 0.0F, -0.045553093477052F, 0.0F);
				
				wingLowerLeft = new EntityPart(this, 76, 0);
				wingLowerLeft.setRotationPoint(1.3F, -2.3F, 0.5F);
				wingLowerLeft.addBox(0.0F, 0.0F, -4.0F, 38, 0, 8, 0.0F);
				setRotateAngle(wingLowerLeft, 0.0F, 0.31869712141416456F, 0.0F);
				
				// ==== Tail ====
				tail1 = new EntityPart(this, 0, 40);
				tail1.setRotationPoint(0.0F, -0.2F, 3.6F);
				tail1.addBox(-2.0F, -2.5F, 0.0F, 4, 5, 9, 0.0F);
				tail2 = new EntityPart(this, 30, 40);
				tail2.setRotationPoint(0.0F, 0.0F, 8.6F);
				tail2.addBox(-1.5F, -2.0F, 0.0F, 3, 4, 10, 0.0F);
				tail3 = new EntityPart(this, 60, 40);
				tail3.setRotationPoint(0.0F, 0.0F, 9.7F);
				tail3.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 11, 0.0F);
				tail4 = new EntityPart(this, 90, 40);
				tail4.setRotationPoint(0.0F, 0.0F, 11.0F);
				tail4.addBox(-0.5F, -1.0F, 0.0F, 1, 2, 12, 0.0F);
				tailTipLeft = new EntityPart(this, 25, 0);
				tailTipLeft.setRotationPoint(0.0F, 0.0F, 10.0F);
				tailTipLeft.addBox(0.0F, -0.5F, -0.5F, 9, 1, 1, 0.0F);
				setRotateAngle(tailTipLeft, 0.0F, -1.2747884856566583F, 0.0F);
				tailTipRight = new EntityPart(this, 25, 0);
				tailTipRight.setRotationPoint(0.0F, 0.0F, 10.0F);
				tailTipRight.addBox(-9.0F, -0.5F, -0.5F, 9, 1, 1, 0.0F);
				setRotateAngle(tailTipRight, 0.0F, 1.2747884856566583F, 0.0F);
				
				main.addChild(body);
					body.addChild(head);
						head.addChild(eyeLeft);
						head.addChild(eyeRight);
						head.addChild(headUpper);
							headUpper.addChild(mouth1);
								mouth1.addChild(mouth2);
									mouth2.addChild(mandibleLeft);
									mouth2.addChild(mandibleRight);
					
					// wings
					body.addChild(wingLeft);
						wingLeft.addChild(wingUpperLeft);
						wingLeft.addChild(wingLowerLeft);
					body.addChild(wingRight);
						wingRight.addChild(wingUpperRight);
						wingRight.addChild(wingLowerRight);
					
					// legs
					body.addChild(legFrontRightUpper);
						legFrontRightUpper.addChild(legFrontRightLower);
							legFrontRightLower.addChild(legFrontRightFoot);
					
					body.addChild(legFrontLeftUpper);
						legFrontLeftUpper.addChild(legFrontLeftLower);
							legFrontLeftLower.addChild(legFrontLeftFoot);
					
					body.addChild(legMiddleRightUpper);
						legMiddleRightUpper.addChild(legMiddleRightLower);
							legMiddleRightLower.addChild(legMiddleRightFoot);
					
					body.addChild(legMiddleLeftUpper);
						legMiddleLeftUpper.addChild(legMiddleLeftLower);
							legMiddleLeftLower.addChild(legMiddleLeftFoot);
					
					body.addChild(legRearRightUpper);
						legRearRightUpper.addChild(legRearRightLower);
							legRearRightLower.addChild(legRearRightFoot);
					
					body.addChild(legRearLeftUpper);
						legRearLeftUpper.addChild(legRearLeftLower);
							legRearLeftLower.addChild(legRearLeftFoot);
					
					// tail
					body.addChild(tail1);
						tail1.addChild(tail2);
							tail2.addChild(tail3);
								tail3.addChild(tail4);
									tail4.addChild(tailTipLeft);
									tail4.addChild(tailTipRight);
				
				main.setDefaultState(true);
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
				
				main.resetState(true);
				
				float pitch = meganeura.prevRotationPitch + (meganeura.rotationPitch - meganeura.prevRotationPitch) * partialTick;
				main.rotateAngleZ += Math.toRadians(pitch);
				
				float roll = meganeura.prevRoll + (meganeura.roll - meganeura.prevRoll) * partialTick;
				main.rotateAngleX += roll;
				
				float wingSwing = meganeura.prevWingSwing + (meganeura.wingSwing - meganeura.prevWingSwing) * partialTick;
				wingLeft.rotateAngleZ += -wingSwing;
				wingRight.rotateAngleZ += wingSwing;
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
			super.doRender(entity, x, y, z, yaw, partialTicks);
			
			/*EntityMeganeura meganeura = (EntityMeganeura) entity;
			
			if (meganeura.targetLocation != null)
			{
				Vec3 pos = meganeura.getPositionEyes(partialTicks).subtract(0, meganeura.getEyeHeight(), 0);
				Vec3 renderPos = new Vec3(x, y, z);
				
				Vec3 offset = renderPos.subtract(pos);
				Vec3 target = meganeura.targetLocation.add(offset);
				
				super.doRender(entity, target.xCoord, target.yCoord, target.zCoord, yaw, partialTicks);
			}*/
		}
		
		@Override
		protected ResourceLocation getEntityTexture(Entity entity)
		{
			return texture;
		}
	}
}
