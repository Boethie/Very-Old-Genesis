package genesis.entity.living.flying;

import io.netty.buffer.ByteBuf;

import java.util.*;

import genesis.client.sound.MovingEntitySound;
import genesis.client.sound.MovingEntitySound.IMovingEntitySoundOwner;
import genesis.combo.variant.EnumFood;
import genesis.common.*;
import genesis.common.sounds.GenesisSoundEvents;
import genesis.entity.fixed.EntityMeganeuraEgg;
import genesis.entity.living.IEntityPreferredBiome;
import genesis.util.*;
import genesis.util.random.d.DoubleRange;
import genesis.util.sound.SoundUtils;

import static genesis.entity.living.flying.EntityMeganeura.State.*;
import static genesis.entity.living.flying.EntityMeganeura.StateCategory.*;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.*;
import net.minecraft.network.datasync.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import net.minecraftforge.fml.common.network.simpleimpl.*;

public class EntityMeganeura extends EntityLiving implements IMovingEntitySoundOwner
{
	public enum StateCategory
	{
		AIR,
		SLOW,
		LANDED;
	}
	
	public enum State
	{
		NONE(null),
		FLYING(AIR),
		LANDING_GROUND(SLOW), LANDING_SIDE(SLOW),
		IDLE_GROUND(LANDED), IDLE_SIDE(LANDED, true), PLACING_EGG(LANDED, true);
		
		public final StateCategory category;
		public final boolean onSide;
		
		State(StateCategory category, boolean onSide)
		{
			this.category = category;
			this.onSide = onSide;
		}
		
		State(StateCategory category)
		{
			this(category, false);
		}
	}
	
	public static final int SPAWN_LIMIT = 2;
	
	public static int getChunkMeganeuraCount(World world, Vec3d pos)
	{
		double radius = 8;
		AxisAlignedBB bb = new AxisAlignedBB(pos.xCoord, pos.yCoord, pos.zCoord, pos.xCoord, pos.yCoord, pos.zCoord).expand(radius, radius, radius);
		List<EntityMeganeura> meganeura = world.getEntitiesWithinAABB(EntityMeganeura.class, bb);
		List<EntityMeganeuraEgg> eggs = world.getEntitiesWithinAABB(EntityMeganeuraEgg.class, bb);
		
		return meganeura.size() + eggs.size();
	}
	
	public static boolean canSpawnMeganeura(World world, Vec3d pos)
	{
		return getChunkMeganeuraCount(world, pos) < SPAWN_LIMIT;
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		return this.posY > 60;
	}
	
	public static final DataParameter<State> STATE =
			EntityDataManager.createKey(EntityMeganeura.class, GenesisDataSerializers.createEnum(State.class));
	
	protected double speed = 1;
	
	private Vec3d targetLocation;
	
	protected int takeoffSoundTimer = 0;

	public float prevEggPlaceTimer = 0;
	public float eggPlaceTimer = 0;
	public float newEggPlaceTimer = -1;
	protected int eggPlaceTicks = 100;
	
	protected boolean placedEgg = false;
	
	public float roll = 0;
	public float prevRoll = 0;
	
	protected float wingSwingMax = 0.5F;
	protected float wingSwingSpeed = 1;
	protected float wingSwingEpsilon = 0.075F;
	protected float wingSwingIdleMax = 0.25F;
	protected float wingSwingIdleSpeed = 0.05F;
	
	public float wingSwing = 0;
	public float prevWingSwing = 0;
	protected boolean wingSwingDown = true;

	public float prevLegPosition = 0;
	public float legPosition = 0;
	
	protected float dopplerPitch = 1;
	
	public EntityMeganeura(World world)
	{
		super(world);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		
		dataManager.register(STATE, NONE);
	}
	
	@Override
	public AxisAlignedBB getEntityBoundingBox()
	{
		setSize(0.75F, 0.5F);
		return super.getEntityBoundingBox();
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer player)
	{
		return 1 + worldObj.rand.nextInt(3);
	}
	
	@Override
	protected void dropFewItems(boolean hitRecently, int looting)
	{
		super.dropFewItems(hitRecently, looting);
		
		entityDropItem(GenesisItems.foods.getRawStack(EnumFood.MEGANEURA), 0);
	}
	
