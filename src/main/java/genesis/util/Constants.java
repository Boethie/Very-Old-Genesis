package genesis.util;

import genesis.metadata.EnumCoral;
import genesis.metadata.EnumFern;
import genesis.metadata.EnumPlant;

import java.util.Random;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;

public final class Constants
{
	public static final Random RANDOM = new Random();

	public static final String MOD_ID = "genesis";
	public static final String MOD_NAME = "Project Genesis";
	public static final String MOD_VERSION = "@VERSION@";

	public static final String PREFIX = MOD_ID + ".";
	public static final String ASSETS = MOD_ID + ":";

	public static final String VERSIONS_URL = "https://raw.githubusercontent.com/GenProject/GenProject/master/versions.json";

	public static final String PROXY_LOCATION = "genesis.common.GenesisProxy";
	public static final String CLIENT_LOCATION = "genesis.client.GenesisClient";

	public static final PropertyEnum PLANT_VARIANT = PropertyEnum.create("variant", EnumPlant.class);
	public static final PropertyEnum FERN_VARIANT = PropertyEnum.create("variant", EnumFern.class);
    public static final PropertyInteger PROTOTAXITES_AGE = PropertyInteger.create("age", 0, 15);
	public static final PropertyEnum CORAL_VARIANT = PropertyEnum.create("variant", EnumCoral.class);
}
