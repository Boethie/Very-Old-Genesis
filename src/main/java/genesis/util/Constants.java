package genesis.util;

import java.util.Random;

import net.minecraft.util.DamageSource;

public final class Constants
{
	public static final Random RANDOM = new Random();
	
	public static final String MOD_ID = "genesis";		// Cannot change.
	public static final String MOD_NAME = "Genesis";	// Cannot change.
	public static final String MOD_VERSION = "@VERSION@";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/EversongMill/Genesis/master/update.json";
	
	/** USE ONLY FOR NON-ResourceLocation USAGE!!!<br>
	 * for ResourceLocations, use "new ResourceLocation(Constants.MOD_ID, location)"<br>
	 * This is for OPTIMIZATION purposes**/
	public static final String ASSETS_PREFIX = MOD_ID + ":"; 
	
	public static final String PROXY_LOCATION = "genesis.common.GenesisProxy";
	public static final String CLIENT_LOCATION = "genesis.client.GenesisClient";
	
	public static final DamageSource CHANCELLORIA_DMG = new DamageSource("chancelloria");
	
	private static final int TITLE_R = 43;
	private static final int TITLE_G = 39;
	private static final int TITLE_B = 15;
	public static final int TITLE_COLOUR = (TITLE_R << 16) | (TITLE_G << 8) | TITLE_B;
	
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
			public static final String SLAB = "slab.";
			public static final String WALL = "wall.";
			
			public static final String MATERIAL = "material.";
			
			public static final String TOOL = "tool.";
			public static final String TOOL_QUALITY = TOOL + "quality.";
			public static final String WEAPON = "weapon.";
			public static final String TOOL_HEAD = MATERIAL + "toolHead.";
			public static final String BOW = "bow.";
			
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
		public static final String WALL = PREFIX + Section.WALL;
		
		public static final String CLOTHING = PREFIX + Section.CLOTHING;
		
		public static final String FOOD = PREFIX + Section.FOOD;
		public static final String MISC = PREFIX + Section.MISC;

		public static final String EFFECT = "effect." + PREFIX;
	}
}
