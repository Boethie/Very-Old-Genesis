package genesis.item;

import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.IMetadata;

import java.util.Comparator;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMulti extends ItemGenesis
{
	public final VariantsOfTypesCombo owner;
	
	protected final List<IMetadata> variants;
	
	public ItemMulti(List<IMetadata> variants, VariantsOfTypesCombo owner)
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
