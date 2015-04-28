package genesis.metadata;

import java.util.*;

import com.google.common.collect.*;

public class ToolTypes
{
	public static class ToolType implements IMetadata
	{
		public final EnumToolMaterial material;
		public final EnumToolQuality quality;
		
		public ToolType(EnumToolMaterial material, EnumToolQuality quality)
		{
			this.material = material;
			this.quality = quality;
		}

		@Override
		public String getName()
		{
			String qualityName = quality.getName();
			
			return (qualityName == null ? "" : qualityName + "_") + material.getName();
		}

		@Override
		public String getUnlocalizedName()
		{
			return material.getUnlocalizedName();
		}
		
		@Override
		public String toString()
		{
			return super.toString() + "[quality=" + quality + ", material" + material + "]";
		}
	}
	
	// Comparator to sort the table to the correct order.
	private static final Comparator<Enum> sorter = new Comparator<Enum>()
			{
				@Override
				public int compare(Enum m1, Enum m2)
				{
					return Integer.compare(m1.ordinal(), m2.ordinal());
				}
			};
	public static final Table<EnumToolMaterial, EnumToolQuality, ToolType> map = TreeBasedTable.create(sorter, sorter);

	static
	{
		for (EnumToolMaterial material : EnumToolMaterial.values())
		{
			for (EnumToolQuality quality : EnumToolQuality.values())
			{
				ToolType toolType = new ToolType(material, quality);
				map.put(material, quality, toolType);
			}
		}
	}

	public static ToolType getToolHead(EnumToolMaterial material, EnumToolQuality quality)
	{
		return map.get(material, quality);
	}
	
	public static ToolType[] getAll()
	{
		Collection<ToolType> types = map.values();
		return types.toArray(new ToolType[types.size()]);
	}
}
