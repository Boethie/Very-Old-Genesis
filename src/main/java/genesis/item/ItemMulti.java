package genesis.item;

import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.IMetadata;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;

import java.util.Comparator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@SuppressWarnings("rawtypes")
public class ItemMulti extends ItemGenesis
{
	public final VariantsOfTypesCombo<ObjectType, IMetadata> owner;
	
	protected final List<IMetadata> variants;
	protected final ObjectType<? extends Block, ? extends ItemMulti> type;
	
	public ItemMulti(List<IMetadata> variants, VariantsOfTypesCombo<ObjectType, IMetadata> owner, ObjectType<? extends Block, ? extends ItemMulti> type)
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
		owner.fillSubItems(type, variants, (List<ItemStack>) subItems);
	}
}
