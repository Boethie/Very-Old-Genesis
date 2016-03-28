package genesis.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import paulscode.sound.SoundSystem;

public class RandomReflection
{
	private static final Field blockHitDelay = ReflectionHelper.findField(PlayerControllerMP.class, "blockHitDelay", "field_78781_i");
	
	public static int getBlockHitDelay()
	{
		try
		{
			return blockHitDelay.getInt(Minecraft.getMinecraft().playerController);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static void setBlockHitDelay(int value)
	{
		try
		{
			blockHitDelay.setInt(Minecraft.getMinecraft().playerController, value);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static final class SoundReflection
	{
		private static SoundManager soundManager;
		private static SoundSystem soundSystem;
		private static BiMap<String, ISound> playingSounds;
		private static Map<String, Integer> playingSoundsStopTime;
		private static Map<ISound, SoundPoolEntry> playingSoundPoolEntries;
		private static Multimap<SoundCategory, String> categorySounds;
		private static List<ISound> tickableSounds;
		
		public static SoundHandler getSoundHandler()
		{
			return Minecraft.getMinecraft().getSoundHandler();
		}
		
		public static SoundManager getSoundManager()
		{
			if (soundManager == null)
			{
				soundManager = ReflectionHelper.getPrivateValue(SoundHandler.class, getSoundHandler(), "sndManager", "field_147694_f");
			}
			
			return soundManager;
		}
		
		public static SoundSystem getSoundSystem()
		{
			if (soundSystem == null)
			{
				soundSystem = ReflectionHelper.getPrivateValue(SoundManager.class, getSoundManager(), "sndSystem", "field_148620_e");
			}
			
			return soundSystem;
		}
		
		public static BiMap<String, ISound> getPlayingSounds()
		{
			if (playingSounds == null)
			{
				playingSounds = ReflectionHelper.getPrivateValue(SoundManager.class, getSoundManager(), "playingSounds", "field_148629_h");
			}
			
			return playingSounds;
		}
		
		public static Map<String, Integer> getPlayingSoundsStopTime()
		{
			if (playingSoundsStopTime == null)
			{
				playingSoundsStopTime = ReflectionHelper.getPrivateValue(SoundManager.class, getSoundManager(), "playingSoundsStopTime", "field_148624_n");
			}
			
			return playingSoundsStopTime;
		}
		
		public static Map<ISound, SoundPoolEntry> getPlayingSoundPoolEntries()
		{
			if (playingSoundPoolEntries == null)
			{
				playingSoundPoolEntries = ReflectionHelper.getPrivateValue(SoundManager.class, getSoundManager(), "playingSoundPoolEntries", "field_148627_j");
			}
			
			return playingSoundPoolEntries;
		}
		
		public static Multimap<SoundCategory, String> getCategorySounds()
		{
			if (categorySounds == null)
			{
				categorySounds = ReflectionHelper.getPrivateValue(SoundManager.class, getSoundManager(), "categorySounds", "field_148628_k");
			}
			
			return categorySounds;
		}
		
		public static List<ISound> getTickableSounds()
		{
			if (tickableSounds == null)
			{
				tickableSounds = ReflectionHelper.getPrivateValue(SoundManager.class, getSoundManager(), "tickableSounds", "field_148625_l");
			}
			
			return tickableSounds;
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
			return getSoundHandler().isSoundPlaying(sound);
		}
		
		public static void forceStopSound(ISound sound)
		{	// Taken from SoundManager.updateAllSounds()
			String id = getSoundID(sound);
			
			getPlayingSounds().remove(id);
			getSoundSystem().removeSource(id);
			getPlayingSoundsStopTime().remove(id);
			getPlayingSoundPoolEntries().remove(sound);
			getCategorySounds().remove(getSoundHandler().getSound(sound.getSoundLocation()).getSoundCategory(), id);
			getTickableSounds().remove(sound);
		}
	}
}
