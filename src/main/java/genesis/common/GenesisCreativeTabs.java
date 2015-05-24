package genesis.common;

import genesis.metadata.*;
import genesis.util.Constants;
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
	
	public static final CreativeTabs BLOCK = new CreativeTabs(Constants.PREFIX + "buildingBlocks")
	{
		@Override
		public Item getTabIconItem()
		{
			return Item.getItemFromBlock(GenesisBlocks.moss);
		}
	};

	public static final CreativeTabs DECORATIONS = new CreativeTabStackIcon(Constants.PREFIX + "decorations")
	{
		@Override
		public ItemStack getIconItemStack()
		{
			return GenesisBlocks.trees.getStack(TreeBlocksAndItems.SAPLING, EnumTree.SIGILLARIA);
		}
	};

	public static final CreativeTabs MISC = new CreativeTabs(Constants.PREFIX + "misc")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.ceramic_bucket_water;
		}
	};

	public static final CreativeTabs FOOD = new CreativeTabs(Constants.PREFIX + "food")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.cooked_eryops_leg;
		}
	};

	public static final CreativeTabs TOOLS = new CreativeTabStackIcon(Constants.PREFIX + "tools")
	{
		@Override
		public ItemStack getIconItemStack()
		{
			return GenesisItems.tools.getStack(ToolItems.KNIFE_HEAD, EnumToolMaterial.GRANITE, EnumToolQuality.CHIPPED);	// TODO: use chipped granite axe when added
		}
	};

	public static final CreativeTabs COMBAT = new CreativeTabStackIcon(Constants.PREFIX + "combat")
	{
		@Override
		public ItemStack getIconItemStack()
		{
			return GenesisItems.tools.getStack(ToolItems.ARROW_HEAD, EnumToolMaterial.BROWN_FLINT, EnumToolQuality.SHARPENED);
		}
	};

	public static final CreativeTabs MATERIALS = new CreativeTabs(Constants.PREFIX + "materials")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.manganese;
		}
	};
}
