package genesis.util;

import genesis.common.GenesisProxy;

import net.minecraftforge.fml.relauncher.*;

@FunctionalInterface
public interface ServerFunction
{
	@SideOnly(Side.SERVER)
	public void apply(GenesisProxy proxy);
}
