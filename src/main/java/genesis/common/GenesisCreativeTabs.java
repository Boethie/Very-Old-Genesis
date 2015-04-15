package genesis.common;

import genesis.metadata.*;
import genesis.util.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class GenesisCreativeTabs
{
	public static final CreativeTabs BLOCK = new CreativeTabs(Constants.PREFIX + "buildingBlocks")
	{
		@Override
		public Item getTabIconItem()
		{
			return Item.getItemFromBlock(GenesisBlocks.moss);
		}
	};

	public static final CreativeTabs DECORATIONS = new CreativeTabs(Constants.PREFIX + "decorations")
	{
		@Override
	    public ItemStack getIconItemStack()
	    {
			return GenesisBlocks.plants.getStack(EnumPlant.SCIADOPHYTON);	// TODO: use sigillaria sapling when added
	    }

		@Override
		public Item getTabIconItem()
		{
			return null;
		}
	};

	public static final CreativeTabs MISC = new CreativeTabs(Constants.PREFIX + "misc")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.ceramic_bucket_water;	// TODO: use komatiitic lava bucket when added
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

	public static final CreativeTabs TOOLS = new CreativeTabs(Constants.PREFIX + "tools")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.flint_and_marcasite;	// TODO: use chipped granite axe when added
		}
	};

	public static final CreativeTabs COMBAT = new CreativeTabs(Constants.PREFIX + "combat")
	{
		@Override
	    public ItemStack getIconItemStack()
	    {
			return GenesisItems.tools.getStack(GenesisItems.tools.PEBBLE, EnumToolMaterial.GRANITE);	// TODO: use chipped bone spear when added
	    }

		@Override
		public Item getTabIconItem()
		{
			return null;
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
