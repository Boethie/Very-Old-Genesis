package genesis.item;

import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.IMetadata;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants;

import java.util.Comparator;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMulti extends ItemGenesis
{
	public final VariantsOfTypesCombo owner;
	
	protected final List<IMetadata> variants;
	protected final ObjectType type;
	
	public ItemMulti(List<IMetadata> variants, VariantsOfTypesCombo owner, ObjectType type)
	{
		super();
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		owner.fillSubItems(type, variants, subItems);
	}
}
