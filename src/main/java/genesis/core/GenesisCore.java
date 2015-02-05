package genesis.core;

import genesis.util.Constants;

import java.io.IOException;
import java.util.Map;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class GenesisCore extends AccessTransformer implements IFMLLoadingPlugin
{
	public GenesisCore() throws IOException
	{
		super(Constants.MOD_ID + "_at.cfg");
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
	}

	@Override
	public String getAccessTransformerClass()
	{
		return getClass().getName();
	}
}
