package genesis.item;

import java.util.List;

import genesis.common.GenesisItems;
import genesis.metadata.EnumPorridge;
import genesis.metadata.IMetadata;
import genesis.metadata.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ItemPorridge extends ItemGenesisFood
{
	public final VariantsOfTypesCombo<ObjectType, IMetadata> owner;
	
	protected final List<IMetadata> variants;
	protected final ObjectType<? extends Block, ? extends ItemMulti> type;
	
	public ItemPorridge(List<IMetadata> variants, VariantsOfTypesCombo<ObjectType, IMetadata> owner, ObjectType<? extends Block, ? extends ItemMulti> type)
	{
		super(0, 0);
		
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
	
	@Override
	public int getHealAmount(ItemStack stack)
	{
		return ((EnumPorridge) owner.getVariant(stack)).getFoodAmount();
	}
	
	@Override
	public float getSaturationModifier(ItemStack stack)
	{
		return ((EnumPorridge) owner.getVariant(stack)).getSaturationModifier();
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player)
	{
		ItemStack out = super.onItemUseFinish(stack, world, player);
		
		ItemStack bowl = GenesisItems.bowls.getStack(EnumCeramicBowls.BOWL);
		
		if (stack.stackSize == 0)
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); i++)
			{
				if (player.inventory.getStackInSlot(i) == stack)
				{
					return bowl;
				}
			}
		}
		else if (!player.inventory.addItemStackToInventory(bowl))
		{
			player.dropItem(bowl, false, true);
		}
		
		return out;
	}
}
