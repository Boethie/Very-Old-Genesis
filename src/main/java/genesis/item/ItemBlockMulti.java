package genesis.item;

import genesis.combo.*;
import genesis.combo.variant.IMetadata;
import genesis.common.GenesisCreativeTabs;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;

public class ItemBlockMulti<V extends IMetadata<V>> extends ItemBlock
{
	public final VariantsOfTypesCombo<V> owner;
	public final ObjectType<V, ? extends Block, ? extends ItemBlockMulti<V>> type;
	
	protected final List<V> variants;
	
	public ItemBlockMulti(Block block, VariantsOfTypesCombo<V> owner,
			ObjectType<V, ? extends Block, ? extends ItemBlockMulti<V>> type,
			List<V> variants, Class<V> variantClass)
	{
		super(block);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		
		setHasSubtypes(true);
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
	{
		super.getSubItems(item, tab, subItems);
	}
	
	/*@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return getBlock().getRenderColor(owner.getBlockState(type, owner.getVariant(stack)));
	}*/
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}
	
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
