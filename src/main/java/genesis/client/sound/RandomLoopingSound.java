package genesis.client.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.*;

public abstract class RandomLoopingSound extends MovingSound
{
	protected boolean forceStop = false;
	
	protected RandomLoopingSound(SoundEvent sound, SoundCategory category, boolean repeat)
	{
		super(sound, category);
		
		this.repeat = repeat;
	}
	
	@Override
	public boolean canRepeat()
	{
		return repeat && !isDonePlaying();
	}
	
	@Override
	public int getRepeatDelay()
	{
		if (repeat)
		{
			return 1;
		}
		
		return 0;
	}
}
