package genesis.client;

import genesis.client.render.RenderPortals;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class GenesisReloadListener implements IResourceManagerReloadListener
{
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		RenderPortals.load();
	}
}