	public void setState(State state)
	{
		if (!isDead)
		{
			State oldState = dataManager.get(STATE);
			
			if (oldState != FLYING && state == FLYING)
			{
				setTargetLocation(null);
				sendUpdateMessage();
			}
			
			if ((oldState == NONE || oldState.category == LANDED || oldState.category == SLOW) && state.category == AIR)
			{
				playMovingSound(GenesisSoundEvents.mob_meganeura_takeoff, false);
				takeoffSoundTimer = 15;	// Start timer to play looping flight sound when takeoff is done.
			}
			else if ((oldState == NONE || oldState.category == AIR) && state.category == SLOW)
			{
				playMovingSound(GenesisSoundEvents.mob_meganeura_land, true);
			}
			
			dataManager.set(STATE, state);
		}
	}
	
	protected void playMovingSound(SoundEvent sound, boolean loop)
	{
		SoundUtils.playMovingEntitySound(sound, getSoundCategory(), loop, this, getSoundVolume(), getSoundPitch());
	}
	
	@Override
	public float getPitch(MovingEntitySound sound, float pitch)
	{
		return pitch * dopplerPitch;
	}
	
	@Override
	public float getVolume(MovingEntitySound sound, float volume)
	{
		return volume;
	}
	
	@Override
	public boolean shouldStopSound(MovingEntitySound sound)
	{
		ResourceLocation loc = sound.getSoundLocation();
		State state = dataManager.get(STATE);
		
		if (state.category != AIR
				&& (loc.equals(GenesisSoundEvents.mob_meganeura_fly.getSoundName())
				|| loc.equals(GenesisSoundEvents.mob_meganeura_takeoff.getSoundName())))
		{
			return true;
		}
		
		if (state.category != SLOW
				&& loc.equals(GenesisSoundEvents.mob_meganeura_land.getSoundName()))
		{
			return true;
		}
		
		return false;
	}
	
	public void setTargetLocation(Vec3d location)
	{
		targetLocation = location;
	}
	
	public Vec3d getTargetLocation()
	{
		return targetLocation;
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4);
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
		
		State state = dataManager.get(STATE);
		boolean idle = state.category == LANDED;
		
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
		
		if (!worldObj.isRemote && takeoffSoundTimer > 0)
		{
			takeoffSoundTimer--;
			
			if (takeoffSoundTimer == 0)
			{
				playMovingSound(GenesisSoundEvents.mob_meganeura_fly, true);
				takeoffSoundTimer = -1;
			}
		}
		
