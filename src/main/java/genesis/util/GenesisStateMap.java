package genesis.util;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;

public class GenesisStateMap implements IStateMapper
{
	protected StateMap stateMap;
	
	public GenesisStateMap(StateMap map)
	{
		stateMap = map;
	}

	@Override
	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block block)
	{
		Map<IBlockState, ModelResourceLocation> map = stateMap.putStateModelLocations(block);
		
		for (Entry<IBlockState, ModelResourceLocation> entry : map.entrySet())
		{
			ModelResourceLocation loc = entry.getValue();
			String domain = loc.getResourceDomain();
			String path = loc.getResourcePath();
			String variant = loc.getVariant();
			
			if (!"genesis".equals(domain))
			{
				domain = "genesis";
			}
			
			loc = new ModelResourceLocation(domain + ":" + path + "#" + variant);
			entry.setValue(loc);
		}
		
		return map;
	}
}
