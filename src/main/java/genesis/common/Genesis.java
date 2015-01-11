package genesis.common;

import genesis.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, dependencies = "required-after:Forge")
public class Genesis {
    public static final Random random = new Random();

    @Mod.Instance(Constants.MOD_ID)
    private static Genesis instance;
    @SidedProxy(clientSide = Constants.CLIENT_LOCATION, serverSide = Constants.PROXY_LOCATION)
    private static GenesisProxy proxy;

    private Logger logger;

    public static Genesis getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return getInstance().logger;
    }

    public static GenesisProxy getProxy() {
        return proxy;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        GenesisVersion.startVersionCheck();
        GenesisConfig.readConfigValues(event.getSuggestedConfigurationFile());

        GenesisBlocks.registerBlocks();
        GenesisItems.registerItems();
        GenesisBlocks.registerBlocks();

        registerTileEntities();

        GenesisBiomes.loadBiomes();

        registerEntities();

        getProxy().preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GenesisRecipes.addRecipes();

        registerHandlers();

        getProxy().init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        getProxy().postInit();

        getLogger().info("Version status: " + GenesisVersion.getStatus().toString());
    }

    private void registerTileEntities() {}

    private void registerEntities() {}

    private void registerHandlers() {}
}
