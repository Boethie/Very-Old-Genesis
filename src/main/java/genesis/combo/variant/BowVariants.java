package genesis.combo.variant;

import java.util.List;

import com.google.common.collect.*;

import genesis.util.Constants.Unlocalized;

public class BowVariants
{
	public static class BowVariant implements IBowMetadata<BowVariant>
	{
		private final EnumBowType type;
		private final EnumTree variant;
		
		private BowVariant(EnumBowType type, EnumTree variant)
		{
			this.type = type;
			this.variant = variant;
		}
		
		@Override
		public String getName()
		{
			return variant.getName() + "_" + type.getName() + "_bow";
		}
		
		@Override
		public String getUnlocalizedName()
		{
			return Unlocalized.Section.BOW + type.getUnlocalizedName() + "." + variant.getUnlocalizedName();
		}
		
		@Override
		public int getDurability()
		{
			return Math.round(type.getDurability() * variant.getBowDurability());
		}
		
		@Override
		public int getDraw()
		{
			return Math.round(type.getDraw() * variant.getBowDraw());
		}
		
		@Override
		public float getVelocity()
		{
			return type.getVelocity() * variant.getBowVelocity();
		}
		
		@Override
		public float getDamage()
		{
			return type.getDamage() * variant.getBowDamage();
		}
		
		@Override
		public float getSpread()
		{
			return type.getSpread() * type.getSpread();
		}
		
		@Override
		public int compareTo(BowVariant o)
		{
			int materialCompare = type.compareTo(o.type);
			
			if (materialCompare != 0)
			{
				return materialCompare;
			}
			
			return variant.compareTo(o.variant);
		}
	}
	
	protected static final ImmutableTable<EnumBowType, EnumTree, BowVariant> TABLE;
	
	static
	{
		ImmutableTable.Builder<EnumBowType, EnumTree, BowVariant> builder = ImmutableTable.builder();
		
		for (EnumBowType type : EnumBowType.values())
		{
			for (EnumTree variant : EnumTree.values())
			{
				if (variant.hasBow())
				{
					builder.put(type, variant, new BowVariant(type, variant));
				}
			}
		}
		
		TABLE = builder.build();
	}
	
	public static BowVariant get(EnumBowType type, EnumTree variant)
	{
		return TABLE.get(type, variant);
	}
	
	public static List<BowVariant> getAll()
	{
		return ImmutableList.copyOf(TABLE.values());
	}
}
