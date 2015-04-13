package genesis.item;

import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemGenesis;
import genesis.metadata.IMetadata;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemToolHead extends ItemGenesis
{
	protected final List<IMetadata> variants;

	public ItemToolHead(List<IMetadata> variants)
	{
		super();

		this.variants = variants;

		setHasSubtypes(true);
		setCreativeTab(GenesisCreativeTabs.TOOLS);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + variants.get(stack.getMetadata()).getUnlocalizedName();
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		for (int i = 0; i < variants.size(); i++)
		{
			ItemStack stack = new ItemStack(itemIn, 1, i);
			subItems.add(stack);
		}
	}
}
