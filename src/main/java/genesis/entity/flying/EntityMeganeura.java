package genesis.entity.flying;

import io.netty.buffer.ByteBuf;

import java.util.*;
import java.util.concurrent.Callable;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Function;

import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import genesis.entity.fixed.EntityMeganeuraEgg;
import static genesis.entity.flying.EntityMeganeura.State.*;
import genesis.util.*;
import genesis.util.render.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.stats.AchievementList;
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
	
	protected double speed = 1;

	public float prevEggPlaceTimer = 0;
	public float eggPlaceTimer = 0;
	public float newEggPlaceTimer = -1;
	protected int eggPlaceTicks = 100;
	
	protected boolean placedEgg = false;
	
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
	
	@Override
	protected int getExperiencePoints(EntityPlayer player)
	{
		return 1 + worldObj.rand.nextInt(3);
	}
	
	protected void dropFewItems(boolean hitRecently, int looting)
	{
		super.dropFewItems(hitRecently, looting);
		
		entityDropItem(new ItemStack(GenesisItems.meganeura), 0);
	}
	
	public State getState()
	{
		return State.values()[dataWatcher.getWatchableObjectByte(STATE)];
	}
	
	public void setState(State state)
	{
		if (getState() != FLYING && state == FLYING)
		{
			targetLocation = null;
			sendUpdateMessage();
		}
		
		dataWatcher.updateObject(STATE, (byte) state.ordinal());
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4);
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
		
		prevEggPlaceTimer = eggPlaceTimer;
		
		if (newEggPlaceTimer != -1)
		{
			eggPlaceTimer = newEggPlaceTimer;
			newEggPlaceTimer = -1;
		}
		
		if (eggPlaceTimer > 0)
		{
			eggPlaceTimer = Math.max(eggPlaceTimer - (1F / eggPlaceTicks), 0);
			sendUpdateMessage();
		}
		
		if (entityAge % 25 == 0)
		{
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
				diffYaw = rotationYawHead - prevRotationYawHead;
				diffYaw += renderYawOffset - prevRenderYawOffset;
				final float max = 15;
				roll = MathHelper.clamp_float(diffYaw, -max, max) / max * 45;
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
		public int entityID;
		public float eggPlaceTimer;
		public Vec3 position;
		public Vec3 velocity;
		public float yaw;
		public Vec3 targetLocation;
		
		public MeganeuraUpdateMessage()
		{
		}
		
		public MeganeuraUpdateMessage(int entityID, Vec3 position, Vec3 velocity, float yaw, Vec3 targetLocation, float eggPlaceTimer)
		{
			this.entityID = entityID;
			this.eggPlaceTimer = eggPlaceTimer;
			this.position = position;
			this.yaw = yaw;
			this.velocity = velocity;
			this.targetLocation = targetLocation;
		}
		
		public MeganeuraUpdateMessage(EntityMeganeura entity)
		{
			this(entity.getEntityId(), entity.getPositionVector(), new Vec3(entity.motionX, entity.motionY, entity.motionZ), entity.rotationYaw, entity.targetLocation, entity.eggPlaceTimer);
		}
		
		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeInt(entityID);
			
			buf.writeFloat(eggPlaceTimer);
			
			boolean has = position != null;
			buf.writeBoolean(has);
			
			if (has)
			{
				buf.writeDouble(position.xCoord);
				buf.writeDouble(position.yCoord);
				buf.writeDouble(position.zCoord);
			}
			
			has = velocity != null;
			buf.writeBoolean(has);
			
			if (has)
			{
				buf.writeDouble(velocity.xCoord);
				buf.writeDouble(velocity.yCoord);
				buf.writeDouble(velocity.zCoord);
			}
			
			buf.writeFloat(yaw);
			
			has = targetLocation != null;
			buf.writeBoolean(has);
			
			if (has)
			{
				buf.writeDouble(targetLocation.xCoord);
				buf.writeDouble(targetLocation.yCoord);
				buf.writeDouble(targetLocation.zCoord);
			}
		}
		
		@Override
		public void fromBytes(ByteBuf buf)
		{
			entityID = buf.readInt();
			
			eggPlaceTimer = buf.readFloat();
			
			if (buf.readBoolean())
			{
				position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
			}
			
			if (buf.readBoolean())
			{
				velocity = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
			}
			
			yaw = buf.readFloat();
			
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
							
							if (message.eggPlaceTimer == 1)	// Set the timer directly when starting it, so it doesn't interpolate.
							{
								meganeura.prevEggPlaceTimer = meganeura.eggPlaceTimer = message.eggPlaceTimer;
							}
							else
							{
								meganeura.newEggPlaceTimer = message.eggPlaceTimer;
							}
							
							Vec3 p = message.position;
							
							if (p != null)
							{
								meganeura.setPositionAndRotation2(p.xCoord, p.yCoord, p.zCoord, message.yaw, meganeura.rotationPitch, 2, false);
								meganeura.serverPosX = (int) (p.xCoord * 32);
								meganeura.serverPosY = (int) (p.yCoord * 32);
								meganeura.serverPosZ = (int) (p.zCoord * 32);
							}
							
							Vec3 v = message.velocity;
							
							if (v != null)
							{
								meganeura.setVelocity(v.xCoord, v.yCoord, v.zCoord);
							}
							
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
		
		double targetDistance = targetLocation.distanceTo(ourPos);
		boolean reachedClose = targetDistance <= 0.75;
		boolean reachedFar = targetDistance <= 1;
		
		ourState = getState();
		
		// Update our state according to whether we've reached our destination.
		switch (ourState)
		{
		case FLYING:
			if (reachedFar)
			{
				targetLocation = null;
			}

			boolean checkIdle = rand.nextInt(100) == 0;
			boolean checkCalamites = rand.nextInt(150) == 0;
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
							double y = checkCalamites ? hit.hitVec.yCoord + 0.5 : landingPos.getY();
							targetLocation = new Vec3(landingPos.getX() + 0.5, y, landingPos.getZ() + 0.5);
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
			
			if (reachedClose)
			{
				setState(PLACING_EGG);
				eggPlaceTimer = 1;
				sendUpdateMessage();
			}
			else
			{
				MovingObjectPosition hit = worldObj.rayTraceBlocks(ourPos, targetLocation, false, false, true);
				
				if (hit != null)
				{
					BlockPos hitPos = hit.getBlockPos();
					
					if (worldObj.getBlockState(hitPos).getBlock() == GenesisBlocks.calamites)
					{
						targetLocation = new Vec3(hitPos.getX() + 0.5, hit.hitVec.yCoord, hitPos.getZ() + 0.5);
						calamites = true;
					}
				}
			}
			
			if (!calamites)
			{
				setState(FLYING);
			}
			break;
		case PLACING_EGG:
			boolean flyAway = worldObj.getBlockState(targetPos).getBlock() != GenesisBlocks.calamites;
			
			if (!flyAway)
			{
				if (eggPlaceTimer <= 0)
				{
					flyAway = true;
				}
				else if (!placedEgg && eggPlaceTimer <= 0.1F)
				{
					MovingObjectPosition hit = worldObj.rayTraceBlocks(ourPos, targetLocation, false, false, true);
					
					if (hit != null)
					{
						EntityMeganeuraEgg egg = new EntityMeganeuraEgg(worldObj);
						egg.setPositionAndUpdate(hit.hitVec.xCoord, hit.hitVec.yCoord - 0.45, hit.hitVec.zCoord);
						worldObj.spawnEntityInWorld(egg);
					}
					
					placedEgg = true;
				}
			}
			
			if (flyAway)
			{
				setState(FLYING);
				eggPlaceTimer = 0;
				placedEgg = false;
				sendUpdateMessage();
			}
			break;
		}
		
		ourState = getState();
		
		boolean slowing = ourState == LANDING || ourState == LANDING_CALAMITES;
		boolean inAir = ourState == FLYING || slowing;
		
		// Get us started in a different direction if necessary.
		if (targetLocation != null && inAir)
		{
			if (ourState == FLYING && entityAge % 20 == 0 && new Vec3(motionX, motionY, motionZ).squareDistanceTo(new Vec3(0, 0, 0)) < 0.01)
			{
				targetLocation = null;
			}
			else
			{
				MovingObjectPosition hit = worldObj.rayTraceBlocks(ourPos, targetLocation, false, false, true);
				
				if (hit != null && hit.typeOfHit == MovingObjectType.BLOCK)
				{
					if (ourState != LANDING_CALAMITES)
					{
						targetLocation = null;
					}
					else if (worldObj.getBlockState(hit.getBlockPos()).getBlock() != GenesisBlocks.calamites)
					{
						targetLocation = null;
					}
				}
				else if (slowing)
				{
					if (rand.nextInt(75) == 0)
					{
						targetLocation = null;
					}
				}
				else
				{
					if (rand.nextInt(30) == 0)
					{
						targetLocation = null;
					}
				}
			}
		}
		
		if (targetLocation == null)
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
			
			setState(FLYING);
			targetLocation = ourPos.add(random);
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
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		boolean ret = super.attackEntityFrom(source, amount);
		
		if (!worldObj.isRemote && amount > 0 && ret)
		{
			setState(FLYING);
		}
		
		return ret;
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
			public EntityPart body;
			
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
				textureWidth = 180;
				textureHeight = 100;
				
				// ~~~~~~~~~~~~~~~~~~~~~~
				// ~~~~==== Body ====~~~~
				body = new EntityPart(this, 0, 0);
				body.setRotationPoint(3, 20, 0);
				body.addBox(-3, -1, -1, 5, 2, 2);
				body.addBox(-1.5F, -1.5F, -0.5F, 3, 1, 1);
				
				// ==== Head ====
				neck = new EntityPart(this, 0, 0);
				neck.setRotationPoint(2, 0, 0);
				neck.addBox(0, 0, -0.5F, 1, 1, 1);
				neck.setRotation(0, 0, (float) Math.toRadians(15));
				
				mouth = new EntityPart(this, 0, 0);
				mouth.setRotationPoint(1.5F, 1, 0);
				mouth.addBox(-0.5F, -1, -0.5F, 1, 1, 1);
				mouth.setRotation(0, 0, (float) Math.toRadians(-10));
				
				face = new EntityPart(this, 0, 0);
				face.setRotationPoint(0.5F, -1, 0);
				face.addBox(-1, -1, -1, 1, 1, 2);
				
				float eyeRot = (float) Math.toRadians(10);
				eyeLeft = new EntityPart(this, 0, 0);
				eyeLeft.setRotationPoint(-1.5F, -1.5F, -1);
				eyeLeft.addBox(0, 0, 0, 1, 2, 1);
				eyeLeft.setRotation(-eyeRot, eyeRot, eyeRot);

				eyeRight = new EntityPart(this, 0, 0);
				eyeRight.setRotationPoint(-1.5F, -1.5F, 1);
				eyeRight.addBox(0, 0, -1, 1, 2, 1);
				eyeRight.setRotation(eyeRot, -eyeRot, eyeRot);
				
				// ==== Wings ====
				// Left
				wingBoneLeft = new EntityPart(this);
				wingBoneLeft.setRotationPoint(0.5F, -1.5F, 0.5F);
				
				wingForeLeft = new EntityPart(this, 0, 0);
				wingForeLeft.setRotationPoint(1, 0, 0);
				wingForeLeft.addElement(new ModelPlane(wingForeLeft, EnumAxis.Y, -4, 0, 0, 8, 4));
				wingForeLeft.setRotation(0, (float) Math.toRadians(20), 0);
				
				wingRearLeft = new EntityPart(this, 0, 0);
				wingRearLeft.setRotationPoint(-1, 0, 0);
				wingRearLeft.addElement(new ModelPlane(wingRearLeft, EnumAxis.Y, -4, 0, 0, 8, 4));
				wingRearLeft.setRotation(0, (float) Math.toRadians(-5), 0);
				
				// Right
				wingBoneRight = new EntityPart(this);
				wingBoneRight.setRotationPoint(0.5F, -1.5F, -0.5F);
				
				wingForeRight = new EntityPart(this, 0, 0);
				wingForeRight.setRotationPoint(1, 0, 0);
				wingForeRight.addElement(new ModelPlane(wingForeRight, EnumAxis.Y, -4, 0, -8, 8, 4));
				wingForeRight.setRotation(0, (float) Math.toRadians(-20), 0);
				
				wingRearRight = new EntityPart(this, 0, 0);
				wingRearRight.setRotationPoint(-1, 0, 0);
				wingRearRight.addElement(new ModelPlane(wingRearRight, EnumAxis.Y, -4, 0, -8, 8, 4));
				wingRearRight.setRotation(0, (float) Math.toRadians(5), 0);
				
				// ==== Legs ====
				// -- Front --
				// Left
				legFrontLeftUpper = new EntityPart(this, 0, 0);
				legFrontLeftUpper.setRotationPoint(1.5F, 1, 0.5F);
				legFrontLeftUpper.addElement(new ModelPlane(legFrontLeftUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legFrontLeftUpper.setRotation((float) Math.toRadians(60), (float) Math.toRadians(-10), 0);

				legFrontLeftLower = new EntityPart(this, 0, 0);
				legFrontLeftLower.setRotationPoint(0, 2, 0);
				legFrontLeftLower.addElement(new ModelPlane(legFrontLeftLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legFrontLeftLower.setRotation((float) Math.toRadians(-80), 0, 0);
				
				// Right
				legFrontRightUpper = new EntityPart(this, 0, 0);
				legFrontRightUpper.setRotationPoint(1.5F, 1, -0.5F);
				legFrontRightUpper.addElement(new ModelPlane(legFrontRightUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legFrontRightUpper.setRotation((float) Math.toRadians(-60), (float) Math.toRadians(10), 0);

				legFrontRightLower = new EntityPart(this, 0, 0);
				legFrontRightLower.setRotationPoint(0, 2, 0);
				legFrontRightLower.addElement(new ModelPlane(legFrontRightLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legFrontRightLower.setRotation((float) Math.toRadians(80), 0, 0);
				
				// -- Middle --
				// Left
				legMiddleLeftUpper = new EntityPart(this, 0, 0);
				legMiddleLeftUpper.setRotationPoint(0.5F, 1, 0.5F);
				legMiddleLeftUpper.addElement(new ModelPlane(legMiddleLeftUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legMiddleLeftUpper.setRotation((float) Math.toRadians(60), (float) Math.toRadians(-30), 0);
				
				legMiddleLeftLower = new EntityPart(this, 0, 0);
				legMiddleLeftLower.setRotationPoint(0, 2, 0);
				legMiddleLeftLower.addElement(new ModelPlane(legMiddleLeftLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legMiddleLeftLower.setRotation((float) Math.toRadians(-80), 0, 0);

				// Right
				legMiddleRightUpper = new EntityPart(this, 0, 0);
				legMiddleRightUpper.setRotationPoint(0.5F, 1, -0.5F);
				legMiddleRightUpper.addElement(new ModelPlane(legMiddleRightUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legMiddleRightUpper.setRotation((float) Math.toRadians(-60), (float) Math.toRadians(30), 0);
				
				legMiddleRightLower = new EntityPart(this, 0, 0);
				legMiddleRightLower.setRotationPoint(0, 2, 0);
				legMiddleRightLower.addElement(new ModelPlane(legMiddleRightLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legMiddleRightLower.setRotation((float) Math.toRadians(80), 0, 0);
				
				// -- Rear --
				// Left
				legRearLeftUpper = new EntityPart(this, 0, 0);
				legRearLeftUpper.setRotationPoint(-0.5F, 1, 0.5F);
				legRearLeftUpper.addElement(new ModelPlane(legRearLeftUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legRearLeftUpper.setRotation((float) Math.toRadians(60), (float) Math.toRadians(-50), 0);
				
				legRearLeftLower = new EntityPart(this, 0, 0);
				legRearLeftLower.setRotationPoint(0, 2, 0);
				legRearLeftLower.addElement(new ModelPlane(legRearLeftLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legRearLeftLower.setRotation((float) Math.toRadians(-80), 0, 0);
				
				// Right
				legRearRightUpper = new EntityPart(this, 0, 0);
				legRearRightUpper.setRotationPoint(-0.5F, 1, -0.5F);
				legRearRightUpper.addElement(new ModelPlane(legRearRightUpper, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legRearRightUpper.setRotation((float) Math.toRadians(-60), (float) Math.toRadians(50), 0);
				
				legRearRightLower = new EntityPart(this, 0, 0);
				legRearRightLower.setRotationPoint(0, 2, 0);
				legRearRightLower.addElement(new ModelPlane(legRearRightLower, EnumAxis.Z, -0.5F, 0, 0, 1, 2));
				legRearRightLower.setRotation((float) Math.toRadians(80), 0, 0);
				
				// ==== Tail ====
				tail1 = new EntityPart(this, 0, 0);
				tail1.setRotationPoint(-3, 0, 0);
				tail1.addBox(-3, -0.5F, -0.5F, 4, 1, 1);
				tail1.setRotation(0, 0, (float) Math.toRadians(2.5));
				
				tail2 = new EntityPart(this, 0, 0);
				tail2.setRotationPoint(-3, -0.5F, 0);
				tail2.addBox(-3, 0, -0.5F, 3, 1, 1);
				tail2.setRotation(0, 0, (float) Math.toRadians(-5));
				
				tail3 = new EntityPart(this, 0, 0);
				tail3.setRotationPoint(-3, 0, 0);
				tail3.addBox(-3, 0, -0.5F, 3, 1, 1);
				tail3.setRotation(0, 0, (float) Math.toRadians(-2.5));
				
				drill = new EntityPart(this, 0, 0);
				drill.setRotationPoint(-3, 1, 0);
				drill.addElement(new ModelPlane(drill, EnumAxis.Y, -2, 0, -0.5F, 1, 2));
				
				spikeLeft = new EntityPart(this, 0, 0);
				spikeLeft.setRotationPoint(-3, 0.5F, 0.5F);
				spikeLeft.addElement(new ModelPlane(spikeLeft, EnumAxis.Z, -2, -0.5F, 0, 2, 1));
				spikeLeft.setRotation(0, (float) Math.toRadians(30), 0);
				
				spikeRight = new EntityPart(this, 0, 0);
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
				body.addChild(wingBoneLeft);
					wingBoneLeft.addChild(wingForeLeft);
					wingBoneLeft.addChild(wingRearLeft);

				body.addChild(wingBoneRight);
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
				EntityMeganeura meganeura = (EntityMeganeura) entity;
				
				body.render(p6);
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
				
				float pitch = meganeura.prevRotationPitch + (meganeura.rotationPitch - meganeura.prevRotationPitch) * partialTick;
				body.rotateAngleZ += Math.toRadians(pitch);
				
				float roll = meganeura.prevRoll + (meganeura.roll - meganeura.prevRoll) * partialTick;
				body.rotateAngleX += Math.toRadians(roll);
				
				float wingSwing = meganeura.prevWingSwing + (meganeura.wingSwing - meganeura.prevWingSwing) * partialTick;
				wingBoneLeft.rotateAngleX += -wingSwing;
				wingBoneRight.rotateAngleX += wingSwing;
				
				// Laying egg animation.
				if (meganeura.getState() == PLACING_EGG)
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
		
		public static final ResourceLocation texture = new ResourceLocation(Constants.ASSETS_PREFIX + "textures/entity/meganeura");
		
		public Render()
		{
			super(Minecraft.getMinecraft().getRenderManager(), new Model(), 0.4F);
		}
		
		@Override
		public void doRender(EntityLiving entity, double x, double y, double z, float yaw, float partialTicks)
		{
			super.doRender(entity, x, y, z, yaw, partialTicks);
			
			//RenderHelpers.renderEntityBounds(entity, x, y, z, partialTicks);
			
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
