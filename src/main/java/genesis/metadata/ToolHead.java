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

	public ToolHead getToolHead(EnumToolQuality quality, EnumToolMaterial material) {
		return bimap.get(Pair.of(quality,material));
	}

	public Pair getTraits() {
		return bimap.inverse().get(this);
	}

	@Override
	public String getName()
	{
		Pair traits = getTraits();
		return traits.getLeft() + "_" + traits.getRight();
	}

	@Override
	public String getUnlocalizedName()
	{
		Pair traits = getTraits();
		return traits.getLeft().toString().toLowerCase() + (traits.getLeft().toString()).substring(0, 1).toUpperCase() + (traits.getRight().toString());
	}
}
