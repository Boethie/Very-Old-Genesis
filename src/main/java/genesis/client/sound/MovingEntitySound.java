package genesis.client.sound;

import net.minecraft.entity.*;
import net.minecraft.util.*;

public class MovingEntitySound extends RandomLoopingSound
{
	public static interface IMovingEntitySoundOwner
	{
		public boolean shouldStopSound(MovingEntitySound sound);
		public float getPitch(MovingEntitySound sound, float pitch);
		public float getVolume(MovingEntitySound sound, float volume);
	}
	
	protected final Entity entity;
	
	public MovingEntitySound(ResourceLocation sound, boolean repeat, Entity entity, float volume, float pitch)
	{
		super(sound, repeat);
		
		this.entity = entity;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	@Override
	public float getVolume()
	{
		float volume = this.volume;
		
		if (entity instanceof IMovingEntitySoundOwner)
		{
			volume = ((IMovingEntitySoundOwner) entity).getVolume(this, volume);
		}
		
		return volume;
	}
	
	@Override
	public float getPitch()
	{
		float pitch = this.pitch;
		
		if (entity instanceof IMovingEntitySoundOwner)
		{
			pitch = ((IMovingEntitySoundOwner) entity).getPitch(this, pitch);
		}
		
		return pitch;
	}
	
	@Override
	public void update()
	{
		donePlaying = donePlaying || entity.isDead;
		
		if (!donePlaying && entity instanceof IMovingEntitySoundOwner)
		{
			donePlaying = ((IMovingEntitySoundOwner) entity).shouldStopSound(this);
		}
		
		xPosF = (float) entity.posX;
		yPosF = (float) entity.posY;
		zPosF = (float) entity.posZ;
	}
}
