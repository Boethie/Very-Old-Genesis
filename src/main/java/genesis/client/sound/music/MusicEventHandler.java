package genesis.client.sound.music;

import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;
import genesis.common.sounds.GenesisSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MusicEventHandler
{
	private static final float VOLUME_MINIMUM = 0.001F;

	private static ISound currentMusic;

	@SubscribeEvent
	public void onMusicPlay(PlaySoundEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();

		//Replace the background music with genesis music if we are in the genesis dimension
		if (GenesisConfig.playDimensionMusic
				&& event.getSound().getCategory() == SoundCategory.MUSIC
				&& mc.gameSettings.getSoundLevel(SoundCategory.MASTER) > VOLUME_MINIMUM
				&& mc.gameSettings.getSoundLevel(SoundCategory.MUSIC) > VOLUME_MINIMUM
				&& mc.theWorld != null
				&& GenesisDimensions.isGenesis(mc.theWorld))
		{
			if (currentMusic == null || !event.getManager().isSoundPlaying(currentMusic))
			{
				currentMusic = PositionedSoundRecord.getMusicRecord(GenesisSoundEvents.MUSIC_GENESIS);
				currentMusic.createAccessor(event.getManager().sndHandler);
				event.setResultSound(currentMusic);
			}
			else
			{
				event.setResultSound(null);
			}
		}
	}
}
