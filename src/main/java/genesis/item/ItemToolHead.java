package genesis.item;

import genesis.combo.*;
import genesis.combo.ToolItems.*;
import genesis.combo.variant.ToolTypes.ToolType;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemGenesis;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemToolHead extends ItemGenesis
{
	protected final List<ToolType> variants;
	public final ToolItems owner;
	public final ToolObjectType<Block, ItemToolHead> type;
	
	public ItemToolHead(ToolItems owner, ToolObjectType<Block, ItemToolHead> type, List<ToolType> variants, Class<ToolType> variantClass)
	{
		super();
		
		this.owner = owner;
		this.type = type;
		this.variants = variants;
		
		setHasSubtypes(true);
		setCreativeTab(GenesisCreativeTabs.TOOLS);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
	{
		owner.fillSubItems(type, variants, subItems);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, player, tooltip, advanced);
		owner.addToolInformation(stack, player, tooltip, advanced);
	}
}
