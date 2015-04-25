package genesis.client;

import net.minecraftforge.fml.relauncher.*;

public abstract class ClientOnlyFunction
{
	@SideOnly(Side.CLIENT)
	public abstract void apply(GenesisClient client);
}
