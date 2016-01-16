package genesis.combo.variant;

import java.util.*;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

import com.google.common.collect.*;

public class ToolTypes
{
	public static class ToolType implements IMetadata<ToolType>
	{
		public final EnumToolMaterial material;
		public final EnumToolQuality quality;
		public final ToolMaterial toolMaterial;
		
		public ToolType(EnumToolMaterial material, EnumToolQuality quality)
		{
			this.material = material;
			this.quality = quality;
			
			// ToolMaterial init
			int usesBase = material.getUses();
			float usesMult = quality.getUsesMult();
			
			float efficiencyBase = material.getEfficiency();
			float efficiencyMult = quality.getEfficiencyMult();
			
			float damageBase = material.getEntityDamage();
			float damageMult = quality.getEntityDamageMult();
			
			int enchantBase = material.getEnchantability();
			float enchantMult = quality.getEnchantabilityMult();
			
			if (usesBase >= 0 && usesMult >= 0 &&
				efficiencyBase >= 0 && efficiencyMult >= 0 &&
				damageBase >= 0 && damageMult >= 0 &&
				enchantBase >= 0 && enchantMult >= 0)
			{
				int uses = Math.round(usesBase * usesMult);
				float efficiency = efficiencyBase * efficiencyMult;
				float entityDamage = damageBase * damageMult;
				int enchantability = Math.round(enchantBase * enchantMult);
				
				this.toolMaterial = EnumHelper.addToolMaterial(getName(),
						material.getHarvestLevel(), uses, efficiency, entityDamage, enchantability);
			}
			else
			{
				this.toolMaterial = null;
			}
		}
		
		@Override
		public String getName()
		{
			String qualityName = quality.getName();
			
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
			return (quality.toString().equals("") ? "" : "quality=" + quality + ", material=") + material;
		}
		
		@Override
		public int compareTo(ToolType o)
		{
			ToolType other = o;
			int materialCompare = material.compareTo(other.material);
			
			if (materialCompare != 0)
			{
				return materialCompare;
			}
			
			return quality.compareTo(other.quality);
		}
	}
	
	// Comparator to sort the table to the correct order.
	private static final Comparator<Enum<?>> sorter = new Comparator<Enum<?>>()
			{
				@Override
				public int compare(Enum<?> m1, Enum<?> m2)
				{
					return Integer.valueOf(m1.ordinal()).compareTo(Integer.valueOf(m2.ordinal()));
				}
			};
	protected static final TreeBasedTable<EnumToolMaterial, EnumToolQuality, ToolType> table = TreeBasedTable.create(sorter, sorter);
	
	static
	{
		for (EnumToolMaterial material : EnumToolMaterial.values())
		{
			for (EnumToolQuality quality : EnumToolQuality.values())
			{
				ToolType toolType = new ToolType(material, quality);
				table.put(material, quality, toolType);
			}
		}
	}
	
	public static ToolType getToolHead(EnumToolMaterial material, EnumToolQuality quality)
	{
		return table.get(material, quality);
	}
	
	public static List<ToolType> getAll()
	{
		return ImmutableList.copyOf(table.values());
	}
	
	public static Collection<ToolType> getToolTypes(EnumToolQuality quality)
	{
		return table.column(quality).values();
	}
}
