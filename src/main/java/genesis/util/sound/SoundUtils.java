package genesis.util.sound;

import genesis.client.sound.MovingEntitySound;
import genesis.common.Genesis;
import genesis.util.GenesisByteBufUtils;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;

import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class SoundUtils
{
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
