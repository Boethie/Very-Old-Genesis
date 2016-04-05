package genesis.client.sound.music;

import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;
import genesis.sounds.GenesisSoundEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundCategory;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MusicEventHandler 
{
	@SubscribeEvent
	public void onMusicPlay(PlaySoundEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		//Replace the background music with genesis music if we are in the genesis dimension
		if (GenesisConfig.playDimensionMusic
				&& event.getSound().getCategory() == SoundCategory.MUSIC
				&& mc.theWorld != null
				&& GenesisDimensions.isGenesis(mc.theWorld))
		{
			ISound sound = PositionedSoundRecord.getMusicRecord(GenesisSoundEvents.music_genesis);
			sound.createAccessor(event.getManager().sndHandler);
			event.setResultSound(sound);
		}
	}
}
