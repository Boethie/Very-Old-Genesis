package genesis.util;

public class Constants {
    public static final String MOD_ID = "genesis";
    public static final String MOD_NAME = "Project Genesis";
    public static final String MOD_VERSION = "@VERSION@";

    public static final String VERSIONS_URL = "https://raw.githubusercontent.com/GenProject/GenProject/master/versions.json";

    public static final String PROXY_LOCATION = "genesis.common.GenesisProxy";
    public static final String CLIENT_LOCATION = "genesis.client.GenesisClient";

    public static String setUnlocalizedName(String unlocalizedName) {
        return MOD_ID + "." + unlocalizedName;
    }
}
