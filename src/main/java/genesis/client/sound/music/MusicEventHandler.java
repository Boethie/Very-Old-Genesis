package genesis.client.sound.music;

import genesis.common.GenesisConfig;
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
		//Replace the background music with genesis music if we are in the genesis dimension
		if(GenesisConfig.playDimensionMusic && event.category == SoundCategory.MUSIC && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().theWorld.provider.getDimensionId() == GenesisConfig.genesisDimId) 
		{
			event.result = PositionedSoundRecord.create(new ResourceLocation("genesis:music.genesis"));
		}
	}
	
}
