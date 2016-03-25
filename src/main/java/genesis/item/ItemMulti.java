package genesis.item;

import java.util.List;

import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.variant.IMetadata;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMulti<V extends IMetadata<V>> extends ItemGenesis
{
	public final VariantsOfTypesCombo<V> owner;
	
	protected final List<V> variants;
	protected final ObjectType<? extends Block, ? extends ItemMulti<V>> type;
	
	public ItemMulti(VariantsOfTypesCombo<V> owner, ObjectType<? extends Block, ? extends ItemMulti<V>> type, List<V> variants, Class<V> variantClass)
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
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		owner.fillSubItems(type, variants, subItems);
	}
}
