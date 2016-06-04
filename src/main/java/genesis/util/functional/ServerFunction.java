package genesis.util.functional;

import genesis.common.GenesisProxy;

import net.minecraftforge.fml.relauncher.*;

@FunctionalInterface
public interface ServerFunction
{
	@SideOnly(Side.SERVER)
	void apply(GenesisProxy proxy);
}
