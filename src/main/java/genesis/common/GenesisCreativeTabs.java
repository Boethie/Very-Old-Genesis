package genesis.common;

import genesis.metadata.*;
import genesis.util.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class GenesisCreativeTabs extends CreativeTabs
{
	public GenesisCreativeTabs(String label) {
		super(Constants.PREFIX + label);
	}

	private static abstract class CreativeTabStackIcon extends GenesisCreativeTabs
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
			return getIconItemStack().getItem();
		}
		
		@Override
		public int getIconItemDamage()
	    {
	        return getIconItemStack().getItemDamage();
	    }
	}
	
	public static final GenesisCreativeTabs BLOCK = new GenesisCreativeTabs("buildingBlocks")
	{
		@Override
		public Item getTabIconItem()
		{
			return Item.getItemFromBlock(GenesisBlocks.moss);
		}
	};

	public static final CreativeTabs DECORATIONS = new CreativeTabStackIcon("decorations")
	{
		@Override
		public ItemStack getIconItemStack()
		{
			return GenesisBlocks.trees.getStack(TreeBlocksAndItems.SAPLING, EnumTree.SIGILLARIA);
		}
	};

	public static final GenesisCreativeTabs MISC = new GenesisCreativeTabs("misc")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.ceramic_bucket_water;
		}
	};

	public static final GenesisCreativeTabs FOOD = new GenesisCreativeTabs("food")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.cooked_eryops_leg;
		}
	};

	public static final CreativeTabs TOOLS = new CreativeTabStackIcon("tools")
	{
		@Override
		public ItemStack getIconItemStack()
		{
			return GenesisItems.tools.getStack(ToolItems.KNIFE_HEAD, EnumToolMaterial.GRANITE, EnumToolQuality.CHIPPED);	// TODO: use chipped granite axe when added
		}
	};

	public static final CreativeTabs COMBAT = new CreativeTabStackIcon("combat")
	{
		@Override
		public ItemStack getIconItemStack()
		{
			return GenesisItems.tools.getStack(ToolItems.ARROW_HEAD, EnumToolMaterial.BROWN_FLINT, EnumToolQuality.SHARPENED);
		}
	};

	public static final GenesisCreativeTabs MATERIALS = new GenesisCreativeTabs("materials")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.manganese;
		}
	};
}
