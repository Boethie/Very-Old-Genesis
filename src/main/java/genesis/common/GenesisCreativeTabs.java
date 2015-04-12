package genesis.common;

import genesis.metadata.EnumPlant;
import genesis.util.Constants;
import genesis.util.Metadata;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

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
		public Item getTabIconItem()
		{
			return Item.getItemFromBlock(GenesisBlocks.plant);// sigillaria sapling
		}

		@Override
		public int getIconItemDamage()
		{
			return Metadata.getMetadata(EnumPlant.BARAGWANATHIA);
		}
	};

	public static final CreativeTabs MISC = new CreativeTabs(Constants.PREFIX + "misc")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.ceramic_bucket;
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
			return GenesisItems.flint_and_marcasite;// chipped granite axe
		}
	};

	public static final CreativeTabs COMBAT = new CreativeTabs(Constants.PREFIX + "combat")
	{
		@Override
		public Item getTabIconItem()
		{
			return GenesisItems.pebble;// chipped bone spear
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
