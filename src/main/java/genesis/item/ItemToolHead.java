package genesis.item;

import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemGenesis;
import genesis.metadata.BlocksAndItemsWithVariantsOfTypes;
import genesis.metadata.IMetadata;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemToolHead extends ItemGenesis
{
	protected final List<IMetadata> variants;
	public final BlocksAndItemsWithVariantsOfTypes owner;
	public ItemToolHead(List<IMetadata> variants, BlocksAndItemsWithVariantsOfTypes owner)
	{
		super();

		this.owner = owner;

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
