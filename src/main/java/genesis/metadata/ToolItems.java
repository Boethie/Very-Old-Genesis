package genesis.metadata;

import java.util.*;

import com.google.common.base.Function;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import genesis.item.ItemToolHead;
import genesis.metadata.ToolTypes.ToolType;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.metadata.VariantsOfTypesCombo.ObjectType.ObjectNamePosition;

public class ToolItems extends VariantsOfTypesCombo
{
	public static class ToolObjectType extends ObjectType<ItemToolHead>
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
		public ToolObjectType setNamePosition(ObjectNamePosition namePosition)
		{
			super.setNamePosition(namePosition);
			
			return this;
		}
	}
	
	public static class ToolObjectTypeSoleQuality extends ToolObjectType
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
	}
	
	public static final ToolObjectType PEBBLE = new ToolObjectTypeSoleQuality("pebble", null, null, EnumToolQuality.NONE).setNamePosition(ObjectNamePosition.PREFIX);
	public static final ToolObjectType PICK_HEAD = new ToolObjectType("tool_head_pickaxe", "toolHead.pickaxe", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	public static final ToolObjectType SHOVEL_HEAD = new ToolObjectType("tool_head_shovel", "toolHead.shovel", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	public static final ToolObjectType AXE_HEAD = new ToolObjectType("tool_head_axe", "toolHead.axe", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	
	public ToolItems()
	{
		super(new ToolObjectType[]{PEBBLE, PICK_HEAD, SHOVEL_HEAD, AXE_HEAD}, ToolTypes.getAll());
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
				return getStack(type, ToolTypes.getToolHead(((ToolObjectTypeSoleQuality) toolType).getSoleQuality(), (EnumToolMaterial) variant));
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
	
	@Override
	public ItemStack getStack(ObjectType type, IMetadata variant)
	{
		return getStack(type, variant, 1);
	}
}
