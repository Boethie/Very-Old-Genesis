package genesis.metadata;

import java.util.*;

import com.google.common.base.Function;
import com.google.common.collect.Sets;

import genesis.common.GenesisCreativeTabs;
import genesis.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import genesis.metadata.ToolTypes.ToolType;
import genesis.metadata.VariantsOfTypesCombo.ObjectType.ObjectNamePosition;

public class ToolItems extends VariantsOfTypesCombo
{
	public static class ToolObjectType<T extends Item> extends ObjectType<T>
	{
		public final HashSet<EnumToolQuality> validQualities;
		public final HashSet<EnumToolMaterial> materialExclusions;
		
		public ToolObjectType(String name, String unlocalizedName, Class blockClass, Class itemClass, EnumToolQuality[] qualities, EnumToolMaterial... materialExclusions)
		{
			super(name, unlocalizedName, blockClass, itemClass);

			this.validQualities = Sets.newHashSet(qualities);
			this.materialExclusions = Sets.newHashSet(materialExclusions);
			
			setNamePosition(ObjectNamePosition.PREFIX);
		}

		public ToolObjectType(String name, Class blockClass, Class itemClass, EnumToolQuality[] qualities, EnumToolMaterial... materialExclusions)
		{
			this(name, name, blockClass, itemClass, qualities, materialExclusions);
		}
		
		@Override
		public List<IMetadata> getValidVariants(List<IMetadata> list)
		{
			Iterator<IMetadata> iter = list.iterator();
			
			while (iter.hasNext())
			{
				ToolType type = (ToolType) iter.next();
				
				if (!validQualities.contains(type.quality) ||
						materialExclusions.contains(type.material))
				{
					iter.remove();
				}
			}
			
			return list;
		}
		
		@Override
		public ToolObjectType<T> setNamePosition(ObjectNamePosition namePosition)
		{
			super.setNamePosition(namePosition);
			return this;
		}
		@Override
		public ToolObjectType<T> setCreativeTab(CreativeTabs tab)
		{
			this.tab = tab;

			return this;
		}
	}
	
	public static class ToolObjectTypeSoleQuality<T extends Item> extends ToolObjectType<T>
	{
		protected final EnumToolQuality soleQuality;
		
		public ToolObjectTypeSoleQuality(String name, String unlocalizedName, Class blockClass, Class itemClass, EnumToolQuality quality, EnumToolMaterial... materialExclusions)
		{
			super(name, unlocalizedName, blockClass, itemClass, new EnumToolQuality[]{quality}, materialExclusions);
			
			this.soleQuality = quality;
		}
		
		public ToolObjectTypeSoleQuality(String name, Class blockClass, Class itemClass, EnumToolQuality quality, EnumToolMaterial... materialExclusions)
		{
			this(name, name, blockClass, itemClass, quality, materialExclusions);
		}
		
		public EnumToolQuality getSoleQuality()
		{
			return soleQuality;
		}
		
		@Override
		public ToolObjectTypeSoleQuality<T> setNamePosition(ObjectNamePosition namePosition)
		{
			super.setNamePosition(namePosition);
			return this;
		}
	}

	public static final ToolObjectTypeSoleQuality<ItemMulti> PEBBLE = new ToolObjectTypeSoleQuality("pebble", null, null, EnumToolQuality.NONE).setNamePosition(ObjectNamePosition.PREFIX);
	public static final ToolObjectType<ItemToolHead> PICK_HEAD = new ToolObjectType("tool_head_pick", "toolHead.pick", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	public static final ToolObjectType<ItemToolHead> AXE_HEAD = new ToolObjectType("tool_head_axe", "toolHead.axe", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	public static final ToolObjectType<ItemToolHead> KNIFE_HEAD = new ToolObjectType("tool_head_knife", "toolHead.knife", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED});
	public static final ToolObjectType<ItemToolHead> SPEAR_HEAD = new ToolObjectType("tool_head_spear", "toolHead.spear", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED}).setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ToolObjectType<ItemToolHead> ARROW_HEAD = new ToolObjectType("tool_head_arrow", "toolHead.arrow", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED}).setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ToolObjectTypeSoleQuality<ItemMulti> POINT_FLAKE = new ToolObjectTypeSoleQuality("point_flake", "pointFlake", null, null, EnumToolQuality.NONE);
	
	public ToolItems()
	{
		super(new ToolObjectType[]{PEBBLE, PICK_HEAD, AXE_HEAD, KNIFE_HEAD, SPEAR_HEAD, ARROW_HEAD, POINT_FLAKE}, ToolTypes.getAll());
	}
	
	@Override
	public ItemStack getStack(ObjectType type, IMetadata variant, int stackSize)
	{
		ToolObjectType toolType = ((ToolObjectType) type);
		
		if (variant instanceof ToolType)
		{
			return super.getStack(type, variant, stackSize);
		}
		
		if (variant instanceof EnumToolMaterial)
		{
			if (toolType instanceof ToolObjectTypeSoleQuality)
			{
				EnumToolMaterial material = (EnumToolMaterial) variant;
				EnumToolQuality quality = ((ToolObjectTypeSoleQuality) toolType).getSoleQuality();
				return getStack(type, ToolTypes.getToolHead(material, quality));
			}
			else
			{
				throw new RuntimeException("Attempted to get a stack using an EnumToolMaterial on a non-ToolObjectTypeSoleQuality.\n" +
						getIdentification());
			}
		}

		throw new RuntimeException("Invalid variant.\n" +
				getIdentification());
	}
	
	public ItemStack getStack(ObjectType type, EnumToolMaterial material, EnumToolQuality quality, int stackSize)
	{
		return getStack(type, ToolTypes.getToolHead(material, quality), stackSize);
	}
	
	public ItemStack getStack(ObjectType type, EnumToolMaterial material, EnumToolQuality quality)
	{
		return getStack(type, material, quality, 1);
	}
	
	@Override
	public ItemStack getStack(ObjectType type, IMetadata variant)
	{
		return getStack(type, variant, 1);
	}
}
