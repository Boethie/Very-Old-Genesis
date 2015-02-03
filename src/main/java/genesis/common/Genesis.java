package genesis.common;

<<<<<<< HEAD
import java.io.File;

=======
import genesis.metadata.EnumCoral;
import genesis.metadata.EnumDung;
import genesis.metadata.EnumFern;
import genesis.metadata.EnumNodule;
import genesis.metadata.EnumPebble;
import genesis.metadata.EnumPlant;
>>>>>>> origin/master
import genesis.util.Constants;
import genesis.world.CommandTestTeleportGenesis;
import genesis.world.WorldProviderGenesis;
import genesis.world.WorldTypeGenesis;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import org.apache.logging.log4j.Logger;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, dependencies = "required-after:Forge")
public class Genesis
{
	@Mod.Instance(Constants.MOD_ID)
	public static Genesis instance;
	@SidedProxy(clientSide = Constants.CLIENT_LOCATION, serverSide = Constants.PROXY_LOCATION)
	public static GenesisProxy proxy;

	public static Logger logger;

	public static int dimensionId = 37;

	public static File configFolder;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();

		configFolder = new File(event.getSuggestedConfigurationFile().getParentFile(), "Genesis");

		if(!configFolder.exists()) {
			configFolder.mkdir();
		}

		GenesisVersion.startVersionCheck();
		GenesisConfig.readConfigValues(event.getSuggestedConfigurationFile());

		initEnums();
		GenesisBlocks.registerBlocks();
		GenesisItems.registerItems();

		registerTileEntities();

		//Register the biomes.
		GenesisBiomes.config();
		GenesisBiomes.loadBiomes();

		registerEntities();

		//Register the dimension.
		WorldTypeGenesis.instance = new WorldTypeGenesis("Genesis");
		DimensionManager.registerProviderType(dimensionId, WorldProviderGenesis.class, false);
		DimensionManager.registerDimension(dimensionId, dimensionId);


		proxy.preInit();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		GenesisRecipes.addRecipes();

		registerHandlers();

		proxy.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}
	
	private void initEnums()
	{
		EnumPlant.values();
		EnumFern.values();
		EnumCoral.values();
		EnumDung.values();
		EnumPebble.values();
		EnumNodule.values();
	}

	@Mod.EventHandler
	public void serverLoaded(FMLServerStartingEvent event) {

		//Added by Tmtravlr: Adds a command that can teleport you to genesis.
		if(event.getServer().getCommandManager() instanceof ServerCommandManager) {
			ServerCommandManager manager = (ServerCommandManager) event.getServer().getCommandManager();

			manager.registerCommand(new CommandTestTeleportGenesis());
		}
	}

	private void registerTileEntities()
	{
	}

	private void registerEntities()
	{
	}

	private void registerHandlers()
	{
	}
}
