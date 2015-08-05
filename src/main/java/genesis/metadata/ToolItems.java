package genesis.metadata;

import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.metadata.ToolItems.*;

import java.util.*;

import com.google.common.base.Function;
import com.google.common.collect.Sets;

import genesis.block.*;
import genesis.common.GenesisCreativeTabs;
import genesis.item.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.StatCollector;
import genesis.metadata.ToolTypes.ToolType;

@SuppressWarnings("rawtypes")
public class ToolItems extends VariantsOfTypesCombo<ToolObjectType, ToolType>
{
	public static class ToolObjectType<B extends Block, I extends Item> extends ObjectType<B, I>
	{
		public final HashSet<EnumToolQuality> validQualities;
		public final HashSet<EnumToolMaterial> materialExclusions;
		
		public ToolObjectType(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass, EnumToolQuality[] qualities, EnumToolMaterial... materialExclusions)
		{
			super(name, unlocalizedName, blockClass, itemClass);
			
			this.validQualities = Sets.newHashSet(qualities);
			this.materialExclusions = Sets.newHashSet(materialExclusions);
			
			setNamePosition(ObjectNamePosition.PREFIX);
		}
		
		public ToolObjectType(String name, Class<? extends B> blockClass, Class<? extends I> itemClass, EnumToolQuality[] qualities, EnumToolMaterial... materialExclusions)
		{
			this(name, name, blockClass, itemClass, qualities, materialExclusions);
		}
		
		@Override
		public <V extends IMetadata> List<V> getValidVariants(List<V> list)
		{
			Iterator<V> iter = list.iterator();
			
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
		public ToolObjectType<B, I> setNamePosition(ObjectNamePosition namePosition)
		{
			super.setNamePosition(namePosition);
			return this;
		}
		
		@Override
		public ToolObjectType<B, I> setCreativeTab(CreativeTabs tab)
		{
			super.setCreativeTab(tab);
			return this;
		}
		
		@Override
		public ToolObjectType<B, I> setUseVariantAsRegistryName(boolean use)
		{
			super.setUseVariantAsRegistryName(use);
			return this;
		}
	}
	
	/**
	 * A {@link ToolObjectType} with only one quality level that is always used to get the {@link ToolType} for this {@link ObjectType}.
	 */
	public static class ToolObjectTypeSoleQuality<B extends Block, I extends Item> extends ToolObjectType<B, I>
	{
		protected final EnumToolQuality soleQuality;
		
		public ToolObjectTypeSoleQuality(String name, String unlocalizedName, Class<? extends B> blockClass, Class<? extends I> itemClass, EnumToolQuality quality, EnumToolMaterial... materialExclusions)
		{
			super(name, unlocalizedName, blockClass, itemClass, new EnumToolQuality[]{quality}, materialExclusions);
			
			this.soleQuality = quality;
		}
		
		public ToolObjectTypeSoleQuality(String name, Class<? extends B> blockClass, Class<? extends I> itemClass, EnumToolQuality quality, EnumToolMaterial... materialExclusions)
		{
			this(name, name, blockClass, itemClass, quality, materialExclusions);
		}
		
		public EnumToolQuality getSoleQuality()
		{
			return soleQuality;
		}
		
		@Override
		public ToolObjectTypeSoleQuality<B, I> setCreativeTab(CreativeTabs tab)
		{
			super.setCreativeTab(tab);
			return this;
		}
		
		@Override
		public ToolObjectTypeSoleQuality<B, I> setNamePosition(ObjectNamePosition namePosition)
		{
			super.setNamePosition(namePosition);
			return this;
		}
		
		@Override
		public ToolObjectTypeSoleQuality<B, I> setUseVariantAsRegistryName(boolean use)
		{
			super.setUseVariantAsRegistryName(use);
			return this;
		}
	}
	
