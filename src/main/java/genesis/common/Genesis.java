package genesis.common;

import genesis.command.CommandTPGenesis;
import genesis.entity.extendedproperties.GenesisEntityData;
import genesis.util.Constants;
import genesis.world.GenesisWorldData;
import genesis.world.OverworldGeneration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
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
		
		GenesisFluids.registerFluids();
		GenesisBlocks.registerBlocks();
		GenesisItems.registerItems();
		GenesisEntityData.register();
		GenesisSounds.register();
		
		GenesisEntities.registerEntities();
		
		GenesisRecipes.addRecipes();
		
		GenesisDimensions.register();
		
		GenesisBiomes.loadBiomes();
		
		OverworldGeneration.register();
		
		GenesisEvent.init(event.getSide());
		
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
