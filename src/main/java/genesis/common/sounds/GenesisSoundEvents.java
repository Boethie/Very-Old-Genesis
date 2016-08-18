package genesis.common.sounds;

import genesis.util.Constants;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GenesisSoundEvents
{
	private static final Map<SoundEvent, ResourceLocation> SOUND_EVENTS = new HashMap<>();

	private static SoundEvent createSoundEvent(String soundName)
	{
		ResourceLocation name = new ResourceLocation(Constants.MOD_ID, soundName);
		SoundEvent soundEvent = new SoundEvent(name);
		SOUND_EVENTS.put(soundEvent, name);
		return soundEvent;
	}
	
	/* Blocks */
	public static final SoundEvent block_moss_break = createSoundEvent("block.moss.break");
	public static final SoundEvent block_moss_step = createSoundEvent("block.moss.step");
	public static final SoundEvent block_moss_place = createSoundEvent("block.moss.place");
	public static final SoundEvent block_moss_hit = createSoundEvent("block.moss.hit");
	public static final SoundEvent block_moss_fall = createSoundEvent("block.moss.fall");

	public static final SoundEvent block_permafrost_break = createSoundEvent("block.permafrost.break");
	public static final SoundEvent block_permafrost_step = createSoundEvent("block.permafrost.step");
	public static final SoundEvent block_permafrost_place = createSoundEvent("block.permafrost.place");
	public static final SoundEvent block_permafrost_hit = createSoundEvent("block.permafrost.hit");
	public static final SoundEvent block_permafrost_fall = createSoundEvent("block.permafrost.fall");

	public static final SoundEvent block_dung_break = createSoundEvent("block.dung.break");
	public static final SoundEvent block_dung_step = createSoundEvent("block.dung.step");
	public static final SoundEvent block_dung_place = createSoundEvent("block.dung.place");
	public static final SoundEvent block_dung_hit = createSoundEvent("block.dung.hit");
	public static final SoundEvent block_dung_fall = createSoundEvent("block.dung.fall");
	
	public static final SoundEvent block_plant_break = createSoundEvent("block.plant.break");
	public static final SoundEvent block_plant_step = createSoundEvent("block.plant.step");
	public static final SoundEvent block_plant_place = createSoundEvent("block.plant.place");
	public static final SoundEvent block_plant_hit = createSoundEvent("block.plant.hit");
	public static final SoundEvent block_plant_fall = createSoundEvent("block.plant.fall");

	public static final SoundEvent block_fern_break = createSoundEvent("block.fern.break");
	public static final SoundEvent block_fern_step = createSoundEvent("block.fern.step");
	public static final SoundEvent block_fern_place = createSoundEvent("block.fern.place");
	public static final SoundEvent block_fern_hit = createSoundEvent("block.fern.hit");
	public static final SoundEvent block_fern_fall = createSoundEvent("block.fern.fall");

	public static final SoundEvent block_calamites_break = createSoundEvent("block.calamites.break");
	public static final SoundEvent block_calamites_step = createSoundEvent("block.calamites.step");
	public static final SoundEvent block_calamites_place = createSoundEvent("block.calamites.place");
	public static final SoundEvent block_calamites_hit = createSoundEvent("block.calamites.hit");
	public static final SoundEvent block_calamites_fall = createSoundEvent("block.calamites.fall");

	public static final SoundEvent block_roots_break = createSoundEvent("block.roots.break");
	public static final SoundEvent block_roots_step = createSoundEvent("block.roots.step");
	public static final SoundEvent block_roots_place = createSoundEvent("block.roots.place");
	public static final SoundEvent block_roots_hit = createSoundEvent("block.roots.hit");
	public static final SoundEvent block_roots_fall = createSoundEvent("block.roots.fall");

	public static final SoundEvent block_coral_break = createSoundEvent("block.coral.break");
	public static final SoundEvent block_coral_step = createSoundEvent("block.coral.step");
	public static final SoundEvent block_coral_place = createSoundEvent("block.coral.place");
	public static final SoundEvent block_coral_hit = createSoundEvent("block.coral.hit");
	public static final SoundEvent block_coral_fall = createSoundEvent("block.coral.fall");

	public static final SoundEvent block_aquatic_plant_break = createSoundEvent("block.aquatic_plant.break");
	public static final SoundEvent block_aquatic_plant_step = createSoundEvent("block.aquatic_plant.step");
	public static final SoundEvent block_aquatic_plant_place = createSoundEvent("block.aquatic_plant.place");
	public static final SoundEvent block_aquatic_plant_hit = createSoundEvent("block.aquatic_plant.hit");
	public static final SoundEvent block_aquatic_plant_fall = createSoundEvent("block.aquatic_plant.fall");

	public static final SoundEvent block_ooze_break = createSoundEvent("block.ooze.break");
	public static final SoundEvent block_ooze_step = createSoundEvent("block.ooze.step");
	public static final SoundEvent block_ooze_place = createSoundEvent("block.ooze.place");
	public static final SoundEvent block_ooze_hit = createSoundEvent("block.ooze.hit");
	public static final SoundEvent block_ooze_fall = createSoundEvent("block.ooze.fall");

	public static final SoundEvent block_cone_break = createSoundEvent("block.cone.break");
	public static final SoundEvent block_cone_step = createSoundEvent("block.cone.step");
	public static final SoundEvent block_cone_place = createSoundEvent("block.cone.place");
	public static final SoundEvent block_cone_hit = createSoundEvent("block.cone.hit");
	public static final SoundEvent block_cone_fall = createSoundEvent("block.cone.fall");

	public static final SoundEvent block_dead_log_break = createSoundEvent("block.dead_log.break");
	public static final SoundEvent block_dead_log_step = createSoundEvent("block.dead_log.step");
	public static final SoundEvent block_dead_log_place = createSoundEvent("block.dead_log.place");
	public static final SoundEvent block_dead_log_hit = createSoundEvent("block.dead_log.hit");
	public static final SoundEvent block_dead_log_fall = createSoundEvent("block.dead_log.fall");

	public static final SoundEvent block_mushroom_break = createSoundEvent("block.mushroom.break");
	public static final SoundEvent block_mushroom_step = createSoundEvent("block.mushroom.step");
	public static final SoundEvent block_mushroom_place = createSoundEvent("block.mushroom.place");
	public static final SoundEvent block_mushroom_hit = createSoundEvent("block.mushroom.hit");
	public static final SoundEvent block_mushroom_fall = createSoundEvent("block.mushroom.fall");

	public static final SoundEvent block_debris_break = createSoundEvent("block.debris.break");
	public static final SoundEvent block_debris_step = createSoundEvent("block.debris.step");
	public static final SoundEvent block_debris_place = createSoundEvent("block.debris.place");
	public static final SoundEvent block_debris_hit = createSoundEvent("block.debris.hit");
	public static final SoundEvent block_debris_fall = createSoundEvent("block.debris.fall");

	public static final SoundEvent block_pebble_break = createSoundEvent("block.pebble.break");
	public static final SoundEvent block_pebble_step = createSoundEvent("block.pebble.step");
	public static final SoundEvent block_pebble_place = createSoundEvent("block.pebble.place");
	public static final SoundEvent block_pebble_hit = createSoundEvent("block.pebble.hit");
	public static final SoundEvent block_pebble_fall = createSoundEvent("block.pebble.fall");

	public static final SoundEvent block_egg_meganeura_break = createSoundEvent("block.egg.meganeura.break");
	public static final SoundEvent block_egg_meganeura_place = createSoundEvent("block.egg.meganeura.place");

	public static final SoundEvent block_storage_box_open = createSoundEvent("block.storage_box.open");
	public static final SoundEvent block_storage_box_close = createSoundEvent("block.storage_box.close");

	/* Items */
	public static final SoundEvent item_flint_and_marcasite_use = createSoundEvent("item.flint_and_marcasite.use");
	public static final SoundEvent item_pebble_hit = createSoundEvent("item.pebble.hit");

	/* Mobs */
	public static final SoundEvent mob_meganeura_fly = createSoundEvent("mob.meganeura.fly");
	public static final SoundEvent mob_meganeura_land = createSoundEvent("mob.meganeura.land");
	public static final SoundEvent mob_meganeura_takeoff = createSoundEvent("mob.meganeura.takeoff");

	/* Player */
	public static final SoundEvent player_knapping_hit = createSoundEvent("player.knapping.hit");

	/* Music */
	public static final SoundEvent music_genesis = createSoundEvent("music.genesis");
	
	public static void registerAll()
	{
		SOUND_EVENTS.forEach((s, n) -> GameRegistry.register(s, n));
	}
}
