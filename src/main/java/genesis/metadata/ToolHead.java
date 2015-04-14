package genesis.metadata;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

public class ToolHead implements IMetadata
{
	static HashMap<Pair<EnumToolQuality, EnumToolMaterial>, ToolHead> map = new HashMap();

	public static ToolHead getToolHead(EnumToolQuality quality, EnumToolMaterial material) {
		return map.get(Pair.of(quality, material));
	}

	static
	{
		for (EnumToolMaterial material : EnumToolMaterial.values())
		{
			for (EnumToolQuality quality : EnumToolQuality.values())
			{
				map.put(Pair.of(quality, material), new ToolHead(material, quality));
			}
		}
	}

	public final EnumToolMaterial material;
	public final EnumToolQuality quality;
	
	public ToolHead(EnumToolMaterial material, EnumToolQuality quality)
	{
		this.material = material;
		this.quality = quality;
	}

	@Override
	public String getName()
	{
		return quality.toString() + "_" + material.toString();
	}

	@Override
	public String getUnlocalizedName()
	{
		return quality.toString().toLowerCase() + material.toString().substring(0, 1).toUpperCase() + material.toString().substring(1);
	}
}
