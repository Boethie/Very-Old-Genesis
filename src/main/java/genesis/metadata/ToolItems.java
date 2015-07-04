package genesis.metadata;

import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.metadata.ToolItems.*;

import java.util.*;

import com.google.common.base.Function;
import com.google.common.collect.Sets;

import genesis.common.GenesisCreativeTabs;
import genesis.item.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.StatCollector;
import genesis.metadata.ToolTypes.ToolType;

public class ToolItems extends VariantsOfTypesCombo<ToolObjectType, ToolType>
{
	public static class ToolObjectType<I extends Item> extends ObjectType<Block, I>
	{
		public final HashSet<EnumToolQuality> validQualities;
		public final HashSet<EnumToolMaterial> materialExclusions;
		
		public ToolObjectType(String name, String unlocalizedName, Class<? extends I> itemClass, EnumToolQuality[] qualities, EnumToolMaterial... materialExclusions)
		{
			super(name, unlocalizedName, null, itemClass);

			this.validQualities = Sets.newHashSet(qualities);
			this.materialExclusions = Sets.newHashSet(materialExclusions);
			
			setNamePosition(ObjectNamePosition.PREFIX);
		}

		public ToolObjectType(String name, Class<? extends I> itemClass, EnumToolQuality[] qualities, EnumToolMaterial... materialExclusions)
		{
			this(name, name, itemClass, qualities, materialExclusions);
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
		public ToolObjectType<I> setNamePosition(ObjectNamePosition namePosition)
		{
			super.setNamePosition(namePosition);
			return this;
		}
		
		@Override
		public ToolObjectType<I> setCreativeTab(CreativeTabs tab)
		{
			super.setCreativeTab(tab);
			return this;
		}
		
		@Override
		public ToolObjectType<I> setUseVariantAsRegistryName(boolean use)
		{
			super.setUseVariantAsRegistryName(use);
			return this;
		}
	}
	
	public static class ToolObjectTypeSoleQuality<I extends Item> extends ToolObjectType<I>
	{
		protected final EnumToolQuality soleQuality;
		
		public ToolObjectTypeSoleQuality(String name, String unlocalizedName, Class<? extends I> itemClass, EnumToolQuality quality, EnumToolMaterial... materialExclusions)
		{
			super(name, unlocalizedName, itemClass, new EnumToolQuality[]{quality}, materialExclusions);
			
			this.soleQuality = quality;
		}
		
		public ToolObjectTypeSoleQuality(String name, Class<? extends I> itemClass, EnumToolQuality quality, EnumToolMaterial... materialExclusions)
		{
			this(name, name, itemClass, quality, materialExclusions);
		}
		
		public EnumToolQuality getSoleQuality()
		{
			return soleQuality;
		}
		
		@Override
		public ToolObjectTypeSoleQuality<I> setCreativeTab(CreativeTabs tab)
		{
			super.setCreativeTab(tab);
			return this;
		}
		
		@Override
		public ToolObjectTypeSoleQuality<I> setNamePosition(ObjectNamePosition namePosition)
		{
			super.setNamePosition(namePosition);
			return this;
		}
		
		@Override
		public ToolObjectTypeSoleQuality<I> setUseVariantAsRegistryName(boolean use)
		{
			super.setUseVariantAsRegistryName(use);
			return this;
		}
	}

