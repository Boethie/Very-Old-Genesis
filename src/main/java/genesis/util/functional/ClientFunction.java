package genesis.util.functional;

import genesis.client.GenesisClient;

import net.minecraftforge.fml.relauncher.*;

@FunctionalInterface
public interface ClientFunction
{
	@SideOnly(Side.CLIENT)
	public void apply(GenesisClient client);
}
