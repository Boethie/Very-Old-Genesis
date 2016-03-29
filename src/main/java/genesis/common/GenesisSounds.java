package genesis.common;

import genesis.client.GenesisClient;
import genesis.client.sound.MovingEntitySound;
import genesis.util.*;

import io.netty.buffer.ByteBuf;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public final class GenesisSounds
{
	public static GenesisSounds INSTANCE = new GenesisSounds();
	
	public static void register()
	{
		Genesis.proxy.callClient((c) -> MinecraftForge.EVENT_BUS.register(INSTANCE));
	}
	
	public static final GenesisSoundType MOSS = new GenesisSoundType("moss", 10.0F, 1.0F);
	public static final GenesisSoundType PERMAFROST = new GenesisSoundType("permafrost", 1.0F, 1.0F);
	public static final GenesisSoundType DUNG = new GenesisSoundType("dung", 1.0F, 1.0F, true);
	
	public static final GenesisSoundType FERN = new GenesisSoundType("fern", 10.0F, 1.0F)
	{
		@Override
		public String getBreakSound()
		{
			return Block.soundTypeGrass.getBreakSound();
		}
		
		@Override
		public String getStepSound()
		{
			return Block.soundTypeGrass.getSoundType();
		}
		
		@Override
		public String getPlaceSound()
		{
			return Block.soundTypeGrass.getPlaceSound();
		}
	};
	
	public static final GenesisSoundType CALAMITES = new GenesisSoundType("calamites", 1.0F, 1.0F)
	{
		@Override
		public String getStepSound()
		{
			return Block.soundTypeGrass.getSoundType();
		}
	};
	
	public static final GenesisSoundType CORAL = new GenesisSoundType("coral", 1.0F, 1.0F, true);
	public static final GenesisSoundType AQUATICPLANT = new GenesisSoundType("aquaticplant", 10.0F, 1.0F);
	public static final GenesisSoundType OOZE = new GenesisSoundType("ooze", 1.5F, 1.5F, true);
	public static final GenesisSoundType DEAD_LOG = new GenesisSoundType("dead_log", 1.5F, 1.5F, true);
	public static final GenesisSoundType MUSHROOM = new GenesisSoundType("mushroom", 1.5F, 1.5F, true);
	public static final GenesisSoundType DEBRIS = new GenesisSoundType("debris", 1.5F, 1.5F, true);
	
	public static class GenesisSoundType extends SoundType
	{
		private final boolean hasCustomPlaceSound;
		
		public GenesisSoundType(String name, float volume, float frequency)
		{
			this(name, volume, frequency, false);
		}
		
		public GenesisSoundType(String name, float volume, float frequency, boolean hasCustomPlaceSound)
		{
			super(name, volume, frequency);
			this.hasCustomPlaceSound = hasCustomPlaceSound;
		}
		
		@Override
		public String getBreakSound()
		{
			return Constants.ASSETS_PREFIX + super.getBreakSound();
		}
		
		@Override
		public String getStepSound()
		{
			return Constants.ASSETS_PREFIX + super.getStepSound();
		}
		
		@Override
		public String getPlaceSound()
		{
			return hasCustomPlaceSound ? Constants.ASSETS_PREFIX + "place." + soundName : super.getPlaceSound();
		}
	}
	
	public static class MovingEntitySoundMessage implements IMessage
	{
		public SoundEvent sound;
		public SoundCategory category;
		public int entityID;
		public float volume;
		public float pitch;
		public boolean loop;
		
		public MovingEntitySoundMessage() {}
		
		public MovingEntitySoundMessage(SoundEvent sound, SoundCategory category, boolean loop, int entityID, float volume, float pitch)
		{
			this.sound = sound;
			this.category = category;
			this.loop = loop;
			this.entityID = entityID;
			this.volume = volume;
			this.pitch = pitch;
		}
		
		public MovingEntitySoundMessage(SoundEvent sound, SoundCategory category, boolean loop, Entity entity, float volume, float pitch)
		{
			this(sound, category, loop, entity.getEntityId(), volume, pitch);
		}
		
		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeInt(SoundEvent.soundEventRegistry.getIDForObject(sound));
			GenesisByteBufUtils.writeEnum(buf, category);
			buf.writeInt(entityID);
			buf.writeFloat(volume);
			buf.writeFloat(pitch);
			buf.writeBoolean(loop);
		}
		
		@Override
		public void fromBytes(ByteBuf buf)
		{
			//sound = new SoundEvent(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
			sound = SoundEvent.soundEventRegistry.getObjectById(buf.readInt());
			category = GenesisByteBufUtils.readEnum(buf, SoundCategory.class);
			entityID = buf.readInt();
			volume = buf.readFloat();
			pitch = buf.readFloat();
			loop = buf.readBoolean();
		}
		
		public static class Handler implements IMessageHandler<MovingEntitySoundMessage, IMessage>
		{
			@Override
			public IMessage onMessage(final MovingEntitySoundMessage message, final MessageContext ctx)
			{
				final Minecraft mc = Minecraft.getMinecraft();
				mc.addScheduledTask(() -> playMovingEntitySound(message.sound, message.category, message.loop, mc.theWorld.getEntityByID(message.entityID), message.volume, message.pitch));
				return null;
			}
		}
	}
	
	public static void playMovingEntitySound(SoundEvent sound, SoundCategory category, final boolean loop, final Entity entity, final float volume, final float pitch)
	{
		if (entity.worldObj.isRemote)
		{
			Genesis.proxy.callClient((c) -> playSound(new MovingEntitySound(sound, category, loop, entity, volume, pitch)));
		}
		else
		{
			Genesis.network.sendToAllTracking(new MovingEntitySoundMessage(sound, category, loop, entity, volume, pitch), entity);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void playSound(ISound sound)
	{
		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
	}
	
	@SideOnly(Side.CLIENT)
	public static float getDopplerEffect(Entity entity, float strength)
	{
		Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
		
		Vec3d renderOff = ActiveRenderInfo.getPosition();
		
		// Dot product of the difference between camera position and the entity, and the relative motion of the entity.
		float relative = (float) (
				(entity.posX - (viewEntity.posX + renderOff.xCoord)) * ((viewEntity.posX - viewEntity.prevPosX) - (entity.posX - entity.prevPosX)) +
				(entity.posY - (viewEntity.posY + renderOff.yCoord)) * ((viewEntity.posY - viewEntity.prevPosY) - (entity.posY - entity.prevPosY)) +
				(entity.posZ - (viewEntity.posZ + renderOff.zCoord)) * ((viewEntity.posZ - viewEntity.prevPosZ) - (entity.posZ - entity.prevPosZ)));
		
		return relative * strength + 1;
	}
}
