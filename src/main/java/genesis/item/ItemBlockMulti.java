package genesis.item;

import genesis.common.GenesisCreativeTabs;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.IMetadata;
import genesis.util.Constants;

import java.util.Comparator;
import java.util.List;

import com.google.common.base.Function;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemMultiTexture
{
	public final VariantsOfTypesCombo owner;
	
	protected final List<IMetadata> variants;
	
	public ItemBlockMulti(Block block, final List<IMetadata> variants, VariantsOfTypesCombo owner)
	{
		super(block, block, new Function()
		{
			@Override
			public Object apply(Object input)
			{
				int metadata = ((ItemStack) input).getMetadata();
				return variants.get(metadata).getUnlocalizedName();
			}
		});
		
		this.owner = owner;
		
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
}
