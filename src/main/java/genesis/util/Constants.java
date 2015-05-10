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

	public static final String PREFIX = MOD_ID + ".";
	public static final String INVALID_METADATA = PREFIX + "multiItem.invalidMetadata";
	public static final String CONTAINER = "container." + PREFIX;
	
	public static final String ASSETS = MOD_ID + ":";

	public static final String VERSIONS_URL = "https://raw.githubusercontent.com/GenProject/GenProject/master/versions.json";

	public static final String PROXY_LOCATION = "genesis.common.GenesisProxy";
	public static final String CLIENT_LOCATION = "genesis.client.GenesisClient";
	
	public static final DamageSource CHANCELLORIA_DMG = new DamageSource("chancelloria");
}
