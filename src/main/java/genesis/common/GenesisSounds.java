package genesis.common;

import java.lang.reflect.Field;
import java.util.*;

import com.google.common.collect.*;

import genesis.client.GenesisClient;
import genesis.client.sound.MovingEntitySound;
import genesis.util.Constants;
import genesis.util.SidedFunction;
import io.netty.buffer.ByteBuf;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public final class GenesisSounds
{
	public static GenesisSounds INSTANCE = new GenesisSounds();
	
	public static void register()
	{
		Genesis.proxy.callSided(new SidedFunction()
		{
			@SideOnly(Side.CLIENT)
			@Override
			public void client(GenesisClient client)
			{
				MinecraftForge.EVENT_BUS.register(INSTANCE);
				FMLCommonHandler.instance().bus().register(INSTANCE);
			}
		});
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
			return Block.soundTypeGrass.getStepSound();
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
			return Block.soundTypeGrass.getStepSound();
		}
	};
	public static final GenesisSoundType CORAL = new GenesisSoundType("coral", 1.0F, 1.0F, true);
	public static final GenesisSoundType AQUATICPLANT = new GenesisSoundType("aquaticplant", 10.0F, 1.0F);
	public static final GenesisSoundType OOZE = new GenesisSoundType("ooze", 1.5F, 1.5F, true);
	public static final GenesisSoundType ROTTEN_LOG = new GenesisSoundType("rotten_log", 1.5F, 1.5F, true);
	public static final GenesisSoundType MUSHROOM = new GenesisSoundType("mushroom", 1.5F, 1.5F, true);

	public static class GenesisSoundType extends Block.SoundType
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
	
	// Custom sound types
	public static class MovingEntitySoundMessage implements IMessage
	{
		public ResourceLocation sound;
		public int entityID;
		public float volume;
		public float pitch;
		public boolean loop;
		
		public MovingEntitySoundMessage() {}
		
		public MovingEntitySoundMessage(ResourceLocation sound, boolean loop, int entityID, float volume, float pitch)
		{
			this.sound = sound;
			this.loop = loop;
			this.entityID = entityID;
			this.volume = volume;
			this.pitch = pitch;
		}
		
		public MovingEntitySoundMessage(ResourceLocation sound, boolean loop, Entity entity, float volume, float pitch)
		{
			this(sound, loop, entity.getEntityId(), volume, pitch);
		}
		
		@Override
		public void fromBytes(ByteBuf buf)
		{
			sound = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
			entityID = buf.readInt();
			volume = buf.readFloat();
			pitch = buf.readFloat();
			loop = buf.readBoolean();
		}
		
		@Override
		public void toBytes(ByteBuf buf)
		{
			ByteBufUtils.writeUTF8String(buf, sound.toString());
			buf.writeInt(entityID);
			buf.writeFloat(volume);
			buf.writeFloat(pitch);
			buf.writeBoolean(loop);
		}
		
		public static class Handler implements IMessageHandler<MovingEntitySoundMessage, IMessage>
		{
			@Override
			public IMessage onMessage(final MovingEntitySoundMessage message, final MessageContext ctx)
			{
				final Minecraft mc = Minecraft.getMinecraft();
				mc.addScheduledTask(new Runnable()
				{
					@Override
					public void run()
					{
						Entity entity = mc.theWorld.getEntityByID(message.entityID);
						playMovingEntitySound(message.sound, message.loop, entity, message.volume, message.pitch);
					}
				});
				return null;
			}
		}
	}
	
	public static void playMovingEntitySound(ResourceLocation sound, boolean loop, Entity entity, float volume, float pitch)
	{
		if (entity.worldObj.isRemote)
		{
			playSound(new MovingEntitySound(sound, loop, entity, volume, pitch));
		}
		else
		{
			Genesis.network.sendToAllTracking(new MovingEntitySoundMessage(sound, loop, entity, volume, pitch), entity);
		}
	}
	
	public static abstract class RandomLoopingSound extends MovingSound implements ITickableSound
	{
		protected RandomLoopingSound(ResourceLocation sound, boolean repeat)
		{
			super(sound);
			
			this.repeat = repeat;
		}
		
		@Override
		public boolean canRepeat()
		{
			return false;
		}
		
		public boolean canActuallyRepeat()
		{
			return repeat;
		}
		
		public abstract void update();
	}
	
	private static List<RandomLoopingSound> loopingSounds = new ArrayList<RandomLoopingSound>();
	
	public static void playSound(ISound sound)
	{
		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
		
		if (sound instanceof RandomLoopingSound)
		{
			RandomLoopingSound repeatSound = (RandomLoopingSound) sound;
			
			if (repeatSound.canActuallyRepeat())
			{
				loopingSounds.add(repeatSound);
			}
		}
	}
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END)
		{
			List<RandomLoopingSound> replay = new ArrayList<RandomLoopingSound>();
			Iterator<RandomLoopingSound> iter = loopingSounds.iterator();
			
			while (iter.hasNext())
			{
				RandomLoopingSound sound = iter.next();
				
				if (!sound.canActuallyRepeat() || sound.isDonePlaying())
				{
					iter.remove();
				}
				else if (!isSoundPlaying(sound))
				{
					getSoundManager().stopSound(sound);
					getPlayingSounds().inverse().remove(sound);
					iter.remove();
					replay.add(sound);
				}
			}
			
			for (RandomLoopingSound sound : replay)
			{
				playSound(sound);
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldUnloaded(WorldEvent.Unload event)
	{
		loopingSounds.clear();
	}
	
	private static SoundManager soundManager;
	private static BiMap<String, ISound> playingSounds;
	
	public static SoundManager getSoundManager()
	{
		if (soundManager == null)
		{
			soundManager = ReflectionHelper.getPrivateValue(SoundHandler.class, Minecraft.getMinecraft().getSoundHandler(), "sndManager", "field_147694_f");
		}
		
		return soundManager;
	}
	
	public static BiMap<String, ISound> getPlayingSounds()
	{
		if (playingSounds == null)
		{
			playingSounds = ReflectionHelper.getPrivateValue(SoundManager.class, getSoundManager(), "playingSounds", "field_148629_h");
		}
		
		return playingSounds;
	}
	
	public static String getSoundID(ISound sound)
	{
		return getPlayingSounds().inverse().get(sound);
	}
	
	public static boolean isSoundInWorld(ISound sound)
	{
		return getPlayingSounds().containsValue(sound);
	}
	
	public static boolean isSoundPlaying(ISound sound)
	{
		return getSoundManager().isSoundPlaying(sound);
	}
	
	public static float getDopplerEffect(Entity entity, float pitch, float strength)
	{
		Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
		
		Vec3 posDiff = entity.getPositionVector().subtract(viewEntity.getPositionVector());
		ActiveRenderInfo.getPosition();
		
		Vec3 camMove = new Vec3(viewEntity.posX - viewEntity.prevPosX, viewEntity.posY - viewEntity.prevPosY, viewEntity.posZ - viewEntity.prevPosZ);
		Vec3 megMove = new Vec3(entity.posX - entity.prevPosX, entity.posY - entity.prevPosY, entity.posZ - entity.prevPosZ);
		Vec3 relMove = camMove.subtract(megMove);
		
		float relative = (float) posDiff.dotProduct(relMove);
		
		float amount = relative;
		amount *= strength;
		amount += 1;
		
		return pitch * amount;
	}
}
