package genesis.metadata;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

public class ToolTypes
{
	public static class ToolType implements IMetadata
	{
		public final EnumToolQuality quality;
		public final EnumToolMaterial material;
		
		public ToolType(EnumToolQuality quality, EnumToolMaterial material)
		{
			this.quality = quality;
			this.material = material;
		}

		@Override
		public String getName()
		{
			String materialName = quality.getName();
			
			return (materialName == null ? "" : materialName + "_") + material.getName();
		}

		@Override
		public String getUnlocalizedName()
		{
			String materialName = quality.getUnlocalizedName();
			
			return (materialName == null ? "" : materialName + ".") + material.getUnlocalizedName();
		}
		
		// TODO: toString() method
	}
	
	public static final LinkedHashMap<Pair<EnumToolQuality, EnumToolMaterial>, ToolType> map = new LinkedHashMap();

	public static ToolType getToolHead(EnumToolQuality quality, EnumToolMaterial material) {
		return map.get(Pair.of(quality, material));
	}

	static
	{
		for (EnumToolMaterial material : EnumToolMaterial.values())
		{
			for (EnumToolQuality quality : EnumToolQuality.values())
			{
				ToolType toolType = new ToolType(quality, material);
				map.put(Pair.of(quality, material), toolType);
			}
		}
		
		map.getClass();
	}

	public static ToolType[] getAll()
	{
		Collection<ToolType> types = map.values();
		return types.toArray(new ToolType[types.size()]);
	}
}
