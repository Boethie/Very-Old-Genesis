package genesis;

import genesis.common.*;
import genesis.util.Constants;
import net.minecraftforge.common.MinecraftForge;
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
    public static Genesis instance;
    @SidedProxy(clientSide = Constants.CLIENT_LOCATION, serverSide = Constants.PROXY_LOCATION)
    public static GenesisProxy proxy;

    public static Logger logger;

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

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GenesisRecipes.addRecipes();

        registerHandlers();

        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();

        logger.info("Version status: " + GenesisVersion.status.toString());
    }

    private void registerTileEntities() {
    }

    private void registerEntities() {
    }

    private void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new GenesisEventHandler());
    }
}
