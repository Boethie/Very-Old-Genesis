package genesis.client.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.ResourceLocation;

public abstract class RandomLoopingSound extends MovingSound
{
	protected boolean forceStop = false;
	
	protected RandomLoopingSound(ResourceLocation sound, boolean repeat)
	{
		super(sound);
		
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
