package genesis.item;

import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.IMetadata;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMulti<V extends IMetadata> extends ItemGenesis
{
	public final VariantsOfTypesCombo<V> owner;
	
	protected final List<V> variants;
	protected final ObjectType<? extends Block, ? extends ItemMulti<V>> type;
	
	public ItemMulti(List<V> variants, VariantsOfTypesCombo<V> owner, ObjectType<? extends Block, ? extends ItemMulti<V>> type)
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
