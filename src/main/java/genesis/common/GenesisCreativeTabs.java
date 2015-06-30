package genesis.common;

import genesis.metadata.*;
import genesis.util.Constants.Unlocalized;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class GenesisCreativeTabs
{
	private static abstract class CreativeTabStackIcon extends CreativeTabs
	{
		public CreativeTabStackIcon(String label)
		{
			super(label);
		}
		
		@Override
		public abstract ItemStack getIconItemStack();
		
		@Override
		public final Item getTabIconItem()
		{
			return null;
		}
	}
	
	public static final CreativeTabs BLOCK = new CreativeTabs(Unlocalized.PREFIX + "buildingBlocks")
	{
		@Override
		public Item getTabIconItem()
		{
			return Item.getItemFromBlock(GenesisBlocks.moss);
		}
	};

	public static final CreativeTabs DECORATIONS = new CreativeTabStackIcon(Unlocalized.PREFIX + "decorations")
	{
		@Override
		public ItemStack getIconItemStack()
		{
			return GenesisBlocks.trees.getStack(TreeBlocksAndItems.SAPLING, EnumTree.SIGILLARIA);
		}
	};

	public static final CreativeTabs MISC = new CreativeTabs(Unlocalized.PREFIX + "misc")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.ceramic_bucket_water;
		}
	};

	public static final CreativeTabs FOOD = new CreativeTabs(Unlocalized.PREFIX + "food")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.cooked_eryops_leg;
		}
	};

	public static final CreativeTabs TOOLS = new CreativeTabStackIcon(Unlocalized.PREFIX + "tools")
	{
		@Override
		public ItemStack getIconItemStack()
		{
			return GenesisItems.tools.getStack(ToolItems.AXE, EnumToolMaterial.GRANITE, EnumToolQuality.CHIPPED);
		}
	};

	public static final CreativeTabs COMBAT = new CreativeTabStackIcon(Unlocalized.PREFIX + "combat")
	{
		@Override
		public ItemStack getIconItemStack()
		{
			return GenesisItems.tools.getStack(ToolItems.ARROW_HEAD, EnumToolMaterial.BROWN_FLINT, EnumToolQuality.SHARPENED);
		}
	};

	public static final CreativeTabs MATERIALS = new CreativeTabs(Unlocalized.PREFIX + "materials")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.manganese;
		}
	};
}