	public static final ToolObjectTypeSoleQuality<ItemPebble> PEBBLE = new ToolObjectTypeSoleQuality<ItemPebble>("pebble", ItemPebble.class, EnumToolQuality.WEAK, EnumToolMaterial.OCTAEDRITE).setNamePosition(ObjectNamePosition.PREFIX);
	public static final ToolObjectTypeSoleQuality<ItemChoppingTool> CHOPPING_TOOL = new ToolObjectTypeSoleQuality<ItemChoppingTool>("chopping_tool", "choppingTool", ItemChoppingTool.class, EnumToolQuality.WEAK, EnumToolMaterial.OCTAEDRITE).setCreativeTab(GenesisCreativeTabs.TOOLS).setNamePosition(ObjectNamePosition.PREFIX).setUseVariantAsRegistryName(true);
	public static final ToolObjectType<ItemToolHead> PICK_HEAD = new ToolObjectType<ItemToolHead>("head_pick", "toolHead.pick", ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	public static final ToolObjectType<ItemGenesisPick> PICK = new ToolObjectType<ItemGenesisPick>("pick", ItemGenesisPick.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED}).setCreativeTab(GenesisCreativeTabs.TOOLS);
	public static final ToolObjectType<ItemToolHead> AXE_HEAD = new ToolObjectType<ItemToolHead>("head_axe", "toolHead.axe", ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	public static final ToolObjectType<ItemGenesisAxe> AXE = new ToolObjectType<ItemGenesisAxe>("axe", ItemGenesisAxe.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED}).setCreativeTab(GenesisCreativeTabs.TOOLS);
	public static final ToolObjectType<ItemToolHead> HOE_HEAD = new ToolObjectType<ItemToolHead>("head_hoe", "toolHead.hoe", ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	public static final ToolObjectType<ItemGenesisHoe> HOE = new ToolObjectType<ItemGenesisHoe>("hoe", ItemGenesisHoe.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED}).setCreativeTab(GenesisCreativeTabs.TOOLS);
	public static final ToolObjectType<ItemToolHead> KNIFE_HEAD = new ToolObjectType<ItemToolHead>("head_knife", "toolHead.knife", ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED});
	public static final ToolObjectType<ItemGenesisKnife> KNIFE = new ToolObjectType<ItemGenesisKnife>("knife", ItemGenesisKnife.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED}).setCreativeTab(GenesisCreativeTabs.TOOLS);
	public static final ToolObjectType<ItemToolHead> SPEAR_HEAD = new ToolObjectType<ItemToolHead>("head_spear", "toolHead.spear", ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED}).setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ToolObjectType<ItemGenesisSpear> SPEAR = new ToolObjectType<ItemGenesisSpear>("spear", ItemGenesisSpear.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED}).setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ToolObjectType<ItemToolHead> ARROW_HEAD = new ToolObjectType<ItemToolHead>("head_arrow", "toolHead.arrow", ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED}).setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ToolObjectTypeSoleQuality<ItemMulti> FLAKE = new ToolObjectTypeSoleQuality<ItemMulti>("flake", null, EnumToolQuality.NONE);
	
	public ToolItems()
	{
		super(new ToolObjectType[]{PEBBLE, CHOPPING_TOOL, PICK_HEAD, PICK, AXE_HEAD, AXE, HOE_HEAD, HOE, KNIFE_HEAD, KNIFE, SPEAR_HEAD, SPEAR, ARROW_HEAD, FLAKE}, ToolTypes.getAll());
	}
	
	public ItemStack getStack(ToolObjectTypeSoleQuality type, EnumToolMaterial material, int stackSize)
	{
		EnumToolQuality quality = type.getSoleQuality();
		return getStack(type, ToolTypes.getToolHead(material, quality));
	}
	
	/**
	 * Get an item stack containing the tool item with the specified tool type (with only one quality level), and the tool material.
	 */
	public ItemStack getStack(ToolObjectTypeSoleQuality type, EnumToolMaterial material)
	{
		return getStack(type, material, 1);
	}
	
	/**
	 * Get an item stack containing the tool item of the specified tool type, material and quality.
	 */
	public ItemStack getStack(ToolObjectType type, EnumToolMaterial material, EnumToolQuality quality, int stackSize)
	{
		return getStack(type, ToolTypes.getToolHead(material, quality), stackSize);
	}
	
	/**
	 * Get an item stack containing the tool item of the specified tool type, material and quality.
	 */
	public ItemStack getStack(ToolObjectType type, EnumToolMaterial material, EnumToolQuality quality)
	{
		return getStack(type, material, quality, 1);
	}
	
	public void addToolInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		IMetadata variant = getVariant(stack.getItem(), stack.getMetadata());
		
		if (variant instanceof ToolType)
		{
			ToolType type = (ToolType) variant;
			
			if (type.quality.hasUnlocalizedName())
			{
				tooltip.add(StatCollector.translateToLocal(type.quality.getUnlocalizedName()));
			}
		}
	}
}
