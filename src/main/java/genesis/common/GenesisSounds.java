package genesis.common;

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
import net.minecraftforge.fml.common.FMLCommonHandler;
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
	
	public static void playMovingEntitySound(final ResourceLocation soundLoc, final boolean loop, final Entity entity, final float volume, final float pitch)
	{
		if (entity.worldObj.isRemote)
		{
			Genesis.proxy.callSided(new SidedFunction()
			{
				@Override
				public void client(GenesisClient client)
				{
					playSound(new MovingEntitySound(soundLoc, loop, entity, volume, pitch));
				}
			});
		}
		else
		{
			Genesis.network.sendToAllTracking(new MovingEntitySoundMessage(soundLoc, loop, entity, volume, pitch), entity);
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
		
		Vec3 renderOff = ActiveRenderInfo.getPosition();
		
		// Dot product of the difference between camera position and the entity, and the relative motion of the entity.
		float relative = (float) (
				(entity.posX - (viewEntity.posX + renderOff.xCoord)) * ((viewEntity.posX - viewEntity.prevPosX) - (entity.posX - entity.prevPosX)) +
				(entity.posY - (viewEntity.posY + renderOff.yCoord)) * ((viewEntity.posY - viewEntity.prevPosY) - (entity.posY - entity.prevPosY)) +
				(entity.posZ - (viewEntity.posZ + renderOff.zCoord)) * ((viewEntity.posZ - viewEntity.prevPosZ) - (entity.posZ - entity.prevPosZ)));
		
		return relative * strength + 1;
	}
}
