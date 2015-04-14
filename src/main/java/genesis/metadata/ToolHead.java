package genesis.metadata;



import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.tuple.Pair;

public class ToolHead implements IMetadata
{
	static HashBiMap<Pair<EnumToolQuality, EnumToolMaterial>, ToolHead> bimap = HashBiMap.create();

	static
	{
		for (EnumToolMaterial material : EnumToolMaterial.values())
		{
			for (EnumToolQuality quality : EnumToolQuality.values())
			{
				bimap.put(Pair.of(quality, material), new ToolHead());
			}
		}
	}

	public Pair traits = bimap.inverse().get(this);
	public EnumToolMaterial material = (EnumToolMaterial) traits.getLeft();
	public EnumToolQuality quality = (EnumToolQuality) traits.getRight();

	public ToolHead getToolHead(EnumToolQuality quality, EnumToolMaterial material) {
		return bimap.get(Pair.of(quality,material));
	}

	@Override
	public String getName()
	{
		return quality.toString() + "_" + material.toString();
	}

	@Override
	public String getUnlocalizedName()
	{
		return quality.toString().toLowerCase() + (material.toString()).substring(0, 1).toUpperCase() + (material.toString());
	}
}
