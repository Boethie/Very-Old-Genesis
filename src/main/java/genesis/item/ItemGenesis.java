package genesis.item;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemGenesis extends Item
{

	public ItemGenesis()
	{
		setCreativeTab(GenesisCreativeTabs.MATERIALS);
	}

	@Override
	public ItemGenesis setUnlocalizedName(String unlocalizedName)
	{
		super.setUnlocalizedName(Constants.PREFIX + "material." + unlocalizedName);
		
		return this;
	}
}
