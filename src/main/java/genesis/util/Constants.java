package genesis.util;

import java.util.Random;

import net.minecraft.util.DamageSource;

public final class Constants
{
	public static final Random RANDOM = new Random();

	public static final String MOD_ID = "genesis";
	public static final String MOD_NAME = "Project Genesis";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/GenProject/GenProject/master/update.json";
	
	public static final String ASSETS_PREFIX = MOD_ID + ":";
	
	public static final String PROXY_LOCATION = "genesis.common.GenesisProxy";
	public static final String CLIENT_LOCATION = "genesis.client.GenesisClient";
	
	public static final DamageSource CHANCELLORIA_DMG = new DamageSource("chancelloria");
	
	final static int r = 43;
	final static int g = 39;
	final static int b = 15;
	public static final int GUI_TITLE_COLOUR = (r << 16) | (g << 8) | b;
	
	public static final class Unlocalized
	{
		public static final class Section
		{
			public static final String ROCK = "rock.";
			public static final String ORE = "ore.";
			public static final String DOUBLE = "double.";
			public static final String PLANT = "plant.";
			public static final String PLANT_DOUBLE = PLANT + DOUBLE;
			public static final String FERN = "fern.";
			public static final String FERN_DOUBLE = FERN + DOUBLE;
			public static final String CROP = "crop.";
			
			public static final String MATERIAL = "material.";
			
			public static final String TOOL = "tool.";
			public static final String TOOL_QUALITY = TOOL + "quality.";
			public static final String WEAPON = "weapon.";
			public static final String TOOL_HEAD = MATERIAL + "toolHead.";
			
			public static final String EGG = MATERIAL + "egg.";
			
			public static final String CLOTHING = "clothing.";
			
			public static final String FOOD = "food.";
			public static final String MISC = "misc.";
		}
		
		public static final String PREFIX = MOD_ID + ".";
		public static final String ITEM_PREFIX = "item." + PREFIX;
		
		public static final String CONTAINER_BLOCK = PREFIX + "container.";
		public static final String CONTAINER_UI = "container." + PREFIX;
		
		public static final String INVALID_METADATA = PREFIX + "multiItem.invalidMetadata";
		
		public static final String MATERIAL = PREFIX + Section.MATERIAL;
		public static final String EGG = PREFIX + Section.EGG;
		
		public static final String ROCK = PREFIX + Section.ROCK;
		public static final String ORE = PREFIX + Section.ORE;
		public static final String PLANT = PREFIX + Section.PLANT;
		public static final String CROP = PREFIX + Section.CROP;
		
		public static final String CLOTHING = PREFIX + Section.CLOTHING;
		
		public static final String FOOD = PREFIX + Section.FOOD;
		public static final String MISC = PREFIX + Section.MISC;
	}
	
	public static final class Sounds
	{
		public static final String IGNITE_FIRE = ASSETS_PREFIX + "fire.ignite";
	}
}
