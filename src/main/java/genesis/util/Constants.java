package genesis.util;

import genesis.metadata.EnumAquaticPlant;
import genesis.metadata.EnumCoral;
import genesis.metadata.EnumDung;
import genesis.metadata.EnumFern;
import genesis.metadata.EnumPlant;

import java.util.Random;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.DamageSource;

public final class Constants
{
	public static final Random RANDOM = new Random();

	public static final String MOD_ID = "genesis";
	public static final String MOD_NAME = "Project Genesis";
	public static final String MOD_VERSION = "@VERSION@";

	public static final String ASSETS_PREFIX = MOD_ID + ":";

	public static final String VERSIONS_URL = "https://raw.githubusercontent.com/GenProject/GenProject/master/versions.json";

	public static final String PROXY_LOCATION = "genesis.common.GenesisProxy";
	public static final String CLIENT_LOCATION = "genesis.client.GenesisClient";
	
	public static final DamageSource CHANCELLORIA_DMG = new DamageSource("chancelloria");
	
	public static final class Unlocalized
	{
		public static final String PREFIX = MOD_ID + ".";
		
		public static final String INVALID_METADATA = PREFIX + "multiItem.invalidMetadata";
		public static final String CONTAINER = "container." + PREFIX;
		
		public static final String ROCK = PREFIX + "rock.";
		public static final String ORE = PREFIX + "ore.";
		public static final String PLANT = PREFIX + "plant.";
		public static final String CROP = PREFIX + "crop.";
		public static final String TOOL = PREFIX + "tool.";
		public static final String TOOL_QUALITY = TOOL + "quality.";
		public static final String WEAPON = PREFIX + "weapon.";
		public static final String MATERIAL = PREFIX + "material.";
		public static final String MISC = PREFIX + "misc.";
		public static final String FOOD = PREFIX + "food.";
	}
}
