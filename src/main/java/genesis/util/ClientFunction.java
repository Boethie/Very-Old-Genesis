package genesis.util;

import genesis.client.GenesisClient;

import net.minecraftforge.fml.relauncher.*;

public interface ClientFunction
{
	@SideOnly(Side.CLIENT)
	public void apply(GenesisClient client);
}
