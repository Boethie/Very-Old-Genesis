package genesis.common;

import genesis.capabilities.GenesisCapabilities;
import genesis.command.CommandTPGenesis;
import genesis.common.sounds.GenesisSoundEvents;
import genesis.network.GenesisNetwork;
import genesis.stats.GenesisAchievements;
import genesis.util.Constants;
import genesis.world.GenesisWorldData;
import genesis.world.WorldGenerators;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import org.apache.logging.log4j.Logger;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, updateJSON = Constants.UPDATE_JSON, dependencies = "required-after:Forge")
public class Genesis
{
	@Mod.Instance(Constants.MOD_ID)
	public static Genesis instance;
	@SidedProxy(clientSide = Constants.CLIENT_LOCATION, serverSide = Constants.PROXY_LOCATION)
	public static GenesisProxy proxy;
	
	public static GenesisNetwork network;
	
	public static Logger logger;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		
		GenesisConfig.readConfigValues(event.getSuggestedConfigurationFile());
		
		network = new GenesisNetwork(Constants.MOD_ID);
		network.registerMessages();
		
		GenesisWorldData.register();
		
		GenesisSoundEvents.registerAll();
		
		GenesisPotions.register();
		
		GenesisFluids.registerFluids();
		GenesisBlocks.preInitCommon();
		GenesisItems.preInitCommon();
		
		GenesisCapabilities.register();
		
		GenesisEntities.registerEntities();
		
		GenesisRecipes.addRecipes();
		
		GenesisDimensions.register();
		
		GenesisBiomes.loadBiomes();
		
		WorldGenerators.register();
		
		GenesisEvent.init(event.getSide());
		
		GenesisAchievements.initAchievements();
		
		proxy.preInit();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		registerHandlers();
		
		proxy.init();
	}

	protected void registerHandlers()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GenesisGuiHandler());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
		
		GenesisRecipes.doSubstitutes();
	}
	
	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event){
		event.registerServerCommand(new CommandTPGenesis());
	}
}