		if (worldObj.isRemote)
		{
			dopplerPitch = SoundUtils.getDopplerEffect(this, 0.03F);
			
			double diffX = posX - prevPosX;
			double diffY = posY - prevPosY;
			double diffZ = posZ - prevPosZ;
			
			Vec3d posDiff = new Vec3d(diffX, diffY, diffZ);
			double rads = Math.toRadians(rotationYaw);
			Vec3d forward = new Vec3d(Math.cos(rads), 0, Math.sin(rads));
			double dotSpeed = MathHelper.sqrt_double(posDiff.dotProduct(forward));
			double maxSpeed = speed * (2 / 3.0F);
			
			final float tiltSpeed = 0.5F;
			
			// Calculate new pitch.
			float pitchTarget = 0;
			
			if (state.onSide)
			{
				pitchTarget = -90;
			}
			else if (state == LANDING_SIDE)
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
			
			rotationPitch += MathHelper.wrapDegrees(pitchTarget - rotationPitch) * tiltSpeed;
			
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
			
			float wingTarget = 0;
			
			if (idle)
			{
				swingMax = wingSwingIdleMax;
				swingSpeed = wingSwingIdleSpeed;
			}
			
			float epsilon = wingSwingEpsilon;
			wingTarget = swingMax;
			
			if (wingSwingDown)
			{
				wingTarget *= -1;
			}
			
			prevWingSwing = wingSwing;
			wingSwing += (wingTarget - wingSwing) * swingSpeed;
			float wingDiff = wingTarget - wingSwing;
			
			if (wingDiff >= -epsilon && wingDiff <= epsilon)
			{
				wingSwingDown = !wingSwingDown;
			}
			
			float legTarget = 0;
			
			if (!idle)
			{
				legTarget = 1;
			}
			
			prevLegPosition = legPosition;
			legPosition += (legTarget - legPosition) * 0.25F;
		}
	}
	
	@Override
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int increments, boolean teleport)
	{
		float oldRotationPitch = rotationPitch;
		float oldPrevRotationPitch = prevRotationPitch;
		super.setPositionAndRotationDirect(x, y, z, yaw, pitch, increments, teleport);
		/*newRotationPitch = */rotationPitch = oldRotationPitch;
		prevRotationPitch = oldPrevRotationPitch;
		super.setPositionAndRotationDirect(x, y, z, yaw, pitch, increments, teleport);
	}
	
	public static class MeganeuraUpdateMessage implements IMessage
	{
		public int entityID;
		public float eggPlaceTimer;
		public Vec3d position;
		public Vec3d velocity;
		public float yaw;
		public Vec3d targetLocation;
		
		public MeganeuraUpdateMessage()
		{
		}
		
		public MeganeuraUpdateMessage(int entityID, Vec3d position, Vec3d velocity, float yaw, Vec3d targetLocation, float eggPlaceTimer)
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
			this(entity.getEntityId(), entity.getPositionVector(), new Vec3d(entity.motionX, entity.motionY, entity.motionZ), entity.rotationYaw, entity.getTargetLocation(), entity.eggPlaceTimer);
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
				position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			}
			
			if (buf.readBoolean())
			{
				velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			}
			
			yaw = buf.readFloat();
			
			if (buf.readBoolean())
			{
				targetLocation = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			}
		}
		
		public static class Handler implements IMessageHandler<MeganeuraUpdateMessage, IMessage>
		{
			@Override
			public IMessage onMessage(final MeganeuraUpdateMessage message, final MessageContext ctx)
			{
				final Minecraft mc = Minecraft.getMinecraft();
				mc.addScheduledTask(() ->
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
						
						Vec3d p = message.position;
						
						if (p != null)
						{
							meganeura.setPositionAndRotationDirect(p.xCoord, p.yCoord, p.zCoord, message.yaw, meganeura.rotationPitch, 2, false);
							meganeura.serverPosX = (int) (p.xCoord * 4096);
							meganeura.serverPosY = (int) (p.yCoord * 4096);
							meganeura.serverPosZ = (int) (p.zCoord * 4096);
						}
						
						Vec3d v = message.velocity;
						
						if (v != null)
						{
							meganeura.setVelocity(v.xCoord, v.yCoord, v.zCoord);
						}
						
						meganeura.setTargetLocation(message.targetLocation);
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
				});
				return null;
			}
		}
	}
	
	public boolean isCorrectBiome(BlockPos pos)
	{
		Biome biome = worldObj.getBiome(pos);
		return biome instanceof IEntityPreferredBiome && ((IEntityPreferredBiome) biome).shouldEntityPreferBiome(this);
	}
	
	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();
		
		Vec3d ourPos = getPositionVector();
		State ourState = dataManager.get(STATE);
		Vec3d ourOldTarget = getTargetLocation();
		Vec3d ourNewTarget = ourOldTarget;
		
		if (ourNewTarget == null)
		{
			ourNewTarget = ourPos;
		}
		
		BlockPos targetPos = new BlockPos(ourNewTarget);
		IBlockState atTarget = worldObj.getBlockState(targetPos);
		
		double targetDistance = ourNewTarget.squareDistanceTo(ourPos);
		
		double close = 0.75;
		close *= close;
		double far = 1;
		far *= far;
		boolean reachedClose = targetDistance <= close;
		boolean reachedFar = targetDistance <= far;
		
		// Update our state according to whether we've reached our destination.
		switch (ourState)
		{
		case NONE:
			setState(FLYING);
			break;
		case FLYING:
			if (reachedFar)
			{
				ourNewTarget = null;
			}
			
			if (rand.nextInt(75) == 0)
			{
				double distance = 16 + rand.nextInt(17);
				
				for (int i = 0; i < 16; i++)
				{
					DoubleRange horizRange = DoubleRange.create(-1, 1);
					DoubleRange vertRange = DoubleRange.create(-1, 0.25);
					Vec3d random = new Vec3d(horizRange.get(rand), vertRange.get(rand), horizRange.get(rand)).normalize();
					Vec3d to = ourPos.addVector(random.xCoord * distance, random.yCoord * distance, random.zCoord * distance);
					RayTraceResult hit = worldObj.rayTraceBlocks(ourPos, to, true, false, false);
					
					if (hit != null && hit.typeOfHit == Type.BLOCK)
					{
						BlockPos checkPos = hit.getBlockPos();
						IBlockState checkState = worldObj.getBlockState(checkPos);
						
						if (!checkState.getMaterial().isLiquid())
						{
							boolean setTarget = false;
							
							switch (hit.sideHit)
							{
							case UP:
								setState(LANDING_GROUND);
								setTarget = true;
								break;
							case EAST:
							case NORTH:
							case SOUTH:
							case WEST:
								// Check whether the position has enough room to hold onto the block, because otherwise it will look strange.
								double offset = 0.0625 * 4;
								RayTraceResult aboveHit = worldObj.rayTraceBlocks(ourPos.addVector(0, offset, 0), to.addVector(0, offset, 0), true, false, false);
								
								if (aboveHit != null && aboveHit.typeOfHit == Type.BLOCK &&
									aboveHit.sideHit == hit.sideHit &&
									(aboveHit.getBlockPos().equals(hit.getBlockPos()) || aboveHit.getBlockPos().equals(hit.getBlockPos().up())))
								{
									setState(LANDING_SIDE);
									setTarget = true;
								}
								break;
							case DOWN:
								break;
							}
							
							if (setTarget)
							{
								sendUpdateMessage();
								ourNewTarget = hit.hitVec;
								break;
							}
						}
					}
				}
			}
			
			break;
		case LANDING_GROUND:
			if (onGround && reachedClose)
			{
				setState(IDLE_GROUND);
			}
			break;
		case IDLE_GROUND:
		case IDLE_SIDE:
			boolean fly = false;
			
			if (rand.nextInt(100) == 0)
			{
				fly = true;
			}
			else if (ourState == IDLE_GROUND)
			{
				if (!onGround)
					fly = true;
			}
			else
			{
				Vec3d forward = ourNewTarget.subtract(ourPos);
				forward = forward.normalize();
				double epsilon = 0.01D;
				forward = forward.addVector(forward.xCoord * epsilon, forward.yCoord * epsilon, forward.zCoord * epsilon).add(ourPos);
				RayTraceResult hit = worldObj.rayTraceBlocks(ourPos, forward, true, false, false);
				
				if (hit == null || hit.typeOfHit != Type.BLOCK)
				{
					fly = true;
				}
			}
			
			if (fly)
			{
				setState(FLYING);
			}
			break;
		case LANDING_SIDE:
			boolean calamites = atTarget.getBlock() == GenesisBlocks.calamites;
			
			if (reachedClose)
			{
				if (calamites && canSpawnMeganeura(worldObj, ourPos))
				{
					ourNewTarget = new Vec3d(targetPos.getX() + 0.5, ourNewTarget.yCoord, targetPos.getZ() + 0.5);
					setState(PLACING_EGG);
					eggPlaceTimer = 1;
					sendUpdateMessage();
				}
				else
				{
					setState(IDLE_SIDE);
					sendUpdateMessage();
				}
			}
			else
			{
				RayTraceResult hit = worldObj.rayTraceBlocks(ourPos, ourNewTarget, false, false, true);
				
				if (hit != null && hit.typeOfHit == Type.BLOCK)
				{
					EnumFacing side = hit.sideHit;
					
					switch (side)
					{
					case EAST:
					case NORTH:
					case SOUTH:
					case WEST:
						ourNewTarget = hit.hitVec;
						break;
					default:
						setState(FLYING);
						break;
					}
				}
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
					RayTraceResult hit = worldObj.rayTraceBlocks(ourPos, ourNewTarget, false, false, false);
					
					if (hit != null)
					{
						EntityMeganeuraEgg egg = new EntityMeganeuraEgg(worldObj);
						egg.setPositionAndRotation(hit.hitVec.xCoord, hit.hitVec.yCoord - 0.45, hit.hitVec.zCoord, 0, 0);
						egg.setFixedTo(targetPos);
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
		
		ourState = dataManager.get(STATE);
		
		boolean slowing = ourState.category == SLOW;
		boolean inAir = ourState.category == AIR || slowing;
		
		// Get us started in a different direction if necessary.
		if (ourNewTarget != null && inAir)
		{
			if (!isCorrectBiome(targetPos) && ourState.category == AIR)
			{
				ourNewTarget = null;
			}
			else if (ourState == FLYING && entityAge % 20 == 0 && new Vec3d(motionX, motionY, motionZ).squareDistanceTo(new Vec3d(0, 0, 0)) < 0.01)
			{
				ourNewTarget = null;
			}
			else
			{
				RayTraceResult hit = worldObj.rayTraceBlocks(ourPos, ourNewTarget, false, false, true);
				
				if (hit != null && hit.typeOfHit == Type.BLOCK)
				{
					if (ourState != LANDING_SIDE)
					{
						ourNewTarget = null;
					}
					else if (worldObj.getBlockState(hit.getBlockPos()).getBlock() != GenesisBlocks.calamites)
					{
						ourNewTarget = null;
					}
				}
				else if (slowing)
				{
					if (rand.nextInt(75) == 0)
					{
						ourNewTarget = null;
					}
				}
				else
				{
					if (rand.nextInt(30) == 0)
					{
						ourNewTarget = null;
					}
				}
			}
		}
		
		if (ourNewTarget == null)
		{
			DoubleRange vertRange = DoubleRange.create(4, 8);
			double vertMove = vertRange.get(rand);
			
			if (!isInWater())
			{
				// Start finding ground level
				Vec3d to = new Vec3d(posX, 0, posZ);
				RayTraceResult hit = worldObj.rayTraceBlocks(ourPos, to, true, false, true);
				
				double ground = 0;
				
				if (hit != null)
				{
					ground = Math.min((hit.hitVec.yCoord + 0.5) - posY, 0);
				}
				
				vertMove += ground;
				
				to = new Vec3d(posX, posY + vertMove, posZ);
				hit = worldObj.rayTraceBlocks(ourPos, to, true, false, true);
				
				if (hit != null)
				{
					double ceiling = Math.max((hit.hitVec.yCoord - 0.5) - posY, 0);
					vertMove = Math.min(vertMove, ceiling);
				}
				// End finding ground level
			}
			
			DoubleRange horiz = DoubleRange.create(-1, 1);
			Vec3d random = new Vec3d(horiz.get(rand), 0, horiz.get(rand)).normalize();
			
			DoubleRange distRange = DoubleRange.create(5, 10);
			double distance = distRange.get(rand);
			random = new Vec3d(random.xCoord * distance, random.yCoord * distance, random.zCoord * distance);
			random = random.addVector(0, vertMove, 0);
			
			setState(FLYING);
			ourNewTarget = ourPos.add(random);
		}
		
		double moveX = motionX, moveY = motionY, moveZ = motionZ;
		boolean strafe = ourState.onSide;
		
		if (inAir || strafe)
		{
			Vec3d moveVec = ourNewTarget.subtract(ourPos).normalize();
			float targetYaw = (float) (Math.toDegrees(Math.atan2(moveVec.zCoord, moveVec.xCoord)));
			float diffYaw = MathHelper.wrapDegrees(targetYaw - rotationYaw);
			
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
			speed = Math.min(ourNewTarget.distanceTo(ourPos) / 10, speed);
		}
		
		moveX *= speed;
		moveZ *= speed;
		
		motionX += (moveX - motionX) * 0.5;
		motionY += (moveY - motionY) * 0.5;
		motionZ += (moveZ - motionZ) * 0.5;
		
		if (ourOldTarget == null ||
			ourOldTarget.xCoord != ourNewTarget.xCoord ||
			ourOldTarget.yCoord != ourNewTarget.yCoord ||
			ourOldTarget.zCoord != ourNewTarget.zCoord)
		{
			sendUpdateMessage();
		}
		
		setTargetLocation(ourNewTarget);
	}
	
	@Override
	protected SoundEvent getHurtSound()
	{
		return new SoundEvent(new ResourceLocation(Constants.ASSETS_PREFIX + "mob.meganeura.hurt"));
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return new SoundEvent(new ResourceLocation(Constants.ASSETS_PREFIX + "mob.meganeura.die"));
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
			setTargetLocation(new Vec3d(targetComp.getDouble("x"), targetComp.getDouble("y"), targetComp.getDouble("z")));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		
		compound.setString("state", dataManager.get(STATE).toString());
		Vec3d target = getTargetLocation();
		
		if (target != null)
		{
			NBTTagCompound targetComp = new NBTTagCompound();
			targetComp.setDouble("x", target.xCoord);
			targetComp.setDouble("y", target.yCoord);
			targetComp.setDouble("z", target.zCoord);
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
	
	@Override
	protected ResourceLocation getLootTable()
    {
        return GenesisLoot.ENTITY_MEGANEURA;
    }
}
