package genesis.util;

import genesis.common.GenesisProxy;

import net.minecraftforge.fml.relauncher.*;

public interface ServerFunction
{
	@SideOnly(Side.SERVER)
	public void apply(GenesisProxy proxy);
}
