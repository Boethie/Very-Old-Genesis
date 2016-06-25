package genesis.combo.variant;

import java.util.List;

import com.google.common.collect.*;

public class ArrowTypes
{
	public static class ArrowType implements IArrowMetadata<ArrowType>
	{
		public final EnumArrowShaft shaft;
		public final EnumToolMaterial tip;
		
		public ArrowType(EnumArrowShaft shaft, EnumToolMaterial tip)
		{
			this.shaft = shaft;
			this.tip = tip;
		}
		
		@Override
		public String getName()
		{
			return tip.getName() + "_tipped_" + shaft.getName();
		}
		
		@Override
		public String getUnlocalizedName()
		{
			return shaft.getUnlocalizedName();
		}
		
		public EnumArrowShaft getShaft()
		{
			return shaft;
		}
		
		public EnumToolMaterial getTip()
		{
			return tip;
		}
		
		@Override
		public float getMass()
		{
			return shaft.getMass() + tip.getMass();
		}
		
		@Override
		public String toString()
		{
			return getName();
		}
		
		@Override
		public int compareTo(ArrowType o)
		{
			int materialCompare = shaft.compareTo(o.shaft);
			
			if (materialCompare != 0)
				return materialCompare;
			
			return tip.compareTo(o.tip);
		}
	}
	
	protected static final ImmutableTable<EnumArrowShaft, EnumToolMaterial, ArrowType> TABLE;
	
	static
	{
		ImmutableTable.Builder<EnumArrowShaft, EnumToolMaterial, ArrowType> builder = ImmutableTable.builder();
		
		for (EnumArrowShaft shaft : EnumArrowShaft.values())
		{
			for (EnumToolMaterial material : EnumToolMaterial.values())
			{
				ArrowType arrowType = new ArrowType(shaft, material);
				builder.put(shaft, material, arrowType);
			}
		}
		
		TABLE = builder.build();
	}
	
	public static ArrowType getToolHead(EnumArrowShaft shaft, EnumToolMaterial material)
	{
		return TABLE.get(shaft, material);
	}
	
	public static List<ArrowType> getAll()
	{
		return ImmutableList.copyOf(TABLE.values());
	}
}
