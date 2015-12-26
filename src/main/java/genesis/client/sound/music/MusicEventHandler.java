package genesis.client.sound.music;

import genesis.common.GenesisConfig;
import genesis.common.GenesisDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MusicEventHandler 
{

	@SubscribeEvent
	public void onMusicPlay (PlaySoundEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		//Replace the background music with genesis music if we are in the genesis dimension
		if (GenesisConfig.playDimensionMusic
				&& event.category == SoundCategory.MUSIC
				&& mc.theWorld != null
				&& GenesisDimensions.isGenesis(mc.theWorld)) 
		{
			event.result = PositionedSoundRecord.create(new ResourceLocation("genesis:music.genesis"));
		}
	}
	
}
