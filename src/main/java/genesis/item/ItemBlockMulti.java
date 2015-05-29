package genesis.item;

import genesis.common.GenesisCreativeTabs;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.Constants;

import java.util.*;

import com.google.common.base.Function;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;

public class ItemBlockMulti extends ItemBlock
{
	public final VariantsOfTypesCombo owner;
	public final ObjectType type;
	
	protected final List<IMetadata> variants;
	
	public ItemBlockMulti(Block block, final List<IMetadata> variants, final VariantsOfTypesCombo owner, ObjectType type)
	{
		super(block);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		super.getSubItems(itemIn, tab, subItems);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return getBlock().getBlockColor();
	}
	
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
