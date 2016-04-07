package genesis.sounds;

import genesis.util.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class GenesisSoundEvents
{
	/* Blocks */
	public static SoundEvent block_moss_break;
	public static SoundEvent block_moss_step;
	public static SoundEvent block_moss_place;
	public static SoundEvent block_moss_hit;
	public static SoundEvent block_moss_fall;
	
	public static SoundEvent block_permafrost_break;
	public static SoundEvent block_permafrost_step;
	public static SoundEvent block_permafrost_place;
	public static SoundEvent block_permafrost_hit;
	public static SoundEvent block_permafrost_fall;
	
	public static SoundEvent block_dung_break;
	public static SoundEvent block_dung_step;
	public static SoundEvent block_dung_place;
	public static SoundEvent block_dung_hit;
	public static SoundEvent block_dung_fall;
	
	public static SoundEvent block_fern_break;
	public static SoundEvent block_fern_step;
	public static SoundEvent block_fern_place;
	public static SoundEvent block_fern_hit;
	public static SoundEvent block_fern_fall;
	
	public static SoundEvent block_calamites_break;
	public static SoundEvent block_calamites_step;
	public static SoundEvent block_calamites_place;
	public static SoundEvent block_calamites_hit;
	public static SoundEvent block_calamites_fall;
	
	public static SoundEvent block_coral_break;
	public static SoundEvent block_coral_step;
	public static SoundEvent block_coral_place;
	public static SoundEvent block_coral_hit;
	public static SoundEvent block_coral_fall;
	
	public static SoundEvent block_aquaticplant_break;
	public static SoundEvent block_aquaticplant_step;
	public static SoundEvent block_aquaticplant_place;
	public static SoundEvent block_aquaticplant_hit;
	public static SoundEvent block_aquaticplant_fall;
	
	public static SoundEvent block_ooze_break;
	public static SoundEvent block_ooze_step;
	public static SoundEvent block_ooze_place;
	public static SoundEvent block_ooze_hit;
	public static SoundEvent block_ooze_fall;
	
	public static SoundEvent block_dead_log_break;
	public static SoundEvent block_dead_log_step;
	public static SoundEvent block_dead_log_place;
	public static SoundEvent block_dead_log_hit;
	public static SoundEvent block_dead_log_fall;
	
	public static SoundEvent block_mushroom_break;
	public static SoundEvent block_mushroom_step;
	public static SoundEvent block_mushroom_place;
	public static SoundEvent block_mushroom_hit;
	public static SoundEvent block_mushroom_fall;
	
	public static SoundEvent block_debris_break;
	public static SoundEvent block_debris_step;
	public static SoundEvent block_debris_place;
	public static SoundEvent block_debris_hit;
	public static SoundEvent block_debris_fall;
	
	/* Items */
	public static SoundEvent item_flintandmarcasite_use;
	
	/* Mobs */
	public static SoundEvent mob_meganeura_fly;
	public static SoundEvent mob_meganeura_land;
	public static SoundEvent mob_meganeura_takeoff;
	
	/* Player */
	public static SoundEvent player_knapping_hit;
	
	/* Music */
	public static SoundEvent music_genesis;
	
	private static int currentID = -1;
	
	private static int getLastID()
	{
		int max = 0;
		
		for (SoundEvent sound : SoundEvent.soundEventRegistry)
		{
			max = Math.max(max, SoundEvent.soundEventRegistry.getIDForObject(sound));
		}
		
		return max;
	}
	
	public static void setCurrentID()
	{
		currentID = getLastID() + 1;
	}
	
	public static SoundEvent registerSound(ResourceLocation location)
	{
		SoundEvent.soundEventRegistry.register(currentID, location, new SoundEvent(location));
		return SoundEvent.soundEventRegistry.getObjectById(currentID++);
	}
	
	private static SoundEvent registerSound(String location)
	{
		return registerSound(new ResourceLocation(Constants.MOD_ID, location));
	}
	
	public static void registerAll()
	{
		setCurrentID();
		
		/* Blocks */
		block_moss_break = registerSound("block.moss.break");
		block_moss_step = registerSound("block.moss.step");
		block_moss_place = registerSound("block.moss.place");
		block_moss_hit = registerSound("block.moss.hit");
		block_moss_fall = registerSound("block.moss.fall");
		
		block_permafrost_break = registerSound("block.permafrost.break");
		block_permafrost_step = registerSound("block.permafrost.step");
		block_permafrost_place = registerSound("block.permafrost.place");
		block_permafrost_hit = registerSound("block.permafrost.hit");
		block_permafrost_fall = registerSound("block.permafrost.fall");
		
		block_dung_break = registerSound("block.dung.break");
		block_dung_step = registerSound("block.dung.step");
		block_dung_place = registerSound("block.dung.place");
		block_dung_hit = registerSound("block.dung.hit");
		block_dung_fall = registerSound("block.dung.fall");
		
		block_fern_break = registerSound("block.fern.break");
		block_fern_step = registerSound("block.fern.step");
		block_fern_place = registerSound("block.fern.place");
		block_fern_hit = registerSound("block.fern.hit");
		block_fern_fall = registerSound("block.fern.fall");
		
		block_calamites_break = registerSound("block.calamites.break");
		block_calamites_step = registerSound("block.calamites.step");
		block_calamites_place = registerSound("block.calamites.place");
		block_calamites_hit = registerSound("block.calamites.hit");
		block_calamites_fall = registerSound("block.calamites.fall");
		
		block_coral_break = registerSound("block.coral.break");
		block_coral_step = registerSound("block.coral.step");
		block_coral_place = registerSound("block.coral.place");
		block_coral_hit = registerSound("block.coral.hit");
		block_coral_fall = registerSound("block.coral.fall");
		
		block_aquaticplant_break = registerSound("block.aquaticplant.break");
		block_aquaticplant_step = registerSound("block.aquaticplant.step");
		block_aquaticplant_place = registerSound("block.aquaticplant.place");
		block_aquaticplant_hit = registerSound("block.aquaticplant.hit");
		block_aquaticplant_fall = registerSound("block.aquaticplant.fall");
		
		block_ooze_break = registerSound("block.ooze.break");
		block_ooze_step = registerSound("block.ooze.step");
		block_ooze_place = registerSound("block.ooze.place");
		block_ooze_hit = registerSound("block.ooze.hit");
		block_ooze_fall = registerSound("block.ooze.fall");
		
		block_dead_log_break = registerSound("block.dead_log.break");
		block_dead_log_step = registerSound("block.dead_log.step");
		block_dead_log_place = registerSound("block.dead_log.place");
		block_dead_log_hit = registerSound("block.dead_log.hit");
		block_dead_log_fall = registerSound("block.dead_log.fall");
		
		block_mushroom_break = registerSound("block.mushroom.break");
		block_mushroom_step = registerSound("block.mushroom.step");
		block_mushroom_place = registerSound("block.mushroom.place");
		block_mushroom_hit = registerSound("block.mushroom.hit");
		block_mushroom_fall = registerSound("block.mushroom.fall");
		
		block_debris_break = registerSound("block.debris.break");
		block_debris_step = registerSound("block.debris.step");
		block_debris_place = registerSound("block.debris.place");
		block_debris_hit = registerSound("block.debris.hit");
		block_debris_fall = registerSound("block.debris.fall");
		
		/* Items */
		item_flintandmarcasite_use = registerSound("item.flintandmarcasite.use");
		
		/* Mobs */
		mob_meganeura_fly = registerSound("mob.meganeura.fly");
		mob_meganeura_land = registerSound("mob.meganeura.land");
		mob_meganeura_takeoff = registerSound("mob.meganeura.takeoff");
		
		/* Player */
		player_knapping_hit = registerSound("player.knapping.hit");
		
		/* Music */
		music_genesis = registerSound("music.genesis");
		
		currentID = -1;
	}
}
