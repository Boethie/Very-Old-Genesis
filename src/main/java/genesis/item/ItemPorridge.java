package genesis.item;

import java.util.List;

import genesis.common.GenesisItems;
import genesis.metadata.*;
import genesis.metadata.ItemsCeramicBowls.*;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPorridge extends ItemGenesisFood<IFoodMetadata>
{
	public ItemPorridge(List<IFoodMetadata> variants, VariantsOfTypesCombo<IFoodMetadata> owner, ObjectType<Block, ? extends ItemPorridge> type)
	{
		super(variants, owner, type);
		
		setHasSubtypes(true);
	}
	
	@Override
	public int getHealAmount(ItemStack stack)
	{
		return owner.getVariant(stack).getFoodAmount();
	}
	
	@Override
	public float getSaturationModifier(ItemStack stack)
	{
		return owner.getVariant(stack).getSaturationModifier();
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
