package genesis.common;

import genesis.metadata.*;
import genesis.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import org.apache.logging.log4j.Logger;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, dependencies = "required-after:Forge")
public class Genesis
{
	@Mod.Instance(Constants.MOD_ID)
	public static Genesis instance;
	@SidedProxy(clientSide = Constants.CLIENT_LOCATION, serverSide = Constants.PROXY_LOCATION)
	public static GenesisProxy proxy;

	public static Logger logger;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		
		GenesisVersion.startVersionCheck();
		
		GenesisConfig.readConfigValues(event.getSuggestedConfigurationFile());
		GenesisBlocks.registerBlocks();
		GenesisItems.registerItems();
		
		GenesisRecipes.addRecipes();
		
		registerEntities();
		
		GenesisBiomes.loadBiomes();
		
		proxy.preInit();
	}

	protected void registerEntities()
	{
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
}
