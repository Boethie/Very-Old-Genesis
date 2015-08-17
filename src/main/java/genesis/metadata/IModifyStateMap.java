package genesis.metadata;

import genesis.util.FlexibleStateMap;
import net.minecraft.client.renderer.block.statemap.StateMap;

public interface IModifyStateMap
{
	public void customizeStateMap(FlexibleStateMap stateMap);
}
