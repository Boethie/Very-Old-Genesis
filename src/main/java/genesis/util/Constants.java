package genesis.util;

import java.util.Random;

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
		public static final class Section
		{
			public static final String ROCK = "rock.";
			public static final String ORE = "ore.";
			public static final String PLANT = "plant.";
			public static final String CROP = "crop.";
			
			public static final String MATERIAL = "material.";
			public static final String TOOL = "tool.";
			public static final String TOOL_QUALITY = TOOL + "quality.";
			public static final String WEAPON = "weapon.";
			public static final String TOOL_HEAD = MATERIAL + "toolHead.";
			
			public static final String FOOD = "food.";
			public static final String MISC = "misc.";
		}
		
		public static final String PREFIX = MOD_ID + ".";
		
		public static final String CONTAINER = "container." + PREFIX;
		
		public static final String INVALID_METADATA = PREFIX + "multiItem.invalidMetadata";

		public static final String MATERIAL = PREFIX + Section.MATERIAL;
		
		public static final String ROCK = PREFIX + Section.ROCK;
		public static final String ORE = PREFIX + Section.ORE;
		public static final String PLANT = PREFIX + Section.PLANT;
		public static final String CROP = PREFIX + Section.CROP;
		
		public static final String MISC = PREFIX + Section.MISC;
		public static final String FOOD = PREFIX + Section.FOOD;
	}
}
