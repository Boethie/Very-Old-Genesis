package genesis.common.sounds;

import genesis.util.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

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
	public static final SoundEvent BLOCK_MOSS_BREAK = createSoundEvent("block.moss.break");
	public static final SoundEvent BLOCK_MOSS_STEP = createSoundEvent("block.moss.step");
	public static final SoundEvent BLOCK_MOSS_PLACE = createSoundEvent("block.moss.place");
	public static final SoundEvent BLOCK_MOSS_HIT = createSoundEvent("block.moss.hit");
	public static final SoundEvent BLOCK_MOSS_FALL = createSoundEvent("block.moss.fall");

	public static final SoundEvent BLOCK_PERMAFROST_BREAK = createSoundEvent("block.permafrost.break");
	public static final SoundEvent BLOCK_PERMAFROST_STEP = createSoundEvent("block.permafrost.step");
	public static final SoundEvent BLOCK_PERMAFROST_PLACE = createSoundEvent("block.permafrost.place");
	public static final SoundEvent BLOCK_PERMAFROST_HIT = createSoundEvent("block.permafrost.hit");
	public static final SoundEvent BLOCK_PERMAFROST_FALL = createSoundEvent("block.permafrost.fall");

	public static final SoundEvent BLOCK_DUNG_BREAK = createSoundEvent("block.dung.break");
	public static final SoundEvent BLOCK_DUNG_STEP = createSoundEvent("block.dung.step");
	public static final SoundEvent BLOCK_DUNG_PLACE = createSoundEvent("block.dung.place");
	public static final SoundEvent BLOCK_DUNG_HIT = createSoundEvent("block.dung.hit");
	public static final SoundEvent BLOCK_DUNG_FALL = createSoundEvent("block.dung.fall");

	public static final SoundEvent BLOCK_PLANT_BREAK = createSoundEvent("block.plant.break");
	public static final SoundEvent BLOCK_PLANT_STEP = createSoundEvent("block.plant.step");
	public static final SoundEvent BLOCK_PLANT_PLACE = createSoundEvent("block.plant.place");
	public static final SoundEvent BLOCK_PLANT_HIT = createSoundEvent("block.plant.hit");
	public static final SoundEvent BLOCK_PLANT_FALL = createSoundEvent("block.plant.fall");

	public static final SoundEvent BLOCK_FERN_BREAK = createSoundEvent("block.fern.break");
	public static final SoundEvent BLOCK_FERN_STEP = createSoundEvent("block.fern.step");
	public static final SoundEvent BLOCK_FERN_PLACE = createSoundEvent("block.fern.place");
	public static final SoundEvent BLOCK_FERN_HIT = createSoundEvent("block.fern.hit");
	public static final SoundEvent BLOCK_FERN_FALL = createSoundEvent("block.fern.fall");

	public static final SoundEvent BLOCK_CALAMITES_BREAK = createSoundEvent("block.calamites.break");
	public static final SoundEvent BLOCK_CALAMITES_STEP = createSoundEvent("block.calamites.step");
	public static final SoundEvent BLOCK_CALAMITES_PLACE = createSoundEvent("block.calamites.place");
	public static final SoundEvent BLOCK_CALAMITES_HIT = createSoundEvent("block.calamites.hit");
	public static final SoundEvent BLOCK_CALAMITES_FALL = createSoundEvent("block.calamites.fall");

	public static final SoundEvent BLOCK_ROOTS_BREAK = createSoundEvent("block.roots.break");
	public static final SoundEvent BLOCK_ROOTS_STEP = createSoundEvent("block.roots.step");
	public static final SoundEvent BLOCK_ROOTS_PLACE = createSoundEvent("block.roots.place");
	public static final SoundEvent BLOCK_ROOTS_HIT = createSoundEvent("block.roots.hit");
	public static final SoundEvent BLOCK_ROOTS_FALL = createSoundEvent("block.roots.fall");

	public static final SoundEvent BLOCK_CORAL_BREAK = createSoundEvent("block.coral.break");
	public static final SoundEvent BLOCK_CORAL_STEP = createSoundEvent("block.coral.step");
	public static final SoundEvent BLOCK_CORAL_PLACE = createSoundEvent("block.coral.place");
	public static final SoundEvent BLOCK_CORAL_HIT = createSoundEvent("block.coral.hit");
	public static final SoundEvent BLOCK_CORAL_FALL = createSoundEvent("block.coral.fall");

	public static final SoundEvent BLOCK_AQUATIC_PLANT_BREAK = createSoundEvent("block.aquatic_plant.break");
	public static final SoundEvent BLOCK_AQUATIC_PLANT_STEP = createSoundEvent("block.aquatic_plant.step");
	public static final SoundEvent BLOCK_AQUATIC_PLANT_PLACE = createSoundEvent("block.aquatic_plant.place");
	public static final SoundEvent BLOCK_AQUATIC_PLANT_HIT = createSoundEvent("block.aquatic_plant.hit");
	public static final SoundEvent BLOCK_AQUATIC_PLANT_FALL = createSoundEvent("block.aquatic_plant.fall");

	public static final SoundEvent BLOCK_OOZE_BREAK = createSoundEvent("block.ooze.break");
	public static final SoundEvent BLOCK_OOZE_STEP = createSoundEvent("block.ooze.step");
	public static final SoundEvent BLOCK_OOZE_PLACE = createSoundEvent("block.ooze.place");
	public static final SoundEvent BLOCK_OOZE_HIT = createSoundEvent("block.ooze.hit");
	public static final SoundEvent BLOCK_OOZE_FALL = createSoundEvent("block.ooze.fall");

	public static final SoundEvent BLOCK_CONE_BREAK = createSoundEvent("block.cone.break");
	public static final SoundEvent BLOCK_CONE_STEP = createSoundEvent("block.cone.step");
	public static final SoundEvent BLOCK_CONE_PLACE = createSoundEvent("block.cone.place");
	public static final SoundEvent BLOCK_CONE_HIT = createSoundEvent("block.cone.hit");
	public static final SoundEvent BLOCK_CONE_FALL = createSoundEvent("block.cone.fall");

	public static final SoundEvent BLOCK_DEAD_LOG_BREAK = createSoundEvent("block.dead_log.break");
	public static final SoundEvent BLOCK_DEAD_LOG_STEP = createSoundEvent("block.dead_log.step");
	public static final SoundEvent BLOCK_DEAD_LOG_PLACE = createSoundEvent("block.dead_log.place");
	public static final SoundEvent BLOCK_DEAD_LOG_HIT = createSoundEvent("block.dead_log.hit");
	public static final SoundEvent BLOCK_DEAD_LOG_FALL = createSoundEvent("block.dead_log.fall");

	public static final SoundEvent BLOCK_MUSHROOM_BREAK = createSoundEvent("block.mushroom.break");
	public static final SoundEvent BLOCK_MUSHROOM_STEP = createSoundEvent("block.mushroom.step");
	public static final SoundEvent BLOCK_MUSHROOM_PLACE = createSoundEvent("block.mushroom.place");
	public static final SoundEvent BLOCK_MUSHROOM_HIT = createSoundEvent("block.mushroom.hit");
	public static final SoundEvent BLOCK_MUSHROOM_FALL = createSoundEvent("block.mushroom.fall");

	public static final SoundEvent BLOCK_DEBRIS_BREAK = createSoundEvent("block.debris.break");
	public static final SoundEvent BLOCK_DEBRIS_STEP = createSoundEvent("block.debris.step");
	public static final SoundEvent BLOCK_DEBRIS_PLACE = createSoundEvent("block.debris.place");
	public static final SoundEvent BLOCK_DEBRIS_HIT = createSoundEvent("block.debris.hit");
	public static final SoundEvent BLOCK_DEBRIS_FALL = createSoundEvent("block.debris.fall");

	public static final SoundEvent BLOCK_PEBBLE_BREAK = createSoundEvent("block.pebble.break");
	public static final SoundEvent BLOCK_PEBBLE_STEP = createSoundEvent("block.pebble.step");
	public static final SoundEvent BLOCK_PEBBLE_PLACE = createSoundEvent("block.pebble.place");
	public static final SoundEvent BLOCK_PEBBLE_HIT = createSoundEvent("block.pebble.hit");
	public static final SoundEvent BLOCK_PEBBLE_FALL = createSoundEvent("block.pebble.fall");

	public static final SoundEvent BLOCK_EGG_MEGANEURA_BREAK = createSoundEvent("block.egg.meganeura.break");
	public static final SoundEvent BLOCK_EGG_MEGANEURA_PLACE = createSoundEvent("block.egg.meganeura.place");

	public static final SoundEvent BLOCK_STORAGE_BOX_OPEN = createSoundEvent("block.storage_box.open");
	public static final SoundEvent BLOCK_STORAGE_BOX_CLOSE = createSoundEvent("block.storage_box.close");

	/* Items */
	public static final SoundEvent ITEM_FLINT_AND_MARCASITE_USE = createSoundEvent("item.flint_and_marcasite.use");
	public static final SoundEvent ITEM_PEBBLE_HIT = createSoundEvent("item.pebble.hit");

	/* Mobs */
	public static final SoundEvent MOB_MEGANEURA_FLY = createSoundEvent("mob.meganeura.fly");
	public static final SoundEvent MOB_MEGANEURA_LAND = createSoundEvent("mob.meganeura.land");
	public static final SoundEvent MOB_MEGANEURA_TAKEOFF = createSoundEvent("mob.meganeura.takeoff");

	/* Player */
	public static final SoundEvent PLAYER_KNAPPING_HIT = createSoundEvent("player.knapping.hit");

	/* Music */
	public static final SoundEvent MUSIC_GENESIS = createSoundEvent("music.genesis");

	public static void registerAll()
	{
		SOUND_EVENTS.forEach(GameRegistry::register);
	}
}
