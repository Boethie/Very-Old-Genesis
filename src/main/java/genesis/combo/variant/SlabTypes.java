package genesis.combo.variant;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import genesis.block.BlockGenesisSlab;
import java.util.List;

public class SlabTypes
{
	public static class SlabType implements IMetadata<SlabType>
	{
		public final EnumSlabMaterial material;
		public final BlockGenesisSlab.EnumHalf half;

		public SlabType(EnumSlabMaterial material, BlockGenesisSlab.EnumHalf half)
		{
			this.material = material;
			this.half = half;
		}
		
		@Override
		public String getName()
		{
			String qualityName = half.getName();
			
			return ("".equals(qualityName) ? "" : qualityName + "_") + material.getName();
		}
		
		@Override
		public String getUnlocalizedName()
		{
			return material.getUnlocalizedName();
		}
		
		@Override
		public String toString()
		{
			return (half.toString().equals("") ? "" : "half=" + half + ", material=") + material;
		}
		
		@Override
		public int compareTo(SlabType o)
		{
			int materialCompare = material.compareTo(o.material);
			
			if (materialCompare != 0)
			{
				return materialCompare;
			}
			
			return half.compareTo(o.half);
		}
	}
	
	protected static final ImmutableTable<EnumSlabMaterial, BlockGenesisSlab.EnumHalf, SlabType> TABLE;
	
	static
	{
		ImmutableTable.Builder<EnumSlabMaterial, BlockGenesisSlab.EnumHalf, SlabType> builder = ImmutableTable.builder();
		
		for (EnumSlabMaterial material : EnumSlabMaterial.values())
		{
			for (BlockGenesisSlab.EnumHalf half : BlockGenesisSlab.EnumHalf.values())
			{
				SlabType slabType = new SlabType(material, half);
				builder.put(material, half, slabType);
			}
		}
		
		TABLE = builder.build();
	}
	
	public static SlabType getSlabType(EnumSlabMaterial material, BlockGenesisSlab.EnumHalf half)
	{
		return TABLE.get(material, half);
	}
	
	public static List<SlabType> getAll()
	{
		return ImmutableList.copyOf(TABLE.values());
	}
}
