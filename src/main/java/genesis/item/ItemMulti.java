package genesis.item;

import genesis.metadata.BlocksAndItemsWithVariantsOfTypes;
import genesis.metadata.IMetadata;
import genesis.util.Metadata;

import java.util.Comparator;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMulti extends ItemGenesis
{
	public final BlocksAndItemsWithVariantsOfTypes owner;
	
	protected final List<IMetadata> variants;
	
	public ItemMulti(List<IMetadata> variants, BlocksAndItemsWithVariantsOfTypes owner)
	{
		super();
		
		this.owner = owner;
		
		this.variants = variants;
		
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + variants.get(stack.getMetadata()).getUnlocalizedName();
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		owner.fillSubItems(this, variants, subItems);
	}
}
