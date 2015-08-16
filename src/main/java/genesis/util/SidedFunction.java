package genesis.util;

import genesis.client.GenesisClient;
import genesis.common.GenesisProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class SidedFunction
{
	@SideOnly(Side.CLIENT)
	public abstract void client(GenesisClient client);
	
	@SideOnly(Side.SERVER)
	public void server(GenesisProxy proxy)
	{
	}
}