	public static final ToolObjectTypeSoleQuality<BlockGenesisPebble, ItemPebble> PEBBLE = (ToolObjectTypeSoleQuality<BlockGenesisPebble, ItemPebble>) new ToolObjectTypeSoleQuality<BlockGenesisPebble, ItemPebble>("pebble", BlockGenesisPebble.class, ItemPebble.class, EnumToolQuality.WEAK, EnumToolMaterial.OCTAEDRITE).setNamePosition(ObjectNamePosition.PREFIX).setUseSeparateVariantJsons(false);
	public static final ToolObjectTypeSoleQuality<Block, ItemChoppingTool> CHOPPING_TOOL = new ToolObjectTypeSoleQuality<Block, ItemChoppingTool>("chopping_tool", "choppingTool", null, ItemChoppingTool.class, EnumToolQuality.WEAK, EnumToolMaterial.OCTAEDRITE).setCreativeTab(GenesisCreativeTabs.TOOLS).setNamePosition(ObjectNamePosition.PREFIX).setUseVariantAsRegistryName(true);
	public static final ToolObjectType<Block, ItemToolHead> PICK_HEAD = new ToolObjectType<Block, ItemToolHead>("head_pick", "toolHead.pick", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	public static final ToolObjectType<Block, ItemGenesisPick> PICK = new ToolObjectType<Block, ItemGenesisPick>("pick", null, ItemGenesisPick.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED}).setCreativeTab(GenesisCreativeTabs.TOOLS);
	public static final ToolObjectType<Block, ItemToolHead> AXE_HEAD = new ToolObjectType<Block, ItemToolHead>("head_axe", "toolHead.axe", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	public static final ToolObjectType<Block, ItemGenesisAxe> AXE = new ToolObjectType<Block, ItemGenesisAxe>("axe", null, ItemGenesisAxe.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED}).setCreativeTab(GenesisCreativeTabs.TOOLS);
	public static final ToolObjectType<Block, ItemToolHead> HOE_HEAD = new ToolObjectType<Block, ItemToolHead>("head_hoe", "toolHead.hoe", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED});
	public static final ToolObjectType<Block, ItemGenesisHoe> HOE = new ToolObjectType<Block, ItemGenesisHoe>("hoe", null, ItemGenesisHoe.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.POLISHED}).setCreativeTab(GenesisCreativeTabs.TOOLS);
	public static final ToolObjectType<Block, ItemToolHead> KNIFE_HEAD = new ToolObjectType<Block, ItemToolHead>("head_knife", "toolHead.knife", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED});
	public static final ToolObjectType<Block, ItemGenesisKnife> KNIFE = new ToolObjectType<Block, ItemGenesisKnife>("knife", null, ItemGenesisKnife.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED}).setCreativeTab(GenesisCreativeTabs.TOOLS);
	public static final ToolObjectType<Block, ItemToolHead> SPEAR_HEAD = new ToolObjectType<Block, ItemToolHead>("head_spear", "toolHead.spear", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED}).setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ToolObjectType<Block, ItemGenesisSpear> SPEAR = new ToolObjectType<Block, ItemGenesisSpear>("spear", null, ItemGenesisSpear.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED}).setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ToolObjectType<Block, ItemToolHead> ARROW_HEAD = new ToolObjectType<Block, ItemToolHead>("head_arrow", "toolHead.arrow", null, ItemToolHead.class, new EnumToolQuality[]{EnumToolQuality.CHIPPED, EnumToolQuality.SHARPENED}).setCreativeTab(GenesisCreativeTabs.COMBAT);
	public static final ToolObjectTypeSoleQuality<Block, ItemMulti> FLAKE = new ToolObjectTypeSoleQuality<Block, ItemMulti>("flake", null, null, EnumToolQuality.NONE);
	
	public ToolItems()
	{
		super(new ToolObjectType[]{PEBBLE, CHOPPING_TOOL, PICK_HEAD, PICK, AXE_HEAD, AXE, HOE_HEAD, HOE, KNIFE_HEAD, KNIFE, SPEAR_HEAD, SPEAR, ARROW_HEAD, FLAKE}, ToolTypes.getAll());
	}
	
	/**
	 * Get an item stack containing the tool item of the specified {@link ToolObjectType}, material and quality.
	 */
	public ItemStack getStack(ToolObjectType type, EnumToolMaterial material, EnumToolQuality quality, int stackSize)
	{
		return getStack(type, ToolTypes.getToolHead(material, quality), stackSize);
	}
	
	/**
	 * Get an item stack containing the tool item of the specified {@link ToolObjectType}, material and quality with a stack size of 1.
	 */
	public ItemStack getStack(ToolObjectType type, EnumToolMaterial material, EnumToolQuality quality)
	{
		return getStack(type, material, quality, 1);
	}

	/**
	 * Get an item stack containing the tool item with the specified {@link ToolObjectTypeSoleQuality}, and the tool material.
	 */
	public ItemStack getStack(ToolObjectTypeSoleQuality type, EnumToolMaterial material, int stackSize)
	{
		return getStack(type, material, type.getSoleQuality(), stackSize);
	}
	
	/**
	 * Get an item stack containing the tool item with the specified {@link ToolObjectTypeSoleQuality}, and the tool material, with a stack size of 1.
	 */
	public ItemStack getStack(ToolObjectTypeSoleQuality type, EnumToolMaterial material)
	{
		return getStack(type, material, 1);
	}
	
	/**
	 * Adds the information about the {@link ToolType} for this stack.
	 */
	public void addToolInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
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
	
	/**
	 * Gets a block state from the specified {@link ToolObjectType}, with the quality level and material specified.
	 */
	public IBlockState getBlockState(ToolObjectTypeSoleQuality type, EnumToolMaterial material, EnumToolQuality quality)
	{
		return getBlockState(type, ToolTypes.getToolHead(material, quality));
	}
	
	/**
	 * Gets a block state from the specified {@link ToolObjectTypeSoleQuality}, with the material specified.
	 */
	public IBlockState getBlockState(ToolObjectTypeSoleQuality type, EnumToolMaterial material)
	{
		return getBlockState(type, material, type.getSoleQuality());
	}
}
